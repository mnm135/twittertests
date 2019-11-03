package test;

import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.Keys;
import pageobject.*;


import static io.qameta.allure.Allure.step;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Feature("User can mange his profile")
class ProfileTests extends BaseTest {

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

    @ParameterizedTest(name = "User can add multi-line bio")
    @CsvSource({
            "multi\\nline\\nbio",
            "more\\nnew\\nlines\\n:)"
    })
    void userCanAddMultiLineBio(String bio) {
        homePage = new HomePage(driver);
        profilePage = new ProfilePage(driver);
        editProfileComponent = new EditProfileComponent(driver);

        openEditProfileComponentFromHomePage();
        String formattedBio = bio.replace("\\n", Keys.chord(Keys.SHIFT, Keys.ENTER));
        editProfileComponent.editProfileData(
                EditProfileComponent.DEFAULT_NAME,
                formattedBio,
                EditProfileComponent.DEFAULT_LOCATION,
                EditProfileComponent.DEFAULT_WEBSITE);
        editProfileComponent.verifyDataInEditForm(
                EditProfileComponent.DEFAULT_NAME,
                bio,
                EditProfileComponent.DEFAULT_LOCATION,
                EditProfileComponent.DEFAULT_WEBSITE);

        editProfileComponent.saveProfileButton.click();
        profilePage.waitForEditPageToDisappear();
        profilePage.verifyUserDataIsCorrect(
                EditProfileComponent.DEFAULT_NAME,
                bio,
                EditProfileComponent.DEFAULT_LOCATION,
                EditProfileComponent.DEFAULT_WEBSITE);
    }

    @ParameterizedTest(name = "User can see in his profile other users that he's following")
    @CsvSource({"@CezaryGutowski", "@ScuderiaFerrari", "@netguru"})
    void userCanSeeHisFollows(String userId) {
        homePage = new HomePage(driver);
        profilePage = new ProfilePage(driver);
        editProfileComponent = new EditProfileComponent(driver);

        homePage.searchAndGoToUserPage(userId);
        profilePage.waitForUserPageToBeOpen(userId);
        homePage.followCurrentUser();
        homePage.profileLink.click();
        profilePage.followingButton.click();
        profilePage.verifyUserIsVisibleInFollowing(userId);

        cleanFollows();
    }

    @ParameterizedTest(name = "User can unfollow other accounts from his profile page")
    @CsvSource({"@netguru"})
    void userCanDeleteFollowsFromProfilePage(String userId) {
        homePage = new HomePage(driver);
        profilePage = new ProfilePage(driver);
        editProfileComponent = new EditProfileComponent(driver);
        followingPage = new FollowingPage(driver);

        homePage.searchAndGoToUserPage(userId);
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

        homePage.searchAndGoToUserPage(userId);
        profilePage.waitForUserPageToBeOpen(userId);
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
        cleanLikes();
    }

    @ParameterizedTest(name = "Added tweets are visible on profile page")
    @CsvSource({"asd 123", "drugi tweet"})
    void userCanSeeHisTweetsOnProfilePage(String tweetContent) throws InterruptedException {
        homePage = new HomePage(driver);
        tweetComposeComponent = new TweetComposeComponent(driver);
        tweetPage = new TweetPage(driver);
        profilePage = new ProfilePage(driver);

        homePage.startAddingTweetProcess();
        tweetComposeComponent.writeAndSendTweet(tweetContent);
        homePage.waitForElement(homePage.tweetSuccessfullySentNotification);

        homePage.profileLink.click();
        Thread.sleep(1000);
        step("Verify that previously added tweet is visible on profile page", (step) -> {
            profilePage.scrollToElement(profilePage.lastTweet);
            Assertions.assertEquals(profilePage.lastTweetContent.getText(), tweetContent);
        });
        cleanTweets();
    }

    @ParameterizedTest(name = "User can't set his website using wrong www format")
    @CsvSource({"www", "google", "www.www", "gooooooooooogle.pl.we......dk"})
    void userCantChangeHisWebsiteToWrongFormat(String website) {
        homePage = new HomePage(driver);
        profilePage = new ProfilePage(driver);
        editProfileComponent = new EditProfileComponent(driver);

        openEditProfileComponentFromHomePage();
        editProfileComponent.editProfileData(
                EditProfileComponent.DEFAULT_NAME,
                EditProfileComponent.DEFAULT_BIO,
                EditProfileComponent.DEFAULT_LOCATION,
                website);
        editProfileComponent.saveProfileButton.click();

        step("Verify that Edit form is not closed and incorrect url message is displayed", (step) -> {
            Assertions.assertTrue(editProfileComponent.editProfileLabel.isDisplayed());
            Assertions.assertTrue(editProfileComponent.incorrectUrlMessage.isDisplayed());
        });
    }
}
