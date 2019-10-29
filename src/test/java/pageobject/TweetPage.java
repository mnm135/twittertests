package pageobject;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    public WebElement hashTagInTweet;
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

    public void deleteTweet() {
        moreIcon.click();
        deleteButton.click();
        confirmDeleteButton.click();
    }

    //@TODO move to helpers or whatever
    public String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        return formatter.format(date);
    }

    public void verifyThatTweetDataIsCorrectlyDisplayedOnTweetPage(String tweetText) {
        //@TODO parametrize in pom or somewhere
        Assertions.assertEquals(authorAccountName.getText(), "@Emil08345731");
        Assertions.assertEquals(authorName.getText(), "Emil132");
        Assertions.assertEquals(tweetContent.getText(), tweetText);
        Assertions.assertTrue(timeAndDate.getText().contains(getCurrentDate()));
    }
}