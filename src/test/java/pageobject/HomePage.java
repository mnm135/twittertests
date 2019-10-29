package pageobject;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class HomePage extends BasePage {

    @FindBy(xpath = "//*[@data-testid='SideNav_NewTweet_Button']")
    public WebElement composeTweetLink;
    @FindBy(xpath = "//a[@aria-label='Profile']/div")
    public WebElement profileLink;
    @FindAll({@FindBy(xpath = "//section[contains(@aria-labelledby, 'accessible-list')]//article")})
    public List<WebElement> visibleTweets;
    @FindBy(xpath = "((//div[@data-testid='tweet'])[1]/div[2]//span)[5]")
    public WebElement lastTweet;
    @FindBy(xpath = "//span[contains(text(), ' sent.')]")
    public WebElement tweetSuccessfullySentNotification;

    @FindAll({@FindBy(xpath = "//div[@data-testid='tweet']")})
    public List<WebElement> tweets;

    @FindBy(xpath = "//input[@aria-label='Search query']")
    public WebElement searchInput;


    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void openTweetByPosition(int position) {
        tweets.get(position).click();
    }

    public void startAddingTweetProcess() {
        waitForElement(composeTweetLink);
        scrollToElement(composeTweetLink);
        composeTweetLink.click();
    }

    public void verifyThatAddedTweetWasAddedSuccessfully(String tweetContent) {
        Assertions.assertEquals(lastTweet.getText(), tweetContent);
        Assertions.assertTrue(tweetSuccessfullySentNotification.isDisplayed());
    }
}
