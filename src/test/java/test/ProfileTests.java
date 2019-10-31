package test;

import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.Keys;
import pageobject.*;

import java.util.List;

import static io.qameta.allure.Allure.step;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Feature("User can mange his profile")
class ProfileTests extends BaseTest {

    private HomePage homePage;
    private ProfilePage profilePage;
    private EditProfileComponent editProfileComponent;
    private FollowingPage followingPage;
    private LikesComponent likesComponent;
    private TweetPage tweetPage;
    private TweetComposePage tweetComposePage;

    @Step("Open edit profile form")
    private void openEditProfileComponentFromHomePage() {
        homePage.profileLink.click();
        profilePage.editProfileButton.click();
        profilePage.waitForElement(editProfileComponent.editProfileWindow);
    }

    @ParameterizedTest(name = "User can change his user data")
    @CsvSource({
            "Emil_new_name1, 1template bio, Warszawa, www.google.pl"
    })
    void userCanChangeProfileData(String name, String bio, String location, String website) {
        homePage = new HomePage(driver);
        profilePage = new ProfilePage(driver);
        editProfileComponent = new EditProfileComponent(driver);

        openEditProfileComponentFromHomePage();
        editProfileComponent.editProfileData(name, bio, location, website);
        editProfileComponent.saveProfileButton.click();
        profilePage.verifyUserDataIsCorrect(name, bio, location, website);
    }

    @ParameterizedTest(name = "User can't add too long values in edit profile form")
    @CsvSource({
            "Emil_new_name1asddsadadsdsadsadasdasdasdasdsadsads123123, 1template bio12332132131232123123123123121template bio12332132131232123123123123121template bio12332132131232123123123123121template bio123321321312321231231231 bio, WarszawaWarszawaWarszawaWarsza, www.google.pl"
    })
    void userCantAddTooLongInfoInProfileEdit(String name, String bio, String location, String website) {
        homePage = new HomePage(driver);
        profilePage = new ProfilePage(driver);
        editProfileComponent = new EditProfileComponent(driver);

        openEditProfileComponentFromHomePage();

        editProfileComponent.editProfileData(name, bio, location, website);

        editProfileComponent.verifyDataInEditForm(
                name.substring(0, EditProfileComponent.NAME_MAX_LENGTH),
                bio.substring(0,EditProfileComponent.BIO_MAX_LENGTH),
                location.substring(0,EditProfileComponent.LOCATION_MAX_LENGTH),
                website);

        editProfileComponent.saveProfileButton.click();
        profilePage.verifyUserDataIsCorrect(
                name.substring(0, EditProfileComponent.NAME_MAX_LENGTH),
                bio.substring(0,EditProfileComponent.BIO_MAX_LENGTH),
                location.substring(0,EditProfileComponent.LOCATION_MAX_LENGTH),
                website);
    }

    @ParameterizedTest
    @CsvSource({
            "multi\\nline\\nbio",
            "two new lines\\n\\nthree new lines\\n\\n\\n:)"
    })

    void userCanAddMultiLineBio(String bio) {
        homePage = new HomePage(driver);
        profilePage = new ProfilePage(driver);
        editProfileComponent = new EditProfileComponent(driver);

        final String name = "default name";
        final String location = "default location";
        final String website = "www.google.pl";

        openEditProfileComponentFromHomePage();

        String formattedBio = bio.replace("\\n", Keys.chord(Keys.SHIFT, Keys.ENTER));
        editProfileComponent.editProfileData(name, formattedBio, location, website);

        //@fixme new line asserts fails
        editProfileComponent.verifyDataInEditForm(
                name,
                bio,
                location,
                website);

        editProfileComponent.saveProfileButton.click();
        profilePage.verifyUserDataIsCorrect(
                name,
                bio,
                location,
                website);
    }

    @ParameterizedTest(name = "User can see in his profile other users that he's following")
    @CsvSource({"@CezaryGutowski", "@ScuderiaFerrari", "@netguru"})
    void userCanSeeHisFollows(String userId) {
        homePage = new HomePage(driver);
        profilePage = new ProfilePage(driver);
        editProfileComponent = new EditProfileComponent(driver);

        homePage.searchForUser(userId);
        homePage.followCurrentUser();
        homePage.profileLink.click();
        profilePage.followingButton.click();
        profilePage.verifyUserIsVisibleInFollowing(userId);
    }

    @ParameterizedTest
    @CsvSource({"@netguru"})
    void userCanDeleteFollowsFromProfilePage(String userId) {
        homePage = new HomePage(driver);
        profilePage = new ProfilePage(driver);
        editProfileComponent = new EditProfileComponent(driver);
        followingPage = new FollowingPage(driver);

        homePage.searchForUser(userId);
        homePage.followCurrentUser();
        homePage.profileLink.click();
        profilePage.followingButton.click();

        step("Verify that user is displayed as followed", (step) -> {
            Assertions.assertTrue(followingPage.followedAccountCell.isDisplayed());
            Assertions.assertEquals(followingPage.followButton.getText(), "Following");
        });
        step("Unfollow user", (step) -> {
            followingPage.followButton.click();
            followingPage.confirmUnfollow.click();
        });
        step("Verify that button changes its text from Following to Follow", (step) -> {
            Assertions.assertEquals(followingPage.followButton.getText(), "Follow");
        });

        followingPage.verifyThatUserIsNoLongerFollowed(userId);
    }

    @ParameterizedTest(name = "User can see tweets he likes on his profile page")
    @CsvSource({"@netguru"})
    void userCanSeeHisLikesOnProfilePage(String userId) {
        homePage = new HomePage(driver);
        profilePage = new ProfilePage(driver);
        editProfileComponent = new EditProfileComponent(driver);
        followingPage = new FollowingPage(driver);
        likesComponent = new LikesComponent(driver);

        homePage.searchForUser(userId);
        profilePage.scrollToElement(profilePage.lastTweet);
        String tweetHref = profilePage.lastTweetLink.getAttribute("href");
        step("Like latest tweet visible", (step) -> {
            profilePage.waitForElementToBeClickable(profilePage.lastTweetLikeButton);
            profilePage.lastTweetLikeButton.click();
        });
        step("Verify that previously liked tweet is visible on profile page", (step) -> {
            homePage.profileLink.click();
            profilePage.likesNavigationButton.click();
            likesComponent.verifyThatTweetIsVisibleByHref(tweetHref);
        });
    }

    //@TODO move to tweeting tests?
    @ParameterizedTest(name = "Added tweets are visible on profile page")
    @CsvSource({"asd 123", "drugi tweet"})
    void userCanSeeHisTweetsOnProfilePage(String tweetContent) throws InterruptedException {
        homePage = new HomePage(driver);
        tweetComposePage = new TweetComposePage(driver);
        tweetPage = new TweetPage(driver);
        profilePage = new ProfilePage(driver);

        homePage.startAddingTweetProcess();
        tweetComposePage.writeTweet(tweetContent);
        tweetComposePage.sendTweetButton.click();
        homePage.waitForElement(homePage.tweetSuccessfullySentNotification);

        homePage.profileLink.click();
        Thread.sleep(1000);
        step("Verify that previously added tweet is visible on profile page", (step) -> {
            profilePage.scrollToElement(profilePage.lastTweet);
            Assertions.assertEquals(profilePage.lastTweetContent.getText(), tweetContent);
        });

    }

    @ParameterizedTest(name = "User can change his profile photos")
    @CsvSource({
            "Emil_new_name1, 1template bio, Warszawa, www.google.pl"
    })
    void userCanChangeProfilePhotos(String name, String bio, String location, String website) {
        homePage = new HomePage(driver);
        profilePage = new ProfilePage(driver);
        editProfileComponent = new EditProfileComponent(driver);

        homePage.profileLink.click();
        profilePage.editProfileButton.click();
        profilePage.waitForElement(editProfileComponent.editProfileWindow);

        //@TODO change to relative paths
        editProfileComponent.changeAvatarPhoto("C:\\Users\\Emil\\IdeaProjects\\twittertests\\src\\test\\resources\\sample_avatar_photo.png");
        editProfileComponent.changeBannerPhoto("C:\\Users\\Emil\\IdeaProjects\\twittertests\\src\\test\\resources\\sample_banner_photo.png");
    }

    @Disabled
    @Test
    void userCantChangeProfilePhotosWithWrongFormat() {
        //infinite loader solution on twitter side, no point to write it
    }

    @Disabled
    @Test
    void userCantChangeProfilePhotosWithTooBigFiles() {
        //infinite loader solution on twitter side, no point to write it
    }

    @ParameterizedTest(name = "User can't set his website using wrong www format")
    @CsvSource({"www", "google", "www.www", "gooooooooooogle.pl.we......dk"})
    void userCantChangeHisWebsiteToWrongFormat(String website) {
        homePage = new HomePage(driver);
        profilePage = new ProfilePage(driver);
        editProfileComponent = new EditProfileComponent(driver);
        final String name = "default name";
        final String location = "default location";
        final String bio = "just random bio";

        homePage.profileLink.click();
        profilePage.editProfileButton.click();
        profilePage.waitForElement(editProfileComponent.editProfileWindow);
        editProfileComponent.editProfileData(name, bio, location, website);
        editProfileComponent.saveProfileButton.click();
        Assertions.assertTrue(editProfileComponent.editProfileLabel.isDisplayed());
        Assertions.assertTrue(editProfileComponent.incorrectUrlMessage.isDisplayed());
    }
/*
    @AfterAll
    void cleanTestData() {
        cleanTweets();
        cleanLikes();
        cleanFollows();
    }

 */
}
