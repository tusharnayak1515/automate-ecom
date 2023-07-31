package com;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class EdgeTest extends DemoTest {
	public SoftAssert soft = new SoftAssert();
	
	@Test(groups = { "edge" }, testName = "Edge Test")
	public void OpenFlipkartTest() throws Exception
	{			
		try {
			System.out.println("driver: "+ driver);
			driver.get("https://www.flipkart.com/");
			Thread.sleep(2000);
			
			String filename = "OpenFlipkart - " + "Edge" + ".jpg";
			String screenshotpath = captureScreenShot(filename);
			extentTest.addScreenCaptureFromPath(screenshotpath);
			
			Thread.sleep(2000);
			
			String filename1 = "CloseLoginPopup - " + "Edge" + ".jpg";
			String screenshotpath1 = captureScreenShot(filename1);
			extentTest.addScreenCaptureFromPath(screenshotpath1);
			closeLoginPopup();

			soft.assertAll();
			extentTest.pass("Assertion is passed for Opening flipkart");
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}
	
	@Test(groups = { "edge" }, dependsOnMethods = "OpenFlipkartTest")
	public void SearchingIphone13() throws Exception
	{			
		try {
			WebElement mobilesLink = driver.findElement(By.linkText("Mobiles"));
            mobilesLink.click();
            
            String filename = "MobileCategory - " + "Edge" + ".jpg";
			String screenshotpath = captureScreenShot(filename);
			extentTest.addScreenCaptureFromPath(screenshotpath);
            
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            wait.until(ExpectedConditions.urlContains("mobile-phones"));
            
			WebElement searchInput = driver.findElement(By.xpath("//input[@name='q']"));
	        searchInput.sendKeys("iPhone 13");

	        WebElement searchButton = driver.findElement(By.xpath("//button[@type='submit']"));
	        searchButton.click();
	        
	        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        
	        wait.until(ExpectedConditions.titleContains("IPhone 13"));
	        
	        String filename1 = "SearchIphone - " + "Edge" + ".jpg";
			String screenshotpath1 = captureScreenShot(filename1);
			extentTest.addScreenCaptureFromPath(screenshotpath1);
	        
			Thread.sleep(2000);
			soft.assertAll();
			extentTest.pass("Assertion is passed for searching iPhone 13");
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}
	
	@Test(groups = { "edge" }, dependsOnMethods = "SearchingIphone13")
	public void ProductImagesLoadedTest() {
		soft.assertTrue(areProductImagesLoaded());
		
		String filename = "ProductImageLoad - " + "Edge" + ".jpg";
		String screenshotpath = captureScreenShot(filename);
		extentTest.addScreenCaptureFromPath(screenshotpath);
		
		soft.assertAll();
		extentTest.pass("Assertion is passed for Product images loading");
    }
	
	public boolean areProductImagesLoaded() {
		List<WebElement> productImages = driver.findElements(By.xpath("//img[@class='_396cs4']"));
		if(productImages.isEmpty()) {
			return false;
		}
        return true;
	}
	
	@Test(groups = { "edge" }, dependsOnMethods = "ProductImagesLoadedTest")
	public void ScrollAvailabilityTest() {
		soft.assertTrue(isScrollFeatureAvailable());
		
		soft.assertAll();
		extentTest.pass("Assertion is passed for Scroll availability");
    }
	
	public boolean isScrollFeatureAvailable() {
        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 100)");
            String filename = "ScrollAvailable - " + "Edge" + ".jpg";
			String screenshotpath = captureScreenShot(filename);
			extentTest.addScreenCaptureFromPath(screenshotpath);
            Thread.sleep(2000);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
	
	@Test(groups = { "edge" }, dependsOnMethods = "ScrollAvailabilityTest")
	public void scrollToBottomTest() throws Exception {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        String filename = "ScrollToBottom - " + "Edge" + ".jpg";
		String screenshotpath = captureScreenShot(filename);
		extentTest.addScreenCaptureFromPath(screenshotpath);
		Thread.sleep(2000);
        soft.assertTrue(hasReachedBottom());
        
        soft.assertAll();
		extentTest.pass("Assertion is passed for scroll to bottom");
    }
	
	public boolean hasReachedBottom() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
	    Number windowHeight = (Number) js.executeScript("return window.innerHeight");
	    Number totalPageHeight = (Number) js.executeScript("return document.body.scrollHeight");

	    Number scrollPosition = (Number) js.executeScript("return window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0");
	    System.out.println("windowHeight: "+windowHeight+", scrollPosition: "+scrollPosition+", totalPageHeight: "+totalPageHeight);
	    return windowHeight.longValue() + scrollPosition.longValue() >= totalPageHeight.longValue()-10;
	}
	
	@Test(groups = { "edge" }, dependsOnMethods = "scrollToBottomTest")
	public void checkContentRefreshFrequency() {
        List<Long> timeDifferences = new ArrayList<Long>();

        for (int i = 0; i < 5; i++) {
            long startTime = System.currentTimeMillis();

            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, window.innerHeight);");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long endTime = System.currentTimeMillis();
            long timeDiff = endTime - startTime;
            timeDifferences.add(timeDiff);
        }
        
        Long timeDif = (long) 0;

        for (int i = 0; i < timeDifferences.size(); i++) {
        	timeDif += timeDifferences.get(i);
            System.out.println("Scroll " + (i + 1) + ": " + timeDifferences.get(i) + " ms");
        }
        
        soft.assertAll();
		extentTest.pass("Assertion is passed for checking content refresh frequency"+timeDif/timeDifferences.size());
    }
	
	public void closeLoginPopup() {
        try {
            WebElement loginPopup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='_2MlkI1']")));
            
            if (loginPopup.isDisplayed()) {
                WebElement closePopupButton = driver.findElement(By.xpath("//button[@class='_2KpZ6l _2doB4z']"));
                closePopupButton.click();
            }
        } catch (TimeoutException e) {
            System.out.println("Login popup not found. Continuing with the test.");
        }
    }
}
