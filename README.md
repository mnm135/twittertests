# twittertests

###### Prerequisites
- Java 12 or newer
- Maven
- Allure

###### Setting up Allure
Mac OS
```
brew install allure
```
Debian based linux:
```
sudo apt-add-repository ppa:qameta/allure
sudo apt-get update
sudo apt-get install allure

sudo apt-add-repository ppa:yandex-qatools/allure-framework
sudo apt-get update
sudo apt-get install allure-commandline
```
Windows
```
Set-ExecutionPolicy RemoteSigned -scope CurrentUser
Invoke-Expression (New-Object System.Net.WebClient).DownloadString('https://get.scoop.sh')

scoop install allure
```

###### Running tests
- clone project
- open src/test/resources/user.properties file and edit username and password values
```
username=correctUsername
password=correctPassword
```
- navigate to project directory
- run following commands
```
mvn clean test
```
When te test execution is done run following command
```
mvn allure:serve
```
which opens allure report locally

###### Project info
Project contains tests of two twitter features with following cases:
- Sending tweets
    - Tweet send button is disabled when the tweet text area is empty
    - User can successfully send tweets (including border cases)
    - User is not able send too long tweet
    - User is able to send tweet with emoji
    - User is able to add multiple chained tweets
    - User can send tweet with external page link
    - User is able to add tweet with hashtag
    - User can @mention other user in his tweet
- Managing profile
    - User can change his user data
    - User can't add too long values in edit profile form
    - User can add multi-line bio
    - User can see in his profile other users that he's following
    - User can unfollow other accounts from his profile page
    - User can see tweets he likes on his profile page
    - Added tweets are visible on profile page
    - User can't set his website using wrong www format
