package pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ProfilePage extends BasePage {

    @FindBy(xpath = "((//*[contains(@aria-labelledby, 'accessible-list-')]//article)[1]//span)[5]")
    public WebElement lastTweet;
    @FindBy(xpath = "(//*[contains(@aria-labelledby, 'accessible-list-')]//article)[1]//div[@aria-label='More']")
    public WebElement moreButtonOfLastTweet;
    @FindBy(xpath = "(//div[@role='menu']//div[@role='menuitem'])[1]")
    public WebElement deleteButton;
    @FindBy(xpath = "(//span[contains(text(),'Delete')])[2]")
    public WebElement confirmDeleteButton;

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
}
