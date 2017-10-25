package pageobjects;

import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import util.Log;
import util.Log.Priority;
import util.reporter.ReportManager;
import util.reporter.Status;
import wrappers.Common;
import wrappers.Data;
import wrappers.GenericWrappers;

public class SignInWiproPage {
	
	@FindBy(xpath = "//input[@id='userNameInput']")
	private WebElement txt_WiproADID;
	
	@FindBy(xpath = "//input[@id='passwordInput']")
	private WebElement txt_ADPassword;
	
	@FindBy(xpath = "//span[@id='submitButton']")
	private WebElement btn_SignIn;
	
	private WebDriver driver;
	private Log log = null;
	
	public SignInWiproPage(WebDriver driver)
	{
		this.driver=driver;
		PageFactory.initElements(driver, this);
		log = new Log(SignInWiproPage.class);		
	}
	
	/*
	 * FUNCTION: LoginToWipro()
	 * 
	 * DESCRIPTION: Login to Application
	 * 
	 * RETURN VALUE: None
	 * 
	 * PARAMETERS: None
	 * 
	 * DATE :
	 */
	public void LoginToWipro(String WindowName) {
		try {
			log.Report(Priority.INFO, "Enter Wipro ADID :" + Data.getVal("ADID"));
			GenericWrappers.setTextToTextBox(txt_WiproADID,Data.getVal("ADID"));
			log.Report(Priority.INFO, "Enter AD Password :" + Data.getVal("ADPassword"));
			GenericWrappers.setTextToTextBox(txt_ADPassword,Data.getVal("ADPassword"));
			log.Report(Priority.INFO, "Click on Sign In button");
			GenericWrappers.clickOnElement(btn_SignIn);
//			btn_SignIn.click();
			log.Report(Priority.INFO, "Performed Login Successfully");			
			Thread.sleep(50000);	
			//new WebDriverWait(driver, 70).until(ExpectedConditions.urlContains("topgear.topcoder.com"));
			System.out.println(driver.getWindowHandles().size());
			driver.switchTo().window(WindowName);			
			System.out.println(driver.getTitle());			
		} catch (Exception e) {
			log.Report(Priority.INFO, e.getMessage());
			System.out.println(driver.getWindowHandles().size());
			ReportManager.GetReporter().Log(Status.FAIL, "Exception", Common.shortenedStackTrace(e, 5), false);			
		}
		
	}


}
