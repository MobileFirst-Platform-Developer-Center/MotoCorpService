app.controller('NewCustomerCtrl', function ($scope, $state, CRM) {

  $scope.customer = {
    'name': null,
    'plate': null,
    'make': null,
    'model': null,
    'vin': null
  };

  /* Create a new customer with inputs */
  $scope.newCustomer = function(){
    var myCustomer = JSON.stringify($scope.customer);
    
    CRM.newCustomer(myCustomer).then(function () {
      var plate = {
        id: $scope.customer.plate
      };
      // takes you to the view-customer page with the customer plate as a url param
      $state.go('app.customer', plate);
    //   if(results.length === 0) {
    //     alert('Cannot add customer.');
    //   } else {
    //     $scope.customer = results;
    //     $scope.$apply();
    //     $state.go('app.search');
        
        /* Custom analytics tracking how many new customers created in app */
        WL.Analytics.log({ NewCustomer: '1' }, results);         
        WL.Analytics.send();
        
    //   }
    // }).fail(function (error) {
    //   alert(error);
    // });
    });
  };
});