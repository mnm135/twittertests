package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pageobject.*;

class ProfileTests extends BaseTest {

    private HomePage homePage;
    private ProfilePage profilePage;
    private EditProfileComponent editProfileComponent;

    @ParameterizedTest
    @CsvSource({
            "Emil_new_name1, 1template bio, Warszawa, www.google.pl"
    })
    void userCanChangeProfileData(String name, String bio, String location, String website) {
        homePage = new HomePage(driver);
        profilePage = new ProfilePage(driver);
        editProfileComponent = new EditProfileComponent(driver);

        homePage.profileLink.click();
        profilePage.editProfileButton.click();
        profilePage.waitForElement(editProfileComponent.editProfileWindow);

        editProfileComponent.editProfileData(name, bio, location, website);
        profilePage.verifyUserDataIsCorrect(name, bio, location, website);
    }

    @Test
    void userCantAddTooLongInfoInProfileEdit() {

    }

    @Test
    void userCanSeeHisFollows() {

    }

    @Test
    void userCanDeleteFollowsFromProfilePage() {

    }

    @Test
    void userCanSeeHisLikesOnProfilePage() {

    }

    @Test
    void userCanSeeHisTweetsOnProfilePage() {

    }



    @ParameterizedTest
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

        editProfileComponent.changeAvatarPhoto("C:\\Users\\Emil\\IdeaProjects\\twittertests\\src\\test\\resources\\sample_avatar_photo.png");
        editProfileComponent.changeBannerPhoto("C:\\Users\\Emil\\IdeaProjects\\twittertests\\src\\test\\resources\\sample_banner_photo.png");
    }

    @Test
    void userCantChangeProfilePhotosWithWrongFormat() {

    }

    @Test
    void userCantChangeProfilePhotosWithTooBigFiles() {

    }
}
