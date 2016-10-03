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
    console.log("Here is my customer: " + myCustomer);

    var req = new WLResourceRequest('/adapters/MessageHubAdapter/resource/sendMessage', WLResourceRequest.POST);
    req.setHeader('Content-type', 'application/json');

    return req.send($scope.customer).then(function (response) {
    return response.responseJSON;
     }, function (error) {
        return WLJQ.Deferred().reject(error.responseText).promise();
      });


    // CRM.newCustomer({name: this.name, plate: this.plate, make: this.make, model: this.model, vin: this.vin}).then(function (results) {
    //   if(results.length === 0) {
    //     alert('Cannot add customer.');
    //   } else {
    //     $scope.newCustomer = results;
    //     $scope.$apply();
    //     $state.go('app.search');
        
    //     /* Custom analytics tracking how many new customers created in app */
    //     WL.Analytics.log({ NewCustomer: '1' }, results);         
    //     WL.Analytics.send();
        
    //   }
    // }).fail(function (error) {
    //   alert(error);
    // });
  };
});