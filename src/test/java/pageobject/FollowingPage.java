package pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class FollowingPage extends BasePage {

    private static final String FOLLOWED_ACCOUNT_CELL_TEMPLATE = "//span[text()='%s']//ancestor::div[@data-testid='UserCell']";
    private static final String FOLLOW_UNFOLLOW_BUTTON_TEMPLATE = "(//span[text()='%s']//ancestor::div[@data-testid='UserCell']//div[@role='button']//span)[last()]";


    @FindBy(xpath = "//span[text()='@netguru']//ancestor::div[@data-testid='UserCell']")
    public WebElement followedAccountCell;

    @FindBy(xpath = "(//span[text()='@netguru']//ancestor::div[@data-testid='UserCell']//div[@role='button']//span)[last()]")
    public WebElement followButton;

    @FindBy(xpath = "//div[@data-testid='confirmationSheetConfirm']")
    public WebElement confirmUnfollow;


    public FollowingPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public WebElement getAccountCellByUserId(String userId) {
        return driver.findElement(By.xpath(String.format(FOLLOWED_ACCOUNT_CELL_TEMPLATE, userId)));
    }

    public WebElement getFollowUnfollowButtonByUserId(String userId) {
        return driver.findElement(By.xpath(String.format(FOLLOW_UNFOLLOW_BUTTON_TEMPLATE, userId)));
    }
}
