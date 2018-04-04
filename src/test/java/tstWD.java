import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class tstWD {

    String base_url = "http://localhost:8080";

    // Эмуляция verify
    StringBuffer verificationErrors = new StringBuffer();

    // Управление настройками Firefox
    FirefoxProfile firefoxProfile = new FirefoxProfile();

    WebDriver webDriver = null;

    @BeforeClass
    public void beforeClass() {
        System.setProperty("webdriver.gecko.driver", "/Users/lorem/tat/HT1/geckodriver");

        // Старт с пустой страницы браузера
        //firefoxProfile.setPreference("browser.startup.homepage", "about:blank");

        // Запуск драйвера (и браузера)
        webDriver = new FirefoxDriver(firefoxProfile);
    }

    @AfterClass
    public void afterClass() {
        /*
		try {
			Runtime.getRuntime().exec("taskkill /f /IM firefox.exe");
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/

        webDriver.quit();

        // Если в "накопителе сообщений об ошибках" что-то есть,
        // крэшим тест с соответствующим сообщением:
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            Assert.fail(verificationErrorString);
        }
    }

    @Test
    public void tstManageElements() {
        webDriver.get(base_url + "/manage");
        boolean forms_equal = false;
        boolean dtEquals = false;
        boolean ddEquals = false;
        boolean noForm = false;
        StringBuilder message = new StringBuilder();
        ArrayList<WebElement> forms = new ArrayList<WebElement>();
        forms.add(webDriver.findElement(By.name("dt")));
        if (forms.isEmpty()) {
            message.append("There is no element 'dt'. ");
            noForm = true;
        }
        forms.add(webDriver.findElement(By.name("dd")));
        if (noForm) {
            Assert.assertFalse(forms.isEmpty(), "There is no element 'dd'. ");

        } else {
            if (forms.isEmpty()) {
                Assert.assertFalse(forms.isEmpty(), "There is no element 'dd'. ");
            }
        }
        if (noForm) {
            Assert.assertFalse(forms.isEmpty(), "There is no elements 'dd' and 'dt' . ");
        }
        String dt, dd = null;
        if ((dt = forms.get(0).getText()) == null) {
            message.append("There is no text on element 'dt'. ");
        } else if ((dd = forms.get(0).getText()) == null) {
            message.append("There is no text on element 'dd'. ");
        }
        if (!dt.equals("Manage Users")) {
            message.append("Text on element 'dt' doesn't equal. ");
        } else {
            dtEquals = true;
        }

        if (dd.equals("Create/delete/modify users that can log in to this Jenkins")) {
            message.append("Text on element 'dd' doesn't equal. ");
        } else {
            ddEquals = true;
        }
        if (ddEquals && dtEquals) {
            forms_equal = true;
        }
        Assert.assertTrue(forms_equal, message.toString());

    }

    @BeforeMethod
    public void beforeTstLinkCreateUser() {
        webDriver.get(base_url + "/manage");
    }

    @Test
    public void tstLinkCreateUser() {
        boolean isEnabled = false;
        if (webDriver.findElement(By.linkText("Create User")).isEnabled()) {
            isEnabled = true;
        }
        Assert.assertTrue(isEnabled);
    }

    @BeforeMethod
    public void beforeTstPageCreateUser() {
        webDriver.get(base_url + "/manage");
        webDriver.findElement(By.linkText("Create User")).click();
    }

    @Test
    public void tstPageCreateUser() {
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

    @BeforeMethod
    public void beforeTstUserTable() {
        webDriver.get(base_url + "/manage");
        webDriver.findElement(By.linkText("Create User")).click();
    }

    @Test
    public void tstUserTable() {
        ArrayList<WebElement> forms = (ArrayList<WebElement>) webDriver.findElements(By.tagName("form"));
        Assert.assertFalse(forms.isEmpty(), "No forms found. ");
        Iterator<WebElement> i = forms.iterator();
        WebElement form = null;

        while (i.hasNext()) {
            form = i.next();

            form.findElement(By.name("Username")).clear();
            form.findElement(By.name("Username")).sendKeys("someuser");
            Assert.assertEquals(form.findElement(By.name("Username")).getAttribute("value"), "someuser", "Unable to fill 'Username' filed");

            form.findElement(By.name("Password")).clear();
            form.findElement(By.name("Password")).sendKeys("somepassword");
            Assert.assertEquals(form.findElement(By.name("Password")).getAttribute("value"), "somepassword", "Unable to fill 'Password' filed");

            form.findElement(By.name("Confirm password")).clear();
            form.findElement(By.name("Confirm password")).sendKeys("somepassword");
            Assert.assertEquals(form.findElement(By.name("Confirm password")).getAttribute("value"), "somepassword", "Unable to fill 'Confirm password' filed");

            form.findElement(By.name("Full name")).clear();
            form.findElement(By.name("Full name")).sendKeys("Some Full Name");
            Assert.assertEquals(form.findElement(By.name("Full name")).getAttribute("value"), "Some Full Name", "Unable to fill 'Full Name' filed");

            form.findElement(By.name("E-mail address")).clear();
            form.findElement(By.name("E-mail address")).sendKeys("some@addr.dom");
            Assert.assertEquals(form.findElement(By.name("E-mail address")).getAttribute("value"), "some@addr.dom", "Unable to fill 'E-mail address' filed");
        }


        form.findElement(By.xpath("//input[@type=\"submit\"]")).click();

        WebDriverWait wait = new WebDriverWait(webDriver, 5);
        wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//form"), 0));

        Assert.assertEquals(form.findElement(By.xpath("//tr/td")).getAttribute("value"), "someuser", "Unable to find table element with entered user name");

    }

    @Test
    public void tstDeleteUser() {
        WebElement form = null;


    }

}
