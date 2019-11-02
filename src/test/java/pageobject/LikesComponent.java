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

public class LikesComponent extends BasePage {

    private static final String HREF_TEMPLATE = "//*[@href='%s']";

    @FindBy(xpath = "(//article)[1]//a[contains(@href, 'status') and @role='link']")
    public WebElement lastTweetLink;
    @FindAll({@FindBy(xpath = "//article")})
    public List<WebElement> tweets;
    @FindBy(xpath = "(//article)[1]//div[@data-testid='unlike']")
    public WebElement lastTweetUnlikeButton;

    public LikesComponent(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @Step("Verify that tweet with {0} href is visible")
    public void verifyThatTweetIsVisibleByHref(String href) {
        scrollToElement(lastTweetLink);
        href = href.replace("https://twitter.com", "");
        Assertions.assertTrue(driver.findElement(By.xpath(String.format(HREF_TEMPLATE, href))).isDisplayed());
    }
}
