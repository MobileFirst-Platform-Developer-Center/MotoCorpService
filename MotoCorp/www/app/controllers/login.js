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

app.controller('LoginCtrl', function ($scope, $state, Auth) {
  var authInProgress = false;

  // TODO: only for testing. Remove before release
  $scope.username = '1234';
  $scope.password = '1234';

  $scope.doLogin = function () {
    if (!authInProgress) {
      Auth.login($scope, this.username.toLowerCase(), this.password.toLowerCase());
      authInProgress = true; 
    }
  };
  $scope.doDemoLogin = function () {
    if (!authInProgress) {
      Auth.login($scope, "demo", "demo");
      authInProgress = true;
    }
  };
  $scope.$on('login-success', function () {
    authInProgress = false;
    $state.go('app.search');
  });

  $scope.$on('login-error', function (event, error) {
    alert(error.message);

    authInProgress = false;
  });
});
