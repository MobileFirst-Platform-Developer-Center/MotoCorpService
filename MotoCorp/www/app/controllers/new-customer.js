app.controller('NewCustomerCtrl', function ($scope, $state, CRM) {

  $scope.customer = {
    'Name': null,
    'LicensePlate': null,
    'Make': null,
    'Model': null,
    'VIN': null
  };

  /* Create a new customer with inputs */
  $scope.newCustomer = function(){
    var myCustomer = JSON.stringify($scope.customer);
    
    CRM.newCustomer(myCustomer).then(function () {
      alert("New customer submitted!");
      var plate = {
        id: $scope.customer.LicensePlate
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