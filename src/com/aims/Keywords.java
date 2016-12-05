package com.aims;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aims.util.Functions;
import com.aims.xls.ExcelOperations;

public class Keywords {
	public String version;
	public String user;
	public String password;
	public static Properties CONFIG;

	public static Properties OR;
	public static Properties APPTEXT;
	public static Properties testCONFIG;
	public Properties reportCONFIG;
	public ExcelOperations controller;
	public ExcelOperations testData;
	public boolean testCasesfileAssigned=false;
	public boolean testDatafileAssigned=false;

	public String currentTest;
	public String currentTest_Description;
	public String keyword;
	public RemoteWebDriver driver = null, driver1 = null, driver2 = null, driver3 = null, driver4 = null, driver5 = null;
	public String object;
	public String objectArr[];
	public String currentTSID;
	public String stepDescription;
	public String proceedOnFail;
	public String testStatus;
	public String data_column_name;
	public String data_column_nameArr[];
	public String data;
	public int testRepeat;
	public String testCaseDescription;
	public Logger log;
	public String userAgent = "Desktop";

	public String runTestApp;
	public String runTest;
	public String runModule;
	public String testBrowser;
	public String launchBrowser;
	public String displayBrowserVersion;

	public static String reportFolder;
	public File html;
	public String captureScreenShot = "true";
	public String screenshotFolder; 
	public int browserNumber=1;
	public boolean mkDir=false;
	public Keywords keywords;
	public String modules[]=new String[1];
	public String sanityModules[] = new String[1];
	public String browsers[] = {"Firefox","InternetExplorer","Chrome"};
	public int moduleFailCount;
	long WAIT1SEC=1000, WAIT2SEC=2000, WAIT3SEC=3000, WAIT4SEC=4000, WAIT5SEC=5000, WAIT6SEC=6000, WAIT7SEC=7000, WAIT8SEC=8000;
	//	long WAIT1SEC=2000, WAIT2SEC=4000, WAIT3SEC=6000, WAIT4SEC=8000, WAIT5SEC=10000, WAIT6SEC=12000, WAIT7SEC=14000, WAIT8SEC=15000;
  
    public Actions action;
	public Keywords()
	{

	}
	
	public By getBy(Properties objectFile, String locator) {

		By by = null;
		String value= null;

		try {
			value = objectFile.getProperty(locator);

			if(locator.endsWith("xpath"))
				by = By.xpath(value);
			else if(locator.endsWith("id"))
				by = By.id(value);
			else if(locator.endsWith("cssSelector"))
				by = By.cssSelector(value);
			else if(locator.endsWith("linkText"))
				by = By.linkText(value);
			else if(locator.endsWith("partialLinkText"))
				by = By.partialLinkText(value);
			else if(locator.endsWith("tagName"))
				by = By.tagName(value);
			else if(locator.endsWith("name"))
				by = By.name(value);
			else if(locator.endsWith("className"))
				by = By.className(value);
			else
				by = By.xpath(value);      //statement added to cater to the rest locator properties
		}catch(Throwable t) {
			log.debug("Exception caught while accessing the locator :" +locator);
		}
		return by;
	}
	public WebElement getWebElement(Properties objectFile,String locator)
	{
		WebElement element = null;
		try {

			element = driver.findElement(getBy(objectFile, locator));
			//Functions.highlighter(driver, element);

		}catch(Throwable t) {
			log.debug("Exception caught at object :" +locator);
		}
		return element;
	}

	public List<WebElement> getWebElements(Properties objectFile,String locator)
	{
		List<WebElement> element = null;
		try {
			element = driver.findElements(getBy(objectFile, locator));

		}catch(Throwable t) {
			log.debug("Exception caught at object :" +locator);
		}
		return element;
	}

	public String extractUser(){

		try{
			String url = CONFIG.getProperty(objectArr[1]);

			user = url.substring(0, url.indexOf(":"));

			/*		//Commenting the code due for security issues
			log.debug("User: "+user);*/
		}catch(Throwable t) {
			log.debug(t.getMessage());
		}
		return user;
	}

	public String extractPassword(){

		try{
			String url = CONFIG.getProperty(objectArr[1]);

			password = url.substring(url.indexOf(":")+1, url.length());

			/*		//Commenting the code due for security issues
			log.debug("Password: "+password);*/
		}catch(Throwable t) {
			log.debug(t.getMessage());
		}
		return password;
	}


	public String navigateMultiWindow(){
		log.debug("=============================");
		log.debug("Executing navigateMultiWindow");		

		try{
			int b = Integer.parseInt(testData.getCellData(currentTest, data_column_nameArr[0],testRepeat));
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/drivers/chromedriver.exe");
			log.debug("No of windows to open " + b);
			for(int i=0;i<b;i++){

				switch(i){

				case 0: 		
					driver1 = new ChromeDriver();
					driver1.manage().window().maximize();
					log.debug("Window 1 launched" );
					break;
				case 1:
					driver2 = new ChromeDriver();
					driver2.manage().window().maximize();
					log.debug("Window 2 launched" );
					break;
				case 2:
					driver3 = new ChromeDriver();
					driver3.manage().window().maximize();
					log.debug("Window 3 launched" );
					break;
				case 3:
					driver4 = new ChromeDriver();
					driver4.manage().window().maximize();
					log.debug("Window 4 launched" );
					break;
				case 4:
					driver5 = new ChromeDriver();
					driver5.manage().window().maximize();
					log.debug("Window 5 launched" );
					break;
				}			
			}
			return "Pass";
		}
		catch(Throwable t) {
			log.debug(t.getMessage());
			return "Fail";
		}
	}


	public String setDriver(){
		log.debug("=============================");
		log.debug("Executing setDriver");
		try{
			int a = Integer.parseInt(testData.getCellData(currentTest, data_column_nameArr[0],testRepeat));
			switch(a){
			case 1: 
				driver = driver1;
				System.out.println(driver.getCurrentUrl());
				break;
			case 2:	
				driver = driver2;
				System.out.println(driver.getCurrentUrl());
				break;
			case 3 :	
				driver = driver3;
				System.out.println(driver.getCurrentUrl());
				break;
			case 4 :	
				driver = driver4;
				System.out.println(driver.getCurrentUrl());
				break;
			case 5 :	
				driver = driver5;
				System.out.println(driver.getCurrentUrl());
				break;
			}
			return "Pass";
		}

		catch(Throwable t) {
			log.debug(t.getMessage());
			return "Fail";
		}

	}


	public String navigate() {
		int count;
		String user,password;
		String getUser,getPassword;

		try{
			getUser = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			getPassword = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
		}catch(Throwable t){
			getUser = null;
			getPassword = null;
		}

		if(!(getUser == null || getUser.isEmpty() || getPassword == null || getPassword.isEmpty())){
			user = getUser;
			password = getPassword;
		}else{
			user = extractUser();
			password = extractPassword();
		}

		try{
			log.debug("=============================");
			log.debug("Executing Navigate");

			//		java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
			//		System.out.println("Hostname of the machine: " + localMachine.getHostName());
			//		log.debug("Hostname of the machine: " + localMachine.getHostName());

			launchWebpage();  //calling function internally
			
			Thread.sleep(WAIT2SEC);
			if(!((objectArr[0].contains("aut")) || (objectArr[0].contains("crx")))) {
				getWebElement(OR, "aims.pub.loginPage.userName.textBox.xpath").click();
				getWebElement(OR, "aims.pub.loginPage.userName.textBox.xpath").sendKeys(user);
				getWebElement(OR, "aims.pub.loginPage.password.textBox.xpath").click();
				getWebElement(OR, "aims.pub.loginPage.password.textBox.xpath").sendKeys(password);
				try {
					driver.manage().timeouts().implicitlyWait(5, TimeUnit.MILLISECONDS);
					count = 0;
					do {
						getWebElement(OR, "aims.pub.loginPage.login.button.xpath").click();
					}while(driver.findElements(By.xpath((OR.getProperty("aims.pub.loginPage.login.button.xpath")))).size()>0 && count++<2);
				}catch(Throwable e) {
					//do nothing
				}finally{
					driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
				}

				Thread.sleep(WAIT3SEC);

				boolean flag = false;
				try{
					String data = testData.getCellData(currentTest, data_column_nameArr[5],testRepeat);
					flag = Boolean.valueOf(data);
				}catch(Throwable r){
					//do nothing
				}
				if(!flag){
					try{
							Functions.handleTnCPopUp(driver, log, OR);		
					}catch(Throwable t){}
				}

				try{
					String logoClick = testData.getCellData(currentTest, data_column_nameArr[4],testRepeat);
					if(!(logoClick == null || logoClick.isEmpty()))
						log.debug("Logo will not be clicked.");
					else
					{
						if(!testBrowser.equals("InternetExplorer")) {
							getWebElement(OR,"aims.global.logo_Image.xpath").click();
						}
						else
						{
							WebElement logo =  getWebElement(OR,"aims.global.logo_Image.xpath");
							logo.sendKeys(Keys.CONTROL);
							logo.click();
						}
					}
				}catch(Throwable r) {
					if(!testBrowser.equals("InternetExplorer")) {
						getWebElement(OR,"aims.global.logo_Image.xpath").click();
					}
					else
					{
						WebElement logo =  getWebElement(OR,"aims.global.logo_Image.xpath");
						logo.sendKeys(Keys.CONTROL);
						logo.click();
					}
				}

			}else if(!(objectArr[0].contains("crx"))) {
				getWebElement(OR,"aims.aut.loginPage.userName.textBox.xpath").click();
				getWebElement(OR,"aims.aut.loginPage.userName.textBox.xpath").sendKeys(user);
				getWebElement(OR,"aims.aut.loginPage.password.textBox.xpath").click();
				getWebElement(OR,"aims.aut.loginPage.password.textBox.xpath").sendKeys(password);
				getWebElement(OR,"aims.aut.loginPage.login.button.xpath").click();
				try{
					Functions.handleTnCPopUp(driver, log, OR);				
				}catch(Throwable t){
					log.debug("Terms and condition did not came in Author Login");
				}
			} else {
				getWebElement(OR,"aims.crx.loginPage.anonymousLogin.button.xpath").click();
				Thread.sleep(WAIT1SEC);
				getWebElement(OR,"aims.crx.loginPage.userName.textBox.xpath").click();
				log.debug("user is: " + user);
				getWebElement(OR,"aims.crx.loginPage.userName.textBox.xpath").sendKeys(user);
				Thread.sleep(WAIT1SEC);
				getWebElement(OR,"aims.crx.loginPage.password.textBox.xpath").click();
				getWebElement(OR,"aims.crx.loginPage.password.textBox.xpath").sendKeys(password);
				Thread.sleep(WAIT1SEC);
				getWebElement(OR,"aims.crx.loginPage.login.button.xpath").click();
			}
			

		}catch(Throwable t){
			log.debug("Error while opening browser -" + t.getMessage());
			return "Fail - Domain Page not Found";
		}

		action = new Actions(driver);

		return "Pass";
	}

	public String verifyUserName() {
		log.debug("=============================");
		log.debug("Executing verifyUserName Keyword");
		// extract the test data
		String expectedUserName = user;
		String actualUserName;
		try {
			String temp = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			actualUserName = temp.replaceAll("\\s+", "");
			System.out.println("actualUserName : " + actualUserName);
			if(expectedUserName.equals("admin")){
				expectedUserName = "Administrator";
			}
			if(actualUserName.equalsIgnoreCase(expectedUserName)){
				log.debug("expected  is : " +expectedUserName );
				log.debug("actual  is : " +actualUserName );
				return "Pass";
			}
			else{
				log.debug("expected  is : " +expectedUserName );
				log.debug("actual  is : " +actualUserName );	
				return "Fail" ;
			}

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing 'verifyUserName' -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}

	}



	/*	public String navigateUserAgent() {
		log.debug("=============================");
		log.debug("Executing NavigateUserAgent");

		if (CONFIG.getProperty("testBrowser").equals("Firefox")) {
			// check for user agent
			log.debug("++++++++++ userAgent" + userAgent);
			if (userAgent.equals("iPhone")) {
				String iPhoneAgent = "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_0 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Version/4.0.5 Mobile/8A293 Safari/6531.22.7";
				FirefoxProfile profile = new FirefoxProfile();
				profile.setPreference("general.useragent.override", iPhoneAgent);
				wbdv = new FirefoxDriver(profile);

				log.debug("++++++++++ setting profile " + userAgent);
			}
			else if(userAgent.equals("android")){
				String AndroidAgent = "Mozilla/5.0 (Linux; U; Android 2.2; de-de; U0101HA Build/FRF85B) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
				FirefoxProfile profile = new FirefoxProfile();
				profile.setPreference("general.useragent.override", AndroidAgent);
				wbdv = new FirefoxDriver(profile);

				log.debug("++++++++++ setting profile " + userAgent);
			}

			else if(userAgent.equals("iPad")){
				String iPadAgent = "Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B334b Safari/531.21.10";
				FirefoxProfile profile = new FirefoxProfile();
				profile.setPreference("general.useragent.override", iPadAgent);
				wbdv = new FirefoxDriver(profile);

				log.debug("++++++++++ setting profile " + userAgent);
			}

			else {
				log.debug("++++++++++ Desktop user agent " + userAgent);
				wbdv = new FirefoxDriver();
			}

			driver = new EventFiringWebDriver(wbdv);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		} 
		else if(CONFIG.getProperty("testBrowser").equals("Safari")) {
			log.debug("++++++++++ userAgent" + userAgent);
			if(userAgent.equals("iPad")){
				String iPadAgent = "Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B334b Safari/531.21.10";
				FirefoxProfile profile = new FirefoxProfile();
				profile.setPreference("general.useragent.override", iPadAgent);
				wbdv = new FirefoxDriver(profile);

				log.debug("++++++++++ setting profile " + userAgent);
			}
			else {
				log.debug("++++++NavRiskAnalysisTitle++++ Desktop user agent " + userAgent);

				    DesiredCapabilities dc = new DesiredCapabilities();
				    dc.setBrowserName("safari");
				    wbdv = new RemoteWebDriver(dc);
			}
			driver = new EventFiringWebDriver(wbdv);
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);


		}
		driver.navigate().to(CONFIG.getProperty(objectArr[0]));
		return "Pass";
	}
	 */

	public String clickLink() {
		log.debug("=============================");
		log.debug("Executing clickLink");
		try {		

			try {
				log.debug("Content of the item clicked :"+ getWebElement(OR, objectArr[0]).getText());
			}catch(Throwable t) {
				//do nothing
			}

			if(!testBrowser.equals("InternetExplorer") || driver.getCurrentUrl().contains("aut"))
				getWebElement(OR,objectArr[0]).click();
			else
			{
				WebElement ele =  getWebElement(OR,objectArr[0]);
				ele.sendKeys(Keys.CONTROL);
				ele.click();
			}
			
			//Handling Data Unavailable Pop Up for Specific Dev and QA Environment pages
			//Functions.handleDataUnavailablePopUp(driver, log, CONFIG);
			//Functions.handleExceptionHandlingPopUp(driver, log, CONFIG);

		} catch (Throwable t) {
			// report error
			log.debug("Error while clicking on link -" + objectArr[0]+ t.getMessage());
			return "Fail - Link Not Found";
		}
		return "Pass";
	}

	/**
	 * Click html icon
	 * @return
	 */
	public String clickButton() {
		log.debug("=============================");
		log.debug("Executing clickButton");
		try {		

			try {
				log.debug("Content of the item clicked :"+ getWebElement(OR, objectArr[0]).getText());
			}catch(Throwable t) {
				//do nothing
			}
			getWebElement(OR, objectArr[0]).click();


		} catch (Throwable t) {
			// report error
			log.debug("Error while clicking on Button -" + objectArr[0]+ t.getMessage());
			return "Fail - Button Not Found";
		}
		return "Pass";
	}
	
	
	
	public String clickByText() {
		log.debug("=============================");
		log.debug("Executing clickByText");
		boolean  flag= false;
		try {		

			try {
				log.debug("Content of the item clicked :"+ getWebElement(OR, objectArr[0]).getText());
			}catch(Throwable t) {
				//do nothing
			}
			String data = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			List<WebElement> elements = getWebElements(OR, objectArr[0]);
			for (WebElement webElement : elements) {
				if(webElement.getText().toLowerCase().contains(data.toLowerCase())){
					webElement.click();
					flag=true;
					break;
				}
			}
           if(flag==false){
        	   log.debug("No buton found to click with text-" + data);
   			return "Fail - Button Not Found";
           }

		} catch (Throwable t) {
			// report error
			log.debug("Error while clicking on Buttonby text-" + objectArr[0]+ t.getMessage());
			return "Fail - Button Not Found";
		}
		return "Pass";
	}


	public String clickCheckBox() {
		log.debug("=============================");
		log.debug("Executing clickCheckBox Keyword");
		try {

			if(!(driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).isSelected())){
				driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
				Thread.sleep(WAIT2SEC);
			}
		} catch (Throwable t) {
			log.debug("Error while clicking on checkbox -" + objectArr[0]
					+ t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String clickLink_linkText() {
		log.debug("=============================");
		log.debug("Executing clickLink_linkText");
		try {
			String linktext = objectArr[0];
			driver.findElement(By.linkText(linktext.substring(5))).click();
		} catch (Throwable t) {
			// report error
			log.debug("Error while clicking on link -" + objectArr[0]+ t.getMessage());
			return "Fail - Link Not Found";
		}
		return "Pass";
	}

	public String input() {
		log.debug("=============================");
		log.debug("Executing input Keyword");
		// extract the test data
		try {
			String data = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			WebElement element = getWebElement(OR, objectArr[0]);
			element.clear();
			element.sendKeys(data);
			log.debug("input data -" + data);
		} catch (Throwable t) {
			// report error
			log.debug("Error while writing into input -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
		return "Pass";
	}


	public String actionInput() {
		log.debug("=============================");
		log.debug("Executing actionInput Keyword");
		// extract the test data
		try {
			String data = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			action.moveToElement(driver.findElement(By.xpath(OR.getProperty(objectArr[0]))));
			action.click();
			action.sendKeys(data);
			action.build().perform();
		} catch (Throwable t) {
			// report error
			log.debug("Error while writing into actionInput -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String inputClear(){
		log.debug("=============================");
		log.debug("Executing inputClear Keyword");

		try {
			getWebElement(OR, objectArr[0]).clear();

		} catch (Throwable t) {
			// report error
			log.debug("Error while clearing the textfield -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
		return "Pass";
	}


	public String Wait() {
		log.debug("=============================");
		log.debug("Executing wait Keyword");

		/*try{
			if(!(testBrowser.equals("Safari"))) {
				Functions.waitForElementClickable(driver, log, objectArr[1]);
			}
		}catch(ArrayIndexOutOfBoundsException e) {

		}*/

		try {
			String data = OR.getProperty(objectArr[0]);
			Thread.sleep(Long.parseLong(data));
		}catch(Throwable t) {

		}

		return "Pass";
	}

	public String waitForElementAndClick() {
		log.debug("=============================");
		log.debug("Executing waitForElementAndClick Keyword");

		try {
			String data = OR.getProperty(objectArr[0]);

			if(!(launchBrowser.equals("Safari")))
				Functions.waitForElementClickable(driver, log, objectArr[1]);
			else
				Thread.sleep(Long.parseLong(data));

			Thread.sleep(Long.parseLong(data));

			if(!testBrowser.equals("InternetExplorer") || driver.getCurrentUrl().contains("aut"))
				getWebElement(OR,objectArr[1]).click();
			else
			{
				WebElement ele =  getWebElement(OR,objectArr[1]);
				ele.sendKeys(Keys.CONTROL);
				ele.click();
			}

		}catch(Throwable t) {
			log.debug("Error while executing waitForElementAndClick-" + t.getMessage());
			return "Fail";
		}

		return "Pass";
	}

	public String rightClickToClickElement(){
		log.debug("=============================");
		log.debug("Executing rightClickToClickElement Keyword");


		try {
			String data = OR.getProperty(objectArr[0]);

			WebElement obj = driver.findElement(By.xpath(OR.getProperty(objectArr[2])));
			(new Actions(driver)).contextClick(obj).perform();
			try{
				Functions.waitForElementClickable(driver, log, objectArr[1]);
			}catch(ArrayIndexOutOfBoundsException e) {

			}

			Thread.sleep(Long.parseLong(data));

			getWebElement(OR, objectArr[1]).click();

		}catch(Throwable t) {
			log.debug("Error while executing rightClickToClickElement"+ t.getMessage());
			return "Fail";
		}

		return "Pass";
	}

	public String closeBrowser(){
		log.debug("=============================");
		log.debug("Executing closeBrowser");
		try {
			if(driver != null){

				driver.close();
			}
		} catch (Throwable t) {
			// report error
			log.debug("Error while closing the browser -" + t.getMessage());
			return "Fail - browser close issue";
		}
		return "Pass";
	}

	public String quitBrowser(){
		log.debug("=============================");
		log.debug("Executing closeBrowser");
		try {
			if(driver != null){

				driver.quit();

				if(launchBrowser.equalsIgnoreCase("Safari") && System.getProperty("os.name").equals("Mac OS X")) {
					Thread.sleep(WAIT5SEC);
				}

			}
		} catch (Throwable t) {
			// report error
			log.debug("Error while closing the browser -" + t.getMessage());
			return "Fail - QUIT Browser issue";
		}
		return "Pass";
	}

	public String shiftToBrowserWindow(){
		log.debug("=============================");
		log.debug("Executing shiftToBrowserWindow");
		try{
			String data = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			int dataIntValue = Integer.parseInt(data);
			Object handle[] = driver.getWindowHandles().toArray();
			String popupWindosID = handle[dataIntValue].toString();
			driver.switchTo().window(popupWindosID);
			Thread.sleep(3000);
		}catch (Throwable t){
			log.debug("Error while passing control to another browser window -- " + t.getMessage());
			return "Fail - Window Not Found";
		}
		return "Pass";
	}

	public String switchToWindow_UsingBrowserTitle() {
		log.debug("====================================");
		log.debug("Executing switchToWindow_UsingTitle");
		try {
			String currentWindowHandle = driver.getWindowHandle();
			Set<String> handles = driver.getWindowHandles();

			String data = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);

			for (String handle : handles) {
				if(!handle.equals(currentWindowHandle)) {
					driver.switchTo().window(handle);
					log.debug(driver.getTitle());
					if(!driver.getTitle().equalsIgnoreCase(data)) {
						continue;
					}else {
						log.debug("Switched to "+data+" Window");
					}
				}
			}
		}catch(Throwable t) {
			log.debug("Error in switchToWindow- "+t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String removeIframes_Author() {
		log.debug("=============================");
		log.debug("Executing removeIframes_Author1");
		try {

			String url = driver.getCurrentUrl();
			log.debug("Current Url: " + url);

			url = url.replace("cf#/", "");
			log.debug("New Url: " + url);

			driver.get(url);

			driver.manage().window().maximize();

			try{
				Functions.handleTnCPopUp(driver, log, OR);				
			}catch(Throwable t){
				log.debug("Terms and condition did not came in Author Login");
			}
		} catch (Throwable t) {
			// report error
			log.debug("Error while Executing removeIframes_Author1 -" + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String navigateForward() {
		log.debug("=============================");
		log.debug("Executing NavigateForward");
		try{

			driver.navigate().forward();
		} catch (Throwable t) {
			log.debug("Error while navigating forward   -"  + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}


	public String navigateBackward() {
		log.debug("=============================");
		log.debug("Executing NavigateBackward");
		try{

			driver.navigate().back();
		} catch (Throwable t) {
			log.debug("Error while navigating backward   -"  + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}


	public String verifySearch(){
		log.debug("===============================");
		log.debug("Executing verifySearch");
		try{
			String searchText = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			int expectedCount = Integer.parseInt(testData.getCellData(currentTest, data_column_nameArr[1],testRepeat));
			List<WebElement> searchResults = getWebElements(OR,objectArr[0]);
			int actualCount = searchResults.size();
			boolean status = false;
			log.debug("Search Text: "+searchText);
			log.debug("Expected Count: "+expectedCount);
			for(int i=0; i<actualCount; i++){
				log.debug("Data-title: "+searchResults.get(i).getAttribute("data-title"));
				if((searchResults.get(i).getAttribute("data-title").contains(searchText)) && actualCount == expectedCount)
					status = true;
			}
			if(status)
				return "Pass";
			else
				return "Fail";
		}catch(Throwable t){
			log.debug("Error while executing verifySearch -" + objectArr[0]+t.getMessage());
			return "Fail";
		}
	}

	public String getAttributeValue(){
		log.debug("====================================");
		log.debug("Executing getAttributeValue");
		String data;
		try{
			String attribute = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat).trim();
			data = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getAttribute(attribute);
			log.debug("Return Value :" + data);
			testData.setCellData(currentTest, data_column_nameArr[0], 2, data );
			return "Pass";

		}catch(Throwable t){
			log.debug("Error in getAttributeValue -- " + objectArr[0]+t);
			return "Fail";
		}
	}


	public String refreshBrowser(){
		try{
			driver.navigate().refresh();

		}catch(Throwable t) {
			log.debug("Error while deselecting the checkbox -" + 
					t.getMessage());
			return "Fail";
		}
		return "Pass";
	}


	public String isWebElementPresent() {
		log.debug("====================================");
		log.debug("Executing isWebElementPresent");

		String expected = null;
		WebElement webElement = null;
		try {
			try {
				expected = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
				log.debug("Expected:" + expected);

				if(!(expected.contains("true") || expected.contains("false"))) {
					return "Fail- Debug Required";
				}

			}catch(Throwable t) {
				log.debug("Test Data Column is not present in controller sheet .Expected variable value :"+ expected);
				return "Fail- Debug Required";
			}

			try{
				webElement = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
				log.debug("WebElement: "+ webElement);
			}catch(Throwable e){
				webElement=null;
			}
			if (webElement == null) {
				if (expected.equalsIgnoreCase("true"))
					return "Fail -" + " Element not present";
				else
					return "Pass";
			}
			else{
				if (expected.equalsIgnoreCase("true"))
					return "Pass";
				else
					return "Fail -" + " Element should not be present";
			}
		}catch (Throwable t) {
			log.debug("Error while executing isWebElementPresent -"+ t.getMessage());
			return "Fail";
		}
	} 



	public String findElementInDAM() {
		log.debug("====================================");
		log.debug("Executing findElementInDAM");
		String expected = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
		WebElement webElement = null;
		int PageNumb,TotalPages=0;
		try {	
			try{ driver.findElement(By.xpath("//button[contains(@class,'page-first')]")).click();
			log.debug("Clicked first page arrow button");
			}catch(Throwable t)
			{
				log.debug("started from first page already");
			}
			do{ 
				String currentpage=driver.findElement(By.xpath("//tr[@class='x-toolbar-left-row']/descendant::input")).getAttribute("value");                
				PageNumb =Integer.parseInt(currentpage.trim());
				try{
					webElement =driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
					webElement.click();
					log.debug("element present on page : "+ PageNumb );
				}
				catch(Throwable x){ 
					String Total=driver.findElement(By.xpath("//tr[@class='x-toolbar-left-row']/descendant::div[contains(text(),'of ')]")).getText();
					String Array[]=Total.split(" ");
					TotalPages=Integer.parseInt(Array[1]);
					log.debug("element not present on page : "+ PageNumb + x.getMessage());
					driver.findElement(By.xpath("//button[contains(@class,'page-next')]")).click();
					Thread.sleep(WAIT2SEC);
				}	
				if(webElement!= null)
				{
					log.debug("element found on "+ PageNumb );
					break;
				}
			}
			while( PageNumb < TotalPages);

			if (webElement == null) {
				if (expected.equalsIgnoreCase("true"))
					return "Fail -" + " Element not present";
				else

					return "Pass";
			}
			else{
				if (expected.equalsIgnoreCase("true"))
					return "Pass";
				else
					return "Fail -" + " Element should not be present";
			}
		}catch (Throwable t) {
			log.debug("Error while executing findElementInDAM -"+ t.getMessage());
			return "Fail";
		}
	}

	public String DragDrop() {
		log.debug("=============================");
		log.debug("Executing DragDrop Keyword");

		try {
			System.out.println(objectArr[0]);
			String Dragdrop[] = objectArr[0].split(";");
			WebElement source = driver.findElement(By.xpath(OR.getProperty(Dragdrop[0])));
			System.out.println("source : " +source.getText());
			WebElement target = driver.findElement(By.xpath(OR.getProperty(Dragdrop[1])));
			System.out.println("source : " +target.getText());

			(new Actions(driver)).dragAndDrop(source, target).perform();

		} catch (Throwable t) {
			log.debug("Error while  dragging and dropping  -" + objectArr[0] +"."
					+ t.getMessage());
			return "Fail";
		}
		return "Pass" + objectArr[0];
	}

	public String doubleClick()
	{
		log.debug("=============================");
		log.debug("Executing doubleClick Keyword");
		try {
			WebElement obj = getWebElement(OR, objectArr[0]);
			(new Actions(driver)).doubleClick(obj).perform();
		} catch (Throwable t) {
			// report error
			log.debug("Error while double clicking on Object -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String rightClick()
	{
		log.debug("=============================");
		log.debug("Executing rightClick Keyword");
		try {
			WebElement obj = getWebElement(OR, objectArr[0]);
			(new Actions(driver)).contextClick(obj).perform();
		} catch (Throwable t) {
			// report error
			log.debug("Error while right clicking on Object -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String clickFund()
	{
		log.debug("=============================");
		log.debug("Executing clickFund Keyword");

		int i=0;

		try 
		{
			while(++i<4) {
				try{
					driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();			

					Thread.sleep(WAIT5SEC);
					WebElement view = driver.findElement(By.xpath(OR.getProperty("aims.global.viewButton.xpath")));
					String viewVal = view.getText();
					if(viewVal.equals("View Fund Profile"))
						return "Pass";
					else
						continue;
				}catch(NoSuchElementException e){
					System.out.println("Attempt : "+ (i+1)+ " failed");
					continue;
				}

			}

		}catch(Throwable t) {
			log.debug("An error has occurred while executing ClickFund keyword "+t.getMessage());
			return "Fail";
		}
		return "Fail";
	}




	public String ClickCarousel()
	{
		log.debug("=============================");
		log.debug("Executing ClickCarousel Keyword");

		int i=0;

		try {
			while(++i<4) {
				try{
					WebElement obj = getWebElement(OR, objectArr[0]);
					obj.click();
				}catch(NoSuchElementException e){
					log.error("Element not found--->"+e);
				}
				Thread.sleep(WAIT5SEC);
				if(!driver.getCurrentUrl().contains("playlist"))
					driver.navigate().refresh();
				else
					return "Pass";
				Thread.sleep(WAIT2SEC);
			}
			return "Fail";
		}catch(Throwable t) {
			log.debug("An error has occurred while executing ClickCarousel keyword "+t.getMessage());
			return "Fail";
		}
	}

	public String editXpathAndClick() {
		log.debug("=============================");
		log.debug("Executing editXpathAndClick Keyword");
		String editXpath=OR.getProperty(objectArr[0]);
		String replaceString=testData.getCellData(currentTest, data_column_nameArr[0],
				testRepeat);
		String finalEditUSerXpath=editXpath.replaceAll("xyz", replaceString);
		try {
			driver.findElement(By.xpath(finalEditUSerXpath)).click();
		} catch (Throwable t) {
			// report error
			log.debug("Error while clicking on link -" + objectArr[0]
					+ t.getMessage());
			return "Fail - Link Not Found";
		}
		return "Pass";

	}


	public String editXpathAndVerifyText() {
		log.debug("=============================");
		log.debug("Executing editXpathAndVerifyText Keyword");
		String expected = APPTEXT.getProperty(objectArr[0]);
		String editXpath=OR.getProperty(objectArr[0]);
		String userEmail=testData.getCellData(currentTest, data_column_nameArr[0],
				testRepeat);
		String finalEditUSerXpath=editXpath.replaceAll("xyz", userEmail);
		String actual=null;
		try {

			actual = driver.findElement(By.xpath(finalEditUSerXpath))
					.getText();
			Assert.assertEquals(expected.trim(), actual.trim());
		} catch (Throwable t) {
			// report error
			log.debug("Error in text - " + objectArr[0]);
			log.debug("Actual - " + actual);
			log.debug("Expected -" + expected);
			return "Fail";
		}
		return "Pass";

	}


	public String verifyTextBoxText() {
		log.debug("=============================");
		log.debug("Executing verifyText");

		String expected = testData.getCellData(currentTest, data_column_nameArr[0],
				testRepeat);
		String actual = driver.findElement(By.xpath(OR.getProperty(objectArr[0])))
				.getAttribute("value");
		log.debug("expected Text  -  " + expected);
		log.debug("actual Text  -  " + actual);
		try {
			Assert.assertEquals(expected.trim(), actual.trim());
		} catch (Throwable t) {
			// error
			log.debug("Error in text - " + objectArr[0]);
			log.debug("Actual - " + actual);
			log.debug("Expected -" + expected);
			return "Fail";
		}
		return "Pass";
	}


	public String verifyImage() {
		log.debug("=============================");
		log.debug("Executing verifyImage");
		String expectedImageName = APPTEXT.getProperty(objectArr[0]);
		String actualImageName=null;
		try{
			actualImageName = driver.findElement(
					By.xpath(OR.getProperty(objectArr[0]))).getAttribute("src");
		} catch (Throwable t) {
			// error
			log.debug("Error while finding the objectArr[0] - " + objectArr[0]);
			log.debug("Object- " + objectArr[0] + " not found");
			return "Fail";
		}

		log.debug("expectedImageName  -  " + expectedImageName);
		log.debug("actualImageName  -  " + actualImageName);
		try {
			Assert.assertEquals(true,
					actualImageName.trim().contains(expectedImageName.trim()));
		} catch (Throwable t) {
			log.debug("Error in text - " + objectArr[0]);
			log.debug("expectedImageName " + expectedImageName);
			log.debug("actualImageName " + actualImageName);
			return "Fail";
		}
		return "Pass";
	}

	public String verifyImageCss() {
		log.debug("=============================");
		log.debug("Executing verifyImageCss");
		String expectedImageName = APPTEXT.getProperty(objectArr[0]);
		String actualImageName =null;
		try{
			actualImageName = driver.findElement(
					By.xpath(OR.getProperty(objectArr[0]))).getCssValue(
							"background-image");
		} catch (Throwable t) {
			// error
			log.debug("Error while finding the objectArr[0] - " + objectArr[0]);
			log.debug("Object- " + objectArr[0] + " not found");
			return "Fail";
		}
		log.debug("expectedImageName  -  " + expectedImageName);
		log.debug("actualImageName  -  " + actualImageName);
		try {
			Assert.assertEquals(true,
					actualImageName.trim().contains(expectedImageName.trim()));
		} catch (Throwable t) {
			log.debug("Error in text - " + objectArr[0]);
			log.debug("expectedImageName " + expectedImageName);
			log.debug("actualImageName " + actualImageName);
			return "Fail";
		}
		return "Pass";
	}

	public String verifyText() {
		log.debug("=============================");
		log.debug("Executing verifyText");
		String expected = null,actual = null;
		try {
			expected = APPTEXT.getProperty(objectArr[0]);
			log.debug("expected Text  -  " + expected);
		}catch(Throwable e) {
			log.debug("expected Text  -  " + expected);
			log.debug("Property " + objectArr[0] +" missing from APPTEXT file or invalid property used.");
			return "Fail- Debug Required";
		}
		try {
			actual = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			log.debug("actual Text  -  " + actual);
		} catch (Throwable t) {
			log.debug("actual Text  -  " + actual);
			log.debug("Property " + objectArr[0] +" missing from OR file or invalid property used.");
			return "Fail- Debug Required";
		}

		if(actual.trim().equals(expected.trim()))
			return "Pass";
		else
			return "Fail";
	}


	public String verifyTextIgnoreCase() {
		log.debug("=============================");
		log.debug("Executing verifyTextIgnoreCase");
		String expected = APPTEXT.getProperty(objectArr[0]);
		String actual = null;
		try {
			actual = getWebElement(OR, objectArr[0]).getText();
		} catch (Throwable t) {
			// error
			log.debug("Error while finding the objectArr[0] - " + objectArr[0]);
			log.debug("Object- " + objectArr[0] + " not found");
			return "Fail";
		}
		log.debug("expected Text  -  " + expected);
		log.debug("actual Text  -  " + actual);

		if(actual.trim().equalsIgnoreCase(expected.trim())){
			return "Pass";
		}else {
			return "Fail";
		}

	}


	public String verifyText_linkText() {
		log.debug("=============================");
		log.debug("Executing verifyText_linkText");
		String expected = APPTEXT.getProperty(objectArr[0]);
		String actual = null;
		try {
			actual = driver.findElement(By.linkText(APPTEXT.getProperty(objectArr[0]))).getText();
		} catch (Throwable t) {
			// error
			log.debug("Error while finding the objectArr[0] - " + objectArr[0]);
			log.debug("Object- " + objectArr[0] + " not found");
			return "Fail";
		}
		log.debug("expected Text  -  " + expected);
		log.debug("actual Text  -  " + actual);
		try {
			Assert.assertEquals(expected.trim(), actual.trim());
		} catch (Throwable t) {
			// error
			log.debug("Error in text - " + objectArr[0]);
			log.debug("Actual - " + actual);
			log.debug("Expected -" + expected);
			return "Fail";
		}
		return "Pass";
	}

	public String verifyLinkText() {
		log.debug("=============================");
		log.debug("Executing verifyLinkText");
		String expected = APPTEXT.getProperty(objectArr[0]);
		String actual = null;
		try {
			WebElement element = driver.findElement(By.linkText("Logout"));
			actual = element.getText();
			System.out.println("******************************************");
			System.out.println("Actual values is: "+actual);
			System.out.println("******************************************");
		} catch (Throwable t) {
			// error
			log.debug("Error while finding the objectArr[0] - " + objectArr[0]);
			log.debug("Object- " + objectArr[0] + " not found");
			return "Fail";
		}
		log.debug("expected Text  -  " + expected);
		log.debug("actual Text  -  " + actual);
		try {
			Assert.assertEquals(expected.trim(), actual.trim());
		} catch (Throwable t) {
			// error
			log.debug("Error in text - " + objectArr[0]);
			log.debug("Actual - " + actual);
			log.debug("Expected -" + expected);
			return "Fail";
		}
		return "Pass";
	}


	public String verifyPartialText() {
		log.debug("=============================");
		log.debug("Executing verifyPartialText");
		String expected = APPTEXT.getProperty(objectArr[0]);
		String actual = null;
		try {
			actual = getWebElement(OR, objectArr[0]).getText();
		} catch (Throwable t) {
			// error
			log.debug("Error while finding the objectArr[0] - " + objectArr[0]);
			log.debug("Object- " + objectArr[0] + " not found");
			return "Fail";
		}
		log.debug("expected Text  -  " + expected);
		log.debug("actual Text  -  " + actual);
		try {
			if(actual.trim().toLowerCase().trim().contains(expected.toLowerCase().trim())){
				return "Pass";
			}
			else
				return "Fail";
		} catch (Throwable t) {
			// error
			log.debug("Error in text - " + objectArr[0]);
			log.debug("Actual - " + actual);
			log.debug("Expected -" + expected);
			return "Fail";
		}

	}

	public String verifyPartialText2() {
		//the keyword checks if the expected value contains the actual value
		log.debug("=============================");
		log.debug("Executing verifyPartialText2");
		String expected = APPTEXT.getProperty(objectArr[0]);
		String actual = null;
		try {
			actual = getWebElement(OR, objectArr[0]).getText();
		} catch (Throwable t) {
			// error
			log.debug("Error while finding the objectArr[0] - " + objectArr[0]);
			log.debug("Object- " + objectArr[0] + " not found");
			return "Fail";
		}
		log.debug("expected Text  -  " + expected);
		log.debug("actual Text  -  " + actual);
		try {
			if(expected.trim().contains(actual.trim())){
				return "Pass";
			}
			else
				return "Fail";
		} catch (Throwable t) {
			// error
			log.debug("Error in text - " + objectArr[0]);
			log.debug("Actual - " + actual);
			log.debug("Expected -" + expected);
			return "Fail";
		}

	}

	public String verifyPartialText_CaseSensitive() {
		log.debug("=============================");
		log.debug("Executing verifyPartialText_CaseSensitive");
		String expected = APPTEXT.getProperty(objectArr[0]);
		String actual = null;
		try {
			actual = getWebElement(OR, objectArr[0]).getText();
		} catch (Throwable t) {
			// error
			log.debug("Error while finding the objectArr[0] - " + objectArr[0]);
			log.debug("Object- " + objectArr[0] + " not found");
			return "Fail";
		}
		log.debug("expected Text  -  " + expected);
		log.debug("actual Text  -  " + actual);
		try {
			if(expected.contains(actual)){
				return "Pass";
			}
			else
				return "Fail";
		} catch (Throwable t) {
			// error
			log.debug("Error in text - " + objectArr[0]);
			log.debug("Actual - " + actual);
			log.debug("Expected -" + expected);
			return "Fail";
		}

	}


	public String verifyTooltip() {
		log.debug("=============================");
		log.debug("Executing verifyTooltip");
		String expectedTooltip = APPTEXT.getProperty(objectArr[0]);
		String actualTooltip =null;
		try{
			actualTooltip = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getAttribute("title").toString();
		} catch (Throwable t) {
			// error
			log.debug("Error while finding the objectArr[0] - " + objectArr[0]);
			log.debug("Object- " + objectArr[0] + " not found");
			return "Fail";
		}
		log.debug("expected Tooltip  -  " + expectedTooltip);
		log.debug("actual Tooltip  -  " + actualTooltip);
		try {
			Assert.assertEquals(expectedTooltip.trim(), actualTooltip.trim());
		} catch (Throwable t) {
			// error
			log.debug("Error in Tooltiptext - " + objectArr[0]);
			log.debug("Actual - " + actualTooltip);
			log.debug("Expected -" + expectedTooltip);
			return "Fail";
		}
		return "Pass";
	}

	public String selectvalidate() {
		log.debug("=============================");
		log.debug("Executing selectvalidate Keyword");
		// extract the test data
		//The expected data given in the TestData excel sheet should be separated by colon( : )
		String data = testData.getCellData(currentTest, data_column_nameArr[0],
				testRepeat);

		String[] setofdata = data.split(":");
		String[] dropdownListData;
		try {
			String listData = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			dropdownListData= listData.split("\n");
		} catch (Throwable t) {
			// report error
			log.debug("Error while validating for droplist list -" + objectArr[0]
					+ t.getMessage());
			return "Fail";
		}
		if (Arrays.asList(dropdownListData).containsAll(Arrays.asList(setofdata)))
		{
			return "Pass"; 
		}
		else{
			return "Fail, Expected data doesnot match with the data in drop down list";
		}

	}


	public String navigateURL() {
		log.debug("=============================");
		log.debug("Executing navigateURL");


		try{
			driver.navigate().to(OR.getProperty(objectArr[0]).toString());    

			log.debug("navigate completed");
		}catch(Throwable t){
			log.debug("error while navigating to the URL" + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String navigateNewEnv() {
		log.debug("=============================");
		log.debug("Executing navigateURL");


		try{

			String url = CONFIG.getProperty(objectArr[0]);
			String[] testDataUrl = url.split("/");

			String url1= testDataUrl[0]+"//" + CONFIG.getProperty(objectArr[1])+"@"+testDataUrl[2] ;

			for(int i=3; i<testDataUrl.length; i++){
				url1=url1 + "/" + testDataUrl[i];
			}

			driver.get(url1);

			log.debug("Navigation completed using navigateNewEnv keyword");
		}catch(Throwable t){
			log.debug("Error while navigating to the URL using navigateNewEnv keyword" + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}


	public String inputAndClickEnterKey() {
		log.debug("=============================");
		log.debug("Executing inputAndClickEnterKey Keyword");
		// extract the test data
		String data = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
		try {
			getWebElement(OR, objectArr[0]).sendKeys(data);
			log.debug("data inserted into the search box");

			if(!testBrowser.equals("Firefox")){
				getWebElement(OR, objectArr[0]).sendKeys(Keys.ENTER);
			}
			else{
				Actions action = new Actions(driver);
				action.sendKeys(Keys.ENTER).build().perform();
			}
			Thread.sleep(WAIT4SEC);
			log.debug("enter clicked");
			String title = driver.getTitle();
			log.debug("browser title is :" + title);
			if(!title.contains("Page Not Found")) {
				log.debug("arrived on search results page. The page title is :" + title);
				return "Pass";
			}
			else {
				log.debug("did not arrive on search results page. The page title is :" + title);
				return "Fail";
			}
		} catch (Throwable t) {
			// report error
			log.debug("Error while inputAndClickEnterKey -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}

	}

	public String verifySearchComponentPresent(){
		log.debug("=============================");
		log.debug("Executing verifySearchComponentPresent  keyword");

		try {
			String searchInput = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getAttribute("id").toString();
			if(searchInput.equalsIgnoreCase("search_input") || searchInput.equalsIgnoreCase("search_submit")){
				return "Pass";
			}
			else
				return "Fail";
		} catch (Throwable t) {
			log.debug("Error while executing  verifySearchComponentPresent- " + objectArr[0]);
			return "Fail";
		}
	}


	public String verifyPDFOverlay(){  
		//pagination check for all the overlay pages that has pagination in home page
		log.debug("=============================");
		log.debug("Executing verifyPDFOverlay  keyword");

		try{
			if(driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).isDisplayed()){
				driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
				Thread.sleep(WAIT5SEC);
				driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).click();

			}
		}catch(Throwable t) {
			log.debug("Error while executing verifyPDFOverlay- " + t);
			return "Pass";
		}
		return "Pass";
	}

	public String SolutionsProfile_FundsPagination(){  
		//pagination check for all the overlay pages that has pagination in home page
		log.debug("=============================");
		log.debug("Executing SolutionsProfile_FundsPagination  keyword");
		int totalPages=0;
		try{
			if(driver.findElement(By.xpath(OR.getProperty("PageNumberTotal2"))).isDisplayed()){
				totalPages = Integer.parseInt(driver.findElement(By.xpath(OR.getProperty("PageNumberTotal2"))).getText());
				if(totalPages > 1){
					int clickForwardCount=0;
					int clickBackwardCount=0;
					int passcount=0;

					try{
						//totalPages = Integer.parseInt(driver.findElement(By.xpath(OR.getProperty("PageNumberTotal"))).getText());
						System.out.println(totalPages);
						System.out.println(driver.findElement(By.xpath(OR.getProperty("PageNumberCurrent2"))).getText());
						int currentPage = Integer.parseInt(driver.findElement(By.xpath(OR.getProperty("PageNumberCurrent2"))).getText());

						System.out.println(currentPage);
						for(int click=1; click<totalPages; click++){
							driver.findElement(By.xpath(OR.getProperty("LinkNext2"))).click();
							int IncreasedcurrentPage=Integer.parseInt(driver.findElement(By.xpath(OR.getProperty("PageNumberCurrent2"))).getText());
							System.out.println(IncreasedcurrentPage);
							clickForwardCount++;
							Thread.sleep(WAIT2SEC);

						}
						if(clickForwardCount == (totalPages-1)){
							log.debug("total forward clicks : " + clickForwardCount);
							log.debug("total pages : " + totalPages);
							passcount=1;
							for(int click=1; click<totalPages; click++){
								driver.findElement(By.xpath(OR.getProperty("LinkPrevious2"))).click();
								clickBackwardCount++;
								Thread.sleep(WAIT2SEC);
							}

						}

						if(clickBackwardCount == (totalPages-1)){
							log.debug("total backward clicks : " + clickForwardCount);
							log.debug("total pages : " + totalPages);
							passcount=2;
						}

						if(passcount==2) 
							return "Pass";
						else
							return "Fail";
					}catch (Throwable t) {
						log.debug("Error while executing  SolutionsProfile_FundsPagination - " + objectArr[0]);
						return "Fail";
					}
				}//pagination loop
			}//main if loop

		}catch(Throwable t) {
			log.debug("Error while finding the pagination element- " + objectArr[0]);
			return "Fail";
		}
		return "Pass";
	}

	public String verifyPaginationSearchResultsPage(){  

		log.debug("=============================");
		log.debug("Executing verifyPaginationSearchResultsPage  keyword");
		int clickForwardCount=0;
		int clickBackwardCount=0;
		int passcount=0;
		try{
			String totalPagesString=driver.findElement(By.xpath(OR.getProperty("aims.SearchPageNumberTotal"))).getText();
			totalPagesString = totalPagesString.trim();
			//System.out.println(totalPagesString);

			int totalPages = Integer.parseInt(totalPagesString);
			System.out.println(totalPages);

			System.out.println(driver.findElement(By.xpath(OR.getProperty("aims.SearchPageNumberCurrent"))).getText());
			int currentPage = Integer.parseInt(driver.findElement(By.xpath(OR.getProperty("aims.SearchPageNumberCurrent"))).getText());
			System.out.println(currentPage);

			for(int click=1; click<totalPages; click++){
				driver.findElement(By.xpath(OR.getProperty("aims.SearchLinkNext"))).click();
				int IncreasedcurrentPage=Integer.parseInt(driver.findElement(By.xpath(OR.getProperty("aims.SearchPageNumberCurrent"))).getText());
				System.out.println(IncreasedcurrentPage);
				clickForwardCount++;
				Thread.sleep(WAIT5SEC);

			}
			if(clickForwardCount == (totalPages-1)){
				log.debug("total forward clicks : " + clickForwardCount);
				log.debug("total pages : " + totalPages);
				passcount=1;
				for(int click=1; click<totalPages; click++){
					driver.findElement(By.xpath(OR.getProperty("aims.SearchLinkPrevious"))).click();
					clickBackwardCount++;
					Thread.sleep(WAIT2SEC);

				}

			}

			if(clickBackwardCount == (totalPages-1)){
				log.debug("total backward clicks : " + clickForwardCount);
				log.debug("total pages : " + totalPages);
				passcount=2;
			}

			if(passcount==2)
				return "Pass";
			else
				return "Fail";
		}catch (Throwable t) {
			log.debug("Error while executing  verifyPaginationSearchResultsPage - " + t.getMessage());
			return "Fail";
		}
	}

	public String verifyPaginationSearchResultsPage_Workspace(){  

		log.debug("=============================");
		log.debug("Executing verifyPaginationSearchResultsPage  keyword");
		int clickForwardCount=0;
		int clickBackwardCount=0;
		int passcount=0;
		try{
			if(driver.findElement(By.xpath(OR.getProperty("Workspace_PageNumberTotal"))).isDisplayed()){

				String totalPagesString=driver.findElement(By.xpath(OR.getProperty("Workspace_PageNumberTotal"))).getText();
				totalPagesString = totalPagesString.trim();
				System.out.println(totalPagesString);
				String temp[]= totalPagesString.split(" ");
				int totalPages = Integer.parseInt(temp[1]);
				System.out.println(totalPages);

				System.out.println(driver.findElement(By.xpath(OR.getProperty("Workspace_PageNumberCurrent"))).getText());
				int currentPage = Integer.parseInt(driver.findElement(By.xpath(OR.getProperty("Workspace_PageNumberCurrent"))).getText());
				System.out.println(currentPage);

				for(int click=1; click<totalPages; click++){
					driver.findElement(By.xpath(OR.getProperty("Workspace_LinkNext"))).click();
					int IncreasedcurrentPage=Integer.parseInt(driver.findElement(By.xpath(OR.getProperty("Workspace_PageNumberCurrent"))).getText());
					System.out.println(IncreasedcurrentPage);
					clickForwardCount++;
					Thread.sleep(WAIT2SEC);

				}
				if(clickForwardCount == (totalPages-1)){
					log.debug("total forward clicks : " + clickForwardCount);
					log.debug("total pages : " + totalPages);
					passcount=1;
					for(int click=1; click<totalPages; click++){
						driver.findElement(By.xpath(OR.getProperty("Workspace_LinkPrevious"))).click();
						clickBackwardCount++;
						Thread.sleep(WAIT2SEC);
					}

				}

				if(clickBackwardCount == (totalPages-1)){
					log.debug("total backward clicks : " + clickForwardCount);
					log.debug("total pages : " + totalPages);
					passcount=2;
				}

				if(passcount==2)
					return "Pass";
				else
					return "Fail";
			}//topmost if loop
			else
				return "Pass";
		}catch (Throwable t) {
			log.debug("Error while executing  verifyPaginationSearchResultsPage - " + t.getMessage());
			return "Fail";
		}
	}


	public String SolutionsProfile_SearchResults() {
		log.debug("=============================");
		log.debug("Executing SolutionsProfile_SearchResults Keyword");
		// extract the test data
		String data = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
		String rowData;
		String flag="true";
		try {
			List<WebElement> search = driver.findElements(By.tagName("tbody"));
			System.out.println("tbody "+search.size());
			Iterator<WebElement> i = search.iterator();
			outer:
				while(i.hasNext()){
					WebElement e = i.next();
					if(e.getAttribute("id").contains("list-target")){
						List<WebElement> s2 = e.findElements(By.tagName("tr"));
						Iterator<WebElement> i2 = s2.iterator();
						while(i2.hasNext()){
							WebElement e2 = i2.next();
							if(e2.getAttribute("class").contains("fund") || e2.getAttribute("class").contains("fund alt")){
								log.debug("Row Data : " + e2.getText());
								rowData = e2.getText();
								rowData = rowData.toLowerCase();
								data = data.toLowerCase();
								if(! rowData.contains(data)){
									log.debug("actual Data : " + data);
									log.debug("Row Data : " + rowData);
									flag="false";
									break outer;
								}

							}
						}
					}
				}
			if(flag.equalsIgnoreCase("false"))
				return "Fail";
			else
				return "Pass";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing SolutionsProfile_SearchResults -" +  t.getMessage());
			return "Fail";
		}
	}

	public String SolutionsProfile_FundLink() {
		log.debug("=============================");
		log.debug("Executing SolutionsProfile_FundLink Keyword");
		String flag="true";
		try {
			String linkText=driver.findElement(By.xpath(OR.getProperty("FundLink"))).getText();
			driver.findElement(By.xpath(OR.getProperty("FundLink"))).click();
			Thread.sleep(WAIT3SEC);
			String titleText=driver.findElement(By.xpath(OR.getProperty("FundPageTitle"))).getText();
			if(! titleText.equalsIgnoreCase("FUND PROFILE")){
				flag="false";
			}
			if(flag.equalsIgnoreCase("false")){
				log.debug("linkText :" + linkText);
				log.debug("titleText :" + titleText);
				return "Fail";
			}
			else
				return "Pass";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing SolutionsProfile_FundLink -" +  t.getMessage());
			return "Fail";
		}
	}

	public String MyPortfolio_FundLink() {
		log.debug("=============================");
		log.debug("Executing MyPortfolio_FundLink Keyword");
		String flag="true";
		try {
			String linkText=driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.fundLink.xpath"))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.linkText("View Fund Profile")).click();
			Thread.sleep(WAIT5SEC);
			String titleText=driver.findElement(By.xpath(OR.getProperty("aims.fundProfile.title.xpath"))).getText();
			if(! linkText.equalsIgnoreCase(titleText)){
				flag="false";
			}
			if(flag.equalsIgnoreCase("false")){
				log.debug("linkText :" + linkText);
				log.debug("titleText :" + titleText);
				return "Fail";
			}
			else
				return "Pass";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyPortfolio_FundLink -" +  t.getMessage());
			return "Fail";
		}
	}

	public String SolutionsProfile_FundLinkCarousel() {
		log.debug("=============================");
		log.debug("Executing SolutionsProfile_FundLinkCarousel Keyword");
		String flag="true";
		try {
			String linkText=driver.findElement(By.xpath(OR.getProperty("FundLinkCarousel"))).getText();
			System.out.println("linkText - "+linkText);
			driver.findElement(By.xpath(OR.getProperty("FundLinkCarousel"))).click();
			Thread.sleep(WAIT3SEC);
			String titleText=driver.findElement(By.xpath(OR.getProperty("FundPageTitle"))).getText();
			if(! titleText.equalsIgnoreCase("FUND PROFILE")){
				flag="false";
			}
			/*String titleText=driver.findElement(By.xpath(OR.getProperty("FundName"))).getText();
			if(! linkText.equalsIgnoreCase(titleText)){
				flag="false";
			}*/
			if(flag.equalsIgnoreCase("false")){
				log.debug("linkText :" + linkText);
				log.debug("titleText :" + titleText);
				return "Fail";
			}
			else
				return "Pass";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing SolutionsProfile_FundLinkCarousel -" +  t.getMessage());
			return "Fail";
		}
	}

	public String SolutionsProfile_ManagerLink() {
		log.debug("=============================");
		log.debug("Executing SolutionsProfile_ManagerLink Keyword");
		String flag="true";
		try {
			String linkText=driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.managerLink.xpath"))).getText();
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.managerLink.xpath"))).click();
			Thread.sleep(WAIT8SEC);
			String titleText=driver.findElement(By.xpath(OR.getProperty("aims.managerProfile.HeaderTitle"))).getText();
			if(! linkText.equals(titleText)){
				flag="false";
			}
			if(flag.equalsIgnoreCase("false")){
				log.debug("linkText :" + linkText);
				log.debug("titleText :" + titleText);
				return "Fail";
			}
			else
				return "Pass";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing SolutionsProfile_ManagerLink -" +  t.getMessage());
			return "Fail";
		}
	}

	public int getTotalItemsinDropdown(String elementXpath) {
		int  actualItems=0;
		try{

			List<WebElement> listObject = driver.findElements(By.xpath(elementXpath));
			System.out.println("list size : " + listObject.size());
			actualItems = listObject.size() + 1;

		} catch (Throwable t) {
			log.debug("error finding element ");
		}
		return actualItems;
	}


	public String verifyNumberOfFunds_solutionProfile(){  
		//solution profile funds section check number in title, dropdown and last page
		log.debug("=============================");
		log.debug("Executing verifyNumberOfFunds  keyword");
		int fundsDropdownCount=0;
		try {
			String fundsXpath = driver.findElement(By.xpath(OR.getProperty("SolutionProfile_Funds"))).getText();
			int funds = Integer.parseInt(fundsXpath);
			log.debug("SolutionProfile_Funds :" + funds);

			List<WebElement> fundsDropdown=driver.findElements(By.tagName("ul"));
			Iterator<WebElement> fundNames= fundsDropdown.iterator();
			while(fundNames.hasNext()){
				WebElement currentFundName = fundNames.next();
				if(currentFundName.getAttribute("class").contains("dk_options_inner")){
					List<WebElement> totalFunds=currentFundName.findElements(By.tagName("li"));
					Iterator<WebElement> fundsCount= totalFunds.iterator();
					while(fundsCount.hasNext()){
						WebElement currentFund = fundsCount.next();
						fundsDropdownCount++;
						log.debug("current fund count is  :  " + fundsDropdownCount + "   fund name  :" + currentFund.getText());
					}
				}
			}

			log.debug("fundsDropdownCount is  : " + fundsDropdownCount);
			String paginationLast = driver.findElement(By.xpath(OR.getProperty("SolutionProfile_PaginationLast"))).getText();
			System.out.println(paginationLast);
			int lastpage = Integer.parseInt(paginationLast);
			System.out.println(lastpage);
			log.debug("lastpage is : " + lastpage);
			//if( funds==lastpage && funds==fundsDropdownCount)
			if( funds==lastpage )
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			log.debug("Error while executing  verifyNumberOfFunds - " + objectArr[0]);
			return "Fail";
		}
	}


	public String isDisplayedandClick(){
		log.debug("====================================");
		log.debug("Executing isDisplayedandClick");

		try{
			//objectStatus =
			if(getWebElement(OR, objectArr[0]).isDisplayed())
			{
				getWebElement(OR, objectArr[0]).click();
			}

		}catch(Throwable t){
			log.debug("Error in isEnabled -- " + objectArr[0]);
			return "Fail";
		}
		return "Pass";
	}


	public String managersVerifyAssetData() {
		log.debug("=============================");
		log.debug("managersVerifyAssetData");

		boolean result = true;

		try {

			try{
				Functions.waitForElementClickable(driver, log, objectArr[1]);
			}catch(ArrayIndexOutOfBoundsException e) {

			}

			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.assetDropdown.xpath"))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
			String assetName=APPTEXT.getProperty(objectArr[0]);
			Thread.sleep(WAIT3SEC);
			driver.findElement(By.xpath("//*[@id='strategy']/div/label[1]")).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.submitButton.xpath"))).click();
			Thread.sleep(WAIT5SEC);

			log.debug("Asset: "+assetName);

			List<WebElement> multiList;
			List<WebElement> rows;

			try {
				rows = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]"));
			}catch(Throwable t) {
				return "Fail-Data not present";
			}

			for(int i=1;i<=rows.size();i++) {			 
				log.debug("\nRow "+i);

				log.debug("Actual Asset: "+driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell ac-cell multi-check')]/span")).getAttribute("innerHTML"));

				if(driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell ac-cell multi-check')]/span")).getAttribute("innerHTML").equals("Multi")) {
					multiList = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell ac-cell multi-check')]/ul/li"));

					log.debug("Multilist Size "+ multiList.size());
					result=false;

					for(int j=0;j<multiList.size();j++) {
						log.debug("List Element: "+multiList.get(j).getAttribute("innerHTML"));

						if(multiList.get(j).getAttribute("innerHTML").contains(assetName)) {
							result = true;
							break;
						}
					}
				}else {
					if(!driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell ac-cell multi-check')]/span")).getAttribute("innerHTML").equals(assetName)) {
						return "Fail";
					}
				}

				if(!result) {
					return "Fail";
				}
			}

			if(result) {
				return "Pass";
			}else {
				return "Fail";
			}

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing managersVerifyAssetData -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}

	}

	public String managersVerifyStrategyData() {
		log.debug("=============================");
		log.debug("managersVerifyStrategyData");
		boolean result=true;

		try {

			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.assetDropdown.xpath"))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
			Thread.sleep(WAIT5SEC);
			String strategyOption = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			Thread.sleep(WAIT3SEC);
			driver.findElement(By.xpath(OR.getProperty(strategyOption))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.submitButton.xpath"))).click();
			Thread.sleep(WAIT5SEC);
			try{
				Functions.waitForElementClickable(driver, log, "aims.managersFunds.goButton.xpath");
			}catch(ArrayIndexOutOfBoundsException e) {

			}
			String strategyOptionText=APPTEXT.getProperty(strategyOption);

			log.debug("Strategy: "+strategyOptionText);

			List<WebElement> multiList;
			List<WebElement> rows;

			try {
				rows = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]"));
			}catch(Throwable t) {
				return "Fail-Data not present";
			}

			for(int i=1;i<=rows.size();i++) {			 
				log.debug("\nRow "+i);

				log.debug("Actual Strategy: "+driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell strat-cell multi-check')]/span")).getAttribute("innerHTML"));

				if(driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell strat-cell multi-check')]/span")).getAttribute("innerHTML").equals("Multi")) {
					multiList = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell strat-cell multi-check')]/ul/li"));

					log.debug("Multilist Size "+ multiList.size());
					result=false;

					for(int j=0;j<multiList.size();j++) {
						log.debug("List Element: "+multiList.get(j).getAttribute("innerHTML"));

						if(multiList.get(j).getAttribute("innerHTML").contains(strategyOptionText)) {
							result = true;
							break;
						}
					}
				}else {
					if(!driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell strat-cell multi-check')]/span")).getAttribute("innerHTML").equals(strategyOptionText)) {
						return "Fail";
					}
				}

				if(!result) {
					return "Fail";
				}
			}

			if(result) {
				return "Pass";
			}else {
				return "Fail";
			}

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing managersVerifyStrategyData -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}


	public String managersVerifyGeographyData() {
		log.debug("=============================");
		log.debug("managersVerifyGeographyData");

		String[] Africa =   { "Africa" };
		String[] Americas = { "Latin Americas" };
		String[] Asia =     { "China", "Greater China", "India", "Indonesia", "Sri Lanka","Japan", "Korea", "Malaysia", "Philippines", "Singapore", "Saudi Arabia", "Micronesia" };        
		String[] EmergingMarkets =  { "Global Emerging Markets" };
		String[] Europe =   { "Germany " , "Russia", "Sweden", "Austria", "Portugal", "Switzerland", "Denmark", "Eastern Europe", "Italy", "Pan Europe" };
		String[] Global =   { "Pan Europe", "Latin Americas", "Africa", "Europe", "Melanesia", "Eastern Europe", "Micronesia", "Asia", "Continental Europe", "Germany", "Global", "Malaysia", "Northern Europe", "Polynesia", "Thailand", "UK", "United States", "Global Emerging Markets" };
		String[] UnitedStates =   { "United States" };

		String asStringAfrica=Arrays.toString(Africa);
		String asStringAmericas=Arrays.toString(Americas);
		String asStringAsia=Arrays.toString(Asia);
		String asStringEmergingMarkets=Arrays.toString(EmergingMarkets);
		String asStringEurope=Arrays.toString(Europe);
		String asStringGlobal=Arrays.toString(Global);
		String asStringUnitedStates=Arrays.toString(UnitedStates);

		boolean result=false;
		boolean noData=false;

		try {
			try{
				Functions.waitForElementClickable(driver, log, objectArr[1]);
				Thread.sleep(WAIT2SEC);
			}catch(ArrayIndexOutOfBoundsException e) {

			}
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.assetDropdown.xpath"))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
			Thread.sleep(WAIT5SEC);
			String strategyOption = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			Thread.sleep(WAIT3SEC);
			driver.findElement(By.xpath(OR.getProperty(strategyOption))).click();
			Thread.sleep(WAIT3SEC);
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.submitButton.xpath"))).click();
			Thread.sleep(WAIT5SEC);
			String strategyOptionText=APPTEXT.getProperty(strategyOption);
			System.out.println("strategyOptionText  :" + strategyOptionText );
			int occurenceFailCount=0;

			List<WebElement> multiList;
			List<WebElement> rows;

			try {
				rows = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]"));
				//System.out.println("number of rows : " +rows.size());
			}catch(Throwable t) {
				noData=true;
				return "Fail-Data not present";
			}

			for(int i=1;i<=rows.size();i++) {			 
				log.debug("\nRow "+i);

				if(driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell geog-cell multi-check')]/span")).getAttribute("innerHTML").equals("Multi")) {
					multiList = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell geog-cell multi-check')]/ul/li"));

					log.debug("Multilist Size "+ multiList.size());
					result=false;

					for(int j=0;j<multiList.size();j++) {
						log.debug("List Element: "+multiList.get(j).getAttribute("innerHTML"));
						String s = multiList.get(j).getAttribute("innerHTML");
						result = false;
						int trueCount=0;

						if (strategyOptionText.equalsIgnoreCase("Africa")) {

							if (asStringAfrica.contains(s)) {
								result = true;
								trueCount++;
								break;

							}
						}
						if (strategyOptionText.equalsIgnoreCase("Americas")) {


							if(asStringAmericas.contains(s))
							{
								result = true;
								trueCount++;
								break;

							}
						}
						if (strategyOptionText.equalsIgnoreCase("Asia")) {

							if(asStringAsia.contains(s))
							{
								result = true;
								trueCount++;
								break;

							}
						}
						if (strategyOptionText.equalsIgnoreCase("Emerging Markets")) {

							if(asStringEmergingMarkets.contains(s))
							{
								result = true;
								trueCount++;
								break;

							}
						}
						if (strategyOptionText.equalsIgnoreCase("Europe")) {

							if(asStringEurope.contains(s))
							{
								result = true;
								trueCount++;
								break;

							}
						}
						if (strategyOptionText.equalsIgnoreCase("Global")) {
							if(asStringGlobal.contains(s))
							{
								result = true;
								trueCount++;
								break;

							}
						}
						if (strategyOptionText.equalsIgnoreCase("United States")) {
							if(asStringUnitedStates.contains(s))
							{
								result = true;
								trueCount++;
								break;

							}
						}

						if((trueCount==0) && (j == multiList.size())){
							occurenceFailCount++;
							break ;

						}


					}
				}else {
					if(!driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell geog-cell multi-check')]/span")).getAttribute("innerHTML").equals(strategyOptionText)) {
						//System.out.println("not Multi : " + driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell geog-cell multi-check')]/span")).getAttribute("innerHTML"));
						result = false;
					}
				}


			}


			if(result==true && occurenceFailCount<1)
				return "Pass";
			if(noData)
				return "Fail - No Data";
			else
				return "Fail";
		} catch (Throwable t) {
			// report error
			if(noData)
				return "Fail - No Data";
			log.debug("Error while executing managersVerifyGeographyData -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}	



	public String managersVerifySubSearchResults() {
		log.debug("=============================");
		log.debug("managersVerifySubSearchResults");
		List<String> names=new ArrayList<String>();

		boolean result=false;
		boolean noData=false;

		try {

			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.assetDropdown.xpath"))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
			String managerName = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			Thread.sleep(WAIT3SEC);
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.subSearchInputBox.xpath"))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.subSearchInputBox.xpath"))).sendKeys(managerName);
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.goButton.xpath"))).click();
			Thread.sleep(WAIT3SEC);
			//System.out.println("manager name  :" + managerName );

			List<WebElement> multiList;
			List<WebElement> rows;

			try {
				rows = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]"));
				//System.out.println("number of rows : " +rows.size());

			}catch(Throwable t) {
				noData=true;
				return "Fail-Data not present";
			}

			for(int i=1;i<=rows.size();i++) {			 
				log.debug("\nRow "+i);

				multiList = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell name-cell')]/a"));
				log.debug("Multilist Size "+ multiList.size());
				for(int j=0;j<multiList.size();j++) {
					log.debug("List Element: "+multiList.get(j).getAttribute("innerHTML"));
					if(!multiList.get(j).getAttribute("innerHTML").isEmpty()) {
						names.add(multiList.get(j).getAttribute("innerHTML"));
					}
				}
			}
			//System.out.println(names.size());

			for(int k=0; k<names.size(); k++){

				System.out.println("names :" + names.get(k));
				String s = names.get(k).toString().trim().toLowerCase();
				if( ! (s.contains(managerName))){
					log.debug("expected  : " +managerName + "  actual : " +  s);
					result=false;
					break;
				}
				else
					result=true;
			}

			if(result)
				return "Pass";
			if(noData)
				return "Fail - No Data";
			else
				return "Fail";
		} catch (Throwable t) {
			// report error
			if(noData)
				return "Fail - No Data";
			log.debug("Error while executing managersVerifySubSearchResults -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}


	public String managersVerifyAUMvalue() {
		log.debug("=============================");
		log.debug("managersVerifyAUMvalue");
		List<String> assetValue=new ArrayList<String>();
		boolean result=false;
		boolean noData=false;

		double minValue = 0;
		double maxValue = 0;

		try {
			String minObject=testData.getCellData(currentTest, "Min",testRepeat);
			String maxObject=testData.getCellData(currentTest, "Max",testRepeat);
			log.debug(minObject + "====="+ maxObject);
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.assetDropdown.xpath"))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();

			Thread.sleep(WAIT3SEC);
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.aumMin.xpath"))).click();
			Thread.sleep(WAIT2SEC);
			String minString=driver.findElement(By.xpath(OR.getProperty(minObject))).getText();
			driver.findElement(By.xpath(OR.getProperty(minObject))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.aumMax.xpath"))).click();
			Thread.sleep(WAIT2SEC);
			String maxString=driver.findElement(By.xpath(OR.getProperty(maxObject))).getText();
			driver.findElement(By.xpath(OR.getProperty(maxObject))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.submitButton.xpath"))).click();
			Thread.sleep(WAIT5SEC);

			minValue=Functions.getDoubleAUMval(minString);
			maxValue=Functions.getDoubleAUMval(maxString);

			log.debug(minValue + "  doublevalues "+ maxValue );

			List<WebElement> multiList;
			List<WebElement> rows;

			try {
				rows = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]"));
			}catch(Throwable t) {
				noData=true;
				return "Fail-Data not present";
			}

			for(int i=1;i<=rows.size();i++) {			 
				log.debug("\nRow "+i);
				multiList = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell aum-cell')]/span"));
				log.debug("Multilist Size "+ multiList.size());
				for(int j=0;j<multiList.size();j++) {
					log.debug("List Element: "+multiList.get(j).getAttribute("innerHTML"));
					if(!multiList.get(j).getAttribute("innerHTML").isEmpty()) {
						assetValue.add(multiList.get(j).getAttribute("innerHTML"));
					}
				}
			}

			//System.out.println(assetValue.size());

			for(int k=0; k<assetValue.size(); k++){

				System.out.println("assetValue :" + assetValue.get(k));
				String s = assetValue.get(k).toString().trim().toLowerCase();
				double current = Functions.getDoubleAUMval(s);
				if( ! (current>=minValue && current<=maxValue)){
					//System.out.println("assetValue  "+ current + "   not in range :"+ minValue + " - "+ maxValue);
					log.debug("assetValue  "+ current + "   not in range :"+ minValue + " - "+ maxValue);
					result=false;
					break;
				}
				else{
					//System.out.println("assetValue : "+ current + " is in the range :"+ minValue + " --"+ maxValue);
					log.debug("assetValue : "+ current + " is in the range :"+ minValue + " --"+ maxValue);
					result=true;
				}
			}

			if(result)
				return "Pass";
			if(noData)
				return "Fail- No Data";
			else
				return "Fail";

		} catch (Throwable t) {
			log.debug("Error while executing managersVerifyAUMvalue -" + objectArr[0]+ t.getMessage());
			if(noData)
				return "Fail- No Data";
			else
				return "Fail";

		}
	}

	public String managersVerifySortNameAsc() {
		log.debug("=============================");
		log.debug("managersVerifySortName");
		List<String> names=new ArrayList<String>();

		List<String> namesSorted=new ArrayList<String>();
		boolean result=false;
		boolean noData=false;

		try {
			String rowsLocator = OR.getProperty(objectArr[0]);
			String dragThumbLocator = OR.getProperty(objectArr[1]);

			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.assetDropdown.xpath"))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty(objectArr[3]))).click();
			Thread.sleep(WAIT2SEC);
			//driver.findElement(By.xpath("//*[@id='strategy']/div/label[1]")).click();
			//Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.submitButton.xpath"))).click();
			Thread.sleep(WAIT2SEC);

			String totalRowCount = driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).getText();
			String totalRowCountTrimed = totalRowCount.trim();
			int totalRowInt = Integer.parseInt(totalRowCountTrimed);
			log.debug("total results is" + totalRowInt);
			boolean present =driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).isDisplayed();

			if(present){
				Functions.dragTillAllRowsLoaded(driver, log, rowsLocator, dragThumbLocator, totalRowInt, 100);
			}

			List<WebElement> multiList;
			List<WebElement> rows;

			try {
				rows = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]"));
				log.debug("number of rows : " +rows.size());
			}catch(Throwable t) {
				noData=true;
				return "Fail-Data not present";
			}

			for(int i=1;i<=rows.size();i++) {			 
				log.debug("\nRow "+i);

				multiList = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell name-cell')]/a"));
				log.debug("Multilist Size "+ multiList.size());
				for(int j=0;j<multiList.size();j++) {
					log.debug("List Element: "+multiList.get(j).getAttribute("innerHTML"));
					if(!multiList.get(j).getAttribute("innerHTML").isEmpty()) {
						names.add(multiList.get(j).getAttribute("innerHTML"));
					}
				}
			}

			namesSorted.addAll(names);
			Collections.sort(namesSorted, String.CASE_INSENSITIVE_ORDER);

			for(int k=0; k<names.size(); k++){

				log.debug("names :" + names.get(k)+ "names sorted  "+ namesSorted.get(k));
				String s = names.get(k).toString().trim();
				String s2 = namesSorted.get(k).toString().trim();

				if(!(s.equalsIgnoreCase(s2)))
				{
					log.debug("name before sort :  " + s + "  name after sort :"  + s2);
					result=false;
					break;
				}
				else
					result=true;
			}

			if(result==true)
				return "Pass";
			if(noData)
				return "Fail - No Data";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			if(noData)
				return "Fail - No Data";
			log.debug("Error while executing managersVerifySortName -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String managersVerifySortNameDesc() {
		log.debug("=============================");
		log.debug("Executing managersVerifySortNameDesc");
		List<String> names=new ArrayList<String>();
		List<String> names2=new ArrayList<String>();
		boolean noData=false;
		boolean result=false;

		try {
			String rowsLocator = OR.getProperty(objectArr[0]);
			String dragThumbLocator = OR.getProperty(objectArr[1]);

			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.assetDropdown.xpath"))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty(objectArr[3]))).click();
			Thread.sleep(WAIT2SEC);
			//driver.findElement(By.xpath("//*[@id='strategy']/div/label[1]")).click();
			//Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.submitButton.xpath"))).click();
			Thread.sleep(WAIT2SEC);

			String totalRowCount = driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).getText();
			String totalRowCountTrimed = totalRowCount.trim();
			int totalRowInt = Integer.parseInt(totalRowCountTrimed);
			log.debug("total results is" + totalRowInt);
			boolean present =driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).isDisplayed();

			if(present){
				Functions.dragTillAllRowsLoaded(driver, log, rowsLocator, dragThumbLocator, totalRowInt, 100);
			}

			List<WebElement> multiList;
			List<WebElement> rows;

			try {
				rows = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]"));
				log.debug("number of rows : " +rows.size());
			}catch(Throwable t) {
				noData=true;
				return "Fail-Data not present";
			}

			for(int i=1;i<=rows.size();i++) {			 
				log.debug("\nRow "+i);

				multiList = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell name-cell')]/a"));

				for(int j=0;j<multiList.size();j++) {
					log.debug("List Element: "+multiList.get(j).getAttribute("innerHTML"));
					if(!multiList.get(j).getAttribute("innerHTML").isEmpty()) {
						names.add(multiList.get(j).getAttribute("innerHTML"));
					}
				}
			}

			Collections.reverse(names);
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.sortName.xpath"))).click();
			Thread.sleep(WAIT3SEC);
			if(present){
				Functions.dragTillAllRowsLoaded(driver, log, rowsLocator, dragThumbLocator, totalRowInt, 100);
			}
			List<WebElement> multiList2;
			List<WebElement> rows2;

			try {
				rows2 = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]"));
				log.debug("number of rows : " +rows2.size());
			}catch(Throwable t) {
				noData=true;
				return "Fail-Data not present";
			}

			for(int i=1;i<=rows2.size();i++) {			 
				log.debug("\nRow2 "+i);

				multiList2 = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell name-cell')]/a"));
				log.debug("Multilist Size "+ multiList2.size());
				for(int j=0;j<multiList2.size();j++) {
					log.debug("List Element: "+multiList2.get(j).getAttribute("innerHTML"));
					if(!multiList2.get(j).getAttribute("innerHTML").isEmpty()) {
						names2.add(multiList2.get(j).getAttribute("innerHTML"));
					}
				}
			}

			for(int k=0; k<names.size(); k++){

				log.debug("names :" + names.get(k)+ "names2: " + names2.get(k));
				String s = names.get(k).toString().trim();
				String s2 = names2.get(k).toString().trim();

				if(!(s.equalsIgnoreCase(s2)))
				{
					log.debug("name before sort :  " + s + "  name after sort :"  + s2);
					result=false;
					break;
				}
				else
					result=true;
			}

			if(result==true)
				return "Pass";
			if(noData)
				return "Fail - No Data";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			if(noData)
				return "Fail - No Data";
			log.debug("Error while executing managersVerifySortNameDesc -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String managersVerifySortAUMAsc() {
		log.debug("=============================");
		log.debug("managersVerifySortAUMAsc");

		List<String> assetValue=new ArrayList<String>();

		boolean result=false;
		boolean noData=false;

		try {
			String rowsLocator = OR.getProperty(objectArr[0]);
			String dragThumbLocator = OR.getProperty(objectArr[1]);
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.assetDropdown.xpath"))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty(objectArr[3]))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.sortAUM.xpath"))).click();
			Thread.sleep(WAIT3SEC);
			//driver.findElement(By.xpath("//*[@id='strategy']/div/label[1]")).click();
			//Thread.sleep(WAIT2SEC);
			String totalRowCount = driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).getText();
			String totalRowCountTrimed = totalRowCount.trim();
			int totalRowInt = Integer.parseInt(totalRowCountTrimed);
			log.debug("total results is" + totalRowInt);
			boolean present =driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).isDisplayed();

			if(present){
				Functions.dragTillAllRowsLoaded(driver, log, rowsLocator, dragThumbLocator, totalRowInt, 100);
			}


			List<WebElement> multiList;
			List<WebElement> rows;

			try {
				rows = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]"));
			}catch(Throwable t) {
				noData=true;
				return "Fail-Data not present";
			}

			for(int i=1;i<=rows.size();i++) {			 
				log.debug("\nRow "+i);
				multiList = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell aum-cell')]/span"));
				log.debug("Multilist Size "+ multiList.size());
				for(int j=0;j<multiList.size();j++) {
					log.debug("List Element: "+multiList.get(j).getAttribute("innerHTML"));
					if(!multiList.get(j).getAttribute("innerHTML").isEmpty()) {
						assetValue.add(multiList.get(j).getAttribute("innerHTML"));
					}
				}
			}


			//System.out.println(assetValue.size());

			for(int k=0; k<assetValue.size()-1; k++){

				//System.out.println("assetValue :" + assetValue.get(k));
				String s = assetValue.get(k).toString().trim().toLowerCase();
				String s2 = assetValue.get(k+1).toString().trim().toLowerCase();
				double current = Functions.getDoubleAUMval(s);
				double next = Functions.getDoubleAUMval(s2);
				if( ! (current<=next)){
					//System.out.println("current : "+ current + "  next : "+ next );
					log.debug("current : " + current + "  next : "+ next);
					result=false;
					break;
				}
				else{
					//System.out.println("current : "+ current + "  next : "+ next );
					log.debug("current : "+ current + "  next : "+ next);
					result=true;
				}
			}

			if(result==true)
				return "Pass";
			if(noData)
				return "Fail - No Data";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			if(noData)
				return "Fail - No Data";
			log.debug("Error while executing managersVerifySortAUMAsc -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String managersVerifySortAUMDsc() {
		log.debug("=============================");
		log.debug("managersVerifySortAUMAsc");

		List<String> assetValue=new ArrayList<String>();
		boolean noData=false;
		boolean result=false;

		/*double minValue = 0;
		double maxValue = 0;*/

		try {
			String rowsLocator = OR.getProperty(objectArr[0]);
			String dragThumbLocator = OR.getProperty(objectArr[1]);
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.assetDropdown.xpath"))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty(objectArr[3]))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.sortAUM.xpath"))).click();
			Thread.sleep(WAIT3SEC);
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.sortAUM.xpath"))).click();
			Thread.sleep(WAIT3SEC);
			//driver.findElement(By.xpath("//*[@id='strategy']/div/label[1]")).click();
			//Thread.sleep(WAIT2SEC);
			String totalRowCount = driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).getText();
			String totalRowCountTrimed = totalRowCount.trim();
			int totalRowInt = Integer.parseInt(totalRowCountTrimed);
			log.debug("total results is" + totalRowInt);
			boolean present =driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).isDisplayed();

			if(present){
				Functions.dragTillAllRowsLoaded(driver, log, rowsLocator, dragThumbLocator, totalRowInt, 100);
			}


			List<WebElement> multiList;
			List<WebElement> rows;

			try {
				rows = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]"));
			}catch(Throwable t) {
				noData=true;
				return "Fail-Data not present";
			}

			for(int i=1;i<=rows.size();i++) {			 
				log.debug("\nRow "+i);
				multiList = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell aum-cell')]/span"));
				log.debug("Multilist Size "+ multiList.size());
				for(int j=0;j<multiList.size();j++) {
					log.debug("List Element: "+multiList.get(j).getAttribute("innerHTML"));
					if(!multiList.get(j).getAttribute("innerHTML").isEmpty()) {
						assetValue.add(multiList.get(j).getAttribute("innerHTML"));
					}
				}
			}


			//System.out.println(assetValue.size());

			for(int k=0; k<assetValue.size()-1; k++){

				System.out.println("assetValue :" + assetValue.get(k));
				String s = assetValue.get(k).toString().trim().toLowerCase();
				String s2 = assetValue.get(k+1).toString().trim().toLowerCase();
				double current = Functions.getDoubleAUMval(s);
				double next = Functions.getDoubleAUMval(s2);
				if( ! (current>=next)){
					//System.out.println("current : "+ current + "  next : "+ next );
					log.debug("current : "+ current + "  next : "+ next);
					result=false;
					break;
				}
				else{
					//System.out.println("current : "+ current + "  next : "+ next );
					log.debug("current : "+ current + "  next : "+ next);
					result=true;
				}

			}

			if(result==true)
				return "Pass";
			if(noData)
				return "Fail - No Data";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			if(noData)
				return "Fail - No Data";
			log.debug("Error while executing managersVerifySortAUMAsc -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}


	public String managersDeleteSavedSearch() {
		log.debug("=============================");
		log.debug("Executing managersDeleteSavedSearch");
		String searchName="";
		boolean deleted=false;
		try {
			if (launchBrowser.equalsIgnoreCase("Firefox")) {
				searchName= "123AUTOMATION-FIREFOX";
			}
			if (launchBrowser.equalsIgnoreCase("InternetExplorer")) {
				searchName= "123AUTOMATION-IE";
			}
			if (launchBrowser.equalsIgnoreCase("Chrome")) {
				searchName= "123AUTOMATION-CHROME";
			}

			log.debug("Deleting Search: "+searchName);
			List<WebElement> L1 = driver.findElements(By.tagName("ul"));

			log.debug("list size 1: " + L1.size());

			Thread.sleep(WAIT2SEC);
			WebElement webElement = driver.findElement(By.xpath("//span[text()='"+searchName+"']/ancestor::li/span[@class='icon-delete delete']"));
			if(webElement!=null) {
				webElement.click();
				deleted=true;
			}

			log.debug("Deleted Search: "+searchName);
			if(deleted)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing managersDeleteSavedSearch -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}

	}


	public String fundsDeleteSavedSearch() {
		log.debug("=============================");
		log.debug("Executing fundsDeleteSavedSearch");
		String searchName="";
		boolean deleted=false;
		try {
			if (launchBrowser.equalsIgnoreCase("Firefox")) {
				searchName= "123AUTOMATION-FIREFOX2";
			}
			if (launchBrowser.equalsIgnoreCase("InternetExplorer")) {
				searchName= "123AUTOMATION-IE2";
			}
			if (launchBrowser.equalsIgnoreCase("Chrome")) {
				searchName= "123AUTOMATION-CHROME2";
			}

			log.debug("Deleting Search: "+searchName);
			List<WebElement> L1 = driver.findElements(By.tagName("ul"));

			log.debug("list size 1: " + L1.size());

			Thread.sleep(WAIT2SEC);
			WebElement webElement = driver.findElement(By.xpath("//span[text()='"+searchName+"']/ancestor::li/span[@class='icon-delete delete']"));
			if(webElement!=null) {
				webElement.click();
				deleted=true;
			}
			if(deleted)
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing fundsDeleteSavedSearch -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}


	public String fundsVerifyAssetData() {
		log.debug("=============================");
		log.debug("fundsVerifyAssetData");

		boolean result = true;
		List<WebElement> multiList;
		List<WebElement> rows;

		try {

			String assetName=APPTEXT.getProperty(objectArr[0]);
			log.debug("Asset: "+assetName);

			try {
				rows = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]"));
			}catch(Throwable t) {
				return "Fail-Data not present";
			}

			for(int i=1;i<=rows.size();i++) {			 
				log.debug("\nRow "+i);

				log.debug("Actual Asset: "+driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell ac-cell multi-check')]/span")).getAttribute("innerHTML"));

				if(driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell ac-cell multi-check')]/span")).getAttribute("innerHTML").equals("Multi")) {
					multiList = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell ac-cell multi-check')]/ul/li"));

					log.debug("Multilist Size "+ multiList.size());
					result=false;

					for(int j=0;j<multiList.size();j++) {
						log.debug("List Element: "+multiList.get(j).getAttribute("innerHTML"));

						if(multiList.get(j).getAttribute("innerHTML").contains(assetName)) {
							result = true;
							break;
						}
					}
				}else {
					if(!driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell ac-cell multi-check')]/span")).getAttribute("innerHTML").equals(assetName)) {
						return "Fail";
					}
				}

				if(!result) {
					return "Fail";
				}
			}

			if(result) {
				return "Pass";
			}else {
				return "Fail";
			}
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing fundsVerifyAssetData -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String fundsVerifyStrategyData() {
		log.debug("=============================");
		log.debug("Executing fundsVerifyStrategyData keyword");


		boolean result=true;
		List<WebElement> multiList;
		List<WebElement> rows;

		try {

			String strategyName=APPTEXT.getProperty(objectArr[0]);
			log.debug("Strategy: "+strategyName);

			try {
				rows = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]"));
			}catch(Throwable t) {
				return "Fail-Data not present";
			}

			for(int i=1;i<=rows.size();i++) {			 
				log.debug("\nRow "+i);

				log.debug("Actual Strategy: "+driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell strat-cell multi-check')]/span")).getAttribute("innerHTML"));

				if(driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell strat-cell multi-check')]/span")).getAttribute("innerHTML").equals("Multi")) {
					multiList = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell strat-cell multi-check')]/ul/li"));

					log.debug("Multilist Size "+ multiList.size());
					result=false;

					for(int j=0;j<multiList.size();j++) {
						log.debug("List Element: "+multiList.get(j).getAttribute("innerHTML"));

						if(multiList.get(j).getAttribute("innerHTML").contains(strategyName)) {
							result = true;
							break;
						}
					}
				}else {
					if(!driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell strat-cell multi-check')]/span")).getAttribute("innerHTML").equals(strategyName)) {
						return "Fail";
					}
				}

				if(!result) {
					return "Fail";
				}
			}

			if(result) {
				return "Pass";
			}else {
				return "Fail";
			}
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing fundsVerifyStrategyData -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String fundsVerifySubSearchResults() {
		log.debug("=============================");
		log.debug("managersVerifySubSearchResults");
		List<String> names=new ArrayList<String>();
		boolean noData=false;
		boolean result=false;

		try {

			String fundName = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			Thread.sleep(WAIT3SEC);
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.subSearchInputBox.xpath"))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.subSearchInputBox.xpath"))).sendKeys(fundName);
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.goButton.xpath"))).click();
			Thread.sleep(WAIT3SEC);
			System.out.println("fund name  :" + fundName );

			List<WebElement> multiList;
			List<WebElement> rows;

			try {
				rows = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]"));
				//System.out.println("number of rows : " +rows.size());
				log.debug("number of rows : " +rows.size());
			}catch(Throwable t) {
				noData=true;
				return "Fail-Data not present";
			}

			for(int i=1;i<=rows.size();i++) {			 
				log.debug("\nRow "+i);
				multiList = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell name-cell')]/a"));
				log.debug("Multilist Size "+ multiList.size());
				for(int j=0;j<multiList.size();j++) {
					log.debug("List Element: "+multiList.get(j).getAttribute("innerHTML"));
					if(!multiList.get(j).getAttribute("innerHTML").isEmpty()) {
						names.add(multiList.get(j).getAttribute("innerHTML"));
					}
				}
			}


			//System.out.println(names.size());

			for(int k=0; k<names.size(); k++){

				System.out.println("names :" + names.get(k));
				String s = names.get(k).toString().trim().toLowerCase();
				if( ! (s.contains(fundName))){
					log.debug("expected : " +fundName + "  actual : " + s );
					result=false;
					break;
				}
				else
					result=true;
			}

			if(result==true)
				return "Pass";
			if(noData)
				return "Fail - No Data";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			if(noData)
				return "Fail - No Data";
			log.debug("Error while executing managersVerifySubSearchResults -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}


	public String fundsVerifySortAsc() {
		log.debug("=============================");
		log.debug("fundsVerifySortAsc");
		List<String> assetClass=new ArrayList<String>();
		List<String> geography=new ArrayList<String>();
		List<String> strategy=new ArrayList<String>();
		List<String> sortedList=new ArrayList<String>();

		boolean result=false;
		boolean noData=false;


		try {

			String rowsLocator = OR.getProperty(objectArr[0]);
			String dragThumbLocator = OR.getProperty(objectArr[1]);
			String totalRowCount = driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).getText();
			String totalRowCountTrimed = totalRowCount.trim();
			int totalRowInt = Integer.parseInt(totalRowCountTrimed);
			log.debug("total results is" + totalRowInt);

			if (objectArr[3].contains("sortAssetClass")){
				driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.sortAssetClass.xpath"))).click();
				Thread.sleep(1000);
			}
			else if (objectArr[3].contains("sortGeography")){

				driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.sortGeography.xpath"))).click();
				Thread.sleep(1000);
			}
			else if (objectArr[3].contains("sortStrategy")){

				driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.sortStrategy.xpath"))).click();
				Thread.sleep(1000);
			}
			boolean present =driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).isDisplayed();

			if(present){
				Functions.dragTillAllRowsLoaded(driver, log, rowsLocator, dragThumbLocator, totalRowInt, 100);
			}

			List<WebElement> rows;
			String AC,strat,geo;

			try {
				rows = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]"));
				System.out.println("number of rows : " +rows.size());
				log.debug("number of rows : " +rows.size());
			}catch(Throwable t) {
				noData=true;
				return "Fail- No Data";
			}
			for(int i=1;i<=rows.size();i++) {			 
				log.debug("\nRow "+i);

				AC	 = driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell ac-cell')]/span")).getAttribute("innerHTML");
				strat = driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell strat-cell')]/span")).getAttribute("innerHTML");
				geo = driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell geog-cell')]/span")).getAttribute("innerHTML");

				if(!AC.isEmpty()) {
					//System.out.println("AC : " + AC);
					assetClass.add(AC);
				}
				if(!strat.isEmpty()) {
					//System.out.println("strategy : " + strat);
					strategy.add(strat);
				}
				if(!geo.isEmpty()) {
					//System.out.println("geography : " + geo);
					geography.add(geo);
				}

			}
			if (objectArr[3].contains("sortAssetClass")) {

				sortedList.addAll(assetClass);
				System.out.println(assetClass.size());
				System.out.println(sortedList.size());
				Collections.sort(sortedList, String.CASE_INSENSITIVE_ORDER);

				for (int k = 0; k < assetClass.size(); k++) {

					log.debug("assetClass :" + assetClass.get(k)+ "assetClass sorted  " + sortedList.get(k));
					String s = assetClass.get(k).toString().trim();
					String s2 = sortedList.get(k).toString().trim();

					if (!(s.equalsIgnoreCase(s2))) {
						log.debug("before sort  :" +s + "  after sort : " + s2);
						result = false;
						break;
					} else
						result = true;
				}
			}

			if (objectArr[3].contains("sortGeography")) {
				sortedList.addAll(geography);
				System.out.println(geography.size());
				System.out.println(sortedList.size());
				Collections.sort(sortedList, String.CASE_INSENSITIVE_ORDER);

				for (int k = 0; k < geography.size(); k++) {

					log.debug("geography :" + geography.get(k)	+ "geography sorted  " + sortedList.get(k));
					String s = geography.get(k).toString().trim();
					String s2 = sortedList.get(k).toString().trim();

					if (!(s.equalsIgnoreCase(s2))) {
						log.debug("before sort  :" +s + "  after sort : " + s2);
						result = false;
						break;
					} else
						result = true;
				}
			}

			if (objectArr[3].contains("sortStrategy")) {
				sortedList.addAll(strategy);
				System.out.println(strategy.size());
				System.out.println(sortedList.size());
				Collections.sort(sortedList, String.CASE_INSENSITIVE_ORDER);

				for (int k = 0; k < strategy.size(); k++) {
					log.debug("strategy :" + strategy.get(k)+ "strategy sorted  " + sortedList.get(k));
					String s = strategy.get(k).toString().trim();
					String s2 = sortedList.get(k).toString().trim();

					if (!(s.equalsIgnoreCase(s2))) {
						log.debug("before sort  :" +s + "  after sort : " + s2);
						result = false;
						break;
					} else
						result = true;
				}
			}

			if(result)
				return "Pass";
			if(noData)
				return "Fail - No Data";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			if(noData)
				return "Fail - No Data";
			else{
				log.debug("Error while executing fundsVerifySortAsc -" + objectArr[0]+ t.getMessage());
				return "Fail";
			}
		}
	}

	public String fundsVerifySortDesc() {
		log.debug("=============================");
		log.debug("fundsVerifySortDesc");
		//List<String> names=new ArrayList<String>();
		List<String> assetClass=new ArrayList<String>();
		List<String> geography=new ArrayList<String>();
		List<String> strategy=new ArrayList<String>();


		//List<String> names2=new ArrayList<String>();
		List<String> assetClass2=new ArrayList<String>();
		List<String> geography2=new ArrayList<String>();
		List<String> strategy2=new ArrayList<String>();


		boolean noData=false;
		boolean result=false;

		try {

			String rowsLocator = OR.getProperty(objectArr[0]);
			String dragThumbLocator = OR.getProperty(objectArr[1]);
			String totalRowCount = driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).getText();
			String totalRowCountTrimed = totalRowCount.trim();
			int totalRowInt = Integer.parseInt(totalRowCountTrimed);
			log.debug("total results is" + totalRowInt);

			if (objectArr[3].contains("sortAssetClass")){
				driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.sortAssetClass.xpath"))).click();
				Thread.sleep(1000);	
			}
			else if (objectArr[3].contains("sortGeography")){

				driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.sortGeography.xpath"))).click();
				Thread.sleep(1000);
			}
			else if (objectArr[3].contains("sortStrategy")){

				driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.sortStrategy.xpath"))).click();
				Thread.sleep(1000);
			}
			boolean present =driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).isDisplayed();

			if(present){
				Functions.dragTillAllRowsLoaded(driver, log, rowsLocator, dragThumbLocator, totalRowInt, 100);
			}




			List<WebElement> rows;
			String AC,strat,geo;

			try {
				rows = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]"));
				//System.out.println("number of rows : " +rows.size());
				log.debug("number of rows : " +rows.size());
			}catch(Throwable t) {
				noData=true;
				return "Fail-Data not present";
			}

			for(int i=1;i<=rows.size();i++) {			 
				log.debug("\nRow "+i);

				//name = driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell name-cell')]/a")).getAttribute("innerHTML");
				AC	 = driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell ac-cell')]/span")).getAttribute("innerHTML");
				strat = driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell strat-cell')]/span")).getAttribute("innerHTML");
				geo = driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell geog-cell')]/span")).getAttribute("innerHTML");

				if(!AC.isEmpty()) {
					//System.out.println("AC : " + AC);
					assetClass.add(AC);
				}
				if(!strat.isEmpty()) {
					//System.out.println("strategy : " + strat);
					strategy.add(strat);
				}
				if(!geo.isEmpty()) {
					//System.out.println("geography : " + geo);
					geography.add(geo);
				}

			}


			if (objectArr[3].contains("sortAssetClass")){
				driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.sortAssetClass.xpath"))).click();
				Thread.sleep(1000);
			}
			else if (objectArr[3].contains("sortGeography")){

				driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.sortGeography.xpath"))).click();
				Thread.sleep(1000);
			}
			else if (objectArr[3].contains("sortStrategy")){

				driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.sortStrategy.xpath"))).click();
				Thread.sleep(1000);
			}


			rowsLocator = OR.getProperty(objectArr[0]);
			dragThumbLocator = OR.getProperty(objectArr[1]);
			totalRowCount = driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).getText();
			totalRowCountTrimed = totalRowCount.trim();
			totalRowInt = Integer.parseInt(totalRowCountTrimed);
			log.debug("total results is" + totalRowInt);
			present =driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).isDisplayed();

			if(present){
				Functions.dragTillAllRowsLoaded(driver, log, rowsLocator, dragThumbLocator, totalRowInt, 100);
			}

			List<WebElement> rows2;

			try {
				rows2 = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]"));
				System.out.println("number of rows : " +rows2.size());
			}catch(Throwable t) {
				noData=true;
				return "Fail-Data not present";
			}

			for(int i=1;i<=rows2.size();i++) {			 
				log.debug("\nRow2 "+i);

				//name = driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell name-cell')]/a")).getAttribute("innerHTML");
				AC	 = driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell ac-cell')]/span")).getAttribute("innerHTML");
				strat = driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell strat-cell')]/span")).getAttribute("innerHTML");
				geo = driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell geog-cell')]/span")).getAttribute("innerHTML");

				if(!AC.isEmpty()) {
					//System.out.println("AC2 : " + AC);
					assetClass2.add(AC);
				}
				if(!strat.isEmpty()) {
					//System.out.println("strategy2 : " + strat);
					strategy2.add(strat);
				}
				if(!geo.isEmpty()) {
					//System.out.println("geography2 : " + geo);
					geography2.add(geo);
				}

			}


			if (objectArr[3].contains("sortAssetClass")) {
				Collections.reverse(assetClass);

				for (int k = 0; k < assetClass.size(); k++) {

					log.debug("assetClass :" + assetClass.get(k) + "assetClass2   "+ assetClass2.get(k));
					String s = assetClass.get(k).toString().trim();
					String s2 = assetClass2.get(k).toString().trim();

					if (!(s.equalsIgnoreCase(s2))) {
						log.debug("before sort  :" +s + "  after sort : " + s2);
						result = false;
						break;
					} else
						result = true;
				}
			}

			if (objectArr[3].contains("sortGeography")) {
				Collections.reverse(geography);
				System.out.println(geography.size());
				System.out.println(geography2.size());
				for (int k = 0; k < geography.size(); k++) {
					log.debug("geography :  " + geography.get(k) + "  geography2   " + geography2.get(k));
					String s = geography.get(k).toString().trim();
					String s2 = geography2.get(k).toString().trim();

					if (!(s.equalsIgnoreCase(s2))) {
						log.debug("before sort  :" +s + "  after sort : " + s2);
						result = false;
						break;
					} else
						result = true;
				}
			}

			if (objectArr[3].contains("sortStrategy")) {
				Collections.reverse(strategy);
				for (int k = 0; k < strategy.size(); k++) {

					log.debug("strategy :" + strategy.get(k) + "strategy2   " + strategy2.get(k));
					String s = strategy.get(k).toString().trim();
					String s2 = strategy2.get(k).toString().trim();

					if (!(s.equalsIgnoreCase(s2))) {
						log.debug("before sort  :" +s + "  after sort : " + s2);
						result = false;
						break;
					} else
						result = true;
				}
			}

			if(result)
				return "Pass";
			if(noData)
				return "Fail - No Data";

			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			if(noData)
				return "Fail - No Data";
			else{
				log.debug("Error while executing fundsVerifySortDesc -" + objectArr[0]+ t.getMessage());
				return "Fail";
			}
		}
	}

	public String fundsVerifyGeographyData() {
		log.debug("=============================");
		log.debug("fundsVerifyGeographyData");

		List<String> geography=new ArrayList<String>();

		String[] Africa =   { "Africa" };
		String[] Americas = { "Latin Americas" };
		String[] Asia =     { "China", "Greater China", "India", "Indonesia", "Sri Lanka","Japan", "Korea", "Malaysia", "Philippines", "Singapore", "Saudi Arabia", "Micronesia" };        
		String[] EmergingMarkets =  { "Global Emerging Markets" };
		String[] Europe =   { "Germany" , "Russia", "Sweden", "Austria", "Portugal", "Switzerland", "Denmark", "Eastern Europe", "Italy", "Pan Europe" };
		String[] Global =   { "Pan Europe", "Latin America", "Africa", "Europe", "Melanesia", "Eastern Europe", "Micronesia", "Asia", "Continental Europe", "Germany", "Global", "Malaysia", "Northern Europe", "Polynesia", "Thailand", "UK", "United States", "Global Emerging Markets" };
		String[] UnitedStates =   { "United States" };

		String asStringAfrica=Arrays.toString(Africa);
		String asStringAmericas=Arrays.toString(Americas);
		String asStringAsia=Arrays.toString(Asia);
		String asStringEmergingMarkets=Arrays.toString(EmergingMarkets);
		String asStringEurope=Arrays.toString(Europe);
		String asStringGlobal=Arrays.toString(Global);
		String asStringUnitedStates=Arrays.toString(UnitedStates);

		boolean noData=false;
		boolean result=false;

		try {
			try{
				Functions.waitForElementClickable(driver, log, objectArr[1]);
				Thread.sleep(WAIT2SEC);
			}catch(ArrayIndexOutOfBoundsException e) {

			}
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.assetDropdown.xpath"))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
			Thread.sleep(WAIT5SEC);
			String strategyOption = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty(strategyOption))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.submitButton.xpath"))).click();
			Thread.sleep(WAIT5SEC);
			try{
				Functions.waitForElementClickable(driver, log, "aims.managersFunds.goButton.xpath");
			}catch(ArrayIndexOutOfBoundsException e) {

			}
			String strategyOptionText=APPTEXT.getProperty(strategyOption);
			System.out.println("strategyOptionText  :" + strategyOptionText );

			List<WebElement> multiList;
			List<WebElement> rows;

			try {
				rows = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]"));
				log.debug("number of rows : " +rows.size());
				System.out.println("number of rows : " +rows.size());
			}catch(Throwable t) {
				noData=true;
				return "Fail-Data not present";
			}

			for(int i=1;i<=rows.size();i++) {			 
				log.debug("\nRow "+i);

				multiList = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell geog-cell multi-check')]/span"));
				log.debug("Multilist Size "+ multiList.size());
				for(int j=0;j<multiList.size();j++) {
					log.debug("List Element: "+multiList.get(j).getAttribute("innerHTML"));
					if(!multiList.get(j).getAttribute("innerHTML").isEmpty()) {
						geography.add(multiList.get(j).getAttribute("innerHTML"));
					}
				}
			}
			//System.out.println(geography.size());

			for(int k=0; k<geography.size(); k++){

				//System.out.println("geography :" + geography.get(k));
				String s = geography.get(k).toString().trim();
				int checkCount=0;

				if (strategyOptionText.equalsIgnoreCase("Africa")) {

					if (!asStringAfrica.contains(s)) {
						log.debug("expected  :" + asStringAfrica+"  actual :" + s);
						result = false;
						checkCount++;
						break;

					}
				}
				if (strategyOptionText.equalsIgnoreCase("Americas")) {


					if(!asStringAmericas.contains(s))
					{
						log.debug("expected  :" + asStringAmericas+"  actual :" + s);
						result = false;
						checkCount++;
						break;

					}
				}
				if (strategyOptionText.equalsIgnoreCase("Asia")) {

					if(!asStringAsia.contains(s))
					{
						log.debug("expected  :" + asStringAsia+"  actual :" + s);
						result = false;
						checkCount++;
						break;

					}
				}
				if (strategyOptionText.equalsIgnoreCase("Emerging Markets")) {

					if(!asStringEmergingMarkets.contains(s))
					{
						log.debug("expected  :" + asStringEmergingMarkets+"  actual :" + s);
						result = false;
						checkCount++;
						break;

					}
				}
				if (strategyOptionText.equalsIgnoreCase("Europe")) {

					if(!asStringEurope.contains(s))
					{
						log.debug("expected  :" + asStringEurope+"  actual :" + s);
						result = false;
						checkCount++;
						break;

					}
				}
				if (strategyOptionText.equalsIgnoreCase("Global")) {
					if(!asStringGlobal.contains(s))
					{
						log.debug("expected  :" + asStringGlobal+"  actual :" + s);
						result = false;
						checkCount++;
						break;

					}
				}
				if (strategyOptionText.equalsIgnoreCase("United States")) {
					if(!asStringUnitedStates.contains(s))
					{
						log.debug("expected  :" + asStringUnitedStates+"  actual :" + s);
						result = false;
						checkCount++;
						break;

					}
				}

				if(checkCount>=1){
					result=false;
					break;
				}
				else
					result=true;
			}

			if(result==true)
				return "Pass";
			if(noData)
				return "Fail - No Data";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			if(noData)
				return "Fail - No Data";
			log.debug("Error while executing managersVerifyGeographyData -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}	

	public String insightsSortByAuthor() {
		log.debug("=============================");
		log.debug("insightsSortByAuthor");
		List<String> author=new ArrayList<String>();
		List<String> authorsSorted=new ArrayList<String>();
		boolean result=true;

		try {
			driver.findElement(By.xpath(OR.getProperty("PlayListSort"))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
			Thread.sleep(WAIT2SEC);

			List<WebElement> authors = driver.findElements(By.tagName("//span[@class='author']"));
			Iterator<WebElement> i = authors.iterator();

			while(i.hasNext()){
				WebElement e = i.next();				
					log.debug("author : "+e.getText());
					if(! e.getText().isEmpty())
						author.add(e.getText());
				
			}

			authorsSorted.addAll(author);
			System.out.println(author.size());
			System.out.println(authorsSorted.size());
			Collections.sort(authorsSorted, String.CASE_INSENSITIVE_ORDER);

			for(int k=0; k<author.size(); k++){

				System.out.println("author :" + author.get(k)+ "   authors sorted  "+ authorsSorted.get(k));
				String s = author.get(k).toString().trim();
				String s2 = authorsSorted.get(k).toString().trim();

				if(!(s.equalsIgnoreCase(s2)))
				{
					result=false;
					break;
				}
				else
					result=true;
			}

			if(result==true)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing managersVerifySortName -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String insightsSortByTitle() {
		log.debug("=============================");
		log.debug("insightsSortByTitle");

		List<String> name=new ArrayList<String>();
		List<String> titlesSorted=new ArrayList<String>();
		boolean result=true;

		try {
			driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).click();
			Thread.sleep(WAIT4SEC);
			driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
			Thread.sleep(WAIT4SEC);

			List<WebElement> names = driver.findElements(By.xpath("//*[@class='listing']/descendant::span[@class='title']"));
			for( WebElement e : names) {
				if(e.getAttribute("class").equalsIgnoreCase("title")){
					log.debug("name : "+e.getText());
					name.add(e.getText());
				}
			}

			titlesSorted.addAll(name);
			System.out.println(name.size());
			System.out.println(titlesSorted.size());
			Collections.sort(titlesSorted, String.CASE_INSENSITIVE_ORDER);

			for(int k=0; k<name.size(); k++){

				System.out.println("name :" + name.get(k)+ "    titles sorted:  "+ titlesSorted.get(k));
				String s = name.get(k).toString().trim();
				String s2 = titlesSorted.get(k).toString().trim();

				if(!(s.equalsIgnoreCase(s2)))
				{
					result=false;
					break;
				}
				else
					result=true;
			}

			if(result==true)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing insightsSortByTitle -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String managersSortByTitle() {
		log.debug("=============================");
		log.debug("managersSortByTitle");

		List<String> titles=new ArrayList<String>();
		List<String> titlesSorted=new ArrayList<String>();
		boolean result=true;

		try {
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.playListSort.xpath"))).click();

			Thread.sleep(WAIT5SEC);

			driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();

			Thread.sleep(WAIT8SEC);

			List<WebElement> names = driver.findElements(By.xpath("//div[@class='listing']/descendant::span[@class='title']"));
			for(WebElement e : names) {
				if(e.getAttribute("class").equalsIgnoreCase("title")){
					System.out.println("title : "+e.getText());
					titles.add(e.getText());
				}
			}

			titlesSorted.addAll(titles);
			System.out.println(titles.size());
			System.out.println(titlesSorted.size());
			Collections.sort(titlesSorted, String.CASE_INSENSITIVE_ORDER);


			for(int k=0; k<titles.size(); k++){

				System.out.println("name :" + titles.get(k)+ "    titles sorted  "+ titlesSorted.get(k));
				String s = titles.get(k).toString().trim();
				String s2 = titlesSorted.get(k).toString().trim();

				if(!(s.equalsIgnoreCase(s2)))
				{
					result=false;
					break;
				}
				else
					result=true;
			}

			if(result==true)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing managersSortByTitle -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String insightsSortByDate() {
		log.debug("=============================");
		log.debug("insightsSortByDate");

		List<String> date=new ArrayList<String>();

		boolean result=true;

		try {

			Thread.sleep(WAIT4SEC);
			driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).click();
			Thread.sleep(WAIT4SEC);
			driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
			Thread.sleep(WAIT4SEC);

			//List<WebElement> dates = driver.findElements(By.tagName("span"));
			//changing to list view
			driver.findElement(By.xpath("//div[@class='fl view-btns clearfix']/descendant::a[@title='List view']")).click();
			Thread.sleep(WAIT5SEC);
			List<WebElement> dates = driver.findElements(By.xpath("//div[@class='date dsp-cell']"));
			
			for (WebElement e : dates){
//			Iterator<WebElement> i3 = dates.iterator();			
//
				if(dates.size()==0 || dates.size()==1)
				return "Fail-Data not present";

//			while(i3.hasNext()){
//				WebElement e = i3.next();
				//if(e.getAttribute("class").equalsIgnoreCase("date")){
				//if(e.getAttribute("class").equalsIgnoreCase("date dsp-cell")){
					String value = e.getAttribute("innerHTML");
					System.out.println("date : "+value);
					log.debug("date : "+value);
					if(!value.isEmpty())
						date.add(value);
				
			}
			//changing to grid view
			driver.findElement(By.xpath("//div[@class='fl view-btns clearfix']/descendant::a[@title='Grid view']")).click();
			Date finalDates[]= new Date[date.size()];

			for(int d=0; d<date.size(); d++){
				//Date finalDates[]= new Date[date.size()];
				String s = date.get(d).toString();
				//String temp[]= s.split(" ");
				//System.out.println(temp.length);
				//System.out.println(temp[0] + "  : " + temp[1]);
				SimpleDateFormat inputFormat = new SimpleDateFormat(
						"MMMM dd yyyy");
				//inputFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
				SimpleDateFormat out = new SimpleDateFormat("MMMM dd yyyy");
				finalDates[d] = inputFormat.parse(s);	//temp[1].replace(",","").trim()
				String output = out.format( finalDates[d]);
				log.debug("final date : "+ output);
				System.out.println("final date : "+ output);
			}

			outer:
				for(int k=0; k<date.size(); k++){
					for(int l=k+1; l<date.size(); l++){
						//System.out.println("dates  :" + finalDates[k] +  "  "+ finalDates[l]);
						if(finalDates[k].before(finalDates[l])){
							log.debug(finalDates[k] + "  falls before " + finalDates[l]);
							result=false;
							break outer;
						}
					}
				}

			if(result==true)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing insightsSortByDate -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String managersSortByDate() {
		log.debug("=============================");
		log.debug("managersSortByDate");

		boolean result=true;

		try {
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.playListSort.xpath"))).click();

			Thread.sleep(WAIT3SEC);

			driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();

			Thread.sleep(WAIT5SEC);

			List<WebElement> managersGridView = driver.findElements(By.xpath("//div[@class='listing']/descendant::span[@class='title']"));

			ArrayList<String> managersGridViewArr = new ArrayList<String>();				

			for (int j=0;j<managersGridView.size();j++){
				managersGridViewArr.add(managersGridView.get(j).getAttribute("title"));
				System.out.println(managersGridViewArr.get(j));
			}
			Thread.sleep(WAIT2SEC);

			//if the order is same in grid view as in list view, it is pass.

			driver.findElement(By.xpath("//div[@class='fr clearfix view-options']/descendant::a[@title='List view']")).click();
			Thread.sleep(WAIT3SEC);
			driver.findElement(By.xpath("//div[@class='fr clearfix view-options']/descendant::a[@title='List view']")).click();
			Thread.sleep(WAIT3SEC);
			List<WebElement> managersListView = driver.findElements(By.xpath("//div[@class='listing dsp-table']/descendant::div[@class='dsp-cell name']/div/span"));

			ArrayList<String> managersListViewArr = new ArrayList<String>();

			for (int j=0;j<managersListView.size();j++){
				managersListViewArr.add(managersListView.get(j).getAttribute("title"));
				System.out.println(managersListViewArr.get(j));
			}

			for(int j=0;j<managersGridViewArr.size();j++){
				log.debug("Grid view Element: "+managersGridViewArr.get(j).trim()+" List View Element: "+ managersListViewArr.get(j).trim());
				if(!(managersGridViewArr.get(j).trim().equals(managersListViewArr.get(j).trim()))){
					result = false;
					break;
				}
			}

			driver.findElement(By.xpath("//div[@class='fr clearfix view-options']/descendant::a[@title='Grid view']")).click();

			Thread.sleep(WAIT5SEC);

			if(result==true)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing managersSortByDate -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}

	}

	public String Workspace_DocCount() {
		log.debug("=============================");
		log.debug("Executing Workspace_DocCount");
		try {
			int DocCountOnBadge=Integer.parseInt(driver.findElement(By.xpath(OR.getProperty("DocCountOnBadge"))).getText());
			int ManagerAlertsCount=Integer.parseInt(driver.findElement(By.xpath(OR.getProperty("ManagerAlertsCount"))).getText());
			int AddForYouCount=Integer.parseInt(driver.findElement(By.xpath(OR.getProperty("AddForYouCount"))).getText());
			int ReportsCount=Integer.parseInt(driver.findElement(By.xpath(OR.getProperty("ReportsCount"))).getText());
			int ManagerResearchCount=Integer.parseInt(driver.findElement(By.xpath(OR.getProperty("ManagerResearchCount"))).getText());
			int PortfolioUpdatesCount=Integer.parseInt(driver.findElement(By.xpath(OR.getProperty("PortfolioUpdatesCount"))).getText());
			int TaxDocumentsCount=Integer.parseInt(driver.findElement(By.xpath(OR.getProperty("TaxDocumentsCount"))).getText());
			Thread.sleep(WAIT2SEC);
			int totalDocs = ManagerAlertsCount + AddForYouCount + ReportsCount +ManagerResearchCount+PortfolioUpdatesCount+TaxDocumentsCount;
			System.out.println(DocCountOnBadge + " " +ManagerAlertsCount +" " + AddForYouCount +
					" " +ReportsCount + " " +ManagerResearchCount+ " " +PortfolioUpdatesCount+ " " +TaxDocumentsCount);
			if(DocCountOnBadge==totalDocs)
				return "Pass";
			else{
				System.out.println("badge count "+DocCountOnBadge+ "  and total docs count  " +totalDocs + "  are not equal"  );
				return "Fail";
			}

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing Workspace_DocCount  -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}

	}

	public String myPortfolio_FundLink_LensesTitle() {
		log.debug("=============================");
		log.debug("Executing myPortfolio_FundLink_LensesTitle Keyword");
		int failCount=0;
		try {
			String linkText=driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText().toLowerCase();
			log.debug("link text before trim  " + linkText);
			driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
			linkText = linkText.replaceAll("[.]", "").trim();
			log.debug("link text after trim  " +linkText);
			Thread.sleep(WAIT3SEC);
			String titleText=driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).getText().toLowerCase();
			log.debug("link text is : " +linkText + "  Lense title text is  :  " + titleText);
			if(! linkText.contains(titleText)){
				failCount++;
			}
			driver.findElement(By.xpath(OR.getProperty("aims.workspace.portfolio.lenses.xpath"))).click();
			Thread.sleep(WAIT3SEC);
			String fundNameOnOverlay=driver.findElement(By.xpath(OR.getProperty("aims.LenseOverlaytitle1"))).getText();
			if( ! fundNameOnOverlay.equalsIgnoreCase(titleText)){
				failCount++;
			}

			if(failCount>0){
				log.debug("linkText :" + linkText);
				log.debug("titleText :" + titleText);
				log.debug("fundNameOnOverlay :" + fundNameOnOverlay);
				return "Fail";
			}
			else
				return "Pass";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing myPortfolio_FundLink_LensesTitle -" +  t.getMessage());
			return "Fail";
		}
	}
	public String MyPortfolioSortFundNameAsc() {
		log.debug("=============================");
		log.debug("MyPortfolioSortFundNameAsc");
		List<String> names=new ArrayList<String>();
		List<String> namesSorted=new ArrayList<String>();

		boolean result=false;

		try {
			String whatToDoString=objectArr[0];
			int whatToDo=0;
			if(whatToDoString.equalsIgnoreCase("aims.PerformanceView")){
				whatToDo=1;
			}
			else if(whatToDoString.equalsIgnoreCase("aims.ListView")){
				whatToDo=2;
			}

			switch(whatToDo){

			case 1:
				List<WebElement> fundsList = driver.findElements(By.xpath("//*[@id='allPortfolioView']/descendant::tbody[@class='all_portfolio ng-scope']/descendant::td[@class='name ng-binding']"));
				for(WebElement e : fundsList) {
					String value = e.getAttribute("innerHTML");
					System.out.println("value is :  " + value);
					log.debug("value is :  " + value);
					if (!value.isEmpty())
					{
						names.add(value);
					}
				}

				break;

			case 2:
				List<WebElement> fundsList2 = driver.findElements(By.xpath("//td[@class='name ng-binding']"));
				for(WebElement e : fundsList2) {
					String value = e.getAttribute("innerHTML");
					System.out.println("value is :  " + value);
					log.debug("value is :  " + value);
					if (!value.isEmpty())
					{
						names.add(value);
					}
				}

				break;
			}



			namesSorted.addAll(names);
			System.out.println(names.size());
			System.out.println(namesSorted.size());
			Collections.sort(namesSorted, String.CASE_INSENSITIVE_ORDER);

			for(int k=0; k<names.size(); k++){

				System.out.println("names :" + names.get(k)+ "names sorted  "+ namesSorted.get(k));
				String s = names.get(k).toString().trim();
				String s2 = namesSorted.get(k).toString().trim();

				if(!(s.equalsIgnoreCase(s2)))
				{
					result=false;
					break;
				}
				else
					result=true;

			}

			if(result==true)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyPortfolioSortFundNameAsc -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String MyPortfolioSortFundNameDsc() {
		log.debug("=============================");
		log.debug("MyPortfolioSortFundNameDsc");
		List<String> names=new ArrayList<String>();

		List<String> names2=new ArrayList<String>();

		boolean result=false;

		try {
			String whatToDoString=objectArr[0];
			int whatToDo=0;
			if(whatToDoString.equalsIgnoreCase("aims.PerformanceView")){
				whatToDo=1;
			}
			else if(whatToDoString.equalsIgnoreCase("aims.ListView")){
				whatToDo=2;
			}

			List<WebElement> fundsList=null;

			if(whatToDo==1){
				fundsList = driver.findElements(By.xpath("//*[@id='allPortfolioView']/descendant::tbody[@class='all_portfolio ng-scope']/descendant::td[@class='name ng-binding']"));
			}
			if(whatToDo==2){
				fundsList = driver.findElements(By.xpath("//td[@class='name ng-binding']"));
			}

			for(WebElement e : fundsList) {
				String value = e.getAttribute("innerHTML");
				System.out.println("value is :  " + value);
				log.debug("value is :  " + value);
				if (!value.isEmpty())
				{
					names.add(value);
				}
			}

			Collections.reverse(names);
			System.out.println(names.size());

			switch(whatToDo){
			case 1:
				driver.findElement(By.xpath(OR.getProperty("aims.Portfolio.Perf.SortByName"))).click();
				Thread.sleep(WAIT2SEC);

				List<WebElement> fundsList2 = driver.findElements(By.xpath("//*[@id='allPortfolioView']/descendant::tbody[@class='all_portfolio ng-scope']/descendant::td[@class='name ng-binding']"));
				for(WebElement e2 : fundsList2) {
					String value2 = e2.getAttribute("innerHTML");
					System.out.println("value is :  " + value2);
					log.debug("value is :  " + value2);
					if (!value2.isEmpty())
					{
						names2.add(value2);
					}
				}

				break;

			case 2:
				driver.findElement(By.xpath(OR.getProperty("aims.Portfolio.List.SortByName"))).click();
				Thread.sleep(WAIT2SEC);

				List<WebElement> fundsList3 = driver.findElements(By.xpath("//td[@class='name ng-binding']"));
				for(WebElement e2 : fundsList3) {
					String value2 = e2.getAttribute("innerHTML");
					System.out.println("value is :  " + value2);
					log.debug("value is :  " + value2);
					if (!value2.isEmpty())
					{
						names2.add(value2);
					}
				}
				break;
			}


			for(int k=0; k<names.size(); k++){

				System.out.println("names :" + names.get(k)+ "names2   "+ names2.get(k));
				String s = names.get(k).toString().trim();
				String s2 = names2.get(k).toString().trim();

				if(!(s.equalsIgnoreCase(s2)))
				{
					result=false;
					break;
				}
				else
					result=true;
			}	

			if(result==true)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyPortfolioSortFundNameDsc -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String MyPortfolioSortNavAsc() {
		//this keyword work as is for both performance view and list view in portfoli landing page without any change
		log.debug("=============================");
		log.debug("MyPortfolioSortNavAsc");

		List<String> nav=new ArrayList<String>();
		boolean result=false;

		try {

			List<WebElement> fundsList = driver.findElements(By.xpath("//td[@class='nav ng-binding']"));
			log.debug("list size : " + fundsList.size());
			for(WebElement e : fundsList) {
				if (e.isDisplayed()) 
				{
					String value = e.getText();
					System.out.println("NAV value is :  " + value);
					log.debug("NAV value is :  " + value);
					if (!value.isEmpty())
					{
						value = value.replaceAll("[^\\d.]", "");
						nav.add(value);
					}
				}

			}

			if(nav.size()<2) {
				log.debug("total number of portfolios present : " + nav.size());
				return "Pass";
			}

			for(int k=0; k<nav.size()-1; k++){

				System.out.println("assetValue :" + nav.get(k));
				String s = nav.get(k).toString().trim().toLowerCase();
				String s2 = nav.get(k+1).toString().trim().toLowerCase();
				double current = Double.parseDouble(s);
				double next = Double.parseDouble(s2);
				if( ! (current<=next)){
					System.out.println("current : "+ current + "  next : "+ next );
					log.debug("current : "+ current + "  next : "+ next );
					result=false;
					break;
				}
				else{
					System.out.println("current : "+ current + "  next : "+ next );
					log.debug("current : "+ current + "  next : "+ next );
					result=true;
				}

			}

			if(result==true)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyPortfolioSortNavAsc -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String MyPortfolioSortNavDsc() {
		//this keyword work as is for both performance view and list view in portfoli landing page without any change
		log.debug("=============================");
		log.debug("MyPortfolioSortNavDsc");

		List<String> nav=new ArrayList<String>();
		boolean result=false;

		try {
			List<WebElement> fundsList = driver.findElements(By.xpath("//*[@id='allPortfolioView']/descendant::tbody[@class='all_portfolio ng-scope']/descendant::td[@class='nav ng-binding']"));
			log.debug("list size : " + fundsList.size());
			for(WebElement e : fundsList) {
				if (e.isDisplayed()) 
				{
					String value = e.getText();
					System.out.println("NAV value is :  " + value);
					log.debug("NAV value is :  " + value);
					if (!value.isEmpty())
					{
						value = value.replaceAll("[^\\d.]", "");
						nav.add(value);
					}
				}

			}

			if(nav.size()<2) {
				log.debug("total number of portfolios present : " + nav.size());
				return "Pass";
			}

			for(int k=0; k<nav.size()-1; k++){

				System.out.println("assetValue :" + nav.get(k));
				String s = nav.get(k).toString().trim().toLowerCase();
				String s2 = nav.get(k+1).toString().trim().toLowerCase();
				double current = Double.parseDouble(s);
				double next = Double.parseDouble(s2);
				if( ! (current>=next)){
					System.out.println("current : "+ current + "  next : "+ next );
					log.debug("current : "+ current + "  next : "+ next );
					result=false;
					break;
				}
				else{
					System.out.println("current : "+ current + "  next : "+ next );
					log.debug("current : "+ current + "  next : "+ next );
					result=true;
				}

			}

			if(result==true)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyPortfolioSortNavDsc -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}


	public String MyPortfolioSortDateDsc() {
		log.debug("=============================");
		log.debug("MyPortfolioSortDateDsc");
		List<String> dates=new ArrayList<String>();


		boolean result=false;

		try {
			String whatToDoString=objectArr[0];
			int whatToDo=0;
			if(whatToDoString.equalsIgnoreCase("aims.PerformanceView")){
				whatToDo=1;
			}
			else if(whatToDoString.equalsIgnoreCase("aims.ListView")){
				whatToDo=2;
			}

			switch(whatToDo){

			case 1:
				List<WebElement> fundsList = driver.findElements(By.xpath("//*[@id='allPortfolioView']/descendant::tbody[@class='all_portfolio ng-scope']/descendant::td[@class='as_of_date ng-binding']"));
				for(WebElement e : fundsList) {
					String value = e.getAttribute("innerHTML");
					//System.out.println("value is :  " + value);
					log.debug("value is :  " + value);
					if (!value.isEmpty())
					{
						dates.add(value);
					}
				}

				break;

			case 2:
				List<WebElement> fundsList2 = driver.findElements(By.xpath("//*[@id='allPortfolioView']/descendant::tbody[@class='all_portfolio ng-scope']/descendant::td[@class='as_of_date ng-binding']"));
				for(WebElement e : fundsList2) {
					String value = e.getAttribute("innerHTML");
					//System.out.println("value is :  " + value);
					log.debug("value is :  " + value);
					if (!value.isEmpty())
					{
						dates.add(value);
					}
				}

				break;

			}


			Date finalDates[]= new Date[dates.size()];

			for(int d=0; d<dates.size(); d++){
				String s = dates.get(d).toString().trim();
				SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM dd yyyy");
				SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd yyyy");
				finalDates[d] = inputFormat.parse(s);
				outputFormat.format( finalDates[d]);
				String output = outputFormat.format( finalDates[d]);
				//System.out.println("final date : "+ output);
			}

			outer:
				for(int k=0; k<dates.size(); k++){
					for(int l=k+1; l<dates.size(); l++){
						//System.out.println("dates  :" + finalDates[k] +  "  "+ finalDates[l]);
						if(finalDates[k].before(finalDates[l])){
							log.debug(finalDates[k] + "  falls before " + finalDates[l]);
							result=false;
							break outer;
						}
						else
							result=true;
					}
				}

			if(result==true)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyPortfolioSortDateDsc -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String MyPortfolioSortDateAsc() {
		log.debug("=============================");
		log.debug("MyPortfolioSortDateAsc");
		List<String> dates=new ArrayList<String>();


		boolean result=false;

		try {
			String whatToDoString=objectArr[0];
			int whatToDo=0;
			if(whatToDoString.equalsIgnoreCase("aims.PerformanceView")){
				whatToDo=1;
			}
			else if(whatToDoString.equalsIgnoreCase("aims.ListView")){
				whatToDo=2;
			}

			switch(whatToDo){

			case 1:
				List<WebElement> fundsList = driver.findElements(By.xpath("//*[@id='allPortfolioView']/descendant::tbody[@class='all_portfolio ng-scope']/descendant::td[@class='as_of_date ng-binding']"));
				for(WebElement e : fundsList) {
					String value = e.getAttribute("innerHTML");
					//System.out.println("value is :  " + value);
					log.debug("value is :  " + value);
					if (!value.isEmpty())
					{
						dates.add(value);
					}
				}

				break;

			case 2:
				List<WebElement> fundsList2 = driver.findElements(By.xpath("//*[@id='allPortfolioView']/descendant::tbody[@class='all_portfolio ng-scope']/descendant::td[@class='as_of_date ng-binding']"));
				for(WebElement e : fundsList2) {
					String value = e.getAttribute("innerHTML");
					//System.out.println("value is :  " + value);
					log.debug("value is :  " + value);
					if (!value.isEmpty())
					{
						dates.add(value);
					}
				}

				break;
			}



			Date finalDates[]= new Date[dates.size()];

			for(int d=0; d<dates.size(); d++){
				String s = dates.get(d).toString().trim();
				SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM dd yyyy");
				SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd yyyy");
				finalDates[d] = inputFormat.parse(s);
				outputFormat.format( finalDates[d]);
				String output = outputFormat.format( finalDates[d]);
				//System.out.println("final date : "+ output);
			}
			outer:
				for(int k=0; k<dates.size(); k++){
					for(int l=k+1; l<dates.size(); l++){
						//System.out.println("dates  :" + finalDates[k] +  "  "+ finalDates[l]);
						if(finalDates[k].after(finalDates[l])){
							log.debug(finalDates[k] + "  falls after " + finalDates[l]);
							result=false;
							break outer;
						}
						else
							result=true;
					}
				}

			if(result)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyPortfolioSortFundNameAsc -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String MyPortfolioSortChartAsc() {
		log.debug("=============================");
		log.debug("MyPortfolioSortChartAsc");

		List<String> nav=new ArrayList<String>();
		boolean result=false;

		try {
			List<WebElement> fundsList = driver.findElements(By.xpath("//*[@id='allPortfolioView']/descendant::tbody[@class='all_portfolio ng-scope']/descendant::td[@class='assetClass ng-binding']"));
			log.debug("list size : " + fundsList.size());
			for(WebElement e : fundsList) {
				if (e.isDisplayed()) 
				{
					String value = e.getText();
					System.out.println("NAV value is :  " + value);
					log.debug("NAV value is :  " + value);
					if (!value.isEmpty())
					{
						//value = value.replaceAll("[^\\d.]", "");
						value = value.substring(0,value.length()-1);
						System.out.println(value);
						nav.add(value);
					}
				}

			}

			if(nav.size()<2) {
				log.debug("total number of portfolios present : " + nav.size());
				return "Pass";
			}

			for(int k=0; k<nav.size()-1; k++){


				String s = nav.get(k).toString();
				String s2 = nav.get(k+1).toString();
				System.out.println("current : " + s + "  next :"+ s2);
				double current = Double.parseDouble(s);
				double next = Double.parseDouble(s2);
				if( ! (current<=next)){
					System.out.println("current : "+ current + "  next : "+ next );
					log.debug("current : "+ current + "  next : "+ next );
					result=false;
					break;
				}
				else{
					System.out.println("current : "+ current + "  next : "+ next );
					log.debug("current : "+ current + "  next : "+ next );
					result=true;
				}

			}

			if(result==true)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyPortfolioSortChartAsc -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String MyPortfolioSortChartDsc() {
		log.debug("=============================");
		log.debug("MyPortfolioSortChartDsc");

		List<String> nav=new ArrayList<String>();
		boolean result=false;

		try {
			List<WebElement> fundsList = driver.findElements(By.xpath("//*[@id='allPortfolioView']/descendant::tbody[@class='all_portfolio ng-scope']/descendant::td[@class='chart']"));
			log.debug("list size : " + fundsList.size());
			for(WebElement e : fundsList) {
				if (e.isDisplayed()) 
				{
					String value = e.getText();
					System.out.println("NAV value is :  " + value);
					log.debug("NAV value is :  " + value);
					if (!value.isEmpty())
					{
						//value = value.replaceAll("[^\\d.]", "");
						value = value.substring(0,value.length()-1);
						System.out.println(value);
						nav.add(value);
					}
				}

			}

			if(nav.size()<2) {
				log.debug("total number of portfolios present : " + nav.size());
				return "Pass";
			}

			for(int k=0; k<nav.size()-1; k++){


				String s = nav.get(k).toString();
				String s2 = nav.get(k+1).toString();
				System.out.println("current : " + s + "  next :"+ s2);
				double current = Double.parseDouble(s);
				double next = Double.parseDouble(s2);
				if( ! (current>=next)){
					System.out.println("current : "+ current + "  next : "+ next );
					log.debug("current : "+ current + "  next : "+ next );
					result=false;
					break;
				}
				else{
					System.out.println("current : "+ current + "  next : "+ next );
					log.debug("current : "+ current + "  next : "+ next );
					result=true;
				}

			}

			if(result==true)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyPortfolioSortChartDsc -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}


	public String MyPortfolioSortAssetClassAsc() {
		log.debug("=============================");
		log.debug("MyPortfolioSortAssetClassAsc");
		List<String> names=new ArrayList<String>();
		List<String> namesSorted=new ArrayList<String>();

		boolean result=false;

		try {

			List<WebElement> fundsList = driver.findElements(By.xpath("//td[@class='assetClass ng-binding']"));
			for(WebElement e : fundsList) {
				String value = e.getAttribute("innerHTML");
				System.out.println("value is :  " + value);
				log.debug("value is :  " + value);
				if (!value.isEmpty())
				{
					names.add(value);
				}
			}


			namesSorted.addAll(names);
			System.out.println(names.size());
			System.out.println(namesSorted.size());
			Collections.sort(namesSorted, String.CASE_INSENSITIVE_ORDER);

			for(int k=0; k<names.size(); k++){

				System.out.println("names :" + names.get(k)+ "names sorted  "+ namesSorted.get(k));
				String s = names.get(k).toString().trim();
				String s2 = namesSorted.get(k).toString().trim();

				if(!(s.equalsIgnoreCase(s2)))
				{
					result=false;
					break;
				}
				else
					result=true;

			}

			if(result==true)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyPortfolioSortAssetClassAsc -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String MyPortfolioSortStrategyAsc() {
		log.debug("=============================");
		log.debug("MyPortfolioSortStrategyAsc");
		List<String> names=new ArrayList<String>();
		List<String> namesSorted=new ArrayList<String>();

		boolean result=false;

		try {

			List<WebElement> fundsList = driver.findElements(By.xpath("//td[@class='strategy ng-binding']"));
			for(WebElement e : fundsList) {
				String value = e.getAttribute("innerHTML");
				System.out.println("value is :  " + value);
				log.debug("value is :  " + value);
				if (!value.isEmpty())
				{
					names.add(value);
				}
			}


			namesSorted.addAll(names);
			System.out.println(names.size());
			System.out.println(namesSorted.size());
			Collections.sort(namesSorted, String.CASE_INSENSITIVE_ORDER);

			for(int k=0; k<names.size(); k++){

				System.out.println("names :" + names.get(k)+ "names sorted  "+ namesSorted.get(k));
				String s = names.get(k).toString().trim();
				String s2 = namesSorted.get(k).toString().trim();

				if(!(s.equalsIgnoreCase(s2)))
				{
					result=false;
					break;
				}
				else
					result=true;

			}

			if(result==true)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyPortfolioSortStrategyAsc -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}


	public String MyPortfolioSortAssetClassDsc() {
		log.debug("=============================");
		log.debug("MyPortfolioSortAssetClassDsc");
		List<String> names=new ArrayList<String>();

		List<String> names2=new ArrayList<String>();

		boolean result=false;

		try {

			List<WebElement> fundsList = driver.findElements(By.xpath("//*[@id='allPortfolioView']/descendant::tbody[@class='all_portfolio ng-scope']/descendant::td[@class='assetClass ng-binding']"));
			for(WebElement e : fundsList) {
				String value = e.getAttribute("innerHTML");
				System.out.println("value is :  " + value);
				log.debug("value is :  " + value);
				if (!value.isEmpty())
				{
					names.add(value);
				}
			}

			Collections.reverse(names);
			System.out.println(names.size());


			driver.findElement(By.xpath(OR.getProperty("aims.Portfolio.List.SortByAssetClass"))).click();
			Thread.sleep(WAIT2SEC);

			List<WebElement> fundsList2 = driver.findElements(By.xpath("//*[@id='allPortfolioView']/descendant::tbody[@class='all_portfolio ng-scope']/descendant::td[@class='assetClass ng-binding']"));
			for(WebElement e2 : fundsList2) {
				String value2 = e2.getAttribute("innerHTML");
				System.out.println("value is :  " + value2);
				log.debug("value is :  " + value2);
				if (!value2.isEmpty())
				{
					names2.add(value2);
				}
			}

			for(int k=0; k<names.size(); k++){

				System.out.println("names :" + names.get(k)+ "names2   "+ names2.get(k));
				String s = names.get(k).toString().trim();
				String s2 = names2.get(k).toString().trim();

				if(!(s.equalsIgnoreCase(s2)))
				{
					result=false;
					break;
				}
				else
					result=true;
			}	

			if(result==true)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyPortfolioSortAssetClassDsc -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}


	public String myPortfolioFundLink() {
		log.debug("=============================");
		log.debug("myPortfolioFundLink");

		try {
			String fundNamePortfolio = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();

			String arg1 = objectArr[0];
			log.debug(" object[0]  is  " + fundNamePortfolio +" and arg1 is " + arg1);
			Thread.sleep(WAIT1SEC);
			if(arg1.equalsIgnoreCase("aims.Portfolio.firstFundNameGrid.xpath"))
				driver.findElement(By.xpath(OR.getProperty("aims.Portfolio.firstFundIconGrid.xpath"))).click();
			else{
				driver.findElement(By.xpath(OR.getProperty("aims.Portfolio.firstFundIcon.xpath"))).click();
			}
			Thread.sleep(WAIT2SEC);
			String fundNameOnPopUp= driver.findElement(By.xpath(OR.getProperty("aims.Portfolio.fundIconPopUpMsg1FundName.xpath"))).getText();
			Thread.sleep(WAIT1SEC);
			driver.findElement(By.xpath(OR.getProperty("aims.Portfolio.fundIconPopUpYesButton.xpath"))).click();
			Thread.sleep(WAIT5SEC);
			String fundNameFundPage=driver.findElement(By.xpath(OR.getProperty("aims.fundProfile.pageTitle"))).getText();

			if(fundNamePortfolio.equalsIgnoreCase(fundNameOnPopUp) && fundNamePortfolio.equalsIgnoreCase(fundNameFundPage))
				return "Pass";
			else{
				log.debug("fund name on Portfolio landing page : " + fundNamePortfolio );
				log.debug("fund name on Portfolio Pop Up Overlay : " + fundNameOnPopUp );
				log.debug("fund name on Fund Landing page : " + fundNameFundPage );
				return "Fail";
			}

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing myPortfolioFundLink -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}




	public String myDropboxDefaultViewType() {
		log.debug("=============================");
		log.debug("Executing myDropboxDefaultViewType");

		String textOnOverviewPage,textOnDropboxPage = null;
		try {
			Thread.sleep(2000);
			textOnOverviewPage = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			Thread.sleep(2000);
			driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
			Thread.sleep(WAIT5SEC);
			textOnDropboxPage = driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxViewSelected"))).getText();


		} catch (Throwable t) {
			// error
			log.debug("Error while executing  myDropboxDefaultViewType - " + objectArr[0]);
			return "Fail";
		}
		log.debug("text On OverView Page  -  " + textOnOverviewPage);
		log.debug("text On Dropbox Page -  " + textOnDropboxPage);
		try {
			if(textOnDropboxPage.contains(textOnOverviewPage)){
				return "Pass";
			}
			else
				return "Fail";
		} catch (Throwable t) {
			// error
			log.debug("Error while executing  myDropboxDefaultViewType" + objectArr[0]);
			log.debug("text On OverView Page  -  " + textOnOverviewPage);
			log.debug("text On Dropbox Page -  " + textOnDropboxPage);
			return "Fail";
		}

	}



	public String MyDropboxDocCountAll() {
		log.debug("=============================");
		log.debug("MyDropboxDocCountAll");
		int docCountAll=0;
		int docCountPage=0;
		int pagesCounted=1;
		int totalPageClicks=0;

		try {
			String s=driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxTotalDocsShown"))).getText();
			String temp[] = s.split("of");
			String f = temp[1].trim();
			System.out.println(f);
			String f2= f.replaceAll("[^\\d.]", "");
			System.out.println(f2);
			int actualDocCount=Integer.parseInt(f2);

			if(actualDocCount>12){
				if((actualDocCount%12)==0)
					totalPageClicks=(actualDocCount/12)-1;
				else if((actualDocCount%12)>0)
					totalPageClicks=(actualDocCount/12);
			}
			else
				totalPageClicks=0;


			System.out.println(totalPageClicks);

			for(int pages=1; pages<=(totalPageClicks+1); pages++){
				//List<WebElement> fundsList = driver.findElements(By.tagName("li"));
				List<WebElement> fundsList = driver.findElements(By.xpath("//*[@id='timelineView']/descendant::li[contains(@class,'result_item')]"));
				for(WebElement e : fundsList) {
					if (e.getAttribute("class").startsWith("result_item")) {
						docCountAll++;
						docCountPage++;
						System.out.println("docountpage : " + docCountPage
								+ "   pagescounted :  " + pagesCounted
								+ "  doccount All : " + docCountAll);

						if (docCountPage == 12) {
							pagesCounted++;
							docCountPage = 0;
							if(docCountAll<actualDocCount){
								driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxPaginationRightArrow"))).click();
								Thread.sleep(WAIT3SEC);
							}
						}
					}
				}
			}

			if(!(actualDocCount==docCountAll)){
				log.debug("selected count : " + actualDocCount);
				log.debug("doc count : " + docCountAll);
				return "Fail";
			}
			else
				return "Pass";


		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyDropboxDocCountAll -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}

	}

	public String MyDropboxDocCountManagerAlerts() {
		log.debug("=============================");
		log.debug("MyDropboxDocCountManagerAlerts");
		int docCountAll=0;
		int docCountPage=0;
		int pagesCounted=1;
		int totalPageClicks=0;

		try {

			String s=driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxTotalDocsShown"))).getText();
			String temp[] = s.split("of");
			String f = temp[1].trim();
			System.out.println(f);
			String f2= f.replaceAll("[^\\d.]", "");
			System.out.println(f2);
			int actualDocCount=Integer.parseInt(f2);

			if(actualDocCount>12){
				if((actualDocCount%12)==0)
					totalPageClicks=(actualDocCount/12)-1;
				else if((actualDocCount%12)>0)
					totalPageClicks=(actualDocCount/12);
			}
			else
				totalPageClicks=0;

			System.out.println(totalPageClicks);

			for(int pages=1; pages<=(totalPageClicks+1); pages++){
				List<WebElement> fundsList = driver.findElements(By.xpath("//*[@id='timelineView']/descendant::li[contains(@class,'result_item')]"));
				for(WebElement e : fundsList) {
					if (e.getAttribute("class").contains("manager_alerts")) {
						docCountAll++;
						docCountPage++;
						System.out.println("docountpage : " + docCountPage
								+ "   pagescounted :  " + pagesCounted
								+ "  doccount All : " + docCountAll);

						if (docCountPage == 12) {
							pagesCounted++;
							docCountPage = 0;
							if(docCountAll<actualDocCount){
								driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxPaginationRightArrow"))).click();
								Thread.sleep(WAIT3SEC);
							}
						}
					}
				}
			}

			if(!(actualDocCount==docCountAll)){
				log.debug("selected count : " + actualDocCount);
				log.debug("doc count : " + docCountAll);
				return "Fail";
			}
			else
				return "Pass";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyDropboxDocCountManagerAlerts -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String MyDropboxDocCountPortfolioUpdates() {
		log.debug("=============================");
		log.debug("MyDropboxDocCountPortfolioUpdates");
		int docCountAll=0;
		int docCountPage=0;
		int pagesCounted=1;
		int totalPageClicks=0;

		try {

			String s=driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxTotalDocsShown"))).getText();
			String temp[] = s.split("of");
			String f = temp[1].trim();
			System.out.println(f);
			String f2= f.replaceAll("[^\\d.]", "");
			System.out.println(f2);
			int actualDocCount=Integer.parseInt(f2);

			if(actualDocCount>12){
				if((actualDocCount%12)==0)
					totalPageClicks=(actualDocCount/12)-1;
				else if((actualDocCount%12)>0)
					totalPageClicks=(actualDocCount/12);
			}
			else
				totalPageClicks=0;

			System.out.println(totalPageClicks);

			for(int pages=1; pages<=(totalPageClicks+1); pages++){
				List<WebElement> fundsList = driver.findElements(By.xpath("//*[@id='timelineView']/descendant::li[contains(@class,'result_item')]"));
				for(WebElement e : fundsList) {
					if (e.getAttribute("class").contains("portfolio_updates")) {
						docCountAll++;
						docCountPage++;
						System.out.println("docountpage : " + docCountPage
								+ "   pagescounted :  " + pagesCounted
								+ "  doccount All : " + docCountAll);

						if (docCountPage == 12) {
							pagesCounted++;
							docCountPage = 0;
							if(docCountAll<actualDocCount){
								driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxPaginationRightArrow"))).click();
								Thread.sleep(WAIT3SEC);
							}
						}
					}
				}
			}

			if(!(actualDocCount==docCountAll)){
				log.debug("selected count : " + actualDocCount);
				log.debug("doc count : " + docCountAll);
				return "Fail";
			}
			else
				return "Pass";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyDropboxDocCountPortfolioUpdates -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String MyDropboxDocCountTaxDocuments() {
		log.debug("=============================");
		log.debug("MyDropboxDocCountTaxDocuments");
		int docCountAll=0;
		int docCountPage=0;
		int pagesCounted=1;
		int totalPageClicks=0;

		try {
			/*String selectedType=driver.findElement(By.xpath(OR.getProperty("MyDropboxViewSelected"))).getText();
		int selectedTypeCount=Functions.getNumberWithinBrackets(selectedType);*/

			String s=driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxTotalDocsShown"))).getText();
			//String temp[] = s.split("\\(|\\)");
			String temp[] = s.split("of");
			String f = temp[1].trim();
			System.out.println(f);
			String f2= f.replaceAll("[^\\d.]", "");
			System.out.println(f2);
			int actualDocCount=Integer.parseInt(f2);

			if(actualDocCount>12){
				if((actualDocCount%12)==0)
					totalPageClicks=(actualDocCount/12)-1;
				else if((actualDocCount%12)>0)
					totalPageClicks=(actualDocCount/12);
			}
			else
				totalPageClicks=0;

			System.out.println(totalPageClicks);

			for(int pages=1; pages<=(totalPageClicks+1); pages++){
				List<WebElement> fundsList = driver.findElements(By.xpath("//*[@id='timelineView']/descendant::li[contains(@class,'result_item')]"));
				for(WebElement e : fundsList) {
					if (e.getAttribute("class").contains("tax_documents")) {
						docCountAll++;
						docCountPage++;
						System.out.println("docountpage : " + docCountPage
								+ "   pagescounted :  " + pagesCounted
								+ "  doccount All : " + docCountAll);

						if (docCountPage == 12) {
							pagesCounted++;
							docCountPage = 0;
							if(docCountAll<actualDocCount){
								driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxPaginationRightArrow"))).click();
								Thread.sleep(WAIT3SEC);
							}
						}
					}
				}
			}

			if(!(actualDocCount==docCountAll)){
				log.debug("selected count : " + actualDocCount);
				log.debug("doc count : " + docCountAll);
				return "Fail";
			}
			else
				return "Pass";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyDropboxDocCountTaxDocuments -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String MyDropboxDocCountManagerResearch() {
		log.debug("=============================");
		log.debug("MyDropboxDocCountManagerResearch");
		int docCountAll=0;
		int docCountPage=0;
		int pagesCounted=1;
		int totalPageClicks=0;

		try {
			/*String selectedType=driver.findElement(By.xpath(OR.getProperty("MyDropboxViewSelected"))).getText();
		int selectedTypeCount=Functions.getNumberWithinBrackets(selectedType);*/

			String s=driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxTotalDocsShown"))).getText();
			//String temp[] = s.split("\\(|\\)");
			String temp[] = s.split("of");
			String f = temp[1].trim();
			System.out.println(f);
			String f2= f.replaceAll("[^\\d.]", "");
			System.out.println(f2);
			int actualDocCount=Integer.parseInt(f2);

			if(actualDocCount>12){
				if((actualDocCount%12)==0)
					totalPageClicks=(actualDocCount/12)-1;
				else if((actualDocCount%12)>0)
					totalPageClicks=(actualDocCount/12);
			}
			else
				totalPageClicks=0;


			System.out.println(totalPageClicks);

			for(int pages=1; pages<=(totalPageClicks+1); pages++){
				List<WebElement> fundsList = driver.findElements(By.xpath("//*[@id='timelineView']/descendant::li[contains(@class,'result_item')]"));
				for(WebElement e : fundsList) {
					if (e.getAttribute("class").contains("manager_research")) {
						docCountAll++;
						docCountPage++;
						System.out.println("docountpage : " + docCountPage
								+ "   pagescounted :  " + pagesCounted
								+ "  doccount All : " + docCountAll);

						if (docCountPage == 12) {
							pagesCounted++;
							docCountPage = 0;
							if(docCountAll<actualDocCount){
								driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxPaginationRightArrow"))).click();
								Thread.sleep(WAIT3SEC);
							}
						}
					}
				}
			}

			if(!(actualDocCount==docCountAll)){
				log.debug("selected count : " + actualDocCount);
				log.debug("doc count : " + docCountAll);
				return "Fail";
			}
			else
				return "Pass";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyDropboxDocCountManagerResearch -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String MyDropboxDocCountReports() {
		log.debug("=============================");
		log.debug("MyDropboxDocCountReports");
		int docCountAll=0;
		int docCountPage=0;
		int pagesCounted=1;
		int totalPageClicks=0;

		try {
			/*String selectedType=driver.findElement(By.xpath(OR.getProperty("MyDropboxViewSelected"))).getText();
		int selectedTypeCount=Functions.getNumberWithinBrackets(selectedType);*/

			String s=driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxTotalDocsShown"))).getText();
			//String temp[] = s.split("\\(|\\)");
			String temp[] = s.split("of");
			String f = temp[1].trim();
			System.out.println(f);
			String f2= f.replaceAll("[^\\d.]", "");
			System.out.println(f2);
			int actualDocCount=Integer.parseInt(f2);

			if(actualDocCount>12){
				if((actualDocCount%12)==0)
					totalPageClicks=(actualDocCount/12)-1;
				else if((actualDocCount%12)>0)
					totalPageClicks=(actualDocCount/12);
			}
			else
				totalPageClicks=0;

			System.out.println(totalPageClicks);

			for(int pages=1; pages<=(totalPageClicks+1); pages++){
				List<WebElement> fundsList = driver.findElements(By.xpath("//*[@id='timelineView']/descendant::li[contains(@class,'result_item')]"));
				for(WebElement e : fundsList) {
					if (e.getAttribute("class").contains("reports")) {
						docCountAll++;
						docCountPage++;
						System.out.println("docountpage : " + docCountPage
								+ "   pagescounted :  " + pagesCounted
								+ "  doccount All : " + docCountAll);

						if (docCountPage == 12) {
							pagesCounted++;
							docCountPage = 0;
							if(docCountAll<actualDocCount){
								driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxPaginationRightArrow"))).click();
								Thread.sleep(WAIT3SEC);
							}
						}
					}
				}
			}

			if(!(actualDocCount==docCountAll)){
				log.debug("selected count : " + actualDocCount);
				log.debug("doc count : " + docCountAll);
				return "Fail";
			}
			else
				return "Pass";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyDropboxDocCountReports -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String MyDropboxDocCountAddedForYou() {
		log.debug("=============================");
		log.debug("MyDropboxDocCountAddedForYou");
		int docCountAll=0;
		int docCountPage=0;
		int pagesCounted=1;
		int totalPageClicks=0;

		try {
			/*String selectedType=driver.findElement(By.xpath(OR.getProperty("MyDropboxViewSelected"))).getText();
		int selectedTypeCount=Functions.getNumberWithinBrackets(selectedType);*/
			Actions dragger = new Actions(driver);
			WebElement draggablePartOfScrollbar = driver.findElement(By.xpath("//*[@id='dk_container_filterByTime']/div/div/div[1]/div/div"));

			// drag downwards
			int numberOfPixelsToDragTheScrollbarDown = 100;
			for (int i=10;i<=188;i=i+numberOfPixelsToDragTheScrollbarDown){
				// this causes a gradual drag of the scroll bar, 100px  at a time
				dragger.moveToElement(draggablePartOfScrollbar).clickAndHold().moveByOffset(0,numberOfPixelsToDragTheScrollbarDown).release().perform();
				Thread.sleep(2000L);
			} 
			driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxViewAddedForYou"))).click();

			String s=driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxTotalDocsShown"))).getText();
			//String temp[] = s.split("\\(|\\)");
			String temp[] = s.split("of");
			String f = temp[1].trim();
			System.out.println(f);
			String f2= f.replaceAll("[^\\d.]", "");
			System.out.println(f2);
			int actualDocCount=Integer.parseInt(f2);

			if(actualDocCount>12){
				if((actualDocCount%12)==0)
					totalPageClicks=(actualDocCount/12)-1;
				else if((actualDocCount%12)>0)
					totalPageClicks=(actualDocCount/12);
			}
			else
				totalPageClicks=0;

			System.out.println(totalPageClicks);

			for(int pages=1; pages<=(totalPageClicks+1); pages++){
				List<WebElement> fundsList = driver.findElements(By.xpath("//*[@id='timelineView']/descendant::li[contains(@class,'result_item')]"));
				for(WebElement e : fundsList) {
					if (e.getAttribute("class").contains("added_for_you")) {
						docCountAll++;
						docCountPage++;
						System.out.println("docountpage : " + docCountPage
								+ "   pagescounted :  " + pagesCounted
								+ "  doccount All : " + docCountAll);

						if (docCountPage == 12) {
							pagesCounted++;
							docCountPage = 0;
							if(docCountAll<actualDocCount){
								driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxPaginationRightArrow"))).click();
								Thread.sleep(WAIT3SEC);
							}
						}
					}
				}
			}

			if(!(actualDocCount==docCountAll)){
				log.debug("selected count : " + actualDocCount);
				log.debug("doc count : " + docCountAll);
				return "Fail";
			}
			else
				return "Pass";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyDropboxDocCountAddedForYou -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}


	public String myDropboxCheckNewDocs() {
		log.debug("=============================");
		log.debug("myDropboxCheckNewDocs");
		int docCountAll=0;
		int docCountPage=0;
		int pagesCounted=1;
		int totalPageClicks=0;
		boolean result=false;
		int newDocsCount=0;

		try {
			String newDocsString = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			int actualCountOnLandingPage = Integer.parseInt(newDocsString);
			String category = objectArr[1].substring(5);
			String categoryNewDocs=category+" new";

			System.out.println("category is : " + category  +" and  actual new doc count is :" +  actualCountOnLandingPage);
			System.out.println(categoryNewDocs);
			if(actualCountOnLandingPage==0){
				System.out.println("NO new Documents added for the category : " +  objectArr[0]);
				result = true;
			}
			else{
				driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
				Thread.sleep(WAIT5SEC);

				String s=driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxTotalDocsShown"))).getText();
				String temp[] = s.split("of");
				String f = temp[1].trim();
				System.out.println(f);
				String f2= f.replaceAll("[^\\d.]", "");
				System.out.println(f2);
				int actualDocCount=Integer.parseInt(f2);

				if(actualDocCount>12){
					if((actualDocCount%12)==0)
						totalPageClicks=(actualDocCount/12)-1;
					else if((actualDocCount%12)>0)
						totalPageClicks=(actualDocCount/12);
				}
				else
					totalPageClicks=0;

				System.out.println(totalPageClicks);

				for(int pages=1; pages<=(totalPageClicks+1); pages++){
					List<WebElement> fundsList = driver.findElements(By.xpath("//*[@id='timelineView']/descendant::li[contains(@class,'result_item')]"));
					for(WebElement e : fundsList) {
						if (e.getAttribute("class").contains(category)) {
							if(e.getAttribute("class").contains(categoryNewDocs))
								newDocsCount++;
							docCountAll++;
							docCountPage++;
							System.out.println("docountpage : " + docCountPage
									+ "   pagescounted :  " + pagesCounted
									+ "  doccount All : " + docCountAll);

							if (docCountPage == 12) {
								pagesCounted++;
								docCountPage = 0;
								if(docCountAll<actualDocCount){
									driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxPaginationRightArrow"))).click();
									Thread.sleep(WAIT3SEC);
								}
							}
						}
					}
				}

				if(!(actualCountOnLandingPage==newDocsCount)){
					log.debug("new doc count shown on landing page : " + actualCountOnLandingPage);
					log.debug("new doc count shown on dropbox page: " + newDocsCount);
					result=false;
				}
				else
					result=true;
			}

			if(result)
				return "Pass";
			else
				return "Fail";




		} catch (Throwable t) {
			// report error
			log.debug("Error while executing myDropboxCheckNewDocs -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}


	public String myDropboxOpenNewDocAndCheckCount() {
		log.debug("=============================");
		log.debug("myDropboxOpenNewDocAndCheckCount");
		int docCountAll=0;
		int docCountPage=0;
		int pagesCounted=1;
		int totalPageClicks=0;
		boolean result=false;
		int newDocsCount=0;

		try {
			String newDocsString = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			int actualCountOnLandingPage = Integer.parseInt(newDocsString);
			String category = objectArr[1].substring(5);
			String categoryNewDocs=category+" new";

			log.debug("category is : " + category  +" and  actual new doc count is :" +  actualCountOnLandingPage);
			//System.out.println(categoryNewDocs);
			if(actualCountOnLandingPage==0){
				log.debug("NO new Documents available for the category : " +  objectArr[0]);
				result = true;
			}
			else{
				driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
				Thread.sleep(WAIT5SEC);

				String s=driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxTotalDocsShown"))).getText();
				String temp[] = s.split("of");
				String f = temp[1].trim();
				System.out.println(f);
				String f2= f.replaceAll("[^\\d.]", "");
				System.out.println(f2);
				int actualDocCount=Integer.parseInt(f2);

				if(actualDocCount>12){
					if((actualDocCount%12)==0)
						totalPageClicks=(actualDocCount/12)-1;
					else if((actualDocCount%12)>0)
						totalPageClicks=(actualDocCount/12);
				}
				else
					totalPageClicks=0;

				System.out.println(totalPageClicks);

				outer:
					for(int pages=1; pages<=(totalPageClicks+1); pages++){
						List<WebElement> fundsList = driver.findElements(By.xpath("//*[@id='timelineView']/descendant::li[contains(@class,'result_item')]"));
						for(WebElement e : fundsList) {
							if (e.getAttribute("class").contains(category)) {
								if(e.getAttribute("class").contains(categoryNewDocs)){
									newDocsCount++;
									e.click();
									Thread.sleep(WAIT3SEC);
									driver.findElement(By.xpath(OR.getProperty("aims.global.viewButton.xpath"))).click();
									Thread.sleep(WAIT3SEC);
									driver.findElement(By.xpath(OR.getProperty("aims.global.closeIcon.xpath"))).click();
									Thread.sleep(WAIT3SEC);
									driver.findElement(By.xpath(OR.getProperty("aims.workspace.overview.xpath"))).click();
									Thread.sleep(WAIT5SEC);
									break outer;
								}
								docCountAll++;
								docCountPage++;
								System.out.println("docountpage : " + docCountPage
										+ "   pagescounted :  " + pagesCounted
										+ "  doccount All : " + docCountAll);

								if (docCountPage == 12) {
									pagesCounted++;
									docCountPage = 0;
									if(docCountAll<actualDocCount){
										driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxPaginationRightArrow"))).click();
										Thread.sleep(WAIT3SEC);
									}
								}
							}
						}
					}

				String newDocsString2 = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
				int actualCountOnLandingPage2 = Integer.parseInt(newDocsString2);
				System.out.println("doc count post access  : " + actualCountOnLandingPage2);
				log.debug("doc count post access  : " + actualCountOnLandingPage2);


				if(actualCountOnLandingPage2==(actualCountOnLandingPage-1)){
					log.debug("new doc count shown before access on landing page : " + actualCountOnLandingPage);
					log.debug("new doc count shown after access on dropbox page: " + newDocsCount);
					result=true;
				}
				else{
					log.debug("new doc count shown before access on landing page : " + actualCountOnLandingPage);
					log.debug("new doc count shown after access on dropbox page: " + newDocsCount);
					result=false;
				}
			}

			if(result)
				return "Pass";
			else
				return "Fail";




		} catch (Throwable t) {
			// report error
			log.debug("Error while executing myDropboxOpenNewDocAndCheckCount -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}



	public String MyDropboxSortByTitle() {
		log.debug("=============================");
		log.debug("MyDropboxSortByTitle");
		List<String> titles= new ArrayList<String>();	
		List<String> titlesSorted= new ArrayList<String>();
		boolean result=true;

		try {
			List<WebElement> t = driver.findElements(By.xpath("//div[@class='search_results']/descendant::h1[@class='title']"));
			Iterator<WebElement> i = t.iterator();

			while(i.hasNext()){
				WebElement e = i.next();

				if(!(e.getText().isEmpty())){
					log.debug("title : " + e.getText());
					titles.add(e.getText());
				}

			}

			titlesSorted.addAll(titles);
			//System.out.println(titles.size());
			//System.out.println(titlesSorted.size());
			Collections.sort(titlesSorted, String.CASE_INSENSITIVE_ORDER);

			for(int k=0; k<titles.size(); k++){

				log.debug("title : " + titles.get(k)+ "titles sorted  "+ titlesSorted.get(k));
				String s = titles.get(k).toString().trim();
				String s2 = titlesSorted.get(k).toString().trim();

				if(!(s.equalsIgnoreCase(s2)))
				{
					result=false;
					break;
				}
				else
					result=true;
			}

			if(result==true)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyDropboxSortByTitle -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}

	}

	public String MyDropboxSortByDate() {
		log.debug("=============================");
		log.debug("MyDropboxSortByDate");
		List<String> dates= new ArrayList<String>();	

		boolean result=true;

		try {
			List<WebElement> datesList = driver.findElements(By.xpath("//div[@class='search_results']/descendant::span[@class='date']"));
			Iterator<WebElement> i = datesList.iterator();
			while(i.hasNext()){
				WebElement e = i.next();

				if(!(e.getText().isEmpty())){
					log.debug("date : " +e.getText());
					dates.add(e.getText());
				}

			}

			Date finalDates[]= new Date[dates.size()];

			for(int d=0; d<dates.size(); d++){
				String s = dates.get(d).toString().trim();
				SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM dd yyyy");
				SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd yyyy");
				finalDates[d] = inputFormat.parse(s);
				outputFormat.format( finalDates[d]);
				String output = outputFormat.format( finalDates[d]);
				//System.out.println("final date : "+ output);
			}

			outer:
				for(int k=0; k<dates.size(); k++){
					for(int l=k+1; l<dates.size(); l++){
						//System.out.println("dates  :" + finalDates[k] +  "  "+ finalDates[l]);
						if(finalDates[k].before(finalDates[l])){
							log.debug(finalDates[k] + "  falls before " + finalDates[l]);
							result=false;
							break outer;
						}
					}
				}

			if(result==true)
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyDropboxSortByDate -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}

	}


	public String myDropboxDateChecker() {
		log.debug("=============================");
		log.debug("myDropboxDateChecker");
		List<String> dates = new ArrayList<String>();
		int docCountAll=0;
		int docCountPage=0;
		int pagesCounted=1;
		int totalPageClicks=0;
		boolean fromDateCompleted=false;
		boolean toDateCompleted=false;
		boolean result=false;

		try {
			String dayInput = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			String monthInput = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
			String yearInput = testData.getCellData(currentTest, data_column_nameArr[2],testRepeat);

			String whatToDo = objectArr[0];
			String verifyString = objectArr[1];
			int verify=0;

			if(verifyString.equalsIgnoreCase("aims.docCount"))
				verify=1;
			if(verifyString.equalsIgnoreCase("aims.dates"))
				verify=2;	

			System.out.println( dayInput + " "+ monthInput +" " + yearInput +  " " +  " " +whatToDo + " "+ verifyString );

			if(whatToDo.equalsIgnoreCase("aims.changeFromDate")  || whatToDo.equalsIgnoreCase("aims.changeBothDates")){
				System.out.println("inside from date");
				WebElement fromDateIcon = driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxFromDateSelector")));
				fromDateIcon.click();
				Thread.sleep(WAIT5SEC);
			}
			if(whatToDo.equalsIgnoreCase("aims.changeToDate") || fromDateCompleted==true){
				WebElement toDateIcon = driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxToDateSelector")));
				toDateIcon.click();
				Thread.sleep(WAIT5SEC);
				toDateCompleted=true;
			}

			//pick year as per input from data sheet
			WebElement yearPicker = driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxYearPicker")));
			yearPicker.click();
			Thread.sleep(WAIT3SEC);
			Select year = new Select(yearPicker);
			year.selectByVisibleText(yearInput);
			yearPicker = driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxYearPicker")));
			yearPicker.sendKeys(Keys.ENTER);
			Thread.sleep(WAIT3SEC);

			//pick month as per input from data sheet
			WebElement monthPicker = driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxMonthPicker")));
			monthPicker.click();
			Thread.sleep(WAIT3SEC);
			Select month = new Select(monthPicker);
			month.selectByVisibleText(monthInput);
			monthPicker = driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxMonthPicker")));
			monthPicker.sendKeys(Keys.ENTER);
			Thread.sleep(WAIT3SEC);



			//pick day as per input from data sheet
			List<WebElement> days = driver.findElements(By.xpath("//*[@id='ui-datepicker-div']/descendant::a[@class='ui-state-default' or @class='ui-state-default ui-state-active']"));
			for(WebElement day : days) {
				System.out.println("inside days");
				String actualDay = day.getText();
				System.out.println(actualDay);
				if(dayInput.equalsIgnoreCase(actualDay)){
					System.out.println(dayInput + " " + actualDay);
					day.click();
					if(whatToDo.equalsIgnoreCase("aims.changeBothDates")){
						if(! toDateCompleted)
							fromDateCompleted=true;
					}
					Thread.sleep(WAIT2SEC);
					break;
				}
			}

			Thread.sleep(WAIT2SEC);

			String s=driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxTotalDocsShown"))).getText();
			String temp[] = s.split("of");
			String f = temp[1].trim();
			System.out.println(f);
			String f2= f.replaceAll("[^\\d.]", "");
			System.out.println(f2);
			int actualDocCount=Integer.parseInt(f2);

			if(actualDocCount>12){
				if((actualDocCount%12)==0)
					totalPageClicks=(actualDocCount/12)-1;
				else if((actualDocCount%12)>0)
					totalPageClicks=(actualDocCount/12);
			}
			else
				totalPageClicks=0;


			System.out.println(totalPageClicks);

			for(int pages=1; pages<=(totalPageClicks+1); pages++){
				System.out.println("pages counted " + 1);
				//*[@id='timelineView']/descendant::li[contains(@class, 'result_item')]
				//*[@id='timelineView']/descendant::li[contains(@class, 'result_item')]/descendant::span[@class='search_sort_date']
				List<WebElement> docsList = driver.findElements(By.xpath("//*[@id='timelineView']/descendant::li[contains(@class, 'result_item')]/descendant::span[@class='date']"));
				for(WebElement e : docsList) {
					String value = e.getText();
					System.out.println("value is " + value);
					dates.add(value);
					docCountAll++;
					docCountPage++;
					/*System.out.println("docountpage : " + docCountPage
										+ "   pagescounted :  " + pagesCounted
										+ "  doccount All : " + docCountAll);*/

					if (docCountPage == 12) {
						pagesCounted++;
						docCountPage = 0;
						if(docCountAll<actualDocCount){
							driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxPaginationRightArrow"))).click();
							Thread.sleep(WAIT3SEC);
						}
					}

				}
			}

			switch(verify){
			case 1:
				System.out.println("inside case 1 ");
				if(!(actualDocCount==docCountAll)){
					log.debug("selected count : " + actualDocCount);
					log.debug("doc count : " + docCountAll);
					result =false;
				}
				else{
					System.out.println("selected count : " + actualDocCount);
					System.out.println("doc count : " + docCountAll);
					result = true;
				}
				break;


			case 2:
				System.out.println("inside case 2");

				String fromDateString = driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxFromDateInput"))).getAttribute("value");
				String toDateString = driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxToDateInput"))).getAttribute("value");
				fromDateString = fromDateString.replaceAll("/", " ");
				toDateString = toDateString.replaceAll("/", " ");
				System.out.println(fromDateString + " " + toDateString);
				System.out.println(dates.size());
				Date finalDates[]= new Date[dates.size()];
				Date finalFromDate = new Date();
				Date finalToDate = new Date();
				SimpleDateFormat inputFormat = new SimpleDateFormat("MM dd yyyy");
				SimpleDateFormat outputFormat = new SimpleDateFormat("MM dd yyyy");
				finalFromDate = inputFormat.parse(fromDateString);
				finalToDate = inputFormat.parse(toDateString);
				outputFormat.format( finalFromDate);
				outputFormat.format( finalToDate);

				SimpleDateFormat inputFormat2 = new SimpleDateFormat("MMMM dd yyyy");
				SimpleDateFormat outputFormat2 = new SimpleDateFormat("MMMM dd yyyy");

				for(int d=0; d<dates.size(); d++){
					String s1 = dates.get(d).toString();
					finalDates[d] = inputFormat2.parse(s1);
					outputFormat2.format( finalDates[d]);
					String output = outputFormat.format( finalDates[d]);
					System.out.println("final date : "+ output);
				}


				for(int k=0; k<dates.size(); k++){
					System.out.println("dates  :" + finalDates[k] );
					if(finalDates[k].before(finalFromDate) &&  finalDates[k].after(finalToDate)){
						log.debug(finalDates[k] + "  is not in the chosen range " +finalFromDate + " - " + finalToDate );
						result=false;
						break ;
					}
					else
						result=true;
				}
				break;
			}
			if(result)	
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyDropboxDateChecker -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}

	}


	public String MyDropboxSubSearch() {
		log.debug("=============================");
		log.debug("MyDropboxSubSearch");
		boolean result=true;

		try {
			String inputText= testData.getCellData(currentTest, "SearchInput",testRepeat);
			List<WebElement> t = driver.findElements(By.xpath("//div[@class='search_results']/descendant::h1[@class='title']"));
			Iterator<WebElement> i = t.iterator();

			while(i.hasNext()){
				WebElement e = i.next();

				if(!(e.getText().isEmpty())){

					String title = e.getText().toLowerCase();
					log.debug("text is " + title);
					if(!(title.contains(inputText))){
						result=false;
						break;
					}
				}

			}

			if(result==true)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyDropboxSubSearch -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String clickAddToLibrary(){

		log.debug("=============================");
		log.debug("Executing clickAddToLibrary Keyword");
		boolean result= false;

		try {
			String entity =driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			log.debug("entity text is : " + entity);	
			if(entity.contains("Remove from Library")){
				System.out.println("already added... so remove and add");
				log.debug("already added... so remove and add");
				driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
				Thread.sleep(WAIT3SEC);
				try{
					result = Functions.handleCustomPlaylistPopUp(driver, log, CONFIG);
					log.debug("custom pop up presence is : " + result);
				}catch(Throwable t){
					//pop-up didn't came
					result=false;
					log.debug("Throwable t message : " + t.getMessage());
				}
				if(result){
					Thread.sleep(WAIT3SEC);
					log.debug("Playlist pop up came and result : " + result);												
					//its a part of custom playlist so it can't be removed.
					return "Fail";
				}			
				else 
				{
					Thread.sleep(WAIT3SEC);
					log.debug("Pop up didnt came and result : " + result);
					//removed and so adding again.
					driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
					Thread.sleep(WAIT3SEC);
					log.debug("new entity text is : " + driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText());
					if(driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText().equals("Remove from Library"))
						return "Pass";
					else
						return "Fail";
				}
			}
			else{
				System.out.println("not added .. so add");
				log.debug("not added .. so add");
				driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
				Thread.sleep(WAIT3SEC);
				log.debug("new entity text is : " + driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText());
				if(driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText().equals("Remove from Library"))
					return "Pass";
				else
					return "Fail";
			}

		}catch (Throwable t) {
			// report error
			log.debug("Error while executing clickAddToLibrary -" +  t.getMessage());
			return "Fail";
		}

	}

	public String addToLibrary(){

		log.debug("=============================");
		log.debug("Executing addToLibrary Keyword");
		//this keywords adds a manager playlist to library while traversing all the available playlists
		try {

			List<WebElement> elements = driver.findElements(By.xpath(OR.getProperty(objectArr[0])));
			log.debug(elements.size());
			for(int k=0;k<elements.size();k++){	
				Thread.sleep(WAIT2SEC);
				elements.get(k).click();
				Thread.sleep(WAIT2SEC);
				String entity =driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).getText();
				log.debug("entity text is : " + entity);	
				if(entity.contains("Add to Library")){
					driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).click();
					Thread.sleep(WAIT3SEC);
					log.debug("new entity text is : " + driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).getText());
					if(driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).getText().equals("Remove from Library"))
					{
						return "Pass";
					}
				}
				else{
					driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).click();
					Thread.sleep(WAIT1SEC);
					driver.findElement(By.xpath(OR.getProperty(objectArr[3]))).click();
					Thread.sleep(WAIT1SEC);
				}
				elements = driver.findElements(By.xpath(OR.getProperty(objectArr[0])));
			}
			return "Fail";
		}catch (Throwable t) {
			// report error
			log.debug("Error while executing addToLibrary -" +  t.getMessage());
			return "Fail";
		}

	}


	public String clickRemoveFromLibrary(){
		log.debug("=============================");
		log.debug("Executing clickRemoveFromLibrary Keyword");

		try {
			String entity =driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			log.debug("text coming as : " + entity);

			if(entity.contains("Remove from Library")){

				driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
				Thread.sleep(WAIT3SEC);

				if(Functions.handleCustomPlaylistPopUp(driver, log, CONFIG))
				{	//do nothing

					log.debug("Custom playlist pop up came. The keyword will fail.");
					return "Fail";
				}
				else{
					Thread.sleep(WAIT3SEC);
					log.debug("Custom playlist pop up didnt came");
					log.debug("Item clicked and removed");
				}

			}else if(entity.contains("Add to Library")){
				driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
				Thread.sleep(WAIT3SEC);
				driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
				Thread.sleep(WAIT3SEC);
				log.debug("Item has been removed from library");
			}

			String newEntity =driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			if(newEntity.equals("Add to Library"))
				return "Pass";
			else
				return "Fail";

		}catch(Throwable t) {
			// report error
			log.debug("Object used in the keyword : " + objectArr[0]);
			log.debug("Error while executing clickRemoveFromLibrary -" +  t.getMessage());
			return "Fail : this is not a bug";
		}


	}

	public String setLibraryEntity(){

		log.debug("=============================");
		log.debug("Executing setLibraryEntity Keyword");

		try {
			String expectedEntity =driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getAttribute("title");
			try {
				String subsearchText = driver.findElement(By.xpath(OR.getProperty("aims.searchResults.searchTextField.xpath"))).getAttribute("value");
				System.out.println("sub search  : " + subsearchText);
			}catch(Throwable e) {
				//do nothing
			}
			System.out.println("set expectedEntity  " + expectedEntity);
			log.debug("set expectedEntity  " +  expectedEntity);
			testData.setCellData(currentTest, data_column_nameArr[0], testRepeat, expectedEntity);
			//		Thread.sleep(WAIT2SEC);
			String expectedEntity2=testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			System.out.println("get expectedEntity  " + expectedEntity2);
			log.debug("get expectedEntity " + expectedEntity2);

		}catch (Throwable t) {
			// report error
			log.debug("Error while executing setLibraryEntity -" +  t.getMessage());
			return "Fail";
		}
		return "Pass";
	}


	public String setLibraryEntity2(){

		log.debug("=============================");
		log.debug("Executing setLibraryEntity2 Keyword");

		try {
			String expectedEntity =driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			try {
				String subsearchText = driver.findElement(By.xpath(OR.getProperty("aims.searchResults.searchTextField.xpath"))).getAttribute("value");
				System.out.println("sub search  : " + subsearchText);
			}catch(Throwable e) {
				log.debug("sub search text value not available ");
			}
			System.out.println("set expectedEntity  " + expectedEntity);
			log.debug("set expectedEntity  " +  expectedEntity);
			testData.setCellData(currentTest, data_column_nameArr[0], testRepeat, expectedEntity);
			Thread.sleep(WAIT2SEC);
			String expectedEntity2=testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			System.out.println("get expectedEntity  " + expectedEntity2);
			log.debug("get expectedEntity " + expectedEntity2);

		}catch (Throwable t) {
			// report error
			log.debug("Error while executing setLibraryEntity2 -" +  t.getMessage());
			return "Fail";
		}
		return "Pass";
	}
	public String loadAllItems() {
		log.debug("=============================");
		log.debug("executing loadAllItems");

		try {
			String rowsLocator = OR.getProperty(objectArr[0]);
			String dragThumbLocator = OR.getProperty(objectArr[1]);
			String totalRowCount = driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).getText();
			int splitTotalRows = Integer.parseInt(totalRowCount);
			log.debug("totalRowCount " + splitTotalRows);
			System.out.println("totalRowCount " + splitTotalRows);
			Functions.dragTillAllRowsLoaded(driver, log, rowsLocator, dragThumbLocator, splitTotalRows, 100);
			return "Pass";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing loadAllItems -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}


	public String MyLibrary_EntityAdded() {
		log.debug("=============================");
		log.debug("MyLibrary_EntityAdded");

		boolean result=false;

		try {

			String expectedValue = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			log.debug("expected value is between colons :"+expectedValue+":");
			if(expectedValue=="" || expectedValue.equals(""))
				return "Fail -expected val is Null";

			Thread.sleep(WAIT5SEC);
			driver.findElement(By.xpath(OR.getProperty("aims.library.myLibraryListViewButton.xpath"))).click();
			Thread.sleep(500);

			String rowsLocator = "//tbody[@class='item_list filterSearch']/tr";
			String dragThumbLocator = "//div[@id='listView']/descendant::div[@class='thumb']";
			String totalRowCount = driver.findElement(By.xpath("//article[@id='library-items']/descendant::h1/span[2]")).getText();
			int splitTotalRows = Functions.getNumberWithinBrackets(totalRowCount);
			log.debug("totalRowCount " + splitTotalRows);
			System.out.println("totalRowCount " + splitTotalRows);
			Functions.dragTillAllRowsLoaded(driver, log, rowsLocator, dragThumbLocator, splitTotalRows, 100);

			List<WebElement> fundsList3 = driver.findElements(By.xpath("//td[@class='name']/span[@class='ng-binding']"));

			System.out.println("list size : " + fundsList3.size());
			log.debug("list size : " + fundsList3.size());

			for (WebElement item : fundsList3) {
				String value = item.getAttribute("innerHTML");

				System.out.println("value is :  " + value);
				log.debug("value is :  " + value);

				if(expectedValue.trim().equalsIgnoreCase(value.trim())) {
					System.out.println("expected : " + expectedValue + "   actual : " + value);
					log.debug("expected : " + expectedValue + "   actual : " + value);
					result=true;
					break;
				}else {
					result = false;
				}
			}

			if(result==true)
				return "Pass";
			else {
				log.debug("The expected item is not present in the library. It should had been added.");
				return "Fail";}

		} catch (Throwable t) {
			log.debug("Error while executing MyLibrary_EntityAdded -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}



	public String MyLibrary_EntityRemoved() {
		log.debug("=============================");
		log.debug("MyLibrary_EntityRemoved");

		boolean result=false;

		try {
			String expectedValue = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			log.debug("expected value is between colons :"+expectedValue+":");
			if(expectedValue=="" || expectedValue.equals(""))
				return "Fail -expected val is Null";

			driver.findElement(By.xpath(OR.getProperty("aims.library.myLibraryListViewButton.xpath"))).click();
			Thread.sleep(500);

			String rowsLocator = "//tbody[@class='item_list filterSearch']/tr";
			String dragThumbLocator = "//div[@id='listView']/descendant::div[@class='thumb']";
			String totalRowCount = driver.findElement(By.xpath("//article[@id='library-items']/descendant::h1/span[2]")).getText();

			int splitTotalRows = Functions.getNumberWithinBrackets(totalRowCount);
			log.debug("totalRowCount " + splitTotalRows);
			System.out.println("totalRowCount " + splitTotalRows);
			Functions.dragTillAllRowsLoaded(driver, log, rowsLocator, dragThumbLocator, splitTotalRows, 100);

			List<WebElement> fundsList3 = driver.findElements(By.xpath("//td[@class='name']/span[@class='ng-binding']"));

			System.out.println("list size : " + fundsList3.size());
			log.debug("list size : " + fundsList3.size());

			for (WebElement item : fundsList3) {
				String value = item.getAttribute("innerHTML");

				System.out.println("value is :  " + value);
				log.debug("value is :  " + value);

				if(expectedValue.trim().equalsIgnoreCase(value.trim())) {
					System.out.println("expected : " + expectedValue + "   actual : " + value);
					log.debug("expected : " + expectedValue + "   actual : " + value);
					result=false;
					break;
				}else {
					result = true;
				}
			}

			if(result)
				return "Pass";
			else {
				log.debug("The expected item is still present in the library. It should had been removed.");
				return "Fail";}

		} catch (Throwable t) {
			log.debug("Error while executing MyLibrary_EntityRemoved -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}


	public String MyLibrary_EntityRemoved_backUp() {//shoud discuss with rahul the purpose and significance of getting data-asset-href
		log.debug("=============================");
		log.debug("MyLibrary_EntityRemoved");

		boolean result=false;
		String actualPath = null;

		try {
			String expectedPath = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			System.out.println("in MyLibrary_EntityRemoved");
			log.debug("in MyLibrary_EntityRemoved");

			List<WebElement> fundsList3 = driver.findElements(By.xpath("//tbody[@class='item_list filterSearch']/tr"));
			System.out.println("list size : " + fundsList3.size());
			for(WebElement e4 : fundsList3) {
				actualPath = e4.getAttribute("data-asset-href");
				System.out.println("actual Path is : "+ actualPath);
				if(expectedPath.equals(actualPath)) {
					System.out.println("item still present in lib. Expected path is : " + expectedPath + "and actual Path is : "+ actualPath);
					log.debug("item still present in lib. Expected path is : " + expectedPath + "and actual Path is : "+ actualPath);
				}
				else{
					result = true;
				}
			}

			if(result==true) {
				log.debug("item not present in lib. This is expected. Expected path is : " + expectedPath + "and actual Path is : "+ actualPath);
				return "Pass";
			}
			else {
				log.debug("item still present in lib. This is not expected. Expected path is : " + expectedPath + "and actual Path is : "+ actualPath);
				return "Fail";
			}

		}catch (Throwable t) {
			// report error
			log.debug("Error while executing MyLibrary_EntityRemoved -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String MyLibrary_SortNameAsc() {
		log.debug("=============================");
		log.debug("MyLibrary_SortName");
		List<String> names=new ArrayList<String>();
		List<String> namesSorted=new ArrayList<String>();

		boolean result=false;

		try {
			List<WebElement> fundsList3 = driver.findElements(By.xpath("//*[@id='list-scroll']/descendant::span[@class='ng-binding']"));
			System.out.println("list size : " + fundsList3.size());
			for(WebElement e4 : fundsList3) {

				String value = e4.getAttribute("innerHTML");
				System.out.println("value is :  " + value);

				if(! value.isEmpty()){
					names.add(value);

				}
			}
			namesSorted.addAll(names);
			System.out.println(names.size());
			System.out.println(namesSorted.size());
			Collections.sort(namesSorted, String.CASE_INSENSITIVE_ORDER);
			Thread.sleep(WAIT4SEC);
			for(int k=0; k<names.size(); k++){

				//System.out.println("names :  " + names.get(k)+ "  names sorted  "+ namesSorted.get(k));
				String s = names.get(k).toString().trim();
				String s2 = namesSorted.get(k).toString().trim();
				if(!(s.equalsIgnoreCase(s2)))
				{
					log.debug("FAILED at  values :   " + names.get(k)+ "  names sorted  "+ namesSorted.get(k));
					result=false;
					break;
				}
				else
					result=true;
			}
			if(result==true)
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyLibrary_SortName -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String MyLibrary_SortAuthorAsc() {
		log.debug("=============================");
		log.debug("MyLibrary_SortAuthorAsc");
		List<String> names=new ArrayList<String>();
		List<String> namesSorted=new ArrayList<String>();

		boolean result=false;

		try {

			List<WebElement> fundsList3 = driver.findElements(By.xpath("//*[@id='list-scroll']/descendant::td[@class='author ng-binding']"));
			log.debug("list size : " + fundsList3.size());
			for(WebElement e4 : fundsList3) {

				String value = e4.getAttribute("innerHTML");
				log.debug("value is :  " + value);

				if(! value.isEmpty()){
					names.add(value);

				}

			}
			namesSorted.addAll(names);
			System.out.println(names.size());
			log.debug(names.size());
			System.out.println(namesSorted.size());
			log.debug("names sorted : " + namesSorted.size());
			Collections.sort(namesSorted, String.CASE_INSENSITIVE_ORDER);
			Thread.sleep(WAIT4SEC);
			for(int k=0; k<names.size(); k++){

				//System.out.println("names :  " + names.get(k)+ "  names sorted  "+ namesSorted.get(k));
				String s = names.get(k).toString().trim();
				String s2 = namesSorted.get(k).toString().trim();
				if(!(s.equalsIgnoreCase(s2)))
				{
					log.debug("FAILED at  values :   " + names.get(k)+ "  names sorted  "+ namesSorted.get(k));
					result=false;
					break;
				}
				else
					result=true;
			}
			if(result==true)
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyLibrary_SortAuthorAsc -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}
	public String MyLibrary_SortDateAsc() {
		log.debug("=============================");
		log.debug("MyLibrary_SortDateAsc");
		List<String> dates=new ArrayList<String>();


		boolean result=true;

		try {

			List<WebElement> fundsList3 = driver.findElements(By.xpath("//*[@id='list-scroll']/descendant::td[@class='date ng-binding']"));
			//System.out.println("list size : " + fundsList3.size());
			for(WebElement e4 : fundsList3) {

				String value = e4.getAttribute("innerHTML");
				//System.out.println("value is :  " + value);

				if(! value.isEmpty()){
					dates.add(value);
				}

			}

			Date finalDates[]= new Date[dates.size()];

			for(int d=0; d<dates.size(); d++){
				String s = dates.get(d).toString().trim();
				SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM dd yyyy");
				SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd yyyy");
				finalDates[d] = inputFormat.parse(s);
				outputFormat.format( finalDates[d]);
				//String output = outputFormat.format( finalDates[d]);
				//System.out.println("final date : "+ output);
			}

			outer:
				for(int k=0; k<dates.size(); k++){
					for(int l=k+1; l<dates.size(); l++){
						log.debug("dates  :" + finalDates[k] +  "  "+ finalDates[l]);
						if(finalDates[k].after(finalDates[l])){
							log.debug(finalDates[k] + "  falls after " + finalDates[l]);
							result=false;
							break outer;
						}
					}
				}

			if(result==true)
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyLibrary_SortDateAsc -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String MyLibrary_SortNameDsc() {
		log.debug("=============================");
		log.debug("MyLibrary_SortNameDsc");
		List<String> names=new ArrayList<String>();
		List<String> names2=new ArrayList<String>();

		int count=0;

		boolean result=false;

		try {

			List<WebElement> fundsList3 = driver.findElements(By.xpath("//*[@id='list-scroll']/descendant::span[@class='ng-binding']"));
			System.out.println("list size : " + fundsList3.size());
			for(WebElement e4 : fundsList3) {

				String value = e4.getAttribute("innerHTML");
				System.out.println("value is :  " + value);
				count++;
				if(! value.isEmpty()){
					names.add(value);

				}
			}
			names2.addAll(names);
			Collections.sort(names2, String.CASE_INSENSITIVE_ORDER);
			Collections.reverse(names2);

			for (int k = 0; k < names.size(); k++) {

				//System.out.println("names :" + names.get(k) + "names2   " 	+ names2.get(k));
				String s = names.get(k).toString().trim();
				String s2 = names2.get(k).toString().trim();

				if (!(s.equalsIgnoreCase(s2))) {
					log.debug("FAILED at  values :   " + names.get(k)+ "  names sorted  "+ names2.get(k));
					result = false;
					break;
				} else
					result = true;
			}

			if (result)
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyLibrary_SortNameDsc -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String MyLibrary_SortAuthorDsc() {
		log.debug("=============================");
		log.debug("MyLibrary_SortAuthorDsc");
		List<String> names=new ArrayList<String>();
		List<String> names2=new ArrayList<String>();


		boolean result=false;

		try {

			List<WebElement> fundsList3 = driver.findElements(By.xpath("//*[@id='list-scroll']/descendant::td[@class='author ng-binding']"));
			log.debug("list size : " + fundsList3.size());
			for(WebElement e4 : fundsList3) {
				String value = e4.getAttribute("innerHTML");
				log.debug("value is :  " + value);

				if(! value.isEmpty()){
					names.add(value);

				}

			}
			names2.addAll(names);
			Collections.sort(names2, String.CASE_INSENSITIVE_ORDER);
			Collections.reverse(names2);

			for (int k = 0; k < names.size(); k++) {

				//System.out.println("names :" + names.get(k) + "names2   " + names2.get(k));
				String s = names.get(k).toString().trim();
				String s2 = names2.get(k).toString().trim();

				if (!(s.equalsIgnoreCase(s2))) {
					log.debug("FAILED at  values :   " + names.get(k)+ "  names sorted  "+ names2.get(k));
					result = false;
					break;
				} else
					result = true;
			}

			if (result == true)
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyLibrary_SortAuthorDsc -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String MyLibrary_SortDateDsc() {
		log.debug("=============================");
		log.debug("MyLibrary_SortDateDsc");
		List<String> dates=new ArrayList<String>();


		boolean result=true;

		try {

			List<WebElement> fundsList3 = driver.findElements(By.xpath("//*[@id='list-scroll']/descendant::td[@class='date ng-binding']"));
			//System.out.println("list size : " + fundsList3.size());
			for(WebElement e4 : fundsList3) {
				String value = e4.getAttribute("innerHTML");
				//System.out.println("value is :  " + value);

				if(! value.isEmpty()){
					dates.add(value);

				}

			}
			Date finalDates[]= new Date[dates.size()];

			for(int d=0; d<dates.size(); d++){
				String s = dates.get(d).toString().trim();
				SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM dd yyyy");
				SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd yyyy");
				finalDates[d] = inputFormat.parse(s);
				outputFormat.format( finalDates[d]);
				//String output = outputFormat.format( finalDates[d]);
				//System.out.println("final date : "+ output);
			}

			outer:
				for(int k=0; k<dates.size(); k++){
					for(int l=k+1; l<dates.size(); l++){
						log.debug("dates  :" + finalDates[k] +  "  "+ finalDates[l]);
						if(finalDates[k].before(finalDates[l])){
							log.debug(finalDates[k] + "  falls before " + finalDates[l]);
							result=false;
							break outer;
						}
					}
				}

			if(result==true)
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyLibrary_SortDateDsc -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String verifyIntValue() {
		log.debug("=============================");
		log.debug("Executing verifyIntValue");

		try {
			int expectedValue = driver.findElements(By.xpath(OR.getProperty(objectArr[0]))).size();
			String data = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			if ( data.equals("Four"))
			{ 
				if(expectedValue<=4){ return "Pass";   }
				else {
					log.debug("actual value" +expectedValue );
					return "Fail";
				}	
			}
			int actualValue = Integer.parseInt(driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).getText());

			if(expectedValue == actualValue) 
				return "Pass";
			else {
				log.debug("expected value:"+ expectedValue + "actual value" + actualValue );
				return "Fail";
			}	

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing verifyIntValue -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}


	public String MyLibrary_GridViewSortByTitle() {
		log.debug("=============================");
		log.debug("MyLibrary_GridViewSortByTitle");
		List<String> names=new ArrayList<String>();
		List<String> namesSorted=new ArrayList<String>();

		boolean result=false;

		try {

			List<WebElement> fundsList3 = driver.findElements(By.xpath("//*[@id='grid-scroll']/descendant::span[@class='libTitle ng-binding']"));
			log.debug("list size : " + fundsList3.size());
			for(WebElement e4 : fundsList3) {
				if(! e4.getAttribute("innerHTML").isEmpty()) {
					String value = e4.getAttribute("innerHTML");

					log.debug("value is :  " + value);
					names.add(value);

					/*				if(count==12){
					Actions dragger = new Actions(driver);
					WebElement draggablePartOfScrollbar = driver.findElement(By.xpath("//*[@id='gridView']/div/div/div[1]/div/div"));

					// drag downwards
					int numberOfPixelsToDragTheScrollbarDown = 100;
					for (int i=10;i<=236;i=i+numberOfPixelsToDragTheScrollbarDown){
						// this causes a gradual drag of the scroll bar, 100px  at a time
						dragger.moveToElement(draggablePartOfScrollbar).clickAndHold().moveByOffset(0,numberOfPixelsToDragTheScrollbarDown).release().perform();
						Thread.sleep(2000L);
					} 
				}*/

				}
			}

			namesSorted.addAll(names);
			System.out.println(names.size());
			System.out.println(namesSorted.size());
			Collections.sort(namesSorted, String.CASE_INSENSITIVE_ORDER);
			Thread.sleep(WAIT4SEC);
			for(int k=0; k<names.size(); k++){

				log.debug("names :  " + names.get(k)+ "  names sorted  "+ namesSorted.get(k));
				String s = names.get(k).toString().trim();
				String s2 = namesSorted.get(k).toString().trim();
				if(!(s.equalsIgnoreCase(s2)))
				{
					result=false;
					break;
				}
				else
					result=true;
			}
			if(result==true)
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyLibrary_GridViewSortByTitle -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String clickIfElementPresent() {
		log.debug("=============================");
		log.debug("Executing clickIfElementPresent");
		try {
			getWebElement(OR, objectArr[0]).click();
			return "Pass";
		} catch (Throwable t) {
			// report error
			log.debug("Element not present, hence will not trigger the click event.");
			return "Pass";
		}
	}

	public String verifyManagerAndFund() {
		log.debug("=============================");
		log.debug("Executing function verifyManagerAndFund");

		List<WebElement> items;
		String itemType, subNavLink;

		try {

			Thread.sleep(WAIT3SEC);

			items = driver.findElements(By.xpath(OR.getProperty("aims.managersFunds.playlistItem.xpath")));

			for(int j=1; j<=items.size();j++) {

				itemType = driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.playlistItem.xpath")+"[" + j + "]")).getAttribute("data-doctype");

				driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.playlistItem.xpath")+"[" + j + "]")).click();

				if(itemType.equalsIgnoreCase("manager")) {
					subNavLink = "aims.managersFunds.manager.xpath";
				}else {
					subNavLink = "aims.managersFunds.fund.xpath";
				}

				Thread.sleep(WAIT3SEC);

				if(!driver.findElement(By.xpath(OR.getProperty(subNavLink))).getCssValue("color").equals("rgba(237, 121, 10, 1)")) {
					driver.navigate().back();
					Thread.sleep(WAIT5SEC);			
					log.debug("Verifying Playlist: " + driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.playlistTitle.xpath"))).getText() + " and Item: " + driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.playlistItem.xpath")+"[" + j + "]")).getAttribute("data-title"));

					return "Fail";
				}else {
					driver.navigate().back();
					Thread.sleep(WAIT5SEC);			
				}
			}



			return "Pass";

		} catch (Throwable t) {
			log.debug("An error has occurred while verifying the Manager/Fund.");
			return "Fail";
		}
	}

	public String verifyFirstViewAll_ManagersAndFunds() {
		log.debug("=============================");
		log.debug("Executing verifyFirstViewAll_ManagersAndFunds");



		try {
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.firstViewAll.xpath"))).click();
			Thread.sleep(WAIT8SEC);

			List<WebElement> AllPlaylists = driver.findElements(By.xpath(OR.getProperty("aims.managersFunds.playlistItem.xpath")));

			if(AllPlaylists.size()!=0){

				for(int i=1;i<=AllPlaylists.size();i++) {

					log.debug("Verifying Playlist: " + i);

					Thread.sleep(WAIT3SEC);

					driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.playlistItem.xpath")+"[" + i + "]")).click();

					if(verifyManagerAndFund().equals("Fail")){
						return "Fail";
					}
					driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.closeIcon.xpath"))).click();

				}

			}else{

				driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.closeIcon.xpath"))).click();

				Thread.sleep(WAIT3SEC);

				log.debug("No data present to verify.");

				return "Fail";
			}

			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.closeIcon.xpath"))).click();

			Thread.sleep(WAIT3SEC);
			return "Pass";

		} catch (Throwable t) {
			log.debug("An error has occurred while verifying the Manager/Fund for all the items of first View All.");
			return "Fail";
		}
	}

	public String verifyViewAll_ManagersAndFunds() {
		log.debug("=============================");
		log.debug("Executing verifyViewAll_ManagersAndFunds");

		Actions dragger = new Actions(driver);
		int count;
		int numberOfPixelsToDragTheScrollbarDown = 0;

		WebElement draggablePartOfScrollbar;
		try {
			List<WebElement> ViewAlls = driver.findElements(By.xpath(OR.getProperty("aims.managersFunds.viewAll.xpath")));
			for(int i=1;i<=ViewAlls.size();i++) {

				log.debug("Verifying View All: " + i);

				Thread.sleep(WAIT3SEC);

				if(!driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.viewAll.xpath")+"[" + i + "]/a")).isDisplayed()) {
					numberOfPixelsToDragTheScrollbarDown+=135;
					count=0;
					while(!driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.viewAll.xpath")+"[" + i + "]/a")).isDisplayed() && ++count<4) {
						draggablePartOfScrollbar = driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.managersLandingScrollBar.xpath")));
						dragger.moveToElement(draggablePartOfScrollbar).clickAndHold().moveByOffset(0,numberOfPixelsToDragTheScrollbarDown).release().perform();
						Thread.sleep(2000L);
					}

				}

				driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.viewAll.xpath")+"[" + i + "]/a")).click();

				if(verifyManagerAndFund().equals("Fail")){
					return "Fail";
				}

				driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.closeIcon.xpath"))).click();

			}
			return "Pass";

		} catch (Throwable t) {
			log.debug("An error has occurred while verifying the Manager/Fund for all the items of View All.");
			return "Fail";
		}
	}

	public String verifyVideoPdf() {
		log.debug("=============================");
		log.debug("Executing verifyVideoPdf");
		try { 
			WebElement elementIcon= driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			//if (elementIcon!= null) {
			String elementIconVal = elementIcon.getText();
			elementIcon.click();
			Thread.sleep(WAIT2SEC);
			WebElement viewOverlay = driver.findElement(By.xpath(OR.getProperty(objectArr[1])));
			String viewOverlayVal = viewOverlay.getText();
			driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).click();
			Thread.sleep(WAIT5SEC);
			WebElement viewOverlayOpen = driver.findElement(By.xpath(OR.getProperty(objectArr[3])));
			String viewOverlayOpenVal = viewOverlayOpen.getText();
			if (Functions.compareActualExpected(driver, log, elementIconVal,viewOverlayVal, CONFIG)) {
				if (Functions.compareActualExpected(driver, log, viewOverlayVal,viewOverlayOpenVal, CONFIG)) {
					log.debug("Element icon on the playlist page is :" +elementIconVal);
					log.debug("Element overlay value when icon clicked is :" +viewOverlayVal);
					log.debug("Element heading when view buttin clicked is :" +viewOverlayOpenVal);
					driver.findElement(By.xpath(OR.getProperty(objectArr[4]))).click();
					return "Pass";

				}else

					log.debug("Element icon on the playlist page is :" +elementIconVal);
				log.debug("Element overlay value when icon is clicked" +viewOverlayVal);
				log.debug("Element heading when view buttin is clicked :" +viewOverlayOpenVal);
				return "Fail";
			} else
				return "Fail";
		} 
		catch (NoSuchElementException e) {
			log.debug("video or pdf not present");
			return "Pass";
		}
		catch (Throwable t) {
			log.debug("Error while exceuting verifyVideoPdf -" + objectArr[0]+ t.getMessage());
			log.debug("Error while exceuting verifyVideoPdf -" + objectArr[1]+ t.getMessage());
			log.debug("Error while exceuting verifyVideoPdf -" + objectArr[2]+ t.getMessage());
			return "Fail - Link Not Found";
		}
	}

	public String dragAndDrop_MyLibrary() {
		log.debug("=============================");
		log.debug("Executing function dragAndDrop_MyLibrary");
		try { 
			WebElement dropElement = driver.findElement(By.xpath(OR.getProperty(objectArr[1])));

			List<WebElement> completeItemList = driver.findElements(By.xpath("//tbody[@class='item_list filterSearch']/tr"));

			log.debug("list size : " + completeItemList.size());

			Iterator<WebElement> i3 = completeItemList.iterator();
			int i = 0;
			int count = 0;
			int flagPlaylist = 0;
			int flagVideo = 0;
			int flagWord = 0;
			int flagPdf = 0;
			int flagFund = 0;
			int flagNews = 0;
			int flagManager = 0;
			int flagPowerpoint = 0;
			int flagExcel = 0;

			while (i3.hasNext() && i<completeItemList.size()-1) {

				WebElement dragElement = driver.findElement(By.xpath(OR.getProperty(objectArr[0])+"[" + ++i + "]"));

				log.debug("Value of i: "+ i);
				log.debug("Total items added:"+count);   

				if(dragElement.getAttribute("class").contains("playlist")) {
					flagPlaylist++;
					if(flagPlaylist>1) {
						continue;
					}

					Functions.dragAndDropElement(driver, log, dragElement, dropElement);

					count++;
					continue;
				}

				if(dragElement.getAttribute("class").contains("video")) {
					flagVideo++;
					if(flagVideo>1) {
						continue;
					}

					Functions.dragAndDropElement(driver, log, dragElement, dropElement);

					count++;
					continue;
				}
				if(dragElement.getAttribute("class").contains("word")) {
					flagWord++;
					if(flagWord>1) {
						continue;
					}

					Functions.dragAndDropElement(driver, log, dragElement, dropElement);

					count++;
					continue;
				}
				if(dragElement.getAttribute("class").contains("excel")) {
					flagExcel++;
					if(flagExcel>1) {
						continue;
					}

					Functions.dragAndDropElement(driver, log, dragElement, dropElement);

					count++;
					continue;
				}
				if(dragElement.getAttribute("class").contains("powerpoint")) {
					flagPowerpoint++;
					if(flagPowerpoint>1) {
						continue;
					}

					Functions.dragAndDropElement(driver, log, dragElement, dropElement);

					count++;
					continue;
				}
				if(dragElement.getAttribute("class").contains("manager")) {
					flagManager++;
					if(flagManager>1) {
						continue;
					}

					Functions.dragAndDropElement(driver, log, dragElement, dropElement);

					count++;
					continue;
				}
				if(dragElement.getAttribute("class").contains("news")) {
					flagNews++;
					if(flagNews>1) {
						continue;
					}

					Functions.dragAndDropElement(driver, log, dragElement, dropElement);

					count++;
					continue;
				}


				if(dragElement.getAttribute("class").contains("pdf")) {
					flagPdf++;
					if(flagPdf>1) {
						continue;
					}

					Functions.dragAndDropElement(driver, log, dragElement, dropElement);

					count++;
					continue;
				}

				if((dragElement.getAttribute("class").endsWith("fund ui-draggable")) || (dragElement.getAttribute("class").endsWith("fund alt ui-draggable"))) {
					flagFund++;
					if(flagFund>1) {
						continue;
					}

					Functions.dragAndDropElement(driver, log, dragElement, dropElement);

					count++;
					continue;
				}

			}

		}catch (Throwable t) {
			log.debug("An error has occurred while exceuting function dragAndDrop_MyLibrary.");
			return "Fail";
		}
		return "Pass";

	}

	public String verifyElementNotPresent(){

		//this keyword looks for elements on the page and passes if all are not present
		log.debug("=============================");
		log.debug("Executing verifyElementNotPresent");
		int i,k;
		k = objectArr.length;
		boolean flag = true;

		for(i = 0; i<k; i++) {
			try {
				WebElement elementIcon= driver.findElement(By.xpath(objectArr[i]));
				if(elementIcon.getAttribute("class").contains("invisible") || !(elementIcon.isDisplayed())) {
					log.debug("element number"+ (i+1) + "not found: " + objectArr[i] + " .This is expected");
				}
				else {
					//fail scenario, make flag as false
					flag = false;
					log.debug("element number"+ (i+1) + "found: " + objectArr[i]);
				}
			}catch(NoSuchElementException e) {
				log.debug("element number"+ (i+1) + "not found: " + objectArr[i]+ " .This is expected");
			}
			catch(Throwable e){
				//do nothing
				log.debug("element number"+ (i+1) + "not found: " + objectArr[i]+ " .This is not expected");
				flag = false;
			}		
		}
		//loop completes
		if(flag)
			return "Pass";
		else
			return "Fail";

	}

	public String verifyTextAddtolib_Managers() {
		log.debug("=============================");
		log.debug("Executing verifyTextAddtolib_Managers Keyword");
		boolean flag = false;
		int countVar = 0;
		int numberOfPixelsToDragTheScrollbarDown=135;
		WebElement draggablePartOfScrollbar;
		Actions dragger = new Actions(driver);
		int count;
		try {
			List<WebElement> playlist = driver.findElements(By.xpath(OR.getProperty(objectArr[0])));
			for(WebElement p : playlist) {
				if(countVar<playlist.size()) {
					WebElement playlistItem = driver.findElement(By.xpath("//div[@class='viewport grid-wrap']/div/div["+ ++countVar + "]/a"));
					if(!(playlistItem.isDisplayed())) {							
						count=0;
						while(!(playlistItem.isDisplayed()) && ++count<4) {
							draggablePartOfScrollbar = driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.managersLandingScrollBar.xpath")));
							dragger.moveToElement(draggablePartOfScrollbar).clickAndHold().moveByOffset(0,numberOfPixelsToDragTheScrollbarDown).release().perform();
							Thread.sleep(2000L);
						}
					}
					playlistItem.click();
					Thread.sleep(WAIT5SEC);
					WebElement en = driver.findElement(By.xpath(OR.getProperty(objectArr[1])));
					String entity = en.getText();
					WebElement close = driver.findElement(By.xpath("//div[@class='no-l-nav']/a/span[@class='icon']"));
					close.click();
					Thread.sleep(WAIT5SEC);
					if((entity.contains("Remove from Library") || entity.contains("Add to Library"))){
						flag = true;
						break;
					}
				}
			}


			if(flag==false)	
				return "Pass";
			else
				return "Fail";

		}catch (Throwable t) {
			// report error
			log.debug("Error while executing verifyTextAddtolib_Managers -" +  t.getMessage());
			return "Fail";
		}
	}


	public String verifyPageTitles_UsingExcel() throws InterruptedException, IOException {
		log.debug("=============================");
		log.debug("Executing verifyPageTitles Keyword");
		log.debug("Config file loaded: " + runTestApp);
		String actualTitle;
		int excelSize;
		boolean flag = true;

		String[] title = driver.getCurrentUrl().split("/");
		String currentEnv  = title[2];
		String url,url1;

		String gsamLibFilter[] = {"managerfundlanding", "externalmanager"};
		boolean flagGSam;
		String expectedTitle;

		try {
		excelSize = ExcelOperations.getRowCount(System.getProperty("user.dir")+"/src/com/aims/xls", "pageTitles_MasterList.xlsx", "Sheet1");
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		String dateToday =  sdf.format(cal.getTime());

		if(runTestApp.equals("GSAMLibrary"))
			ExcelOperations.setCellComments(System.getProperty("user.dir")+"/src/com/aims/xls", "pageTitles_MasterList.xlsx", "Sheet1", "MatchStatus_GSamLib", 1, dateToday);
		else
			ExcelOperations.setCellComments(System.getProperty("user.dir")+"/src/com/aims/xls", "pageTitles_MasterList.xlsx", "Sheet1", "MatchStatus_Aims", 1, dateToday);

		for (int i= 1;i<excelSize;i++) {
			flagGSam = false;
			url = ExcelOperations.getCellData(System.getProperty("user.dir")+"/src/com/aims/xls", "pageTitles_MasterList.xlsx", "Sheet1", "Url", i+1);
			url1= title[0]+"//" + currentEnv + url;

			if(runTestApp.equals("GSAMLibrary")) {
				for(String var : gsamLibFilter ) {
					if(!url.contains(var)) {
						continue;
					}else {
						flagGSam = true;
						break;
					}
				}
			}

			if(!flagGSam) {
				String environment = "http://" + title[2];
				ExcelOperations.setCellData(System.getProperty("user.dir")+"/src/com/aims/xls", "pageTitles_MasterList.xlsx", "Sheet1", "Environment", i+1, environment);

				if(!url.isEmpty()) {

					driver.get(url1);
					Thread.sleep(WAIT5SEC);
					actualTitle = driver.getTitle();

					if(runTestApp.equals("GSAMLibrary")) {
						expectedTitle = ExcelOperations.getCellData(System.getProperty("user.dir")+"/src/com/aims/xls", "pageTitles_MasterList.xlsx", "Sheet1", "ExpectedTitle_GSamLib", i+1);
						ExcelOperations.setCellData(System.getProperty("user.dir")+"/src/com/aims/xls", "pageTitles_MasterList.xlsx", "Sheet1", "ActualTitle_GSamLib", i+1, actualTitle);
						actualTitle = ExcelOperations.getCellData(System.getProperty("user.dir")+"/src/com/aims/xls", "pageTitles_MasterList.xlsx", "Sheet1", "ActualTitle_GSamLib", i+1);
						ExcelOperations.setCellData(System.getProperty("user.dir")+"/src/com/aims/xls", "pageTitles_MasterList.xlsx", "Sheet1", "MatchStatus_GSamLib", i+1, String.valueOf(actualTitle.equals(expectedTitle)));
					}
					else {
						expectedTitle = ExcelOperations.getCellData(System.getProperty("user.dir")+"/src/com/aims/xls", "pageTitles_MasterList.xlsx", "Sheet1", "ExpectedTitle_Aims", i+1);
						ExcelOperations.setCellData(System.getProperty("user.dir")+"/src/com/aims/xls", "pageTitles_MasterList.xlsx", "Sheet1", "ActualTitle_Aims", i+1, actualTitle);
						actualTitle = ExcelOperations.getCellData(System.getProperty("user.dir")+"/src/com/aims/xls", "pageTitles_MasterList.xlsx", "Sheet1", "ActualTitle_Aims", i+1);
						ExcelOperations.setCellData(System.getProperty("user.dir")+"/src/com/aims/xls", "pageTitles_MasterList.xlsx", "Sheet1", "MatchStatus_Aims", i+1, String.valueOf(actualTitle.equals(expectedTitle)));
					}

					if(testBrowser.contains("InternetExplorer"))
					{
						if(actualTitle.contains(expectedTitle))
							log.debug("title matched. Actual title: " + actualTitle + " expected title: " + expectedTitle);
						else
						{
							log.debug("title didnt match. Actual title: " + actualTitle + " expected title: " + expectedTitle);
							flag = false;
						}
					}
					else
					{
					if(actualTitle.equalsIgnoreCase(expectedTitle)) {
						log.debug("title matched. Actual title: " + actualTitle + " expected title: " + expectedTitle);
					}
					else {
						log.debug("title didnt match. Actual title: " + actualTitle + " expected title: " + expectedTitle);
						flag = false;
					}
					}
				}
			}

		}		

		if(flag)	
			return "Pass";
		else
			return "Fail";
		}catch(Throwable e)
		{
			log.debug("Error while executing verifyPageTitles_UsingExcel -"+e.getMessage());
			return "Fail";
		}

	}


	public String allPlaylistsSortByTitle(){
		log.debug("=============================");
		log.debug("Executing allPlaylistsSortByTitle Keyword");
		List<String> titles=new ArrayList<String>();
		List<String> titlesSorted=new ArrayList<String>();
		boolean result=true;

		try {
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.playListSort.xpath"))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
			Thread.sleep(WAIT2SEC);
			List<WebElement> names = driver.findElements(By.xpath("//div[@class='listing']/descendant::span[@class='title']"));
			for(WebElement e : names) {
				if(e.getAttribute("class").equalsIgnoreCase("title")){
					System.out.println("title : "+e.getText());
					titles.add(e.getText());
				}
			}

			titlesSorted.addAll(titles);
			System.out.println(titles.size());
			System.out.println(titlesSorted.size());
			Collections.sort(titlesSorted);


			for(int k=0; k<titles.size(); k++){

				System.out.println("name :" + titles.get(k)+ "    titles sorted  "+ titlesSorted.get(k));
				String s = titles.get(k).toString().trim();
				String s2 = titlesSorted.get(k).toString().trim();

				if(!(s.equalsIgnoreCase(s2)))
				{
					result=false;
					break;
				}
				else
					result=true;
			}

			if(result==true)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing allPlaylistsSortByTitle -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}

	}

	public String pageCount_filter(){

		log.debug("=============================");
		log.debug("Executing pageCount_filter Keyword");
		//this keyword verifies the count of the results by clicking on the cross button
		int pageCountBefore,pageCountAfter;
		String firstElement;
		
		try {
			Thread.sleep(WAIT2SEC);
			pageCountBefore = Integer.parseInt(driver.findElement(By.xpath("//div[@id='disp-status']/descendant::span[@class='total ng-binding']")).getText());

			if(pageCountBefore==0) {
				log.debug("Zero results on page.");
				return "Fail-Data not present";
			}

			firstElement = driver.findElement(By.xpath("//div[@class='overview']/descendant::tr[1]/descendant::a")).getText();

			if(!firstElement.isEmpty()) {
				driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.subSearchInputBox.xpath"))).sendKeys(firstElement.substring(0, 4));
				Thread.sleep(WAIT1SEC);
				driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.goButton.xpath"))).click();
				Thread.sleep(WAIT2SEC);
				driver.findElement(By.xpath("//span[@class='clearText']")).click();
				Thread.sleep(WAIT2SEC);
				pageCountAfter = Integer.parseInt(driver.findElement(By.xpath("//div[@id='disp-status']/descendant::span[@class='total ng-binding']")).getText());
				if(pageCountBefore == pageCountAfter) {
					log.debug("Values for page Count Before : " + pageCountBefore + "page Count After :" + pageCountAfter);
					return "Pass";
				}
				else {
					log.debug("Values for page Count Before : " + pageCountBefore + "page Count After :" + pageCountAfter);
					return "Fail";
				}
			}else {
				log.debug("No data present to verify.");
				return "Fail-No Data Present";
			}
		}catch(Throwable t) {
			log.debug("Error while executing pageCount_filter -" + objectArr[0]+t.getMessage());
			return "Fail";
		}

	}

	public String clickLinkDiv() {

		log.debug("=============================");
		log.debug("Executing clickLinkDiv Keyword");
		//this keyword clicks on the links present inside a div
		int elementCount;
		try {
			Thread.sleep(WAIT1SEC);
			List<WebElement> names = getWebElements(OR, objectArr[0]);
			elementCount = names.size();
			String dynamicXpath = null;

			//		for(int i=1;i<names.size();i++) {
			//			
			//		}
			for (WebElement webElement : names) {
				webElement.click();
			}
			/*int i = 1;
		while(elementCount!=0) {
			System.out.println("");
			dynamicXpath = OR.getProperty(objectArr[0]) + "[" + i++ + "]";
			driver.findElement(By.xpath(dynamicXpath)).click();
			Thread.sleep(500);
			elementCount--;
		}*/
		}catch(Throwable t) {
			log.debug("Error while executing clickLinkDiv -" + objectArr[0]+t);
			return "Fail";
		}
		//	if(elementCount == 0)

		return "Pass";
		//else
		//return "Fail";
	}


	public String clickOnTableByColumnIndex(){
		//TestData will be like columnindex;coulmnvalue;click index
		log.debug("=========================================");
		log.debug("Executing clickOnTableByColumnIndex Keyword");
		List<WebElement> names = getWebElements(OR, objectArr[0]);
		try {
			String[] values =  testData.getCellData(currentTest, data_column_nameArr[0],testRepeat).split(";");
			for (WebElement webElement : names) {
				if(webElement.findElement(By.xpath(".//td["+Integer.parseInt(values[0])+"]")).getText().toLowerCase().contains(values[1].toLowerCase())){
					webElement.findElement(By.xpath(".//td["+Integer.parseInt(values[2])+"]")).click();
					break;
				}
			}
			return "Pass";
		}catch (Throwable t) {
			log.debug("An error has occurred while executing clickOnTableByColumnHeader "+t.getMessage());
			return "Fail";
		}
	}


	public String verifySaveSearchName() {
		log.debug("=========================================");
		log.debug("Executing verifySaveSearchName Keyword");
		try {		

			String val1 = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			String val = val1.substring(0, val1.length()-1);

			log.debug("Save Search Name (31 characters):"+ val1);
			log.debug("Save Search Name (30 characters):"+ val);

			List<WebElement> savedSearchesElement = driver.findElements(By.xpath(OR.getProperty("aims.managersFunds.savedSearchesNames.xpath")));
			ArrayList<String> savedSearches = new ArrayList<String>();

			log.debug("====================Saved Searches===============");
			for (WebElement webElement : savedSearchesElement) {
				log.debug(webElement.getAttribute("innerHTML"));
				savedSearches.add(webElement.getAttribute("innerHTML"));
			}

			if(!savedSearches.contains(val1) && savedSearches.contains(val)) {
				return "Pass";
			}else {
				return "Fail";
			}

		}catch (Throwable t) {
			log.debug("An error has occurred while executing verifySaveSearchName "+t.getMessage());
			return "Fail";
		}
	}


	public String MyLibraryVerifyItemCountListView() {
		log.debug("==================================================");
		log.debug("Executing MyLibraryVerifyItemCountListView Keyword");
		//this method counts the number of items listed below the selected carousel in the table for List view.
		//verifies if the count shown in the brackets on the top of the table matches with no. of items shown

		int count=0;	
		try {
			String expectedString = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			int expectedValue = Functions.getNumberWithinBrackets(expectedString);
			System.out.println("expected Value :     " + expectedValue);
			log.debug("expected Value :     " + expectedValue);

			String rowsLocator = "//tbody[@class='item_list filterSearch']/tr";
			String dragThumbLocator = "//div[@id='listView']/descendant::div[@class='thumb']";
			String totalRowCount = driver.findElement(By.xpath("//*[@id='library-items']/descendant::h1/span[2]")).getText();

			int splitTotalRows = Functions.getNumberWithinBrackets(totalRowCount);
			log.debug("total count shown " + totalRowCount);
			System.out.println("total count shown " + totalRowCount);

			Functions.dragTillAllRowsLoaded(driver, log, rowsLocator, dragThumbLocator, splitTotalRows, 100);

			List<WebElement> fundsList3 = driver.findElements(By.tagName("span"));
			System.out.println("list size : " + fundsList3.size());
			log.debug("list size : " + fundsList3.size());
			for (WebElement e4 : fundsList3) {
				if (e4.getAttribute("class").equals("ng-binding")) {
					String value = e4.getAttribute("innerHTML");
					System.out.println("value is :  " + value);
					log.debug("value is :  " + value);

					count++;

				}
			}
			System.out.println("count is  " + count);
			if(expectedValue==count) 
				return "Pass";
			else {
				log.debug("count of items do not match");
				return "Fail";}

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyLibraryVerifyItemCountListView -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}


	public String LibraryManagePlaylistVerifyItemCountListView() {
		log.debug("==============================================================");
		log.debug("Executing LibraryManagePlaylistVerifyItemCountListView Keyword");
		//this method counts the number of items under user playlist accessed from manage playlist lense for List view.
		//verifies if the count shown in the brackets on the top of the table matches with no. of items shown

		int count=0;
		try {

			driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).click();
			Thread.sleep(WAIT2SEC);
			System.out.println("before list count");

			List<WebElement> fundsList3 = driver.findElements(By.xpath("//span[@class='title_data ng-binding']"));
			System.out.println("list size : " + fundsList3.size());
			log.debug("list size : " + fundsList3.size());
			count = fundsList3.size();

			System.out.println("count is  " + count);
			driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).click();
			Thread.sleep(WAIT5SEC);
			driver.findElement(By.xpath(OR.getProperty(objectArr[3]))).click();
			Thread.sleep(WAIT8SEC);

			//go to list view first
			driver.findElement(By.xpath(OR.getProperty("aims.library.myLibraryListViewButton.xpath"))).click();
			Thread.sleep(WAIT5SEC);

			String actualCountString =driver.findElement(By.xpath(OR.getProperty(objectArr[4]))).getText();
			int actualCount = Functions.getNumberWithinBrackets(actualCountString);
			System.out.println("expectedCount :" + count  + "actualCount  : "+ actualCount);
			log.debug("expectedCount :" + count  + "actualCount  : "+ actualCount);

			if(count==actualCount)
				return "Pass";
			else {
				log.debug("count of items do not match");
				return "Fail";}

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing LibraryManagePlaylistVerifyItemCountListView -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}


	public String LibraryManagePlaylistVerifyItemCountGridView() {
		log.debug("==============================================================");
		log.debug("Executing LibraryManagePlaylistVerifyItemCountGridView Keyword");
		//this method counts the number of items under user playlist accessed from manage playlist lense for List view.
		//verifies if the count shown in the brackets on the top of the table matches with no. of items shown

		int count=0;	
		try {
			driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).click();
			Thread.sleep(WAIT2SEC);

			List<WebElement> fundsList3 = driver.findElements(By.xpath("//span[@class='title_data ng-binding']"));
			System.out.println("list size : " + fundsList3.size());
			log.debug("list size : " + fundsList3.size());
			count = fundsList3.size();
			System.out.println("count is  " + count);

			driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).click();
			Thread.sleep(WAIT5SEC);
			driver.findElement(By.xpath(OR.getProperty(objectArr[3]))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty(objectArr[4]))).click();
			Thread.sleep(WAIT2SEC);

			String actualCountString =driver.findElement(By.xpath(OR.getProperty(objectArr[5]))).getText();
			int actualCount = Functions.getNumberWithinBrackets(actualCountString);
			System.out.println("expectedCount :" + count  + "actualCount  : "+ actualCount);
			log.debug("expectedCount :" + count  + "actualCount  : "+ actualCount);

			if(count==actualCount)
				return "Pass";
			else {
				log.debug("count of items do not match");
				return "Fail";}

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing LibraryManagePlaylistVerifyItemCountGridView -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}



	public String MyLibraryVerifyItemCountGridView() {
		log.debug("==================================================");
		log.debug("Executing MyLibraryVerifyItemCountGridView Keyword");
		//this method counts the number of items listed below the selected carousel in the table for grid view.
		//verifies if the count shown in the brackets on the top of the table matches with no. of items shown
		try {
			String expectedString = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			int expectedValue = Functions.getNumberWithinBrackets(expectedString);
			System.out.println("expectedValue    " + expectedValue);
			log.debug("expectedValue    " + expectedValue);

			String rowsLocator = "//ul[@class='item_grid item_list clearfix']/li";
			String dragThumbLocator = "//div[@id='grid-scroll']/descendant::div[@class='thumb']";
			String totalRowCount = driver.findElement(By.xpath("//*[@id='library-items']/descendant::h1/span[2]")).getText();

			int splitTotalRows = Functions.getNumberWithinBrackets(totalRowCount);
			log.debug("total count shown " + totalRowCount);
			System.out.println("total count shown " + totalRowCount);

			Functions.dragTillAllRowsLoaded(driver, log, rowsLocator, dragThumbLocator, splitTotalRows, 100);

			List<WebElement> actualList = driver.findElements(By.xpath("//div[@class='search_results']/ul[@class]/li"));
			int actualCount = actualList.size();
			System.out.println("actual count : " + actualCount);

			if (expectedValue == actualCount)
				return "Pass";
			else {
				log.debug("count of items do not match");
				return "Fail";
			}

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing MyLibraryVerifyItemCountGridView -"
					+ objectArr[0] + t.getMessage());
			return "Fail";
		}
	}



	public String compareTextColorOnMouseHover() {
		log.debug("==============================================");
		log.debug("Executing compareTextColorOnMouseHover Keyword");
		//this method hovers the mouse on the object specified and gets its actual text color and compares it with the expected color
		//objectArr[0] is the object ; objectArr[1] is the expected color before hover; objectArr[2] is the expected color after hover
		int resultCount =0;

		try {

			String expectedColorBeforeHover=testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			String expectedColorAfterHover=testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);

			WebElement object2 = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));

			String actualColorBeforeHoverRGB = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getCssValue("Color");
			String actualColorBeforeHoverHex = Color.fromString(actualColorBeforeHoverRGB).asHex().toUpperCase();
			log.debug(objectArr[0] + " :  expected Color BEFORE Hover : " + expectedColorBeforeHover);
			log.debug(objectArr[0] + " :  actual Color BEFORE Hover : " +actualColorBeforeHoverHex);
			System.out.println(objectArr[0] + " :  expected Color BEFORE Hover : " + expectedColorBeforeHover);
			System.out.println(objectArr[0] + " :  actual Color BEFORE Hover : " +actualColorBeforeHoverHex);	 
			if(actualColorBeforeHoverHex.trim().equals(expectedColorBeforeHover.trim()))
				resultCount++;
			Thread.sleep(WAIT2SEC);

			Actions builder=new Actions(driver);
			builder.moveToElement(object2).build().perform();

			String actualColorAfterHoverRGB = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getCssValue("Color");
			String actualColorAfterHoverHex = Color.fromString(actualColorAfterHoverRGB).asHex().toUpperCase();
			log.debug(objectArr[0] + " :  expected Color AFTER Hover : " + expectedColorAfterHover);
			log.debug(objectArr[0] +" :  actual Color AFTER Hover : " +actualColorAfterHoverHex);
			System.out.println(objectArr[0] + " :  expected Color AFTER Hover : " + expectedColorAfterHover);
			System.out.println(objectArr[0] +" :  actual Color AFTER Hover : " +actualColorAfterHoverHex);

			if(actualColorAfterHoverHex.trim().equalsIgnoreCase(expectedColorAfterHover.trim()))
				resultCount++;

			if(resultCount==2){
				System.out.println(resultCount);
				return "Pass";

			}

			else{
				System.out.println(resultCount);
				return "Fail";
			}
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing compareTextColorOnMouseHover -"+ objectArr[0] + t.getMessage());
			return "Fail";
		}

	}



	public String verifyElementOrdering() {
		log.debug("=======================================");
		log.debug("Executing verifyElementOrdering Keyword");
		//this keyword checks whether the order of the links present
		//objectArr[0] is the object whose links order is to be verified

		try {
			String expectedOrder = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			String order = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
			List<WebElement> search = driver.findElements(By.xpath(OR.getProperty(objectArr[0])));
			String actualOrder= "";
			String seperator = "";
			for(WebElement e2 : search) {
					if(!e2.getText().isEmpty()){
						log.debug("category link : " + e2.getText());
						if(order.equalsIgnoreCase("Reverse"))
							actualOrder = e2.getText() + seperator + actualOrder;
						else if(order.equalsIgnoreCase("Forward"))
							actualOrder = actualOrder + seperator + e2.getText();
					}
					seperator = ",";
			}

			if(actualOrder.trim().equals(expectedOrder)) {
				log.debug("actual order is : " + actualOrder + "and expected order is : " + expectedOrder);
				return "Pass";
			}
			else {
				log.debug("actual order is : " + actualOrder + "and expected order is : " + expectedOrder);
				return "Fail";
			}
		}catch(Throwable t) {
			log.debug("Error while executing verifyElementOrdering -"+ objectArr[0] + t.getMessage());
			return "Fail";
		}
	}

	public String verifyFont() {
		log.debug("=============================");
		log.debug("Executing verifyFont Keyword");
		String flag = "Pass", font_expected1, font_expected2, font_expected3, font_actual, elementKey, elementLocator;
		elementKey=objectArr[0]; elementLocator=OR.getProperty(objectArr[0]); 

		try {

			WebElement element = driver.findElement(By.xpath(elementLocator));
			Functions.highlighter(driver, element);
			font_expected1 = "\'"+testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);

			font_actual = element.getCssValue("font-family").substring(0, element.getCssValue("font-family").indexOf(","))+","+element.getCssValue("font-size")+","+element.getCssValue("color");

			if(!font_expected1.equals(font_actual)) {
				log.debug("\nFont Values for the element do not match.\nActual   Font: \t"+font_actual+"\nExpected Font: \t"+font_expected1);
				log.debug("\nElement     key: \t"+elementKey+"\nElement locator: \t"+elementLocator+"\n");
				flag="Fail";
			}

			try{
				font_expected2 = "\'"+testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);

				action.moveToElement(element).build().perform();

				font_actual = element.getCssValue("font-family").substring(0, element.getCssValue("font-family").indexOf(","))+","+element.getCssValue("font-size")+","+element.getCssValue("color");

				if(!font_expected2.equals(font_actual)) {
					log.debug("\nOn hovering, Font Values for the element do not match.\nActual   Font: \t"+font_actual+"\nExpected Font: \t"+font_expected2);
					log.debug("\nElement     key: \t"+elementKey+"\nElement locator: \t"+elementLocator+"\n");
					flag="Fail";
				}

			}catch(ArrayIndexOutOfBoundsException e) {
			}

			try{
				font_expected3 = "\'"+testData.getCellData(currentTest, data_column_nameArr[2],testRepeat);
				element.click();

				//Handling Data Unavailable Pop Up for Specific Dev and QA Environment pages
				Functions.handleDataUnavailablePopUp(driver, log, CONFIG);

				Thread.sleep(WAIT3SEC);

				element = driver.findElement(By.xpath(elementLocator));	
				font_actual = element.getCssValue("font-family").substring(0, element.getCssValue("font-family").indexOf(","))+","+element.getCssValue("font-size")+","+element.getCssValue("color");

				if(!font_expected3.equals(font_actual)) {
					log.debug("\nOn Clicking, Font Values for the element do not match.\nActual   Font: \t"+font_actual+"\nExpected Font: \t"+font_expected3);
					log.debug("\nElement     key: \t"+elementKey+"\nElement locator: \t"+elementLocator+"\n");
					flag="Fail";
				}

			}catch(ArrayIndexOutOfBoundsException e) {
			}

		}catch(Throwable t) {
			log.debug("Error while executing verifyFont on: \nElement     Key= "+ elementKey+"\nElement Locator"+ elementLocator + "\n" + t.getMessage());
			return "Fail";
		}
		return flag;

	}

	public String verifyViewportRefresh() {
		log.debug("=======================================");
		log.debug("Executing verifyViewportRefresh Keyword");
		//this keyword checks whether a particular part of the page got loaded
		int passCount=0;

		try {
			WebElement testElement1 = getWebElement(OR, objectArr[0]);
			WebElement testElement2 = getWebElement(OR, objectArr[1]);
			JavascriptExecutor js = (JavascriptExecutor) driver;

			String testElement1_Class1 = null,testElement2_Class1 = null;
			String testElement1_Class2 = null,testElement2_Class2 = null;
			String testElement1_Class3 = null,testElement2_Class3 = null;

			testElement1_Class1 = testElement1.getAttribute("class");
			js.executeScript("arguments[0].setAttribute('class', 'Title')",testElement1);

			testElement1_Class2 = testElement1.getAttribute("class");

			testElement2_Class1 = testElement2.getAttribute("class");
			String changedClass = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			js.executeScript("arguments[0].setAttribute('class', '" + changedClass + "')",testElement2);
			testElement2_Class2 = testElement2.getAttribute("class");

			getWebElement(OR, objectArr[2]).click(); //click to see what portion of page got loaded
			Thread.sleep(WAIT5SEC);

			if(driver.getCurrentUrl().contains("externalmanager")){
				try {
					driver.navigate().back(); 
				} catch (Throwable t) {
					// report error
					log.debug("Error while clicking browserBack button -" + t.getMessage());           
					return "Fail - Browser back failed";
				}
				Thread.sleep(WAIT3SEC);
			}
			testElement1 = getWebElement(OR, objectArr[0]);
			testElement2 = getWebElement(OR, objectArr[1]);

			testElement1_Class3 = testElement1.getAttribute("class");
			log.debug("class of first element after refresh: " + testElement1_Class3);
			testElement2_Class3 = testElement2.getAttribute("class");
			log.debug("class of second element after refresh: " + testElement2_Class3);

			log.debug("test element1 original class :" + testElement1_Class1);
			log.debug("test element1 changed class  :" + testElement1_Class2);
			log.debug("test element2 original class :" + testElement2_Class1);
			log.debug("test element2 changed class  :" + testElement2_Class2);
			log.debug("test element1 class after click :" + testElement1_Class3);
			log.debug("test element2 class after click :" + testElement2_Class3);
			Thread.sleep(WAIT3SEC);
			if(!testElement1_Class1.equals(testElement1_Class3) && testElement2_Class1.equals(testElement2_Class3) && testElement2_Class2.equals(changedClass)) {
				passCount++;
			}
			else if(driver.getCurrentUrl().endsWith("home.html") || driver.getCurrentUrl().endsWith("myworkspace.html") || driver.getCurrentUrl().endsWith("managerfundlanding.html?section=overview") || driver.getCurrentUrl().endsWith("aimsinsightslanding.html#/section/insight") ) {
				if(testElement1_Class1.equals(testElement1_Class2) && testElement2_Class1.equals(testElement2_Class2))
					passCount++;
				else 
					return "Fail";

			} 

			if(passCount==1)
				return "Pass";
			else
				return "Fail";

		}catch(Throwable t) {
			log.debug("Error while executing verifyViewportRefresh -"+ t.getMessage());
			return "Fail";
		}

	}

	public String workspaceLandingPageClickLense() {
		log.debug("=======================================");
		log.debug("Executing workspaceLandingPageClickLense Keyword");
		//this keyword checks whether a particular part of the page got loaded

		try {
			int lenseNumber = Integer.parseInt(OR.getProperty(objectArr[0]));
			int elementCount = 0;
			boolean clicked=false;
			System.out.println("lense number    " + lenseNumber);
			log.debug("lense number     " + lenseNumber);

			List<WebElement> ulList = driver.findElements(By.xpath("//div[@id='lenses']/descendant::li[@class='ng-scope']"));
			Iterator<WebElement> i2 = ulList.iterator();

			while(i2.hasNext()){
				WebElement e2 = i2.next();

				++elementCount;
				log.debug("element count : " +elementCount);
				if(elementCount==lenseNumber){
					e2.click();
					clicked=true;
					break ;
				}


			}

			if(clicked)
				return "Pass";
			else
				return "Fail-LenseNotPresent";

		}catch(Throwable t) {
			log.debug("Error while executing workspaceLandingPageClickLense -"+ objectArr[0] + t.getMessage());
			return "Fail";
		}
	}

	public String portfolioPageClickLense() {
		log.debug("=======================================");
		log.debug("Executing portfolioPageClickLense Keyword");
		//this keyword checks whether a particular part of the page got loaded

		try {
			int lenseNumber = Integer.parseInt(OR.getProperty(objectArr[0]));
			int elementCount = 0;
			boolean clicked=false;
			System.out.println("lense number    " + lenseNumber);
			log.debug("lense number     " + lenseNumber);

			List<WebElement> ulList = driver.findElements(By.xpath("//div[@id='lenses']/descendant::li[@class='ng-scope']"));
			Iterator<WebElement> i2 = ulList.iterator();
			log.debug(ulList.size());

			while(i2.hasNext()){
				WebElement e2 = i2.next();

				++elementCount;
				log.debug("element count : " +elementCount);
				if(elementCount==lenseNumber){
					e2.click();
					clicked=true;
					break ;
				}


			}

			if(clicked)
				return "Pass";
			else
				return "Fail-LenseNotShown";

		}catch(Throwable t) {
			log.debug("Error while executing portfolioPageClickLense -"+ objectArr[0] + t.getMessage());
			return "Fail";
		}
	}

	public String libraryPageClickLense() {
		log.debug("=======================================");
		log.debug("Executing libraryPageClickLense Keyword");
		//this keyword checks whether a particular part of the page got loaded
		int lenseNumber, elementCount = 0;
		try {
			lenseNumber = Integer.parseInt(OR.getProperty(objectArr[0]));
			System.out.println("lense number    " + lenseNumber);
			log.debug("lense number     " + lenseNumber);

			List<WebElement> ulList = driver.findElements(By.xpath("//div[@class='lenses parbase']/descendant::li[contains(@class,'list-trigger')]"));
			for (WebElement e2 : ulList) {
				if(e2.getAttribute("class").contains("list-trigger")) {
					++elementCount;
					log.debug("element count : " +elementCount);
					if(elementCount==lenseNumber){
						e2.click();
						break ;
					}
				}
			}
			if(elementCount>0)
				return "Pass";
			else
				return "Fail";

		}catch(Throwable t) {
			log.debug("Error while executing libraryPageClickLense -"+ objectArr[0] + t.getMessage());
			return "Fail";
		}
	}

	public String libraryPageCloseLense() {
		log.debug("=======================================");
		log.debug("Executing libraryPageCloseLense Keyword");
		//this keyword checks whether a particular part of the page got loaded

		try {
			WebElement closeIcon = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			if(closeIcon.isDisplayed()){
				closeIcon.click();
				return "Pass";
			}
			else 
				return "Fail";



		}catch(Throwable t) {
			log.debug("close Icon not present"+ objectArr[0] + t.getMessage());
			return "Fail";
		}
	}

	public String getCount()

	{

		log.debug("=============================");
		log.debug("Executing getCount Keyword");
		try
		{
			List<WebElement> actualdata = driver.findElements(By.xpath(OR.getProperty(objectArr[0])));
			int actualcount = actualdata.size();
			String count= String.valueOf(actualcount);
			testData.setCellData(currentTest, data_column_nameArr[0], testRepeat, count );
			log.debug("Actual count : "+actualcount);
			return "Pass";
		}catch(Throwable e)
		{
			log.debug("Error while executing getcount " + e.getMessage());
			return "Fail";
		}
	}

	public String compareTwoStrings()
	{
		log.debug("=======================================");
		log.debug("Executing compareTwoStrings Keyword");

		try {
			String value1,value2,getValFromTestData = null;

			try {
				getValFromTestData = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);

			}catch(Throwable e) {
				//do nothing
			}

			if(getValFromTestData.equals("default"))
				value1 = driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).getText();
			else 
				value1 = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);

			if(value1.contains("_.-/+',()&:"))
				value1 = Functions.replaceAll(value1, "[^-_./+',()&:]", "");


			if(driver.getTitle().contains("Search Results") || driver.getTitle().contains("Home"))
				value2 = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getAttribute("value");
			else if ( getValFromTestData.equals("src"))
				value2 = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getAttribute("src");
			else
				value2 = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();


			if(value1.equals(value2)) {
				log.debug("value1 is :" + value1);
				log.debug("value2 is :" + value2);
				return "Pass";}
			else {
				log.debug("value1 is :" + value1);
				log.debug("value2 is :" + value2);
				return "Fail";
			}

		}catch(Throwable t) {
			log.debug("error while executing compareTwoStrings keyword "+ objectArr[0] + t.getMessage());
			return "Fail";
		}
	}

	public String compareText()
	{
		log.debug("=======================================");
		log.debug("Executing compareText Keyword");

		try {
			String value1,value2 = null;

			try {
				value1 = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
				log.debug("value1  data sheet is :" + value1);

			}catch(Throwable e) {
				log.debug("Data from sheet isn't fetched");
				value1 = null;
				//do nothing
			}

			value2 = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			log.debug("value2 application value is :" + value2);
			if(value2.contains("_.-/+',()&:"))
				value2 = Functions.replaceAll(value2, "[^-_. /+',()&:]", "");	
			if(value1.contains("_.-/+',()&:"))
				value1 = Functions.replaceAll(value1, "[^-_. /+',()&:]", "");	
			if(value2.trim().toLowerCase().trim().contains(value1.toLowerCase().trim())) {
				log.debug("value1 is :" + value1);
				log.debug("value2 is :" + value2);
				return "Pass";}
			else {
				log.debug("value1 is :" + value1);
				log.debug("value2 is :" + value2);
				return "Fail";
			}

		}catch(Throwable t) {
			log.debug("error while executing compareText keyword "+ objectArr[0] + t.getMessage());
			return "Fail";
		}
	}
	public String compareTwoDataColumns()
	{
		log.debug("=======================================");
		log.debug("Executing compareTwoDataColumns Keyword");

		try {
			String flag;
			String value1,value2 ;		
			try {value1 = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			value2 = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
			flag= testData.getCellData(currentTest, data_column_nameArr[2],testRepeat);
			}catch(Throwable t) {
				log.debug("error while executing compareTwoDataColumns keyword "+ objectArr[0] + t.getMessage());
				return "Fail";
			}
			if(value1.contains("_.-/+',()&:"))
				value1 = Functions.replaceAll(value1, "[^-_./+',()&:]", "").toString();
			if(value2.contains("_.-/+',()&:"))
				value2 = Functions.replaceAll(value2, "[^-_./+',()&:]", "").toString();
			log.debug("value1 is :" + value1);
			log.debug("value2 is :" + value2);
			log.debug("flag is :" + flag);
			if(flag.equalsIgnoreCase("True"))
			{if(value1.trim().equals(value2.trim())  ) {
				log.debug("value1 is :" + value1);
				log.debug("value2 is :" + value2);
				return "Pass";}
			else {
				log.debug("value1 is :" + value1);
				log.debug("value2 is :" + value2);
				return "Fail";}
			}
			else 
			{
				if(value1.trim().equals(value2.trim())  ) {
					log.debug("value1 is :" + value1);
					log.debug("value2 is :" + value2);
					return "Fail";}
				else {
					log.debug("value1 is :" + value1);
					log.debug("value2 is :" + value2);
					return "Pass";}			
			}

		}catch(Throwable t) {
			log.debug("error while executing compareTwoDataColumns keyword "+ objectArr[0] + t.getMessage());
			return "Fail";
		}
	}

	public String managersProfile_ListView() {
		log.debug("=============================");
		log.debug("managersProfile_ListView");
		List<String> names=new ArrayList<String>();
		List<String> assetClass=new ArrayList<String>();
		List<String> geography=new ArrayList<String>();
		List<String> strategy=new ArrayList<String>();
		List<String> assetValue=new ArrayList<String>();

		List<String> names2=new ArrayList<String>();
		List<String> assetClass2=new ArrayList<String>();
		List<String> geography2=new ArrayList<String>();
		List<String> strategy2=new ArrayList<String>();
		List<String> assetValue2=new ArrayList<String>();

		WebElement listbutton = getWebElement(OR, "aims.managersProfile.ListViewButton");
		WebElement gridbutton = getWebElement(OR, "aims.managersProfile.GridViewButton");

		int count = 0;
		int totalCount=0;
		boolean result=false;

		try {

			String whatToDoString=objectArr[0];
			System.out.println("what to do is   :" + whatToDoString );
			int whatToDo=0;
			if(whatToDoString.equalsIgnoreCase("aims.subSearch")){
				whatToDo=1;
			}
			else if(whatToDoString.equalsIgnoreCase("aims.sortByNameAsc")){
				whatToDo=2;
				driver.findElement(By.xpath(OR.getProperty("aims.managersProfile.listView.sortName"))).click();
				Thread.sleep(WAIT2SEC);
			}
			else if(whatToDoString.equalsIgnoreCase("aims.sortByNameDesc")){
				whatToDo=22;
			}
			else if(whatToDoString.equalsIgnoreCase("aims.sortByAssetClassAsc")){
				whatToDo=3;
				driver.findElement(By.xpath(OR.getProperty("aims.managersProfile.listView.sortAssetClass"))).click();
				Thread.sleep(WAIT2SEC);
			}
			else if(whatToDoString.equalsIgnoreCase("aims.sortByAssetClassDesc")){
				whatToDo=33;
			}
			else if(whatToDoString.equalsIgnoreCase("aims.sortByStrategyAsc")){
				whatToDo=4;
				driver.findElement(By.xpath(OR.getProperty("aims.managersProfile.listView.sortStrategy"))).click();
				Thread.sleep(WAIT2SEC);
			}
			else if(whatToDoString.equalsIgnoreCase("aims.sortByStrategyDesc")){
				whatToDo=44;
			}
			else if(whatToDoString.equalsIgnoreCase("aims.sortByGeographyAsc")){
				whatToDo=5;
				driver.findElement(By.xpath(OR.getProperty("aims.managersProfile.listView.sortGeography"))).click();
				Thread.sleep(WAIT2SEC);
			}
			else if(whatToDoString.equalsIgnoreCase("aims.sortByGeographyDesc")){
				whatToDo=55;
			}
			else if(whatToDoString.equalsIgnoreCase("aims.sortByAUMAsc")){
				whatToDo=6;
				driver.findElement(By.xpath(OR.getProperty("aims.managersProfile.listView.sortAUM"))).click();
				Thread.sleep(WAIT2SEC);
			}
			else if(whatToDoString.equalsIgnoreCase("aims.sortByAUMDesc")){
				whatToDo=66;
			}
			else if(whatToDoString.equalsIgnoreCase("aims.count")){
				whatToDo=7;
			}
			else if(whatToDoString.equalsIgnoreCase("aims.managementTeamCarouselCount")){
				whatToDo=8;
			}

			List<WebElement> search = driver.findElements(By.tagName("div"));

			for(WebElement e:search)

			{

				if(e.getAttribute("class").equals("overview")){
					List<WebElement> s2 = e.findElements(By.tagName("div"));

					for(WebElement e2:s2)
					{

						if(( e2.getAttribute("class").contains("dsp-row row-height force-hover ng-scope"))  ){
							List<WebElement> s3 = e2.findElements(By.tagName("div"));

							count=0;
							for(WebElement e3:s3)

							{

								if ((e3.getAttribute("class")
										.contains("name-cell"))) {
									String value = e3.getAttribute("innerHTML");
									System.out.println("count is : " + count  + " " +value);
									if (!value.isEmpty())
										names.add(value);
									totalCount++;
									// System.out.println("name  " +totalCount +
									// " added");
								}
								if ((e3.getAttribute("class")
										.contains("ac-cell"))) {
									String value = e3.getAttribute("innerHTML");
									System.out.println("count is : " + count  + " " +value);
									if (!value.isEmpty())
										assetClass.add(value);
									// count++;
									// System.out.println("asset added");
								}
								if ((e3.getAttribute("class")
										.contains("geog-cell"))) {
									String value = e3.getAttribute("innerHTML");
									System.out.println("count is : " + count  + " " +value);
									if (!value.isEmpty())
										geography.add(value);
									// count++;
									// System.out.println("geo added");
								}
								if ((e3.getAttribute("class")
										.contains("strat-cell"))) {
									String value = e3.getAttribute("innerHTML");
									System.out.println("count is : " + count  + " " +value);
									if (!value.isEmpty())
										strategy.add(value);
									// count++;
									// System.out.println("str added");
								}
								if ((e3.getAttribute("class")
										.contains("aum-cell"))) {
									String value = e3.getAttribute("innerHTML");
									System.out.println("count is : " + count  + " " +value);
									if (!value.isEmpty())
										assetValue.add(value);
									// count++;
									// System.out.println("AUM added");
								}

								count++;

							}

						}
					}
				}
			}



			switch(whatToDo){
			case 1://subsearch
				String searchString = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
				String whatToSearch=objectArr[1];
				String sName,s2Asset,s3Geo,s4Strat,s5AssetVal;
				System.out.println("search string is : " + searchString);
				for(int k=0; k<names.size(); k++){
					int falseCount=0;
					System.out.println("names :" + names.get(k));
					if(whatToSearch.equals("aims.name")) {
						sName = names.get(k).toString().trim().toLowerCase();
						if( ! (sName.contains(searchString)))
							++falseCount;
					}
					else if(whatToSearch.equals("aims.assetClass")) {
						s2Asset = assetClass.get(k).toString().trim().toLowerCase();
						if( ! (s2Asset.contains(searchString)))
							++falseCount;}
					else if(whatToSearch.equals("aims.geo")) {
						s3Geo = geography.get(k).toString().trim().toLowerCase();
						if( ! (s3Geo.contains(searchString)))
							++falseCount;
					}
					else if(whatToSearch.equals("aims.strategy")) {
						s4Strat = strategy.get(k).toString().trim().toLowerCase();
						if( ! (s4Strat.contains(searchString)))
							++falseCount;
					}
					else if(whatToSearch.equals("aims.aum")) {
						s5AssetVal = assetValue.get(k).toString().trim();
						if( ! (s5AssetVal.contains(searchString)))
							++falseCount;
					}

					if(falseCount==5){
						result=false;
						break;
					}
					else
						result=true;
				}
				break;

			case 2://sort by Name ascending
				names2.addAll(names);
				System.out.println(names.size());
				System.out.println(names2.size());
				gridbutton.click();
				Thread.sleep(5000);
				getWebElement(OR,"aims.managersProfile.gridViewSort").click();
				getWebElement(OR,"aims.managersProfile.gridViewSortByDate").click();
				Thread.sleep(5000);
				listbutton.click();
				Collections.sort(names2, String.CASE_INSENSITIVE_ORDER);

				for (int k = 0; k < names.size(); k++) {

					System.out.println("names :" + names.get(k)
							+ "names sorted  " + names2.get(k));
					String s = names.get(k).toString().trim();
					String s2 = names2.get(k).toString().trim();

					if (!(s.equalsIgnoreCase(s2))) {
						result = false;
						break;
					} else
						result = true;
				}
				break;
			case 22://sort by Name descending
				driver.findElement(By.xpath(OR.getProperty("aims.managersProfile.listView.sortName"))).click();
				Thread.sleep(WAIT3SEC);
				List<WebElement> searchName = driver.findElements(By.tagName("div"));

				for(WebElement e : searchName)

				{

					if(e.getAttribute("class").equals("overview")){
						List<WebElement> s2 = e.findElements(By.tagName("div"));

						for(WebElement e2 : s2)

						{

							if(( e2.getAttribute("class").contains("dsp-row row-height force-hover ng-scope"))  ){
								List<WebElement> s3 = e2.findElements(By.tagName("div"));

								count=0;
								for(WebElement e3 : s3)

								{

									if ((e3.getAttribute("class")
											.contains("name-cell"))) {
										String value = e3.getAttribute("innerHTML");
										// System.out.println("count is : " + count
										// + " " +value);
										if (!value.isEmpty())
											names2.add(value);
										totalCount++;
										// System.out.println("name  " +totalCount +
										// " added");
									}
								}
							}
						}
					}
				}

				Collections.reverse(names);
				for (int k = 0; k < names.size(); k++) {

					System.out.println("names :" + names.get(k) + "names2   "
							+ names2.get(k));
					String s = names.get(k).toString().trim();
					String s2 = names2.get(k).toString().trim();

					if (!(s.equalsIgnoreCase(s2))) {
						result = false;
						break;
					} else
						result = true;
				}
				break;

			case 3://sort by Asset Class ascending
				assetClass2.addAll(assetClass);
				System.out.println(assetClass.size());
				System.out.println(assetClass2.size());
				gridbutton.click();
				Thread.sleep(5000);
				getWebElement(OR,"aims.managersProfile.gridViewSort").click();
				getWebElement(OR,"aims.managersProfile.gridViewSortByDate").click();
				Thread.sleep(5000);
				listbutton.click();
				Collections.sort(assetClass2, String.CASE_INSENSITIVE_ORDER);

				for (int k = 0; k < assetClass.size(); k++) {

					System.out.println("assetClass :" + assetClass.get(k)
							+ "assetClass sorted  " + assetClass2.get(k));
					String s = assetClass.get(k).toString().trim();
					String s2 = assetClass2.get(k).toString().trim();

					if (!(s.equalsIgnoreCase(s2))) {
						result = false;
						break;
					} else
						result = true;
				}
				break;



			case 33://sort by Asset Class descending
				driver.findElement(By.xpath(OR.getProperty("aims.managersProfile.listView.sortAssetClass"))).click();
				Thread.sleep(WAIT3SEC);
				List<WebElement> searchAC = driver.findElements(By.tagName("div"));

				for(WebElement e : searchAC)

				{

					if(e.getAttribute("class").equals("overview")){
						List<WebElement> s2 = e.findElements(By.tagName("div"));

						for(WebElement e2 : s2)

						{

							if(( e2.getAttribute("class").contains("dsp-row row-height force-hover ng-scope"))  ){
								List<WebElement> s3 = e2.findElements(By.tagName("div"));

								count=0;
								for(WebElement e3 : s3)

								{

									if ((e3.getAttribute("class")
											.contains("ac-cell"))) {
										String value = e3.getAttribute("innerHTML");
										// System.out.println("count is : " + count
										// + " " +value);
										if (!value.isEmpty())
											assetClass2.add(value);
										totalCount++;
										// System.out.println("name  " +totalCount +
										// " added");
									}
								}
							}
						}
					}
				}

				Collections.reverse(assetClass);
				for (int k = 0; k < assetClass.size(); k++) {

					System.out.println("assetClass :" + assetClass.get(k) + "assetClass2   "
							+ assetClass2.get(k));
					String s = assetClass.get(k).toString().trim();
					String s2 = assetClass2.get(k).toString().trim();

					if (!(s.equalsIgnoreCase(s2))) {
						result = false;
						break;
					} else
						result = true;
				}
				break;

			case 4://sort by strategy  ascending
				strategy2.addAll(strategy);
				System.out.println(strategy.size());
				System.out.println(strategy2.size());
				gridbutton.click();
				Thread.sleep(5000);
				getWebElement(OR,"aims.managersProfile.gridViewSort").click();
				getWebElement(OR,"aims.managersProfile.gridViewSortByDate").click();
				Thread.sleep(5000);
				listbutton.click();
				Collections.sort(strategy2, String.CASE_INSENSITIVE_ORDER);

				for (int k = 0; k < strategy.size(); k++) {

					System.out.println("strategy :" + strategy.get(k)
							+ "strategy2   " + strategy2.get(k));
					String s = strategy.get(k).toString().trim();
					String s2 = strategy2.get(k).toString().trim();

					if (!(s.equalsIgnoreCase(s2))) {
						result = false;
						break;
					} else
						result = true;
				}
				break;



			case 44://sort by strategy descending
				driver.findElement(By.xpath(OR.getProperty("aims.managersProfile.listView.sortStrategy"))).click();
				Thread.sleep(WAIT3SEC);
				List<WebElement> searchStrategy = driver.findElements(By.tagName("div"));

				for(WebElement e : searchStrategy)

				{

					if(e.getAttribute("class").equals("overview")){
						List<WebElement> s2 = e.findElements(By.tagName("div"));

						for(WebElement e2 : s2)

						{

							if(( e2.getAttribute("class").contains("dsp-row row-height force-hover ng-scope"))  ){
								List<WebElement> s3 = e2.findElements(By.tagName("div"));

								count=0;
								for(WebElement e3 : s3)

								{

									if ((e3.getAttribute("class")
											.contains("strat-cell"))) {
										String value = e3.getAttribute("innerHTML");
										// System.out.println("count is : " + count
										// + " " +value);
										if (!value.isEmpty())
											strategy2.add(value);
										totalCount++;
										// System.out.println("name  " +totalCount +
										// " added");
									}
								}
							}
						}
					}
				}

				Collections.reverse(strategy);
				for (int k = 0; k < strategy.size(); k++) {

					System.out.println("strategy :" + strategy.get(k) + "strategy2   "
							+ strategy2.get(k));
					String s = strategy.get(k).toString().trim();
					String s2 = strategy2.get(k).toString().trim();

					if (!(s.equalsIgnoreCase(s2))) {
						result = false;
						break;
					} else
						result = true;
				}
				break;

			case 5://sort by geography  ascending
				geography2.addAll(geography);
				System.out.println(geography.size());
				System.out.println(geography2.size());
				gridbutton.click();
				Thread.sleep(5000);
				getWebElement(OR,"aims.managersProfile.gridViewSort").click();
				getWebElement(OR,"aims.managersProfile.gridViewSortByDate").click();
				Thread.sleep(5000);
				listbutton.click();
				Collections.sort(geography2, String.CASE_INSENSITIVE_ORDER);

				for (int k = 0; k < geography.size(); k++) {

					System.out.println("geography :" + geography.get(k)
							+ "geography2   " + geography2.get(k));
					String s = geography.get(k).toString().trim();
					String s2 = geography2.get(k).toString().trim();

					if (!(s.equalsIgnoreCase(s2))) {
						result = false;
						break;
					} else
						result = true;
				}
				break;



			case 55://sort by geography descending
				driver.findElement(By.xpath(OR.getProperty("aims.managersProfile.listView.sortGeography"))).click();
				Thread.sleep(WAIT3SEC);
				List<WebElement> searchGeography = driver.findElements(By.tagName("div"));

				for(WebElement e : searchGeography)

				{

					if(e.getAttribute("class").equals("overview")){
						List<WebElement> s2 = e.findElements(By.tagName("div"));

						for(WebElement e2 : s2)

						{

							if(( e2.getAttribute("class").contains("dsp-row row-height force-hover ng-scope"))  ){
								List<WebElement> s3 = e2.findElements(By.tagName("div"));

								count=0;
								for(WebElement e3 : s3)

								{

									if ((e3.getAttribute("class")
											.contains("geog-cell"))) {
										String value = e3.getAttribute("innerHTML");
										// System.out.println("count is : " + count
										// + " " +value);
										if (!value.isEmpty())
											geography2.add(value);
										totalCount++;
										// System.out.println("name  " +totalCount +
										// " added");
									}
								}
							}
						}
					}
				}

				Collections.reverse(geography);
				for (int k = 0; k < geography.size(); k++) {

					System.out.println("geography :" + geography.get(k) + "geography2   "
							+ geography2.get(k));
					String s = geography.get(k).toString().trim();
					String s2 = geography2.get(k).toString().trim();

					if (!(s.equalsIgnoreCase(s2))) {
						result = false;
						break;
					} else
						result = true;
				}
				break;

			case 6://sort by AUM  ascending

				gridbutton.click();
				Thread.sleep(5000);
				getWebElement(OR,"aims.managersProfile.gridViewSort").click();
				getWebElement(OR,"aims.managersProfile.gridViewSortByDate").click();
				Thread.sleep(5000);
				listbutton.click();
				for(int k=0; k<assetValue.size()-1; k++){

					System.out.println("assetValue :" + assetValue.get(k));
					String s = assetValue.get(k).toString().trim().toLowerCase();
					String s2 = assetValue.get(k+1).toString().trim().toLowerCase();
					double current = Functions.getDoubleAUMval(s);
					double next = Functions.getDoubleAUMval(s2);
					if( ! (current<=next)){
						System.out.println("current : "+ current + "  next : "+ next );
						result=false;
						break;
					}
					else{
						System.out.println("current : "+ current + "  next : "+ next );
						result=true;
					}
				}
				break;



			case 66://sort by AUM descending
				driver.findElement(By.xpath(OR.getProperty("aims.managersProfile.listView.sortAUM"))).click();
				Thread.sleep(WAIT3SEC);
				List<WebElement> searchAUM = driver.findElements(By.tagName("div"));

				for(WebElement e : searchAUM)

				{

					if(e.getAttribute("class").equals("overview")){
						List<WebElement> s2 = e.findElements(By.tagName("div"));

						for(WebElement e2 : s2)

						{

							if(( e2.getAttribute("class").contains("dsp-row row-height force-hover ng-scope"))  ){
								List<WebElement> s3 = e2.findElements(By.tagName("div"));

								count=0;
								for(WebElement e3 : s3)

								{

									if ((e3.getAttribute("class")
											.contains("aum-cell"))) {
										String value = e3.getAttribute("innerHTML");
										// System.out.println("count is : " + count
										// + " " +value);
										if (!value.isEmpty())
											assetValue2.add(value);
										totalCount++;
										// System.out.println("name  " +totalCount +
										// " added");
									}
								}
							}
						}
					}
				}

				for(int k=0; k<assetValue2.size()-1; k++){

					System.out.println("assetValue2 :" + assetValue2.get(k));
					String s = assetValue2.get(k).toString().trim().toLowerCase();
					String s2 = assetValue2.get(k+1).toString().trim().toLowerCase();
					double current = Functions.getDoubleAUMval(s);
					double next = Functions.getDoubleAUMval(s2);
					if( ! (current>=next)){
						System.out.println("current : "+ current + "  next : "+ next );
						result=false;
						break;
					}
					else{
						System.out.println("current : "+ current + "  next : "+ next );
						result=true;
					}

				}

				break;



			case 7://count the number shown in brackets and the number of results listed
				String inBracketsString=driver.findElement(By.xpath(OR.getProperty("aims.managerProfile.FundCount"))).getText();
				int inBrackets=Integer.parseInt(inBracketsString);
				if(totalCount==inBrackets)
					result=true;
				else
					result=false;

				break;

			case 8:////On the management team overlay , check if the default number of images shown is four
				List<WebElement> teamMembers = driver.findElements(By.tagName("li"));

				int teamMembersCount=0;
				for(WebElement e : teamMembers)
				{

					if(e.getAttribute("class").contains("team_member_item")){
						++teamMembersCount;
					}
				}
				if(teamMembersCount>=1)
					result=true;
				else
					result=false;

				break;
			}//closes switch loop

			if (result == true)
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing managersProfile_ListView -"
					+ objectArr[0] + t.getMessage());
			return "Fail";
		}
	}


	public String managersProfile_GridView() {
		log.debug("=============================");
		log.debug("managersProfile_GridView");
		List<String> names=new ArrayList<String>();
		List<String> namesSorted=new ArrayList<String>();
		List<String> assetClass=new ArrayList<String>();
		int count = 0;
		boolean result=false;
		int totalCount=0;
		try {

			String whatToDoString=objectArr[0];
			int whatToDo=0;
			if(whatToDoString.equalsIgnoreCase("aims.subSearch")){
				whatToDo=1;
			}
			else if(whatToDoString.equalsIgnoreCase("aims.sortByTitle")){
				whatToDo=2;
			}
			else if(whatToDoString.equalsIgnoreCase("aims.count")){
				whatToDo=3;
			}

			System.out.println("what to do is   :" + whatToDoString );

			List<WebElement> search = driver.findElements(By.tagName("div"));

			for(WebElement e : search)

			{

				if(e.getAttribute("class").contains("grid-item-details")){
					List<WebElement> s2 = e.findElements(By.tagName("span"));

					for(WebElement e2 : s2)

					{
						if(( e2.getAttribute("class").contains("title ng-binding"))  ){
							String value = e2.getAttribute("innerHTML");
							System.out.println("title is : " + count + " " +value);
							if(! value.isEmpty())
								names.add(value);
							totalCount++;
							//System.out.println("name  " +totalCount + " added");
							count++;
						}
					}
				}
			}

			List<WebElement> searchLi = driver.findElements(By.tagName("div"));

			for(WebElement e : searchLi)

			{

				if(e.getAttribute("class").contains("grid-item-details")){
					List<WebElement> s3 = e.findElements(By.tagName("li"));

					for(WebElement e3 : s3)

					{

						if(( e3.getAttribute("class").equals("ng-binding"))  ){
							String value = e3.getAttribute("innerHTML");
							System.out.println("assetValue is : " + count + " " +value);
							if(! value.isEmpty())
								assetClass.add(value);
							//System.out.println("name  " +totalCount + " added");
						}
					}
				}
			}



			System.out.println(names.size());
			System.out.println(assetClass.size());

			switch(whatToDo){
			case 1://subsearch
				String searchString = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
				for(int k=0; k<names.size(); k++){
					int falseCount=0;
					System.out.println("names :" + names.get(k));
					String s = names.get(k).toString().trim().toLowerCase();
					String s2 = assetClass.get(k).toString().trim().toLowerCase();
					System.out.println(s + " " + s2);
					if( ! (s.contains(searchString)))
						++falseCount;

					if( ! (s2.contains(searchString)))
						++falseCount;

					if(falseCount==2){
						result=false;
						break;
					}
					else
						result=true;
				}
				break;

			case 2://sort by title
				namesSorted.addAll(names);
				System.out.println(names.size());
				System.out.println(namesSorted.size());
				Collections.sort(namesSorted, String.CASE_INSENSITIVE_ORDER);

				for (int k = 0; k < names.size(); k++) {

					System.out.println("names :" + names.get(k)
							+ "names sorted  " + namesSorted.get(k));
					String s = names.get(k).toString().trim();
					String s2 = namesSorted.get(k).toString().trim();

					if (!(s.equalsIgnoreCase(s2))) {
						result = false;
						break;
					} else
						result = true;
				}
				break;



			case 3://count the number shown in brackets and the number of rows listed
				String inBracketsString=driver.findElement(By.xpath(OR.getProperty("aims.managerProfile.FundCount"))).getText();
				int inBrackets=Integer.parseInt(inBracketsString);
				if(count==inBrackets)
					result=true;
				else
					result=false;

				break;

			}

			if(result)
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing managersProfile_GridView -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}


	public String managersProfile_FundLink() {
		log.debug("=============================");
		log.debug("Executing managersProfile_FundLink Keyword");
		String flag="true";
		try {
			String linkText=driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
			Thread.sleep(WAIT5SEC);
			driver.findElement(By.linkText("View Fund Profile")).click();
			Thread.sleep(WAIT5SEC);
			String titleText=driver.findElement(By.xpath(OR.getProperty("aims.fundProfile.pageTitle"))).getText();
			if(! linkText.equals(titleText)){
				flag="false";
			}
			if(flag.equalsIgnoreCase("false")){
				log.debug("linkText :" + linkText);
				log.debug("titleText :" + titleText);
				return "Fail";
			}
			else
				return "Pass";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing managersProfile_FundLink -" +  t.getMessage());
			return "Fail";
		}
	}


	public String managersProfile_PopUp_RelatedFunds() {
		log.debug("=============================");
		log.debug("Executing managersProfile_PopUp_RelatedFunds Keyword");
		String flag="true";
		try {
			String linkText=driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			Thread.sleep(WAIT1SEC);
			driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
			Thread.sleep(WAIT5SEC);
			Thread.sleep(WAIT5SEC);
			String titleText=driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).getText();
			Thread.sleep(WAIT1SEC);
			if(! linkText.equals(titleText)){
				log.debug("linkText :" + linkText);
				log.debug("titleText :" + titleText);
				flag="false";
			}
			if(flag.equalsIgnoreCase("false")){
				log.debug("linkText :" + linkText);
				log.debug("titleText :" + titleText);
				return "Fail";
			}
			else
				return "Pass";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing managersProfile_PopUp_RelatedFunds -" +  t.getMessage());
			return "Fail";
		}
	}

	public String verifyClassAttribute() {
		log.debug("=====================================");
		log.debug("Executing verifyClassAttribute Keyword");
		//this keyword checks whether the particular element is active/current or not
		//objectArr[0] is the object ;

		try {
			String attributeVal,activeCurrentSelectedVal,parentVal,objectParent = null;
			attributeVal = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			log.debug("Attribute to check for is: " + attributeVal);
			activeCurrentSelectedVal = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
			log.debug("Value to verify in attribute is: " + activeCurrentSelectedVal);

			try {
				parentVal = testData.getCellData(currentTest, data_column_nameArr[2],testRepeat);
				log.debug("Parent val is: " + objectParent);
				objectParent = OR.getProperty(objectArr[0]) + parentVal;
			}catch(Throwable e) {
				//do nothing
				objectParent = OR.getProperty(objectArr[0]);
			}

			WebElement elementLi = driver.findElement(By.xpath(objectParent));

			String getClassVal = elementLi.getAttribute(attributeVal);

			if(getClassVal.contains(activeCurrentSelectedVal)) {
				log.debug("actual Value is :" + getClassVal);
				log.debug("expected Value is :" + activeCurrentSelectedVal);
				return "Pass";
			}
			else {
				log.debug("actual Value is : :" + getClassVal);
				log.debug("expected  Value is : :" + activeCurrentSelectedVal);
				return "Fail";
			}

		}catch(Throwable t) {
			log.debug("Error while executing verifyClassAttribute -"+ objectArr[0] + t.getMessage());
			return "Fail";
		}
	}

	
	
	public String sortGeneric() {
		log.debug("=============================");
		log.debug("executing keyword sortGeneric");
		List<String> names=new ArrayList<String>();
		List<String> namesSorted=new ArrayList<String>();
		String ascOrDesc = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
		String attribute = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
		
		boolean result=false;
		String value = null;
		String dragThumbLocator=null;

		try {
			Thread.sleep(WAIT2SEC);
			String rowsLocator = OR.getProperty(objectArr[0]);
			try{
				dragThumbLocator = OR.getProperty(objectArr[1]);
			}catch(Throwable t){
				//do nothing 
			}

			String totalRowCount = driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).getText();
			log.debug("total results is" + totalRowCount);

			if(Integer.parseInt(totalRowCount)==0)
				return "Fail-Data not present";

			if(!(dragThumbLocator==null)){
				boolean present =getWebElement(OR,objectArr[1]).isDisplayed();

				if(present){
					Functions.dragTillAllRowsLoaded(driver, log, rowsLocator, dragThumbLocator, Integer.parseInt(totalRowCount), 100);
				}
			}
			List<WebElement> search = getWebElements(OR,objectArr[0]);

			for (WebElement item : search) {
				value = item.getAttribute(attribute).trim();
				log.debug("unsorted value is : " + value );
				if(! value.isEmpty())
					names.add(value);
			}

			namesSorted.addAll(names);

			if(value.contains("$")) {
				//for sort by AUM
				result = Functions.sortAum(names, names.size(), ascOrDesc);

				if(result==true)
					return "Pass";
				else
					return "Fail";
			}

			log.debug(names.size());
			log.debug(namesSorted.size());
			if(ascOrDesc.equals("asc"))
				Collections.sort(namesSorted, String.CASE_INSENSITIVE_ORDER);
			else {
				Collections.sort(namesSorted, String.CASE_INSENSITIVE_ORDER);
				Collections.reverse(namesSorted);
			}

			for(int k=0; k<names.size(); k++){

				log.debug("Names:" + names.get(k)+ " Names sorted:  "+ namesSorted.get(k));
				String s = names.get(k).toString().trim();
				String s2 = namesSorted.get(k).toString().trim();

				if(!(s.equalsIgnoreCase(s2)))
				{
					result=false;
					break;
				}
				else
					result=true;
			}

			if(result==true)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			//		 report error
			log.debug("Error while executing sortGeneric -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String verifyListSize() {
		log.debug("=============================");
		log.debug("executing keyword verifyListSize");

		try {

			String size = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();

			if(driver.getTitle().contains("Search Results"))
				size = size.replace(" Results", "");

			int sizeOfList = Integer.parseInt(size);
			List<WebElement> search = driver.findElements(By.xpath(OR.getProperty(objectArr[1])));
			int listCount = search.size();


			if(sizeOfList == listCount) {
				log.debug("UI count: " + sizeOfList + "list count is :" + listCount);
				return "Pass";
			}
			else {
				log.debug("UI count: " + sizeOfList + "list count is :" + listCount);
				return "Fail";
			}


		}catch(Throwable t) {
			//		 report error
			log.debug("Error while executing verifyListSize -" + t.getMessage());
			return "Fail";
		}

	}

	public String searchResults_GridView() {
		log.debug("=============================");
		log.debug("searchResults_GridView");
		List<String> category=new ArrayList<String>();
		List<String> title=new ArrayList<String>();
		List<String> date=new ArrayList<String>();
		List<String> titlesSorted=new ArrayList<String>();
		int count = 0;
		boolean result=false;

		try {

			String whatToDoString=objectArr[0];
			String categoryType=objectArr[1];
			int whatToDo=0;
			if(whatToDoString.equalsIgnoreCase("aims.contentType") || whatToDoString.equalsIgnoreCase("gsam.contentType")){
				whatToDo=1;
			}
			else if(whatToDoString.equalsIgnoreCase("aims.sortByTitle")|| whatToDoString.equalsIgnoreCase("gsam.sortByTitle")){
				whatToDo=2;
			}
			else if(whatToDoString.equalsIgnoreCase("aims.sortByDate")|| whatToDoString.equalsIgnoreCase("gsam.sortByDate")){
				whatToDo=3;
			}
			else if(whatToDoString.equalsIgnoreCase("aims.verifyTargetPage")|| whatToDoString.equalsIgnoreCase("gsam.verifyTargetPage")){
				whatToDo=4;
			}
			else if(whatToDoString.equalsIgnoreCase("aims.verifyDateRange")|| whatToDoString.equalsIgnoreCase("gsam.verifyDateRange")){
				whatToDo=5;
			}

			System.out.println("what to do is   :" + whatToDoString );
			System.out.println("category   :" + categoryType );
			log.debug("what to do is   :" + whatToDoString );
			log.debug("category   :" + categoryType );

			switch(whatToDo){
			case 1://checks if the results shown contains the expected header passsed from the data sheet  eg. MANAGER,FUND,NEWS,etc

				System.out.println("inside case 1 ");
				log.debug("inside case 1 ");

				List<WebElement> headerResults = driver.findElements(By.xpath("//div[@class='overview isotope']/descendant::div[@class='entry-header ng-binding']"));

				System.out.println(headerResults.size());
				log.debug(headerResults.size());
				for(WebElement e : headerResults) {
					if(e.getAttribute("class").contains("entry-header ng-binding")){
						String value = e.getAttribute("innerHTML");
						System.out.println("category is : " + count + " " +value);
						log.debug("category is : " + count + " " +value);
						if(! value.isEmpty())
							category.add(value);
					}
				}

				String contentType = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
				for(int k=0; k<category.size(); k++){

					System.out.println("category :" + category.get(k));
					log.debug("category :" + category.get(k));
					String s = category.get(k).toString().trim();
					if( ! (s.equals(contentType))){
						result=false;
						break;
					}
					else
						result=true;
				}

				break;

			case 2://sort by title
				System.out.println("inside case 2 ");
				log.debug("inside case 2 ");

				List<WebElement> titleResults = null;
				if(categoryType.equalsIgnoreCase("aims.managers") || categoryType.equalsIgnoreCase("aims.news") || categoryType.equalsIgnoreCase("aims.videos")
						|| categoryType.equalsIgnoreCase("aims.playlists") || categoryType.equalsIgnoreCase("gsam.managers") || categoryType.equalsIgnoreCase("gsam.news") || categoryType.equalsIgnoreCase("gsam.videos")
						|| categoryType.equalsIgnoreCase("gsam.playlists")){
					//		titleResults = driver.findElements(By.xpath("//div[@class='overview isotope']/div/div/div/div/div[1]"));
					titleResults = driver.findElements(By.xpath("//div[@class='overview isotope']/descendant::div[@class='entry-title ng-binding']"));
				}
				if(categoryType.equalsIgnoreCase("aims.funds") || categoryType.equalsIgnoreCase("gsam.funds")){
					//			titleResults = driver.findElements(By.xpath("//div[@class='overview isotope']/div/div/div/div[@class='entry-title']/div[1]"));
					titleResults = driver.findElements(By.xpath("//div[@class='overview isotope']/descendant::div[@class='entry-title']/div[1]"));
				}
				if(categoryType.equalsIgnoreCase("aims.documents") || categoryType.equalsIgnoreCase("gsam.documents")){
					//			titleResults = driver.findElements(By.xpath("//div[@class='overview isotope']/div/div/div[3]"));
					titleResults = driver.findElements(By.xpath("//div[@class='overview isotope']/descendant::div[@class='entry-title ng-binding']"));
				}

				System.out.println(titleResults.size());
				log.debug(titleResults.size());

				for(WebElement e : titleResults) {
					System.out.println(e.getAttribute("class"));
					if( e.getAttribute("class").equals("entry-title ng-binding") || e.getAttribute("class").equals("ng-binding") || e.getAttribute("class").equals("entry-titleSub ng-binding")){
						String value = e.getAttribute("innerHTML");
						System.out.println("title is : " + count + " " +value);
						log.debug("title is : " + count + " " +value);
						if(! value.isEmpty())
							title.add(value);

					}

				}

				titlesSorted.addAll(title);
				System.out.println(title.size());
				System.out.println(titlesSorted.size());
				Collections.sort(titlesSorted, String.CASE_INSENSITIVE_ORDER);

				for (int k = 0; k < title.size(); k++) {

					System.out.println("title :" + title.get(k) + "title sorted  " + titlesSorted.get(k));
					log.debug("title :" + title.get(k) + "title sorted  " + titlesSorted.get(k));

					String s = title.get(k).toString().trim();
					String s2 = titlesSorted.get(k).toString().trim();

					if (!(s.equalsIgnoreCase(s2))) {
						result = false;
						break;
					} else
						result = true;
				}
				break;

			case 3: // sort by date
				System.out.println("inside case 3 ");
				log.debug("inside case 3 ");

				List<WebElement> dateResults = null;
				if(categoryType.equalsIgnoreCase("aims.documents") || categoryType.equalsIgnoreCase("gsam.documents")){
					//		dateResults = driver.findElements(By.xpath("//div[@class='overview isotope']/div/div/div/div[1]"));
					dateResults = driver.findElements(By.xpath("//div[@class='overview isotope']/descendant::div[@class='entry-date ng-binding']"));
				}
				if(categoryType.equalsIgnoreCase("aims.news") || categoryType.equalsIgnoreCase("aims.videos") || categoryType.equalsIgnoreCase("gsam.news") || categoryType.equalsIgnoreCase("gsam.videos")){
					dateResults = driver.findElements(By.xpath("//div[@class='overview isotope']/descendant::div[@class='entry-date ng-binding']"));
				}
				if(categoryType.equalsIgnoreCase("aims.managers") || categoryType.equalsIgnoreCase("gsam.managers")){
					//			dateResults = driver.findElements(By.xpath("//div[@class='overview isotope']/div/div/div/div[1]"));
					dateResults = driver.findElements(By.xpath("//div[@class='overview isotope']/descendant::div[contains(text(),'AS OF DATE')]/following-sibling::div[@class='entry-column-right ng-binding']"));
				}

				log.debug(dateResults.size());
				for(WebElement e2 : dateResults) {
					if( e2.getAttribute("class").contains("entry-date ng-binding") || e2.getAttribute("class").contains("entry-column-right ng-binding")){
						String value = e2.getAttribute("innerHTML");
						System.out.println("date  is : " + count + " " +value);
						log.debug("date  is : " + count + " " +value);
						if(! value.isEmpty())
							date.add(value);

					}

				}


				System.out.println(date.size());
				log.debug(date.size());

				Date finalDates[]= new Date[date.size()];

				for(int d=0; d<date.size(); d++){
					String s = date.get(d).toString().trim();
					System.out.println(s);
					String trimmed = s.replaceAll(","," ");
					System.out.println(trimmed);

					if(categoryType.equalsIgnoreCase("aims.managers") || categoryType.equalsIgnoreCase("gsam.managers")){
						SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM yyyy");
						SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM yyyy");
						finalDates[d] = inputFormat.parse(trimmed);
						outputFormat.format( finalDates[d]);
					}
					else{
						SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM dd yyyy");
						SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd yyyy");
						finalDates[d] = inputFormat.parse(trimmed);
						outputFormat.format( finalDates[d]);
					}
					//String output = outputFormat.format( finalDates[d]);

				}

				outer:
					for(int k=0; k<date.size(); k++){
						for(int l=k+1; l<date.size(); l++){
							System.out.println("dates  :" + finalDates[k] +  "  "+ finalDates[l]);
							if(finalDates[k].before(finalDates[l])){
								log.debug(finalDates[k] + "  falls before " + finalDates[l]);
								result=false;
								break outer;
							}
							else
								result=true;
						}
					}
				break;

			case 4: // click on the first result of each category and verify if the correct target page/overlay is reached and close if applicable
				System.out.println("inside case 4 ");
				log.debug("inside case 4 ");

				
				if(categoryType.equalsIgnoreCase("aims.managers")){
					String linkText = driver.findElement(By.xpath(OR.getProperty("aims.searchResults.managers.FirstTitle"))).getText();
					driver.findElement(By.xpath(OR.getProperty("aims.searchResults.managers.FirstTitle"))).click();
					Thread.sleep(WAIT3SEC);
					String managerPageTitleText = driver.findElement(By.xpath(OR.getProperty("aims.managerProfile.HeaderTitle"))).getText();

					if(linkText.equalsIgnoreCase(managerPageTitleText))
						result = true;
					else{
						log.debug("linkText is : " + linkText);
						log.debug("managerPageTitleText is : " + managerPageTitleText);
						result = false;
					}
				}

				else if(categoryType.equalsIgnoreCase("gsam.managers")){
					String linkText = driver.findElement(By.xpath(OR.getProperty("gsam.searchResults.managers.FirstTitle"))).getText();
					driver.findElement(By.xpath(OR.getProperty("gsam.searchResults.managers.FirstTitle"))).click();
					Thread.sleep(WAIT3SEC);
					String managerPageTitleText = driver.findElement(By.xpath(OR.getProperty("gsam.managerProfile.HeaderTitle"))).getText();

					if(linkText.equalsIgnoreCase(managerPageTitleText))
						result = true;
					else{
						log.debug("linkText is : " + linkText);
						log.debug("managerPageTitleText is : " + managerPageTitleText);
						result = false;
					}
				}

				
				if(categoryType.equalsIgnoreCase("aims.funds")){
					String linkText = driver.findElement(By.xpath(OR.getProperty("aims.searchResults.funds.FirstTitle"))).getText();
					driver.findElement(By.xpath(OR.getProperty("aims.searchResults.funds.FirstTitle"))).click();
					Thread.sleep(WAIT2SEC);
					driver.findElement(By.linkText("View Fund Profile")).click();
					Thread.sleep(WAIT3SEC);
					String fundPageTitleText = driver.findElement(By.xpath(OR.getProperty("aims.fundProfile.title.xpath"))).getText();

					if(linkText.equalsIgnoreCase(fundPageTitleText))
						result = true;
					else{
						log.debug("linkText is : " + linkText);
						log.debug("fundPageTitleText is : " + fundPageTitleText);
						result = false;
					}
				}

				else if(categoryType.equalsIgnoreCase("gsam.funds")){
					String linkText = driver.findElement(By.xpath(OR.getProperty("gsam.searchResults.funds.FirstTitle"))).getText();
					driver.findElement(By.xpath(OR.getProperty("gsam.searchResults.funds.FirstTitle"))).click();
					Thread.sleep(WAIT2SEC);
					driver.findElement(By.linkText("View Fund Profile")).click();
					Thread.sleep(WAIT3SEC);
					String fundPageTitleText = driver.findElement(By.xpath(OR.getProperty("gsam.fundProfile.title.xpath"))).getText();

					if(linkText.equalsIgnoreCase(fundPageTitleText))
						result = true;
					else{
						log.debug("linkText is : " + linkText);
						log.debug("fundPageTitleText is : " + fundPageTitleText);
						result = false;
					}
				}
				if(categoryType.equalsIgnoreCase("aims.documents")){
					int stepCount=0;
					String linkText = driver.findElement(By.xpath(OR.getProperty("aims.searchResults.documents.FirstTitle"))).getText();
					driver.findElement(By.xpath(OR.getProperty("aims.searchResults.documents.FirstTitle"))).click();
					Thread.sleep(WAIT2SEC);
					driver.findElement(By.linkText("View")).click();
					++stepCount;
					Thread.sleep(WAIT3SEC);
					if(stepCount>0)
						result = true;
					else
						result = false;
				}
				
				else if(categoryType.equalsIgnoreCase("gsam.documents")){
					int stepCount=0;
					String linkText = driver.findElement(By.xpath(OR.getProperty("gsam.searchResults.documents.FirstTitle"))).getText();
					driver.findElement(By.xpath(OR.getProperty("gsam.searchResults.documents.FirstTitle"))).click();
					Thread.sleep(WAIT2SEC);
					driver.findElement(By.linkText("View")).click();
					++stepCount;
					Thread.sleep(WAIT3SEC);
					if(stepCount>0)
						result = true;
					else
						result = false;
				}
				if(categoryType.equalsIgnoreCase("aims.news")){
					int stepCount=0;
					String linkText = driver.findElement(By.xpath(OR.getProperty("aims.searchResults.news.FirstTitle"))).getText();
					driver.findElement(By.xpath(OR.getProperty("aims.searchResults.news.FirstTitle"))).click();
					++stepCount;
					Thread.sleep(WAIT2SEC);
					if(stepCount>0)
						result = true;
					else
						result = false;
				}
				else if(categoryType.equalsIgnoreCase("gsam.news")){
					int stepCount=0;
					String linkText = driver.findElement(By.xpath(OR.getProperty("gsam.searchResults.news.FirstTitle"))).getText();
					driver.findElement(By.xpath(OR.getProperty("gsam.searchResults.news.FirstTitle"))).click();
					++stepCount;
					Thread.sleep(WAIT2SEC);
					if(stepCount>0)
						result = true;
					else
						result = false;
				}
				if(categoryType.equalsIgnoreCase("aims.videos")){
					int stepCount=0;
					String linkText = driver.findElement(By.xpath(OR.getProperty("aims.searchResults.videos.FirstTitle"))).getText();
					driver.findElement(By.xpath(OR.getProperty("aims.searchResults.videos.FirstTitle"))).click();
					Thread.sleep(WAIT2SEC);
					driver.findElement(By.linkText("View")).click();
					++stepCount;
					Thread.sleep(WAIT3SEC);
					if(stepCount>0)
						result = true;
					else
						result = false;
				}
				if(categoryType.equalsIgnoreCase("gsam.videos")){
					int stepCount=0;
					String linkText = driver.findElement(By.xpath(OR.getProperty("gsam.searchResults.videos.FirstTitle"))).getText();
					driver.findElement(By.xpath(OR.getProperty("gsam.searchResults.videos.FirstTitle"))).click();
					Thread.sleep(WAIT2SEC);
					driver.findElement(By.linkText("View")).click();
					++stepCount;
					Thread.sleep(WAIT3SEC);
					if(stepCount>0)
						result = true;
					else
						result = false;
				}

				if(categoryType.equalsIgnoreCase("aims.playlists")){
					int stepCount=0;
					String linkText = driver.findElement(By.xpath(OR.getProperty("aims.searchResults.playlists.FirstTitle"))).getText();
					driver.findElement(By.xpath(OR.getProperty("aims.searchResults.playlists.FirstTitle"))).click();
					++stepCount;
					Thread.sleep(WAIT2SEC);
					if(stepCount>0)
						result = true;
					else
						result = false;
				}
				else if(categoryType.equalsIgnoreCase("gsam.playlists")){
					int stepCount=0;
					String linkText = driver.findElement(By.xpath(OR.getProperty("gsam.searchResults.playlists.FirstTitle"))).getText();
					driver.findElement(By.xpath(OR.getProperty("gsam.searchResults.playlists.FirstTitle"))).click();
					++stepCount;
					Thread.sleep(WAIT2SEC);
					if(stepCount>0)
						result = true;
					else
						result = false;
				}
				break;

			case 5: // check the date shown for the results fall in the date range selected
				System.out.println("inside case 5 ");
				log.debug("inside case 5 ");

				String filterToBeSelected = "";
				if(categoryType.equalsIgnoreCase("aims.documents")) 
					filterToBeSelected = "aims.searchResults.documentsLabel.xpath";
				else if (categoryType.equalsIgnoreCase("gsam.documents"))
					filterToBeSelected = "gsam.searchResults.documentsLabel.xpath";
				if(categoryType.equalsIgnoreCase("aims.news") || categoryType.equalsIgnoreCase("gsams.news"))
					filterToBeSelected = "aims.searchResults.newsLabel.xpath";
				else if (categoryType.equalsIgnoreCase("gsam.news"))
					filterToBeSelected = "gsam.searchResults.newsLabel.xpath";
				if(categoryType.equalsIgnoreCase("aims.videos") || categoryType.equalsIgnoreCase("gsam.videos"))
					filterToBeSelected = "aims.searchResults.videosLabel.xpath";
				else if (categoryType.equalsIgnoreCase("gsam.news"))
					filterToBeSelected = "gsam.searchResults.videosLabel.xpath";

				String fromDateString,toDateString;
				
				if(categoryType.contains("gsam"))
				{
					driver.findElement(By.xpath(OR.getProperty("gsam.searchResults.filterResults.xpath"))).click();
					Thread.sleep(WAIT1SEC);

					try{
						Functions.waitForElementClickable(driver, log, "gsam.searchResults.documentsLabel.xpath");
					}catch(ArrayIndexOutOfBoundsException e) {

					}

					fromDateString = driver.findElement(By.xpath(OR.getProperty("gsam.searchResults.fromDate.xpath"))).getAttribute("value");
					toDateString = driver.findElement(By.xpath(OR.getProperty("gsam.searchResults.toDate.xpath"))).getAttribute("value");
					System.out.println(fromDateString + "  " + toDateString);
					Thread.sleep(WAIT2SEC);	
					driver.findElement(By.xpath(OR.getProperty(filterToBeSelected))).click();
					Thread.sleep(WAIT1SEC);
					driver.findElement(By.xpath(OR.getProperty("gsam.searchResults.applyFiltersButton.xpath"))).click();
					Thread.sleep(WAIT5SEC);
				}
				else
				{
					driver.findElement(By.xpath(OR.getProperty("aims.searchResults.filterResults.xpath"))).click();
					Thread.sleep(WAIT1SEC);

					try{
						Functions.waitForElementClickable(driver, log, "aims.searchResults.documentsLabel.xpath");
					}catch(ArrayIndexOutOfBoundsException e) {

					}

					fromDateString = driver.findElement(By.xpath(OR.getProperty("aims.searchResults.fromDate.xpath"))).getAttribute("value");
					toDateString = driver.findElement(By.xpath(OR.getProperty("aims.searchResults.toDate.xpath"))).getAttribute("value");
					System.out.println(fromDateString + "  " + toDateString);
					Thread.sleep(WAIT2SEC);	
					driver.findElement(By.xpath(OR.getProperty(filterToBeSelected))).click();
					Thread.sleep(WAIT1SEC);
					driver.findElement(By.xpath(OR.getProperty("aims.searchResults.applyFiltersButton.xpath"))).click();
					Thread.sleep(WAIT5SEC);

				}

				List<WebElement> dateResults2 = null;
				if(categoryType.equalsIgnoreCase("aims.documents") || categoryType.equalsIgnoreCase("gsam.documents")){
					dateResults2 = driver.findElements(By.xpath("//div[@class='overview isotope']/div/div/div/div[1]"));
				}
				if(categoryType.equalsIgnoreCase("aims.news") || categoryType.equalsIgnoreCase("aims.videos") || categoryType.equalsIgnoreCase("gsam.news") || categoryType.equalsIgnoreCase("gsam.videos")){
					dateResults2 = driver.findElements(By.xpath("//div[@class='overview isotope']/div/div/div/div[2]"));
				}

				for(WebElement e2 : dateResults2) {
					if( e2.getAttribute("class").contains("entry-date ng-binding")){
						String value = e2.getAttribute("innerHTML");
						System.out.println("date  is : " + count + " " +value);
						if(! value.isEmpty())
							date.add(value);

					}

				}


				System.out.println(date.size());

				Date finalDates2[]= new Date[date.size()];
				Date finalFromDate = new Date();
				Date finalToDate = new Date();

				SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM dd yyyy");
				SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd yyyy");
				String trimmedFromDate = fromDateString.replaceAll(","," ");
				String trimmedToDate = toDateString.replaceAll(","," ");
				finalFromDate = inputFormat.parse(trimmedFromDate);
				finalToDate = inputFormat.parse(trimmedToDate);
				outputFormat.format( finalFromDate);
				outputFormat.format( finalToDate);

				for(int d=0; d<date.size(); d++){
					String s = date.get(d).toString().trim();
					System.out.println(s);
					String trimmed = s.replaceAll(","," ");
					System.out.println(trimmed);
					finalDates2[d] = inputFormat.parse(trimmed);
					outputFormat.format( finalDates2[d]);
					//String output = outputFormat.format( finalDates[d]);
					//System.out.println("final date : "+ output);
				}


				for(int k=0; k<date.size(); k++){
					System.out.println("dates  :" + finalDates2[k] );
					if(finalDates2[k].before(finalFromDate) &&  finalDates2[k].after(finalFromDate)){
						log.debug(finalDates2[k] + "  is not in the chosen range " );
						result=false;
						break ;
					}
					else
						result=true;
				}
				break;

			}

			if(result)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing searchResults_GridView -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}


	public String searchResults_ListView() {
		log.debug("=============================");
		log.debug("searchResults_ListView");
		List<String> category=new ArrayList<String>();
		List<String> title=new ArrayList<String>();
		List<String> titlesSorted=new ArrayList<String>();
		List<String> date=new ArrayList<String>();

		int count = 0;
		boolean result = false;

		try {

			String whatToDoString=objectArr[0];
			int whatToDo=0;
			if(whatToDoString.equalsIgnoreCase("aims.verifyCategory") || whatToDoString.equalsIgnoreCase("gsam.verifyCategory")){
				whatToDo=1;
			}
			else if(whatToDoString.equalsIgnoreCase("aims.sortByDate")|| whatToDoString.equalsIgnoreCase("gsam.sortByDate")){
				whatToDo=2;
			}
			else if(whatToDoString.equalsIgnoreCase("aims.sortByTitle")|| whatToDoString.equalsIgnoreCase("gsam.sortByTitle")){
				whatToDo=3;
			}

			System.out.println("what to do is   :" + whatToDoString );

			List<WebElement> headerResults = driver.findElements(By.xpath("//div[@class='overview']/div/div/div/span[1]"));

			log.debug(headerResults.size());

			for (WebElement e : headerResults) {
				if(e.getAttribute("class").contains("ng-binding") && (e.getAttribute("title").equals(""))){
					String value = e.getAttribute("innerHTML");
					log.debug("category is : " + count + " " +value);
					if(! value.isEmpty())
						category.add(value);

				}
			}

			List<WebElement> dateResults = driver.findElements(By.xpath("//div[@class='overview']/div/div/div/span[2]"));
			log.debug(dateResults.size());

			for (WebElement e : dateResults) {
				if( e.getAttribute("class").contains("entry-date ng-binding")){
					String value = e.getAttribute("innerHTML");
					log.debug("date is : " + count + " " +value);
					if(! value.isEmpty())
						date.add(value);

				}
			}


			List<WebElement> titleResults = driver.findElements(By.xpath("//div[@class='overview']/div/div/div/span"));
			log.debug(titleResults.size());

			for (WebElement e : titleResults) {
				if( e.getAttribute("class").contains("ng-binding") && !(e.getAttribute("title").equals(""))){
					String value = e.getAttribute("innerHTML");
					log.debug("title is : " + count + " " +value);
					if(! value.isEmpty())
						title.add(value);

				}

			}



			switch(whatToDo){
			case 1://verify category name

				log.debug("inside case 1 ");
				log.debug("inside case 1 ");
				String categoryValue = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
				if(category.size()==0) {
					log.debug("category list size :" + category.size());
					return "Fail";}
				for(int k=0; k<category.size(); k++){

					System.out.println("category names :" + category.get(k));
					log.debug("category names :" + category.get(k));
					String categoryNameFetched = category.get(k).toString().trim();
					if(!(categoryValue.equals("All"))){
						if(!categoryNameFetched.equals(categoryValue)) {
							log.debug("categoryValue is :  " + categoryValue);
							log.debug("categoryNameFetched  is :  " + categoryNameFetched);
							result=false;
							break;}
						else {
							result = true;
							log.debug("categoryValue is :  " + categoryValue);
							log.debug("categoryNameFetched  is :  " + categoryNameFetched);
						}
					}
					else{
						if(!(categoryNameFetched.equals("MANAGER") || categoryNameFetched.equals("FUND") || categoryNameFetched.equals("VIDEO")
								|| categoryNameFetched.equals("NEWS") || categoryNameFetched.equals("DOCUMENT") || categoryNameFetched.equals("PLAYLIST"))) {
							log.debug("categoryValue is :  " + categoryValue);
							log.debug("categoryNameFetched  is :  " + categoryNameFetched);
							result=false;
							break;}
						else {
							result = true;
							log.debug("categoryValue is :  " + categoryValue);
							log.debug("categoryNameFetched  is :  " + categoryNameFetched);
						}	
					}
				}

				break;

			case 2://sort by date

				System.out.println(date.size());

				Date finalDates[]= new Date[date.size()];

				for(int d=0; d<date.size(); d++){
					String s = date.get(d).toString().trim();
					System.out.println(s);
					//	                     String trimmed = s.substring(13);
					//	                     System.out.println(trimmed);
					String trimmed2 = s.replaceAll(",", " ");
					System.out.println(trimmed2);
					SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM dd yyyy");
					SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd yyyy");
					finalDates[d] = inputFormat.parse(trimmed2);
					outputFormat.format( finalDates[d]);
					//String output = outputFormat.format( finalDates[d]);
					//System.out.println("final date : "+ output);
				}

				outer:
					for(int k=0; k<date.size(); k++){
						for(int l=k+1; l<date.size(); l++){
							System.out.println("dates  :" + finalDates[k] +  "  "+ finalDates[l]);
							if(finalDates[k].before(finalDates[l])){
								log.debug(finalDates[k] + "  falls before " + finalDates[l]);
								result=false;
								break outer;
							}
							else
								result = true;
						}
					}
				break;

			case 3://sort by title
				titlesSorted.addAll(title);
				System.out.println(title.size());
				System.out.println(titlesSorted.size());
				Collections.sort(titlesSorted, String.CASE_INSENSITIVE_ORDER);

				for (int k = 0; k < title.size(); k++) {

					System.out.println("names :" + title.get(k) + "names sorted  " + titlesSorted.get(k));
					log.debug("names :" + title.get(k) + "names sorted  " + titlesSorted.get(k));
					String s = title.get(k).toString().trim();
					String s2 = titlesSorted.get(k).toString().trim();

					if (!(s.equalsIgnoreCase(s2))) {
						result = false;
						break;
					} else
						result = true;
				}
				break;
			}



			if(result)
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing searchResults_ListView -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String verifySearchRelatedFunds() {
		log.debug("=============================");
		log.debug("Executing keyword verifySearchRelatedFunds");
		try {
			String searchChar = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).clear();
			driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).sendKeys(searchChar);

			String fetchedResult = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();

			if(searchChar.equals(fetchedResult)) {
				log.debug("Search character is :  " + searchChar);
				log.debug("Fetched Result is :  " + fetchedResult);
				return "Pass";
			}
			else {
				log.debug("Search character is :  " + searchChar);
				log.debug("Fetched Result is :  " + fetchedResult);
				return "Fail";
			}
		}catch(Throwable t) {
			log.debug("Error while executing verifySearchRelatedFunds " +  t.getMessage());
			return "Fail";
		}
	}
	public String verifyTotalCharacterLength()
	{
		log.debug("=============================");
		log.debug("executing keyword verifyTotalCharacterLength");
		// the keyword verifies the total character count for more than one paragraph
		try {
			String length = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			int expectedCharLength = Integer.parseInt(length);
			int initialpara = 0,initialcharcount = 0;
			String initpara = String.valueOf(initialpara);
			String initcharcount = String.valueOf(initialcharcount);
			String actualChar;
			int actualCharLength = 0;

			actualChar = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			actualCharLength = actualChar.length();

			String paracount = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
			int paras = Integer.parseInt(paracount);
			String totalcharcount = testData.getCellData(currentTest, data_column_nameArr[2],testRepeat);
			int totalcharacters = Integer.parseInt(totalcharcount);
			if(paras == 0)
			{
				paras++;
				paracount = String.valueOf(paras);
				testData.setCellData(currentTest, data_column_nameArr[1], testRepeat, paracount);

				totalcharacters=totalcharacters+actualCharLength;
				totalcharcount = String.valueOf(totalcharacters);
				testData.setCellData(currentTest, data_column_nameArr[2], testRepeat, totalcharcount);

				if(actualCharLength <= expectedCharLength) {
					log.debug("actual character length is :  " + actualCharLength);
					log.debug("expected character length is :  " + expectedCharLength);
					return "Pass";
				}
				else {
					log.debug("actual character length is :  " + actualCharLength);
					log.debug("expected character length is :  " + expectedCharLength);
					return "Fail";
				}
			}
			else
			{
				totalcharacters=totalcharacters+actualCharLength;
				testData.setCellData(currentTest, data_column_nameArr[1], testRepeat, initpara);
				testData.setCellData(currentTest, data_column_nameArr[2], testRepeat, initcharcount);

				if(totalcharacters<=expectedCharLength){
					log.debug("total character length expected is :" + expectedCharLength);
					log.debug("total character length actual is :"+ totalcharacters);
					return "Pass";
				}
				else
				{
					log.debug("total character length expected is :" + expectedCharLength);
					log.debug("total character length actual is :"+ totalcharacters);
					return "Fail";
				}
			}
		}catch(Throwable t) {
			log.debug("Error while executing verifyCharacterLength " +  t.getMessage());
			return "Fail";
		}
	}

	public String verifyCharacterLength() {
		log.debug("=============================");
		log.debug("executing keyword verifyCharacterLength");

		try {
			String length = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			int expectedCharLength = Integer.parseInt(length);
			String actualChar,seeMore="";
			int actualCharLength = 0;

			actualChar = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();

			try {
				seeMore = driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).getText();	
				actualCharLength = actualChar.length() + seeMore.length();
			}catch(Throwable e) {
				//do nothing
				actualCharLength = actualChar.length();
			}

			if(actualCharLength <= expectedCharLength) {
				log.debug("actual character lenth is :  " + actualCharLength);
				log.debug("expected character lenth is :  " + expectedCharLength);
				return "Pass";
			}
			else {
				log.debug("actual character lenth is :  " + actualCharLength);
				log.debug("expected character lenth is :  " + expectedCharLength);
				return "Fail";
			}
		}catch(Throwable t) {
			log.debug("Error while executing verifyCharacterLength " +  t.getMessage());
			return "Fail";
		}
	}
	



	public String getCRXContent() {
		log.debug("=============================");
		log.debug("Executing getCRXContent Keyword");

		try {

			String url = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);

			String[] title = driver.getCurrentUrl().split("#");
			String url1= title[0] + url;

			driver.get(url1);		

			log.debug("Get CRX Content - http://" + url1);

			if(!driver.getTitle().contains("CRXDE"))
				return "Fail";
			else
				return "Pass";
		}catch(Throwable e) {
			log.debug("Error while executing getCRXContent " +  e.getMessage());
			return "Fail";
		}
	}

	public String getContent() {
		log.debug("=============================");
		log.debug("Executing getContent Keyword");

		try {
			//	String loginUser = testCONFIG.getProperty("aims.AIMSDomainMedium");
			//	String preUrl[] = loginUser.split("@");
			//	
			String url = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			//	log.debug("Get Content - http://" + preUrl[1] + url);

			String[] title = driver.getCurrentUrl().split("/");
			String currentEnv  = title[2];
			String url1= title[0]+"//" + currentEnv + url;

			driver.get(url1);

			if(!driver.getTitle().contains("gsCIO"))
				return "Fail";
			else
				return "Pass";
		}catch(Throwable e) {
			log.debug("Error while executing getContent " +  e.getMessage());
			return "Fail";
		}
	}

	public String verifyTeamMemberUsingArrow() {
		log.debug("=============================");
		log.debug("Executing verifyTeamMemberUsingArrow Keyword");

		try {
			int loopVar = 0,totalTeamMembers,count,newMargin,val;
			boolean flag = true;
			String marginLeftVal; 

			WebElement carouselNext,carouselPrev;

			List<WebElement> resultRows = driver.findElements(By.xpath(OR.getProperty(objectArr[0])));
			totalTeamMembers = resultRows.size();
			log.debug("Team number of team members present :" + totalTeamMembers);

			if(totalTeamMembers>4) {
				loopVar = totalTeamMembers-4;

				carouselNext = driver.findElement(By.xpath(OR.getProperty(objectArr[1])));
				WebElement ulXpath = driver.findElement(By.xpath(OR.getProperty(objectArr[2])));

				if(loopVar>0) {
					for(count = 0; count<= loopVar; count++) {
						marginLeftVal = ulXpath.getAttribute("style");
						String arrSplit[] = marginLeftVal.split("margin-left: ");
						String arrSplitBeforePX[] = arrSplit[1].split("px");
						val = Integer.parseInt(arrSplitBeforePX[0]);
						newMargin = val/-229;

						if(newMargin != count) {
							flag = false;
							break;
						}

						if(carouselNext.isDisplayed()) {
							carouselNext.click();
							log.debug("Next arrow clicked");
							log.debug("Member : "+(newMargin+1));
						}
						Thread.sleep(WAIT1SEC);

					}

					carouselPrev = driver.findElement(By.xpath(OR.getProperty(objectArr[3])));

					for(count = loopVar; count>= 0; count--) {
						marginLeftVal = ulXpath.getAttribute("style");
						String arrSplit[] = marginLeftVal.split("margin-left: ");
						String arrSplitBeforePX[] = arrSplit[1].split("px");
						val = Integer.parseInt(arrSplitBeforePX[0]);
						newMargin = val/-229;

						if(newMargin != count) {
							flag = false;
							break;
						}

						if(carouselPrev.isDisplayed()) {
							carouselPrev.click();
							log.debug("Previous arrow clicked");
							log.debug("Member : "+(newMargin+1));
						}
						Thread.sleep(WAIT1SEC);
					}
				}

			}else if(totalTeamMembers<=4){
				try {
					carouselNext = driver.findElement(By.xpath(OR.getProperty(objectArr[1])));
					if(carouselNext.isDisplayed())
						flag = false;
				}catch(Throwable e) {
					return "Pass";
				}

			}else if(totalTeamMembers==0){
				log.debug("Team members count is coming zero");
				return "Fail";
			}

			if(flag)
				return "Pass";
			else
				return "Fail";
		}catch(Throwable e) {
			log.debug("Error while executing verifyTeamMemberUsingArrow " +  e.getMessage());
			return "Fail";
		}
	}

	public String verifyScrollbarPosition() {
		log.debug("=============================");
		log.debug("Executing verifyScrollbarPosition");
		try {
			String rowsLocator = OR.getProperty(objectArr[4]);
			String dragThumbLocator = OR.getProperty(objectArr[5]);
			String totalRowCount = driver.findElement(By.xpath(OR.getProperty(objectArr[6]))).getText();
			int totalRowInt = Integer.parseInt(totalRowCount);
			Functions.dragTillAllRowsLoaded(driver, log, rowsLocator, dragThumbLocator, totalRowInt, 100);
			WebElement element1; 
			String val,scrollHeight;
			double topVal,topHeightVal,scrollHeightVal;
			int top=0;
			int pixelsToClick = 0;
			boolean flag = true;	
			element1 = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			val = element1.getAttribute("style");
			String scenario = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			topHeightVal = Functions.pixelValDouble(val, "height:");
			WebElement element2 = driver.findElement(By.xpath(OR.getProperty(objectArr[1])));
			scrollHeight = element2.getAttribute("style");
			scrollHeightVal = Functions.pixelValDouble(scrollHeight, "height:");
			top =(int) -scrollHeightVal;
			Functions.dragTo(driver, element1, top);
			//cases
			if(scenario.equals("bottom")) {
				pixelsToClick = (int) (scrollHeightVal- topHeightVal);
				Functions.dragTo(driver, element1, pixelsToClick);
			}
			else if(scenario.equals("top"))
				pixelsToClick = 0;
			else if(scenario.equals("mid1")) {
				pixelsToClick =(int) (scrollHeightVal- topHeightVal)/2;
				Functions.dragTo(driver, element1, pixelsToClick);
			}
			else if(scenario.equals("mid2")) {
				pixelsToClick = (int) (scrollHeightVal- topHeightVal)/4;
				Functions.dragTo(driver, element1, pixelsToClick);
			}
			//fetch top value now and match it with the pixelsToClick
			element1 = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			val = element1.getCssValue("top");
			topVal = Functions.pixelVal(val);
			if(pixelsToClick != topVal) {
				log.debug("pixels to Click value :" + pixelsToClick);
				log.debug("top value :" + topVal);
				flag = false;}
			//navigate to the other view and return back
			driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).click();
			Thread.sleep(WAIT1SEC);
			driver.findElement(By.xpath(OR.getProperty(objectArr[3]))).click();
			//verify again
			element1 = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			val = element1.getCssValue("top");
			topVal = Functions.pixelVal(val);
			if(pixelsToClick != topVal) {
				log.debug("pixels to Click value :" + pixelsToClick);
				log.debug("top value :" + topVal);
				flag = false;
			}
			if(flag)
			{
				log.debug("pixels to Click value :" + pixelsToClick);
				log.debug("top value :" + topVal);
				return "Pass";}
			else
				return "Fail";
		}catch(Throwable e) {
			log.debug("Error while executing verifyScrollbarPosition " +  e.getMessage());
			return "Fail";
		}
	}


	public String verifyScrollbarPresence() {
		log.debug("=============================");
		log.debug("Executing verifyScrollbarPresence Keyword");

		int listSize = 0;
		WebElement scrollbar,relatedVideos = null ;
		String relatedVideosText = "default";
		boolean flag = false;

		try {
			try {
				List<WebElement> elements = driver.findElements(By.xpath(OR.getProperty(objectArr[0])));
				listSize = elements.size();
				log.debug("listsize " + listSize);
				relatedVideos = driver.findElement(By.xpath("//article[@class='related_videos box']/h1"));
				relatedVideosText = relatedVideos.getText();
				if(listSize==0) {
					scrollbar = driver.findElement(By.xpath(OR.getProperty(objectArr[1])));
					Functions.dragTo(driver, scrollbar, 5);
					if(relatedVideosText.equals("Related Videos") || relatedVideos.equals(null))
						return "Fail";
				}
			}catch(Throwable t) {
				if(relatedVideosText.equals("Related Videos"))
					return "Fail";
				else
					return "Pass";

			}

			scrollbar = driver.findElement(By.xpath(OR.getProperty(objectArr[1])));

			if(listSize > 3) {
				if(scrollbar.isDisplayed())
					flag = true;
			}else {
				if(!scrollbar.isDisplayed())
					flag = true;
			}

			if(flag)
				return "Pass";
			else
				return "Fail";

		}catch(Throwable t) {
			log.debug("Error while executing verifyScrollbarPresence " +  t.getMessage());
			return "Fail";
		}

	}


	public String workSpace_ViewMoreLenses() {
		log.debug("=============================");
		log.debug("workSpace_ViewMoreLenses");
		List<String> lensesChecked=new ArrayList<String>();
		List<String> lensesLanding=new ArrayList<String>();
		List<String> lensesOverlay=new ArrayList<String>();
		List<WebElement> unCheckedElements = new ArrayList<WebElement>();

		int totalCheckedCount=0;
		int totalLensesShown=0;
		boolean result = false;
		boolean changeAction=false;
		boolean intialCountingOver=false;
		int failCount=0;
		String asStringCheckedNames=null;
		try {

			String whatToDoString=objectArr[0];
			int whatToDo=0;
			if(whatToDoString.equalsIgnoreCase("aims.checkTitles") ){
				whatToDo=1;
			}
			else if(whatToDoString.equalsIgnoreCase("aims.checkCount") ){
				whatToDo=2;
			}
			else if(whatToDoString.equalsIgnoreCase("aims.saveChange") ){
				whatToDo=3;
				changeAction=true;
			}
			else if(whatToDoString.equalsIgnoreCase("aims.cancelChange") ){
				whatToDo=4;
				changeAction=true;
			}

			log.debug("what to do is   :" + whatToDoString );
			List<WebElement> lensesOnOverlay=null;
			List<WebElement> lensesOnLanding=null;


			lensesOnOverlay = driver.findElements(By.xpath("//*[@id='lens_settings_overlay']/descendant::div[@class='overview']/ul/li"));


			System.out.println(lensesOnOverlay.size());

			for(int i=1; i<=lensesOnOverlay.size(); i++){
				String value = null;
				WebElement e = driver.findElement(By.xpath("//*[@id='lens_settings_overlay']/descendant::div[@class='overview']/ul/li["+i+"]"));
				if(e.getAttribute("class").equals("ng-scope checked")){
					if(i==lensesOnOverlay.size()){
						intialCountingOver=true;
					}
					if(changeAction){
						if(whatToDo==3 ){
							e.click();
							Thread.sleep(2000);
							driver.findElement(By.xpath(OR.getProperty("aims.ViewMoreLenseSave"))).click();  // click Save button
							Thread.sleep(2000);
							driver.findElement(By.xpath(OR.getProperty("aims.Landing.ViewMoreLensesButton"))).click(); 
							Thread.sleep(2000);

							lensesOnOverlay = driver.findElements(By.xpath("//*[@id='lens_settings_overlay']/descendant::div[@class='overview']/ul/li"));

							changeAction=false;
							i=0;
							continue;
						}
						if(whatToDo==4){
							e.click();
							Thread.sleep(2000);
							driver.findElement(By.xpath(OR.getProperty("aims.ViewMoreLenseCancel"))).click();  // click Save button
							Thread.sleep(2000);
							driver.findElement(By.xpath(OR.getProperty("aims.Landing.ViewMoreLensesButton"))).click(); 
							Thread.sleep(2000);

							lensesOnOverlay = driver.findElements(By.xpath("//*[@id='lens_settings_overlay']/descendant::div[@class='overview']/ul/li"));

							changeAction=false;
							i=0;
							continue;
						}
					}

					value = driver.findElement(By.xpath("//*[@id='lens_settings_overlay']/descendant::div[@class='overview']/ul/li["+i+"]")).getText();
					lensesOverlay.add(value);



					log.debug("lense checked on overlay : " + i + " : " +value);
					if(! value.isEmpty())
						lensesChecked.add(value);
					totalCheckedCount++;

				}
				else{
					if(i==lensesOnOverlay.size()){
						unCheckedElements.add(e);
						intialCountingOver=true;
					}
					else
						unCheckedElements.add(e);
				}
			}

			if(intialCountingOver){

				//asStringCheckedNames = lensesChecked.toString();
				log.debug(asStringCheckedNames);
				log.debug("lense checked count : " + totalCheckedCount);


				driver.findElement(By.xpath(OR.getProperty("aims.ViewMoreLenseClose"))).click();  // close the overlay
				Thread.sleep(2000);

				lensesOnLanding = driver.findElements(By.xpath("//*[@id='lenses']/descendant::ul[contains(@class, 'lenses')]/li"));
				log.debug("landing page lenses count : " + lensesOnLanding.size());
				for(int i=1; i<=lensesOnLanding.size(); i++){

					WebElement e = driver.findElement(By.xpath("//*[@id='lenses']/descendant::ul[contains(@class, 'lenses')]/li["+i+"]"));
					if(! e.getAttribute("class").contains("inactive")) {
						String value = driver.findElement(By.xpath("//*[@id='lenses']/descendant::ul[contains(@class, 'lenses')]/li["+i+"]/h4")).getAttribute("innerHTML");
						log.debug("lense landing : " + i + " : " +value);
						if(! value.isEmpty())
							lensesLanding.add(value);
						totalLensesShown++;
					}
				}


			}

			log.debug("lense shown count : " + totalLensesShown);


			switch(whatToDo){
			case 1://verify lenses titles to that selected/shown in overlay

				//System.out.println("inside case 1 ");
				log.debug("inside case 1 ");


				for(int i=0; i<lensesLanding.size(); i++){
					String temp = lensesLanding.get(i).toString().trim();
					String temp2 = lensesOverlay.get(i).toString().replaceAll(".", "").trim();
					if(! temp.contains(temp2)){
						log.debug("lense : " + temp +"  does not contain " + temp2);
						result=false;
						break;
					}
					else
						result=true;
				}
				break;


			case 2://verify count of lenses shown with that selected/shown in overlay
				System.out.println("inside case 2 ");
				log.debug("inside case 2 ");

				if(totalCheckedCount==totalLensesShown)
					result=true;
				else{
					//System.out.println("totalCheckedCount : " + totalCheckedCount + " " + " totalLensesShown: " + totalLensesShown);
					log.debug("totalCheckedCount : " + totalCheckedCount + " " + " totalLensesShown: " + totalLensesShown);
					result=false;
				}
				break;


			case 3: case 4://verify count and titles of lenses shown with that selected/shown in overlay
				//System.out.println("inside case 3 ");
				log.debug("inside case 3 ");
				for(int i=0; i<lensesLanding.size(); i++){
					String temp = lensesLanding.get(i).toString().trim();
					String temp2 = lensesOverlay.get(i).toString().replaceAll(".", "").trim();
					if(! temp.contains(temp2)){
						log.debug("lense : " + temp +"  does not contain " + temp2);
						failCount++;
						break;
					}
				}
				if(! (totalCheckedCount==totalLensesShown)){
					//System.out.println("totalCheckedCount : " + totalCheckedCount + " " + " totalLensesShown: " + totalLensesShown);
					log.debug("totalCheckedCount : " + totalCheckedCount + " " + " totalLensesShown: " + totalLensesShown);
					failCount++;

				}
				if(failCount>0)
					result=false;
				else
					result=true;
				break;


			}



			if(result==true)
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing workSpace_ViewMoreLenses -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}


	public String workSpace_ViewMoreLenses_Portfolio() {
		log.debug("=============================");
		log.debug("workSpace_ViewMoreLenses_Portfolio");
		List<String> lensesChecked=new ArrayList<String>();
		List<String> lensesLanding=new ArrayList<String>();
		List<String> lensesOverlay=new ArrayList<String>();
		List<WebElement> unCheckedElements = new ArrayList<WebElement>();

		int totalCheckedCount=0;
		int totalLensesShown=0;
		boolean result = false;
		boolean changeAction=false;
		boolean intialCountingOver=false;
		int failCount=0;
		String asStringCheckedNames=null;
		try {

			String whatToDoString=objectArr[0];
			int whatToDo=0;
			if(whatToDoString.equalsIgnoreCase("aims.checkTitlesPortfolio")){
				whatToDo=1;
			}
			else if( whatToDoString.equalsIgnoreCase("aims.checkCountPortfolio")){
				whatToDo=2;
			}
			else if(whatToDoString.equalsIgnoreCase("aims.saveChangePortfolio")){
				whatToDo=3;
				changeAction=true;
			}
			else if( whatToDoString.equalsIgnoreCase("aims.cancelChangePortfolio")){
				whatToDo=4;
				changeAction=true;
			}

			log.debug("what to do is   :" + whatToDoString );
			List<WebElement> lensesOnOverlay=null;
			List<WebElement> lensesOnLanding=null;

			lensesOnOverlay = driver.findElements(By.xpath("//*[@id='lens_settings_overlay']/descendant::div[@class='overview']/ul/li"));

			System.out.println(lensesOnOverlay.size());

			for(int i=1; i<=lensesOnOverlay.size(); i++){
				String value = null;
				WebElement e = driver.findElement(By.xpath("//*[@id='lens_settings_overlay']/descendant::div[@class='overview']/ul/li["+i+"]"));
				if(e.getAttribute("class").contains("checked")){
					if(i==lensesOnOverlay.size()){
						intialCountingOver=true;
					}
					if(changeAction){
						if(whatToDo==3 ){
							e.click();
							Thread.sleep(2000);
							driver.findElement(By.xpath(OR.getProperty("aims.Portfolio.ViewMoreLenseSave"))).click();  // click Save button
							Thread.sleep(2000);
							driver.findElement(By.xpath(OR.getProperty("aims.Landing.ViewMoreLensesButton"))).click(); 
							Thread.sleep(2000);
							lensesOnOverlay = driver.findElements(By.xpath("//*[@id='lens_settings_overlay']/descendant::div[@class='overview']/ul/li"));
							changeAction=false;
							i=0;
							continue;
						}
						if(whatToDo==4){
							e.click();
							Thread.sleep(2000);
							driver.findElement(By.xpath(OR.getProperty("aims.Portfolio.ViewMoreLenseCancel"))).click();  // click Save button
							Thread.sleep(2000);
							driver.findElement(By.xpath(OR.getProperty("aims.Landing.ViewMoreLensesButton"))).click(); 
							Thread.sleep(2000);
							lensesOnOverlay = driver.findElements(By.xpath("//*[@id='lens_settings_overlay']/descendant::div[@class='overview']/ul/li"));
							changeAction=false;
							i=0;
							continue;
						}
					}
					value = driver.findElement(By.xpath("//*[@id='lens_settings_overlay']/descendant::div[@class='overview']/ul/li["+i+"]")).getText();
					lensesOverlay.add(value);

					log.debug("lense checked on overlay : " + i + " : " +value);
					if(! value.isEmpty())
						lensesChecked.add(value);
					totalCheckedCount++;

				}
				else{
					if(i==lensesOnOverlay.size()){
						unCheckedElements.add(e);
						intialCountingOver=true;
					}
					else
						unCheckedElements.add(e);
				}
			}

			if(intialCountingOver){

				//asStringCheckedNames = lensesChecked.toString();
				log.debug(asStringCheckedNames);
				log.debug("lense checked count : " + totalCheckedCount);


				driver.findElement(By.xpath(OR.getProperty("aims.Portfolio.ViewMoreLenseClose"))).click();  // close the overlay
				Thread.sleep(2000);

				lensesOnLanding = driver.findElements(By.xpath("//*[@id='lenses']/descendant::ul[contains(@class, 'lenses')]/li"));
				log.debug("landing page lenses count : " + lensesOnLanding.size());
				for(int i=1; i<=lensesOnLanding.size(); i++){

					WebElement e = driver.findElement(By.xpath("//*[@id='lenses']/descendant::ul[contains(@class, 'lenses')]/li["+i+"]"));
					if(! e.getAttribute("class").contains("ng-hide")) {
						String value = driver.findElement(By.xpath("//*[@id='lenses']/descendant::ul[contains(@class, 'lenses')]/li["+i+"]/h4")).getAttribute("innerHTML");
						log.debug("lense landing : " + i + " : " +value);
						if(! value.isEmpty())
							lensesLanding.add(value);
						totalLensesShown++;
					}
				}


			}

			log.debug("lense shown count : " + totalLensesShown);


			switch(whatToDo){
			case 1://verify lenses titles to that selected/shown in overlay

				//System.out.println("inside case 1 ");
				log.debug("inside case 1 ");


				for(int i=0; i<lensesLanding.size(); i++){
					String temp = lensesLanding.get(i).toString().trim();
					String temp2 = lensesOverlay.get(i).toString().replaceAll(".", "").trim();
					if(! temp.contains(temp2)){
						log.debug("lense : " + temp +"  does not contain " + temp2);
						result=false;
						break;
					}
					else
						result=true;
				}
				break;


			case 2://verify count of lenses shown with that selected/shown in overlay
				System.out.println("inside case 2 ");
				log.debug("inside case 2 ");

				if(totalCheckedCount==totalLensesShown)
					result=true;
				else{
					//System.out.println("totalCheckedCount : " + totalCheckedCount + " " + " totalLensesShown: " + totalLensesShown);
					log.debug("totalCheckedCount : " + totalCheckedCount + " " + " totalLensesShown: " + totalLensesShown);
					result=false;
				}
				break;


			case 3: case 4://verify count and titles of lenses shown with that selected/shown in overlay
				//System.out.println("inside case 3 ");
				log.debug("inside case 3 ");
				for(int i=0; i<lensesLanding.size(); i++){
					String temp = lensesLanding.get(i).toString().trim();
					String temp2 = lensesOverlay.get(i).toString().replaceAll(".", "").trim();
					if(! temp.contains(temp2)){
						log.debug("lense : " + temp +"  does not contain " + temp2);
						failCount++;
						break;
					}
				}
				if(! (totalCheckedCount==totalLensesShown)){
					//System.out.println("totalCheckedCount : " + totalCheckedCount + " " + " totalLensesShown: " + totalLensesShown);
					log.debug("totalCheckedCount : " + totalCheckedCount + " " + " totalLensesShown: " + totalLensesShown);
					failCount++;

				}
				if(failCount>0)
					result=false;
				else
					result=true;
				break;


			}



			if(result==true)
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing workSpace_ViewMoreLenses_Portfolio -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}



	public String workSpace_ViewMoreLenses_SelectMaxLenses() {
		log.debug("=============================");
		log.debug("workSpace_ViewMoreLenses_SelectMaxLenses");

		List<WebElement> unCheckedElements = new ArrayList<WebElement>();

		int totalCheckedCount=0;

		boolean result = false;

		boolean intialCountingOver=false;
		String maxSelectActualMessage=null;
		String maxSelectExpectedMessage=null;

		try {
			maxSelectExpectedMessage=APPTEXT.getProperty("aims.maxSelectMessage");
			String whatToDoString=objectArr[0];
			int whatToDo=0;
			List<WebElement> lensesOnOverlay=null;
			String messageXpath=null;
			String saveButtonXpath=null;
			String elementString=null;

			if(whatToDoString.equalsIgnoreCase("aims.checkSix")){
				whatToDo=1;
				messageXpath="aims.maxSelectMessage";
				saveButtonXpath="aims.ViewMoreLenseSave";
				lensesOnOverlay = driver.findElements(By.xpath("//*[@id='lens_settings_overlay']/descendant::div[@class='overview']/ul/li"));
				elementString = "//*[@id='lens_settings_overlay']/descendant::div[@class='overview']/ul/li";
			}
			else if(whatToDoString.equalsIgnoreCase("aims.checkEight")){
				whatToDo=2;
				messageXpath="aims.Portfolio.maxSelectMessage";
				saveButtonXpath="aims.Portfolio.ViewMoreLenseSave";
				lensesOnOverlay = driver.findElements(By.xpath("//*[@id='lens_settings_overlay']/descendant::div[@class='overview']/ul/li"));
				elementString = "//*[@id='lens_settings_overlay']/descendant::div[@class='overview']/ul/li";
			}


			log.debug("what to do is   :" + whatToDoString );
			log.debug("expected : " + maxSelectExpectedMessage);
			log.debug(lensesOnOverlay.size());

			for(int i=1; i<=lensesOnOverlay.size(); i++){

				WebElement e = driver.findElement(By.xpath(elementString+"["+i+"]"));
				if(e.getAttribute("class").contains("checked")){

					totalCheckedCount++;
					if(i==lensesOnOverlay.size()){
						intialCountingOver=true;
					}

				}

				else{

					if(i==lensesOnOverlay.size()){
						unCheckedElements.add(e);
						intialCountingOver=true;
					}
					else
						unCheckedElements.add(e);
				}
			}
			log.debug("count" + totalCheckedCount);
			log.debug(intialCountingOver );

			if(intialCountingOver && whatToDo==1){
				if(totalCheckedCount<6){
					for(int k=1; totalCheckedCount<6; k++,totalCheckedCount++){
						unCheckedElements.get(k).click();
						Thread.sleep(2000);
						if(totalCheckedCount==5)
							maxSelectActualMessage=driver.findElement(By.xpath(OR.getProperty(messageXpath))).getText();

					}
				}
				else
					maxSelectActualMessage=driver.findElement(By.xpath(OR.getProperty(messageXpath))).getText();

			}
			if(intialCountingOver && whatToDo==2){

				if(totalCheckedCount<8){
					for(int k=1; totalCheckedCount<8; k++,totalCheckedCount++){
						unCheckedElements.get(k).click();
						Thread.sleep(2000);
						if(totalCheckedCount==7)
							maxSelectActualMessage=driver.findElement(By.xpath(OR.getProperty(messageXpath))).getText();

					}
				}
				else
					maxSelectActualMessage=driver.findElement(By.xpath(OR.getProperty(messageXpath))).getText();
			}




			switch(whatToDo){

			case 1: case 2:

				//System.out.println("inside case 1 and 2 ");
				log.debug("inside case 1 and 2 ");
				if(maxSelectExpectedMessage.equalsIgnoreCase(maxSelectActualMessage)){
					//System.out.println("expected : " +maxSelectExpectedMessage + "   actual : " +maxSelectActualMessage );
					log.debug("expected : " +maxSelectExpectedMessage + "   actual : " +maxSelectActualMessage);
					result=true;
				}
				else{
					//System.out.println("expected : " +maxSelectExpectedMessage + "   actual : " +maxSelectActualMessage );
					log.debug("expected : " +maxSelectExpectedMessage + "   actual : " +maxSelectActualMessage);
					result=false;
				}

				driver.findElement(By.xpath(OR.getProperty(saveButtonXpath))).click();  // click Save button
				Thread.sleep(WAIT3SEC);

				break;

			}



			if(result==true)
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			// report error
			driver.findElement(By.xpath(OR.getProperty("aims.ViewMoreLenseSave"))).click();  // click Save button
			log.debug("Error while executing workSpace_ViewMoreLenses -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}


	public String workspace_VerifyCarouselSlides() {
		log.debug("=============================");
		log.debug("Executing workspace_VerifyCarouselSlides");
		int expectedSlidesCount =0;
		int whatToDo=0;
		int shownSlidesCount=0;	
		boolean result=false;
		try {	
			String whatToDoString=objectArr[0];

			if(whatToDoString.equalsIgnoreCase("aims.any")){
				whatToDo=1;
			}
			else{
				expectedSlidesCount = Integer.parseInt(objectArr[0].substring(5));
				whatToDo=2;
			}

			List<WebElement> carouselSlides = driver.findElements(By.xpath("//*[@id='icarousel']/div"));
			System.out.println(carouselSlides.size());
			for(int i=1; i<=carouselSlides.size(); i++){
				WebElement slideElement = driver.findElement(By.xpath("//*[@id='icarousel']/div["+i+"]"));
				if(slideElement.getAttribute("class").contains("current") || slideElement.getAttribute("class").contains("leftSlide") 
						|| slideElement.getAttribute("class").contains("rightSlide")){
					shownSlidesCount++;
				}

			}

			switch(whatToDo){
			case 1:
				log.debug("inside case 1 : any ");
				if(shownSlidesCount==3 || shownSlidesCount==5){
					log.debug("number of slides shown : " +shownSlidesCount );
					result = true;
				}
				else{
					log.debug("number of slides shown : " +shownSlidesCount );
					result = false;
				}
				break;

			case 2:
				log.debug("inside case 2 :  ");
				if(shownSlidesCount==expectedSlidesCount){
					log.debug("number of slides expected : " +expectedSlidesCount );
					log.debug("number of slides shown : " +shownSlidesCount );
					result = true;
				}
				else{
					log.debug("number of slides expected : " +expectedSlidesCount );
					log.debug("number of slides shown : " +shownSlidesCount );
					result = false;
				}
				break;
			}

			if(result)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing workspace_VerifyCarouselSlides -" + objectArr[0]+ t.getMessage());
			return "Fail - Link Not Found";
		}

	}


	public String workSpace_CompareLenses() {
		log.debug("=============================");
		log.debug("workSpace_CompareLenses");
		List<String> lensesOverview=new ArrayList<String>();
		List<String> lensesPortfolio=new ArrayList<String>();


		int totalLensesOverview=0;
		int totalLensesPortfolio=0;
		int failCount=0;
		String asStringPortfolioLenses=null;
		try {


			List<WebElement> lensesOnPortfolio = driver.findElements(By.xpath("//*[@id='lenses']/descendant::ul[@class='lenses clearfix']/li"));
			//System.out.println("portfolio page lenses count : " +lensesOnPortfolio.size());

			for(int i=1; i<=lensesOnPortfolio.size(); i++){

				WebElement e = driver.findElement(By.xpath("//*[@id='lenses']/descendant::ul[@class='lenses clearfix']/li["+i+"]"));
				if(! e.getAttribute("class").contains("inactive")) {
					String value = driver.findElement(By.xpath("//*[@id='lenses']/descendant::ul[@class='lenses clearfix']/li["+i+"]/h4")).getAttribute("innerHTML");
					//System.out.println("Portfolio landing : " + i + " : " +value);
					log.debug("Portfolio page selection : " + i + " : " +value);
					if(! value.isEmpty())
						lensesPortfolio.add(value);
					totalLensesPortfolio++;
				}
			}

			asStringPortfolioLenses = lensesPortfolio.toString();
			log.debug(asStringPortfolioLenses);
			log.debug("Portfolio lenses selected  count : " + totalLensesPortfolio);


			driver.findElement(By.xpath(OR.getProperty("aims.workspace.overview.xpath"))).click();  // navigate to Overview page
			Thread.sleep(5000);

			List<WebElement> lensesOnOverview = driver.findElements(By.xpath("//*[@id='lenses']/descendant::ul[@class='lenses']/li"));
			//System.out.println("landing page lenses count : " + lensesOnOverview.size());
			for(int i=1; i<=lensesOnOverview.size(); i++){

				WebElement e = driver.findElement(By.xpath("//*[@id='lenses']/descendant::ul[@class='lenses']/li["+i+"]"));
				if(! e.getAttribute("class").contains("inactive")) {
					String value = driver.findElement(By.xpath("//*[@id='lenses']/descendant::ul[@class='lenses']/li["+i+"]/h4")).getAttribute("innerHTML");
					log.debug("Overview page selection : " + i + " : " +value);
					if(! value.isEmpty())
						lensesOverview.add(value);
					totalLensesOverview++;
				}
			}


			System.out.println("Overview lenses selected  count : " + totalLensesOverview);

			for(int i=0; i<lensesOverview.size(); i++){
				String temp = lensesOverview.get(i).toString().trim();
				if(! asStringPortfolioLenses.contains(temp)){
					//System.out.println("lense : " + temp +"  is not shown in the Portfolio Page");
					log.debug("lense : " + temp +"  is not shown in the Portfolio Page");
					failCount++;
					break;
				}

			}
			if(totalLensesOverview > totalLensesPortfolio)
				failCount++;


			if(failCount>0){
				log.debug("lenses selected in portfolio page : " + totalLensesPortfolio);
				log.debug("lenses selected in overview page : " + totalLensesOverview);
				log.debug("fail count is : " + failCount);
				return "Fail";
			}
			else
				return "Pass";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing workSpace_CompareLenses -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String verifyHistory_backword(){
		log.debug("=============================");
		log.debug("Executing verifyHistory_backward Keyword");
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		try{
			String backElementVerify = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			String backElementVerify2 = testData.getCellData(currentTest, data_column_nameArr[2], testRepeat);
			String expectedURL = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);

			if(expectedURL.isEmpty() && expectedURL.equals(null))
				return "Fail- Value not present";		
			flag1 = Functions.verifyElementPresenceOrAbsence(driver, log, objectArr[0],backElementVerify2);
			log.debug("1.Flag value at click:" + flag1);
			driver.navigate().back();
			log.debug("Clicked on browser back");
			Thread.sleep(WAIT5SEC);
			//verify presence or not present	
			flag2 = Functions.verifyElementPresenceOrAbsence(driver, log, objectArr[0], backElementVerify);
			log.debug("2.Flag value at browser back:" + flag2);
			if(!flag2) {
				log.debug("Expected flag value at browser back is true");
				return "Fail";
			}
			String actualURL = driver.getCurrentUrl();
			log.debug("Actual URL:"+actualURL);
			log.debug("Expected URL:"+expectedURL);
			if(!actualURL.equals(expectedURL))
				flag3=true;
		}catch(Throwable t){
			log.debug("Error while executing verifyHistory_backward keyword - Object: "+ objectArr[0] + "\n Stacktrace: \n"+t.getMessage());
			return "Fail- Debug Required in catch";
		}
		log.debug("Flag1: "+flag1+"\n Flag2: "+flag2+"\n Flag3: "+flag3 );
		if(flag2 && flag1 && flag3) {
			return "Pass";
		}
		else {
			return "Fail";
		}
	}

	public String verifyHistory_forward(){
		log.debug("=============================");
		log.debug("Executing verifyHistory_forward Keyword");
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		try{
			String fowardElementVerify = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			String fowardElementVerify2 = testData.getCellData(currentTest, data_column_nameArr[2], testRepeat);
			String expectedURL = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);

			if(expectedURL.isEmpty() && expectedURL.equals(null))
				return "Fail- Value not present";
			flag1 = Functions.verifyElementPresenceOrAbsence(driver, log, objectArr[0],fowardElementVerify2);
			log.debug("1.Flag value at click:" + flag1);

			driver.navigate().forward();
			log.debug("Clicked on browser forward");
			Thread.sleep(WAIT8SEC);
			//verify presence or not present
			flag2 = Functions.verifyElementPresenceOrAbsence(driver, log, objectArr[0],fowardElementVerify);
			log.debug("3. Flag value at forward :" + flag2);
			//verify flag
			if(!flag2) {
				log.debug("Expected flag value at browser forward is false");
				return "Fail";
			}
			String actualURL = driver.getCurrentUrl();
			log.debug("Actual URL:"+actualURL);
			log.debug("Expected URL:"+expectedURL);
			if(actualURL.equals(expectedURL))
				flag3=true;
		}catch(Throwable t){
			log.debug("Error while executing verifyHistory_forward keyword - Object: "+ objectArr[0] + "\n Stacktrace: \n"+t.getMessage());
			return "Fail- Debug Required in catch";
		}
		log.debug("Flag1: "+flag1+"\n Flag2: "+flag2+"\n Flag3: "+flag3 );
		if(flag2 && flag1 && flag3) {
			return "Pass";
		}
		else {
			return "Fail";
		}
	}
	public String verifyHistory_refresh(){
		log.debug("=============================");
		log.debug("Executing verifyHistory_refresh Keyword");
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		try{
			String refreshElementVerify = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			String refreshElementVerify2 = testData.getCellData(currentTest, data_column_nameArr[2], testRepeat);
			String expectedURL = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
			if(expectedURL.isEmpty() && expectedURL.equals(null))
				return "Fail- Value not present";
			flag1 = Functions.verifyElementPresenceOrAbsence(driver, log, objectArr[0],refreshElementVerify2);
			log.debug("1.Flag value at click:" + flag1);

			driver.navigate().refresh();
			Thread.sleep(WAIT5SEC);
			//verify presence or not present
			flag2 = Functions.verifyElementPresenceOrAbsence(driver, log, objectArr[0],refreshElementVerify);
			log.debug("4. Flag value at refresh:" + flag2);
			//verify flag
			if(!flag2) {
				log.debug("Expected flag value at refresh is true");
				return "Fail";
			}
			String actualURL = driver.getCurrentUrl();
			log.debug("Actual URL:"+actualURL);
			log.debug("Expected URL:"+expectedURL);
			if(actualURL.equals(expectedURL))
				flag3=true;
		}catch(Throwable t){
			log.debug("Error while executing verifyHistory_refresh keyword - Object: "+ objectArr[0] + "\n Stacktrace: \n"+t.getMessage());
			return "Fail- Debug Required in catch";
		}
		log.debug("Flag1: "+flag1+"\n Flag2: "+flag2+"\n Flag3: "+flag3 );
		if(flag2 && flag1 && flag3) {
			return "Pass";
		}
		else {
			return "Fail";
		}
	}
	public String verifyHistory_bookmark(){
		log.debug("=============================");
		log.debug("Executing verifyHistory_bookmark Keyword");
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		String URL;
		try{
			String bookmarkElementVerify = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			String bookmarkElementVerify2 = testData.getCellData(currentTest, data_column_nameArr[2], testRepeat);
			String expectedURL = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);

			if(expectedURL.isEmpty() && expectedURL.equals(null))
				return "Fail- Value not present";
			Thread.sleep(WAIT5SEC);
			flag1 = Functions.verifyElementPresenceOrAbsence(driver, log, objectArr[0],bookmarkElementVerify2);
			log.debug("1.Flag value at click:" + flag1);
			URL = driver.getCurrentUrl();
			driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).click();
			Thread.sleep(WAIT5SEC);
			//Goto homepage
			driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).click();
			Thread.sleep(WAIT5SEC);
			//Navigate to the URL
			driver.navigate().to(URL);
			Thread.sleep(WAIT8SEC);
			//verify presence or not present
			Thread.sleep(WAIT5SEC);
			flag2 = Functions.verifyElementPresenceOrAbsence(driver, log, objectArr[0],bookmarkElementVerify);
			log.debug("4. Flag value at bookmark:" + flag2);
			String actualURL = driver.getCurrentUrl();
			log.debug("Actual URL:"+actualURL);
			log.debug("Expected URL:"+expectedURL);
			//close overlay
			if(objectArr[1]!=null){
				driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).click();
				Thread.sleep(WAIT5SEC);
				log.debug("Overlay closed.");
			}
			else
				log.debug("Third Object is empty");
			if(!flag2) {
				log.debug("Expected flag value at bookmark is true");
				return "Fail";
			}	
			if(actualURL.equals(expectedURL))
				flag3=true;
		}catch(Throwable t){
			log.debug("Error while executing verifyHistory_bookmark keyword - Object: "+ objectArr[0] + "\n Stacktrace: \n"+t.getMessage());
			return "Fail- Debug Required in catch";
		}
		log.debug("Flag1: "+flag1+"\n Flag2: "+flag2+"\n Flag3: "+flag3 );
		if(flag2 && flag1 && flag3) {
			return "Pass";
		}
		else {
			return "Fail";
		}
	}

	public String verifyHistory_url(){
		log.debug("=============================");
		log.debug("Executing verifyHistory_url Keyword");
		String flag;
		String actualURL;
		String expectedURL;
		try{
			expectedURL = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			if(expectedURL.isEmpty() && expectedURL.equals(null))
				return "Fail- Value not present";
			flag = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
			log.debug("Expected URL: "+expectedURL);
			log.debug("Input Flag: "+flag);
			actualURL = driver.getCurrentUrl();
			log.debug("Actual URL: "+actualURL);
			if(expectedURL.equals(actualURL) && flag.equalsIgnoreCase("TRUE"))
				return "Pass";
			else if(!expectedURL.equals(actualURL) && flag.equalsIgnoreCase("FALSE"))
				return "Pass";
			else
				return "Fail";
		}catch(Throwable t){
			log.debug("Error while executing verifyHistory_url keyword - Object: "+ objectArr[0] + "\n Stacktrace: \n"+t.getMessage());
			return "Fail- Debug Required in catch";
		}
	}
	public String setURL(){
		log.debug("====================================");
		log.debug("Executing setURL");
		try{
			String data = driver.getCurrentUrl();
			log.debug("URL: "+data);
			testData.setCellData(currentTest, data_column_nameArr[0], 2, data);
			return "Pass";
		}catch(Throwable t){
			log.debug("Error while executing setURL keyword - \n Stacktrace: \n"+t.getMessage());
			return "Fail- Debug Required in catch";
		}
	}

	public String verifymaxitemcountinpseudoplaylist(){ 
		try{
			log.debug("=============================");
			log.debug(" Executing verifymaxitemcountinpseudoplaylist");
			boolean flag = false;
			String maximunCountInString = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			int maximumCount = Integer.parseInt(maximunCountInString);
			System.out.println("Count"+maximumCount);
			try {
				int numofplst = driver.findElements(By.xpath(OR.getProperty(objectArr[0]))).size();
				System.out.println("Presentlt the number of playlist is " + numofplst);
				log.debug("Presentlt the number of playlist is " + numofplst);

				if (numofplst<=maximumCount)
					flag = true;
				else
					flag = false;
			}
			catch(Throwable t){
				log.debug("Error in verifymaxitemcountinpseudoplaylist");
				log.debug(t.getMessage());
				return "Fail";
			}
			if	(flag==true) 		
				return "Pass";
			else
				return "Fail";
		}catch(Exception e){
			//e.printStackTrace();
		}
		return "Fail";
	}


	public String verifyMinItemCount(){ 
		log.debug("=============================");
		log.debug(" Executing verifyMinItemCount");
		boolean flag = false;
		String maximunCountInString = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
		int maximumCount = Integer.parseInt(maximunCountInString);
		System.out.println("Count"+maximumCount);
		try {
			int numofplst = driver.findElements(By.xpath(OR.getProperty(objectArr[0]))).size();
			System.out.println("Presentlt the number of playlist is " + numofplst);
			log.debug("Presentlt the number of playlist is " + numofplst);

			if (numofplst>=maximumCount)
				flag = true;
			else
				flag = false;
		}
		catch(Throwable t){
			log.debug("Error in verifyMinItemCount");
			log.debug(t.getMessage());
			return "Fail";
		}
		if	(flag==true) 		
			return "Pass";
		else
			return "Fail";
	}


	public String verifyVideosUnderRelatedTab(){
		log.debug("=============================");
		log.debug("Executing verifyVideosUnderRelatedTab");

		try{
			int countOfVideos = driver.findElements(By.xpath(OR.getProperty(objectArr[0]))).size();
			if(countOfVideos<=4)
				return "Pass";
			else
				return "Fail";

		}catch(Throwable t){
			log.debug("Error while executing verifyVideosUnderRelatedTab");
			log.debug(t.getMessage());
			return "Fail";
		}


	}

	public String verifyCurrentVideoWithRelatedVideos(){
		log.debug("=============================");
		log.debug("Executing verifyCurrentVideoWithRelatedVideos");

		try{
			String titleTextCurrentVideo = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			log.debug("title of current video :"+titleTextCurrentVideo);

			List<WebElement> allRelatedVideos = driver.findElements(By.xpath(OR.getProperty(objectArr[1])));

			Boolean flag=true;

			for (WebElement element : allRelatedVideos) {
				String titleTextRelatedVideo = element.getText();
				log.debug("title of related video :" + titleTextRelatedVideo);
				if(titleTextCurrentVideo.equalsIgnoreCase(titleTextRelatedVideo)){
					flag=false;
					break;
				}
				else{
					flag=true;
				}
			}
			if(flag==true)
				return "Pass";
			else
				return "Fail";
		}catch(Throwable t){
			log.debug("Error while executing verifyCurrentVideoWithRelatedVideos");
			log.debug(t.getMessage());
			return "Fail";
		}
	}

	public String hoverOverVideoOverlay(){
		log.debug("=============================");
		log.debug("Executing hoverOverVideoOverlay");
		try{	
			Actions actions = new Actions(driver);

			WebElement menuHoverLink = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));

			actions.moveToElement(menuHoverLink);

			actions.perform();
			menuHoverLink.click();

			return "Pass";
		}catch(Throwable t){
			log.debug("Error while executing hoverOverVideoOverlay");
			log.debug(t.getMessage());
			return "Fail";
		}
	}


	public String verifyFiveSubsectionsOfTop5(){
		log.debug("=============================");
		log.debug("Executing verifyFiveSubsectionsOfTop5");
		boolean result=true;
		try{
			for(int i=0;i<3;i++){
				int subHeadings = driver.findElements(By.xpath(OR.getProperty(objectArr[0]))).size();
				if(subHeadings==1){
					int listElements = driver.findElements(By.xpath("//div[@class='outer_wrapper']/div[@style='display: block;']/ul/li")).size();
					if(listElements==5){
						result=true;
					}
					else{
						result=false;
						break;
					}
				}
				else{
					int firstListElements= driver.findElements(By.xpath("//div[@class='outer_wrapper']/div[@style='display: block;']/ul[1]/li")).size();
					int secondListElements= driver.findElements(By.xpath("//div[@class='outer_wrapper']/div[@style='display: block;']/ul[2]/li")).size();
					if(firstListElements==5 && secondListElements==5){
						result=true;
					}
					else{
						result=false;
						break;
					}
				}
				Thread.sleep(4000);
			}
			if(result==true)
				return "Pass";
			else
				return "Fail";
		}catch(Throwable t){
			log.debug("Error while executing verifyFiveSubsectionsOfTop5");
			log.debug(t.getMessage());
			return "Fail";
		}
	}
	public String maxTwoSubSectionsDisplayedUnderTop5(){
		log.debug("=============================");
		log.debug("Executing maxTwoSubSectionsDisplayed");
		boolean result=false;
		try{
			for(int i=0;i<3;i++){
				int subHeadings = driver.findElements(By.xpath(OR.getProperty(objectArr[0]))).size();
				if(subHeadings<=2){
					result=true;
				}
				else{
					result=false;
					break;
				}
				Thread.sleep(2000);
			}
			if(result)
				return "Pass";
			else
				return "Fail";
		}catch(Throwable t){
			log.debug("Error while executing maxTwoSubSectionsDisplayed" + objectArr[0] + t.getMessage());
			log.debug(t.getMessage());
			return "Fail";
		}
	}

	public String verifyVideoLengthDecrease(){
		log.debug("=============================");
		log.debug("Executing verifyVideoLengthDecrease");
		try{
			Thread.sleep(WAIT5SEC);
			
			Actions actions = new Actions(driver);

			WebElement menuHoverLink = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));

			actions.moveToElement(menuHoverLink);
			actions.perform();
			menuHoverLink.click();

			WebElement we = driver.findElement(By.xpath(OR.getProperty(objectArr[1])));
			String initialPixelValue = we.getCssValue("width");
			log.debug("initialPixelValue: "+initialPixelValue);
			System.out.println("initialPixelValue: "+initialPixelValue);
			double initialValue = Functions.pixelValDouble(initialPixelValue);

			log.debug("initial value :"+initialValue);
			System.out.println("initial value :"+initialValue);

			Thread.sleep(3000);

			actions.moveToElement(menuHoverLink);
			actions.perform();
			menuHoverLink.click();

			String finalPixelValue = we.getCssValue("width");
			double finalValue = Functions.pixelValDouble(finalPixelValue);
			log.debug("final value :"+finalValue);

			if(finalValue > initialValue)
				return "Pass";
			else
				return "Fail";

		}catch(Throwable t){
			log.debug("Error while executing verifyVideoLengthDecrease");
			log.debug(t.getMessage());
			return "Fail";
		}
	}

	public String Verifyfieldpresense(){
		log.debug("=============================");
		log.debug("Verifyfieldpresense");
		try{	
			int numElements1 = getWebElements(OR, objectArr[0]).size();
			if(numElements1 == 1)
				return "Pass";
			else
				return "Fail-field not present";
		}catch(Throwable t){
			log.debug("Error while executing hoverOverVideoOverlay");
			log.debug(t.getMessage());
			return "Fail";
		}
	}

	public String verifyPauseVideoAction(){
		log.debug("=============================");
		log.debug("Executing verifyPauseVideoAction");
		Boolean flag1=false;
		Boolean flag2=false;
		try{
			WebElement menuHoverLink = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));

			String whatToDo=objectArr[2];
			Actions actions = new Actions(driver);
			if(whatToDo.equalsIgnoreCase("aims.required")) {



				actions.moveToElement(menuHoverLink);
				actions.perform();
				menuHoverLink.click();


				String initialPixelValue = driver.findElement(By.xpath("//*[@class='vjs-play-progress']")).getCssValue("width");
				double initialValue=Functions.pixelValDouble(initialPixelValue);
				log.debug("Initial Value:"+initialValue);

				Thread.sleep(WAIT3SEC);


				actions.moveToElement(menuHoverLink);
				actions.perform();
				menuHoverLink.click();

				String finalPixelValue = driver.findElement(By.xpath("//*[@class='vjs-play-progress']")).getCssValue("width");
				double finalValue=Functions.pixelValDouble(finalPixelValue);
				log.debug("Final Value:"+finalValue);

				if(finalValue==initialValue)
					flag1=true;


				Thread.sleep(WAIT3SEC);

				actions.moveToElement(menuHoverLink);
				actions.perform();
				menuHoverLink.click();
				String initPixelValue= driver.findElement(By.xpath("//*[@class='vjs-play-progress']")).getCssValue("width");
				double initValue=Functions.pixelValDouble(initPixelValue);
				log.debug("Init Value:"+initValue);

				driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).click();	

				Thread.sleep(WAIT3SEC);

				actions.moveToElement(menuHoverLink);
				actions.perform();
				menuHoverLink.click();

				String finPixelValue =  driver.findElement(By.xpath("//*[@class='vjs-play-progress']")).getCssValue("width");
				double finValue=Functions.pixelValDouble(finPixelValue);
				log.debug("Final Length"+finValue);

				if(initValue==initialValue && finValue>initValue)
					flag2=true;

				log.debug("Flag1: "+flag1);
				log.debug("Flag2: "+flag2);

				if(flag1 && flag2)
					return "Pass";
				else 
					return "Fail";
			}
			else {
				actions.moveToElement(menuHoverLink);
				actions.perform();
				menuHoverLink.click();

				driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).click();	

				return "Pass";
			}
		}catch(Throwable t){
			log.debug("Error while executing verifyPauseVideoAction");
			log.debug(t.getMessage());
			return "Fail";
		}
	}

	public String verifyCountOfCarouselsInPerspectiveSection(){
		log.debug("=============================");
		log.debug("Executing verifyCountOfCarouselsInPerspectiveSection");
		try{
			int countOfCarousels = driver.findElements(By.xpath(OR.getProperty(objectArr[0]))).size();
			if(countOfCarousels<=6)
				return "Pass";
			else
				return "Fail";
		}catch(Throwable t){
			log.debug("Error while executing verifyCountOfCarouselsInPerspectiveSection");
			log.debug(t.getMessage());
			return "Fail";
		}
	}

	public String verifyRotationOfArticlesInCarousel(){
		log.debug("=============================");
		log.debug("Executing verifyRotationOfArticlesInCarousel");
		try{
			//the xpath of active slide is used		
			WebElement firstArticle = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			String firstArticlePath =firstArticle.getAttribute("src");
			// a wait of 5 seconds is given
			Thread.sleep(5000);
			WebElement secondArticle = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			String secondArticlePath = secondArticle.getAttribute("src");

			if(!(firstArticlePath.equals(secondArticlePath)))
				return "Pass";		
			else
				return "Fail";
		}catch(Throwable t){
			log.debug("Error while executing verifyRotationOfArticlesInCarousel");
			log.debug(t.getMessage());
			return "Fail";
		}
	}

	public String openPdfUnderMostRead(){
		log.debug("=============================");
		log.debug("Executing openPdfUnderMostRead");
		boolean result=false;
		try{
			for (int i=0;i<3;i++){
				String firstHeading = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
				if(firstHeading.equals("Most Read")){
					Thread.sleep(1000);
					driver.findElement(By.xpath("//div[@class='outer_wrapper']/div[@style='display: block;']/ul[1]/li[3]/a")).click();
					result=true;
					break;
				}else{
					Thread.sleep(2000);
				}
			}
			if(result==true)
				return "Pass";
			else
				return "Fail";
		}catch(Throwable t){
			log.debug("Error while executing openPdfUnderMostRead"+objectArr[0]+t.getMessage());
			log.debug(t.getMessage());
			return "Fail";

		}
	}

	public String openVideoPdfUnderNewVideos(){
		log.debug("=============================");
		log.debug("Executing openVideoPdfUnderNewVideos");
		boolean result=false;
		try{
			//mouse hover on top5 section--
			/*Actions actions = new Actions(driver);
			WebElement menuHoverLink = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));

			actions.moveToElement(menuHoverLink);
	        actions.perform();
	        Thread.sleep(4000);

	        WebElement logoHoverLink = driver.findElement(By.xpath("//*[@id='main-header']/div[1]/div/a/img"));
	        actions.moveToElement(logoHoverLink);
	        actions.perform();
	        Thread.sleep(4000);*/
			//--
			for (int i=0;i<6;i++){
				String firstHeading = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
				if(firstHeading.equals("New Videos")){
					Thread.sleep(1000);
					driver.findElement(By.xpath("//h2[text()='New Videos']/parent::div/descendant::a")).click();
					Thread.sleep(2000);
					result=true;
					break;
				}else{
					Thread.sleep(2000);
				}
			}
			if(result==true)
				return "Pass";
			else
				return "Fail";
		}catch(Throwable t){
			log.debug("Error while executing openVideoPdfUnderNewVideos");
			log.debug(t.getMessage());
			return "Fail";

		}
	}

	public String openPlaylistUnderMostRead(){
		log.debug("=============================");
		log.debug("Executing openPlaylistUnderMostRead");
		boolean result=false;
		try{
			for (int i=0;i<3;i++){
				String firstHeading = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
				if(firstHeading.equals("Most Read")){
					driver.findElement(By.xpath("//div[@class='outer_wrapper']/div[@style='display: block;']/ul[1]/li[1]/a")).click();
					result=true;
					break;
				}else{
					Thread.sleep(2000);
				}
			}
			if(result==true)
				return "Pass";
			else
				return "Fail";
		}catch(Throwable t){
			log.debug("Error while executing openPlaylistUnderMostRead");
			log.debug(t.getMessage());
			return "Fail";

		}
	}

	public String compareContactStrings() {
		log.debug("=======================================");
		log.debug("Executing compareContactStrings Keyword");
		// the keyword checks fot the special characters in contact message.
		try {
			String value1, value2, getValFromTestData = null;

			try {
				getValFromTestData = testData.getCellData(currentTest,data_column_nameArr[1], testRepeat);
			} catch (Throwable e) {

			}

			if (getValFromTestData.equals("default"))
				value1 = driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).getText();
			else
				value1 = testData.getCellData(currentTest,data_column_nameArr[0], testRepeat);

			log.debug("Before replacing value1 is :" + value1);

			boolean value = value1.matches(".*[\\`\\~\\#\\^\\=\\{\\}\\[\\]\\<\\>\\\\].*");

			if (value) {
				value1 = Functions.replaceAll(value1,"[\\`\\~\\#\\^\\=\\{\\}\\[\\]\\<\\>\\\\]", "");
			}

			WebElement we = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));

			value2 = we.getAttribute("value");
			if (value1.equals(value2)) {
				log.debug("value1 is :" + value1);
				log.debug("value2 is :" + value2);
				return "Pass";
			} else {
				log.debug("value1 is :" + value1);
				log.debug("value2 is :" + value2);
				return "Fail";
			}

		} catch (Throwable t) {
			log.debug("error while executing compareContactStrings keyword "
					+ objectArr[0] + t.getMessage());
			return "Fail";

		}
	}

	public String compareContactMessage() {
		log.debug("=======================================");
		log.debug("Executing compareContactMessage Keyword");
		// the keyword compares the value of text "ThankYou..." after contact submits any text
		try {
			String value1, value2;

			driver.findElement(By.xpath(OR.getProperty("aims.contactUs.submitLink.xpath"))).click();
			Thread.sleep(WAIT1SEC);

			value1 = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);

			WebElement we = driver.findElement(By.xpath("//textarea[@id='innerText']"));

			value2 = we.getAttribute("value");

			if (value1.equals(value2)) {
				log.debug("Value1 is :" + value1);
				log.debug("Value2 is :" + value2);
				return "Pass";
			} else {
				log.debug("Value1 is :" + value1);
				log.debug("Value2 is :" + value2);
				return "Fail";
			}

		} catch (Throwable t) {
			log.debug("error while executing compareContactMessage keyword "
					+ objectArr[0] + t.getMessage());
			return "Fail";
		}
	}

	public String insightsClickCarousal() {
		log.debug("=============================");
		log.debug("Executing InsightsclickCarousal");
		try {
			//Functions.highlighter(driver,
			//		driver.findElement(By.xpath(OR.getProperty(objectArr[0]))));
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(OR.getProperty(objectArr[0]))));
			driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();

			// Handling Data Unavailable Pop Up for Specific Dev and QA
			// Environment pages
			Functions.handleDataUnavailablePopUp(driver, log, CONFIG);

		} catch (Throwable t) {
			// report error
			log.debug("Error while clicking on link -" + objectArr[0]
					+ t.getMessage());

			return "Fail - Link Not Found";
		}
		return "Pass";


	}


	public String insightsdefaultSortByDate() {
		log.debug("=============================");
		log.debug("Executing insightsdefaultSortByDate");

		List<String> date = new ArrayList<String>();

		boolean result = true;

		try {
			driver.findElement(By.xpath("//div[@class='fl view-btns clearfix']/descendant::a[@title='List view']")).click();
			Thread.sleep(WAIT5SEC);
	List<WebElement> dates = driver.findElements(By.xpath("//div[@class='date dsp-cell']"));
			
			for (WebElement e : dates){
				// if(e.getAttribute("class").equalsIgnoreCase("date")){
				//if (e.getAttribute("class").equalsIgnoreCase("date dsp-cell")) {
					String value = e.getAttribute("innerHTML");
					System.out.println("date : " + value);
					log.debug("date : " + value);
					if (!value.isEmpty())
						date.add(value);
				//}
			}
			// changing to grid view
			driver.findElement(By.xpath("//div[@class='fl view-btns clearfix']/descendant::a[@title='Grid view']")).click();
			Thread.sleep(WAIT5SEC);
			Date finalDates[] = new Date[date.size()];

			for (int d = 0; d < date.size(); d++) {
				// Date finalDates[]= new Date[date.size()];
				String s = date.get(d).toString();

				SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM dd yyyy");
				// inputFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
				SimpleDateFormat out = new SimpleDateFormat("MMMM dd yyyy");
				finalDates[d] = inputFormat.parse(s); // temp[1].replace(",","").trim()
				String output = out.format(finalDates[d]);
				log.debug("final date : " + output);
				System.out.println("final date : " + output);
			}

			outer: for (int k = 0; k < date.size(); k++) {
				for (int l = k + 1; l < date.size(); l++) {
					// System.out.println("dates  :" + finalDates[k] + "  "+
					// finalDates[l]);
					if (finalDates[k].before(finalDates[l])) {
						log.debug(finalDates[k] + "  falls before "+ finalDates[l]);
						result = false;
						break outer;
					}
				}
			}

			if (result == true)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing insightsdefaultSortByDate -"+ t.getMessage());
			return "Fail";
		}
	}

	public String insightsdefaultSortByMTD() {
		log.debug("=============================");
		log.debug("Executing insightsdefaultSortByMTD");

		List<String> MTD = new ArrayList<String>();
		List<WebElement> MTDs ;
		boolean result = true;
		Iterator<WebElement> i3;
		String ListFlag=null;

		try { 
			try{
				ListFlag = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			}catch(Throwable t){

				log.debug("Executing Performance View Default Sorting");
			}
			MTDs = driver.findElements(By.xpath("//span[@class='return ng-binding']"));
			i3 = MTDs.iterator();

			while (i3.hasNext()) {
				WebElement e = i3.next();					 
					String value = e.getAttribute("innerHTML");
					value = value.replaceAll("%", "");
					log.debug("MTD: " + value);
					if (!value.isEmpty())
						MTD.add(value);
				
			}


			Float finalMTD[] = new Float[MTD.size()];
			for (int d = 0; d < MTD.size(); d++) {
				Float s =Float.parseFloat(MTD.get(d).toString());
				finalMTD[d] = s; 

			}

			outer: for (int k = 0; k < MTD.size(); k++) {
				for (int l = k + 1; l < MTD.size(); l++) {

					if (finalMTD[k] < finalMTD[l]) {
						log.debug(finalMTD[k] + "  falls before "+ finalMTD[l]);
						result = false;
						break outer;
					}
				}
			}
			if (result)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing insightsdefaultSortByMTD -"+ t.getMessage());
			return "Fail";
		}
	}

	public String insightsdefaultSortByMTDListView() {
		log.debug("=============================");
		log.debug("Executing insightsdefaultSortByMTDListView");

		List<String> MTD = new ArrayList<String>();
		List<WebElement> MTD1s = new ArrayList<WebElement>();
		boolean result = true;
		Iterator<WebElement> i4;

		try {     	  	       
			MTD1s = driver.findElements(By.xpath("//td[@class='name ng-binding']"));
			i4 = MTD1s.iterator();

			while (i4.hasNext()) {
				WebElement e = i4.next();
					String value = e.getAttribute("innerHTML");
					log.debug("MTD: " + value);
					if (!value.isEmpty())
						MTD.add(value);
				
			}

			driver.findElement(By.xpath("//*[contains(@class,' list')]")).click();
			MTD1s = driver.findElements(By.xpath("//td[@class='name ng-binding']"));
			i4 = MTD1s.iterator();
			int i=0;
			while (i4.hasNext()) {
				i++;
				log.debug("element number"+ i);
				WebElement e = i4.next();
				// if(e.getAttribute("class").equalsIgnoreCase("date")){
				
					String value = e.getAttribute("innerHTML");
					log.debug("MTD: " + value);
					if (!value.equals(MTD.get(i))){
						result = true;
						break;
					}
			}

			if (result == true)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing insightsdefaultSortByMTD -"+ t.getMessage());
			return "Fail";
		}
	}
	public String checkNoOfLenses(){
		log.debug("=============================");
		log.debug("Executing checkNoOfLenses");
		// the keyword counts the number of lenses in "View More Lenses and make them 6

		boolean result = false;
		try {

			driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();	
			List<WebElement> selectedList = driver.findElements(By.xpath("//*[@id='lens_settings_overlay']/descendant::li[@class='ng-scope checked']"));

			int size = selectedList.size();
			log.debug("currently number of lenses checked is" + size);
			if(size==6) {
				result= true;
			}
			else if(size<6) {
				List<WebElement> unSelectedList = driver.findElements(By.xpath("//*[@id='lens_settings_overlay']/descendant::li[@class='ng-scope disabled']"));
				for(int j=1;j<=6-size;j++){
					unSelectedList.get(j).click();
				}
				result = true;
			}

			else if(size>6) {
				for(int j=1;j<=size-6;j++){
					selectedList.get(j).click();
				}
				result = true;
			}
			if (result == true)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Executing checkNoOfLenses" + t.getMessage());
			return "Fail";
		}
	}
	public String checkNavItemsNotSelected(){
		log.debug("=============================");
		log.debug("Executing checkNavItemsNotSelected");
		boolean flag=true;
		List<WebElement> ulList = null;
		try {
			ulList = driver.findElements(By.xpath(OR.getProperty(objectArr[0])));
		}catch(Throwable t){
		}
		try{		
			log.debug("List Size: "+ulList.size());
			for (WebElement liElement : ulList) {
				String className=liElement.getAttribute("class");

				log.debug("l1 Element: "+liElement+" Class Name: "+className );

				if(className.contains("active")){
					flag=false;
					break;
				}
				else
					flag=true;
			}
			if(flag)
				return "Pass";
			else
				return "Fail";
		}catch (Throwable t) {
			// report error
			log.debug("Error while executing checkNavItemsNotSelected for object " + objectArr[0] + "\n StackTrace: \n"+t.getMessage());
			return "Fail";
		}
	}
	public String verifyIsDisabled(){
		log.debug("=============================");
		log.debug("Executing verifyIsDisabled");
		try{
			String check=driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getCssValue("display");
			log.debug("Display: "+check);
			if(check.equalsIgnoreCase("none"))
				return "Pass";
			else
				return "Fail";
		}catch(Throwable t){
			log.debug("Error while executing verifyIsDisabled for object " + objectArr[0] + "\n StackTrace: \n"+t.getMessage());
			return "Fail";
		}

	}

	public String verifyScrollDrag(){
		log.debug("====================================");
		log.debug("Executing verifyScrollDrag");
		// the keyword drags the scroll bar up and down 
		try{
			WebElement element = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			Actions actions = new Actions(driver);
			int pixelsToClick = 30;
			actions.dragAndDropBy(element, 0, pixelsToClick).perform();
			int pixelsToClick1 = -30;
			actions.dragAndDropBy(element, 0, pixelsToClick1).perform();
			return "Pass";
		}catch(Throwable t){
			log.debug("Error while executing verifyScrollDrag"+t.getMessage());
			return "Fail";
		}
	}



	public String isDisplayed(){
		log.debug("====================================");
		log.debug("Executing isDisplayed");
		// the keyword checks whether an element is displayed or not
		boolean result=false;
		String expected = null;
		try {
			try {
				expected = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
				log.debug("Expected:" + expected);
			}catch(Throwable r) {
				log.debug("Test Data Column is not present in controller sheet .Expected variable value :"+ expected);
				return "Fail- Debug Required";
			}

			if(expected.equals(null)|| expected.isEmpty()) {
				log.debug("Test Data value is blank. Expoected variable value :"+ expected);
				return "Fail- Debug Required";
			}

			try{
				result = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).isDisplayed();
				log.debug("Result: "+ result);
			}catch(Throwable e){
				result = false;
			}

			if (!result) {
				if (expected.equalsIgnoreCase("true"))
					return "Fail -" + " Element not present";
				else
					return "Pass";
			}
			else{
				if (expected.equalsIgnoreCase("true")) 
					return "Pass";
				else
					return "Fail -" + " Element should not be present";
			}
		}catch (Throwable t) {
			log.debug("Error while executing isDisplayed for object "+ objectArr[0]+"\n StackTrace: \n"+ t.getMessage());
			return "Fail";
		}
	}

	public String verifymanagerSearchResultsCount(){
		log.debug("====================================");
		log.debug("Executing verifymanagerSearchResultsCount");
		// the keyword verifies count of search results by dragging
		String rowsLocator = OR.getProperty(objectArr[0]);
		String dragThumbLocator = OR.getProperty(objectArr[1]);
		String totalRowCount = driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).getText();
		String totalRowCountTrimed = totalRowCount.trim();
		int totalRowInt = Integer.parseInt(totalRowCountTrimed);
		log.debug("total results is" + totalRowInt);
		boolean result = false;
		try
		{
			Functions.dragTillAllRowsLoaded(driver, log, rowsLocator, dragThumbLocator, totalRowInt, 100);
			List<WebElement> fundsList = driver.findElements(By.xpath(OR.getProperty(objectArr[0])));
			int listSize = fundsList.size();
			log.debug("list size after scrolling is : " + fundsList.size());

			if(totalRowInt==listSize)
				result=true;

			if(result==true)
				return "Pass";
			else {
				log.debug("count is not correct");
				return "Fail";}

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing verifymanagerSearchResultsCount-" + t.getMessage());
			return "Fail";
		}
	}
	public String compareInputData(){
		log.debug("====================================");
		log.debug("Executing compareInputData");
		try{
			String actualValue = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getAttribute("value");
			String expectedValue = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);

			if(actualValue.equals(expectedValue))
				return "Pass";
			else
				return "Fail";

		}catch(Throwable t){
			log.debug("Error in compareInputData- "+t.getMessage());
			return "Fail";
		}
	}



	public String verifyIsEnabled(){
		log.debug("=============================");
		log.debug("Executing verifyIsEnabled");
		try{
			String check=driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getCssValue("display");
			log.debug("Display: "+check);
			if(check.equalsIgnoreCase("block"))
				return "Pass";
			else
				return "Fail";
		}catch(Throwable t){
			log.debug("Error in verifyIsDisabled -- " + objectArr[0]);
			return "Fail";
		}

	}

	public String scrollDrag(){
		log.debug("====================================");
		log.debug("Executing scrollDrag");
		// the keyword drags the scroll bar up and down 

		try{
			WebElement element = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			Actions actions = new Actions(driver);
			String data = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			double doubleData = Double.parseDouble(data);
			int pixelsToClick = (int) doubleData;
			log.debug("Pixels to move:" + pixelsToClick);
			actions.dragAndDropBy(element, 0, pixelsToClick).perform();
			return "Pass";
		}catch(Throwable t){
			log.debug("Error in dragging scroll bar "+t.getMessage());
			return "Fail";
		}
	}

	public String setData(){
		log.debug("====================================");
		log.debug("Executing setData");
		// the keyword drags the scroll bar up and down 

		try{
			WebElement element = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			String data = element.getText();
			log.debug("Data: "+data);
			testData.setCellData(currentTest, data_column_nameArr[0], 2, data);
			return "Pass";
		}catch(Throwable t){
			log.debug("Error in setData "+t.getMessage());
			return "Fail";
		}
	}

	public String verifyDataEquals(){
		log.debug("====================================");
		log.debug("Executing verifyDataEquals");

		try{
			WebElement element = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));	
			String actualData = element.getText();
			log.debug("Actual Data: "+actualData);
			String data1=Functions.replaceAll(actualData,"[a-zA-Z \\( \\)]", "");
			log.debug("Actual Data: "+data1);
			int actualIntData = Integer.parseInt(data1.trim());
			log.debug("Actual Integer Data: "+actualIntData);
			String expectedData = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			log.debug("Expected Data: "+expectedData);
			String data2=Functions.replaceAll(expectedData,"[a-zA-Z \\( \\)]", "");
			log.debug("Expected Data: "+data2);
			int expectedIntData = Integer.parseInt(data2.trim());
			log.debug("Actual Integer Data: "+expectedIntData);	
			if(expectedIntData == actualIntData)
				return "Pass";
			else
				return "Fail";
		}catch(Throwable t){
			log.debug("Error while executing verifyDataEquals for object "+objectArr[0]+"\n Stack Trace: \n"+t.getMessage());
			return "Fail";
		}
	}

	public String verifyCount(){
		log.debug("====================================");
		log.debug("Executing verifyCount");
		try{

			String flag=testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
			String rowsLocator = OR.getProperty(objectArr[0]);
			String dragThumbLocator = OR.getProperty(objectArr[1]);
			String actualData = driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).getText();
			String data1=Functions.replaceAll(actualData,"[a-zA-Z \\( \\)]", "");
			log.debug("Expected Data: "+data1);
			System.out.println("Expected Data: "+data1);
			int actualIntData = Integer.parseInt(data1.trim());
			log.debug("Actual Integer Data: "+actualIntData);
			System.out.println("Actual Integer Data: "+actualIntData);

			Functions.dragTillAllRowsLoadedWithWait(driver, log, rowsLocator, dragThumbLocator, actualIntData, 200);
			int count = driver.findElements(By.xpath(rowsLocator)).size();
			log.debug("Count after dragTillAllRowsLoadedWithWait: "+count);
			log.debug("Actual Integer Data: "+actualIntData);
			String expectedData = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			log.debug("Expected Data: "+expectedData);
			String data2=Functions.replaceAll(expectedData,"[a-zA-Z \\( \\)]", "");
			log.debug("Expected Data: "+data2);
			int expectedIntData = Integer.parseInt(data2.trim());
			log.debug("Actual Integer Data: "+expectedIntData);
			if(flag.equalsIgnoreCase("TRUE")){
				if(expectedIntData == actualIntData && count== actualIntData)
					return "Pass";
				else
					return "Fail";
			}
			else{
				if(expectedIntData != actualIntData && count== actualIntData)
					return "Pass";
				else
					return "Fail";
			}
		}catch(Throwable t){
			log.debug("Error while executing verifyCount \n Stack Trace: \n"+t.getMessage());
			return "Fail";
		}
	}


	public String verifySavedSearchCount(){
		log.debug("====================================");
		log.debug("Executing verifySavedSearchCount ");
		// the keyword verifies count of a saved search
		String searchName= testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
		boolean flag1= false;
		boolean flag2= false;
		log.debug("Search name is " + searchName);
		try{

			WebElement duplicateSearch = getWebElement(OR, "aims.managersFunds.sameSaveSearchNamePopUpText.xpath");
			boolean present = duplicateSearch.isDisplayed();
			if(present){
				if(duplicateSearch.getText().equals(APPTEXT.getProperty("aims.managersFunds.sameSaveSearchNamePopUpText.xpath"))) {
					getWebElement(OR, "aims.managersFunds.saveSearchCancel.xpath").click();
					Thread.sleep(WAIT2SEC);
					log.debug("Duplicate search exists. Test Case will fail.");
					return "Fail";
				}
			}

			String totalRowCount = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			String totalRowCountTrimed = totalRowCount.trim();
			int totalRowInt = Integer.parseInt(totalRowCountTrimed);
			log.debug("expected no of results is" + totalRowInt );
			System.out.println(totalRowInt);
			List<WebElement> rows =  driver.findElements(By.xpath("//div[@id='savesearch-wrap']/descendant::ul[@class='dk_options_inner']/descendant::li/a"));

			int rowSize = rows.size();
			log.debug("countRows" + rowSize);

			for (WebElement e1 : rows) {
				log.debug("Saved search is" + e1.getAttribute("title").trim());
				if (!(e1.getAttribute("title").trim().equalsIgnoreCase(searchName))&&(!(e1.getAttribute("title").trim().equalsIgnoreCase("Saved Searches"))))
				{
					e1.click();
					Thread.sleep(2000);
					break;
				}
			}

			driver.findElement(By.xpath("//*[@id='dk_container_saved-searches']/a")).click();
			Thread.sleep(2000);

			WebElement element = driver.findElement(By.xpath("//div[@id='savesearch-wrap']/descendant::div[@class='thumb']"));
			boolean result = driver.findElement(By.xpath("//div[@id='savesearch-wrap']/descendant::div[@class='thumb']")).isDisplayed();
			if(result)
			{
				Functions.dragAndFindSavedSearch(driver, element, searchName, rows, log);
			}
			else {
				for (WebElement e1 : rows) {
					if (e1.getAttribute("title").trim().equals(searchName)){
						e1.click();
						Thread.sleep(1000);
						break;
					}
				}
			}
			String totalRowCountActual = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			String totalRowCountTrimed1 = totalRowCountActual.trim();
			int totalRowIntActual = Integer.parseInt(totalRowCountTrimed1);
			log.debug("Actual count is" + totalRowIntActual);
			if(totalRowIntActual==totalRowInt)
				flag1=true;

			driver.findElement(By.xpath("//*[@id='dk_container_saved-searches']/a")).click();
			WebElement element1 = driver.findElement(By.xpath("//div[@id='savesearch-wrap']/descendant::div[@class='thumb']"));
			boolean result1 = driver.findElement(By.xpath("//div[@id='savesearch-wrap']/descendant::div[@class='thumb']")).isDisplayed();
			if(result1)
			{
				Functions.dragAndDeleteSavedSearch(driver, element1, searchName, rows, log);
				flag2=true;
			}
			else {
				for (WebElement e1 : rows) {
					if (e1.getAttribute("title").trim().equals(searchName)){
						WebElement webElement = driver.findElement(By.xpath("//span[text()='"+searchName+"']/ancestor::li/span[@class='icon-delete delete']"));
						if(webElement!=null) {
							webElement.click();
							Thread.sleep(2000);
							flag2=true;
							break;
						}
					}
				}
			}
			if(flag1 && flag2)
				return "Pass";
			else
				return "Fail";
		}catch (Throwable t) {
			// report error
			log.debug("Error while executing verifySavedSearchCount-" + t.getMessage());
			return "Fail";
		}
	}


public String setESTDate(){	
	log.debug("====================================");
	log.debug("Executing setESTDate");
	try{
		Date currentDate = new Date();
		log.debug("Current Date: " + currentDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.MINUTE, -630);
		Date EST = cal.getTime();
		
		SimpleDateFormat inputFormat = new SimpleDateFormat("dd");
		log.debug("Day: "+inputFormat.format(EST));
		testData.setCellData(currentTest, data_column_nameArr[0], 2,inputFormat.format(EST));
		
		inputFormat = new SimpleDateFormat("MMM");
		log.debug("Month: "+inputFormat.format(EST));
		testData.setCellData(currentTest, data_column_nameArr[1], 2, inputFormat.format(EST));
		
		inputFormat = new SimpleDateFormat("yyyy");
		log.debug("Month: "+inputFormat.format(EST));
		testData.setCellData(currentTest, data_column_nameArr[2], 2, inputFormat.format(EST));
		
		
	}catch(Throwable t){
		log.debug("Error in setESTDate "+t.getMessage());
		return "Fail";
	}
	return "Pass";
}

	public String setCurrentDate(){	
		log.debug("====================================");
		log.debug("Executing setCurrentDate ");
		try{
			Date currentDate = new Date();
			log.debug("Current Date: " + currentDate);

			SimpleDateFormat inputFormat = new SimpleDateFormat("dd");
			log.debug("Day: "+inputFormat.format(currentDate));
			testData.setCellData(currentTest, data_column_nameArr[0], 2,inputFormat.format(currentDate));

			inputFormat = new SimpleDateFormat("MMM");
			log.debug("Month: "+inputFormat.format(currentDate));
			testData.setCellData(currentTest, data_column_nameArr[1], 2, inputFormat.format(currentDate));

			inputFormat = new SimpleDateFormat("yyyy");
			log.debug("Month: "+inputFormat.format(currentDate));
			testData.setCellData(currentTest, data_column_nameArr[2], 2, inputFormat.format(currentDate));

		}catch(Throwable t){
			log.debug("Error in setCurrentDate "+t.getMessage());
			return "Fail";
		}
		return "Pass";	
	}
	public String setFutureDate(){	
		log.debug("====================================");
		log.debug("Executing setFutureDate ");
		try{
			Date currentDate = new Date();
			log.debug("Current Date: " + currentDate);
			currentDate.setDate(currentDate.getDate()+1);
			log.debug("Future Date: " + currentDate);
			SimpleDateFormat inputFormat = new SimpleDateFormat("dd");
			log.debug("Day: "+inputFormat.format(currentDate));
			testData.setCellData(currentTest, data_column_nameArr[0], 2,inputFormat.format(currentDate));

			inputFormat = new SimpleDateFormat("MMM");
			log.debug("Month: "+inputFormat.format(currentDate));
			testData.setCellData(currentTest, data_column_nameArr[1], 2, inputFormat.format(currentDate));

			inputFormat = new SimpleDateFormat("yyyy");
			log.debug("Month: "+inputFormat.format(currentDate));
			testData.setCellData(currentTest, data_column_nameArr[2], 2, inputFormat.format(currentDate));

		}catch(Throwable t){
			log.debug("Error in setFutureDate "+t.getMessage());
			return "Fail";
		}
		return "Pass";	
	}

	public String searchFiltersCurrentDateChecker(){
		log.debug("=============================");
		log.debug("Executing searchFiltersCurrentDateChecker");
		try {		
			String dayInputString =  testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			int dayInput = Integer.parseInt(dayInputString);
			String monthInput = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
			String yearInput = testData.getCellData(currentTest, data_column_nameArr[2],testRepeat);
			log.debug("Day : "+ dayInput+"Month : "+ monthInput+"Year : "+ yearInput);
			/*WebElement dateIcon = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
		dateIcon.click();
		Thread.sleep(WAIT2SEC);
		Functions.selectDateFromCalender(driver, dayInput, monthInput, yearInput, log);
		Thread.sleep(WAIT2SEC);*/
			WebElement we = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			String fromDateString = we.getAttribute("value");
			log.debug("From Date Picked(before) : "+fromDateString);
			System.out.println("From Date Picked(before) : "+fromDateString);
			fromDateString = fromDateString.replaceAll(",", "");
			System.out.println("From Date Picked(after): "+fromDateString);
			log.debug("From Date Picked(after): "+fromDateString);
			String requiredFromdate = monthInput+" "+ dayInputString+" "+yearInput;
			if(fromDateString.trim().equals(requiredFromdate.trim()))
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing  searchFiltersCurrentDateChecker- "+ t.getMessage());
			return "Fail";
		}
	}

	public String searchFiltersFutureDateChecker(){
		log.debug("=============================");
		log.debug("Executing searchFiltersFutureDateChecker");
		boolean result = false;
		try{
			String dayInputString =  testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			int dayInput = Integer.parseInt(dayInputString);
			String monthInput = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
			String yearInput = testData.getCellData(currentTest, data_column_nameArr[2],testRepeat);
			log.debug("Day : "+ dayInput);
			log.debug("Month : "+ monthInput);
			log.debug("Year : "+ yearInput);

			WebElement dateIcon = null;

			//System.out.println("inside from date");
			dateIcon = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			dateIcon.click();
			Thread.sleep(WAIT2SEC);

			//pick year as per input from data sheet
			WebElement yearPicker = driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxYearPicker")));
			yearPicker = driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxYearPicker")));
			yearPicker.click();
			Thread.sleep(WAIT1SEC);
			Select year = new Select(yearPicker);
			year.selectByVisibleText(yearInput);
			yearPicker = driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxYearPicker")));
			yearPicker.sendKeys(Keys.ENTER);
			Thread.sleep(WAIT2SEC);

			//pick month as per input from data sheet
			WebElement monthPicker = driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxMonthPicker")));
			monthPicker.click();
			Thread.sleep(WAIT1SEC);
			Boolean result1 = false;
			List<WebElement> months = getWebElements(OR, objectArr[1]);
			for(WebElement mon : months) {
				if(!monthInput.equalsIgnoreCase(mon.getText()))
					result1 = true;
			}
			if(!result1){
				Select month = new Select(monthPicker);
				month.selectByVisibleText(monthInput);
				monthPicker = driver.findElement(By.xpath(OR.getProperty("aims.MyDropboxMonthPicker")));
				monthPicker.sendKeys(Keys.ENTER);
				Thread.sleep(WAIT2SEC);

				List<WebElement> days = driver.findElements(By.xpath("//table[@class='ui-datepicker-calendar']/descendant::td/*/parent::td"));
				for (WebElement day : days) {
					System.out.println("inside days");
					String actualDay = day.getText();
					log.debug("Actual Day(String): "+actualDay);
					int actualDayInt = Integer.parseInt(actualDay);
					log.debug("Actual Day(Int): "+actualDayInt);
					String className = day.getAttribute("class");
					System.out.println("Class Name: "+className);
					log.debug("Class Name: "+className);

					if((actualDayInt == dayInput) && className.contains("ui-state-disabled")){
						result=true;
						break;
					}
				}
				if(result){
					return "Pass";
				}
				else
					return "Fail";
			}else
				return "Pass";
		}catch(Throwable t){
			log.debug("Error while executing  searchFiltersFutureDateChecker: "+ t.getMessage());
			return "Fail";
		}
	}


	public String findSavedSearch(){
		log.debug("====================================");
		log.debug("Executing findSavedSearch ");
		// the keyword finds a saved search
		String searchName= testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
		boolean flag= false;
		log.debug(searchName);
		try{
			List<WebElement> rows =  driver.findElements(By.xpath("//div[@id='savesearch-wrap']/descendant::ul[@class='dk_options_inner']/descendant::li/a"));


			WebElement element = driver.findElement(By.xpath("//div[@id='savesearch-wrap']/descendant::div[@class='thumb']"));
			boolean result = driver.findElement(By.xpath("//div[@id='savesearch-wrap']/descendant::div[@class='thumb']")).isDisplayed();
			if(result)
			{
				Functions.dragAndFindSavedSearch(driver, element, searchName, rows, log);
				flag=true;
			}
			else {
				for (WebElement e1 : rows) {
					if (e1.getAttribute("title").trim().equals(searchName)){
						e1.click();
						Thread.sleep(1000);
						flag=true;
						log.debug("Found search is" + searchName );
					}
				}
			}
			if(flag)
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing findSavedSearch-" + t.getMessage());
			return "Fail";
		}
	}

	public String deleteSavedSearch(){
		log.debug("====================================");
		log.debug("Executing deleteSavedSearch ");
		// the keyword deletes a specified saved search
		String searchName= testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
		boolean flag= false;
		log.debug(searchName);
		try{
			List<WebElement> rows =  driver.findElements(By.xpath("//div[@id='savesearch-wrap']/descendant::ul[@class='dk_options_inner']/descendant::li/a"));

			WebElement element1 = driver.findElement(By.xpath("//div[@id='savesearch-wrap']/descendant::div[@class='thumb']"));
			boolean result1 = driver.findElement(By.xpath("//div[@id='savesearch-wrap']/descendant::div[@class='thumb']")).isDisplayed();
			if(result1)
			{
				Functions.dragAndDeleteSavedSearch(driver, element1, searchName, rows, log);
				flag=true;
			}
			else {
				for (WebElement e1 : rows) {
					if (e1.getAttribute("title").trim().equals(searchName)){
						WebElement webElement = driver.findElement(By.xpath("//span[text()='"+searchName+"']/ancestor::li/span[@class='icon-delete delete']"));
						if(webElement!=null) {
							webElement.click();
							Thread.sleep(1000);
							flag=true;
							log.debug("Deleted search is" + searchName);
							break;
						}
					}
				}
			}if(flag)
				return "Pass";
			else
				return "Fail";
		}catch (Throwable t) {
			// report error
			log.debug("Error while executing deleteSavedSearch-" + t.getMessage());
			return "Fail";
		}
	}

	public String verifyPresenceByResultCount(){
		log.debug("====================================");
		log.debug("Executing verifyPresenceByResultCount ");
		// the keyword verifies count of result is more than specified number 
		try
		{
			String totalRowCount = driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).getText();
			String totalRowCountTrimed = totalRowCount.trim();
			int totalRowInt = Integer.parseInt(totalRowCountTrimed);
			String expected = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
			int expectedTotalRowCount = Integer.parseInt(expected);
			String string = null;
			if(totalRowInt > expectedTotalRowCount){
				//using objectArr[0] and data_column_nameArr[0] in isDisplayed() keyword
				string = isDisplayed();
				if(string.equalsIgnoreCase("Pass"))
					return "Pass";
				else 
					return "Fail";
			}
			else
				return "Fail: Data not present";
		}catch (Throwable t) {
			// report error
			log.debug("Error while executing verifyPresenceByResultCount-" + t.getMessage());
			return "Fail";
		}
	}

	public String verifyAbsenceByResultCount(){
		log.debug("====================================");
		log.debug("Executing verifyAbsenceByResultCount ");
		// the keyword verifies count of result is more than nine 
		try
		{
			String totalRowCount = driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).getText();
			String data1=Functions.replaceAll(totalRowCount,"[a-zA-Z \\( \\)]", "");
			//String totalRowCountTrimed = data1.trim();
			int totalRowInt = Integer.parseInt(data1);
			log.debug("Actual: "+totalRowInt);
			String expected = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
			int expectedTotalRowCount = Integer.parseInt(expected);
			log.debug("Expected: "+expectedTotalRowCount);
			String string = null;
			if(totalRowInt <= expectedTotalRowCount){
				string = isDisplayed();
				if(string.equalsIgnoreCase("Pass"))
					return "Pass";
				else 
					return "Fail";
			}
			else
				return "Fail: Data not present";
		}catch (Throwable t) {
			// report error
			log.debug("Error while executing verifyAbsenceByResultCount-" + t.getMessage());
			return "Fail";
		}
	}

	public String selectDate(){
		log.debug("=============================");
		log.debug("Executing selectDate");

		try {
			String dayInputString =  testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			int dayInput = Integer.parseInt(dayInputString);
			String monthInput = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
			String yearInput = testData.getCellData(currentTest, data_column_nameArr[2],testRepeat);
			log.debug("Day : "+ dayInput);
			log.debug("Month : "+ monthInput);
			log.debug("Year : "+ yearInput);
			//System.out.println("inside from date");
			WebElement dateIcon = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			dateIcon.click();
			Thread.sleep(WAIT2SEC);

			Functions.selectDateFromCalender(driver, dayInput, monthInput, yearInput, log);
			return "Pass";
		}catch(Throwable t){
			log.debug("Error while executing  selectDate: "+ t.getMessage());
			return "Fail";
		}
	}

	public String setPreviousDate(){

		log.debug("====================================");
		log.debug("Executing setPreviousDate ");
		try{
			Date currentDate = new Date();
			log.debug("Current Date: " + currentDate);
			currentDate.setDate(currentDate.getDate()-1);
			log.debug("Future Date: " + currentDate);
			SimpleDateFormat inputFormat = new SimpleDateFormat("dd");
			log.debug("Day: "+inputFormat.format(currentDate));
			testData.setCellData(currentTest, data_column_nameArr[0], 2,inputFormat.format(currentDate));

			inputFormat = new SimpleDateFormat("MMM");
			log.debug("Month: "+inputFormat.format(currentDate));
			testData.setCellData(currentTest, data_column_nameArr[1], 2, inputFormat.format(currentDate));

			inputFormat = new SimpleDateFormat("yyyy");
			log.debug("Month: "+inputFormat.format(currentDate));
			testData.setCellData(currentTest, data_column_nameArr[2], 2, inputFormat.format(currentDate));

		}catch(Throwable t){
			log.debug("Error in setPreviousDate "+t.getMessage());
			return "Fail";
		}
		return "Pass";	
	}

	public String verifyDateRange_SearchResults(){
		log.debug("====================================");
		log.debug("Executing verifyDateRange_SearchResults ");
		try{

			String dateRange = driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).getText();
			String toDate = driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).getAttribute("value");
			String fromDate = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getAttribute("value");
			log.debug("Date Range: "+dateRange);
			log.debug("To Date: "+toDate);
			log.debug("From Date: "+fromDate);
			String expectedDateRange = fromDate + " - " + toDate;	
			log.debug("Expected Date Range: "+expectedDateRange);
			System.out.println("Expected Date Range: "+expectedDateRange);
			System.out.println("Actual Date Range: "+dateRange);

			if(dateRange.equals(expectedDateRange))
				return "Pass";
			else
				return "Fail";
		}catch(Throwable t){
			log.debug("Error in verifyDateRange_SearchResults "+t.getMessage());
			return "Fail";
		}
	}

	public String verifyCountAfterClickingCross(){
		log.debug("====================================");
		log.debug("Executing verifyCountAfterClickingCross ");
		// the keyword verifies count of result before and after a filter criteria
		boolean flag1 = false;
		boolean flag2 = false;
		try
		{      
			String totalRowCount1 = driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).getText();
			String totalRowCountTrimed1 = totalRowCount1.trim();
			int totalRowInt1 = Integer.parseInt(totalRowCountTrimed1);
			log.debug("Expected no of results is: " + totalRowInt1);

			String data = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).sendKeys(data);
			Thread.sleep(2000);
			driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).click();	
			Thread.sleep(2000);
			String totalRowCount2 = driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).getText();
			String totalRowCountTrimed2 = totalRowCount2.trim();
			int totalRowInt2 = Integer.parseInt(totalRowCountTrimed2);
			log.debug("Number of results after filtering is: " + totalRowInt2);
			if(!(totalRowInt1==totalRowInt2))
				flag1 = true;
			try{
				driver.findElement(By.xpath(OR.getProperty(objectArr[3]))).click();	
				Thread.sleep(2000);
			} catch (Throwable t) {
				log.debug("Cross button not visible");
				return "Fail:cross button not visible";
			}

			String totalRowCount3 = driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).getText();
			String totalRowCountTrimed3 = totalRowCount3.trim();
			int totalRowInt3 = Integer.parseInt(totalRowCountTrimed3);
			log.debug("no of results after clicking cross button is: " + totalRowInt3);
			if(totalRowInt3==totalRowInt1)
				flag2 = true;

			if(flag1 && flag2)
				return "Pass";
			else
				return "Fail";
		}catch (Throwable t) {
			// report error
			log.debug("Error while executing verifyCountAfterClickingCross-" + t.getMessage());
			return "Fail";
		}
	}

	public String compareResultCount(){
		log.debug("====================================");
		log.debug("Executing compareResultCount");
		// the keyword compares two values, one from test data and other from OR
		try
		{      
			String totalRowCount1 = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			String totalRowCountTrimed1 = totalRowCount1.trim();
			int totalRowInt1 = Integer.parseInt(totalRowCountTrimed1);
			log.debug("actual no of results is: " + totalRowInt1);

			String expectedValue = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			int expectedValueInt = Integer.parseInt(expectedValue);
			log.debug("expected no of results is : " + expectedValueInt);

			if(totalRowInt1==expectedValueInt)
				return "Pass";
			else
				return "Fail";

		}catch(Throwable t){
			log.debug("Error in compareResultCount- "+t.getMessage());
			return "Fail";
		}
	}

	public String managersVerifyAllData() {
		log.debug("=============================");
		log.debug("Executing managersVerifyAllData");
		// the keyword verifies the result after selecting combination of Asset Class, Strategy, Geography, AUM/Fund Size
		boolean result1=true;

		try {
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.assetDropdown.xpath"))).click();
			Thread.sleep(WAIT2SEC);
			String assetOption = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty(assetOption))).click();
			Thread.sleep(WAIT2SEC);
			String strategyOption = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty(strategyOption))).click();
			Thread.sleep(WAIT2SEC);
			String geographyOption=testData.getCellData(currentTest, data_column_nameArr[2],testRepeat);
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty(geographyOption))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.aumMin.xpath"))).click();
			Thread.sleep(WAIT1SEC);
			String minObject=testData.getCellData(currentTest, "Min",testRepeat);
			Thread.sleep(WAIT2SEC);
			String minString=driver.findElement(By.xpath(OR.getProperty(minObject))).getText();
			driver.findElement(By.xpath(OR.getProperty(minObject))).click();
			Thread.sleep(WAIT2SEC);
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.aumMax.xpath"))).click();
			Thread.sleep(WAIT1SEC);
			String maxObject=testData.getCellData(currentTest, "Max",testRepeat);
			Thread.sleep(WAIT2SEC);
			String maxString=driver.findElement(By.xpath(OR.getProperty(maxObject))).getText();
			driver.findElement(By.xpath(OR.getProperty(maxObject))).click();
			Thread.sleep(WAIT2SEC);

			log.debug("range for AUM is" + minString + "to" + maxString);
			driver.findElement(By.xpath(OR.getProperty("aims.managersFunds.submitButton.xpath"))).click();
			Thread.sleep(WAIT2SEC);

			String rowsLocator = OR.getProperty(objectArr[0]);
			String dragThumbLocator = OR.getProperty(objectArr[1]);
			String totalRowCount = driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).getText();
			String totalRowCountTrimed = totalRowCount.trim();
			int totalRowInt = Integer.parseInt(totalRowCountTrimed);
			log.debug("total results is" + totalRowInt);
			boolean present =driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).isDisplayed();

			if(present){
				Functions.dragTillAllRowsLoaded(driver, log, rowsLocator, dragThumbLocator, totalRowInt, 100);
			}

			String assetOptionText=APPTEXT.getProperty(assetOption);
			log.debug("Asset: "+assetOptionText);

			List<WebElement> multiList;
			List<WebElement> rows;

			try {
				rows = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]"));
			}catch(Throwable t) {
				return "Fail-Data not present";
			}

			for(int i=1;i<=rows.size();i++) {			 
				log.debug("\nRow "+i);

				log.debug("Actual Asset: "+driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell ac-cell multi-check')]/span")).getAttribute("innerHTML"));

				if(driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell ac-cell multi-check')]/span")).getAttribute("innerHTML").equals("Multi")) {
					multiList = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell ac-cell multi-check')]/ul/li"));

					log.debug("Multilist Size "+ multiList.size());
					result1=false;

					for(int j=0;j<multiList.size();j++) {
						log.debug("List Element: "+multiList.get(j).getAttribute("innerHTML"));

						if(multiList.get(j).getAttribute("innerHTML").contains(assetOptionText)) {
							result1 = true;
							break;
						}
					}
				}else {
					if(!driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell ac-cell multi-check')]/span")).getAttribute("innerHTML").equals(assetOptionText)) {
						return "Fail";
					}
				}

				if(!result1) {
					return "Fail";
				}
			}

			boolean result2=true;
			String strategyOptionText=APPTEXT.getProperty(strategyOption);
			log.debug("Strategy: "+strategyOptionText);

			for(int i=1;i<=rows.size();i++) {			 
				log.debug("\nRow "+i);

				log.debug("Actual Strategy: "+driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell strat-cell multi-check')]/span")).getAttribute("innerHTML"));

				if(driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell strat-cell multi-check')]/span")).getAttribute("innerHTML").equals("Multi")) {
					multiList = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell strat-cell multi-check')]/ul/li"));

					log.debug("Multilist Size "+ multiList.size());
					result2=false;

					for(int j=0;j<multiList.size();j++) {
						log.debug("List Element: "+multiList.get(j).getAttribute("innerHTML"));

						if(multiList.get(j).getAttribute("innerHTML").contains(strategyOptionText)) {
							result2 = true;
							break;
						}
					}
				}else {
					if(!driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell strat-cell multi-check')]/span")).getAttribute("innerHTML").equals(strategyOptionText)) {
						return "Fail";
					}
				}

				if(!result2) {
					return "Fail";
				}
			}

			String[] Africa =   { "Africa" };
			String[] Americas = { "Latin Americas" };
			String[] Asia =     { "China", "Greater China", "India", "Indonesia", "Sri Lanka","Japan", "Korea", "Malaysia", "Philippines", "Singapore", "Saudi Arabia", "Micronesia" };        
			String[] EmergingMarkets =  { "Global Emerging Markets" };
			String[] Europe =   { "Germany " , "Russia", "Sweden", "Austria", "Portugal", "Switzerland", "Denmark", "Eastern Europe", "Italy", "Pan Europe" };
			String[] Global =   { "Pan Europe", "Latin Americas", "Africa", "Europe", "Melanesia", "Eastern Europe", "Micronesia", "Asia", "Continental Europe", "Germany", "Global", "Malaysia", "Northern Europe", "Polynesia", "Thailand", "UK", "United States", "Global Emerging Markets" };
			String[] UnitedStates =   { "United States" };

			String asStringAfrica=Arrays.toString(Africa);
			String asStringAmericas=Arrays.toString(Americas);
			String asStringAsia=Arrays.toString(Asia);
			String asStringEmergingMarkets=Arrays.toString(EmergingMarkets);
			String asStringEurope=Arrays.toString(Europe);
			String asStringGlobal=Arrays.toString(Global);
			String asStringUnitedStates=Arrays.toString(UnitedStates);

			boolean result3=false;
			String geographyOptionText=APPTEXT.getProperty(geographyOption);
			log.debug("geographyOptionText  :" + geographyOptionText );

			int occurenceFailCount=0;
			for(int i=1;i<=rows.size();i++) {			 
				log.debug("\nRow "+i);

				if(driver.findElement(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell geog-cell multi-check')]/span")).getAttribute("innerHTML").equals("Multi")) {
					multiList = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell geog-cell multi-check')]/ul/li"));

					log.debug("Multilist Size "+ multiList.size());
					result3=false;

					for(int j=0;j<multiList.size();j++) {
						log.debug("List Element: "+multiList.get(j).getAttribute("innerHTML"));
						String s = multiList.get(j).getAttribute("innerHTML");
						result3 = false;
						int trueCount=0;

						if (geographyOptionText.equalsIgnoreCase("Africa")) {

							if (asStringAfrica.contains(s)) {
								result3 = true;
								trueCount++;
								break;

							}
						}
						if (geographyOptionText.equalsIgnoreCase("Americas")) {


							if(asStringAmericas.contains(s))
							{
								result3 = true;
								trueCount++;
								break;

							}
						}
						if (geographyOptionText.equalsIgnoreCase("Asia")) {

							if(asStringAsia.contains(s))
							{
								result3 = true;
								trueCount++;
								break;

							}
						}
						if (geographyOptionText.equalsIgnoreCase("Emerging Markets")) {

							if(asStringEmergingMarkets.contains(s))
							{
								result3 = true;
								trueCount++;
								break;

							}
						}
						if (geographyOptionText.equalsIgnoreCase("Europe")) {

							if(asStringEurope.contains(s))
							{
								result3 = true;
								trueCount++;
								break;

							}
						}
						if (geographyOptionText.equalsIgnoreCase("Global")) {
							if(asStringGlobal.contains(s))
							{
								result3 = true;
								trueCount++;
								break;

							}
						}
						if (geographyOptionText.equalsIgnoreCase("United States")) {
							if(asStringUnitedStates.contains(s))
							{
								result3 = true;
								trueCount++;
								break;

							}
						}

						if((trueCount==0) && (j == multiList.size())){
							occurenceFailCount++;
							break ;

						}


					}
				}else
				{
					multiList = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell geog-cell multi-check')]/span"));

					log.debug("Multilist Size "+ multiList.size());
					result3=false;

					for(int j=0;j<multiList.size();j++) {
						log.debug("List Element: "+multiList.get(j).getAttribute("innerHTML"));
						String s = multiList.get(j).getAttribute("innerHTML");
						result3 = false;
						int trueCount=0;

						if (geographyOptionText.equalsIgnoreCase("Africa")) {

							if (asStringAfrica.contains(s)) {
								result3 = true;
								trueCount++;
								break;

							}
						}
						if (geographyOptionText.equalsIgnoreCase("Americas")) {


							if(asStringAmericas.contains(s))
							{
								result3 = true;
								trueCount++;
								break;

							}
						}
						if (geographyOptionText.equalsIgnoreCase("Asia")) {

							if(asStringAsia.contains(s))
							{
								result3 = true;
								trueCount++;
								break;

							}
						}
						if (geographyOptionText.equalsIgnoreCase("Emerging Markets")) {

							if(asStringEmergingMarkets.contains(s))
							{
								result3 = true;
								trueCount++;
								break;

							}
						}
						if (geographyOptionText.equalsIgnoreCase("Europe")) {

							if(asStringEurope.contains(s))
							{
								result3 = true;
								trueCount++;
								break;

							}
						}
						if (geographyOptionText.equalsIgnoreCase("Global")) {
							if(asStringGlobal.contains(s))
							{
								result3 = true;
								trueCount++;
								break;

							}
						}
						if (geographyOptionText.equalsIgnoreCase("United States")) {
							if(asStringUnitedStates.contains(s))
							{
								result3 = true;
								trueCount++;
								break;

							}
						}

						if((trueCount==0) && (j == multiList.size())){
							occurenceFailCount++;
							break ;

						}
					}
				}
			}
			List<String> assetValue=new ArrayList<String>();
			boolean result4=false;
			double minValue = 0;
			double maxValue = 0;

			minValue=Functions.getDoubleAUMval(minString);
			maxValue=Functions.getDoubleAUMval(maxString);
			log.debug(minValue + "  doublevalues "+ maxValue );

			for(int i=1;i<=rows.size();i++) {			 
				log.debug("\nRow "+i);
				multiList = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell aum-cell')]/span"));
				log.debug("Multilist Size "+ multiList.size());
				for(int j=0;j<multiList.size();j++) {
					log.debug("List Element: "+multiList.get(j).getAttribute("innerHTML"));
					if(!multiList.get(j).getAttribute("innerHTML").isEmpty()) {
						assetValue.add(multiList.get(j).getAttribute("innerHTML"));
					}
				}
			}

			log.debug("asset value size: " + assetValue.size());

			for(int k=0; k<assetValue.size(); k++){

				String s = assetValue.get(k).toString().trim().toLowerCase();
				double current = Functions.getDoubleAUMval(s);
				if( ! (current>=minValue && current<=maxValue)){
					log.debug("assetValue  "+ current + "   not in range :"+ minValue + " - "+ maxValue);
					result4=false;
					break;
				}
				else{
					log.debug("assetValue : "+ current + " is in the range :"+ minValue + " --"+ maxValue);
					result4=true;
				}
			}
			if((result1) && (result2) && (result3==true && occurenceFailCount<1) && (result4) ) {
				return "Pass";
			}else {
				return "Fail";
			}
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing managersVerifyAllData -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}

	public String setDynamicValue() {
		log.debug("=============================");
		log.debug("Executing setDynamicValue");
		try {
			String x =  driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			log.debug("Dynamic value is" + x);
			testData.setCellData(currentTest, data_column_nameArr[0], testRepeat, x);
			log.debug("Value set in excel is"+testData.getCellData(currentTest, data_column_nameArr[0], testRepeat));
		} catch (Throwable t) {
			// report error
			log.debug("Error while clicking on link -" + objectArr[0]
					+ t.getMessage());    
			return "Fail - Link Not Found";
		}
		return "Pass";  
	}

	public String verifyDynamicValue() {
		log.debug("=============================");
		log.debug("Executing verifyDynamicValue");
		try {

			String x =  driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();

			log.debug("Actual value is: " + x);
			String expectedValue = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			log.debug("Expected value is: " + expectedValue);
			try {
				int x1=Integer.parseInt(x);
				int expectedValue1 = Integer.parseInt(expectedValue);
				if (x1==expectedValue1){
					return "Pass";
				}
				else
					return "Fail";
			}catch(Throwable t) {
				if(x.equals(expectedValue)) {
					return "Pass";
				}else {
					return "Fail";
				}

			}
		}catch (Throwable t) {
			// report error
			log.debug("Error while executing verifyDynamicValue -" + objectArr[0]+ t.getMessage());
			return "Fail - Link Not Found";
		}
	}

	public String browserBack() {
		log.debug("=============================");
		log.debug("Executing browserBack");
		try {
			driver.navigate().back(); 
		} catch (Throwable t) {
			// report error
			log.debug("Error while clicking browserBack button -" + objectArr[0]+ t.getMessage());           
			return "Fail - Link Not Found";
		}
		return "Pass";
	}
	public String managersDefaultSortAUMDsc() {
		log.debug("=============================");
		log.debug("managersVerifySortAUMAsc");
		List<String> assetValue=new ArrayList<String>();
		boolean noData=false;
		boolean result=false;
		/*double minValue = 0;
	double maxValue = 0;*/
		try {
			List<WebElement> multiList;
			List<WebElement> rows;
			try {
				rows = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]"));
			}catch(Throwable t) {
				noData=true;
				return "Fail-Data not present";
			}
			for(int i=1;i<=rows.size();i++) {			 
				log.debug("\nRow "+i);
				multiList = driver.findElements(By.xpath("//tr[contains(@class,'dsp-row force-hover ng-scope')]["+i+"]/descendant::td[contains(@class,'dsp-cell aum-cell')]/span"));
				log.debug("Multilist Size "+ multiList.size());
				for(int j=0;j<multiList.size();j++) {
					log.debug("List Element: "+multiList.get(j).getAttribute("innerHTML"));
					if(!multiList.get(j).getAttribute("innerHTML").isEmpty()) {
						assetValue.add(multiList.get(j).getAttribute("innerHTML"));
					}
				}
			}
			//System.out.println(assetValue.size());
			for(int k=0; k<assetValue.size()-1; k++){

				System.out.println("assetValue :" + assetValue.get(k));
				String s = assetValue.get(k).toString().trim().toLowerCase();
				String s2 = assetValue.get(k+1).toString().trim().toLowerCase();
				double current = Functions.getDoubleAUMval(s);
				double next = Functions.getDoubleAUMval(s2);
				if( ! (current>=next)){
					//System.out.println("current : "+ current + "  next : "+ next );
					log.debug("current : "+ current + "  next : "+ next);
					result=false;
					break;
				}
				else{
					//System.out.println("current : "+ current + "  next : "+ next );
					log.debug("current : "+ current + "  next : "+ next);
					result=true;
				}
			}
			if(result==true)
				return "Pass";
			if(noData)
				return "Fail - No Data";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			if(noData)
				return "Fail - No Data";
			log.debug("Error while executing managersVerifySortAUMAsc -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}
	public String searchResults_verifyScrollbarPosition() {
		log.debug("=============================");
		log.debug("Executing searchResults_verifyScrollbarPosition Keyword");
		try {
			String rowsLocator = OR.getProperty(objectArr[4]);
			String dragThumbLocator = OR.getProperty(objectArr[5]);
			String totalRowCount = driver.findElement(By.xpath(OR.getProperty(objectArr[6]))).getText();
			String totalRowCountTrimed=Functions.replaceAll(totalRowCount,"[a-zA-Z \\( \\)]", "");
			int totalRowInt = Integer.parseInt(totalRowCountTrimed);
			Functions.dragTillAllRowsLoadedWithWait(driver, log, rowsLocator, dragThumbLocator, totalRowInt, 100);
			WebElement element1; 
			String val,scrollHeight;
			double topVal,topHeightVal,scrollHeightVal;
			int top=0;
			int pixelsToClick = 0;
			boolean flag = true;	
			element1 = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			val = element1.getAttribute("style");
			String scenario = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			topHeightVal = Functions.pixelValDouble(val, "height:");
			WebElement element2 = driver.findElement(By.xpath(OR.getProperty(objectArr[1])));
			scrollHeight = element2.getAttribute("style");
			scrollHeightVal = Functions.pixelValDouble(scrollHeight, "height:");
			top =(int) -scrollHeightVal;
			Functions.dragTo(driver, element1, top);
			//cases
			if(scenario.equals("bottom")) {
				pixelsToClick = (int) (scrollHeightVal- topHeightVal);
				Functions.dragTo(driver, element1, pixelsToClick);
			}
			else if(scenario.equals("top"))
				pixelsToClick = 0;
			else if(scenario.equals("mid1")) {
				pixelsToClick =(int) (scrollHeightVal- topHeightVal)/2;
				Functions.dragTo(driver, element1, pixelsToClick);
			}
			else if(scenario.equals("mid2")) {
				pixelsToClick = (int) (scrollHeightVal- topHeightVal)/4;
				Functions.dragTo(driver, element1, pixelsToClick);
			}
			//fetch top value now and match it with the pixelsToClick
			element1 = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			val = element1.getCssValue("top");
			topVal = Functions.pixelVal(val);
			if(pixelsToClick != topVal) {
				log.debug("pixels to Click value :" + pixelsToClick);
				log.debug("top value :" + topVal);
				flag = false;}
			//navigate to the other view and return back
			driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).click();
			Thread.sleep(WAIT1SEC);
			driver.findElement(By.xpath(OR.getProperty(objectArr[3]))).click();
			//verify again
			element1 = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			val = element1.getCssValue("top");
			topVal = Functions.pixelVal(val);
			if(pixelsToClick != topVal) {
				log.debug("pixels to Click value :" + pixelsToClick);
				log.debug("top value :" + topVal);
				flag = false;
			}
			if(flag)
			{
				log.debug("pixels to Click value :" + pixelsToClick);
				log.debug("top value :" + topVal);
				return "Pass";}
			else
				return "Fail";
		}catch(Throwable e) {
			log.debug("Error while executing searchResults_verifyScrollbarPosition " +  e.getMessage());
			return "Fail";
		}
	}

	public String verifyTitlecount()
	{
		log.debug("=============================");
		log.debug("Executing countTitles Keyword");
		//this keyword counts the number of titles in the profile navigation defined on author page
		try {
			int titlecount;
			List<WebElement> list = driver.findElements(By.xpath(OR.getProperty(objectArr[0])));
			titlecount = list.size();
			WebElement add = driver.findElement(By.xpath(OR.getProperty(objectArr[1])));
			add.click();
			Thread.sleep(5000);
			WebElement popup = driver.findElement(By.xpath(OR.getProperty(objectArr[2]))); 
			log.debug("Title size : "+titlecount);
			int expectedCount = Integer.parseInt(testData.getCellData(currentTest, data_column_nameArr[1],testRepeat));
			log.debug("Expected size : "+expectedCount);
			if(titlecount < expectedCount)
			{

				if(popup.isDisplayed())
				{						
					log.debug("Pop up is displayed");
					driver.findElement(By.xpath(OR.getProperty(objectArr[3]))).click();
					return "Fail";
				}
				else
				{
					titlecount++;
					String count = String.valueOf(titlecount);
					testData.setCellData(currentTest, data_column_nameArr[0], testRepeat, count);
					return "Pass";
				}

			}
			else
			{
				if(popup.isDisplayed())
				{
					String count = String.valueOf(titlecount);
					testData.setCellData(currentTest, data_column_nameArr[0], testRepeat, count);
					getWebElement(OR,objectArr[3]).click();
					return "Pass";
				}
				else
				{
					log.debug("Pop up is not displayed");
					return "Fail";
				}
			}

		}catch(Throwable e)
		{
			log.debug("Error while executing countTitles " +  e.getMessage());
			return "Fail";
		}

	}

	public String verifyTabs()
	{
		log.debug("=============================");
		log.debug("Executing verifyTabs Keyword");

		//this keyword counts the number of tabs present on fund profile page
		try
		{
			String expecteddata = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			int expectedcount = Integer.parseInt(expecteddata);
			List<WebElement> actualdata = driver.findElements(By.xpath(OR.getProperty(objectArr[0])));
			int actualcount = actualdata.size();

			log.debug("Expected count : "+expectedcount);
			log.debug("Actual count : "+actualcount);
			if(expectedcount == actualcount)
			{
				return "Pass";
			}
			else
			{
				return "Fail";
			}
		}catch(Throwable e)
		{
			log.debug("Error while executing verifyTabs " +  e.getMessage());
			return "Fail";
		}
	}

	public String verifyScrollbarReset() {
		log.debug("=============================");
		log.debug("Executing verifyScrollbarReset");
		//this keyword verifies whther a element gets reset or not after particular operation
		try {

			boolean flag1 = false;
			boolean flag2 = false;
			WebElement element = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			String val = element.getCssValue("top");
			int topVal = (int) Functions.pixelValDouble(val);
			log.debug("Expected position of scroll bar is :" + topVal);

			String string = scrollDrag();
			if(string.equalsIgnoreCase("Pass"))
				flag1=true;
			else
				return "Fail: can not drag scroll bar";

			WebElement element1 = driver.findElement(By.xpath(OR.getProperty(objectArr[1])));

			element1.click();
			Thread.sleep(1000);

			WebElement element2 = null;
			try{
				element2 = driver.findElement(By.xpath(OR.getProperty(objectArr[2])));
			}catch(Throwable e){
				log.debug("third object not given");
			}
			if(element2 != null)
				element2.click();
			Thread.sleep(1000);


			String val1 = element.getCssValue("top");
			int topVal1 = (int) Functions.pixelValDouble(val1);
			log.debug("Current position of scroll bar :" + topVal1);

			if(topVal==topVal1)
				flag2=true;

			if(flag1&&flag2)
				return "Pass";
			else
				return "Fail: can not reset Scroll Bar";

		}catch(Throwable e) {
			log.debug("Error while executing verifyScrollbarReset" +  e.getMessage());
			return "Fail";
		}
	}

	public String bookmarkPage(){
		log.debug("=============================");
		log.debug("Executing bookmarkPage Keyword");
		String URL;
		try{
			URL = driver.getCurrentUrl();
			Thread.sleep(WAIT5SEC);
			//Goto homepage
			getWebElement(OR, objectArr[0]).click();
			Thread.sleep(WAIT5SEC);
			//Navigate to the URL
			driver.navigate().to(URL);
		}catch(Throwable t){
			log.debug("Error while executing bookmarkPage keyword - Object: "+ objectArr[0] + "\n Stacktrace: \n"+t.getMessage());
			return "Fail- Debug Required in catch";
		}
		return "Pass";
	}

	public String getURL() {
		log.debug("=============================");
		log.debug("Executing getURL Keyword");
		try {		
			String url = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			String[] testDataUrl = url.split("/");
			String[] title = driver.getCurrentUrl().split("/");
			String currentEnv  = title[2];
			String url1= title[0]+"//" + currentEnv ;

			for(int i=3; i<testDataUrl.length; i++){
				url1=url1 + "/" + testDataUrl[i];
			}

			log.debug("URL: "+ url1 );
			driver.get(url1);	
			return "Pass";
		}catch(Throwable e) {
			log.debug("Error while executing getURL keyword - \n Stacktrace: \n"+e.getMessage());
			return "Fail- Debug Required in catch";
		}
	}

	public String author_clickWorkspaceOKButton(){
		log.debug("=============================");
		log.debug("Executing author_clickWorkspaceOKButton Keyword");

		try{
			for(int i=2; i<=10; i++){
				WebElement OKButton = driver.findElement(By.xpath("//div[@class=' x-window-plain x-form-label-left' or @class='x-window-plain x-form-label-left'][" + i +"]/descendant::div[@class='x-window x-window-plain x-resizable-pinned' or @class=' x-window x-window-plain x-resizable-pinned']/descendant::button[text()='OK' and (@class=' x-btn-text' or @class='x-btn-text')]"));
				if(OKButton.isDisplayed()){
					OKButton.click();
					break;
				}

			}

		}catch(Throwable t){
			log.debug("Error while executing author_clickWorkspaceOKButton keyword");
			log.debug(t.getMessage());
			return "Fail";
		}
		return "Pass";
	}


	public String verifyTextFromDataSheet() {
		log.debug("=============================");
		log.debug("Executing verifyTextFromDataSheet");
		String expected = null,actual = null;
		try {
			expected = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			log.debug("expected Text  -  " + expected);
		}catch(Throwable e) {
			log.debug("expected Text  -  " + expected);
			log.debug("Property " + objectArr[0] +" missing from data  file");
			return "Fail- Debug Required";
		}
		try {
			actual = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			log.debug("actual Text  -  " + actual);
		} catch (Throwable t) {
			log.debug("actual Text  -  " + actual);
			log.debug("Property " + objectArr[0] +" missing from the web page");
			return "Fail- Debug Required";
		}

		if(expected.trim().contains(actual.trim())  || actual.trim().contains(expected.trim()) )
			return "Pass";
		else {
			log.debug("expected Text  -  " + expected);
			log.debug("actual Text  -  " + actual);
			return "Fail";
		}
	}

	public String workspace_Landing_VerifyPlaylist(){
		log.debug("=============================");
		log.debug("Executing workspace_Landing_VerifyPlaylist Keyword");
		int whatToDo = 0;
		boolean result=false;

		try{
			String whatToDoString = objectArr[0];
			log.debug("what to do : " + whatToDoString);
			String expected = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
			log.debug("playlist name  : " + expected);

			if(whatToDoString.equalsIgnoreCase("aims.activate"))
				whatToDo = 1;
			if(whatToDoString.equalsIgnoreCase("aims.deactivate"))
				whatToDo = 2;

			List<WebElement> playlists = driver.findElements(By.xpath("//*[@id='icarousel']/descendant::div[contains(@class,'slide')]"));
			log.debug("size  :" + playlists.size());

			switch(whatToDo){
			case 1:
				log.debug("inside case 1- Activate");
				for(int i=1; i<=playlists.size(); i++){
					WebElement e = driver.findElement(By.xpath("//*[@id='icarousel']/descendant::div[contains(@class,'slide')][" + i + "]"));
					if(e.getAttribute("data-href").endsWith(expected)){
						log.debug("data href  : " +e.getAttribute("data-href").toString());
						result = true;
						break;
					}
					else{
						log.debug("data href  : " +e.getAttribute("data-href").toString());
						result = false;
					}
				}
				break;

			case 2:
				log.debug("inside case 2 - Deactivate");
				for(int i=1; i<=playlists.size(); i++){
					WebElement e = driver.findElement(By.xpath("//*[@id='icarousel']/descendant::div[contains(@class,'slide')][" + i + "]"));
					if(e.getAttribute("data-href").endsWith(expected)){
						log.debug("data href  : " +e.getAttribute("data-href").toString());
						result = false;
						break;
					}
					else{
						log.debug("data href  : " +e.getAttribute("data-href").toString());
						result = true;
					}
				}
				break;
			}

			if(result)
				return "Pass";
			else
				return "Fail";

		}catch(Throwable t){
			log.debug("Error while executing workspace_Landing_VerifyPlaylist keyword");
			log.debug(t.getMessage());
			return "Fail";
		}

	}

	public String verifyDisclaimerScrollPresence()
	{
		log.debug("=============================");
		log.debug("Executing verifyDisclaimerScrollPresence Keyword");

		String charcount;
		int size;
		try
		{
			WebElement add = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			charcount = add.getText();
			size = charcount.length();
			WebElement scrollbar = driver.findElement(By.xpath(OR.getProperty(objectArr[1])));

			if(size >= 500)
			{
				if(scrollbar.isDisplayed())
					return "Pass";
				else
					return "Fail";
			}
			else
			{
				if(scrollbar.isDisplayed())
					return "Fail";
				else
					return "Pass";
			}

		}catch(Throwable e)
		{
			log.debug("Error while executing verifyScrollPresence keyword"+ e.getMessage());
			return "Fail";
		}
	}

	public String verifyCheckBoxSelected() {
		log.debug("=============================");
		log.debug("Executing verifyCheckBoxSelected Keyword");
		try {
			WebElement checkbox = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			String expected = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			boolean isChecked = checkbox.isSelected();
			log.debug("Expected Value: "+expected);
			log.debug("isChecked Value: "+isChecked);
			if(isChecked && expected.equalsIgnoreCase("TRUE")){
				return "Pass";
			}
			else if(!isChecked && expected.equalsIgnoreCase("FALSE"))
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			log.debug("Error while executing verifyCheckBoxSelected keyword - Object: "+ objectArr[0] + "\n Stacktrace: \n"+t.getMessage());
			return "Fail- Debug Required in catch";
		}
	}

	public String inputAndClickEnterKeyTwice() {
		log.debug("=============================");
		log.debug("Executing inputAndClickEnterKeyTwice Keyword");
		//this keyword takes an input and press enter key twice
		String data = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
		try {
			getWebElement(OR, objectArr[0]).sendKeys(data);
			Thread.sleep(WAIT2SEC);
			log.debug("data inserted into the search box: " + data);
			getWebElement(OR, objectArr[0]).sendKeys(Keys.ENTER);
			Thread.sleep(WAIT2SEC);
			getWebElement(OR, objectArr[0]).sendKeys(Keys.ENTER);
			Thread.sleep(WAIT2SEC);
			log.debug("Enter clicked");
			String title = driver.getTitle();
			log.debug("Browser title is :" + title);
			if(!title.contains("Page Not Found")) {
				log.debug("Arrived on crx page. The page title is :" + title);
				return "Pass";
			}
			else {
				log.debug("Did not arrive on crx page. The page title is :" + title);
				return "Fail";
			}
		} catch (Throwable t) {
			// report error
			log.debug("Error while inputAndClickEnterKeyTwice -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}

	}
	public String setAppendedDynamicValue() {
		log.debug("=============================");
		log.debug("Executing setAppendedDynamicValue");
		//this keyword appends a string to a dynamic string and sets it in test data
		try {
			Functions.highlighter(driver,
					driver.findElement(By.xpath(OR.getProperty(objectArr[0]))));
			String x =  driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			log.debug("Dynamic value is" + x);
			String string = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			String appendedString=x.concat(string);
			log.debug("Appended string is: " + appendedString);
			testData.setCellData(currentTest, data_column_nameArr[1], testRepeat, "");
			testData.setCellData(currentTest, data_column_nameArr[1], testRepeat, appendedString);
			log.debug("Value set in excel is"+testData.getCellData(currentTest, data_column_nameArr[1], testRepeat));
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing setAppendedDynamicValue -" + objectArr[0]
					+ t.getMessage());    
			return "Fail";
		}
		return "Pass";
	}
	public String setAppendedURL() {
		log.debug("=============================");
		log.debug("Executing setAppendedURL");
		try {
			String string = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
			System.out.println(string);
			String x = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			System.out.println(x);
			String appendedString;
			if((string.contains("Manager_"))) {		
				String[] str;
				str = string.split("/");
				int size= str.length;
				String manID= str[size-1];
				testData.setCellData(currentTest, data_column_nameArr[3], testRepeat, "");
				testData.setCellData(currentTest, data_column_nameArr[3], testRepeat, manID);
				appendedString=x.concat(manID);
			}
			else {
				appendedString=x.concat(string);
			}
			log.debug(appendedString);
			testData.setCellData(currentTest, data_column_nameArr[2], testRepeat, "");
			testData.setCellData(currentTest, data_column_nameArr[2], testRepeat, appendedString);

			log.debug("value set in excel is"+testData.getCellData(currentTest, data_column_nameArr[2], testRepeat));
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing setAppendedURL -" + t.getMessage());    
			return "Fail";
		}
		return "Pass";  
	}

	public String dateConverter() {
		log.debug("=============================");
		log.debug("Executing dateConverter");
		//this keyword converts date in specified format sets it in testData
		try {
			String actualDate =  driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			log.debug("Date from crx is: " + actualDate);
			String array[]=actualDate.split("T");
			String dateFormat = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat df = new SimpleDateFormat(dateFormat); 
			Date date = formatter.parse(array[0]);
			String convertedDate = df.format(date);
			log.debug("Converted date is: " + convertedDate);
			String flag = testData.getCellData(currentTest, data_column_nameArr[2], testRepeat);
			if(flag.equalsIgnoreCase("yes")){
				String expectedFormat = " (" + convertedDate + ")";
				log.debug("Converted date in expected format is: " + expectedFormat);
				testData.setCellData(currentTest, data_column_nameArr[0], testRepeat, expectedFormat);
			}
			else 
				testData.setCellData(currentTest, data_column_nameArr[0], testRepeat, convertedDate);
		}catch (Throwable t) {
			// report error
			log.debug("Error while executing dateConverter -" + objectArr[0]
					+ t.getMessage());    
			return "Fail";
		}
		return "Pass";  
	}


	public String closeTab(){
		log.debug("=============================");
		log.debug("CloseTab");

		try{

			ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
			driver.switchTo().window(tabs2.get(1));
			driver.close();
			driver.switchTo().window(tabs2.get(0));

		}catch(Throwable t){
			log.debug("CloseTab");
			log.debug(t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String splitStringAndVerify() {
		log.debug("=============================");
		log.debug("Executing splitStringAndVerify");
		try {
			String actualString =  driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			String pos = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			String array[]=actualString.split(pos+" ");
			log.debug("String after splitting is: " + array[1]);
			log.debug("String after splitting is: " + array[0]);
			String expectedValue = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);

			log.debug("expected value is: " + expectedValue);
			if(array[1].equals(expectedValue)) {
				return "Pass";
			}else {
				return "Fail";
			}

		}catch (Throwable t) {
			// report error
			log.debug("Error while executing splitStringAndVerify -" + objectArr[0]
					+ t.getMessage());    
			return "Fail";
		}
	}
	public String splitStringAndStore() {
		log.debug("=============================");
		log.debug("Executing splitStringAndStore");
		String array[];
		String actualString,pos,arrayindex,storedString;
		try {
			System.out.println(testData.getCellData(currentTest, data_column_nameArr[0], testRepeat));
			actualString = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			pos = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
			array =actualString.split(pos);
			log.debug("String after splitting is: " + array[1]);
			log.debug("String after splitting is: " + array[0]);
			arrayindex = testData.getCellData(currentTest, data_column_nameArr[2], testRepeat);
			storedString= array[Integer.parseInt(arrayindex)];
			if(pos.equals("Manager_"))
			{
				String array1[]=storedString.split("/");
				storedString=pos.concat(array1[0]);			
			}

			testData.setCellData(currentTest, data_column_nameArr[3], testRepeat,storedString );
			log.debug("stored value is: " + storedString);
			return "Pass";

		}catch (Throwable t) {
			// report error
			log.debug("Error while executing splitStringAndStore -" + objectArr[0]
					+ t.getMessage());    
			return "Fail";
		}
	}

	public String findElementAndDelete(){
		log.debug("=============================");
		log.debug("findElementAndDelete");

		WebElement webElement;

		try {
			Thread.sleep(WAIT5SEC);
			try {
				webElement = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			}catch(Throwable e){
				webElement=null;
			}
			while (webElement != null) {
				Thread.sleep(WAIT5SEC);
				action.contextClick(getWebElement(OR, objectArr[0])).perform();
				//action.contextClick(driver.findElement(By.xpath(OR.getProperty(objectArr[0])))).perform();
				getWebElement(OR, objectArr[1]).click();
				Thread.sleep(WAIT4SEC);				
				getWebElement(OR, objectArr[2]).click();
				Thread.sleep(WAIT5SEC);
				try{driver.findElement(By.xpath(OR.getProperty(objectArr[2])));
				driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).click();
				Thread.sleep(WAIT5SEC);
				}
				catch(NoSuchElementException f){
					log.debug("Second pop up asking delete confirmation did not appear");				
				}
				try {
					webElement = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));

				}catch(Throwable e){
					webElement=null;
				}
			}
			return "Pass";
		}catch (Throwable f){
			log.debug("Error while executing findElementAndDelete");
			log.debug(f.getMessage());
			return "Fail";
		}

	}




	public String verifyVisibility()
	{
		log.debug("=============================");
		log.debug("Executing verifyVisibility Keyword");
		//keyword clicks the object until its visibillity is true
		try
		{
			WebElement element = getWebElement(OR, objectArr[0]);
			WebElement element1 = getWebElement(OR, objectArr[1]);
			int i = 0;
			while (i<2)
			{
				if(element.isDisplayed())
				{		
					element.click();
					log.debug("Element click at "+(i+1)+" attempt");
					return "Pass";		
				}
				else
				{
					element1.click();
					Thread.sleep(5000);
					if(element.isDisplayed())
					{
						element.click();
						log.debug("Element clicked at "+(i+1)+" attempt");
						return "Pass";
					}
					else
						continue;
				}
			}
			log.debug("Failed 3 attempts!");
			return "Fail";
		}catch(Throwable e)
		{
			log.debug("Error while executing verifyVisibility keyword"+ e.getMessage());
			return "Fail";

		}
	}
	public String messageValidation_splitText() {
		log.debug("=============================");
		log.debug("Executing messageValidation_splitText");
		try {
			String actualString =  getWebElement(OR, objectArr[0]).getText();
			String splitString =  getWebElement(OR, objectArr[1]).getText();
			String expected = APPTEXT.getProperty(objectArr[0]);
			log.debug("actual value is: " + actualString);
			log.debug("split value is: " + splitString);
			log.debug("expected value is: " + expected);
			String array[]=actualString.split(splitString);
			log.debug("String after splitting is: " + array[0]);
			if(array[0].trim().equals(expected.trim())) {
				return "Pass";
			}else {
				return "Fail";
			}
		}catch (Throwable t) {
			// report error
			log.debug("Error while executing messageValidation_splitText -" + objectArr[0]
					+ t.getMessage());    
			return "Fail";
		}
	}

	public String verifySearchFilter()
	{
		log.debug("=============================");
		log.debug("Executing verifySearchFilter Keyword");
		//keyword verifies search result with filter maintained by going from one view to another
		try
		{
			WebElement firstviewfirstfund = getWebElement(OR, objectArr[0]);
			WebElement changeviewbutton = getWebElement(OR, objectArr[1]);
			String first = firstviewfirstfund.getText();
			changeviewbutton.click();
			Thread.sleep(5000);
			WebElement secondviewfirstfund = getWebElement(OR, objectArr[2]);
			String second = secondviewfirstfund.getText();


			if(second.contains(first) || first.contains(second))
			{
				log.debug("List view fund: "+first);
				log.debug("Grid view fund: "+second);
				return "Pass";
			}
			else {
				log.debug("List view fund: "+second);
				log.debug("Grid view fund: "+first);
				return "Fail";
			}
		}
		catch(Throwable e)
		{
			log.debug("Error while executing verifySearchFilter keyword"+ e.getMessage());
			return "Fail";
		}
	}

	public String verifySpaceSearch()
	{
		log.debug("=============================");
		log.debug("Executing verifySpaceSearch Keyword");
		//this keyword verifies search results with any number of spaces in the search box
		try
		{
			String input = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			String trimmedinput = input.trim();
			WebElement firstfund = getWebElement(OR, objectArr[0]);
			String checktext = firstfund.getAttribute("innerHTML");

			log.debug("Search text : "+trimmedinput);
			log.debug("Fund : "+checktext);
			if(checktext.contains(trimmedinput))
				return "Pass";
			else
				return "Fail";

		}catch(Throwable e)
		{
			log.debug("Error while executing verifySpaceSearch keyword"+ e.getMessage());
			return "Fail";

		}
	}

	public String scrollAndClick(){	
		log.debug("=============================");
		log.debug("Executing scrollAndClick");
		WebElement element1=null;
		int n = 0;
		int m = 0;
		try {
			n = Integer.parseInt(testData.getCellData(currentTest, data_column_nameArr[2], testRepeat));
			m = Integer.parseInt(testData.getCellData(currentTest, data_column_nameArr[3], testRepeat));
			System.out.println(m+"   "+n);
			element1 = getWebElement(OR, objectArr[1]);
		}catch(Throwable t){
			log.debug("No values in Test Data.");    
		}
		try{
			List<WebElement> elements = getWebElements(OR, objectArr[0]);
			log.debug(elements.size());
			String categoryType = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			String categoryName = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
			log.debug("Category Type: "+categoryType);
			log.debug("Category Name: "+categoryName);
			int j=1;
			for(int k=0;k<elements.size();k++){	
				Thread.sleep(2000);
				if(elements.get(k).getAttribute(categoryType).equalsIgnoreCase(categoryName)){
					if(!(launchBrowser.equals("Firefox") || launchBrowser.equals("InternetExplorer")))
						(new Actions(driver)).doubleClick(elements.get(k)).perform();
					else
						elements.get(k).click();
					break;
				}
				j++;
				if(element1 != null){
					if(j>m)
					{
						try{
							Functions.dragTo(driver, element1, n);
						}catch(Throwable t){
							log.debug("Scroll Values: "+t.getMessage());
						}
						Thread.sleep(1000);
						j=1;
					}
				}
			}
			return "Pass";
		}catch (Throwable t) {
			// report error
			log.debug("Error while executing scrollAndClick -" + objectArr[0]
					+ t.getMessage());    
			return "Fail";
		}
	}

	public String storeRelatedTabDefaultMessage()
	{
		log.debug("=============================");
		log.debug("Executing storeRelatedTabDefaultMessage Keyword");
		//keyword verifies the Related Tab description as per the fund selected.
		try
		{
			WebElement strategy =  getWebElement(OR, objectArr[0]);
			WebElement nameoffund = getWebElement(OR, objectArr[1]);
			WebElement managername = getWebElement(OR, objectArr[2]);

			String strategytext = strategy.getText();
			String nameoffundtext = nameoffund.getText();
			String managernametext = managername.getText();

			String msgpart1 = "You may be interested in the following funds based on their ";
			String msgpart2 = " strategy classification which is similar classification of ";
			String msgpart3 = ", or the fund is managed by ";
			String msg;
			String overallmsg = msgpart1+strategytext+msgpart2+nameoffundtext+msgpart3+managernametext+".";
			int length = overallmsg.length();

			if(length > 235)
			{
				msg = overallmsg.substring(0, 235);	
			}
			else
			{
				msg = overallmsg;
			}

			log.debug("expected message  " +  msg);
			testData.setCellData(currentTest, data_column_nameArr[0], testRepeat, msg);
			return "Pass";

		}catch(Throwable e)
		{
			log.debug("Error while executing storeRelatedTabDefaultMessage keyword"+ e.getMessage());
			return "Fail";
		}

	}

	public String setSize(){
		log.debug("=============================");
		log.debug("Executing setSize");
		//this keyword finds size of an object and sets it in testData
		try{
			int sizeOfObject = getWebElements(OR, objectArr[0]).size();
			log.debug("size on crx is: " + sizeOfObject);
			String sizeOfObject2 = String.valueOf(sizeOfObject);
			testData.setCellData(currentTest, data_column_nameArr[0], testRepeat, sizeOfObject2);
			log.debug("Value set in excel is"+testData.getCellData(currentTest, data_column_nameArr[0], testRepeat));
		} catch (Throwable t) {
			// report error
			log.debug("Error while clicking on link -" + objectArr[0]
					+ t.getMessage());    
			return "Fail - Link Not Found";
		}
		return "Pass";  
	}

	public String checkSize(){
		log.debug("=============================");
		log.debug("Executing checkSize");
		//this keyword checks and sets size of an object in testData
		try{
			String size = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			int sizeInt = Integer.parseInt(size);
			String data = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
			if(sizeInt>1){
				testData.setCellData(currentTest, data_column_nameArr[1], testRepeat, data);
				log.debug("Value set in excel is"+testData.getCellData(currentTest, data_column_nameArr[1], testRepeat));
				return "Pass";
			}
		} catch (Throwable t) {
			// report error
			log.debug("Error while Executing checkSize -" + objectArr[0]
					+ t.getMessage());    
			return "Fail";
		}
		return "Fail";
	}


	public String executeExeFile() {
		log.debug("=============================");
		log.debug("executeExeFile");
		try {
			String exefilename = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			//		String exepath = "C:/Users/dsing6/Desktop/"+exefilenameC:\Test\dependencies\workflow_Files;
			String exepath = System.getProperty("user.dir")+"/dependencies/workflow_Files/"+exefilename;
			log.debug(System.getProperty("user.dir")+"/dependencies/workflow_Files/"+exefilename);
			Runtime.getRuntime().exec(exepath);
			Thread.sleep(WAIT5SEC);
			return "Pass";
		}catch (Throwable t) {
			log.debug("executeExeFile" + data_column_nameArr[0] + t.getMessage() );
			return "Fail";
		}
	}

	public String verifyFieldPresenseAndClick(){
		log.debug("=============================");
		log.debug("Verifyfieldpresense");
		try{	
			int numElements1 = driver.findElements(By.xpath(OR.getProperty(objectArr[0]))).size();
			if(numElements1 == 1)
				return "Pass";
			else
				return "Fail-field not present";
		}catch(Throwable t){
			log.debug("Error while executing hoverOverVideoOverlay");
			log.debug(t.getMessage());
			return "Fail";
		}
	}
	
	public String  setUploadFile() {
		log.debug("=============================");
		log.debug("setUploadFile");
		try {
			String filePath = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			//		String exepath = "C:/Users/dsing6/Desktop/"+exefilenameC:\Test\dependencies\workflow_Files;
			String fileName = System.getProperty("user.dir")+"/dependencies/workflow_Files/"+filePath;
			log.debug(System.getProperty("user.dir")+"/dependencies/workflow_Files/"+fileName);
			driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).sendKeys(fileName);
			return "Pass";
		}catch (Throwable t) {
			log.debug("setUploadFile" + data_column_nameArr[0] + t.getMessage() );
			return "Fail";
		}
	}


	public String customClickLink() {
		log.debug("=============================");
		log.debug("Executing customClickLink");
		try {		
			if(launchBrowser.equals("Firefox")){
				Actions action = new Actions(driver);
				action.sendKeys(Keys.ESCAPE).build().perform();
				Thread.sleep(WAIT2SEC);
			}
			else{
				WebElement obj = getWebElement(OR, objectArr[0]);
				(new Actions(driver)).doubleClick(obj).perform();
			}
		} catch (Throwable t) {
			// report error
			log.debug("Error while clicking on link -" + objectArr[0]+ t);
			return "Fail - Link Not Found";
		}
		return "Pass";
	}

	public String verifyDocumentAndClick(){
		log.debug("=============================");
		log.debug("Executing verifyDocumentAndClick");
		try {		
			WebElement obj = getWebElement(OR, objectArr[0]);
			if(obj.getAttribute("data-doctype").equals("pdf")){
				obj.click();
				Thread.sleep(WAIT1SEC);
				getWebElement(OR, objectArr[1]).click();
			}else
				obj.click();
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing verifyDocumentAndClick -" + objectArr[0]+ t.getMessage());
			return "Fail - Link Not Found";
		}
		return "Pass";
	}


	public String sortDate() {
		log.debug("=============================");
		log.debug("Executing keyword sortDate");
		//this keyword sorts a list in asc or des order according to date
		List<Date> dates=new ArrayList<Date>();
		List<Date> datesSorted=new ArrayList<Date>();
		String ascOrDesc = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
		String attribute = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
		String dateFormat = testData.getCellData(currentTest, data_column_nameArr[2],testRepeat);

		boolean result=false;
		String value = null;
		try {	
			List<WebElement> search = getWebElements(OR, objectArr[0]);

			for (WebElement item : search) {
				value = item.getAttribute(attribute).trim();
				log.debug("Date is: " + value);
				SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
				Date date = formatter.parse(value);
				if(! value.isEmpty())
					dates.add(date);
			}

			datesSorted.addAll(dates);

			log.debug(dates.size());
			log.debug(datesSorted.size());
			if(ascOrDesc.equals("asc"))
				Collections.sort(datesSorted);
			else {
				Collections.sort(datesSorted);
				Collections.reverse(datesSorted);
			}

			for(int k=0; k<dates.size(); k++){

				log.debug("Dates: " + dates.get(k) + " Dates sorted:  "+ datesSorted.get(k));
				String unsorted = dates.get(k).toString().trim();
				String sorted = datesSorted.get(k).toString().trim();

				if(!(unsorted.equalsIgnoreCase(sorted)))
				{
					result=false;
					break;
				}
				else
					result=true;
			}

			if(result==true)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			//		 report error
			log.debug("Error while executing sortDate -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
	}
	public String storeDate()
	{
		log.debug("=============================");
		log.debug("Executing storeDate");
		//this keyword stores the date fetched from global search filters
		try {		

			WebElement month = getWebElement(OR, objectArr[0]);
			String Month = month.getAttribute("value");
			WebElement year = getWebElement(OR, objectArr[1]);
			String Year = year.getAttribute("value");
			log.debug("Month picked : "+Month);
			System.out.println("Month picked : "+Month);
			log.debug("Year picked : "+Year);
			System.out.println("Year picked : "+Year);
			testData.setCellData(currentTest, data_column_nameArr[0], 2, Month);
			testData.setCellData(currentTest, data_column_nameArr[1], 2, Year);
			return "Pass";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing  storeDate- "+ t.getMessage());
			return "Fail";
		}
	}


	public String verifyDateDifference()
	{
		log.debug("=============================");
		log.debug("Executing verifydateDifference");
		//this keyword verifies the difference in 2 stored dates and compares it with a specified number of months
		try {	
			String FromMonth = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			String FromYear = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
			String ToMonth = testData.getCellData(currentTest, data_column_nameArr[2],testRepeat);
			String ToYear = testData.getCellData(currentTest, data_column_nameArr[3],testRepeat);
			String ExpectedMonths = testData.getCellData(currentTest, data_column_nameArr[4],testRepeat);
			int fromMonth = Integer.parseInt(FromMonth);
			int fromYear = Integer.parseInt(FromYear);
			int toMonth = Integer.parseInt(ToMonth);
			int toYear = Integer.parseInt(ToYear);
			int expectedMonths = Integer.parseInt(ExpectedMonths);
			int yeardiff = toYear - fromYear;
			int totalMonths = (yeardiff*12) - fromMonth + toMonth;
			log.debug("TotalMonths : "+ totalMonths);
			log.debug("Expected Months : "+ expectedMonths);
			if(totalMonths <= expectedMonths)
			{
				return "Pass";
			}
			else
			{
				return "Fail";
			}

		}catch (Throwable t) {
			// report error
			log.debug("Error while executing  verifydateDifference- "+ t.getMessage());
			return "Fail";
		}

	}

	public String setMinimumDate(){

		log.debug("====================================");
		log.debug("Executing setMinimumDate ");
		try{
			int months = Integer.parseInt(testData.getCellData(currentTest, data_column_nameArr[0],testRepeat));
			int date = Integer.parseInt(testData.getCellData(currentTest, data_column_nameArr[1],testRepeat));
			Date currentDate = new Date();
			log.debug("Current Date: " + currentDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(currentDate);
			calendar.add(Calendar.MONTH,-(months+1));
			log.debug("Future Date: " + currentDate);
			calendar.add(Calendar.DATE,date);
			SimpleDateFormat inputFormat = new SimpleDateFormat("dd");
			log.debug("Day: "+inputFormat.format(calendar.getTime()));
			testData.setCellData(currentTest, data_column_nameArr[2], testRepeat,inputFormat.format(calendar.getTime()));

			inputFormat = new SimpleDateFormat("MMM");
			log.debug("Month: "+inputFormat.format(calendar.getTime()));
			testData.setCellData(currentTest, data_column_nameArr[3], testRepeat, inputFormat.format(calendar.getTime()));

			inputFormat = new SimpleDateFormat("yyyy");
			log.debug("Month: "+inputFormat.format(calendar.getTime()));
			testData.setCellData(currentTest, data_column_nameArr[4], testRepeat, inputFormat.format(calendar.getTime()));

		}catch(Throwable t){
			log.debug("Error in setMinimumDate "+t.getMessage());
			return "Fail";
		}
		return "Pass";	
	}

	public String dragAndDropElement()
	{
		log.debug("=============================");
		log.debug("Executing dragAndDropElement");
		try
		{

			WebElement dragElement = getWebElement(OR, objectArr[0]);
			WebElement dropElement = getWebElement(OR, objectArr[1]);

			try
			{
				WebElement anyReport = getWebElement(OR, "aims.library.source.firstRow.xpath");
				anyReport.click();
				WebElement closeButton = getWebElement(OR, "aims.fundProfile.iView.close.xpath");
				closeButton.click();
			}
			catch(Throwable t)
			{}
			Thread.sleep(3000);

			Actions builder = new Actions(driver);
			//		builder.dragAndDrop(dragElement, dropElement).build().perform();

			builder.clickAndHold(dragElement).perform();
			Thread.sleep(2000);

			builder.moveToElement(dropElement).perform();

			Thread.sleep(2000);

			builder.release(dropElement).perform();

			return "Pass";

		}catch(Throwable t)
		{
			log.debug("Error while executing  dragAndDropElement- "+ t.getMessage());
			return "Fail";

		}
	}
	public String verifySize(){
		log.debug("=============================");
		log.debug("Executing verifySize");
		try
		{
			List<WebElement> playList = (List<WebElement>) getWebElement(OR, objectArr[0]);
			int size = playList.size();

			log.debug("Size : "+size);
			return "Pass";

		}catch(Throwable t)
		{
			log.debug("Error while executing  verifySize- "+ t.getMessage());
			return "Fail";

		}
	}

	public String sortByTitle() {
		log.debug("=============================");
		log.debug("Executing sortByTitle");
		List<String> names=new ArrayList<String>();
		List<String> namesSorted=new ArrayList<String>();
		String ascOrDesc = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
		String attribute = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
		
		boolean result=false;
		String value = null;
		try
		{
			Thread.sleep(WAIT2SEC);
			List<WebElement> search = driver.findElements(By.xpath(OR.getProperty(objectArr[0])));

			for (WebElement item : search) {
				value = item.getAttribute(attribute).trim();
				log.debug("unsorted value is : " + value );
				if(! value.isEmpty())
					names.add(value);
			}

			namesSorted.addAll(names);

			log.debug(names.size());
			log.debug(namesSorted.size());
			if(ascOrDesc.equals("asc"))
				Collections.sort(namesSorted, String.CASE_INSENSITIVE_ORDER);
			else {
				Collections.sort(namesSorted, String.CASE_INSENSITIVE_ORDER);
				Collections.reverse(namesSorted);
			}

			for(int k=0; k<names.size(); k++){

				log.debug("Names:" + names.get(k)+ " Names sorted:  "+ namesSorted.get(k));
				String s = names.get(k).toString().trim();
				String s2 = namesSorted.get(k).toString().trim();

				if(!(s.equalsIgnoreCase(s2)))
				{
					result=false;
					break;
				}
				else
					result=true;
			}

			if(result)
				return "Pass";
			else
				return "Fail";

		}catch(Throwable t)
		{
			log.debug("Error while executing  sortByTitle- "+ t.getMessage());
			return "Fail";
		}

	}

	public String searchFiltersDateCheck(){
		log.debug("=============================");
		log.debug("Executing searchFiltersDateCheck");
		try {		
			WebElement we = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			String fromDateString = we.getAttribute("value");
			log.debug("From Date Picked : "+fromDateString);

			WebElement we1 = driver.findElement(By.xpath(OR.getProperty(objectArr[1])));
			String toDateString = we1.getAttribute("value");
			log.debug("To Date Picked : "+toDateString);

			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");

			Calendar from = Calendar.getInstance();
			from.setTime(sdf.parse(fromDateString));
			from.add(Calendar.MONTH,1);

			Calendar to = Calendar.getInstance();
			to.setTime(sdf.parse(toDateString));


			/*Date fromDate = formatter.parse(fromDateString);
		Date toDate = formatter.parse(toDateString);*/

			if(to.equals(from) || to.equals(Calendar.getInstance()) )
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing  searchFiltersDateCheck- "+ t.getMessage());
			return "Fail";
		}
	}

	public String verifyCheckboxUnchecked(){
		log.debug("=============================");
		log.debug("Executing verifyCheckboxUnchecked");
		try{
			WebElement element = getWebElement(OR, objectArr[0]);
			String value = element.getAttribute("checked");

			if(value!=null){
				//unchecking the checkbox
				element.click();
			}
			return "Pass";
		}catch(Throwable t){
			log.debug("Error while executing verifyCheckboxUnchecked "+t.getMessage());
			return "Fail";
		}
	}


	public String verifyCount_psuedoPlaylist(){
		log.debug("=============================");
		log.debug("Executing verifyCount_psuedoPlaylist");
		//this keyword reduces the number of psuedoPlaylists to two
		try{
			String size = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			int sizeInt = Integer.parseInt(size);
			for(int i=sizeInt ; i>2; i-- ){
				driver.findElement(By.xpath("//div[contains(@class,'x-panel-body x-panel-body-noheader')]/descendant::div[contains(@class,'x-panel')]/descendant::button[contains(text(),'-')][" + i + "]")).click();
			}
			return "Pass";
		} catch (Throwable t) {
			// report error
			log.debug("Error while Executing verifyCount_psuedoPlaylis -" + t.getMessage());    
			return "Fail";
		}

	}

	public String isElementPresentInList(){
		log.debug("====================================");
		log.debug("Executing isElementPresentInList ");
		// the keyword finds the absence of element in a list
		String searchName= testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
		boolean flag= false;
		log.debug(searchName);
		try{
			List<WebElement> rows =  getWebElements(OR,objectArr[0]);

			String rowsLocator = OR.getProperty(objectArr[0]);
			String dragThumbLocator = OR.getProperty(objectArr[1]);
			String totalRowCount = driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).getText();
			int totalRowInt = Integer.parseInt(totalRowCount);

			if(totalRowInt > 14)
			{
				Functions.dragTillAllRowsLoaded(driver, log, rowsLocator, dragThumbLocator, totalRowInt, 100);
			}

			for (WebElement item : rows) {
				if (item.getAttribute("data-title").trim().equals(searchName)){
					item.click();
					Thread.sleep(1000);
					flag=false;
					log.debug("Found search is" + searchName );
				}
			}

			if(!flag)
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing isElementPresentInList-" + t.getMessage());
			return "Fail";
		}
	}

	public String compareColor()
	{
		//this keyword verifies the actual colour with a given expected colour
		log.debug("==============================================");
		log.debug("Executing compareColor Keyword");
		try {

			String expectedColor=testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			log.debug("Expected Color: " + expectedColor);
			System.out.println(objectArr[0] + " :  expected Color: " + expectedColor);
			String actualColor = getWebElement(OR,objectArr[0]).getCssValue("Color");
			String actualColorHex = Color.fromString(actualColor).asHex().toUpperCase();
			log.debug("Actual Color: " + actualColorHex);
			System.out.println(objectArr[0] + " :  actual Color: " + actualColorHex);
			if(actualColorHex.trim().equalsIgnoreCase(expectedColor.trim()))
			{
				return "Pass";
			}

			else
			{
				return "Fail";
			}


		}
		catch(Throwable t)
		{
			log.debug("Error while executing compareColor -"+ objectArr[0] + t.getMessage());
			return "Fail";
		}		
	}

	public String setDataByValue(){
		//This keyword sets the value attribute of an element into the test sheet
		log.debug("====================================");
		log.debug("Executing setDataByValue");
		try{
			WebElement element = getWebElement(OR, objectArr[0]);
			String actualValue = element.getAttribute("value");

			log.debug("Value is" + actualValue);
			testData.setCellData(currentTest, data_column_nameArr[0], testRepeat, actualValue);
			log.debug("Value set in excel is"+testData.getCellData(currentTest, data_column_nameArr[0], testRepeat));
			return "Pass";

		}catch(Throwable t){
			log.debug("Error in setDataByValue- "+t.getMessage());
			return "Fail";
		}
	}

	public String verifyPath(){
		//This keyword verifies path of a document by using the test data sheet
		log.debug("==============================================");
		log.debug("Executing verifyPath Keyword");
		try{
			String expectedPath=testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			WebElement element = getWebElement(OR, objectArr[0]);
			String actualPath=element.getAttribute("data-href");

			if(expectedPath.trim().equals(actualPath.trim()))
				return "Pass";
			else
				return "Fail";
		}catch(Throwable t){
			log.debug("Error while executing verifyPath "+t.getMessage());
			return "Fail";
		}
	}

	public String verifyHelpImage(){
		log.debug("====================================");
		log.debug("Executing verifyHelpImage");
		String imageFileName;
		try{
			imageFileName = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			imageFileName = imageFileName+".png";
			log.debug("Input Image File Name: "+imageFileName);
		}catch(Throwable e){
			log.debug("Error while executing verifyHelpImage- TestData value is Null. "+"\n Stack Trace: \n"+e.getMessage());
			return "Fail";
		}
		try{
			WebElement element = getWebElement(OR, objectArr[0]);
			Functions.downloadImage(driver, log, element, imageFileName, "png");

			boolean ret1 = false;
			boolean ret2 = false;

			String image1 = System.getProperty("user.dir")+"/images/"+imageFileName;
			log.debug("Actual Image Path: "+image1);
			BufferedImage originalImage = ImageIO.read(new File(image1));
			String image2 = System.getProperty("user.dir")+"/inputImages/"+imageFileName;
			log.debug("Input Image Path: "+image2);
			BufferedImage inputImage = ImageIO.read(new File(image2));

			Raster ras1 = originalImage.getData();
			log.debug("Raster for Image1: "+ras1);
			Raster ras2 = inputImage.getData();
			log.debug("Raster for Image2: "+ras2);
			//Comparing the the two images for number of bands,width & height.
			if (ras1.getNumBands() == ras2.getNumBands()
					&& ras1.getWidth() == ras2.getWidth()
					&& ras1.getHeight() == ras2.getHeight()) {
				ret1=true;
			}
			// Once the band ,width & height matches, comparing the images.
			search: for (int i = 0; i < ras1.getNumBands(); ++i) {
				for (int x = 0; x < ras1.getWidth(); ++x) {
					for (int y = 0; y < ras1.getHeight(); ++y) {
						if (ras1.getSample(x, y, i) == ras2.getSample(x, y, i)) {
							ret2 = true;
							break search;
						}
					}
				}
			}

			log.debug("Net Result Value: "+ret1);
			if (ret1 && ret2) 
				return "Pass";
			else{
				log.debug("Images are not Same.");
				return "Fail";
			}
		}catch(Throwable t){
			log.debug("Error while executing verifyHelpImage. Object:  " + objectArr[0] +"\n Stack Trace: \n"+t.getMessage());
			return "Fail";
		}
	}

	public String clickAtCoordinate(){
		log.debug("==============================================");
		log.debug("Executing clickAtCoordinate Keyword");
		try{
			WebElement toElement = getWebElement(OR, objectArr[0]);
			Actions builder = new Actions(driver);
			builder.moveToElement(toElement, 1, 1).click().perform();

			return "Pass";
		}catch(Throwable t){
			log.debug("Error while executing clickAtCoordinate "+t.getMessage());
			return "Fail";
		}
	}

	public String pressEscape() {
		log.debug("=============================");
		log.debug("Executing pressEscape Keyword");
		// extract the test data
		try {
			Thread.sleep(WAIT2SEC);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.ESCAPE).build().perform();
			Thread.sleep(WAIT2SEC);
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing pressEscape -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String pressBackSpace() {
		log.debug("=============================");
		log.debug("Executing pressBackSpace Keyword");
		// extract the test data
		try {
			Thread.sleep(WAIT2SEC);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.BACK_SPACE).build().perform();
			Thread.sleep(WAIT2SEC);
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing pressBackSpace -" + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}
	
	public String pressEnd() {
		log.debug("=============================");
		log.debug("Executing pressEnd Keyword");
		// extract the test data
		try {
			Thread.sleep(WAIT2SEC);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.END).build().perform();
			Thread.sleep(WAIT2SEC);
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing pressEscape -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}
		return "Pass";
	}
	
	
	public String verifyTimeDifference(){
		log.debug("==============================================");
		log.debug("Executing verifyTimeDifference Keyword");
		String Flag=null;
		String Start =testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
		String Stop = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
		try{
			Flag = testData.getCellData(currentTest, data_column_nameArr[2],testRepeat);
		}catch (Exception t) {
			log.debug("Error while executing verifyTimeDifference -"+ t.getMessage());

		}

		String Start1 = Start.split("T")[0];
		String Start2 = (Start.split("T")[1]).split("\\.")[0];
		String dateStart = Start1 + " " +Start2;
		String Stop1 = Stop.split("T")[0];
		String Stop2 = Stop.split("T")[1].split("\\.")[0];
		String dateStop = Stop1 + " " +Stop2;

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Date d1 = null;
			Date d2 = null;

			d1 = format.parse(dateStart);
			d2 = format.parse(dateStop);

			//in milliseconds
			long diff = d2.getTime() - d1.getTime();
			log.debug("Time Difference is: " + diff);

			if (diff>0 && Flag==null){
				return "Pass";
			}
			else if (diff==0 && Flag.equalsIgnoreCase("FALSE")){
				return "Pass";
			}
			else {
				log.debug("Time Difference is: " + diff+ "  Please Debug");
				return "Fail";
			}

		} catch (Exception e) {
			log.debug("Error while executing verifyTimeDifference -"+ e.getMessage());
			return "Fail";
		}

	}

	public String removeAllLenses(){
		log.debug("====================================");
		log.debug("Executing setDataByValue");
		try{

			List<WebElement> checkedLens = driver.findElements(By.xpath(OR.getProperty(objectArr[0])));

			for(int i=1;i<=checkedLens.size();i++){
				getWebElement(OR, objectArr[0]).click();
			}

			return "Pass";

		}catch(Throwable t){
			log.debug("Error in compareInputData- "+t.getMessage());
			return "Fail";
		}
	}

	public String dragTill(){

		log.debug("=============================");
		log.debug("Executing dragTill");
		try {
			String rowsLocator = OR.getProperty(objectArr[0]);
			String dragThumbLocator = OR.getProperty(objectArr[1]);

			String data = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			int count = Integer.parseInt(data);
			Functions.dragTillAllRowsLoaded(driver, log, rowsLocator, dragThumbLocator, count, 100);
			return "Pass";
		}
		catch(Throwable t)
		{
			log.debug("Error while executing dragTill -" + t.getMessage());    
			return "Fail";
		}

	}
	public String selectFirstNLens()
	{
		log.debug("=============================");
		log.debug("Executin selectFirstNLens");

		boolean result = false;
		try {

			String whatToDoString=objectArr[0];
			int whatToDo=0;
			List<WebElement> lensesOnOverlay=null;
			String saveButtonXpath=null;
			String elementString=null;

			if(whatToDoString.equalsIgnoreCase("aims.firstSix")){
				whatToDo=1;
				saveButtonXpath="aims.Portfolio.ViewMoreLenseSave";
				lensesOnOverlay = driver.findElements(By.xpath("//*[@id='lens_settings_overlay']/descendant::div[@class='overview']/ul/li"));
				elementString = "//*[@id='lens_settings_overlay']/descendant::div[@class='overview']/ul/li";
			}
			else if(whatToDoString.equalsIgnoreCase("aims.firstEight")){
				whatToDo=2;
				saveButtonXpath="aims.Portfolio.ViewMoreLenseSave";
				lensesOnOverlay = driver.findElements(By.xpath("//*[@id='lens_settings_overlay']/descendant::div[@class='overview']/ul/li"));
				elementString = "//*[@id='lens_settings_overlay']/descendant::div[@class='overview']/ul/li";
			}


			log.debug("What to do is   :" + whatToDoString );
			log.debug(lensesOnOverlay.size());

			for(int i=1; i<=lensesOnOverlay.size(); i++){

				WebElement e = driver.findElement(By.xpath(elementString+"["+i+"]"));
				if(e.getAttribute("class").contains("checked")){
					e.click();
				}

			}
			if(whatToDo==1){
				for(int k=0; k<6; k++){
					lensesOnOverlay.get(k).click();
					Thread.sleep(2000);
					result = true;
				}
				log.debug("All six lenses are selected.");
			}

			else if(whatToDo==2){

				for(int k=0; k<8; k++){
					lensesOnOverlay.get(k).click();
					Thread.sleep(2000);
					result = true;
				}
				log.debug("All eight lenses are selected.");
			}
			else
			{
				System.out.println("Invalid object specified!!");
			}
			driver.findElement(By.xpath(OR.getProperty(saveButtonXpath))).click();  // click Save button
			Thread.sleep(WAIT3SEC);

			if(result)
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			// report error
			driver.findElement(By.xpath(OR.getProperty("aims.Portfolio.ViewMoreLenseSave"))).click();  // click Save button
			log.debug("Error while executing selectFirstNLens -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}

	}

	public String runPdfComparison() {
		log.debug("=============================");
		log.debug("Executing runPdfComparison");
		try {
			File exepath, path;
			String cmd;
			exepath = new File(System.getProperty("user.dir")+"/dependencies/PDFComparison");
			path= new File(System.getProperty("user.dir")+"/dependencies");
			cmd = "cmd.exe /c start compare.bat "+path.toString();
			Runtime.getRuntime().exec(cmd , null, exepath);
			Thread.sleep(WAIT5SEC);
			return "Pass";
		}catch (Throwable t) {
			log.debug("Error while executing runPdfComparison" + t.getMessage());
			return "Fail";
		}
	}

	public String verifyTitle(){
		log.debug("=============================");
		log.debug("Executing verifyTitle");

		String expectedTitle, actualTitle;
		Boolean flag = true;

		try{
			expectedTitle = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			actualTitle = driver.getTitle();

			try{
				String append = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
				expectedTitle = expectedTitle+append;
			}
			catch(Throwable t){
				//do nothing
			}

			log.debug("Expected Title :"+expectedTitle);
			log.debug("Actual Title :"+actualTitle);

			if(testBrowser.contains("InternetExplorer"))
			{
				if(actualTitle.contains(expectedTitle))
				{
					log.debug("Values are equal.");
				}
				else{
					log.debug("Values are not equal.");
					flag = false;
				}

			}
			else
			{
				if(expectedTitle.equalsIgnoreCase(actualTitle)){
					log.debug("Values are equal.");
				}
				else{
					log.debug("Values are not equal.");
					flag = false;
				}
			}
			if(flag)
				return "Pass";
			else
				return "Fail";

		}catch(Throwable t){
			log.debug("Error while executing verifyTitle -" + objectArr[0]+ t.getMessage());
			return "Fail";
		}	
	}

	public String verifyPdfNameAndRename(){
		log.debug("=============================");
		log.debug("Executing verifyPdfNameAndRename");

		String fileName,replaceString = "", replaceByString ="", dir, temp1 = "", temp2 = "", concatString, newFileName, expectedFileName, dirPath, flag = "true";
		boolean result = false;	
		File file;
		File[] files;
		try{

			dir = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);

			try{
				temp1 = testData.getCellData(currentTest, data_column_nameArr[6],testRepeat);
				replaceString = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
				replaceByString = testData.getCellData(currentTest, data_column_nameArr[4],testRepeat);
				temp2 = testData.getCellData(currentTest, data_column_nameArr[5],testRepeat);
				flag = testData.getCellData(currentTest, data_column_nameArr[7],testRepeat);

			}catch(Throwable e){
				log.debug("Do Nothing");
				//do nothing
			}

			concatString = testData.getCellData(currentTest, data_column_nameArr[2],testRepeat);
			newFileName = testData.getCellData(currentTest, data_column_nameArr[3],testRepeat);

			log.debug("Replace String: "+replaceString);
			log.debug("Concat String: "+concatString);
			log.debug("NewFileName : "+newFileName);
			dirPath = System.getProperty("user.dir")+dir; 
			log.debug("Directory Path: "+dirPath);

			if(!temp2.equals("")){
				concatString = temp2.trim().replaceAll(replaceString, replaceByString).concat(concatString);	
			}
			else{
				concatString = temp1.replaceAll(replaceString, replaceByString).trim().concat("_").concat(concatString.trim().replaceAll(" ", "+"));
			}
			Thread.sleep(WAIT2SEC);
			expectedFileName = concatString.concat(".pdf");
			log.debug("Expected File Name: "+expectedFileName);

			file = new File(dirPath);
			files = file.listFiles();
			for (File f : files) {
				if(f.exists()){
					if(!f.getName().contains("SitePrinting") && f.getName().contains(".pdf")){
						fileName = f.getName();

						if(fileName.equals(expectedFileName))
							result = true;

						log.debug("FileName to be renamed is: "+fileName);  
						Functions.renameFile(dirPath, newFileName, f, log);
						Thread.sleep(WAIT2SEC);
					}
				}
			}
			if(flag.equalsIgnoreCase("true")){
				if(result)
					return "Pass";
				else
					return "Fail";
			}else{
				if(result)
					return "Fail";
				else
					return "Pass";
			}

		}catch(Throwable t)
		{
			log.debug("Error while executing verifyPdfNameAndRename -"+ t.getMessage());
			return "Fail";
		}	
	}
/*
	public String convertAndComparePdfFiles() {
		log.debug("=============================");
		log.debug("Executing convertAndComparePdfFiles");

		String fileName = null, tempDirTest,file1 = null,file2 = null,tempDirStage,testDirectory,stageDirectory;
		boolean result = true;
		File tmpFile1, tmpFile2, fileTemp1, fileTemp2, temp1, temp2;
		BufferedImage tempImage1, tempImage2;
		File[] files1, files2;

		try{

			fileName = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			log.debug("FileName: "+fileName);

			stageDirectory = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
			testDirectory = testData.getCellData(currentTest, data_column_nameArr[2],testRepeat);

			file1 = System.getProperty("user.dir")+stageDirectory+fileName;
			file2 = System.getProperty("user.dir")+testDirectory+fileName;

			tempDirStage = System.getProperty("user.dir")+"/dependencies/tempDirStage";
			tmpFile1 = new File(tempDirStage);
			tmpFile1.mkdir();

			tempDirTest = System.getProperty("user.dir")+"/dependencies/tempDirTest";
			tmpFile2 = new File(tempDirTest);
			tmpFile2.mkdir();

			Functions.convertPdfToImage(file1, log, "/dependencies/tempDirStage");
			Functions.convertPdfToImage(file2, log, "/dependencies/tempDirTest");

			fileTemp1 = new File(tempDirStage);
			files1 = fileTemp1.listFiles();

			fileTemp2 = new File(tempDirTest);
			files2 = fileTemp2.listFiles();

			if(files1.length != files2.length){
				result = false;
				log.debug("No. of pages in the pdfs are different.");
			}
			else{
				for(int i=0;i<files1.length;i++){
					if(result){
						temp1 = files1[i];
						temp2 = files2[i];
						tempImage1 = ImageIO.read(temp1);
						tempImage2 = ImageIO.read(temp2);
						log.debug("ImageFile1 at "+i+" :"+temp1);
						log.debug("ImageFile2 at "+i+" :"+temp2);
						result = Functions.compareTwoImages(tempImage1, tempImage2, log);
					}
				}
			}
			FileUtils.deleteDirectory(tmpFile1);
			FileUtils.deleteDirectory(tmpFile2);

			log.debug("ImageFiles1 Deleted from: "+tmpFile1);
			log.debug("ImageFiles2 Deleted from: "+tmpFile2);
			log.debug("Result: "+result);

			if(result){
				return "Pass";
			}
			else{
				return "Fail";
			}
		}catch (Throwable t) {
			log.debug("Error while executing convertAndComparePdfFiles" + t.getMessage());
			return "Fail";
		}
	}

*/	public String verifyDynamicSearch(){
		log.debug("=============================");
		log.debug("Executing verifyDynamicSearch");
		List<WebElement> search;
		boolean result=false;
		String searchInput, attribute;
		try
		{
			search = driver.findElements(By.xpath(OR.getProperty(objectArr[0])));
			searchInput = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			attribute = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
			for (WebElement item : search){
				if(!item.getAttribute(attribute).contains(searchInput)){
					result = false;
					break;
				}
				else
					result = true;
			}
			if(result)
				return "Pass";
			else
				return "Fail";

		}catch(Throwable t)
		{
			log.debug("Error while executing  verifyDynamicSearch- "+ t.getMessage());
			return "Fail";
		}
	}

	public String trimAndVerifyText()
	{
		log.debug("=============================");
		log.debug("Executing trimAndVerifyText");
		String actualText,expectedText;

		try
		{
			WebElement element = getWebElement(OR,objectArr[0]);
			expectedText = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			actualText = element.getText().replaceAll("\n", " ");

			log.debug("Actual Text Trimmed : " + actualText.trim());
			log.debug("Expected Text Trimmed : " + expectedText.trim());

			if(actualText.trim().equals(expectedText.trim()))
				return "Pass";
			else
				return "Fail";
		}
		catch(Throwable t)
		{
			log.debug("Error while executing trimAndVerifyText" + t.getMessage());
			return "Fail";

		}
	}

	public String verifyDropdownSize(){
		log.debug("=============================");
		log.debug("Executing verifyDropdownSize");
		List<WebElement> search;
		boolean result=false;
		String expectedSize;
		int size,expectedIntSize;
		try
		{
			search = driver.findElements(By.xpath(OR.getProperty(objectArr[0])));
			size = search.size();
			expectedSize = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			expectedIntSize = Integer.parseInt(expectedSize);
			if(expectedIntSize == size)
				result = true;

			if(result)
				return "Pass";
			else
				return "Fail";

		}catch(Throwable t)
		{
			log.debug("Error while executing  verifyDropdownSize- "+ t.getMessage());
			return "Fail";
		}
	}

	public String verifyWorkspaceExcelNameAndRename(){
		log.debug("=============================");
		log.debug("Executing verifyWorkspacePdfNameAndRename");
		String fileName,dir,name,temp,concatString,newFileName,expectedFileName,dirPath,replaceString = "", replaceByString ="";
		boolean result = false;
		File tmpFile = null;
		try
		{
			dir = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			temp = getWebElement(OR, objectArr[0]).getText();
			replaceString = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
			replaceByString = testData.getCellData(currentTest, data_column_nameArr[2],testRepeat);
			name=temp.trim().replaceAll(replaceString, replaceByString);
			testData.setCellData(currentTest, data_column_nameArr[5], testRepeat, name);
			concatString = testData.getCellData(currentTest, data_column_nameArr[3],testRepeat);
			newFileName = testData.getCellData(currentTest, data_column_nameArr[4],testRepeat);
			expectedFileName = name.concat(concatString);
			log.debug("NewFileName : "+newFileName);
			log.debug("Expected File Name: "+expectedFileName);
			dirPath = System.getProperty("user.dir")+dir; 
			log.debug("Directory Path: "+dirPath);
			File file = new File(dirPath);
			File[] files = file.listFiles();
			for (File f : files) {
				if(f.exists()){
				if(!(f.getName().contains("ExcelDownload") || f.getName().contains("svn"))){
						fileName = f.getName();
						if(fileName.equals(expectedFileName)){
							result = true; 
							log.debug("Name of downloaded file is as expected name");
						}
						log.debug("FileName to be renamed is: "+fileName);
						tmpFile = new File(dirPath+"/"+newFileName);
						log.debug("File Renamed at: "+tmpFile);
						f.renameTo(tmpFile);
					}
				} 
			}
			if(result)
				return "Pass";
			else
				return "Fail";

		}catch(Throwable t)
		{
			log.debug("Error while executing verifyWorkspaceExcelNameAndRename -"+ t.getMessage());
			return "Fail";
		}	
	}

	public String mergeAndReplaceStrings() {
		log.debug("=============================");
		log.debug("Executing  mergeAndReplaceStrings");
		String firstString ,secondString ,thirdString ,fourthString,finalString,fifthString=null,replacedString=null;
		try {

			firstString= testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			secondString = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
			thirdString = testData.getCellData(currentTest, data_column_nameArr[2], testRepeat);
			fourthString = testData.getCellData(currentTest, data_column_nameArr[3], testRepeat);
			try{
				fifthString = testData.getCellData(currentTest, data_column_nameArr[4], testRepeat);
			}catch (Throwable t){
				fifthString = null;
			}
			if(!fifthString.equals(null) && !fifthString.isEmpty()){
				replacedString= fifthString.replaceAll(" ", "_").trim();
				finalString= firstString.concat(secondString).concat(replacedString).concat(secondString).concat(thirdString).concat(fourthString);
				log.debug("String after concatenation is: " + finalString);
				testData.setCellData(currentTest, data_column_nameArr[5], testRepeat, finalString);
				log.debug("Value set in excel is"+testData.getCellData(currentTest, data_column_nameArr[5], testRepeat));
			}
			else{
				finalString= firstString.concat(secondString).concat(thirdString).concat(fourthString);
				log.debug("String after concatenation is: " + finalString);
				testData.setCellData(currentTest, data_column_nameArr[5], testRepeat, finalString);
				log.debug("Value set in excel is"+testData.getCellData(currentTest, data_column_nameArr[5], testRepeat));
			}

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing mergeAndReplaceStrings -" + t.getMessage());    
			return "Fail - test Data Not Found";
		}
		return "Pass";  
	}


	public String verifyWorkspaceLenseExcelName(){
		log.debug("=============================");
		log.debug("Executing verifyWorkspaceLenseExcelName");
		String fileName,expectedFileName,newFileName,dirPath,dir;
		boolean result = false;
		File tmpFile = null;
		try
		{
			dir = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			expectedFileName = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
			newFileName = testData.getCellData(currentTest, data_column_nameArr[2],testRepeat);
			log.debug("NewFileName : "+newFileName);
			log.debug("Expected File Name: "+expectedFileName);
			dirPath = System.getProperty("user.dir")+dir; 
			log.debug("Directory Path: "+dirPath);
			File file = new File(dirPath);
			File[] files = file.listFiles();
			for (File f : files) {
				if(f.exists()){
					if(!(f.getName().contains("ExcelDownload") || f.getName().contains("svn"))){
						fileName = f.getName();
						log.debug("File name is: " + f.getName());
						if(fileName.equals(expectedFileName)){
							result = true;
							log.debug("Name of downloaded file is as expected name");
						}
						log.debug("FileName to be renamed is: "+fileName);
						tmpFile = new File(dirPath+"/"+newFileName);

						log.debug("File Renamed at: "+tmpFile);
						f.renameTo(tmpFile);
					}
				} 
			}
			if(result)
				return "Pass";
			else
				return "Fail";

		}catch(Throwable t)
		{
			log.debug("Error while executing verifyWorkspaceLenseExcelName -"+ t.getMessage());
			return "Fail";
		}	
	}


	public String compareExcel(){
		log.debug("=============================");
		log.debug("Executing compareExcel");
		//this keyword compares two excel files
		String fileName = null,file1 = null,file2 = null,testDirectory,stageDirectory;
		File myFile,myFile2;
		FileInputStream fis,fis2;
		XSSFWorkbook myWorkBook,myWorkBook2;
		XSSFSheet mySheet,mySheet2;
		Iterator<Row> rowIterator,rowIterator2;
		boolean flag = false;
		try{

			fileName = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			log.debug("FileName: "+fileName);

			stageDirectory = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
			testDirectory = testData.getCellData(currentTest, data_column_nameArr[2],testRepeat);

			file1 = System.getProperty("user.dir")+stageDirectory+fileName;
			file2 = System.getProperty("user.dir")+testDirectory+fileName;

			myFile = new File(file1);
			fis = new FileInputStream(myFile);
			myFile2 =  new File (file2 );
			fis2 = new FileInputStream(myFile2);

			myWorkBook = new XSSFWorkbook (fis);
			myWorkBook2 = new XSSFWorkbook (fis2);

			mySheet = myWorkBook.getSheetAt(0);
			mySheet2 = myWorkBook2.getSheetAt(0);

			rowIterator = mySheet.iterator();
			rowIterator2 = mySheet2.iterator();

			while (rowIterator.hasNext() && rowIterator2.hasNext()){
				Row row1 = rowIterator.next();
				Row row2 = rowIterator2.next();
				Iterator<Cell> cellIterator1 = row1.cellIterator();
				Iterator<Cell> cellIterator2 = row2.cellIterator();

				while (cellIterator1.hasNext() && cellIterator2.hasNext()){
					Cell cell1 = cellIterator1.next();
					Cell cell2 = cellIterator2.next();

					switch (cell1.getCellType()){

					case Cell.CELL_TYPE_STRING:
						if(cell1.getStringCellValue().equals(cell2.getStringCellValue())){
							flag=true;
							log.debug(cell1.getStringCellValue() + "\t");
						}else{
							log.debug("Failed at: " + cell1.getColumnIndex() + " " + cell1.getRowIndex() + " Data:  " + cell1.getStringCellValue() + "\t");
							return "Fail: files are not same";
						}
						break;

					case Cell.CELL_TYPE_NUMERIC:
						if(cell1.getNumericCellValue() == cell2.getNumericCellValue()){
							flag=true;
							log.debug(cell1.getNumericCellValue() + "\t");
						}
						else{
							log.debug("Failed at: " + cell1.getColumnIndex() + " " + cell1.getRowIndex() + " Data:  " + cell1.getNumericCellValue() + "\t");
							return "Fail: files are not same";
						}
						break;

					case Cell.CELL_TYPE_BOOLEAN:
						if(cell1.getBooleanCellValue() == cell1.getBooleanCellValue()){
							flag=true;
							log.debug(cell1.getBooleanCellValue() + "\t");
						} else{
							log.debug("Failed at: " + cell1.getColumnIndex() + " " + cell1.getRowIndex() + " Data:  " + cell1.getBooleanCellValue() + "\t");
							return "Fail: files are not same";
						}
						break;

					default : log.debug("In default ");
					}
				}
			}

			log.debug("Flag is "  + flag);

			if(flag){
				log.debug("Files are same");
				return "Pass";
			}
			else{
				log.debug("Files are not same");
				return "Fail";
			}
		}catch(Throwable t){
			log.debug("Error while executing compareExcel -"+ t.getMessage());
			return "Fail";
		}
	}

	public String verifyTotalItemsBeforeLoad() {
		log.debug("=============================");
		log.debug("Executing verifyTotalItemsinDropdown");
		int expectedItems=0, actualItems=0;
		List<WebElement> listObject;
		String data;
		boolean result = false;
		try{
			data = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
			expectedItems = Integer.parseInt(data);
			listObject = driver.findElements(By.xpath(OR.getProperty(objectArr[0])));
			actualItems = listObject.size();
			if(expectedItems == actualItems)
				result = true;
			log.debug("expected no.of Items  -  " + expectedItems);
			log.debug("actual no. of Items  -  " + actualItems);
		} catch (Throwable t) {
			// error
			log.debug("Error in verifyTotalItemsinDropdown - " + objectArr[0]);
			log.debug("Actual - " + actualItems);
			log.debug("Expected -" + expectedItems);
			return "Fail";
		}
		if(result)
			return "Pass";
		else
			return "Fail";
	}	
	
	public String setClientId() {
		log.debug("=============================");
		log.debug("Executing setClientId");
		String attribute, value, clientName, clientId;
		String[] list1, list2;
		
		try {
			attribute = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
			value =  driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getAttribute(attribute);
			log.debug("Attribute is" + attribute);
			log.debug("Value is" + value);
			list1 = value.split("/");
			clientName = list1[list1.length - 1];
			list2 = clientName.split("_");
			clientId = list2[1]; 
			testData.setCellData(currentTest, data_column_nameArr[0], testRepeat, clientId);
			log.debug("Value set in excel is"+testData.getCellData(currentTest, data_column_nameArr[0], testRepeat));
		} catch (Throwable t) {
			// report error
			log.debug("Error while clicking on link -" + objectArr[0]
					+ t.getMessage());    
			return "Fail - Link Not Found";
		}
		return "Pass";  
	}
	
	public String setValueByAttribute() {
		log.debug("=============================");
		log.debug("Executing setValueByAttribute");
		String attribute, value;

		try {
			attribute = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
			value =  driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getAttribute(attribute);
			Thread.sleep(WAIT2SEC);
			testData.setCellData(currentTest, data_column_nameArr[0], testRepeat, value);
			log.debug("Value set in excel is"+testData.getCellData(currentTest, data_column_nameArr[0], testRepeat));
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing setValueByAttribute -" + objectArr[0]
					+ t.getMessage());    
			return "Fail - Link Not Found";
		}
		return "Pass";  
	}
	
	public String verifyScrollbarForListAndGrid() {
		log.debug("=============================");
		log.debug("Executing verifyScrollbarForListAndGrid Keyword");

		int listSize = 0;
		WebElement scrollbar;
		String listOrGrid = null;
		try {
			try {
				List<WebElement> elements = driver.findElements(By.xpath(OR.getProperty(objectArr[0])));
				listOrGrid = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
				listSize = elements.size();
				log.debug("listsize " + listSize);

				if(listSize == 0)
					return "Fail";
			}catch(Throwable t) {

			}
			if(listOrGrid.equalsIgnoreCase("List")){
				try {
					scrollbar = driver.findElement(By.xpath(OR.getProperty(objectArr[1])));
					if(listSize > 7){
						if(scrollbar.isDisplayed())
							return "Pass";
						else
							return "Fail";
					}
					else{
						if(scrollbar.isDisplayed())
							return "Fail";
						else
							return "Pass";
					}
				}catch(Throwable t){
					if(listSize < 7)
						return "Pass";
					else{
						log.debug("Debug required");
						return "Fail";
					}
				}
			}
			else if(listOrGrid.equalsIgnoreCase("Grid")){
				try {
					scrollbar = driver.findElement(By.xpath(OR.getProperty(objectArr[1])));
					if(listSize > 14){
						if(scrollbar.isDisplayed())
							return "Pass";
						else
							return "Fail";
					}
					else{
						if(scrollbar.isDisplayed())
							return "Fail";
						else
							return "Pass";
					}
				}catch(Throwable t){
					if(listSize < 14)
						return "Pass";
					else{
						log.debug("Debug required");
						return "Fail";
					}
				}
			}
			else if(listOrGrid.equalsIgnoreCase("PseudoPlaylist")){
				try {
					scrollbar = driver.findElement(By.xpath(OR.getProperty(objectArr[1])));
					if(listSize > 2){
						if(scrollbar.isDisplayed())
							return "Pass";
						else
							return "Fail";
					}
					else{
						if(scrollbar.isDisplayed())
							return "Fail";
						else
							return "Pass";
					}
				}catch(Throwable t){
					if(listSize <= 2)
						return "Pass";
					else{
						log.debug("Debug required");
						return "Fail";
					}
				}
			}
			else{
				log.debug("Wrong test data value");
				return "Fail";
			}
		}catch(Throwable t) {
			log.debug("Error while executing verifyScrollbarPresence " +  t.getMessage());
			return "Fail";
		}
	}


public String verifyScrollbarDragForList() {
	log.debug("=============================");
	log.debug("Executing 	verifyScrollbarDragForList Keyword");

	int listSize = 0 ,intExpectedCount ;
    String expectedCount ,scrollResult;
	WebElement scrollbar;
	expectedCount = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
	intExpectedCount = Integer.parseInt(expectedCount);
		List<WebElement> elements = driver.findElements(By.xpath(OR.getProperty(objectArr[1])));
		listSize = elements.size();
		log.debug("listsize " + listSize);

		if(listSize == 0)
			return "Fail";
		if(listSize > intExpectedCount){
		 try{
			 scrollbar = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			 
			if(scrollbar.isDisplayed()) {
			scrollResult = scrollDrag();
			if(scrollResult.equalsIgnoreCase("Pass"))
				return "Pass";
			}
		}catch(Throwable t){
			 log.debug("Scroll Bar not present"); 
		 }
		}
		else{
			try{
			scrollbar = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			}catch(Throwable t){
				log.debug("Results are less than seven so scroll bar is not present");
				return "Pass";
			}
		 }
			return "Fail: No Results";
	
	}

public String verifyDateDisplay()
{
	log.debug("=============================");
	log.debug("Executing verifyDateDisplay Keyword");
	String crxDate,date,actualDate,expectedDate;
	try {
		crxDate = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
		date = crxDate.split("T")[0];
		expectedDate = getWebElement(OR, objectArr[0]).getText();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("MMM dd yyyy");

		Date dateformatted = format1.parse(date);
		actualDate = format2.format(dateformatted);

		log.debug("Actual date from CRX : "+ actualDate);
		log.debug("Expected date : "+ expectedDate);

		if(actualDate.equals(expectedDate))
			return "Pass";
		else
			return "Fail";		    

	}catch(Throwable t)
	{
		log.debug("Error while executing verifyDateDisplay " +  t.getMessage());
		return "Fail";
	}
}
public String compareSize(){
	log.debug("=============================");
	log.debug("Executing compareSize");
	String size;
	int expectedSize;
	try
	{
		List<WebElement> playList = (List<WebElement>) getWebElements(OR, objectArr[0]);
		int actualSize = playList.size();
		size = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
		expectedSize = Integer.parseInt(size);
		
		log.debug("Expected size : "+expectedSize);
		log.debug("Actual size : "+actualSize);
		
		if(expectedSize == actualSize)
			return "Pass";
		else
			return "Fail";
	}catch(Throwable t)
	{
		log.debug("Error while executing  compareSize- "+ t.getMessage());
		return "Fail";

	}
}


public String verifySorting() {
	log.debug("=============================");
	log.debug("executing keyword verifySorting");
	List<String> names=new ArrayList<String>();
	List<String> namesSorted=new ArrayList<String>();
	String ascOrDesc = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
	String attribute = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
	boolean result=false;
	String value = null;

	try {
		Thread.sleep(WAIT2SEC);
		List<WebElement> search = getWebElements(OR,objectArr[0]);

		if(search.size() == 0)
		{
		log.debug("List size is zero");
		return " Fail";
		}
		for (WebElement item : search) {
			value = item.getAttribute(attribute).trim();
			log.debug("unsorted value is : " + value );
			if(! value.isEmpty())
				names.add(value);
		}

		namesSorted.addAll(names);

		if(value.contains("$")) {
			//for sort by AUM
			result = Functions.sortAum(names, names.size(), ascOrDesc);

			if(result==true)
				return "Pass";
			else
				return "Fail";
		}

		log.debug(names.size());
		log.debug(namesSorted.size());
		if(ascOrDesc.equals("asc"))
			Collections.sort(namesSorted, String.CASE_INSENSITIVE_ORDER);
		else {
			Collections.sort(namesSorted, String.CASE_INSENSITIVE_ORDER);
			Collections.reverse(namesSorted);
		}

		for(int k=0; k<names.size(); k++){

			log.debug("Names:" + names.get(k)+ " Names sorted:  "+ namesSorted.get(k));
			String s = names.get(k).toString().trim();
			String s2 = namesSorted.get(k).toString().trim();

			if(!(s.equalsIgnoreCase(s2)))
			{
				result=false;
				break;
			}
			else
				result=true;
		}

		if(result==true)
			return "Pass";
		else
			return "Fail";

	} catch (Throwable t) {
		//		 report error
		log.debug("Error while executing sortGeneric -" + objectArr[0]+ t.getMessage());
		return "Fail";
	}
}

public String verifyListText(){
	log.debug("===============================");
	log.debug("Executing verifyListText");
	
	String date;
	List<WebElement> searchResults;
	int actualCount;
	boolean status = false;
	try{
		date = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
		searchResults = getWebElements(OR,objectArr[0]);
		actualCount = searchResults.size();
		
		log.debug("Search Text: "+date);
		log.debug("Expected Count: "+actualCount);
		
		for(int i=0; i<actualCount; i++){
			log.debug("Data-title: "+searchResults.get(i).getText());
			if((searchResults.get(i).getText().contains(date)))
				status = true;
		}
		if(status)
			return "Pass";
		else
			return "Fail";
	}catch(Throwable t){
		log.debug("Error while executing verifyListText -" + objectArr[0]+t.getMessage());
		return "Fail";
	}
}

public String launchWebpage()
{
	log.debug("=============================");
	log.debug("executing keyword launchWebpage");

	String currentTitle = null;
	DesiredCapabilities cap = null;


	if((objectArr[0]).contains("author")) {
		launchBrowser="Chrome";
	}else if((objectArr[0]).contains("crx")) {
		launchBrowser="Firefox";
	}else {
		launchBrowser=testBrowser;
	}

	if (launchBrowser.equalsIgnoreCase("Firefox")) {

		String dirPath;
		try{
			String dir = testData.getCellData(currentTest, data_column_nameArr[2],testRepeat);
			dirPath = System.getProperty("user.dir")+dir;
		}catch(Throwable t){
			dirPath = System.getProperty("user.dir")+"\\dependencies";
		}
		log.debug("Download Directory Path: "+dirPath);
		FirefoxProfile profile = new FirefoxProfile();

		profile.setPreference("geo.prompt.testing", true);
		profile.setPreference("geo.prompt.testing.allow", true);

		if(runModule.contains("sitePrinting") || runModule.contains("excelDataDownload")){
			profile.setAcceptUntrustedCertificates(true);
			profile.setPreference("browser.download.folderList", 2);
			profile.setPreference("browser.download.manager.showWhenStarting", false);
			profile.setPreference("browser.download.dir",dirPath);
			profile.setPreference("browser.helperApps.neverAsk.saveToDisk","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/ms-excel,application/pdf,application/csv,text/csv,application/zip, application/x-zip, application/x-zip-compressed, application/download, application/octet-stream,application/vnd.ms-excel,application/xlsx");
			profile.setPreference("browser.download.manager.showAlertOnComplete", false);
			profile.setPreference("browser.download.panel.shown", false);
			profile.setPreference("pdfjs.disabled", true);
		}

		log.debug("inside navigate firefox");
		cap = DesiredCapabilities.firefox();
		cap.setBrowserName("firefox");
		cap.setCapability(FirefoxDriver.PROFILE, profile);

	}else if (launchBrowser.equalsIgnoreCase("InternetExplorer")){
		log.debug("webdriver.ie.driver: "+System.getProperty("user.dir")+"/drivers/IEDriverServer.exe");
		System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"/drivers/IEDriverServer.exe");
		log.debug("inside navigate IE");
		cap = DesiredCapabilities.internetExplorer();
		//cap.setBrowserName("iexplore");
		cap.setPlatform(Platform.WINDOWS);
		cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		cap.setCapability("enablePersistentHover", false);
		//				cap.setCapability("requireWindowFocus", true);
		cap.setCapability("ignoreProtectedModeSettings", true);
		cap.setCapability("ie.ensureCleanSession", true); 

	}else if (launchBrowser.equalsIgnoreCase("Chrome")){

		log.debug("inside navigate chrome");
		cap = DesiredCapabilities.chrome();
		cap.setBrowserName("chrome");

		String chromeDriver;
		if(System.getProperty("os.name").equals("Mac OS X")) {
			cap.setPlatform(Platform.MAC);
			chromeDriver = "chromedriver";
		}else {
			cap.setPlatform(Platform.WINDOWS);
			chromeDriver = "chromedriver.exe";

			log.debug("webdriver.chrome.driver: "+System.getProperty("user.dir")+"/drivers/"+chromeDriver);
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/drivers/"+chromeDriver);
		}

		ChromeOptions options = new ChromeOptions();
		String dirPath;
		try{
			String dir = testData.getCellData(currentTest, data_column_nameArr[3],testRepeat);
			dirPath = System.getProperty("user.dir")+dir;
		}catch(Throwable t){
			dirPath = System.getProperty("user.dir")+"/dependencies";
		}
		log.debug("Download Directory Path: "+dirPath);
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("download.default_directory", dirPath);

		options.addArguments("--silent");
		options.addArguments("--disable-extensions");
		options.addArguments("test-type");
		options.addArguments("start-maximized");		
		options.setExperimentalOption("prefs", prefs);
		cap.setCapability(ChromeOptions.CAPABILITY, options);

	}else if (launchBrowser.equalsIgnoreCase("Safari")){
		cap = DesiredCapabilities.safari();
		cap.setBrowserName("safari");
		cap.setPlatform(Platform.MAC);
	}

	log.debug("Url: "+CONFIG.getProperty(objectArr[0]));

	try {
		for(int i=0;i<3;i++) {
			try {
				if(testCONFIG.getProperty("Env").equals("LocalMachine")) {
					driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), cap);
				}else {
					driver = new RemoteWebDriver(new URL("http://ggstoolsvc.sapient.com:4444/wd/hub"), cap);
				}

				driver.navigate().to(CONFIG.getProperty(objectArr[0]));
				currentTitle = driver.getTitle();
			}catch(Throwable t) {
				log.debug(t.getMessage());
				continue;
			}

			if(!(currentTitle.contains("Login") || currentTitle.contains("CQ5 - Sign In") || currentTitle.contains("CRXDE Lite") || currentTitle.contains("gsCIO | Goldman Sachs") || currentTitle.contains("Google"))) {
				if(driver!=null) {
					driver.quit();
				}
				continue;
			}else {
				break;
			}
		}

	}catch(Throwable e) {
		log.debug(e.getMessage());
		return "Fail";
	}
	log.debug("@@@ DRIVER K1 "+driver);

	driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);

	if(launchBrowser.equalsIgnoreCase("Chrome") && System.getProperty("os.name").equals("Mac OS X")) {
		driver.manage().window().setSize(new Dimension(1920, 978));
	}else {
		driver.manage().window().maximize();
	}
	return "Pass";
}

public String removeSpecialChar() {

	log.debug("=============================");
	log.debug("executing keyword removeSpecialChar");
	
	String oldCount;

	try {
		oldCount  = getWebElement(OR, objectArr[0]).getText();
		Functions.replaceAll(oldCount,"[^0-9]","");
		testData.setCellData(currentTest, data_column_nameArr[0], 2, oldCount );

		log.debug("data set in excel :" + oldCount);
		return "Pass";
	}catch(Throwable r) {
		log.debug("Error while executing removeSpecialChar keyword" + r.getMessage());
		return "Fail";
	}
}

public String matchReadCount() {
	
	log.debug("=============================");
	log.debug("executing keyword matchReadCount");
	
	String newCount,oldCount;

	try {
		newCount = getWebElement(OR, objectArr[0]).getText();
		oldCount = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
		
		log.debug("new Count :" + newCount);
		log.debug("old Count :" + oldCount);
		
		if(oldCount==(newCount+1))
			return "Pass";
		else
			return "Fail";
	}catch(Throwable r) {
		log.debug("Error while executing matchReadCount keyword" + r.getMessage());
		return "Fail";
	}
}
public String deletePlaylist(){
	log.debug("===============================");
	log.debug("Executing deletePlaylist");
	WebElement playlist,delete,confirmDelete;
	try
	{
		playlist = getWebElement(OR,objectArr[0]);
		if(playlist.isDisplayed())
		{
			(new Actions(driver)).contextClick(playlist).perform();
				
		delete = getWebElement(OR,objectArr[1]);
		delete.click();
		
		Thread.sleep(2000);
		
		confirmDelete = getWebElement(OR,objectArr[2]);
		confirmDelete.click();
		Thread.sleep(2000);
		
		}
		
		if(playlist.isDisplayed())
			return "Fail";
		else
			return "Pass";
	}
	catch(Throwable t)
	{
		log.debug("Playlist not present");
		return "Pass";
	}
	}

public String checkDateFormat(){
	log.debug("=============================");
	log.debug("Executing checkDateFormat");
	//this keyword takes a date input and checks its format
	String date;
	String[] format = { "MMM dd yyyy"};
	date = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
	if(date!=null){
		for (String string : format) {
			SimpleDateFormat sdf = new SimpleDateFormat(string);
			try {
				sdf.parse(date);
				log.debug("Printing the value of " + string);
			} catch (ParseException e) {
				log.debug("Date is not in required format");
				return "Fail";
			}
		}
	}
	return "Pass";
}

public String splitAndSetString(){
	log.debug("=============================");
	log.debug("Executing splitAndSetString");
	//this keyword takes an input string splits it and sets it
	String inputString, setString,splitBy;
	String[] splitString;
	inputString = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
	splitBy = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
	try{
		splitString =inputString.split(splitBy);
	}catch(Throwable r) {
		log.debug("Error while executing splitAndSetString keyword" + r.getMessage());
		return "Fail";
	}
	if(splitBy.contains(":"))
		setString = splitString[0];
	else
		setString = splitString[1].trim();
	testData.setCellData(currentTest, data_column_nameArr[2], testRepeat, setString );
	log.debug("Value set in excel is"+testData.getCellData(currentTest, data_column_nameArr[2], testRepeat));
	return "Pass";
}

public String compareInputFromExcel(){
	log.debug("=============================");
	log.debug("Executing compareInputFromExcel");
	//this keyword takes two string and compares them.
	String inputString, expectedString;
	inputString = testData.getCellData(currentTest, data_column_nameArr[0],testRepeat);
	expectedString = testData.getCellData(currentTest, data_column_nameArr[1],testRepeat);
	if(inputString.equals(expectedString))
		return "Pass";
	else 
		return "Fail";

}

}

//close keywords class