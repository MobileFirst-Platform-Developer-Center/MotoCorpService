app.factory('Auth', function ($rootScope) {

	var securityCheckName = 'UserLogin',
		authInProgress = false,
		_$scope = null,
		challengeHandler = null,
		activeUser = null,
        bound = false;
    
    
    function bind() {
        if(!bound) {
            challengeHandler = WL.Client.createSecurityCheckChallengeHandler(securityCheckName);
            challengeHandler.securityCheckName = securityCheckName;

            challengeHandler.handleChallenge = function (challenge) {
                authInProgress = true;

                if (challenge.errorMsg !== null && _$scope) {
                    _$scope.$emit('login-error', {
                        message: challenge.errorMsg
                    });
                } else {
                    // redirect to login page
                    $rootScope.$emit('login-challenge');
                }


            };

            challengeHandler.handleSuccess = function (data) {
                authInProgress = false;

                activeUser = data.user;

                if(_$scope) {
                    _$scope.$emit('login-success', {
                        data: data
                    });
                }
            };

            challengeHandler.handleFailure = function (error) {
                authInProgress = false;

                var message = error.failure !== null ? error.failure : 'Failed to login.';

                if(_$scope) {
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
		getActiveUser: function () {
			return activeUser ? activeUser : false;
		},
		login: function ($scope, username, password) {
            bind();

			_$scope = $scope;

			if (!username || !password) {
				$scope.$emit('login-error', {
					message: 'Username and Password are required.'
				});
			} else if (authInProgress) {
				challengeHandler.submitChallengeAnswer({'username': username, 'password': password});
			} else {
				WLAuthorizationManager.login(securityCheckName, {'username': username, 'password': password});
			}
		}
	};
});