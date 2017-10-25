
package wrappers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import core.Globals;
import util.Log;
import util.Log.Priority;

public class Common {
	private static Map<String, String> globalParam = new LinkedHashMap<String, String>();	
	private static Log log = new Log(Common.class);
	private static Properties OR = null;
	
	public static String getGlobalParam(String paramName) {
		if(!globalParam.isEmpty() && globalParam.containsKey(paramName.toUpperCase())){
			return globalParam.get(paramName.toUpperCase());
		}
		return Globals.GC_EMPTY;
	}

	public static void setGlobalParam(String parameterName,String parameterValue) {
		if(!globalParam.containsKey(parameterValue)){
			globalParam.put(parameterName.trim().toUpperCase(), parameterValue);
		}
	}
	
	/*
	 * -------------------------------------------------------------------------
	 * FUNCTION:loadConfigProperty(String configPath) 
	 * DESCRIPTION: Load config property values into Map<String, String> globalParam
	 * PARAMETERS: String configPath 
	 * RETURNS : NA 
	 * REVISION :
	 * HISTORY:
	 * -------------------------------------------------------------------------
	 * Author :  Date : 
	 * -------------------------------------------------------------------------
*/ 	public static void loadConfigProperty(String configPath) {
		Properties prop = new Properties();InputStream ins = null;		
		if (globalParam.isEmpty()) {
			try {
				ins = new FileInputStream(configPath);
				log.Report(Priority.INFO, "reading " + configPath + " file");
				prop.load(ins);
				Enumeration<?> e = prop.propertyNames();
				while (e.hasMoreElements()) {
					String key = (String) e.nextElement();
					String val = prop.getProperty(key);
					globalParam.put(key.toString().trim().toUpperCase(), val);
					log.Report(Priority.DEBUG, "setting @globalParam with key :"+ key + " -- value :" + val);				
				}				
				globalParam.remove(Globals.GC_EMPTY);
			} catch (IOException ex) {
				log.Report(Priority.ERROR, "Unable to read Config :"+ ex.getMessage());
			} catch (Exception e) {
				log.Report(Priority.ERROR, "Unable to read Config :"+ e.getMessage());
			} finally {
				if (ins != null) {
					try {
						ins.close();
					} catch (IOException e) {
						log.Report(Priority.ERROR, "Unable to read Config :"+ e.getMessage());
					}
				}
			}
		}	
	}
	
	public static String getIterationDataAsString(Map<String, String> testdata){
		String result = "";
		for(Map.Entry<String,String> td : testdata.entrySet()){
			result = result+(td.getKey()+" : "+td.getValue())+" | ";
		}
		return result.substring(0,result.lastIndexOf("|"));
	}
		
	public static String shortenedStackTrace(Throwable t, int maxLines) {
		String str = logIt(t);
		StringWriter writer = new StringWriter();
		t.printStackTrace(new PrintWriter(writer));
		String[] lines = writer.toString().split("\n");
		StringBuilder sb = new StringBuilder();
		sb.append("["+str+"]\n");
		for (int i = 0; i < Math.min(lines.length, maxLines); i++) {
			sb.append(lines[i]).append("\n");
		}
		return sb.toString();
	}
	
	private static String logIt(Throwable e1) {
	    StackTraceElement[] stacktrace = e1.getStackTrace();
	    List<String> res = new ArrayList<String>();
	    String finalTrace = "";
	    for(int i=0; i<stacktrace.length; i++) {
	        StackTraceElement e = stacktrace[i];
	        String methodName = e.getMethodName();
	       
	        if (e.getClassName().startsWith("pageobject")) {   
	        	res.add("Class " + e.getClassName()+" -- Method "+ methodName+ " @ line " + stacktrace[i].getLineNumber()); 
	        }
	    }
	    for(String str : res){finalTrace = finalTrace + str +" ;";}
	    return finalTrace;
	}
	
	public static void loadCustomOR() throws FileNotFoundException, IOException{
		OR = new Properties();
		OR.load(new FileInputStream(Globals.GC_CUSTOM_OR));
	}

	

	public static String getDynamicLocator(String locatorKey, String... dynamicAttribute) throws Exception {

		String locator = null;
		if (OR.isEmpty() || OR == null)
			throw new Exception("Custom OR is not loaded");
		try {
			if (dynamicAttribute.length == 0) {
				return OR.getProperty(locatorKey);
			} else {
				locator = OR.getProperty(locatorKey);
				for (String loc : dynamicAttribute) {
					locator = locator.replaceFirst("@@Value@@", loc);
				}
			}
		} catch (Exception e) {
			throw new Exception("Locator " + locatorKey + " not found");
		}
		return locator;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/*public static int getIterations() {
		return dataProviderIterations;
	}

	private static String checkEnv(String envName) {
		if (envName.contains("PROJ")) {
			return Globals.DB_TYPE.get("PROJ");
		}
		if (envName.contains("QA")) {
			return Globals.DB_TYPE.get("QA");
		}
		if (envName.contains("PROD")) {
			return Globals.DB_TYPE.get("PROD");
		}
		return null;
	}*/

	/*public static String[] getTestQuery(String queryName) {
		try {
			String[] queryData = new String[3];
			String appName = getConfigParam("AUT");

			File xmlfile = new File(Globals.GC_TESTDATALOC
					+ Globals.GC_TESTDATAPREFIX + appName + "_"
					+ checkEnv(getConfigParam("TEST_ENV")) + ".xml");

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlfile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("Module");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
				Element eElement = (Element) nNode;
				String name = eElement.getAttribute("name");
				if (name.equalsIgnoreCase("query")) {
					NodeList parameterList = eElement
							.getElementsByTagName("parameter");

					for (int temp1 = 0; temp1 < parameterList.getLength(); temp1++) {
						Node pNode = parameterList.item(temp1);
						Element pElement = (Element) pNode;

						String pName = pElement.getAttribute("QueryName");
						if (pName.equalsIgnoreCase(queryName)) {
							queryData[0] = pElement.getAttribute("DB");
							queryData[1] = pElement.getAttribute("Query");
						}
					}
				}
			}
			return queryData;
		} catch (Exception e) {
			ThrowException.Report(TYPE.EXCEPTION, "Unable to get test query :"
					+ e.getMessage());
		}
		return null;

	}*/
	 
	

	/*public static void getParam(String configPath) {
		readConfigProperty(configPath);
	}*/

/*	public static String GetParameterValue(String strParamName) {
		String value = null;
		if (globalTestdata.containsKey(strParamName.toUpperCase().trim())) {
			if (globalTestdata.get(strParamName.toUpperCase().trim()).length() > 0)
				value = globalTestdata.get(strParamName.trim().toUpperCase());
		} else {
			throw new Error(
					"Parameter '"
							+ strParamName
							+ "' does not exist in Test data!\nStopping script execution!");
		}
		return value;
	}*/

	/**
	 * <pre>
	 * Method to set config property in globalParam map
	 * </pre>
	 * 
	 * <pre>
	 * If overWriteExisting is 
	 * @param parameterName
	 * @param parameterValue
	 * @param overWriteExisting
	 * 
	 * <pre>
	 * <b> - true</b> to update the property value even if the property already exists.
	 * <b> - false</b> to ignore updating value to already existing property.
	 * Default is <b>true</b>
	 * </pre>
	 */


	/**
	 * <pre>
	 * This method returns the testdata for the current iteration to access them in @Beforemethod
	 * </pre>
	 * 
	 * @param globalTestData
	 *            <pre>
	 * It is the testdata map for the respective testcase
	 * </pre>
	 * @return <pre>
	 * <b>Returns the data for the running iteration number</b>
	 * </pre>
	 */
	/*public static Map<String, String> getIterationTestData(
			LinkedHashMap<Integer, Map<String, String>> globalTestData) {
		LinkedList<Integer> keyList = new LinkedList<>(globalTestData.keySet());
		int index = keyList.get(iterationNumber);
		iterationNumber++;
		return globalTestData.get(index);
	}

	public static String getTestDataAsString() throws Exception {
		String printTestData="";
		for (Map.Entry<String, String> entry : Common.globalTestdata.entrySet()) {
			if(!entry.getKey().equalsIgnoreCase("PASSWORD"))
				printTestData=printTestData+entry.getKey() + "="+ entry.getValue() +"\n";
		}
	 return printTestData;
	}*/

}


/*package lib;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import core.Globals;
import util.Log;
import util.Log.Priority;

public class Common {
	private static Map<String, String> globalParam = new LinkedHashMap<String, String>();	
	private static Log log = new Log(Common.class);
	
	public static String getGlobalParam(String paramName) {
		if(!globalParam.isEmpty() && globalParam.containsKey(paramName.toUpperCase())){
			return globalParam.get(paramName.toUpperCase());
		}
		return Globals.GC_EMPTY;
	}

	public static void setGlobalParam(String parameterName,String parameterValue) {
		if(!globalParam.containsKey(parameterValue)){
			globalParam.put(parameterName.trim().toUpperCase(), parameterValue);
		}
	}
	
	
	 * -------------------------------------------------------------------------
	 * FUNCTION:loadConfigProperty(String configPath) 
	 * DESCRIPTION: Load config property values into Map<String, String> globalParam
	 * PARAMETERS: String configPath 
	 * RETURNS : NA 
	 * REVISION :
	 * HISTORY:
	 * -------------------------------------------------------------------------
	 * Author :  Date : 
	 * -------------------------------------------------------------------------
 	public static void loadConfigProperty(String configPath) {
		Properties prop = new Properties();InputStream ins = null;		
		if (globalParam.isEmpty()) {
			try {
				ins = new FileInputStream(configPath);
				log.Report(Priority.INFO, "reading " + configPath + " file");
				prop.load(ins);
				Enumeration<?> e = prop.propertyNames();
				while (e.hasMoreElements()) {
					String key = (String) e.nextElement();
					String val = prop.getProperty(key);
					globalParam.put(key.toString().trim().toUpperCase(), val);
					log.Report(Priority.DEBUG, "setting @globalParam with key :"+ key + " -- value :" + val);				
				}				
				globalParam.remove(Globals.GC_EMPTY);
			} catch (IOException ex) {
				log.Report(Priority.ERROR, "Unable to read Config :"+ ex.getMessage());
			} catch (Exception e) {
				log.Report(Priority.ERROR, "Unable to read Config :"+ e.getMessage());
			} finally {
				if (ins != null) {
					try {
						ins.close();
					} catch (IOException e) {
						log.Report(Priority.ERROR, "Unable to read Config :"+ e.getMessage());
					}
				}
			}
		}	
	}
	
	public static String getIterationDataAsString(Map<String, String> testdata){
		String result = "";
		for(Map.Entry<String,String> td : testdata.entrySet()){
			result = result+(td.getKey()+" : "+td.getValue())+" | ";
		}
		return result.substring(0,result.lastIndexOf("|"));
	}
		
	public static String shortenedStackTrace(Throwable t, int maxLines) {
		StringWriter writer = new StringWriter();
		t.printStackTrace(new PrintWriter(writer));
		String[] lines = writer.toString().split("\n");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < Math.min(lines.length, maxLines); i++) {
			sb.append(lines[i]).append("\n");
		}
		return sb.toString();
	}
	*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/*public static int getIterations() {
		return dataProviderIterations;
	}

	private static String checkEnv(String envName) {
		if (envName.contains("PROJ")) {
			return Globals.DB_TYPE.get("PROJ");
		}
		if (envName.contains("QA")) {
			return Globals.DB_TYPE.get("QA");
		}
		if (envName.contains("PROD")) {
			return Globals.DB_TYPE.get("PROD");
		}
		return null;
	}*/

	/*public static String[] getTestQuery(String queryName) {
		try {
			String[] queryData = new String[3];
			String appName = getConfigParam("AUT");

			File xmlfile = new File(Globals.GC_TESTDATALOC
					+ Globals.GC_TESTDATAPREFIX + appName + "_"
					+ checkEnv(getConfigParam("TEST_ENV")) + ".xml");

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlfile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("Module");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
				Element eElement = (Element) nNode;
				String name = eElement.getAttribute("name");
				if (name.equalsIgnoreCase("query")) {
					NodeList parameterList = eElement
							.getElementsByTagName("parameter");

					for (int temp1 = 0; temp1 < parameterList.getLength(); temp1++) {
						Node pNode = parameterList.item(temp1);
						Element pElement = (Element) pNode;

						String pName = pElement.getAttribute("QueryName");
						if (pName.equalsIgnoreCase(queryName)) {
							queryData[0] = pElement.getAttribute("DB");
							queryData[1] = pElement.getAttribute("Query");
						}
					}
				}
			}
			return queryData;
		} catch (Exception e) {
			ThrowException.Report(TYPE.EXCEPTION, "Unable to get test query :"
					+ e.getMessage());
		}
		return null;

	}*/
	 
	

	/*public static void getParam(String configPath) {
		readConfigProperty(configPath);
	}*/

/*	public static String GetParameterValue(String strParamName) {
		String value = null;
		if (globalTestdata.containsKey(strParamName.toUpperCase().trim())) {
			if (globalTestdata.get(strParamName.toUpperCase().trim()).length() > 0)
				value = globalTestdata.get(strParamName.trim().toUpperCase());
		} else {
			throw new Error(
					"Parameter '"
							+ strParamName
							+ "' does not exist in Test data!\nStopping script execution!");
		}
		return value;
	}*/

	/**
	 * <pre>
	 * Method to set config property in globalParam map
	 * </pre>
	 * 
	 * <pre>
	 * If overWriteExisting is 
	 * @param parameterName
	 * @param parameterValue
	 * @param overWriteExisting
	 * 
	 * <pre>
	 * <b> - true</b> to update the property value even if the property already exists.
	 * <b> - false</b> to ignore updating value to already existing property.
	 * Default is <b>true</b>
	 * </pre>
	 */


	/**
	 * <pre>
	 * This method returns the testdata for the current iteration to access them in @Beforemethod
	 * </pre>
	 * 
	 * @param globalTestData
	 *            <pre>
	 * It is the testdata map for the respective testcase
	 * </pre>
	 * @return <pre>
	 * <b>Returns the data for the running iteration number</b>
	 * </pre>
	 */
	/*public static Map<String, String> getIterationTestData(
			LinkedHashMap<Integer, Map<String, String>> globalTestData) {
		LinkedList<Integer> keyList = new LinkedList<>(globalTestData.keySet());
		int index = keyList.get(iterationNumber);
		iterationNumber++;
		return globalTestData.get(index);
	}

	public static String getTestDataAsString() throws Exception {
		String printTestData="";
		for (Map.Entry<String, String> entry : Common.globalTestdata.entrySet()) {
			if(!entry.getKey().equalsIgnoreCase("PASSWORD"))
				printTestData=printTestData+entry.getKey() + "="+ entry.getValue() +"\n";
		}
	 return printTestData;
	}*/


