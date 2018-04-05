import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class tstWD {

    private String base_url = "http://localhost:8080";
    private String next_url;
    private WebDriver webDriver = null;

    // Some settings and Authentication
    @BeforeClass
    public void beforeClass() {
        System.setProperty("webdriver.chrome.driver", "/Users/lorem/tat/HT1/chromedriver");

        // Старт с пустой страницы браузера
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability("chrome.switches", Arrays.asList("--homepage=about:blank"));

        // Запуск драйвера (и браузера)
        webDriver = new ChromeDriver(capabilities);
        webDriver.get(base_url);

        // Authentication
        webDriver.findElement(By.linkText("log in")).click();

        WebDriverWait wait = new WebDriverWait(webDriver, 5);
        wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//form"), 2));

        webDriver.findElement(By.name("j_username")).sendKeys("Lorem");
        webDriver.findElement(By.name("j_password")).sendKeys("admin");

        webDriver.findElement(By.name("Submit")).click();
    }

    @AfterClass
    public void afterClass() {
        webDriver.quit();
    }

    @Test
    public void tstManageElements() {
        webDriver.get(base_url + "/manage");

        boolean forms_equal = false;
        WebElement link = webDriver.findElement(By.xpath("//div/a[@title=\"Manage Users\"]"));

        WebElement element1 = webDriver.findElement(By.xpath("//div/a[@title=\"Manage Users\"]/dl/dt"));
        Assert.assertNotNull(element1, "Unable to locate element 'dt'. ");

        WebElement element2 = webDriver.findElement(By.xpath("//div/a[@title=\"Manage Users\"]/dl/dd"));
        Assert.assertNotNull(element2, "Unable to locate element 'dd'. ");

        if (element1.getText().equals("Manage Users") &&
                element2.getText().equals("Create/delete/modify users that can log in to this Jenkins")) {
            forms_equal = true;
        }

        Assert.assertTrue(forms_equal);

        link.click();
        next_url = webDriver.getCurrentUrl();

    }

    @Test(dependsOnMethods = {"tstManageElements"})
    public void tstLinkCreateUser() {
        webDriver.get(next_url);
        WebElement element = webDriver.findElement(By.xpath("//div/a[@class=\"task-link\"][@href=\"addUser\"]"));
        Assert.assertTrue(element.getText().equals("Create User"));

        element.click();
        next_url = webDriver.getCurrentUrl();
    }

    @Test(dependsOnMethods = {"tstLinkCreateUser"})
    public void tstPageCreateUser() {
        webDriver.get(next_url);
        boolean isClear = false;

        WebElement form = webDriver.findElement(By.xpath("//table"));
        Assert.assertNotNull(form, "No forms found. ");

        WebElement text1 = form.findElement(By.xpath("//input[@name=\"username\"][@type=\"text\"]"));
        WebElement text2 = form.findElement(By.xpath("//input[@name=\"fullname\"][@type=\"text\"]"));
        WebElement text3 = form.findElement(By.xpath("//input[@name=\"email\"][@type=\"text\"]"));
        WebElement password1 = form.findElement(By.xpath("//input[@name=\"password1\"][@type=\"password\"]"));
        WebElement password2 = form.findElement(By.xpath("//input[@name=\"password2\"][@type=\"password\"]"));

        if (text1.getText().equals("") &&
                text2.getText().equals("") &&
                text3.getText().equals("") &&
                password1.getText().equals("") &&
                password2.getText().equals("")) {
            isClear = true;
        }
        Assert.assertTrue(isClear);
    }

    @Test(dependsOnMethods = {"tstPageCreateUser"})
    public void tstUserTable() {
        WebElement form = webDriver.findElement(By.xpath("//table"));
        Assert.assertNotNull(form, "No forms found. ");

        WebElement text1 = form.findElement(By.xpath("//input[@name=\"username\"][@type=\"text\"]"));
        WebElement text2 = form.findElement(By.xpath("//input[@name=\"fullname\"][@type=\"text\"]"));
        WebElement text3 = form.findElement(By.xpath("//input[@name=\"email\"][@type=\"text\"]"));
        WebElement password1 = form.findElement(By.xpath("//input[@name=\"password1\"][@type=\"password\"]"));
        WebElement password2 = form.findElement(By.xpath("//input[@name=\"password2\"][@type=\"password\"]"));

        text1.sendKeys("someuser");
        Assert.assertEquals(text1.getAttribute("value"), "someuser", "Unable to fill 'Username' field");

        password1.sendKeys("somepassword");
        Assert.assertEquals(password1.getAttribute("value"), "somepassword", "Unable to fill 'Password' field");

        password2.sendKeys("somepassword");
        Assert.assertEquals(password2.getAttribute("value"), "somepassword", "Unable to fill 'Confirm password' field");

        text2.sendKeys("Some Full Name");
        Assert.assertEquals(text2.getAttribute("value"), "Some Full Name", "Unable to fill 'Full Name' field");

        text3.sendKeys("some@addr.dom");
        Assert.assertEquals(text3.getAttribute("value"), "some@addr.dom", "Unable to fill 'E-mail address' field");

        WebElement button = webDriver.findElement(By.xpath("//form/span[@name=\"Submit\"]"));

        button.click();

        WebDriverWait wait = new WebDriverWait(webDriver, 5);
        wait.until(ExpectedConditions.numberOfElementsToBe(By.id("//main-panel"), 0));

        WebElement user = webDriver.findElement(By.xpath("//a[@href=\"user/someuser/\"]"));
        Assert.assertNotNull(user, "Unable to find table entry 'someuser'. ");
        next_url = webDriver.getCurrentUrl();
    }

    @Test(dependsOnMethods = {"tstUserTable"})
    public void tstDeleteUser() {
        webDriver.get(next_url);
        WebElement link = webDriver.findElement(By.xpath("//a[@href=\"user/someuser/delete\"]"));

        Assert.assertNotNull(link, "Unable to find 'user/someuser/delete' link element. ");

        link.click();
        WebDriverWait wait = new WebDriverWait(webDriver, 5);
        wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//form"), 2));

        WebElement form = webDriver.findElement(By.xpath("//form[@name=\"delete\"]"));
        Assert.assertEquals(form.getText(), "Are you sure about deleting the user from Jenkins?\nYes",
                "Unable to find text '«Are you sure about deleting the user from Jenkins?». '");
    }

    @Test(dependsOnMethods = {"tstDeleteUser"})
    public void tstAfterDeleteUser() {
        webDriver.findElement(By.cssSelector("link=Yes")).click();

        WebDriverWait wait = new WebDriverWait(webDriver, 5);
        wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//form"), 0));

        Collection<WebElement> table = webDriver.findElements(By.xpath("//table/tbody"));
        Iterator<WebElement> i = table.iterator();
        WebElement element;

        while (i.hasNext()) {
            element = i.next();
            if (element.getText().equals("someuser")) {
                Assert.fail("Unable to delete user 'someuser'. ");
            }
        }
    }

}
