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
