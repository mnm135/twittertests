package pageobject;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class FollowingPage extends BasePage {

    private static final String FOLLOWED_ACCOUNT_CELL_TEMPLATE = "//span[text()='%s']//ancestor::div[@data-testid='UserCell']";

    @FindBy(xpath = "//span[text()='@netguru']//ancestor::div[@data-testid='UserCell']")
    public WebElement followedAccountCell;
    @FindBy(xpath = "(//span[text()='@netguru']//ancestor::div[@data-testid='UserCell']//div[@role='button']//span)[last()]")
    public WebElement followButton;
    @FindBy(xpath = "//div[@data-testid='confirmationSheetConfirm']")
    public WebElement confirmUnfollow;
    @FindAll({@FindBy(xpath = "//div[@data-testid='primaryColumn']//span//ancestor::div[@data-testid='UserCell']//div[@role='button']")})
    public List<WebElement> followButtonsList;

    public FollowingPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @Step("Unfollow {0} account")
    public void unFollowUserByPosition(int position) {
        followButtonsList.get(position).click();
        confirmUnfollow.click();
    }

    @Step("Verify that {0} is no longer visible on Following page")
    public void verifyThatUserIsNoLongerFollowed(String userId) {
        driver.navigate().refresh();
        Assertions.assertTrue(driver.findElements(By.xpath(String.format(FOLLOWED_ACCOUNT_CELL_TEMPLATE, userId))).isEmpty());
    }
}
