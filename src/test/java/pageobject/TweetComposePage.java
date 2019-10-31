package pageobject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TweetComposePage extends BasePage {

    private static final String TEMPLATE = "//div[@aria-label='%s']";

    @FindBy(xpath = "//div[@aria-label='Tweet text']")
    public WebElement tweetTextArea;
    @FindBy(xpath = "//div[@data-testid='addButton']")
    public WebElement multiTweetButton;
    @FindBy(xpath = "//div[@data-testid='tweetButton']")
    public WebElement sendTweetButton;
    @FindBy(xpath = "//div[@aria-label='Add emoji']")
    public WebElement addEmojiButton;
    @FindBy(xpath = "//div[@aria-label='Add poll']")
    public WebElement addPollButton;
    @FindBy(xpath = "//div[@aria-label='Add a GIF']")
    public WebElement addGifButton;
    @FindBy(xpath = "//div[@aria-label='Add photos or video']")
    public WebElement addPhotoButton;
    @FindBy(xpath = "//div[@aria-label='Close']")
    public WebElement closeWindowIcon;

    @FindBy(xpath = "(//*[@aria-label='Tweet text'])[2]")
    public WebElement multiTweet2ndTextArea;

    @FindBy(xpath = "//span[@style='background-color: rgb(255, 184, 194);']")
    public WebElement textOverLimit;

    public TweetComposePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @Step("Type in tweet text")
    public void writeTweet(String tweet) {
        tweetTextArea.sendKeys(tweet);
    }

    @Step("Write and send tweet")
    public void writeAndSendTweet(String tweet) {
        tweetTextArea.sendKeys(tweet);
        sendTweetButton.click();
    }

    @Step("Add {0} emoji")
    public void addEmojiByName(String name) {
        addEmojiButton.click();
        driver.findElement(By.xpath(String.format(TEMPLATE, name))).click();
        driver.findElement(By.xpath("//body")).click();
    }

    public void addUserMention(String userName) {
        tweetTextArea.sendKeys(" ");
        tweetTextArea.sendKeys(userName);
        tweetTextArea.sendKeys(Keys.SPACE);
    }
}
