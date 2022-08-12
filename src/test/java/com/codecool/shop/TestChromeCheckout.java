package com.codecool.shop;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class TestChromeCheckout {

    public WebDriver driver;

    @BeforeClass
    public void setUp() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.navigate().to("http://localhost:8080/");
        fillTheCart();
    }

    @BeforeMethod
    public void navigateToPage() {
        driver.navigate().to("http://localhost:8080/");
        driver.findElement(By.id("go-to-checkout")).click();
    }

    @Test
    public void goToCheckout_NoExceptionsThrown() throws InterruptedException {
        String expectedUrl = "http://localhost:8080/checkout";
        sleep();
        Assert.assertEquals(driver.getCurrentUrl(), expectedUrl);
    }

    @Test
    public void tryToCheckout_DetailsMissing_RedirectsBackToCheckout() throws InterruptedException {
        int inputFieldsNumber = driver.findElements(By.cssSelector("[id='checkout-form'] input")).size();
        String expectedUrl = "http://localhost:8080/checkout";
        for (int i = 0; i < inputFieldsNumber; i++) {
            navigateToPage();
            List<WebElement> inputFields = driver.findElements(By.cssSelector("[id='checkout-form'] input"));
            int index = 0;
            for (WebElement inputField : inputFields) {
                if (i != index) {
                    inputField.sendKeys("One data missing");
                }
                index++;
            }
            sleep();
            WebElement submitButton = driver.findElement(By.cssSelector("[id='checkout-form'] button"));
            submitButton.click();
            Assert.assertEquals(driver.getCurrentUrl(), expectedUrl);
        }
    }

    @Test
    public void tryToCheckout_NoMissingDetails_RedirectsToPayment() throws InterruptedException {
        List<WebElement> inputFields = driver.findElements(By.cssSelector("[id='checkout-form'] input"));
        for (WebElement inputField : inputFields) {
            inputField.sendKeys("No missing data");
            sleep();
        }
        sleep();
        WebElement submitButton = driver.findElement(By.cssSelector("[id='checkout-form'] button"));
        submitButton.click();
        String expectedUrl = "http://localhost:8080/payment";
        String actualUrl = driver.getCurrentUrl().split("\\?")[0];
        Assert.assertEquals(actualUrl, expectedUrl);
    }

    @AfterClass
    public void tearDown() throws InterruptedException {
        if(driver!=null) {
            for (int i = 0; i < 4; i++) {
                sleep();
            }
            System.out.println("Closing chrome browser");
            driver.quit();
        }
    }

    private void sleep() throws InterruptedException {
        Thread.sleep(500);
    }

    private void fillTheCart() throws InterruptedException {
        driver.navigate().to("http://localhost:8080/");
        List<WebElement> addToCartButtons = driver.findElements(By.linkText("Add to cart"));
        for (WebElement addToCartButton : addToCartButtons) {
            addToCartButton.click();
            sleep();
            driver.switchTo().alert().accept();
        }
    }
}
