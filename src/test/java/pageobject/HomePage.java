package pageobject;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class HomePage extends BasePage {

    private static final String SEARCH_RESULT_TEMPLATE = "(//div[@data-testid='TypeaheadUser'])[1]/div//*[contains(text(), '%s')]//ancestor::div[@data-testid='TypeaheadUser']";

    @FindBy(xpath = "//*[@data-testid='SideNav_NewTweet_Button']")
    public WebElement composeTweetLink;
    @FindBy(xpath = "//a[@aria-label='Profile']/div")
    public WebElement profileLink;
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
        Assertions.assertEquals(tweetContent, lastTweet.getText());
        Assertions.assertTrue(tweetSuccessfullySentNotification.isDisplayed());
    }

    @Step("Find and go to {0} user profile page")
    public void searchAndGoToUserPage(String userId) {
        searchInput.sendKeys(userId);
        WebElement element = driver.findElement(By.xpath(String.format(SEARCH_RESULT_TEMPLATE, userId)));
        waitForElement(element);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        element.click();
    }

    //separate method to avoid StaleElementException after reloading page
    @Step("Add current user to following")
    public void followCurrentUser() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement followButton = driver.findElement(By.xpath("//div[@data-testid='primaryColumn']//div[contains(@data-testid, '-follow')]/div"));
        waitForElement(followButton);
        waitForElementToBeClickable(followButton);
        scrollToElement(followButton);
        ((JavascriptExecutor)driver).executeScript("arguments[0].click()", followButton);
    }
}
