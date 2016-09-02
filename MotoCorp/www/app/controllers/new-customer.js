app.controller('NewCustomerCtrl', function ($scope, $state, CRM) {
  /* Create a new customer with inputs */
  $scope.newCustomer = function(){
    CRM.newCustomer({name: this.name, plate: this.plate, make: this.make, model: this.model, vin: this.vin}).then(function (results) {
      if(results.length === 0) {
        alert('No match found');
      } else {
        $scope.newCustomer = results;
        $scope.$apply();
        $state.go('app.search');
        
        /* Custom analytics tracking how many new customers created in app */
        WL.Analytics.log({ NewCustomer: '1' }, results);         
        WL.Analytics.send();
        
      }
    }).fail(function (error) {
      alert(error);
    });
  };
});