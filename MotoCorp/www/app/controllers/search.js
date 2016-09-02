app.controller('SearchCtrl', function ($scope, $state, Auth, CRM) {
  // Add scope variables here

  $scope.addCustomer = function(){
    $state.go('app.new-customer');
  };

  $scope.searchPlate = function () {
    
  };
});
