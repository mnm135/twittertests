package pageobject;

import helper.Datehelper;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TweetPage extends BasePage {

    @FindBy(xpath = "(//div[@data-testid='tweet']/div[2]//a//span)[2]")
    public WebElement authorName;
    @FindBy(xpath = "(//div[@data-testid='tweet']/div[2]//a//span)[3]")
    public WebElement authorAccountName;
    @FindBy(xpath = "(//article/div//div[3]//span)[1]")
    public WebElement tweetContent;
    @FindBy(xpath = "(//article/div//div[4])//span[1]/span")
    public WebElement timeAndDate;
    @FindBy(xpath = "(//article/div//div[3]//span)//img")
    public WebElement emojiInTweet;
    @FindBy(xpath = "(//article/div//div[3]//span)[2]/a")
    public WebElement mediaInTweet;
    @FindBy(xpath = "(//div[@aria-label='More'])[1]")
    public WebElement moreIcon;
    @FindBy(xpath = "(//div[@role='menuitem'])[1]")
    public WebElement deleteButton;
    @FindBy(xpath = "//span[text()='Delete']")
    public WebElement confirmDeleteButton;
    @FindBy(xpath = "((//article)[2]/div//div[3]//span)[1]")
    public WebElement multiTweet2ndTweetContent;

    public TweetPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @Step("Delete tweet")
    public void deleteTweet() {
        moreIcon.click();
        deleteButton.click();
        confirmDeleteButton.click();
    }

    @Step("Verify that tweet contains correct content and user data")
    public void verifyThatTweetDataIsCorrectlyDisplayedOnTweetPage(String tweetText, String userName, String userAccountName) {
        Assertions.assertEquals(userAccountName, authorAccountName.getText());
        Assertions.assertEquals(userName, authorName.getText());
        Assertions.assertEquals(tweetText, tweetContent.getText());
        Assertions.assertTrue(timeAndDate.getText().contains(Datehelper.getCurrentDate()));
    }

    @Step("Verify that tweet contains correct text")
    public void verifyThatTweetTextIsCorrectlyDisplayed(String tweetText) {
        Assertions.assertEquals(tweetText, tweetContent.getText());
    }
}
