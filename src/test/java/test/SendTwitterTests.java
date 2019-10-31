package test;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.Keys;
import pageobject.HomePage;
import pageobject.TweetComposePage;
import pageobject.TweetPage;

import static io.qameta.allure.Allure.step;

@Feature("User can sent tweets")
class SendTwitterTests extends BaseTest {

    private HomePage homePage;
    private TweetComposePage tweetComposePage;
    private TweetPage tweetPage;

    @DisplayName("Tweet send button is disabled when the tweet text area is empty")
    @Test
    void tweetWindowIsDisabledWhenEmpty() {
        homePage = new HomePage(driver);
        tweetComposePage = new TweetComposePage(driver);
        homePage.startAddingTweetProcess();
        Assertions.assertEquals(tweetComposePage.sendTweetButton.getAttribute("aria-disabled"), "true");
        tweetComposePage.writeTweet("a");
        Assertions.assertNull(tweetComposePage.sendTweetButton.getAttribute("aria-disabled"));
    }

    @ParameterizedTest(name = "User can successfully send tweets")
    @CsvFileSource(resources = "/tweetContent.csv", delimiter = ';')
    void userCanAddTweet(String tweetContent) {
        homePage = new HomePage(driver);
        tweetComposePage = new TweetComposePage(driver);
        tweetPage = new TweetPage(driver);
        homePage.startAddingTweetProcess();
        tweetComposePage.writeAndSendTweet(tweetContent);
        homePage.waitForElement(homePage.tweetSuccessfullySentNotification);
        homePage.verifyThatAddedTweetWasAddedSuccessfully(tweetContent);
        homePage.openTweetByPosition(0);
        tweetPage.verifyThatTweetDataIsCorrectlyDisplayedOnTweetPage(tweetContent);
        tweetPage.deleteTweet();
    }

    @ParameterizedTest(name = "User is not able add too long tweet")
    @CsvFileSource(resources = "/tweetContent.csv", delimiter = ';')
    void userCantAddTooLongTweet(String tweetContent, String additionalCharacters) {
        homePage = new HomePage(driver);
        tweetComposePage = new TweetComposePage(driver);
        tweetPage = new TweetPage(driver);
        homePage.startAddingTweetProcess();
        tweetComposePage.writeTweet(tweetContent);
        tweetComposePage.writeTweet(additionalCharacters);
        step("Verify that text over tweet limit is highlighted", (step) -> {
            Assertions.assertEquals(tweetComposePage.textOverLimit.getText(), additionalCharacters);
        });
    }

    //@TODO add handling more than one emoji per tweet
    @ParameterizedTest(name = "User is able to send tweet with emoji")
    @CsvSource({
            "this is tweet with emoji, Grinning face, https://abs-0.twimg.com/emoji/v2/svg/1f600.svg",
            "emoji heart eyes, Smiling face with heart-shaped eyes, https://abs-0.twimg.com/emoji/v2/svg/1f60d.svg"
    })
    void userCanAddTweetWithEmoji(String tweetText, String emojiName, String emojiUrl) {
        homePage = new HomePage(driver);
        tweetComposePage = new TweetComposePage(driver);
        tweetPage = new TweetPage(driver);
        homePage.startAddingTweetProcess();
        tweetComposePage.writeTweet(tweetText);
        tweetComposePage.addEmojiByName(emojiName);
        tweetComposePage.sendTweetButton.click();
        homePage.waitForElement(homePage.tweetSuccessfullySentNotification);
        homePage.openTweetByPosition(0);
        //@todo separte method for this assert
        Assertions.assertEquals(tweetPage.tweetContent.getText(), tweetText);
        step("Verify that emojij is displayed correctly in tweet", (step) -> {
            Assertions.assertEquals(tweetPage.emojiInTweet.getAttribute("src"), emojiUrl);
        });
        tweetPage.deleteTweet();
    }

    //@TODO add handling more than one chained tweet
    @ParameterizedTest(name = "User is able to add multiple chained tweets")
    @CsvSource({
            "asdd, zxcd",
            "tweet numer jeden, tweet numer dwa",
            "troche dłuższy tweet numer jeden, troche dłuższyyyyyyyy tweet numer dwa"
    })
    void userCanAddMultiTweet(String firstTweetText, String secondTweetText) {
        homePage = new HomePage(driver);
        tweetComposePage = new TweetComposePage(driver);
        tweetPage = new TweetPage(driver);
        homePage.startAddingTweetProcess();
        tweetComposePage.writeTweet(firstTweetText);
        step("Add another chained tweet", (step) -> {
            tweetComposePage.multiTweetButton.click();
            tweetComposePage.multiTweet2ndTextArea.sendKeys(secondTweetText);
            tweetComposePage.sendTweetButton.click();
        });
        homePage.waitForElement(homePage.tweetSuccessfullySentNotification);
        homePage.openTweetByPosition(0);
        step("Verify that all tweets were added correctly", (step) -> {
            Assertions.assertEquals(tweetPage.tweetContent.getText(), firstTweetText);
            Assertions.assertEquals(tweetPage.multiTweet2ndTweetContent.getText(), secondTweetText);
        });
    }

    @ParameterizedTest(name = "")
    @CsvSource({
            "https://www.netguru.com/, Netguru – Custom software development company",
            "https://www.wikipedia.org/, Wikipedia"
    })
    void userCanAddLinkToExternalPage(String link, String pageTitle) {
        homePage = new HomePage(driver);
        tweetComposePage = new TweetComposePage(driver);
        tweetPage = new TweetPage(driver);
        homePage.startAddingTweetProcess();
        tweetComposePage.writeAndSendTweet(link);
        homePage.waitForElement(homePage.tweetSuccessfullySentNotification);
        homePage.openTweetByPosition(0);
        homePage.switchToNewTab();
        step("Verify that link leads to a correct external page", (step) -> {
            Assertions.assertEquals(pageTitle, driver.getTitle());
        });

    }

    //@TODO add handling more than one hashtag
    @ParameterizedTest(name = "User is able to add tweet with hashtag")
    @CsvSource({
            "tweet with hashtag, #has",
            "some more test data, #kolejnytag"
    })
    void userCanAddTweetWithHashtag(String tweetText, String hashtag) {
        homePage = new HomePage(driver);
        tweetComposePage = new TweetComposePage(driver);
        tweetPage = new TweetPage(driver);
        homePage.startAddingTweetProcess();
        tweetComposePage.writeTweet((String.format("%s %s", tweetText, hashtag)));
        tweetComposePage.tweetTextArea.sendKeys(Keys.SPACE);
        tweetComposePage.sendTweetButton.click();
        homePage.waitForElement(homePage.tweetSuccessfullySentNotification);

        homePage.openTweetByPosition(0);
        step("Verify that tweet contains correct content", (step) -> {
            Assertions.assertEquals(tweetPage.tweetContent.getText().trim(), tweetText.trim());
            Assertions.assertEquals(tweetPage.hashTagInTweet.getText(), hashtag);
        });
        step("Verify that user can search for similar tweets when clicking at hashtag", (step) -> {
            tweetPage.hashTagInTweet.click();
            Assertions.assertEquals(homePage.searchInput.getAttribute("value"), hashtag);
        });
    }

    @ParameterizedTest(name = "User can mention other user in his tweet")
    @CsvSource({"asd, @Emil08345731"})
    void userCanSendTweetWithUserMention(String tweetText, String userMention) {
        homePage = new HomePage(driver);
        tweetComposePage = new TweetComposePage(driver);
        tweetPage = new TweetPage(driver);

        homePage.startAddingTweetProcess();
        tweetComposePage.writeTweet(tweetText);
        tweetComposePage.addUserMention(userMention);
        tweetComposePage.sendTweetButton.click();
        homePage.waitForElement(homePage.tweetSuccessfullySentNotification);
        homePage.openTweetByPosition(0);
        step("Verify that tweet contains correct content", (step) -> {
            tweetPage.hashTagInTweet.click();
            Assertions.assertEquals(tweetPage.tweetContent.getText(), tweetText + " ");
            Assertions.assertEquals(tweetPage.hashTagInTweet.getText(), userMention);
        });
        step("Verify that @mention contains correct url to access mentioned user's profile", (step) -> {
            tweetPage.hashTagInTweet.click();
            Assertions.assertEquals(tweetPage.hashTagInTweet.getAttribute("href"), userMention.replace("@", "/"));

        });
        tweetPage.deleteTweet();
    }
}
