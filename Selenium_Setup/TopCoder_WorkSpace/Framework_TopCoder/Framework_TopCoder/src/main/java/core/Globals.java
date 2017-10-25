package core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Globals {

	// General
	public static final String GC_EMPTY = "";
	public static final boolean GC_FALSE = false;
	public static final boolean GC_TRUE = true;
	public static final String GC_KEYAUT = "AUT";
	public static final String GC_KEYTESTTYPE = "TESTTYPE";
	public static final String GC_DEFAULTTESTTYPE = "DefaultTest";
	public static final String GC_KEY_RUNXML = "RUNXML";
	public static final String GC_VAL_RUNALLITR = "ALL";
	public static final String GC_AUT_TYPE = "WEB";
	public static final Map<String, String> DB_TYPE;
	public static final String GC_COLNAME_TEST_ENV = "TEST_ENV";
	public static final String GC_COLNAME_BROWSER = "BROWSER";

	// Environment MAP for DB
	static {
		DB_TYPE = new HashMap<String, String>();
		DB_TYPE.put("PROJ", "DEV");
	}

	// SpreadSheet
	public static final String GC_CONFIGFILEANDSHEETNAME = "testconfig";
	public static final String GC_DATASHEET_ERR = "Sheet not found";
	public static final String GC_TESTCASERUNORDERPREFIX = "runorder_";
	public static final String GC_TESTDATAPREFIX = "testdata_";
	public static final String GC_TESTDATATCCOLNAME = "testcasename";
	public static final String GC_ITRCCOLNAME = "iteration";
	public static final String GC_COLNAME_CONFIG = "config";
	public static final String GC_COLNAME_VALUE = "value";
	public static final String GC_COLNAME_MODULENAME = "modulename";
	public static final String GC_COLNAME_RUNSTATUS = "runstatus";
	public static final String GC_COLNAME_TESTCASES = "testcases";
	public static final String GC_COLNAME_SETPRIORITY = "setpriority";
	public static final String GC_COLNAME_DEPNDTC = "dependenttestcase";
	public static final String GC_RUNSTATUS_YES = "YES";
	public static final String GC_RUNSTATUS_NO = "NO";
	public static final String GC_RUNXML_DEFAULT = "SelectXML";
	public static final String GC_XML_ATTR_VAL_TEST_COL_NM = "ManualTestcases";
	public static final String GC_COLNAME_CLASSPATH = "ClassPath";
	public static String GC_OBJECT_SYNC_TIMEOUT = null;
	public static final String GC_OQ_MAP_SHEET = "OQMAP";

	// FileLocations
	public static final String GC_COMMON_TESTDATALOC = "\\\\its\\\\fss\\\\DEVGHOME\\\\Automation-Selenium\\\\TestData\\\\";
	public static final String GC_TESTCASESLOC = System.getProperty("user.dir") + "\\TestCases\\";
	public static final String GC_TESTDATALOC = System.getProperty("user.dir") + "\\TestData\\";
	public static final String GC_TESTNG_XML_PATH = System.getProperty("user.dir") + "\\RunXML";
	public static final String GC_PROJECT_BIN_DIR = System.getProperty("user.dir") + "\\bin";
	public static final String GC_TESTCASE_RELPATH = ".testcases.";
	public static final String GC_LISTENERS_CLASSNAME = "core.TestListener";
	public static final String GC_PROJECT_DIR = System.getProperty("user.dir") + "\\";
	public static final String GC_TESTCONFIGLOC = GC_PROJECT_DIR;
	public static final String GC_TESTNG_TEST_OUTPUT = System.getProperty("user.dir") + "\\test-output";
	public static final String GC_TEST_HTMLREPORT_DIR;
	public static final String GC_TEST_PDFREPORT_DIR;
	public static final String GC_TEST_REPORT_RESOURCE = GC_PROJECT_DIR + "src\\devresource\\reports\\";
	public static final String GC_EXTENT_CONFIG_LOC = GC_PROJECT_DIR + "src\\main\\java\\util\\reporter\\extent-config.xml";
	public static final String GC_SIKULI_IMG_LOC = GC_PROJECT_DIR + "src\\main\\java\\common\\sikuli_resources\\";
	public static final String GC_OQ_TEMPLATE_PATH = GC_PROJECT_DIR + "OQ_Templates";
	public static final String GC_PDF_COVERTER_VBS = GC_PROJECT_DIR + "src\\main\\java\\common\\util\\";
	public static final String GC_CUSTOM_OR = GC_PROJECT_DIR + "src\\test\\java\\common\\Object.properties";
	
	
	public static String GBL_SuiteName = "";
	//public static String GBL_REPLACE_EXISTING_HTML_REPORT = Common.getConfigParam("Overwrite_Existing_Report");
	public static String GBL_TestCaseName = "";
	public static int GBL_CurrentIterationNumber = 1;
	public static String GBL_strScreenshotsFolderPath = "";
	public static Exception exception = null;
	public static AssertionError assertionerror = null;
	public static Error error = null;
	
	static{
		//Setting HTML report folder
		String timeStamp = "";
		DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmssSSSS");
		Date date = new Date();
		timeStamp = df.format(date);
		GC_TEST_HTMLREPORT_DIR = Globals.GC_PROJECT_DIR+"TestReport\\HTMLReport\\"+timeStamp;
	}
	
	static{
		//Setting PDF report folder
		String timeStmp = "";
		DateFormat dfor = new SimpleDateFormat("yyyyMMdd_HHmmssSSSS");
		Date dateA = new Date();
		timeStmp = dfor.format(dateA);
		GC_TEST_PDFREPORT_DIR = Globals.GC_PROJECT_DIR + "TestReport\\PDFReport\\"+timeStmp;
	}

	// CI
	/*public static final String GC_EXECUTION_ENVIRONMENT = System.getProperties().containsKey("env")
			? System.getProperty("env").toUpperCase() : Common.getConfigParam(GC_COLNAME_TEST_ENV);
	public static String GC_CAPTURE_SCREENSHOT = System.getProperties().containsKey("CAPTURESCREENSHOT")
			? System.getProperty("CAPTURESCREENSHOT").trim().toUpperCase() : Common.getConfigParam("CAPTURESCREENSHOT");
	public static final String GC_EXECUTION_BROWSER = System.getProperties().containsKey("BROWSER")
			? System.getProperty("BROWSER").toUpperCase() : Common.getConfigParam(GC_COLNAME_BROWSER);
	 */
	
	//CAPTURE SCREEN SHOTS
	public static final String option_UserDefault = "AS_USER_DEFINED";
	public static final String option_Never = "NEVER";
	public static final String option_OnFailure = "ON_FAILURE_ONLY";
	public static final String option_Always = "ON_EVERY_STEP";

	// XML
	public static final String GC_XML_SUITE = "suite";
	public static final String GC_XML_TEST = "test";
	public static final String GC_XML_LISTENERS = "listeners";
	public static final String GC_XML_LISTENER = "listener";
	public static final String GC_XML_CLASSES = "classes";
	public static final String GC_XML_CLASS = "class";
	public static final String GC_XML_METHODS = "methods";
	public static final String GC_XML_INCLUDE = "include";
	public static final String GC_XML_EXCLUDE = "exclude";
	public static final String GC_XML_ATTR_NAME = "name";
	public static final String GC_XML_ATTR_CLASSNAME = "class-name";
	public static final String GC_XML_ATTR_VERBOSE = "verbose";
	public static final String GC_XML_ATTR_VAL_SUITE = "testsuite";
	public static final String GC_XML_ATTR_VAL_TEST = "testXML";
	public static String GC_MANUAL_TC_NAME;
	public static final String GC_COL_MANUAL_TC = "ManualTestCaseName";

	// LOGGER
	public static final String GC_LOG_DEBUG = "debug";
	public static final String GC_LOG_INFO = "info";
	public static final String GC_LOG_WARN = "warn";
	public static final String GC_LOG_ERR = "error";
	public static final String GC_LOG_TRACE = "trace";
	public static final String GC_LOG_INIT_MSG = "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ STARTING TEST EXECUTION @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@";
	public static final String GC_LOG_INITTC_MSG = " ****************************** ";

	// EXCEPTION
	public static final String GC_EXPNTYPE_EXCEPTION = "exception";
	public static final String GC_EXPNTYPE_NULLPOINTER = "nullpointerexception";
	public static final String GC_EXPNTYPE_CLASSNOTFOUND = "classnotfoundexception";
	public static final String GC_EXPNTYPE_INTERRUPTED = "interruptedexception";
	public static final String GC_EXPNTYPE_NOSUCHMETHOD = "nosuchmethodexception";
	public static final String GC_EXPNTYPE_NOSUCHFIELD = "nosuchfieldexception";
	public static final String GC_EXPNTYPE_ILLEGALSTATE = "illegalstateexception";
	public static final String GC_EXPNTYPE_IOEXCEPTION = "illegalstateexception";

}



/*package core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Globals {

	// General
	public static final String GC_EMPTY = "";
	public static final boolean GC_FALSE = false;
	public static final boolean GC_TRUE = true;
	public static final String GC_KEYAUT = "AUT";
	public static final String GC_KEYTESTTYPE = "TESTTYPE";
	public static final String GC_DEFAULTTESTTYPE = "DefaultTest";
	public static final String GC_KEY_RUNXML = "RUNXML";
	public static final String GC_VAL_RUNALLITR = "ALL";
	public static final String GC_AUT_TYPE = "WEB";
	public static final Map<String, String> DB_TYPE;
	public static final String GC_COLNAME_TEST_ENV = "TEST_ENV";
	public static final String GC_COLNAME_BROWSER = "BROWSER";

	// Environment MAP for DB
	static {
		DB_TYPE = new HashMap<String, String>();
		DB_TYPE.put("PROJ", "DEV");
	}

	// SpreadSheet
	public static final String GC_CONFIGFILEANDSHEETNAME = "testconfig";
	public static final String GC_DATASHEET_ERR = "Sheet not found";
	public static final String GC_TESTCASERUNORDERPREFIX = "runorder_";
	public static final String GC_TESTDATAPREFIX = "testdata_";
	public static final String GC_TESTDATATCCOLNAME = "testcasename";
	public static final String GC_ITRCCOLNAME = "iteration";
	public static final String GC_COLNAME_CONFIG = "config";
	public static final String GC_COLNAME_VALUE = "value";
	public static final String GC_COLNAME_MODULENAME = "modulename";
	public static final String GC_COLNAME_RUNSTATUS = "runstatus";
	public static final String GC_COLNAME_TESTCASES = "testcases";
	public static final String GC_COLNAME_SETPRIORITY = "setpriority";
	public static final String GC_COLNAME_DEPNDTC = "dependenttestcase";
	public static final String GC_RUNSTATUS_YES = "YES";
	public static final String GC_RUNSTATUS_NO = "NO";
	public static final String GC_RUNXML_DEFAULT = "SelectXML";
	public static final String GC_XML_ATTR_VAL_TEST_COL_NM = "ManualTestcases";
	public static final String GC_COLNAME_CLASSPATH = "ClassPath";
	public static String GC_OBJECT_SYNC_TIMEOUT = null;
	public static final String GC_OQ_MAP_SHEET = "OQMAP";

	// FileLocations
	public static final String GC_COMMON_TESTDATALOC = "\\\\its\\\\fss\\\\DEVGHOME\\\\Automation-Selenium\\\\TestData\\\\";
	public static final String GC_TESTCASESLOC = System.getProperty("user.dir") + "\\TestCases\\";
	public static final String GC_TESTDATALOC = System.getProperty("user.dir") + "\\TestData\\";
	public static final String GC_TESTNG_XML_PATH = System.getProperty("user.dir") + "\\RunXML";
	public static final String GC_PROJECT_BIN_DIR = System.getProperty("user.dir") + "\\bin";
	public static final String GC_TESTCASE_RELPATH = ".testcases.";
	public static final String GC_LISTENERS_CLASSNAME = "core.TestListener";
	public static final String GC_PROJECT_DIR = System.getProperty("user.dir") + "\\";
	public static final String GC_TESTCONFIGLOC = GC_PROJECT_DIR;
	public static final String GC_TESTNG_TEST_OUTPUT = System.getProperty("user.dir") + "\\test-output";
	public static final String GC_TEST_HTMLREPORT_DIR;
	public static final String GC_TEST_PDFREPORT_DIR;
	public static final String GC_TEST_REPORT_RESOURCE = GC_PROJECT_DIR + "src\\devresource\\reports\\";
	public static final String GC_EXTENT_CONFIG_LOC = GC_PROJECT_DIR + "src\\main\\java\\util\\reporter\\extent-config.xml";
	public static final String GC_SIKULI_IMG_LOC = GC_PROJECT_DIR + "src\\main\\java\\common\\sikuli_resources\\";
	public static final String GC_OQ_TEMPLATE_PATH = GC_PROJECT_DIR + "OQ_Templates";
	public static final String GC_PDF_COVERTER_VBS = GC_PROJECT_DIR + "src\\main\\java\\common\\util\\";
	
	
	public static String GBL_SuiteName = "";
	//public static String GBL_REPLACE_EXISTING_HTML_REPORT = Common.getConfigParam("Overwrite_Existing_Report");
	public static String GBL_TestCaseName = "";
	public static int GBL_CurrentIterationNumber = 1;
	public static String GBL_strScreenshotsFolderPath = "";
	public static Exception exception = null;
	public static AssertionError assertionerror = null;
	public static Error error = null;
	
	static{
		//Setting HTML report folder
		String timeStamp = "";
		DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmssSSSS");
		Date date = new Date();
		timeStamp = df.format(date);
		GC_TEST_HTMLREPORT_DIR = Globals.GC_PROJECT_DIR+"TestReport\\HTMLReport\\"+timeStamp;
	}
	
	static{
		//Setting PDF report folder
		String timeStmp = "";
		DateFormat dfor = new SimpleDateFormat("yyyyMMdd_HHmmssSSSS");
		Date dateA = new Date();
		timeStmp = dfor.format(dateA);
		GC_TEST_PDFREPORT_DIR = Globals.GC_PROJECT_DIR + "TestReport\\PDFReport\\"+timeStmp;
	}

	// CI
	public static final String GC_EXECUTION_ENVIRONMENT = System.getProperties().containsKey("env")
			? System.getProperty("env").toUpperCase() : Common.getConfigParam(GC_COLNAME_TEST_ENV);
	public static String GC_CAPTURE_SCREENSHOT = System.getProperties().containsKey("CAPTURESCREENSHOT")
			? System.getProperty("CAPTURESCREENSHOT").trim().toUpperCase() : Common.getConfigParam("CAPTURESCREENSHOT");
	public static final String GC_EXECUTION_BROWSER = System.getProperties().containsKey("BROWSER")
			? System.getProperty("BROWSER").toUpperCase() : Common.getConfigParam(GC_COLNAME_BROWSER);
	 
	
	//CAPTURE SCREEN SHOTS
	public static final String option_UserDefault = "AS_USER_DEFINED";
	public static final String option_Never = "NEVER";
	public static final String option_OnFailure = "ON_FAILURE_ONLY";
	public static final String option_Always = "ON_EVERY_STEP";

	// XML
	public static final String GC_XML_SUITE = "suite";
	public static final String GC_XML_TEST = "test";
	public static final String GC_XML_LISTENERS = "listeners";
	public static final String GC_XML_LISTENER = "listener";
	public static final String GC_XML_CLASSES = "classes";
	public static final String GC_XML_CLASS = "class";
	public static final String GC_XML_METHODS = "methods";
	public static final String GC_XML_INCLUDE = "include";
	public static final String GC_XML_EXCLUDE = "exclude";
	public static final String GC_XML_ATTR_NAME = "name";
	public static final String GC_XML_ATTR_CLASSNAME = "class-name";
	public static final String GC_XML_ATTR_VERBOSE = "verbose";
	public static final String GC_XML_ATTR_VAL_SUITE = "testsuite";
	public static final String GC_XML_ATTR_VAL_TEST = "testXML";
	public static String GC_MANUAL_TC_NAME;
	public static final String GC_COL_MANUAL_TC = "ManualTestCaseName";

	// LOGGER
	public static final String GC_LOG_DEBUG = "debug";
	public static final String GC_LOG_INFO = "info";
	public static final String GC_LOG_WARN = "warn";
	public static final String GC_LOG_ERR = "error";
	public static final String GC_LOG_TRACE = "trace";
	public static final String GC_LOG_INIT_MSG = "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ STARTING TEST EXECUTION @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@";
	public static final String GC_LOG_INITTC_MSG = " ****************************** ";

	// EXCEPTION
	public static final String GC_EXPNTYPE_EXCEPTION = "exception";
	public static final String GC_EXPNTYPE_NULLPOINTER = "nullpointerexception";
	public static final String GC_EXPNTYPE_CLASSNOTFOUND = "classnotfoundexception";
	public static final String GC_EXPNTYPE_INTERRUPTED = "interruptedexception";
	public static final String GC_EXPNTYPE_NOSUCHMETHOD = "nosuchmethodexception";
	public static final String GC_EXPNTYPE_NOSUCHFIELD = "nosuchfieldexception";
	public static final String GC_EXPNTYPE_ILLEGALSTATE = "illegalstateexception";
	public static final String GC_EXPNTYPE_IOEXCEPTION = "illegalstateexception";

}
*/