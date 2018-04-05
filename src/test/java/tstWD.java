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
    WebElement someElement = null;
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
        WebElement panel = webDriver.findElement(By.xpath("//div/a[@title=\"Manage Users\"]"));
        someElement = panel;

        WebElement element1 = webDriver.findElement(By.xpath("//div/a[@title=\"Manage Users\"]/dl/dt"));
        Assert.assertNotNull(element1, "Unable to locate element 'dt'. ");

        WebElement element2 = webDriver.findElement(By.xpath("//div/a[@title=\"Manage Users\"]/dl/dd"));
        Assert.assertNotNull(element2, "Unable to locate element 'dd'. ");

        if (element1.getText().equals("Manage Users") &&
                element2.getText().equals("Create/delete/modify users that can log in to this Jenkins")) {
            forms_equal = true;
        }

        Assert.assertTrue(forms_equal);

        panel.click();
        next_url = webDriver.getCurrentUrl();

    }

    @Test(dependsOnMethods = {"tstManageElements"})
    public void tstLinkCreateUser() {
        webDriver.get(next_url);
        WebElement element = webDriver.findElement(By.xpath("//div/a[@class=\"task-link\"][@href=\"addUser\"]"));
        Assert.assertTrue(element.getText().equals("Create User"));
    }

    @Test
    public void tstPageCreateUser() {

        webDriver.get(base_url + "/manage");
        webDriver.findElement(By.linkText("Create User")).click();
        WebElement text1 = null, text2 = null, text3 = null, password1 = null, password2 = null;
        boolean isClear = false;

        Collection<WebElement> forms = webDriver.findElements(By.tagName("form"));
        Assert.assertFalse(forms.isEmpty(), "No forms found. ");

        Iterator<WebElement> i = forms.iterator();
        boolean formFound = false;
        WebElement form;

        while (i.hasNext()) {
            form = i.next();
            if (((text1 = form.findElement(By.xpath("//form[1]"))).getAttribute("type").equalsIgnoreCase("text")) &&
                    ((text2 = form.findElement(By.xpath("//form[2]"))).getAttribute("type").equalsIgnoreCase("text")) &&
                    ((text3 = form.findElement(By.xpath("//form[3]"))).getAttribute("type").equalsIgnoreCase("text")) &&
                    ((password1 = form.findElement(By.xpath("//form[4]"))).getAttribute("type").equalsIgnoreCase("password")) &&
                    ((password2 = form.findElement(By.xpath("//form[5]"))).getAttribute("type").equalsIgnoreCase("password"))) {
                formFound = true;
                break;
            }
        }

        Assert.assertTrue(formFound, "There is no such forms. ");

        if (text1.getText().equals("") &&
                text2.getText().equals("") &&
                text3.getText().equals("") &&
                password1.getText().equals("") &&
                password2.getText().equals("")) {
            isClear = true;
        }
        Assert.assertTrue(isClear);
    }

    @Test
    public void tstUserTable() {

        webDriver.get(base_url + "/manage");
        webDriver.findElement(By.linkText("Create User")).click();
        WebElement form = webDriver.findElement(By.xpath("//form"));
        WebElement tableCell;

        tableCell = form.findElement(By.xpath("//tr[1]"));
        if (tableCell.findElement(By.xpath("td[1]")).getText().equals("Username")) {
            tableCell.findElement(By.xpath("//td[2]")).clear();
            tableCell.findElement(By.xpath("//td[2]")).sendKeys("someuser");
            Assert.assertEquals(form.findElement(By.name("Username")).getAttribute("value"), "someuser", "Unable to fill 'Username' field");
        } else {
            Assert.fail("Unable to find 'Username' field. ");
        }

        tableCell = form.findElement(By.xpath("//tr[2]"));
        if (tableCell.findElement(By.xpath("//td[1]")).getText().equals("Password")) {
            form.findElement(By.name("Password")).clear();
            form.findElement(By.name("Password")).sendKeys("somepassword");
            Assert.assertEquals(form.findElement(By.name("Password")).getAttribute("value"), "somepassword", "Unable to fill 'Password' field");
        } else {
            Assert.fail("Unable to find 'Password' field. ");
        }

        tableCell = form.findElement(By.xpath("//tr[3]"));
        if (tableCell.findElement(By.xpath("//td[1]")).getText().equals("Confirm password")) {
            form.findElement(By.name("Confirm password")).clear();
            form.findElement(By.name("Confirm password")).sendKeys("somepassword");
            Assert.assertEquals(form.findElement(By.name("Confirm password")).getAttribute("value"), "somepassword", "Unable to fill 'Confirm password' field");
        } else {
            Assert.fail("Unable to find 'Confirm password' field. ");
        }

        tableCell = form.findElement(By.xpath("//tr[4]"));
        if (tableCell.findElement(By.xpath("//td[1]")).getText().equals("Full name")) {
            form.findElement(By.name("Full name")).clear();
            form.findElement(By.name("Full name")).sendKeys("Some Full Name");
            Assert.assertEquals(form.findElement(By.name("Full name")).getAttribute("value"), "Some Full Name", "Unable to fill 'Full Name' field");
        } else {
            Assert.fail("Unable to find 'Full name' field. ");
        }

        tableCell = form.findElement(By.xpath("//tr[5]"));
        if (tableCell.findElement(By.xpath("//td[1]")).getText().equals("E-mail address")) {
            form.findElement(By.name("E-mail address")).clear();
            form.findElement(By.name("E-mail address")).sendKeys("some@addr.dom");
            Assert.assertEquals(form.findElement(By.name("E-mail address")).getAttribute("value"), "some@addr.dom", "Unable to fill 'E-mail address' field");
        } else {
            Assert.fail("Unable to find 'E-mail address' field. ");
        }

        form.findElement(By.xpath("//input[@type=\"submit\"]")).click();

        WebDriverWait wait = new WebDriverWait(webDriver, 5);
        wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//form"), 0));

        Assert.assertEquals(form.findElement(By.xpath("//tr/td")).getAttribute("value"), "someuser", "Unable to find table element with entered user name");

    }

    @Test(dependsOnMethods = {"tstUserTable"})
    public void tstDeleteUser() {
        WebElement element = webDriver.findElement(By.cssSelector("link=user/someuser/delete"));

        Assert.assertEquals(element.getAttribute("href"), "user/someuser/delete", "Unable to find 'user/someuser/delete' link element. ");

        element.click();
        WebDriverWait wait = new WebDriverWait(webDriver, 5);
        wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//form"), 0));

        Assert.assertEquals(webDriver.findElement(By.xpath("//table/tbody/tr[1]/td[1]")).getText(), "«Are you sure about deleting the user from Jenkins?»",
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
