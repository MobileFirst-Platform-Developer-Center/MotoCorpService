#  MobileFirst Foundation - Implement Login (Security)

In this section you will learn how to quickly implement a secure login for your application using the MobileFirst Foundation security framework.

## What you will learn in this guide

- How to use to use the Mobile Foundation Security Check
- How to implement OAuth in your mobile application
- How to create a client secret

## Requirements of this guide

- [Mobile Foundation Setup](/Lab/Contents/MFP-Setup-Mobile-Foundation-on-Bluemix/Readme.md)

## Guide
  
It is important to authenticate a user before they can access sensitive customer information. 
We will do this by using the MobileFirst security framework to easily implement the OAuth2 protocol to do a handshake with the server for a live token every time a resource request is made via the adapter.

When the app is first launched, it will take you to a login screen where you can sign into the app.
In our example, the username and password used in the security adapter logic is such that the user name and password are the same values.

In this lab you will explore the **Auth** service that will communicate between the **login controller** and the **security adapter**.

> To learn more about the security adapter please see our getting started labs [here](https://mobilefirstplatform.ibmcloud.com/tutorials/en/foundation/8.0/authentication-and-security/creating-a-security-check/).

Let's take a look at how this works.
 
![Login](img/login.png)

### Server Side Configuration
1 - Register and push your app to your MobileFirst console
```bash
cd ./MotoCorp
cordova platform add ios
mfpdev app register bluemix-server
cordova prepare
```

1 - Go to your UserLogin Adapter folder and build the javascript adapter
```bash
cd ./Adapters/UserLogin
mfpdev adapter build
```

2 - Upload the adapter to your MobileFirst console by selecting "Deploy Adapter" and uploading the `UserLogin.adapter` file.
![Deploy Adapter](img/upload-login-adapter.png)
3 - Map the `user-restricted` scope to the UserLogin security check inside the application security settings in the MobileFirst Foundation Operations Console.

The security framework ensures that only a client that passes all of the security checks of the protecting scope is granted access to the resource. A security check is a server-side entity that implements a specific authorization logic, such as obtaining and validating client credentials.

![Map user-restricted scope](img/login-security-check.png)

By adding `user-restricted` scope to a resource (in this lab, `CustomerAdapter`), MobileFirst allows you to easily map security scopes into each resource request so that the security frameworks triggers the mapped security checks.

```bash
@OAuthSecurity(scope = "user-restricted")
```

4 - To configure how long before a token expires (when a user is logged out), configure the `successStateExpirationSec`*[]: 

![Token](img/token.png)

### Client Side Configuration 

#### Auth Service

In the services/auth.js file, the following *login()* function uses the username and password that you type in and sends it to the UserLogin security adapter via the `WLAuthorizationManager.login` function to validate the credentials (if username and password are the same).

```js
login: function ($scope, username, password) {
      _$scope = $scope;

      // register the challenge handler for `UserLogin`
      bindChallengeHandler();

      if (!username || !password) {
        $scope.$emit('login-error', {
          message: 'Username and Password are required.'
        });
      } else if (authInProgress) {
        // if the authorization is in progress then submit the user credentials to the challenge handler
        challengeHandler.submitChallengeAnswer({'username': username, 'password': password});
      } else {
        // the first time the user clicks login submit the user credentials along with the security check name `UserLogin`
        WLAuthorizationManager.login(securityCheckName, {'username': username, 'password': password});
      }
    }
```

> > This lab uses the [UserLogin Adapter](https://mobilefirstplatform.ibmcloud.com/tutorials/en/foundation/8.0/authentication-and-security/user-authentication/security-check/) from the Getting Started tutorials. Please familiarize yourself with this in order to better understand the logic implemented on the client side.

#### Login Controller

In the **login.js** file, the following code snippet shows passes the username and password to the Auth service.

```js
Auth.login($scope, this.username.toLowerCase(), this.password.toLowerCase());
authInProgress = true;
```

The `Auth.login()` function sends the $scope object from the controller along with the **username and password** you enter to the Auth service.

```js
  $scope.$on('login-success', function () {
    authInProgress = false;
    $state.go('app.search');
  });

  $scope.$on('login-error', function (event, error) {
    alert(error.message);

    authInProgress = false;
  });
```

Once authenticated, you will be redirected to the customer search page.
If the login credentials are invalid, you will receive a popup describing the error.
Additionally, the MobileFirst security framework handles the time-to-live of the OAuth token so that when the token's life expires, a user will be redirected to the login page.

### Testing

To to test the login, build, deploy, and emulate the app from the cli.

```
cordova build ios
cordova emulate ios
``` 

To login, use the same username/password (pre-populated). Once successfully logged in, you will be directed to the next page.

![Login](img/login.gif)

### Creating a Client Secret

For future testing in other protected adapters, create a client secret. 
This will allow you to test adapters in swagger within any scope.

> Learn more about client secrets [here](https://mobilefirstplatform.ibmcloud.com/tutorials/en/foundation/8.0/authentication-and-security/confidential-clients/)

1 - Go to your Runtime Settings > Confidential Clients in your mfp console

2 - Create a confidential client with id "test", secret "test", and scope "*"

![confidential-client](img/confidential-client.png)


## Next guide

[MFP-MessageHub-Adapter](/Lab/Contents/MFP-MessageHub-Adapter/Readme.md)  
