app.controller('TestCtrl', function($scope, $state , CRM){
  $scope.customer1 = "";

  $scope.customerOne = function(){
     CRM.getCustomer(1).then(function (results) {
      if(results.length === 0) {
        alert('No match found');
      } else {
        $scope.customer1 = results;
        $scope.$apply();
      }
    }).fail(function (error) {
      alert(error);
    });
  };

  $scope.$on('$ionicView.enter', function() {
    CRM.test();
  });

});
