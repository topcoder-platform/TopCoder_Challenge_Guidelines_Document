package pageobjects;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import util.Log;
import util.Log.Priority;
import util.reporter.ReportManager;
import util.reporter.Status;
import wrappers.Common;
import wrappers.Data;
import wrappers.GenericWrappers;

public class TopCoderSSOPage {
	
	private WebDriver driver;
	private Log log = null;

	public TopCoderSSOPage(WebDriver driver)
	{
		this.driver=driver;
		PageFactory.initElements(driver, this);
		log = new Log(TopCoderSSOPage.class);		
	}

	/*
	 * FUNCTION: LaunchApp()
	 * 
	 * DESCRIPTION: Launch The Application
	 * 
	 * RETURN VALUE: None
	 * 
	 * PARAMETERS: None
	 * 
	 * DATE :
	 * 
	 */
	public void LaunchApp() {
		try {
			log.Report(Priority.INFO, "Launching the Application : "
					+ (Common.getGlobalParam("URL_" + Common.getGlobalParam("TEST_ENV"))));
			driver.get(Common.getGlobalParam("URL_" + Common.getGlobalParam("TEST_ENV")));
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			ReportManager.GetReporter().Log(Status.PASS, "Launch Application",
					"Application has been launched successfully.", false);
		} catch (Exception e) {
			log.Report(Priority.ERROR, "Launching the Application : "
					+ (Common.getGlobalParam("URL_" + Common.getGlobalParam("TEST_ENV"))));
			ReportManager.GetReporter().Log(Status.FAIL, "Launch Application", Common.shortenedStackTrace(e, 5), true);
		}

	}
	
	public String TopCoderWindowName()
	{
		String LoginWindow=driver.getWindowHandle();	
		return LoginWindow;
	}
	
	/*
	 * FUNCTION: LoginToTopCoder()
	 * 
	 * DESCRIPTION: Login to Application
	 * 
	 * RETURN VALUE: None
	 * 
	 * PARAMETERS: None
	 * 
	 * DATE :
	 */
	public void LoginToTopCoder() {
		try {
			log.Report(Priority.INFO, "Login to the application with credential :" + Data.getVal("Username"));
			GenericWrappers.setTextToTextBox(driver.findElement(By.xpath(Common.getDynamicLocator("TCSSOPage.txt_EmailOrHandle.XPath"))),Data.getVal("Username"));
			log.Report(Priority.INFO, "Click on Continue button");
			GenericWrappers.clickOnElement(driver.findElement(By.xpath(Common.getDynamicLocator("TCSSOPage.btn_Continue.XPath"))));
			log.Report(Priority.INFO, "Performed Login Successfully");
			Set<String> windowCount=driver.getWindowHandles();
			//String LoginWindow=driver.getWindowHandle();			
			Iterator<String> i1=windowCount.iterator();
			Thread.sleep(10000);
			while(i1.hasNext())
			{
				String ChildWindow=i1.next();
				//if(!LoginWindow.equalsIgnoreCase(ChildWindow))
				if(!driver.getTitle().equalsIgnoreCase("Sign In"))
				{			
					driver.switchTo().window(ChildWindow);
					//System.out.println(driver.getTitle());				
				}
			}			
		} catch (Exception e) {
			log.Report(Priority.INFO, e.getMessage());
			ReportManager.GetReporter().Log(Status.FAIL, "Exception", Common.shortenedStackTrace(e, 5), false);
		}
		
	}

	
	

}
