app.controller('SignInCtrl', function($scope, $state, Auth){
    var authInProgress = false;

	$scope.doLogin = function () {
		if (!authInProgress) {
			Auth.login($scope, this.username.toLowerCase(), this.password.toLowerCase());
			authInProgress = true;
		}
	};
    
    $scope.$on('login-success', function () {
		$state.go('app.search');
	});

	$scope.$on('login-error', function (event, error) {
		alert(error.message);
		authInProgress = false;
	});
});
