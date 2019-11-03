package test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pageobject.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class BaseTest {

    WebDriver driver;
    ProfilePage profilePage;
    TweetPage tweetPage;
    HomePage homePage;
    LikesComponent likesComponent;
    FollowingPage followingPage;
    EditProfileComponent editProfileComponent;
    TweetComposeComponent tweetComposeComponent;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--lang=en_GB");
        options.addArguments("--headless");
        options.addArguments("window-size=1920x1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        login();
    }

    private void login() {
        LoginPage loginPage = new LoginPage(driver);
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("user.properties");
            Properties properties = new Properties();
            if (inputStream!=null) {
                properties.load(inputStream);
            } else {
                throw new NullPointerException("user.properties file not found");
            }
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            String url = properties.getProperty("loginpage");
            driver.get(url);
            loginPage.waitForLoginPageToLoad();
            loginPage.loginInput.sendKeys(username);
            loginPage.passwordInput.sendKeys(password);
        } catch (IOException e) {
            e.printStackTrace();
        }
        loginPage.loginButton.click();
    }

    @AfterEach
    void cleanUp() {
        driver.quit();
    }

    void cleanTweets() {
        profilePage = new ProfilePage(driver);
        tweetPage = new TweetPage(driver);
        homePage = new HomePage(driver);

        if (driver.getCurrentUrl().contains("twitter")) {
            homePage.profileLink.click();
        } else {
            driver.get("https://twitter.com");
            homePage.profileLink.click();
        }
        profilePage.waitForElement(profilePage.editProfileButton);
        while (profilePage.tweets.size() > 0) {
            profilePage.scrollToElement(profilePage.lastTweet);
            profilePage.lastTweet.click();
            tweetPage.deleteTweet();
            homePage.profileLink.click();
        }
    }

    void cleanLikes() {
        profilePage = new ProfilePage(driver);
        tweetPage = new TweetPage(driver);
        homePage = new HomePage(driver);
        likesComponent = new LikesComponent(driver);

        homePage.profileLink.click();
        profilePage.likesNavigationButton.click();
        while(likesComponent.tweets.size() > 0) {
            likesComponent.scrollToElement(likesComponent.lastTweetUnlikeButton);
            likesComponent.lastTweetUnlikeButton.click();
            driver.navigate().refresh();
        }
    }

    void cleanFollows() {
        profilePage = new ProfilePage(driver);
        tweetPage = new TweetPage(driver);
        homePage = new HomePage(driver);
        followingPage = new FollowingPage(driver);

        homePage.profileLink.click();
        profilePage.followingButton.click();
        int noOfFollowedUsers = followingPage.followButtonsList.size();
        if(noOfFollowedUsers > 0) {
            for (int i=0; i<noOfFollowedUsers; i++) {
                followingPage.scrollToElement(followingPage.followButtonsList.get(i));
                followingPage.unFollowUserByPosition(i);
            }
        }
    }
}
