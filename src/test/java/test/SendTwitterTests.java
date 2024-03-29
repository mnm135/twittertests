package test;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.Keys;
import pageobject.HomePage;
import pageobject.ProfilePage;
import pageobject.TweetComposeComponent;
import pageobject.TweetPage;

import static io.qameta.allure.Allure.step;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Feature("User can sent tweets")
class SendTwitterTests extends BaseTest {

    @DisplayName("Tweet send button is disabled when the tweet text area is empty")
    @Test
    void tweetWindowIsDisabledWhenEmpty() {
        homePage = new HomePage(driver);
        tweetComposeComponent = new TweetComposeComponent(driver);

        homePage.startAddingTweetProcess();
        Assertions.assertEquals("true", tweetComposeComponent.sendTweetButton.getAttribute("aria-disabled"));
        tweetComposeComponent.writeTweet("a");
        Assertions.assertEquals(null, tweetComposeComponent.sendTweetButton.getAttribute("aria-disabled"));
    }

    @ParameterizedTest(name = "User can successfully send tweets")
    @CsvFileSource(resources = "/testdata/tweetContent.csv", delimiter = ';')
    void userCanAddTweet(String tweetContent) {
        homePage = new HomePage(driver);
        tweetComposeComponent = new TweetComposeComponent(driver);
        tweetPage = new TweetPage(driver);
        profilePage = new ProfilePage(driver);

        homePage.profileLink.click();
        String userName = profilePage.userName.getText();
        String userAccountName = profilePage.userAccountName.getText();
        homePage.startAddingTweetProcess();
        tweetComposeComponent.writeAndSendTweet(tweetContent);
        homePage.waitForElement(homePage.tweetSuccessfullySentNotification);
        homePage.verifyThatAddedTweetWasAddedSuccessfully(tweetContent);
        homePage.openTweetByPosition(0);
        tweetPage.verifyThatTweetDataIsCorrectlyDisplayedOnTweetPage(tweetContent, userName, userAccountName);

        cleanTweets();
    }

    @ParameterizedTest(name = "User is not able add too long tweet")
    @CsvFileSource(resources = "/testdata/tweetContent.csv", delimiter = ';')
    void userCantAddTooLongTweet(String tweetContent, String additionalCharacters) {
        homePage = new HomePage(driver);
        tweetComposeComponent = new TweetComposeComponent(driver);
        tweetPage = new TweetPage(driver);

        homePage.startAddingTweetProcess();
        tweetComposeComponent.writeTweet(tweetContent);
        tweetComposeComponent.writeTweet(additionalCharacters);
        step("Verify that text over tweet limit is highlighted", (step) -> {
            Assertions.assertEquals(additionalCharacters, tweetComposeComponent.textOverLimit.getText());
        });
    }

    @ParameterizedTest(name = "User is able to send tweet with emoji")
    @CsvSource({
            "this is tweet with emoji, Grinning face, https://abs-0.twimg.com/emoji/v2/svg/1f600.svg",
            "emoji heart eyes, Smiling face with heart-shaped eyes, https://abs-0.twimg.com/emoji/v2/svg/1f60d.svg"
    })
    void userCanAddTweetWithEmoji(String tweetText, String emojiName, String emojiUrl) {
        homePage = new HomePage(driver);
        tweetComposeComponent = new TweetComposeComponent(driver);
        tweetPage = new TweetPage(driver);

        homePage.startAddingTweetProcess();
        tweetComposeComponent.writeTweet(tweetText);
        tweetComposeComponent.addEmojiByName(emojiName);
        tweetComposeComponent.sendTweetButton.click();
        homePage.waitForElement(homePage.tweetSuccessfullySentNotification);
        homePage.openTweetByPosition(0);
        tweetPage.verifyThatTweetTextIsCorrectlyDisplayed(tweetText);
        step("Verify that emojij is displayed correctly in tweet", (step) -> {
            Assertions.assertEquals(emojiUrl, tweetPage.emojiInTweet.getAttribute("src"));
        });

        cleanTweets();
    }

    @ParameterizedTest(name = "User is able to add multiple chained tweets")
    @CsvSource({
            "asdd, zxcd",
            "tweet numer jeden, tweet numer dwa",
            "troche dłuższy tweet numer jeden, troche dłuższyyyyyyyy tweet numer dwa"
    })
    void userCanAddMultiTweet(String firstTweetText, String secondTweetText) {
        homePage = new HomePage(driver);
        tweetComposeComponent = new TweetComposeComponent(driver);
        tweetPage = new TweetPage(driver);

        homePage.startAddingTweetProcess();
        tweetComposeComponent.writeTweet(firstTweetText);
        step("Add another chained tweet", (step) -> {
            tweetComposeComponent.multiTweetButton.click();
            tweetComposeComponent.multiTweet2ndTextArea.sendKeys(secondTweetText);
            tweetComposeComponent.sendTweetButton.click();
        });
        homePage.waitForElement(homePage.tweetSuccessfullySentNotification);
        homePage.openTweetByPosition(0);
        step("Verify that all tweets were added correctly", (step) -> {
            Assertions.assertEquals(firstTweetText, tweetPage.tweetContent.getText());
            Assertions.assertEquals(secondTweetText, tweetPage.multiTweet2ndTweetContent.getText());
        });
        cleanTweets();
    }

    @ParameterizedTest(name = "User can send tweet with external page link")
    @CsvSource({
            "https://www.netguru.com/, Netguru – Custom software development company"
    })
    void userCanAddLinkToExternalPage(String link, String pageTitle) {
        homePage = new HomePage(driver);
        tweetComposeComponent = new TweetComposeComponent(driver);
        tweetPage = new TweetPage(driver);

        homePage.startAddingTweetProcess();
        tweetComposeComponent.writeAndSendTweet(link);
        homePage.waitForElement(homePage.tweetSuccessfullySentNotification);
        homePage.openTweetByPosition(0);
        homePage.switchToNewTab();
        step("Verify that link leads to a correct external page", (step) -> {
            Assertions.assertEquals(pageTitle, driver.getTitle());
        });
        cleanTweets();
    }

    @ParameterizedTest(name = "User is able to add tweet with hashtag")
    @CsvSource({
            "tweet with hashtag, #has",
            "some more test data, #kolejnytag"
    })
    void userCanAddTweetWithHashtag(String tweetText, String hashtag) {
        homePage = new HomePage(driver);
        tweetComposeComponent = new TweetComposeComponent(driver);
        tweetPage = new TweetPage(driver);

        homePage.startAddingTweetProcess();
        tweetComposeComponent.writeTweet((String.format("%s %s", tweetText, hashtag)));
        tweetComposeComponent.tweetTextArea.sendKeys(Keys.SPACE);
        tweetComposeComponent.waitForElementToBeClickable(tweetComposeComponent.sendTweetButton);
        tweetComposeComponent.sendTweetButton.click();
        homePage.waitForElement(homePage.tweetSuccessfullySentNotification);
        homePage.openTweetByPosition(0);

        step("Verify that tweet contains correct text and hashtag", (step) -> {
            Assertions.assertEquals(tweetText + " ", tweetPage.tweetContent.getText());
            Assertions.assertEquals(hashtag, tweetPage.mediaInTweet.getText());
        });
        step("Verify that user can search for similar tweets when clicking at hashtag", (step) -> {
            tweetPage.mediaInTweet.click();
            Assertions.assertEquals(hashtag, homePage.searchInput.getAttribute("value"));
        });
        cleanTweets();
    }

    @ParameterizedTest(name = "User can @mention other user in his tweet")
    @CsvSource({"asd, @Emil08345731"})
    void userCanSendTweetWithUserMention(String tweetText, String userMention) {
        homePage = new HomePage(driver);
        tweetComposeComponent = new TweetComposeComponent(driver);
        tweetPage = new TweetPage(driver);

        homePage.startAddingTweetProcess();
        tweetComposeComponent.writeTweet(tweetText);
        tweetComposeComponent.addUserMention(userMention);
        tweetComposeComponent.sendTweetButton.click();
        homePage.waitForElement(homePage.tweetSuccessfullySentNotification);
        homePage.openTweetByPosition(0);
        step("Verify that tweet contains correct content", (step) -> {
            Assertions.assertEquals(tweetText + " ", tweetPage.tweetContent.getText());
            Assertions.assertEquals(userMention, tweetPage.mediaInTweet.getText());
        });
        step("Verify that @mention contains correct url to access mentioned user's profile", (step) -> {
            Assertions.assertEquals(userMention.replace("@", "/"), tweetPage.mediaInTweet.getAttribute("href").replace("https://twitter.com", ""));
        });
        cleanTweets();
    }
}