
package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import util.Log;
import util.Log.Priority;
import util.reporter.ReportManager;
import util.reporter.Status;
import wrappers.Common;
import wrappers.Data;
import wrappers.GenericWrappers;

public class TestListener extends TestBase
		implements ITestListener, ISuiteListener, IInvokedMethodListener, WebDriverEventListener {
	File files = null;
	String timeStamp = "";
	private Log log = new Log(TestListener.class);
	String failedTCDet;
	private Map<String, String> failedTCMap = null;

	public void onStart(ISuite suite) {
		try {
			// suite.getXmlSuite().setParallel(ParallelMode.TESTS);
			// suite.getXmlSuite().setDataProviderThreadCount(count);
			// suite.get
			Common.loadConfigProperty(Globals.GC_TESTCONFIGLOC + Globals.GC_CONFIGFILEANDSHEETNAME + ".properties");
			Common.loadCustomOR();
			log.Report(Priority.INFO, "Test Configuration initialized successfully");
		} catch (Exception e) {
			log.Report(Priority.ERROR, e.getMessage());
		}
	}

	public void onStart(ITestContext test) {

		try {
			log.Report(Priority.INFO,
					" ******************* Execution started for " + test.getName() + "******************");
			if (!driver().getWindowHandle().isEmpty()) {
				log.Report(Priority.INFO,
						"Web Driver instance found to be active for the Test Case :" + test.getName());
			}
		} catch (Exception t) {
			try {
				log.Report(Priority.INFO, "Web Driver instance found to be inactive for the Test Case :"
						+ test.getName() + " ,hence re-initiating");

				// Setting driver to null so that it can reinitialize after quit
				Method m = BrowserFactory.class.getDeclaredMethod("setDriverThread", long.class, WebDriver.class);
				m.setAccessible(true);
				m.invoke(null, Thread.currentThread().getId(), null);

				BrowserFactory.setWebDriver(Common.getGlobalParam("BROWSER"));
				log.Report(Priority.INFO,
						"Web Driver instance re-initiated " + "successfully the Test Case :" + test.getName());
			} catch (Exception e) {
				log.Report(Priority.ERROR, "Failed to re-initialize Web Driver :" + e.getMessage());
			}
		}
	}

	public void onTestStart(ITestResult result) {

	}

	public void onTestSuccess(ITestResult result) {
		// tcSuccessCnt++;
	}

	public void onTestFailure(ITestResult result) {
		// capturing failed TCs
		failedTCMap = new LinkedHashMap<String, String>();
		failedTCMap.put(result.getTestContext().getName(),
				result.getMethod().getQualifiedName().substring(0,
						result.getMethod().getQualifiedName().lastIndexOf(".")) + "|"
						+ result.getMethod().getMethodName());

		// tcFaliureCnt++;
	}

	public void onTestSkipped(ITestResult result) {
		// tcSkipCnt++;
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	public void onFinish(ITestContext context) {
		try {
			if (Data.getVal("CloseBrowser").equalsIgnoreCase("true")) {
				if (driver() != null)
					driver().quit();
			}
		} catch (Exception e) {
			log.Report(Priority.ERROR, "Exception :" + e.getLocalizedMessage());
		}
	}

	public void onFinish(ISuite suite) {
		log.Report(Priority.INFO, "Close browser for suite :" + suite.getName());
		try {
			// to handle scenario where browser already closed
			BrowserFactory.getDriverThread(Thread.currentThread().getId()).getWindowHandles();
			if (BrowserFactory.getDriverThread(Thread.currentThread().getId()) != null) {
				log.Report(Priority.INFO, "Close browser if driver not null");
				BrowserFactory.getDriverThread(Thread.currentThread().getId()).close();
				BrowserFactory.getDriverThread(Thread.currentThread().getId()).quit();
				log.Report(Priority.INFO, "Browser close/quite");
			}

		} catch (Exception e) {
			// do nothing
		}
		ReportManager.GetReporter().CloseReport();
		log.Report(Priority.INFO, "Extent report closed");
	}

	@Override
	public synchronized void afterInvocation(IInvokedMethod method, ITestResult result) {
		if (method.isTestMethod()) {

			// Handling test exceptions and errors
			if (result.getThrowable() != null) {
				result.setStatus(ITestResult.FAILURE);
				log.Report(Priority.ERROR, "Failing test " + result.getName() + "exception occcured");

				// to handle capture screen shot only if the browser is active
				try {
					driver().getTitle();
					reporter().Log(Status.FAIL, "Exception Occured", shortenedStackTrace(result.getThrowable(), 5),
							true);
				} catch (Exception e) {
					try {
						reporter().Log(Status.FAIL, "Exception Occured", shortenedStackTrace(result.getThrowable(), 5),
								true);

						// Setting driver to null to escape exception for
						// unusual closer of driver
						Method m = BrowserFactory.class.getDeclaredMethod("setDriverThread", long.class,
								WebDriver.class);
						m.setAccessible(true);
						m.invoke(null, Thread.currentThread().getId(), null);
					} catch (Throwable e1) {
						log.Report(Priority.ERROR, "Handle capture screen for active browser" + result.getName()
								+ "exception occcured " + shortenedStackTrace(e1, 15));
					}
				}
			}

			// Extent end test
			ReportManager.GetReporter().EndTest(result);
			log.Report(Priority.INFO, "Extent report end test for " + result.getName());

			// Sync testng run result with extent rpt
			if (result.getStatus() == ITestResult.FAILURE) {
				log.Report(Priority.INFO, "Setting testng report to fail in case of faliure  " + result.getName());
				result.getTestContext().getFailedTests().addResult(result, method.getTestMethod());
				result.getTestContext().getPassedTests().removeResult(method.getTestMethod());
			}

			ReportManager.GetReporter().FlushReport();
			log.Report(Priority.INFO, "Flushing report for " + result.getName());

			if (Data.getVal("CloseBrowser").equalsIgnoreCase("true")) {
				if (driver() != null) {
					driver().quit();
				}
			/*	try {
					if (driver() != null) {
						List<Integer> pids = new ArrayList<Integer>();
						String out;
						Process p = Runtime.getRuntime().exec("tasklist /FI \"IMAGENAME eq chromedriver.exe*\"");
						BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
						while ((out = input.readLine()) != null) {
							String[] items = StringUtils.split(out, " ");
							if (items.length > 1 && StringUtils.isNumeric(items[1])) {
								pids.add(NumberUtils.toInt(items[1]));
							}
						}
						driver().quit();
						System.out.println();
						Runtime.getRuntime().exec("taskkill /F /PID " + pids.get(0));
					}
				} catch (Exception e) {
					log.Report(Priority.ERROR, "Failed to quit Web Driver :" + e.getMessage());
				}*/

			}
		}
	}

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult result) {
		if (method.isTestMethod()) {
			log.Report(Priority.INFO, "Start extent report for " + result.getName());

			// Initializing browser once driver is closed in the previous
			// iteration
			try{
				if (driver().getTitle().isEmpty()) {
					onStart(result.getTestContext());
				}
  
			}catch(Exception e){
				onStart(result.getTestContext());
			}
			
			ReportManager.GetReporter().StartTest(method.getTestResult().getTestContext().getName());

			// Setting global data
			try {
				if (testDataObj != null) {
					log.Report(Priority.INFO, "Setting Global Data Map " + result.getName());
					Method m = Data.class.getDeclaredMethod("setVal", HashMap.class);
					m.setAccessible(true);
					m.invoke(null, testDataObj.get(result.getParameters()[0]));

					// Setting all itr data
					Method m1 = Data.class.getDeclaredMethod("setAllItrData", LinkedHashMap.class);
					m1.setAccessible(true);
					m1.invoke(null, testDataObj);
				}

				if (extItrTestDataObject != null) {
					log.Report(Priority.INFO, "Setting Global Data Map " + result.getName());
					Method m = Data.class.getDeclaredMethod("setValExtItr", List.class);
					m.setAccessible(true);
					m.invoke(null, extItrTestDataObject.get(result.getParameters()[0]));
				}

			} catch (Exception e) {
				log.Report(Priority.ERROR,
						"Setting Global Data Map Failed for " + result.getName() + " exception " + e.getMessage());
			}
		}
	}

	@SuppressWarnings("unused")
	private String genStackTraceLink(String stackTrace) {
		String stackTraceLnk = "";
		int iRandTraceCntr = 0;
		if (!stackTrace.isEmpty()) {
			iRandTraceCntr = new Random().nextInt(10000);
			stackTraceLnk = "<a onclick=\"toggle_div('" + iRandTraceCntr + "');\">stacktrace</a>" + "<br><div id=\""
					+ iRandTraceCntr + "\">" + stackTrace + "</div>";
			return stackTraceLnk;
		}
		return Globals.GC_EMPTY;
	}

	@Override
	public void beforeNavigateTo(String url, WebDriver driver) {
		log.Report(Priority.INFO, "Event Listener - Before Navigate URL : " + url);
	}

	@Override
	public void afterNavigateTo(String url, WebDriver driver) {
		log.Report(Priority.INFO, "Event Listener - After Navigate URL : " + url);
	}

	@Override
	public void beforeNavigateBack(WebDriver driver) {
		log.Report(Priority.INFO, "Event Listener - Before Navigate Back");
	}

	@Override
	public void afterNavigateBack(WebDriver driver) {
		log.Report(Priority.INFO, "Event Listener - After Navigate Back");
	}

	@Override
	public void beforeNavigateForward(WebDriver driver) {
		log.Report(Priority.INFO, "Event Listener - Before Navigate Forward");
	}

	@Override
	public void afterNavigateForward(WebDriver driver) {
		log.Report(Priority.INFO, "Event Listener - After Navigate Forward");
	}

	@Override
	public void beforeNavigateRefresh(WebDriver driver) {
		log.Report(Priority.INFO, "Event Listener - Before Navigate Refresh");
	}

	@Override
	public void afterNavigateRefresh(WebDriver driver) {
		log.Report(Priority.INFO, "Event Listener - After Navigate Refresh");
	}

	@Override
	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		// log.Report(Priority.INFO,"Event Listener - Before FindBy - WebElement
		// "+Web.getLocString(element));
	}

	@Override
	public void afterFindBy(By by, WebElement element, WebDriver driver) {
		// log.Report(Priority.INFO,"Event Listener - After FindBy - WebElement
		// "+Web.getLocString(element));
	}

	@Override
	public void beforeClickOn(WebElement element, WebDriver driver) {
		log.Report(Priority.INFO, "Event Listener - Before Click On - WebElement " + GenericWrappers.getLocString(element));
	}

	@Override
	public void afterClickOn(WebElement element, WebDriver driver) {
		log.Report(Priority.INFO, "Event Listener - After Click On - WebElement " + GenericWrappers.getLocString(element));
	}

	@Override
	public void beforeChangeValueOf(WebElement element, WebDriver driver) {
		log.Report(Priority.INFO, "Event Listener - Before Change Value of - WebElement " + GenericWrappers.getLocString(element));
	}

	@Override
	public void afterChangeValueOf(WebElement element, WebDriver driver) {
		log.Report(Priority.INFO, "Event Listener - After Change Value of - WebElement " + GenericWrappers.getLocString(element));
	}

	@Override
	public void beforeScript(String script, WebDriver driver) {
		// log.Report(Priority.INFO,"Event Listener - Before Script - Script
		// "+script);
	}

	@Override
	public void afterScript(String script, WebDriver driver) {
		// log.Report(Priority.INFO,"Event Listener - After Script - Script
		// "+script);
	}

	@Override
	public void onException(Throwable throwable, WebDriver driver) {
		log.Report(Priority.ERROR, "Event Listener - On Exception " + shortenedStackTrace(throwable, 2));
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

}
