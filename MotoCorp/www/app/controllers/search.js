app.controller('SearchCtrl', function ($scope, $state, Auth, CRM) {
  $scope.results = [];
  $scope.searchType = {
    name:""
  };

  $scope.addCustomer = function(){
    $state.go('app.new-customer');
  };

  $scope.searchPlate = function () {
    var search = {};
    search[$scope.searchType.name] = this.query;
    
    CRM.search(search).then(function (results) {
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
