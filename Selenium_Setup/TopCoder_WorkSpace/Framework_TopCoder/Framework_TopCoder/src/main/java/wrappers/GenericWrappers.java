
package wrappers;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import core.BrowserFactory;
import util.Log;
import util.Log.Priority;


public class GenericWrappers {
	
	private static Log log = new Log(GenericWrappers.class);
	

	public static boolean isWebElementDisplayed(WebDriver driver, String xpath, boolean... waitForElement) {
		try {
			return isWebElementDisplayed(driver.findElement(By.xpath(xpath)), waitForElement);
		} catch (Exception e) {
			// do nothing when element not present
			log.Report(Priority.ERROR, "Error " + Common.shortenedStackTrace(e, 10));
		}
		return false;
	}

	public static boolean isWebElementDisplayed(WebElement element,boolean... waitForElement) {
		log.Report(Priority.INFO, "check if WebElement " + getLocString(element)+ " is displayed");
		boolean blnElementDisplayed = false;
		try {
			try {
				if (waitForElement.length > 0) {
					if (waitForElement[0] == true) {
						GenericWrappers.waitForElement(element);
					}
				}
			} catch (Exception e) {
				// Do nothing
			}
			blnElementDisplayed = element.isDisplayed();
		} catch (NoSuchElementException e) {
			blnElementDisplayed = false;
		}
		log.Report(Priority.INFO, "WebElement " + getLocString(element)+ " display status "+blnElementDisplayed );
		return blnElementDisplayed;
	}
	
	public static void selectItemByIndex(WebElement ele, String index) {
		Select sel = new Select(ele);
		sel.getFirstSelectedOption();
		sel.selectByIndex(Integer.parseInt(index));
	}

	public static boolean isWebElementsDisplayed(List<WebElement> elements,boolean... waitForElement) {
		boolean blnElementDisplayed = false;
		try {
			log.Report(Priority.INFO, "check if WebElements " + getLocString(elements.get(0))+ " is displayed");
			try {
				if (waitForElement.length > 0) {
					if (waitForElement[0] == true) {
						GenericWrappers.waitForElements(elements);
					}
				}
			} catch (Exception e) {
				// Do nothing
			}
			try {
				if ((new WebDriverWait(BrowserFactory.getDriverThread(Thread.currentThread().getId()),
						Long.parseLong(Common.getGlobalParam("GLB_OBJ_SYNC_TIMEOUT")))
						.ignoring(StaleElementReferenceException.class)
						.until(ExpectedConditions.visibilityOfAllElements(elements)).size() == elements.size())) {
					 blnElementDisplayed = true;
				}
			} catch (Exception e) {
				// Do nothing
			}
		} catch (NoSuchElementException e) {
			blnElementDisplayed = false;
		}
		log.Report(Priority.INFO, "WebElement " + getLocString(elements.get(0))+ " display status "+blnElementDisplayed);
		return blnElementDisplayed;
	}

	
	public static String setTextToTextBox(WebElement textBoxField,CharSequence... valueToSet) {
		String fieldTextValue = "";
		try{
			log.Report(Priority.INFO, "Set text to input box " + getLocString(textBoxField)+ " with value : "+valueToSet);		
			if (GenericWrappers.isWebElementDisplayed(textBoxField)) {
				textBoxField.clear();
				textBoxField.sendKeys(valueToSet);
				textBoxField.click();
				fieldTextValue = textBoxField.getAttribute("value");
			}
			log.Report(Priority.INFO, "Value set to input box " + getLocString(textBoxField)
									+ " with value : "+fieldTextValue);
		}catch(Exception e){
			log.Report(Priority.ERROR, e.getLocalizedMessage());
		}
		return fieldTextValue;
	}

	
	public static boolean clickOnElement(WebElement clickableElement) {
		boolean success = false;
		try{
			log.Report(Priority.INFO, "Click on element " + getLocString(clickableElement));
			if (GenericWrappers.isWebElementDisplayed(clickableElement)) {
				clickableElement.click();
				success = true;
			}
		}catch(Exception e){
			log.Report(Priority.ERROR, e.getLocalizedMessage());
		}	
		log.Report(Priority.INFO, "Click on element status" + success);
		return success;
	}


	public static void waitForElement(WebElement element) {
		try{
			log.Report(Priority.INFO, "Wait for element " + getLocString(element));
			(new WebDriverWait(BrowserFactory.getDriverThread(Thread.currentThread().getId()), 
			Long.parseLong(Common.getGlobalParam("GLB_OBJ_SYNC_TIMEOUT"))))
			.ignoring(StaleElementReferenceException.class)
			.until(ExpectedConditions.elementToBeClickable(element));	
		}catch(Exception e){
			log.Report(Priority.INFO, e.getLocalizedMessage());
		}
	}

	
	public static void waitForElement(WebElement element,int timeout) {
		try{
			log.Report(Priority.INFO, "Wait for element " + getLocString(element)+ " with timeout "+timeout);
			(new WebDriverWait(BrowserFactory.getDriverThread(Thread.currentThread().getId()), timeout))
		    .ignoring(StaleElementReferenceException.class)
			.until(ExpectedConditions.elementToBeClickable(element));	
		}catch(Exception e){
			log.Report(Priority.INFO,"Element "+getLocString(element)+" not found");
		}	
	}
	
	
	public static void waitForElements(List<WebElement> elements) {
		try {
			log.Report(Priority.INFO, "Wait for elements " + getLocString(elements.get(0)));
			(new WebDriverWait(BrowserFactory.getDriverThread(Thread.currentThread().getId()),
			 Long.parseLong(Common.getGlobalParam("objectSyncTimeout"))))
			.ignoring(StaleElementReferenceException.class)
			.until(ExpectedConditions.visibilityOfAllElements(elements));
		} catch (Exception e) {
			log.Report(Priority.INFO, e.getLocalizedMessage());
		}
	}	

	
    public static String getLocString(WebElement ele){
    	if(ele != null){
    		if(ele.toString().contains("Proxy element for: DefaultElementLocator")){
    			return ele.toString().split("By.")[1];
    		}else{
    			return ele.toString().split("->")[1];
    		}	
    	}
    	return ""; 
    }

    public static boolean selectItem(WebElement ele,String item){
    	try{
    		
    	    Select sel = new Select(ele);
    	    for(WebElement el : sel.getOptions()){
    	    	
    	    	if(el.getText().contains(item)){
    	    	   sel.selectByVisibleText(el.getText());
    	    	   return true;
    	    	}	
    	    } 
    	}catch(Exception e){
    		log.Report(Priority.ERROR, e.getMessage());
    	}
    	return false;
    }
    
	public static void getSelectedByIndex(WebElement ele, int ind) {
		try {
			Select sel = new Select(ele);
			sel.selectByIndex(ind);
		} catch (Exception e) {
			log.Report(Priority.ERROR, e.getMessage());
		}

	}
    
    public static String getSelectedItemfromList(WebElement ele){
    	try{
    		Select sel = new Select(ele);
    		return sel.getFirstSelectedOption().getText();
    	}catch(Exception e){
    		log.Report(Priority.ERROR, e.getMessage());
    	}
    	return "";
    }
    
    public static List<WebElement> getAllListOptions(WebElement ele){
    	try{
    		Select sel = new Select(ele);
    		return sel.getOptions();
    	}catch(Exception e){
    		log.Report(Priority.ERROR, e.getMessage());
    	}
    	return null;
    }
    
    public static List<String> getSelectItems(WebElement ele){
    	List<String> listItems =  new ArrayList<String>();
    	try{
    		Select sel = new Select(ele);
    		for(WebElement e : sel.getOptions()){
    			listItems.add(e.getText());
    		}
    		return listItems;
    	}catch(Exception e){
    		log.Report(Priority.ERROR, e.getMessage());
    	}
    	return null;
    }
    
	public static boolean setCheckboxStatus(List<WebElement> elem, boolean toCheck) {
		try {
			boolean checkstatus;
			for (WebElement ele : elem) {
				checkstatus = ele.isSelected();
				if (checkstatus == toCheck) {
					log.Report(Priority.INFO, "Checkbox is already checked");
					return true;
				} else {
					ele.click();
					log.Report(Priority.INFO, "Checked the checkbox");
					return true;
				}
			}
		} catch (Exception e) {
			log.Report(Priority.ERROR, e.getMessage());
			return false;
		}
		return toCheck;
	}
	
	public static void SwitchToFrame(WebDriver driver,WebElement frm){
		driver.switchTo().frame(frm);
	}
	
	public static void SwitchToDefaultContent(WebDriver driver){
		driver.switchTo().defaultContent();
	}
	
	public static void WebScrollToElement(WebDriver driver, WebElement ele){
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",ele);
	} 
	
	public static String getText(WebElement ele){
		return ele.getText();
	} 
	
	public static void openNewWindow(WebDriver driver) {
		((JavascriptExecutor) driver).executeScript("window.open();");
	}

	public static String GetElementValue(WebElement Element) {
			if (GenericWrappers.isWebElementDisplayed(Element, true)) {
				log.Report(Priority.INFO, "Get WebElement value");
				return Element.getText().trim();
		}
		return null;
	}
	
	
}

