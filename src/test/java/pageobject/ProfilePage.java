package pageobject;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class ProfilePage extends BasePage {

    private static final String USERNAME_TEMPLATE = "(//span[text()='%s'])[2]";
    private static final String FOLLOWED_USER_TEMPLATE = "//span[contains(text(), '%s')]";

    @FindBy(xpath = "//span[text()='Edit profile']")
    public WebElement editProfileButton;

    @FindBy(xpath = "//div[@data-testid='UserDescription']/span")
    public WebElement userBio;
    @FindBy(xpath = "(//div[@data-testid='UserProfileHeader_Items']//span)[3]")
    public WebElement userLocation;
    @FindBy(xpath = "//div[@data-testid='UserProfileHeader_Items']/a")
    public WebElement userWebsite;
    @FindBy(xpath = "(//span[contains(text(), '@')])[1]")
    public WebElement userAccountName;

    @FindBy(xpath = "//span[text()='Following']")
    public WebElement followingButton;


    @FindAll({@FindBy(xpath = "//article")})
    public List<WebElement> tweets;
    @FindBy(xpath = "(//article)[1]")
    public WebElement lastTweet;
    @FindBy(xpath = "((//article)[1]//div/span)[4]")
    public WebElement lastTweetContent;
    @FindBy(xpath = "(//article)[1]//div[@data-testid='like']")
    public WebElement lastTweetLikeButton;

    @FindBy(xpath = "(//div[@role='tablist'])[2]//span[text()='Likes']")
    public WebElement likesNavigationButton;

    @FindBy(xpath = "(//article)[1]//a[contains(@href, 'status') and @role='link']")
    public WebElement lastTweetLink;


    public ProfilePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @Step("Verify edited user data is correctly reflected on user profile page")
    public void verifyUserDataIsCorrect(String name, String bio, String location, String website) {
        Assertions.assertTrue(driver.findElement(By.xpath(String.format(USERNAME_TEMPLATE, name))).isDisplayed());
        Assertions.assertEquals(userBio.getText().replaceAll("(\\r|\\n|\\r\\n)+", "\\\\n"), bio);
        Assertions.assertEquals(userLocation.getText(), location);
        Assertions.assertEquals(userWebsite.getText(), website.substring(4));
    }

    @Step("Verify {0} user is visible as followed")
    public void verifyUserIsVisibleInFollowing(String userId) {
        Assertions.assertTrue(driver.findElement(By.xpath(String.format(FOLLOWED_USER_TEMPLATE, userId))).isDisplayed());
    }

    public void waitForEditPageToDisappear()  {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.urlContains(userAccountName.getText().substring(1)));
    }
}
