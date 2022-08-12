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

public class TestChromePayment {

    private WebDriver driver;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @BeforeMethod
    public void navigateToPage() throws InterruptedException {
        driver.navigate().to("http://localhost:8080/");
        fillTheCart();
        driver.findElement(By.id("go-to-checkout")).click();
        checkout();
    }

    @Test
    public void paypalPayment_InvalidEmailTooShort_CannotPay() throws InterruptedException {
        paypalTry("e@e.e", "validPassword");
        String expectedUrl = "http://localhost:8080/payment";
        String actualUrl = driver.getCurrentUrl().split("\\?")[0];
        Assert.assertEquals(actualUrl, expectedUrl);
        sleep();
    }

    @Test
    public void paypalPayment_InvalidEmailNoAt_CannotPay() throws InterruptedException {
        paypalTry("invalidemail.noat", "validPassword");
        String expectedUrl = "http://localhost:8080/payment";
        String actualUrl = driver.getCurrentUrl().split("\\?")[0];
        Assert.assertEquals(actualUrl, expectedUrl);
        sleep();
    }

    @Test
    public void paypalPayment_InvalidPasswordTooShort_CannotPay() throws InterruptedException {
        paypalTry("valid@email.com", "lilpw");
        String expectedUrl = "http://localhost:8080/payment";
        String actualUrl = driver.getCurrentUrl().split("\\?")[0];
        Assert.assertEquals(actualUrl, expectedUrl);
        sleep();
    }

    @Test
    public void paypalPayment_InvalidPasswordMissing_CannotPay() throws InterruptedException {
        paypalTry("valid@email.com", "");
        String expectedUrl = "http://localhost:8080/payment";
        String actualUrl = driver.getCurrentUrl().split("\\?")[0];
        Assert.assertEquals(actualUrl, expectedUrl);
        sleep();
    }

    @Test
    public void paypalPayment_InvalidEmailMissing_CannotPay() throws InterruptedException {
        paypalTry("", "ValidPassword");
        String expectedUrl = "http://localhost:8080/payment";
        String actualUrl = driver.getCurrentUrl().split("\\?")[0];
        Assert.assertEquals(actualUrl, expectedUrl);
        sleep();
    }

    @Test
    public void paypalPayment_ValidData_RedirectHomePage() throws InterruptedException {
        paypalTry("valid@email.com", "validPassword");
        sleep();
        driver.switchTo().alert().accept();
        String expectedUrl = "http://localhost:8080/";
        Assert.assertEquals(driver.getCurrentUrl(), expectedUrl);
    }

    @Test
    public void paypalPayment_ValidPayment_CartEmptyAfterPayment() throws InterruptedException {
        paypalTry("valid@email.com", "validPassword");
        sleep();
        driver.switchTo().alert().accept();
        driver.findElement(By.linkText("My Cart")).click();
        sleep();
        int tableRowsExpected = 0;
        int tableRows = driver.findElements(By.cssSelector("[class='table table-image'] tr")).size();
        Assert.assertEquals(tableRows, tableRowsExpected);
    }

    @Test
    public void creditCardPayment_InvalidCardNumberMissing_CannotPay() throws InterruptedException {
        creditCardTry("", "24", "08", "111", "John Johnson");
        String expectedUrl = "http://localhost:8080/payment";
        String actualUrl = driver.getCurrentUrl().split("\\?")[0];
        Assert.assertEquals(actualUrl, expectedUrl);
        sleep();
    }

    @Test
    public void creditCardPayment_InvalidCardNumberTooShort_CannotPay() throws InterruptedException {
        creditCardTry("1222 3444", "24", "08", "111", "John Johnson");
        String expectedUrl = "http://localhost:8080/payment";
        String actualUrl = driver.getCurrentUrl().split("\\?")[0];
        Assert.assertEquals(actualUrl, expectedUrl);
        sleep();
    }

    @Test
    public void creditCardPayment_InvalidYearTooSmall_CannotPay() throws InterruptedException {
        creditCardTry("1222 3444 5666 7888", "0", "08", "111", "John Johnson");
        String expectedUrl = "http://localhost:8080/payment";
        String actualUrl = driver.getCurrentUrl().split("\\?")[0];
        Assert.assertEquals(actualUrl, expectedUrl);
        sleep();
    }

    @Test
    public void creditCardPayment_InvalidYearTooBig_CannotPay() throws InterruptedException {
        creditCardTry("1222 3444 5666 7888", "100", "08", "111", "John Johnson");
        String expectedUrl = "http://localhost:8080/payment";
        String actualUrl = driver.getCurrentUrl().split("\\?")[0];
        Assert.assertEquals(actualUrl, expectedUrl);
        sleep();
    }

    @Test
    public void creditCardPayment_InvalidYearMissing_CannotPay() throws InterruptedException {
        creditCardTry("1222 3444 5666 7888", "", "08", "111", "John Johnson");
        String expectedUrl = "http://localhost:8080/payment";
        String actualUrl = driver.getCurrentUrl().split("\\?")[0];
        Assert.assertEquals(actualUrl, expectedUrl);
        sleep();
    }

    @Test
    public void creditCardPayment_InvalidMonthMissing_CannotPay() throws InterruptedException {
        creditCardTry("1222 3444 5666 7888", "24", "", "111", "John Johnson");
        String expectedUrl = "http://localhost:8080/payment";
        String actualUrl = driver.getCurrentUrl().split("\\?")[0];
        Assert.assertEquals(actualUrl, expectedUrl);
        sleep();
    }

    @Test
    public void creditCardPayment_InvalidMonthTooSmall_CannotPay() throws InterruptedException {
        creditCardTry("1222 3444 5666 7888", "24", "0", "111", "John Johnson");
        String expectedUrl = "http://localhost:8080/payment";
        String actualUrl = driver.getCurrentUrl().split("\\?")[0];
        Assert.assertEquals(actualUrl, expectedUrl);
        sleep();
    }

    @Test
    public void creditCardPayment_InvalidMonthTooBig_CannotPay() throws InterruptedException {
        creditCardTry("1222 3444 5666 7888", "24", "13", "111", "John Johnson");
        String expectedUrl = "http://localhost:8080/payment";
        String actualUrl = driver.getCurrentUrl().split("\\?")[0];
        Assert.assertEquals(actualUrl, expectedUrl);
        sleep();
    }

    @Test
    public void creditCardPayment_InvalidCvvTooBig_CannotPay() throws InterruptedException {
        creditCardTry("1222 3444 5666 7888", "24", "10", "1000", "John Johnson");
        String expectedUrl = "http://localhost:8080/payment";
        String actualUrl = driver.getCurrentUrl().split("\\?")[0];
        Assert.assertEquals(actualUrl, expectedUrl);
        sleep();
    }

    @Test
    public void creditCardPayment_InvalidCvvTooSmall_CannotPay() throws InterruptedException {
        creditCardTry("1222 3444 5666 7888", "24", "10", "-1", "John Johnson");
        String expectedUrl = "http://localhost:8080/payment";
        String actualUrl = driver.getCurrentUrl().split("\\?")[0];
        Assert.assertEquals(actualUrl, expectedUrl);
        sleep();
    }

    @Test
    public void creditCardPayment_InvalidCvvMissing_CannotPay() throws InterruptedException {
        creditCardTry("1222 3444 5666 7888", "24", "10", "", "John Johnson");
        String expectedUrl = "http://localhost:8080/payment";
        String actualUrl = driver.getCurrentUrl().split("\\?")[0];
        Assert.assertEquals(actualUrl, expectedUrl);
        sleep();
    }

    @Test
    public void creditCardPayment_InvalidNameMissing_CannotPay() throws InterruptedException {
        creditCardTry("1222 3444 5666 7888", "24", "10", "111", "");
        String expectedUrl = "http://localhost:8080/payment";
        String actualUrl = driver.getCurrentUrl().split("\\?")[0];
        Assert.assertEquals(actualUrl, expectedUrl);
        sleep();
    }

    @Test
    public void creditCardPayment_InvalidNameTooShort_CannotPay() throws InterruptedException {
        creditCardTry("1222 3444 5666 7888", "24", "10", "111", "Joe");
        String expectedUrl = "http://localhost:8080/payment";
        String actualUrl = driver.getCurrentUrl().split("\\?")[0];
        Assert.assertEquals(actualUrl, expectedUrl);
        sleep();
    }

    @Test
    public void creditCardPayment_ValidData_RedirectToHomePage() throws InterruptedException {
        creditCardTry("1222 3444 5666 7888", "24", "10", "111", "John Johnson");
        sleep();
        driver.switchTo().alert().accept();
        String expectedUrl = "http://localhost:8080/";
        Assert.assertEquals(driver.getCurrentUrl(), expectedUrl);
    }

    @Test
    public void creditCardPayment_ValidPayment_CartEmptyAfterPayment() throws InterruptedException {
        creditCardTry("1222 3444 5666 7888", "24", "10", "111", "John Johnson");
        sleep();
        driver.switchTo().alert().accept();
        driver.findElement(By.linkText("My Cart")).click();
        sleep();
        int tableRowsExpected = 0;
        int tableRows = driver.findElements(By.cssSelector("[class='table table-image'] tr")).size();
        Assert.assertEquals(tableRows, tableRowsExpected);
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

    private void paypalTry(String email, String password) throws InterruptedException {
        goToPaypal();
        WebElement emailField = driver.findElement(By.id("paypal-email"));
        WebElement passwordField = driver.findElement(By.id("paypal-password"));
        emailField.sendKeys(email);
        passwordField.sendKeys(password);
        WebElement submitButton = driver.findElement(By.cssSelector("[id='payment-form'] button"));
        submitButton.click();
        sleep();
    }

    private void creditCardTry(String cardNumber, String year, String month, String cvv, String name) throws InterruptedException {
        goToCreditCard();
        WebElement cardNumberField = driver.findElement(By.id("card-number"));
        WebElement yearField = driver.findElement(By.id("expiration-year"));
        WebElement monthField = driver.findElement(By.id("expiration-month"));
        WebElement cvvField = driver.findElement(By.id("cvv"));
        WebElement nameField = driver.findElement(By.id("card-owner"));
        WebElement submitButton = driver.findElement(By.cssSelector("[id='payment-form'] button"));
        cardNumberField.sendKeys(cardNumber);
        yearField.sendKeys(year);
        monthField.sendKeys(month);
        cvvField.sendKeys(cvv);
        nameField.sendKeys(name);
        submitButton.click();
        sleep();
    }

    private void goToPaypal() throws InterruptedException {
        WebElement paypalButton = driver.findElement(By.id("paypal"));
        WebElement submitButton = driver.findElement(By.cssSelector("[id='payment-selector'] button"));
        paypalButton.click();
        sleep();
        submitButton.click();
    }

    private void goToCreditCard() throws InterruptedException {
        WebElement paypalButton = driver.findElement(By.id("credit"));
        WebElement submitButton = driver.findElement(By.cssSelector("[id='payment-selector'] button"));
        paypalButton.click();
        sleep();
        submitButton.click();
    }

    private void checkout() throws InterruptedException {
        List<WebElement> inputFields = driver.findElements(By.cssSelector("[id='checkout-form'] input"));
        for (WebElement inputField : inputFields) {
            inputField.sendKeys("Valid data");
        }
        sleep();
        WebElement submitButton = driver.findElement(By.cssSelector("[id='checkout-form'] button"));
        submitButton.click();
    }

    private void sleep() throws InterruptedException {
        Thread.sleep(500);
    }

    private void fillTheCart() throws InterruptedException {
        driver.navigate().to("http://localhost:8080/");
        WebElement addToCartButton = driver.findElement(By.linkText("Add to cart"));
        addToCartButton.click();
        sleep();
        driver.switchTo().alert().accept();
    }
}
