// Yoel's commit
//app.controller('CustomerCtrl', function ($scope, $state, CRM) {
//  var id = $state.params.id;
//
//  $scope.addVisit = function () {
//    $state.go('app.customer.visit', {id: id});
//  };
//
//  CRM.getCustomer(id).then(function (customer) {
//    $scope.customer = customer;
//    $scope.$apply();
//  }).fail(function (error) {
//    alert(error);
//  });
//});

app.controller('CustomerCtrl', function($scope,$state,CRM){
  $scope.customer = getCustomer($state.params.customerId);

  /* Create a new customer with inputs */
  $scope.newCustomer = function(){
     CRM.newCustomer({name: this.name, plate: this.plate, make: this.make, model: this.model, vin: this.vin}).then(function (results) {
      if(results.length === 0) {
        alert('No match found');
      } else {
        $scope.newCustomer = results;
        $scope.$apply();
        $state.go('app.search');
      }
    }).fail(function (error) {
      alert(error);
    });
  };
});

function getCustomer(customerId) {
  
  CRM.getAllCustomers().then(function(results){
    console.log(results);
    for (var i = 0; i < results.length; i++) {
    if (results[i].id == customerId) {
      return results[i];
    }
  }
  return undefined;
  });
  
}

// var customers = [
//   {
//     name: 'Jack Reacher',
//     plate: '1234',
//     make: 'Honda',
//     model: 'Accord',
//     vin: '1234567890',
//     visits: [{date: '01/01/16', type: "oil change"}, {date: '06/11/16', type: "tire rotation"}, {
//       date: '08/01/16',
//       type: "tune up"
//     }],
//     id: 1
//   },
//   {
//     name: 'Joe Blow',
//     plate: '9876',
//     make: 'Honda',
//     model: 'Accord',
//     vin: '1234567890',
//     visits: [{date: '01/01/16', type: "oil change"}, {date: '06/11/16', type: "tire rotation"}],
//     id: 2
//   }
// ];
