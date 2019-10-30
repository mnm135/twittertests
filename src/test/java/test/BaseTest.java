package test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pageobject.LoginPage;

import java.util.concurrent.TimeUnit;

public class BaseTest {

    WebDriver driver;
    LoginPage loginPage;

    @BeforeEach
    void setUp() {
        //@TODO fix to handle headless correctly
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        //options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        login();
    }

    private void login() {
        loginPage = new LoginPage(driver);
        //@TODO move to pom
        driver.get("https://twitter.com");
        loginPage.loginLink.click();


        loginPage.loginButton.click();
    }

    @AfterEach
    void cleanUp() {
        //driver.close();
    }
}
