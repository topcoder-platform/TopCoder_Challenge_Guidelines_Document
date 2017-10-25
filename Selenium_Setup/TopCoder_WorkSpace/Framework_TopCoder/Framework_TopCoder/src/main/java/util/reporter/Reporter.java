package util.reporter;

import java.util.Vector;
import org.testng.ITestResult;

public class Reporter implements IReporter{

	Vector<IReporter> reporter;
	
	public Reporter(String rptType){
		reporter =  new Vector<IReporter>();
	
		switch(rptType){
			case "HTML" : 
				reporter.add(new HTMLReporter("HTMLReport.html"));break;	
			default: 
				reporter.add(new HTMLReporter("filepath"));break;
		}	
	}
	
	@Override
	public synchronized void StartTest(String testName) {
		for(IReporter rep : reporter){
			rep.StartTest(testName);
		}
	}

	@Override
	public synchronized void Log(Status logStatus, String stepName, String stepDetails, boolean captureScreen) {
		for(IReporter rep : reporter){
			rep.Log(logStatus,stepName,stepDetails,captureScreen);
		}
	}

	@Override
	public synchronized void EndTest(ITestResult result) {
		for(IReporter rep : reporter){
			rep.EndTest(result);
		}
	}

	@Override
	public synchronized void FlushReport() {
		for(IReporter rep : reporter){
			rep.FlushReport();
		}
	}

	@Override
	public void CloseReport() {
		for(IReporter rep : reporter){
			rep.CloseReport();
		}	
	}
}
