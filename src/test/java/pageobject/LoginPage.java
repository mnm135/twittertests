package pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class LoginPage extends BasePage {

    @FindBy(xpath = "//div[@class='clearfix field']//input[@name='session[username_or_email]']")
    public WebElement loginInput;
    @FindBy(xpath = "//div[@class='clearfix field']//input[@name='session[password]']")
    public WebElement passwordInput;
    @FindBy(xpath = "//button[@type='submit']")
    public WebElement loginButton;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void waitForLoginPageToLoad() {
        waitForMultipleElements(List.of(loginInput, passwordInput, loginButton));
    }
}
