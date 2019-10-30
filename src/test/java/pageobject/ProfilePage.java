package pageobject;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ProfilePage extends BasePage {

    private static final String USERNAME_TEMPLATE = "(//span[text()='%s'])[2]";

    @FindBy(xpath = "//span[text()='Edit profile']")
    public WebElement editProfileButton;

    @FindBy(xpath = "//div[@data-testid='UserDescription']/span")
    public WebElement userBio;
    @FindBy(xpath = "(//div[@data-testid='UserProfileHeader_Items']//span)[3]")
    public WebElement userLocation;
    @FindBy(xpath = "//div[@data-testid='UserProfileHeader_Items']/a")
    public WebElement userWebsite;


    public ProfilePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void verifyUserDataIsCorrect(String name, String bio, String location, String website) {
        Assertions.assertTrue(driver.findElement(By.xpath(String.format(USERNAME_TEMPLATE, name))).isDisplayed());
        Assertions.assertEquals(userBio.getText(), bio);
        Assertions.assertEquals(userLocation.getText(), location);
        Assertions.assertEquals(userWebsite.getText(), website.substring(4));
    }
}
