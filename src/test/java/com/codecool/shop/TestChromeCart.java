package com.codecool.shop;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;

public class TestChromeCart {

    public WebDriver driver;

    @BeforeClass
    public void setUp() {
        System.out.println("*******************");
        System.out.println("launching chrome browser");
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @BeforeMethod
    public void navigateToPage() {
        driver.navigate().to("http://localhost:8080/");
    }

    @Test
    public void testPageTitleInBrowser() {
        String strPageTitle = driver.getTitle();
        Assert.assertTrue(strPageTitle.equalsIgnoreCase("Prello"), "Page title doesn't match");
    }

    @Test
    public void addEveryElementToTheBasket_CartTableRowsMatch() throws InterruptedException {
        int addToCartButtonNumber = fillTheCart();
        driver.findElement(By.linkText("My Cart")).click();
        sleep();
        List<WebElement> tableRows = driver.findElements(By.cssSelector("[class='table table-image'] tr"));
        Assert.assertEquals(addToCartButtonNumber, tableRows.size());
    }

    @Test
    public void useEveryPlusButton_ItemQuantityIncreases() throws InterruptedException {
        driver.findElement(By.linkText("My Cart")).click();
        sleep();
        List<WebElement> tableRows = driver.findElements(By.cssSelector("[class='table table-image'] tr"));
        for (WebElement tableRow : tableRows) {
            sleep();
            String id = tableRow.findElement(By.className("product-quantity")).getAttribute("id").replace("quantity-", "");
            String quantity = tableRow.findElement(By.className("product-quantity")).getAttribute("innerHTML");
            driver.findElement(By.id("increase-cart-" + id)).click();
            String newQuantity = tableRow.findElement(By.className("product-quantity")).getAttribute("innerHTML");
            Assert.assertEquals(Integer.parseInt(quantity) + 1, Integer.parseInt(newQuantity));
        }
    }

    @Test
    public void useEveryMinusButton_ItemQuantityDecreases() throws InterruptedException {
        driver.findElement(By.linkText("My Cart")).click();
        sleep();
        List<WebElement> tableRows = driver.findElements(By.cssSelector("[class='table table-image'] tr"));
        for (WebElement tableRow : tableRows) {
            sleep();
            String id = tableRow.findElement(By.className("product-quantity")).getAttribute("id").replace("quantity-", "");
            String quantity = tableRow.findElement(By.className("product-quantity")).getAttribute("innerHTML");
            driver.findElement(By.id("decrease-cart-" + id)).click();
            String newQuantity = tableRow.findElement(By.className("product-quantity")).getAttribute("innerHTML");
            Assert.assertEquals(Integer.parseInt(quantity) - 1, Integer.parseInt(newQuantity));
        }
    }

    @Test
    public void useMinusButtonTillZeroQuantity_ProductDisappears() throws InterruptedException {
        driver.findElement(By.linkText("My Cart")).click();
        sleep();
        int tableRowsExpected = driver.findElements(By.cssSelector("[class='table table-image'] tr")).size() - 1;
        WebElement tableRow = driver.findElement(By.cssSelector("[class='table table-image'] tr"));
        sleep();
        String id = tableRow.findElement(By.className("product-quantity")).getAttribute("id").replace("quantity-", "");
        String quantity = tableRow.findElement(By.className("product-quantity")).getAttribute("innerHTML");
        WebElement minusButton = driver.findElement(By.id("decrease-cart-" + id));
        for (int i = 0; i < Integer.parseInt(quantity); i++) {
            sleep();
            minusButton.click();
        }
        int tableRows = driver.findElements(By.cssSelector("[class='table table-image'] tr")).size();
        Assert.assertThrows(StaleElementReferenceException.class, tableRow::isDisplayed);
        Assert.assertEquals(tableRowsExpected, tableRows);
    }

    @Test
    public void useDeleteProductButtonOnce_ProductDisappears() throws InterruptedException {
        driver.findElement(By.linkText("My Cart")).click();
        sleep();
        int tableRowsExpected = driver.findElements(By.cssSelector("[class='table table-image'] tr")).size() - 1;
        WebElement deleteProductButton = driver.findElement(By.linkText("Delete product"));
        deleteProductButton.click();
        int tableRows = driver.findElements(By.cssSelector("[class='table table-image'] tr")).size();
        sleep();
        Assert.assertThrows(StaleElementReferenceException.class, deleteProductButton::isDisplayed);
        Assert.assertEquals(tableRowsExpected, tableRows);
    }

    @Test
    public void useDeleteProductButtonTillCartEmpty_AllProductsDisappear() throws InterruptedException {
        driver.findElement(By.linkText("My Cart")).click();
        sleep();
        int tableRows = driver.findElements(By.cssSelector("[class='table table-image'] tr")).size();
        for (int i = 0; i < tableRows; i++) {
            sleep();
            WebElement deleteProductButton = driver.findElement(By.linkText("Delete product"));
            deleteProductButton.click();
            int tableRowsAfterDelete = driver.findElements(By.cssSelector("[class='table table-image'] tr")).size();
            Assert.assertThrows(StaleElementReferenceException.class, deleteProductButton::isDisplayed);
            Assert.assertEquals(tableRowsAfterDelete, tableRows - i - 1);
        }
        fillTheCart();
    }

    @Test
    public void changeFilter_NoExceptionsThrown() throws InterruptedException {
        sleep();
        driver.findElement(By.id("filter-modal-button")).click();
        sleep();
        Select dropdown = new Select(driver.findElement(By.cssSelector("[id='category-selector'] select")));
        dropdown.selectByVisibleText("Sandbox, simulation");
        sleep();
        driver.findElement(By.cssSelector("[id='category-selector'] button")).click();
        sleep();
        driver.findElement(By.id("filter-modal-button")).click();
        sleep();
        dropdown = new Select(driver.findElement(By.cssSelector("[id='supplier-selector'] select")));
        dropdown.selectByVisibleText("Codecool");
        sleep();
        driver.findElement(By.cssSelector("[id='supplier-selector'] button")).click();
        sleep();
        sleep();
    }

    @AfterClass
    public void tearDown() {
        if(driver!=null) {
            System.out.println("Closing chrome browser");
            driver.quit();
        }
    }

    private void sleep() throws InterruptedException {
        Thread.sleep(500);
    }

    private int fillTheCart() throws InterruptedException {
        driver.navigate().to("http://localhost:8080/");
        List<WebElement> addToCartButtons = driver.findElements(By.linkText("Add to cart"));
        for (WebElement addToCartButton : addToCartButtons) {
            for (int i = 0; i < 2; i++) {
                addToCartButton.click();
                sleep();
                driver.switchTo().alert().accept();
            }
        }
        return addToCartButtons.size();
    }

}
