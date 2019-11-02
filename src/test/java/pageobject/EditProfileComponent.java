package pageobject;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class EditProfileComponent extends BasePage {

    public static final int NAME_MAX_LENGTH = 50;
    public static final int BIO_MAX_LENGTH = 160;
    public static final int LOCATION_MAX_LENGTH = 30;
    public static final int WEBSITE_MAX_LENGTH = 100;

    public static final String DEFAULT_NAME = "default name";
    public static final String DEFAULT_BIO = "default bio";
    public static final String DEFAULT_LOCATION = "Warsaw";
    public static final String DEFAULT_WEBSITE = "www.google.pl";

    @FindBy(xpath = "//div[@aria-labelledby='modal-header']")
    public WebElement editProfileWindow;
    @FindBy(xpath = "//div[@aria-labelledby='modal-header']//span[text()='Edit profile']")
    public WebElement editProfileLabel;
    @FindBy(xpath = "//input[@name='name']")
    public WebElement nameInput;
    @FindBy(xpath = "//textarea[@name='description']")
    public WebElement bioInput;
    @FindBy(xpath = "//input[@name='location']")
    public WebElement locationInput;
    @FindBy(xpath = "//input[@name='url']")
    public WebElement websiteInput;
    @FindBy(xpath = "(//input[@type='file'])[1]")
    public WebElement addBannerPhotoInput;
    @FindBy(xpath = "(//input[@type='file'])[2]")
    public WebElement addAvatarPhotoInput;
    @FindBy(xpath = "//span[text()='Save']")
    public WebElement saveProfileButton;
    @FindBy(xpath = "//span[text()='Url is not valid']")
    public WebElement incorrectUrlMessage;

    public EditProfileComponent(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @Step("Edit profile data")
    public void editProfileData(String name, String bio, String location, String website) {
        nameInput.clear();
        nameInput.sendKeys(name);
        bioInput.clear();
        bioInput.sendKeys(bio);
        scrollToElement(locationInput);
        locationInput.clear();
        locationInput.sendKeys(location);
        scrollToElement(locationInput);
        websiteInput.clear();
        websiteInput.sendKeys(website);
    }

    @Step("Change banner photo")
    public void changeBannerPhoto(String photoPath) {
        addBannerPhotoInput.sendKeys(photoPath);
    }

    @Step("Change avatar photo")
    public void changeAvatarPhoto(String photoPath) {
        addAvatarPhotoInput.sendKeys(photoPath);
    }

    @Step("Verify edited values are correctly reflected in edit profile form")
    public void verifyDataInEditForm(String name, String bio, String location, String website) {
        scrollToElement(nameInput);
        Assertions.assertEquals(nameInput.getAttribute("value"), name);
        scrollToElement(bioInput);
        Assertions.assertEquals(bioInput.getAttribute("value").replaceAll("(\\r|\\n|\\r\\n)+", "\\\\n"), bio);
        scrollToElement(bioInput);
        Assertions.assertEquals(locationInput.getAttribute("value"), location);
        scrollToElement(websiteInput);
        Assertions.assertEquals(websiteInput.getAttribute("value"), website);
    }
}
