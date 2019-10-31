package pageobject;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class HomePage extends BasePage {

    private static final String SEARCH_RESULT_TEMPLATE = "(//div[@data-testid='TypeaheadUser'])[1]/div//*[contains(text(), '%s')]";

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
    @FindBy(xpath = "(//div[@data-testid='TypeaheadUser'])[1]/div")
    public WebElement firstFoundResult;
    @FindBy(xpath = "(//div[contains(@data-testid, '-follow')])[1]")
    public WebElement followButton;


    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @Step("Open {0} tweet")
    public void openTweetByPosition(int position) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tweets.get(position).click();
    }

    @Step("Navigate to compose tweet page")
    public void startAddingTweetProcess() {
        waitForElement(composeTweetLink);
        scrollToElement(composeTweetLink);
        composeTweetLink.click();
    }

    @Step("Verify that tweed was added successfully")
    public void verifyThatAddedTweetWasAddedSuccessfully(String tweetContent) {
        Assertions.assertEquals(lastTweet.getText(), tweetContent);
        Assertions.assertTrue(tweetSuccessfullySentNotification.isDisplayed());
    }

    @Step("Find and go to {0} user profile page")
    public void searchForUser(String userId) {
        searchInput.sendKeys(userId);
        waitForElement(firstFoundResult);
        waitForElementToBeClickable(firstFoundResult);
        waitForElement(driver.findElement(By.xpath(String.format(SEARCH_RESULT_TEMPLATE, userId))));
        driver.findElement(By.xpath(String.format(SEARCH_RESULT_TEMPLATE, userId))).click();
        //waitForElementToDisappear(driver.findElement(By.xpath(String.format(SEARCH_RESULT_TEMPLATE, userId))));
    }

    //separate method to avoid StaleElementException after reloading page
    //@TODO can be written nicer
    @Step("Add current user to following")
    public void followCurrentUser() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement followButton = driver.findElement(By.xpath("(//div[contains(@data-testid, '-follow')])[1]/div"));
        waitForElement(followButton);
        waitForElementToBeClickable(followButton);
        scrollToElement(followButton);
        //followButton.click();
        ((JavascriptExecutor)driver).executeScript("arguments[0].click()", followButton);
    }
}
