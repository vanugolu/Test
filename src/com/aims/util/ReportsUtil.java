package com.aims.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.aims.Keywords;
import com.aims.report.ModuleStats;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


public class ReportsUtil extends Keywords{
	public String version;
	public Logger log;
	public static File indexHTML;
	public static String RUN_DATE;
	public static String testStartTime;
	public static String testEndTime;
	public static String ENVIRONMENT;
	public static String suite;
	
	public static Integer passCount ;
	public static Integer failCount ;
	public static Integer skipCount ;
	public static Integer grandTotal ;
	
	public static ArrayList<ModuleStats> allModulesStats;
	public static Map<String, Object> indexFileData;
	
	static {
		allModulesStats = new ArrayList<ModuleStats>();
		indexFileData = new HashMap<String, Object>();
	}

	public ReportsUtil() {

	}

	// returns current date and time
	public static String now(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(cal.getTime());

	}

	// store screenshots
	public static void takeScreenShot(String file, RemoteWebDriver driver, String reportFolder, Logger log) {
		try {
			driver = (RemoteWebDriver) new Augmenter().augment(driver);
			File scrFile = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);
			FileUtils.moveFile(scrFile, new File(System.getProperty("user.dir")
					+ File.separator + reportFolder, file));
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Throwable t) {
			// TODO: handle exception
			log.debug(t.getMessage());
		}

	}

	public String getBrowserVersion(String launchBrowser) {
		try {
			log.debug("=============================");
			log.debug("Executing getBrowserVersion from reportsUtil");
			System.out.println("GET BROWS " + launchBrowser);

			if(launchBrowser.equalsIgnoreCase("Firefox")) {
				/*System.out.println("GET BROWS 1" + launchBrowser);
				WebDriver versionDriver = new FirefoxDriver();
				System.out.println("GET BROWS 2" + launchBrowser);
				String temp = (String) ((JavascriptExecutor) versionDriver)
						.executeScript("return navigator.userAgent;");
				System.out.println("GET BROWS 3" + launchBrowser);
				System.out.println("temp " + temp);
				version = temp.substring(59);
				log.debug("browser version is : " + version);
				System.out.println("browser version is : " + version);
				Thread.sleep(2000);
				versionDriver.close();*/
				version = "Firefox";

			}else if(launchBrowser.equalsIgnoreCase("InternetExplorer")) {
				/*DesiredCapabilities capabilities = DesiredCapabilities
						.internetExplorer();
				capabilities
						.setCapability(
								InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
								true);

				WebDriver versionDriver = new InternetExplorerDriver(
						capabilities);
				String temp = (String) ((JavascriptExecutor) versionDriver)
						.executeScript("return navigator.userAgent;");
				System.out.println(temp);
				version = temp.substring(25, 33);
				log.debug("browser version is : " + version);
				System.out.println("browser version is : " + version);
				Thread.sleep(2000);
				versionDriver.close();*/
				version = "InternetExplorer";

			}else if(launchBrowser.equalsIgnoreCase("Chrome")) {
				/*DesiredCapabilities capabilities = DesiredCapabilities.chrome();
				String chromeDriver = CONFIG.getProperty("ChromeDriver");
				String chromeBinary = CONFIG.getProperty("ChromeBinary");
				System.setProperty("webdriver.chrome.driver", chromeDriver);
				capabilities.setCapability("chrome.binary", chromeBinary);

				WebDriver versionDriver = new ChromeDriver(capabilities);
				String temp = (String) ((JavascriptExecutor) versionDriver)
						.executeScript("return navigator.userAgent;");
				System.out.println(temp);
				version = temp.substring(74, 84);
				log.debug("browser version is : " + version);
				System.out.println("browser version is : " + version);
				Thread.sleep(2000);
				versionDriver.close();*/
				version = "Chrome";
			}else if(launchBrowser.equalsIgnoreCase("Safari")) {
				/*DesiredCapabilities capabilities = DesiredCapabilities.chrome();
				String chromeDriver = CONFIG.getProperty("ChromeDriver");
				String chromeBinary = CONFIG.getProperty("ChromeBinary");
				System.setProperty("webdriver.chrome.driver", chromeDriver);
				capabilities.setCapability("chrome.binary", chromeBinary);

				WebDriver versionDriver = new ChromeDriver(capabilities);
				String temp = (String) ((JavascriptExecutor) versionDriver)
						.executeScript("return navigator.userAgent;");
				System.out.println(temp);
				version = temp.substring(74, 84);
				log.debug("browser version is : " + version);
				System.out.println("browser version is : " + version);
				Thread.sleep(2000);
				versionDriver.close();*/
				version = "Safari";
			}
			return version;
		} catch (Throwable t) {
			log.debug("Error while returning browser version -"
					+ t.getMessage());
			return "Chrome/27";

		}

	}

	
	public static void prepareWebReport(String templatePath, Map<String, Object> data, File targetFile) throws IOException{
		Configuration cfg = new Configuration();
		FileWriter filestream = null;
		BufferedWriter bw = null;
	    try {
	        //Load template from source folder
	        Template template = cfg.getTemplate(templatePath);
	        // File output
	     // Create file if it doesn't exists
	  	  if (!targetFile.exists()) {
	  		targetFile.createNewFile();
	  	  }
	        filestream = new FileWriter (targetFile);
	        bw = new BufferedWriter(filestream);
	        template.process(data, bw);
	        bw.flush();
	         
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (TemplateException e) {
	        e.printStackTrace();
	    } finally {
	    	if(bw != null) {
	    		bw.close();
	    	}
	    }
		
	}
	public static void clearTempFolder() throws IOException {

        try {
        File file = new File(System.getProperty("java.io.tmpdir"));
        FileUtils.cleanDirectory(file);
        }

        catch (IOException e) {
        // Do nothing since
        }
        }

	
	public static void shutDownGrid() throws IOException{
		try {
			if(((String)testCONFIG.getProperty("Env")).equals("LocalMachine")) {
				Runtime.getRuntime().exec("taskkill /IM cmd.exe");
				Runtime.getRuntime().exec("taskkill /IM java.exe");
				Runtime.getRuntime().exec("taskkill /IM chromedriver.exe /f");
				Runtime.getRuntime().exec("taskkill /IM IEDriverServer.exe /f");
				Runtime.getRuntime().exec("taskkill /IM iexplore.exe /f");
			}else {
				Runtime.getRuntime().exec("taskkill /IM cmd.exe");
				Runtime.getRuntime().exec("taskkill /IM java.exe");
				Runtime.getRuntime().exec("taskkill /IM chromedriver.exe /f");
				Runtime.getRuntime().exec("taskkill /IM IEDriverServer.exe /f");
				Runtime.getRuntime().exec("taskkill /IM iexplore.exe /f");
				Runtime.getRuntime().exec("taskkill /IM chrome.exe /f");
				Runtime.getRuntime().exec("taskkill /IM firefox.exe /f");
			}
		}catch(Throwable t) {
			
		}

	}

}
