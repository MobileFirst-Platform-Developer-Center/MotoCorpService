app.controller('SearchCtrl', function ($scope, $state, Auth, CRM) {
  $scope.results = [];
  $scope.search = {
    "text":"",
    "type":"plate"
  };

  $scope.addCustomer = function(){
    $state.go('app.new-customer');
  };

  $scope.searchPlate = function () {
    $scope.search.text = this.query;

// This sends the search payload to the search function at the CRM service.
    CRM.search($scope.search).then(function (results) {
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
