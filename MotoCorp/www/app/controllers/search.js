app.controller('SearchCtrl', function ($scope, $state, Auth, CRM) {
  $scope.results = [];

  $scope.addCustomer = function(){
    $state.go('app.new-customer');
  };

  $scope.searchPlate = function () {
    CRM.search({plate: this.query}).then(function (results) {
      if(results.length === 0) {
        alert('No match found');
      } else {
        $scope.results = results;
        $scope.$apply();
      }
    }).fail(function (error) {
      alert(error);
    });
  };
});
