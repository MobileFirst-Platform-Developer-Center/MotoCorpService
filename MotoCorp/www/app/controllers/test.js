app.controller('TestCtrl', function($scope, $state , CRM){
  $scope.date = new Date();

  $scope.$on('$ionicView.enter', function() {
    CRM.test();
  });

});
