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
      
      $state.go('app.customer', plate);
    });

    // CRM.newCustomer({name: this.name, plate: this.plate, make: this.make, model: this.model, vin: this.vin}).then(function (results) {
    //   if(results.length === 0) {
    //     alert('No match found');
    //   } else {
    //     $scope.newCustomer = results;
    //     $scope.$apply();
    //     $state.go('app.search');
    //   }
    // }).fail(function (error) {
    //   alert(error);
    // });
  };
});