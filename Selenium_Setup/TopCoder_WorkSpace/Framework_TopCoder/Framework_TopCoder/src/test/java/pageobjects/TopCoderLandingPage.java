package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

import util.Log;
import util.Log.Priority;
import util.reporter.ReportManager;
import util.reporter.Status;
import wrappers.Common;
import wrappers.Data;
import wrappers.GenericWrappers;

public class TopCoderLandingPage {
	
	private WebDriver driver;
	private Log log = null;

	public TopCoderLandingPage(WebDriver driver)
	{
		this.driver=driver;
		PageFactory.initElements(driver, this);
		log = new Log(TopCoderLandingPage.class);		
	}
	
	/*
	 * FUNCTION: NavigateToChallengesSection()
	 * 
	 * DESCRIPTION: Login to Application
	 * 
	 * RETURN VALUE: None
	 * 
	 * PARAMETERS: None
	 * 
	 * DATE :
	 */
	public void NavigateToChallengesSection() {
		try {
			log.Report(Priority.INFO, "Click on Challenges.");			
			GenericWrappers.clickOnElement(driver.findElement(By.xpath(Common.getDynamicLocator("TCChallengesPage.lnk_Challenges.XPath"))));
			GenericWrappers.waitForElement(driver.findElement(By.xpath(Common.getDynamicLocator("TCChallengesPage.lnk_ChallengesList.XPath"))));
			if(GenericWrappers.isWebElementDisplayed(driver.findElement(By.xpath(Common.getDynamicLocator("TCChallengesPage.lnk_ChallengesList.XPath")))))
			{
				log.Report(Priority.INFO, "Challenges Section is opened.");
				ReportManager.GetReporter().Log(Status.PASS, "Click on Challenges.", "Challenges Section is opened successfully.", false);
			}else
			{
				log.Report(Priority.ERROR, "Challenges Section is not opened.");
				ReportManager.GetReporter().Log(Status.FAIL, "Click on Challenges.", "Challenges Section is not opened properly.", false);
			}
		} catch (Exception e) {
			log.Report(Priority.INFO, e.getMessage());
			ReportManager.GetReporter().Log(Status.FAIL, "Exception", Common.shortenedStackTrace(e, 5), false);
		}
		
	}

}
