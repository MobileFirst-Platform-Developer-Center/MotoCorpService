app.controller('LoginCtrl', function ($scope, $state, Auth) {
  var authInProgress = false;

  // TODO: only for testing. Remove before release
  $scope.username = '1234';
  $scope.password = '1234';

  $scope.doLogin = function () {
  if (!authInProgress) {
        // Add login code here
      }
  };

  // If login is successful, take me to the search screen
  $scope.$on('login-success', function () {
    authInProgress = false;
    $state.go('app.search');
  });
  // if login is unsuccessful, show me the error message in an alert message
  $scope.$on('login-error', function (event, error) {
    alert(error.message);

    authInProgress = false;
  });
});
