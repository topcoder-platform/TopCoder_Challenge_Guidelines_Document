package core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.Log;
import util.XL_ReadWrite;
import util.Log.Priority;
import wrappers.Common;

public class GetXLData implements IGetData{
	private Log log = new Log(GetXLData.class);
	
	

	/*
	 * Generating test data map by reading data from excel
	*/
	@Override
	public LinkedHashMap<Integer, LinkedHashMap<String, String>> getTestDataMap(String tcClassPkgNm, String tcName) {
		//Common.iterationNumber = 0;
		log.Report(Priority.INFO, Globals.GC_LOG_INITTC_MSG + tcClassPkgNm + "."+ tcName + Globals.GC_LOG_INITTC_MSG);
		
		// Getting Application name and Module name so that the correct excel is picked up
		LinkedHashMap<Integer, LinkedHashMap<String, String>> td = null;
		LinkedHashMap<String, String> mapData = null;
		String appName =  Common.getGlobalParam("AUT"); 
		String modName = tcClassPkgNm.split("\\.")[(tcClassPkgNm.split("\\.").length) - 1];

		boolean ifTCFound = false;
		log.Report(Priority.DEBUG, "Preparing test data for Test Case : " + tcName);

		try {
			XL_ReadWrite XL ;
			
			// Setting up common test data location
			if (System.getProperties().containsKey("testDataPath") 
				&& System.getProperty("testDataPath").equalsIgnoreCase("true")) {
				
				XL = new XL_ReadWrite(Globals.GC_COMMON_TESTDATALOC + appName
					 + "\\\\" + Globals.GC_TESTDATAPREFIX + appName + "_"
					 + Common.getGlobalParam("TEST_ENV") + Default.getGC_XL_EXTN());
			} else {
				XL = new XL_ReadWrite(Globals.GC_TESTDATALOC
					 + Globals.GC_TESTDATAPREFIX + appName + "_"
				     + Common.getGlobalParam("TEST_ENV") + Default.getGC_XL_EXTN());
			}

			int manualTCColNo = XL.getColNum(modName, 0,Globals.GC_COL_MANUAL_TC);
			int itrColNo = XL.getColNum(modName, 0, Globals.GC_ITRCCOLNAME);
			int itcPointer = 0;

			// Finding the TC in given TestData sheet
			for (; itcPointer <= XL.getRowCount(modName); itcPointer++) {
				if (XL.getCellData(modName, itcPointer, manualTCColNo).trim().equalsIgnoreCase(tcName)) {
					log.Report(Priority.DEBUG, tcName + " found in " + modName
							   + " sheet for row : " + itcPointer);
					ifTCFound = true;
					break;
				}
			}

			if (ifTCFound) { // Loop to TD index
				String strLoopPattern = XL.getCellData(modName, itcPointer,itrColNo).trim();
				int counter = 0;
				
				// Run ALL Test Data iteration
				if (strLoopPattern.equals(Globals.GC_EMPTY) || strLoopPattern.equalsIgnoreCase(Globals.GC_VAL_RUNALLITR)) {
					for (int iLoop = itcPointer; iLoop <= XL.getRowCount(modName); iLoop++) {
						if (XL.getCellData(modName, iLoop + 2, manualTCColNo).trim().equals(tcName)) {
							counter = counter + 1;
						} else if (XL.getCellData(modName, iLoop + 2, manualTCColNo).trim().equals(Globals.GC_EMPTY)) {
							// breaking out after all row count
							break;
						}
					}
					if (counter > 1) {
						strLoopPattern = ("1>" + String.valueOf(counter));
					} else {
						strLoopPattern = "1";
					}
				}

				td = new LinkedHashMap<Integer, LinkedHashMap<String, String>>();
				Map<String, String> testDataItr = getLoopIndex(strLoopPattern);

				for (String itrNo : testDataItr.keySet()) {
					mapData = new LinkedHashMap<String, String>();
					// Loop to point to TC iterations
					for (int iSelIndx = itcPointer + 2; iSelIndx <= XL.getRowCount(modName); iSelIndx++) {
						if (itrNo.equals(XL.getCellData(modName, iSelIndx,itrColNo))) {
							// Loop through the column to generate Key,Value pair TD map
							
							for (int iColCnt = itrColNo + 1; iColCnt <= XL.getColCount(iSelIndx - 1, modName); iColCnt++) {
								if (!XL.getCellData(modName, itcPointer + 1,iColCnt).trim().equals(Globals.GC_EMPTY)) {
									mapData.put(XL.getCellData(modName,itcPointer + 1, iColCnt)	.trim().toUpperCase(),
											    XL.getCellData(modName, iSelIndx,iColCnt).trim());
							        log.Report(Priority.DEBUG,"test data mapped for index "+ itrNo
											   + " with key "+ XL.getCellData(modName,itcPointer + 1,iColCnt)
											   + " and value "+ XL.getCellData(modName,iSelIndx, iColCnt));
								}
							}
							mapData.remove("");td.put(Integer.valueOf(itrNo), mapData);	break;
						}
					}
				}
				XL.clearXL();
			} else {
				log.Report(Priority.DEBUG, tcName + " not found in test data : "+ modName + " sheet");
			}
			return td;
		} catch (Exception e) {
			log.Report(Priority.ERROR	, "Exception occurred while preparing test data from excel : "+ e.getMessage());
		}
		return null;
	}
	
	@Override
	public LinkedHashMap<Integer, List<LinkedHashMap<String, String>>> getTestDataMapExtrnlItr(String tcClassPkgNm,
			String tcName) {
		//Common.iterationNumber = 0;
				log.Report(Priority.INFO, Globals.GC_LOG_INITTC_MSG + tcClassPkgNm + "."+ tcName + Globals.GC_LOG_INITTC_MSG);
				
				// Getting Application name and Module name so that the correct excel is picked up
				LinkedHashMap<Integer, LinkedHashMap<String, String>> td = null;
				LinkedHashMap<Integer, List<LinkedHashMap<String, String>>> itrtd = null;
				LinkedHashMap<String, String> mapData = null;
				String appName =  Common.getGlobalParam("AUT"); 
				String modName = tcClassPkgNm.split("\\.")[(tcClassPkgNm.split("\\.").length) - 1];
				List<LinkedHashMap<String, String>> lstTd = null;
				String strExtItrNumber = "1";

				boolean ifTCFound = false;
				log.Report(Priority.DEBUG, "Preparing test data for Test Case : " + tcName);

				try {
					XL_ReadWrite XL ;
					
					// Setting up common test data location
					if (System.getProperties().containsKey("testDataPath") 
						&& System.getProperty("testDataPath").equalsIgnoreCase("true")) {
						
						XL = new XL_ReadWrite(Globals.GC_COMMON_TESTDATALOC + appName
							 + "\\\\" + Globals.GC_TESTDATAPREFIX + appName + "_"
							 + Common.getGlobalParam("TEST_ENV") + Default.getGC_XL_EXTN());
					} else {
						XL = new XL_ReadWrite(Globals.GC_TESTDATALOC
							 + Globals.GC_TESTDATAPREFIX + appName + "_"
						     + Common.getGlobalParam("TEST_ENV") + Default.getGC_XL_EXTN());
					}

					int manualTCColNo = XL.getColNum(modName, 0,Globals.GC_COL_MANUAL_TC);
					int itrColNo = XL.getColNum(modName, 0, Globals.GC_ITRCCOLNAME);
					int itrExtParamCol = itrColNo+1; // Passing external testng param to the test @ column next to iteration
					int itcPointer = 0;

					// Finding the TC in given TestData sheet
					for (; itcPointer <= XL.getRowCount(modName); itcPointer++) {
						if (XL.getCellData(modName, itcPointer, manualTCColNo).trim().equalsIgnoreCase(tcName)) {
							log.Report(Priority.DEBUG, tcName + " found in " + modName
									   + " sheet for row : " + itcPointer);
							ifTCFound = true;
							break;
						}
					}

					if (ifTCFound) { // Loop to TD index
						String strLoopPattern = XL.getCellData(modName, itcPointer,itrColNo).trim();
						strExtItrNumber =  XL.getCellData(modName, itcPointer,itrExtParamCol).trim();
						int counter = 0;
						
						// Run ALL Test Data iteration
						if (strLoopPattern.equals(Globals.GC_EMPTY) || strLoopPattern.equalsIgnoreCase(Globals.GC_VAL_RUNALLITR)) {
							for (int iLoop = itcPointer; iLoop <= XL.getRowCount(modName); iLoop++) {
								if (XL.getCellData(modName, iLoop + 2, manualTCColNo).trim().equals(tcName)) {
									counter = counter + 1;
								} else if (XL.getCellData(modName, iLoop + 2, manualTCColNo).trim().equals(Globals.GC_EMPTY)) {
									// breaking out after all row count
									break;
								}
							}
							if (counter > 1) {
								strLoopPattern = ("1>" + String.valueOf(counter));
							} else {
								strLoopPattern = "1";
							}
						}

						td = new LinkedHashMap<Integer, LinkedHashMap<String, String>>();
						itrtd = new LinkedHashMap<Integer, List<LinkedHashMap<String, String>>>();
						lstTd = new ArrayList<LinkedHashMap<String, String>>();
						Map<String, String> testDataItr = getLoopIndex(strLoopPattern);

						for (String itrNo : testDataItr.keySet()) {
							mapData = new LinkedHashMap<String, String>();
							// Loop to point to TC iterations
							for (int iSelIndx = itcPointer + 2; iSelIndx <= XL.getRowCount(modName); iSelIndx++) {
								if (itrNo.equals(XL.getCellData(modName, iSelIndx,itrColNo))) {
									// Loop through the column to generate Key,Value pair TD map
									
									for (int iColCnt = itrColNo + 1; iColCnt <= XL.getColCount(iSelIndx - 1, modName); iColCnt++) {
										if (!XL.getCellData(modName, itcPointer + 1,iColCnt).trim().equals(Globals.GC_EMPTY)) {
											mapData.put(XL.getCellData(modName,itcPointer + 1, iColCnt)	.trim().toUpperCase(),
													    XL.getCellData(modName, iSelIndx,iColCnt).trim());
									        log.Report(Priority.DEBUG,"test data mapped for index "+ itrNo
													   + " with key "+ XL.getCellData(modName,itcPointer + 1,iColCnt)
													   + " and value "+ XL.getCellData(modName,iSelIndx, iColCnt));
										}
									}
									mapData.remove("");td.put(Integer.valueOf(itrNo), mapData);	break;
								}
							}
						}
						XL.clearXL();
					} else {
						log.Report(Priority.DEBUG, tcName + " not found in test data : "+ modName + " sheet");
					}
					
					// Adding all iterations to the list
					for(Map.Entry<Integer, LinkedHashMap<String,String>> itrMap : td.entrySet()){
						lstTd.add(itrMap.getValue());
					}
					
					// Adding All iteration list to external itr map
					for(int iLoop=1;iLoop<=Integer.valueOf(strExtItrNumber);iLoop++){
						itrtd.put(iLoop, lstTd);
					}
					
					return itrtd;
				} catch (Exception e) {
					log.Report(Priority.ERROR	, "Exception occurred while preparing test data from excel : "+ e.getMessage());
				}
				return null;

	}
	
	
	/* -------------------------------------------------------------------------
	 * FUNCTION : getLoopIndex(String indexString)
	 * DESCRIPTION : Get final excel test data index 
	 * PARAMETERS : String indexString
	 * RETURNS : NA 
	 * REVISION :
	 * HISTORY:
	 * -------------------------------------------------------------------------
	 * Author : Souvik Date : 21-12-2016
	 * -------------------------------------------------------------------------
   */private Map<String, String> getLoopIndex(String indexString) {
		String GET_INDEX_PATTERN = "[0-9,>temp]+";
		String[] arrFirstSplit = null;
		Pattern pattern = null;
		Matcher matcher = null;
		Map<String, String> mapIndex = new LinkedHashMap<String, String>();
		log.Report(Priority.INFO, "computing pattern : " + indexString+ " to get test data index");
		if (!indexString.contains(",")) {
			indexString = indexString + ",temp";
		}
		try {
			pattern = Pattern.compile(GET_INDEX_PATTERN);
			matcher = pattern.matcher(indexString);
			if (!matcher.matches()) {
				log.Report(Priority.ERROR, "Pattern mismatch found for getLoopIndex() -Only "
			    + "allowed characters: '0-9,>' and no consecutive repetions of operator is allowed (>>)");
			}
			if (indexString.contains(",")) {
				arrFirstSplit = indexString.split(",");
			} else { // to do else if
				arrFirstSplit = (indexString).split(">");
			}

			if (arrFirstSplit.length > 1) {
				String[] arrayOfString1;
				int j = (arrayOfString1 = arrFirstSplit).length;
				for (int i = 0; i < j; i++) {
					String str = arrayOfString1[i];
					if (str.split(">").length - 1 != 0) {
						int varA = Integer.parseInt(str.split(">")[1]);
						int varB = Integer.parseInt(str.split(">")[0]);
						if (varA - varB > 0) {
							for (int iLoop = 0; iLoop <= varA - varB; iLoop++) {
								mapIndex.put(Integer.toString(varB + iLoop), "");
							}
						}
					} else {
						mapIndex.put(str, "");
					}
				}
			}
			mapIndex.remove("");
			mapIndex.remove("temp");
			return mapIndex;
		} catch (Exception e) {
			log.Report(Priority.ERROR, "Exception at getLoopIndex() : " + e.getMessage());
		}
		return null;
	}

}
