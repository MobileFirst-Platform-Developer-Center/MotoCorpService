/**
 * Copyright 2016 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

app.factory('Auth', function ($rootScope) {

  var securityCheckName = 'UserLogin',
    authInProgress = false,
    _$scope = null,
    challengeHandler = null,
    activeUser = null,
    bound = false;

  function bindChallengeHandler() {
    if (!bound) {
      // create challenge handler for the `UserLogin` security check
      challengeHandler = WL.Client.createSecurityCheckChallengeHandler(securityCheckName);
      challengeHandler.securityCheckName = securityCheckName;

      challengeHandler.handleChallenge = function (challenge) {
        authInProgress = true;

        // when the authorization fails with error message fire the login-error event with the error message
        // the event receiver should display the error message to the user to take action i.e., wrong password
        if (challenge.errorMsg !== null && _$scope) {
          _$scope.$emit('login-error', {
            message: challenge.errorMsg
          });
        } else {
          // fire the login-challenge event to indicate that the user needs to be authenticate
          // the event receiver should display the login form to the user
          $rootScope.$emit('login-challenge');
        }
      };

      challengeHandler.handleSuccess = function (data) {
        authInProgress = false;

        // save the active user
        activeUser = data.user;

        if (_$scope) {
          // fire the login-success event to indicate the status of the login
          // the event receiver should redirect the user to the protected page i.e., dashboard
          _$scope.$emit('login-success', {
            data: data
          });
        }
      };

      challengeHandler.handleFailure = function (error) {
        authInProgress = false;

        // check if an error message exists, add a generic error message if it doesn't
        var message = error.failure !== null ? error.failure : 'Failed to login.';

        if (_$scope) {
          // when the authorization fails with error message fire the login-error event with the error message
          // the event receiver should display the error message to the user to take action i.e., wrong password
          _$scope.$emit('login-error', {
            message: message
          });
        }
      };
    }

    bound = true;
  }

  return {
    logout: function () {
      return WLAuthorizationManager.logout(securityCheckName);
    },
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
  };
});