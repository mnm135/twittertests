package pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    WebDriver driver;

    @FindBy(xpath = "//div[@class='clearfix field']//input[@name='session[username_or_email]']")
    public WebElement loginInput;
    @FindBy(xpath = "//div[@class='clearfix field']//input[@name='session[password]']")
    public WebElement password;
    @FindBy(xpath = "//button[@type='submit']")
    public WebElement loginButton;
    @FindBy(xpath = "(//*[@href='/login'])[1]")
    public WebElement loginLink;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
}
