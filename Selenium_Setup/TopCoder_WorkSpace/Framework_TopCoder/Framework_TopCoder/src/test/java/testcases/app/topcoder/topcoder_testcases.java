package testcases.app.topcoder;

import org.testng.annotations.Test;

import core.TestBase;
import pageobjects.SignInWiproPage;
import pageobjects.TopCoderLandingPage;
import pageobjects.TopCoderSSOPage;

import util.reporter.ReportManager;
import util.reporter.Status;

public class topcoder_testcases extends TestBase {
	
	TopCoderSSOPage TopCoderSSO = null;
	SignInWiproPage SignIn=null;
	TopCoderLandingPage TopcoderLanding=null;
	
	@Test(dataProvider = "setData")
	public void Auto_TC_001(int itr, Object o) {
		ReportManager.GetReporter().Log(Status.INFO, "TC_001-Test Objectives",
				"Login to TopCoder", false);

		ReportIterationData(itr);
		TopCoderSSO=new TopCoderSSOPage(driver());
		// Login to the TrendNext
		TopCoderSSO.LaunchApp();
		String DefaultWindow=TopCoderSSO.TopCoderWindowName();
		TopCoderSSO.LoginToTopCoder();
		
		SignIn=new SignInWiproPage(driver());
		SignIn.LoginToWipro(DefaultWindow);
		
		TopcoderLanding=new TopCoderLandingPage(driver());
		TopcoderLanding.NavigateToChallengesSection();	

	}
	

}
