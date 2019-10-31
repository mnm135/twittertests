package pageobject;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LikesComponent extends BasePage {

    private static final String HREF_TEMPLATE = "//*[@href='%s']";

    @FindBy(xpath = "(//article)[1]//a[contains(@href, 'status') and @role='link']")
    public WebElement lastTweetLink;

    public LikesComponent(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void verifyThatTweetIsVisibleByHref(String href) {
        scrollToElement(lastTweetLink);
        href = href.replace("https://twitter.com", "");
        Assertions.assertTrue(driver.findElement(By.xpath(String.format(HREF_TEMPLATE, href))).isDisplayed());
    }
}
