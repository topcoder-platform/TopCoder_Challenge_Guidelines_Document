package util.reporter;

import org.testng.ITestResult;

public interface IReporter {

	// Start Test
	void StartTest(String testName);

	// Log steps
	void Log(Status logStatus, String stepName, String stepDetails,boolean captureScreen);

	// End Test
	void EndTest(ITestResult result);
	
	// Flush report
	void FlushReport();
	
	// Close Report
	void CloseReport();
	
	


}
