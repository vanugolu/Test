package com.aims;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aims.util.CreateLogger;
import com.aims.util.Functions;

public class TestDriver extends Controller {
	String moduleName;
	
	@Parameters("moduleName")
	public TestDriver(String moduleName)
	{
		super();
		this.moduleName = moduleName;
		runModule = moduleName;
	}
	
	@BeforeTest
	public void startTesting()
	{
		String runTest = (String) testCONFIG.get("RunTest");
		String testBrowser = (String) testCONFIG.get("TestBrowser");
			
		if(runTest.equals(moduleName) || runTest.equals("sanitySuite") || runTest.equals("regressionSuite")) {	

			if(((moduleName.contains("workflows") || moduleName.contains("reverseReplication")) && !testBrowser.equals("Chrome")) || 
					(moduleName.contains("excelDataDownload") && (testBrowser.equals("InternetExplorer") || testBrowser.equals("Safari")))) {
				Functions.skipModule();
			}else {
				CreateLogger logger = new CreateLogger();
				logger.setModuleName(moduleName);
				logger.createLogger();	
				log = Logger.getLogger(moduleName);
				
				reportsUtil.log = log;
				
				html = new File(System.getProperty("user.dir")+File.separator+ reportFolder , moduleName+".html");
				
				screenshotFolder = System.getProperty("user.dir") +File.separator+ reportFolder+ File.separator;
				modules[0]=moduleName;
				
				super.startTesting();
			}
			
		}else {
			Functions.skipModule();
		}
	}
		
		
	

  @Test
  public void callMainTest() throws InterruptedException, IOException
  {
	  super.testAppMain();
  }
  
  @AfterClass
	public void endScript(){
	  super.endScript();
		
	}
}
