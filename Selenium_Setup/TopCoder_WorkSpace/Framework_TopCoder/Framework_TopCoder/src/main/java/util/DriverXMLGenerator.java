package util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import core.Default;
import core.Globals;
import util.Log.Priority;
import wrappers.Common;

public class DriverXMLGenerator {

	private XL_ReadWrite xlRW;
	private Map<String, String> runOrderDet;
	private String RunXMLFileName;
	private String testConfigPath;
	private Log log;

	public static void main(String[] args) throws Exception {		
		new DriverXMLGenerator();
	}

	public DriverXMLGenerator() throws Exception {
		log = new Log(DriverXMLGenerator.class);
		log.Report(Priority.INFO, "*************** Generating Run XML ***************");
		testConfigPath = Globals.GC_TESTCONFIGLOC + Globals.GC_CONFIGFILEANDSHEETNAME + ".properties";
		
		// Read Config
		ReadTestConfig();

		// Building TestNG XML
		BuildTestNGXML();
	}

	/*
	 * -------------------------------------------------------------------------
	 * FUNCTION: ReadTestConfig() DESCRIPTION: Reading Test Config
	 * sheet to access Driver script parameters PARAMETERS: NA RETURNS: NA
	 * REVISION HISTORY:
	 * -------------------------------------------------------------------------
	 * Author :  Date : 
	 */ private void ReadTestConfig() throws Exception {
		Common.loadConfigProperty(testConfigPath);
	}

	/*
	 * -------------------------------------------------------------------------
	 * FUNCTION: ReadRunOrder(String runOrderFilePath) DESCRIPTION:
	 * Reading Run Order sheet to get a runnable test module names required to
	 * build testNG xml PARAMETERS: String runOrderFilePath RETURNS: NA REVISION
	 * HISTORY:
	 * -------------------------------------------------------------------------
	 * Author :  Date : 
	 */ private void ReadRunOrder(String runOrderFilePath) throws Exception {
		try {
			xlRW = new XL_ReadWrite(runOrderFilePath);
			runOrderDet = new LinkedHashMap<String, String>();
			String key = Globals.GC_EMPTY;
			String val = Globals.GC_EMPTY;
			log.Report(Priority.INFO, "reading " + runOrderFilePath + " file to build TestNG XML");
			for (int iROLoop = 0; iROLoop < xlRW.getRowCount((Globals.GC_TESTCASERUNORDERPREFIX).split("_")[0])
					- 1; iROLoop++) {
				key = xlRW.getCellData((Globals.GC_TESTCASERUNORDERPREFIX).split("_")[0], iROLoop + 1,
						Globals.GC_COLNAME_MODULENAME).trim();
				val = xlRW.getCellData((Globals.GC_TESTCASERUNORDERPREFIX).split("_")[0], iROLoop + 1,
						Globals.GC_COLNAME_RUNSTATUS).trim();
				runOrderDet.put(key, val);
				log.Report(Priority.INFO, "setting @runOrderDet with key :" + key + " -- value :" + val);
			}
			runOrderDet.remove(Globals.GC_EMPTY);
			xlRW.clearXL();
		} catch (Exception e) {
			log.Report(Priority.ERROR, e.getMessage());
		}
	}

	/*
	 * -------------------------------------------------------------------------
	 * FUNCTION: BuildTestNGXML() DESCRIPTION: Build testNG xml and
	 * saves the same to RunXML directory PARAMETERS: NA RETURNS: NA REVISION
	 * HISTORY:
	 * -------------------------------------------------------------------------
	 * Author :  Date : 
	 */ private void BuildTestNGXML() throws Exception {
		String autName = Globals.GC_EMPTY; String runOrderPath = Globals.GC_EMPTY;
		String testType = Globals.GC_EMPTY; String manualTCName = Globals.GC_EMPTY;
		String classPath = Globals.GC_EMPTY;
		try {
			autName = Common.getGlobalParam(Globals.GC_KEYAUT).toLowerCase();
			if (autName.isEmpty())
			{throw new Exception("Application name is not provided in TestExecutionConfig sheet");}
			
			runOrderPath = Globals.GC_TESTCASESLOC + Globals.GC_TESTCASERUNORDERPREFIX + autName + Default.getGC_XL_EXTN();
			log.Report(Priority.INFO, "building TestNG XML to execute automation tests for application : " + autName);

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();Document doc = docBuilder.newDocument();

			// Initializing TestNG XML nodes
			Element suite = doc.createElement(Globals.GC_XML_SUITE); 
			log.Report(Priority.INFO, "Initialize SUITE node to build TestNG XML ");
			Element listeners = doc.createElement(Globals.GC_XML_LISTENERS);Element listener = doc.createElement(Globals.GC_XML_LISTENER);
			log.Report(Priority.INFO, "Initialize LISTENER node to build TestNG XML ");
			
			doc.appendChild(suite);	suite.setAttribute(Globals.GC_XML_ATTR_NAME, Globals.GC_XML_ATTR_VAL_SUITE);
			log.Report(Priority.INFO, "Setting attribute to SUITE node (" + Globals.GC_XML_ATTR_NAME + "--"+ Globals.GC_XML_ATTR_VAL_SUITE + ") to build TestNG XML");
			suite.setAttribute(Globals.GC_XML_ATTR_VERBOSE, "1");suite.appendChild(listeners);	listeners.appendChild(listener);
			listener.setAttribute(Globals.GC_XML_ATTR_CLASSNAME, Globals.GC_LISTENERS_CLASSNAME);

			// Read Run Order
			ReadRunOrder(runOrderPath);

			// Building testNG XML
			xlRW = new XL_ReadWrite(runOrderPath);
			for (Map.Entry<String, String> moduleName : runOrderDet.entrySet()) {
				if (moduleName.getValue().equalsIgnoreCase(Globals.GC_RUNSTATUS_YES)) {
					if (xlRW.isSheetExist(moduleName.getKey())) { // check if the sheet exist

						// Looping through Test
						for (int iModuleLoop = 0; iModuleLoop < xlRW.getRowCount(moduleName.getKey())- 1; iModuleLoop++) {
							 manualTCName = xlRW.getCellData(moduleName.getKey(), iModuleLoop + 1,Globals.GC_XML_ATTR_VAL_TEST_COL_NM);
							// Creating Methods Tags
							Element methods = doc.createElement(Globals.GC_XML_METHODS);

					        // Not including Run Status NO scenarios as exclude tag in not allowing to update
						    // Globals.GC_MANUAL_TC_NAME for following include tags, this leads to
						    // incorrect data setup for the TC
							if (!manualTCName.equals(Globals.GC_EMPTY) & !xlRW
									.getCellData(moduleName.getKey(), iModuleLoop + 1, Globals.GC_COLNAME_RUNSTATUS)
									.equalsIgnoreCase(Globals.GC_RUNSTATUS_NO)) {

								// Creating Test Tags
								Element test = doc.createElement(Globals.GC_XML_TEST);
								log.Report(Priority.INFO,
										"Initialize TEST node to build TestNG XML for TC :" + manualTCName);

								// Creating Classes Tags
								Element classes = doc.createElement(Globals.GC_XML_CLASSES);
								suite.appendChild(test);
								test.setAttribute(Globals.GC_XML_ATTR_NAME, manualTCName);
								test.appendChild(classes);

								// Creating class tags
								Element clazz = doc.createElement(Globals.GC_XML_CLASS);
								classes.appendChild(clazz);
								classPath = xlRW
										.getCellData(moduleName.getKey(), iModuleLoop + 1, Globals.GC_COLNAME_CLASSPATH)
										.toLowerCase();
								clazz.setAttribute(Globals.GC_XML_ATTR_NAME, classPath);

								// Appending Methods as Child for Class
								clazz.appendChild(methods);
							} else {
								log.Report(Priority.INFO,
										"Manual Test case name not provided for Test Case :" + xlRW.getCellData(
												moduleName.getKey(), iModuleLoop + 1, Globals.GC_COLNAME_TESTCASES));
							}

							// Creating Including/Excluding Tags
							if (xlRW.getCellData(moduleName.getKey(), iModuleLoop + 1, Globals.GC_COLNAME_RUNSTATUS)
									.equalsIgnoreCase(Globals.GC_RUNSTATUS_YES)) {
								Element include = doc.createElement(Globals.GC_XML_INCLUDE);
								log.Report(Priority.INFO, "initialize INCLUDE node to build TestNG XML ");
								methods.appendChild(include);
								include.setAttribute(Globals.GC_XML_ATTR_NAME, xlRW.getCellData(moduleName.getKey(),
										iModuleLoop + 1, Globals.GC_COLNAME_TESTCASES));
								log.Report(Priority.INFO, xlRW.getCellData(moduleName.getKey(), iModuleLoop + 1,
										Globals.GC_COLNAME_TESTCASES) + " included for execution");
							}
						}
					} else {
						log.Report(Priority.ERROR, "Valid module does not exist");
					}
				} else if (moduleName.getValue().equalsIgnoreCase(Globals.GC_RUNSTATUS_NO)) {
					log.Report(Priority.INFO,
							moduleName.getKey() + " module test cases are skipped as RUN STATUS = NO");
				}
			}
			xlRW.saveXL();xlRW.clearXL();

			// Transforming to XML
			Transformer docTransformer = TransformerFactory.newInstance().newTransformer();
			log.Report(Priority.INFO, "initialize document transformer to create TestNG XML");
			docTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
			docTransformer.setOutputProperty(OutputKeys.METHOD, "xml");
			docTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			docTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			DOMImplementation domImpl = doc.getImplementation();
			DocumentType doctype = domImpl.createDocumentType("suite", "SYSTEM", "http://testng.org/testng-1.0.dtd");
			docTransformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
			docTransformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());

			DOMSource source = new DOMSource(doc);SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyy_HHmmss");
			Date date = Calendar.getInstance().getTime();File file = new File(Globals.GC_TESTNG_XML_PATH);
			log.Report(Priority.INFO, "verify if " + file.getPath() + " exists to store TestNG XML");
			if (!file.exists()) {
				file.mkdir();
				log.Report(Priority.INFO, "created directory : " + file.getPath() + " to store TestNG XML");
			}
			if (Common.getGlobalParam(Globals.GC_KEYTESTTYPE).equals(Globals.GC_EMPTY)) {
				testType = Globals.GC_DEFAULTTESTTYPE;
			} else {
				testType = Common.getGlobalParam(Globals.GC_KEYTESTTYPE);
			}

			RunXMLFileName = testType + "_" + dateFormat.format(date) + ".xml";
			log.Report(Priority.INFO, "finalizing TestNG XML file name : " + RunXMLFileName);

			StreamResult result = new StreamResult(new File(Globals.GC_TESTNG_XML_PATH + "\\" + RunXMLFileName));
			docTransformer.transform(source, result);
			log.Report(Priority.INFO, "created " + Globals.GC_TESTNG_XML_PATH + "\\" + RunXMLFileName);
		} catch (Exception e) {
			log.Report(Priority.ERROR,"Exception occurred while building TestNG XML : " + e.getMessage());
		}
	}
}
