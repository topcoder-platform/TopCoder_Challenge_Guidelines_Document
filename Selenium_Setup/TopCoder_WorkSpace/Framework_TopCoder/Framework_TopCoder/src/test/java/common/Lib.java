package common;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import util.Log;
import util.Log.Priority;
import wrappers.Common;
import wrappers.GenericWrappers;


public class Lib {
	
	private static Log log = new Log(Lib.class);
	
	public static void RoboTAB(){
		try {
			Robot robo = new Robot();
			robo.keyPress(KeyEvent.VK_TAB);
			robo.keyRelease(KeyEvent.VK_TAB);
		} catch (Exception e) {
			log.Report(Priority.ERROR,"Robot TAB action failed : "+e.getMessage());
		}
	}
	
	public static boolean checkPattern(String pattern, String text){
		try{
			Pattern patrn = Pattern.compile(pattern);
		    Matcher matcher = patrn.matcher(text.trim());
		    boolean matches = matcher.matches();   
			return matches;
		}catch(Exception e){
			log.Report(Priority.ERROR,"Check pattern exception : "+e.getMessage());
		}
		return false;
	}
	
	public static void SelectDate(WebDriver driver,WebElement calTrigger,String date){
		try{
			Thread.sleep(5000);
			GenericWrappers.waitForElement(calTrigger, 20);
			GenericWrappers.clickOnElement(calTrigger);
			GenericWrappers.waitForElement(driver.findElement(By.xpath(Common.getDynamicLocator("pspCalPrev"))));
			String day = String.valueOf(Integer.parseInt(date.split("/")[0]));
			String mon = String.valueOf(Integer.parseInt(date.split("/")[1])-1); // to select month by index
			String year = String.valueOf(Integer.parseInt(date.split("/")[2]));
			GenericWrappers.selectItem(driver.findElement(By.xpath(Common.getDynamicLocator("selYear"))), year);
			GenericWrappers.selectItemByIndex(driver.findElement(By.xpath(Common.getDynamicLocator("selMonth"))), mon);
			GenericWrappers.clickOnElement(driver.findElement(By.xpath("(.//a[text()='"+day.trim()+"'])[2]")));
		}catch(Exception e){
			log.Report(Priority.ERROR,"Calendar date selection faile: "+e.getMessage());
		}
	}
	
	public static boolean logout(WebDriver driver,WebElement logoutid){
		GenericWrappers.SwitchToDefaultContent(driver);
		   if(GenericWrappers.isWebElementDisplayed(logoutid, true)){
			   GenericWrappers.clickOnElement(logoutid);
			   try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					
				}
			   	return true;
		   }
		   return false;
	}
	
}
