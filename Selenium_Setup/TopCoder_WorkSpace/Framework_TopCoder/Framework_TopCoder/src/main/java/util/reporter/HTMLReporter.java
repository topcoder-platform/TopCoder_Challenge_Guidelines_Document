package util.reporter;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import core.Globals;
import core.TestBase;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import util.Log;
import util.Log.Priority;
import wrappers.Common;

public class HTMLReporter extends TestBase implements IReporter{

	ExtentReports reporter = null;
	Map<Long,ExtentReports> mapExtent = null;
	Map<Long, ExtentTest> loggers = null;
	File files = null;
	File videoFile = null;
	
	private static Log log = new Log(HTMLReporter.class);
	
	public HTMLReporter(String filePath) {
		try{
			files = new File(Globals.GC_TEST_HTMLREPORT_DIR);
			if(!files.exists()){files.mkdirs();}
			reporter = new ExtentReports(Globals.GC_TEST_HTMLREPORT_DIR+"\\"+filePath, false);
			reporter.loadConfig(new File(Globals.GC_EXTENT_CONFIG_LOC));
			loggers = new HashMap<Long, ExtentTest>();
		}catch(Exception e){
			log.Report(Priority.INFO, "Exception occurred " + e.getMessage());
		}
	}
	
	@Override
	public void StartTest(String testName) {
		try{
			long threadId = Thread.currentThread().getId();
			loggers.put(threadId, reporter.startTest(testName));
			//loggers.put(threadId, mapExtent.get(threadId).startTest(testName));
		}catch(Exception e){
			log.Report(Priority.INFO, "Exception occurred " + e.getMessage());
		}
	}

	@Override
	public void Log(Status logStatus, String stepName, String stepDetails, boolean captureScreen) {
		try {
			long threadId = Thread.currentThread().getId();

			switch (Common.getGlobalParam("CAPTURESCREENSHOT").toLowerCase().trim()) {
			case "always":
				captureScreen = true;
				break;
			case "never":
				captureScreen = false;
				break;
			case "user_defined":
				break;
			case "on_faliure_only":
				if (logStatus.equals(Status.FAIL)) {
					captureScreen = true;
				}
				break;
			default:
				break;
			}

			if (loggers.containsKey(threadId)) {
				if (!captureScreen) {
					switch (logStatus) {
					case PASS:
						loggers.get(threadId).log(LogStatus.PASS, stepName, stepDetails);
						break;
					case FAIL:
						loggers.get(threadId).log(LogStatus.FAIL, stepName, stepDetails);
						break;
					case WARN:
						loggers.get(threadId).log(LogStatus.WARNING, stepName, stepDetails);
						break;
					case INFO:
						loggers.get(threadId).log(LogStatus.INFO, stepName, stepDetails);
						break;
					default:
						loggers.get(threadId).log(LogStatus.UNKNOWN, "Invalid step", "Invalid step details");
					}
				} else {
					switch (logStatus) {
					case PASS:
						loggers.get(threadId).log(LogStatus.PASS, stepName,
								stepDetails + loggers.get(threadId).addScreenCapture(captureScreen()));
						break;
					case FAIL:
						loggers.get(threadId).log(LogStatus.FAIL, stepName,
								stepDetails + loggers.get(threadId).addScreenCapture(captureScreen()));
						break;
					case WARN:
						loggers.get(threadId).log(LogStatus.WARNING, stepName,
								stepDetails + loggers.get(threadId).addScreenCapture(captureScreen()));
						break;
					case INFO:
						loggers.get(threadId).log(LogStatus.INFO, stepName, stepDetails);
						break;
					default:
						loggers.get(threadId).log(LogStatus.UNKNOWN, "Invalid step", "Invalid step details");
					}
				}
			}
		} catch (Exception e) {
			log.Report(Priority.INFO, "Exception occurred " + e.getMessage());
		}
	}
	
	@Override
	public void EndTest(ITestResult result) {
		try{
			long threadId = Thread.currentThread().getId();	
			if(loggers.containsKey(threadId))
			{
				
				if(loggers.get(threadId).getRunStatus().equals(LogStatus.FAIL)){
				   result.setStatus(ITestResult.FAILURE);
				}
				if(loggers.get(threadId).getRunStatus().equals(LogStatus.PASS)){
				   result.setStatus(ITestResult.SUCCESS);
				}
				if(loggers.get(threadId).getRunStatus().equals(LogStatus.WARNING)){
				   result.setStatus(ITestResult.SKIP);
				}		
				reporter.endTest(loggers.get(threadId));
				System.out.println(result.getName());
			}
		}catch(Exception e){
			log.Report(Priority.INFO, "Exception occurred " + e.getMessage());
		}	
	}

	@Override
	public void FlushReport() {
		reporter.flush();
	}
	
	@Override
	public void CloseReport() {
		reporter.close();
	}
	
	@SuppressWarnings("unused")
	private synchronized String captureScreen3(){
		String timeStamp =  Globals.GC_EMPTY;
		if(driver()!=null){
			files = new File(Globals.GC_TEST_HTMLREPORT_DIR+"\\Screens");
			if(!files.exists()){files.mkdirs();}
			try {
			Thread.sleep(1000);
			DateFormat dateFormat = new SimpleDateFormat("MMddyyyy_HHmmss");
			Date date = new Date();
			timeStamp = dateFormat.format(date);
			
				TakesScreenshot screen = (TakesScreenshot)driver();
				File fileSrc = screen.getScreenshotAs(OutputType.FILE);
				String dest = Globals.GC_TEST_HTMLREPORT_DIR+"\\Screens\\"+timeStamp+".png";
				
				File fileDestn = new File(dest);
				FileUtils.copyFile(fileSrc, fileDestn);
				
				return dest;
			} catch (IOException | InterruptedException e) {
				log.Report(Priority.ERROR,"Exception : "+e.getMessage());	
			}
		}
		return "";
	}
    
	private synchronized String captureScreen(){
		String timeStamp =  Globals.GC_EMPTY;
		if(driver()!=null){
			files = new File(Globals.GC_TEST_HTMLREPORT_DIR+"\\Screens");
			if(!files.exists()){files.mkdirs();}
			try {
			Thread.sleep(1000);
			DateFormat dateFormat = new SimpleDateFormat("MMddyyyy_HHmmss");
			Date date = new Date();
			timeStamp = dateFormat.format(date);
			String rel = "\\Screens\\"+timeStamp+".png";
			String dest = Globals.GC_TEST_HTMLREPORT_DIR+rel;
			   
			Screenshot ss = new AShot().shootingStrategy
			    					(ShootingStrategies.viewportPasting(100))
			    					.takeScreenshot(driver());
			    ImageIO.write(ss.getImage(),"PNG", new File(dest));
				return "."+rel;
			} catch (IOException | InterruptedException e) {
				log.Report(Priority.ERROR,"Exception : "+e.getMessage());	
			}
		}
		return "";
	}
	
	

	

	
	
}
