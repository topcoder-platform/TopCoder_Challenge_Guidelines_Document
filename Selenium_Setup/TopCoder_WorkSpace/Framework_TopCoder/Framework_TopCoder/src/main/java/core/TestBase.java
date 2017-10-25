package core;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.DataProvider;

import util.Log;
import util.Log.Priority;
import util.reporter.IReporter;
import util.reporter.ReportManager;
import util.reporter.Status;
import wrappers.Common;
import wrappers.Data;

public class TestBase {
	protected static String MANUAL_TC_NAME = Globals.GC_EMPTY; 
	protected static LinkedHashMap<Integer, LinkedHashMap<String, String>> testDataObj = null;
	protected static LinkedHashMap<Integer, List<LinkedHashMap<String, String>>> extItrTestDataObject = null;
	private static Map<Long,String> manualTCName = new HashMap<Long,String>();
	private Log log = new Log(TestBase.class);
	
	protected IReporter reporter() {
		return ReportManager.GetReporter();
	}
	
	protected WebDriver driver(){
		return BrowserFactory.getDriverThread(Thread.currentThread().getId());
	}
	
	protected String getManualTCName(){
		return manualTCName.get(Thread.currentThread().getId());
	}
	
	@DataProvider
	public Object[][] setData(ITestNGMethod method, ITestContext test){
		String tcClassPkgNm = Globals.GC_EMPTY;
		String tcName = Globals.GC_EMPTY;
		IGetData getData = null;
		Object[][] dataProviderObj = null;
	
		try{
			tcClassPkgNm = method.getRealClass().getPackage().getName();
			tcName = test.getName();
			manualTCName.put(Thread.currentThread().getId(), tcName);
			log.Report(Priority.INFO,"Setting data provider with TestData "
			                  +Common.getGlobalParam("TESTDATA_FORMAT"));
		    if(Common.getGlobalParam("TESTDATA_FORMAT").equals("XLSX")
		       ||Common.getGlobalParam("TESTDATA_FORMAT").equals("XLS")){
		       getData = new GetXLData();
		    }
		   
		    testDataObj = getData.getTestDataMap(tcClassPkgNm, tcName);
		    if (testDataObj != null) {
		    	dataProviderObj = new Object[testDataObj.size()][2];
				int count = 0;
				
				for(Integer itr : testDataObj.keySet()){
					dataProviderObj[count][0] = itr;
					dataProviderObj[count][1] = null;
					count++;
				}
				
				return dataProviderObj;
			} 
		}catch(Exception e){
			log.Report(Priority.ERROR, "Unable to initialize DataProvider :"+e.getMessage());
		}
		return new Object[][] {{}}; // As null cannot be returned in DataProvider	
	}
	
	@DataProvider
	public Object[][] setExtItrData(ITestNGMethod method, ITestContext test){
		String tcClassPkgNm = Globals.GC_EMPTY;
		String tcName = Globals.GC_EMPTY;
		IGetData getData = null;
		Object[][] dataProviderObj = null;
	
		try{
			tcClassPkgNm = method.getRealClass().getPackage().getName();
			tcName = test.getName();
			manualTCName.put(Thread.currentThread().getId(), tcName);
			log.Report(Priority.INFO,"Setting data provider with TestData "
			                  +Common.getGlobalParam("TESTDATA_FORMAT"));
		    if(Common.getGlobalParam("TESTDATA_FORMAT").equals("XLSX")
		       ||Common.getGlobalParam("TESTDATA_FORMAT").equals("XLS")){
		       getData = new GetXLData();
		    }
		
		    extItrTestDataObject = getData.getTestDataMapExtrnlItr(tcClassPkgNm, tcName);
		    if (extItrTestDataObject != null) {
		    	dataProviderObj = new Object[extItrTestDataObject.size()][2];
				int count = 0;
				
				for(Integer itr : extItrTestDataObject.keySet()){
					dataProviderObj[count][0] = itr;
					dataProviderObj[count][1] = null;
					count++;
				}			
				return dataProviderObj;
			} 
		}catch(Exception e){
			log.Report(Priority.ERROR, "Unable to initialize DataProvider :"+e.getMessage());
		}
		return new Object[][] {{}}; // As null cannot be returned in DataProvider	
	}
	
	
	
	protected void ReportIterationData(int itr){
		reporter().Log(Status.INFO,"Test data for iteration : "+ itr,
			       Common.getIterationDataAsString(Data.getItrDataMap()),false);
	}
	
	
	
}
