package core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import util.Log;
import util.Log.Priority;
import wrappers.Common;

public class BrowserFactory {
	
	private static WebDriver driver = null;
	private static Map<Long,WebDriver> driverThread = new HashMap<Long,WebDriver>();
	private static Log log = new Log(BrowserFactory.class);

	
		
	public static WebDriver getDriverThread(long threadID) {
		
		return driverThread.get(threadID);
	}

	private static void setDriverThread(long threadID,WebDriver driver) {
				driverThread.put(threadID, driver);
	}

	protected static synchronized void setWebDriver(String webBrowser) {
		try{
			if(driverThread.get(Thread.currentThread().getId()) == null){
				
				if (webBrowser.trim().equalsIgnoreCase("INTERNET_EXPLORER") ||
				    webBrowser.trim().equalsIgnoreCase("IEXPLORE") ||
				    webBrowser.trim().equalsIgnoreCase("IE")) {
					/*DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
					capabilities.setCapability("ignoreZoomSetting", true);
					capabilities.setCapability("ie.ensureCleanSession", true);
					System.setProperty("webdriver.ie.driver",Common.getGlobalParam("IEDriverClassPath"));
					driver = new InternetExplorerDriver(capabilities);
					 */
					
				} else if (webBrowser.trim().equalsIgnoreCase("CHROME")) {
					System.setProperty("webdriver.chrome.driver",Common.getGlobalParam("ChromeDriverPath"));
					ChromeOptions opt = new ChromeOptions();
					/*String userProfile="C:\\Users\\"+System.getProperty("user.name")+"\\AppData\\Local\\Google\\Chrome\\User Data";
					
					//opt.addArguments("disable-extensions");
					opt.addArguments("--start-maximized");
		            opt.addArguments("--user-data-dir="+userProfile);	           
		            driver = new ChromeDriver(opt);*/
					opt.addArguments("--start-maximized");
					driver = new ChromeDriver(opt);
		             
				} else if (webBrowser.trim().equalsIgnoreCase("FIREFOX") ||
						   webBrowser.trim().equalsIgnoreCase("FF")) {				
					File pathToBinary = new File(Common.getGlobalParam("FirefoxBinaryPath"));
					FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
					ProfilesIni profiles = new ProfilesIni();
					FirefoxProfile ffProfile = profiles.getProfile("default");
					// ffProfile.setPreference("signon.autologin.proxy", true);
					
					if (ffProfile == null) {
						System.out.println("Initiating Firefox with dynamic profile");									
						driver = new FirefoxDriver();
					} else {
						System.out.println("Initiating Firefox with default profile");										
						driver = new FirefoxDriver(ffBinary,ffProfile);
					}
				} else {
					throw new Error("Unknown browser type specified: " + webBrowser);
				}	
				
				// setting event firing webdriver
				EventFiringWebDriver eventDriver = new EventFiringWebDriver(driver);
				TestListener listener = new TestListener();
				eventDriver.register(listener);
				
				// Implicit timeout
				/*driver.manage().timeouts().implicitlyWait(Long.parseLong(Common.getGlobalParam(""
													  + "GLB_OBJ_SYNC_TIMEOUT")),TimeUnit.SECONDS);*/	
				// PageLoad timeout
				driver.manage().timeouts().pageLoadTimeout(Long.parseLong(Common.getGlobalParam(""
													  + "PG_LOAD_TIMEOUT")), TimeUnit.SECONDS);
							
				Capabilities cap = ((RemoteWebDriver)driver).getCapabilities();		
		        String browserName = cap.getBrowserName().toUpperCase();
		        Common.setGlobalParam("BROWSER_NAME", browserName);	
		        System.out.println("BROWSER NAME:"+browserName);
		        String os = cap.getPlatform().toString();
		        System.out.println("OPERATING SYSTEM:"+os);
		        String browserVersion = cap.getVersion().toString().substring(0, 4);
		        Common.setGlobalParam("BROWSER_VER", browserVersion);	
		        System.out.println("BROWSER VERSION:"+browserVersion);	       
		        
		        setDriverThread(Thread.currentThread().getId(),eventDriver);
			}
		}catch(Exception e){
			log.Report(Priority.ERROR, "Unable to initialize web driver :"+e.getMessage());		
		}
	}
	
}
