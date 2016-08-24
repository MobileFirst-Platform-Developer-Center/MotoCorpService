app.controller('CustomerCtrl', function($scope,$state){
  $scope.customer = getCustomer($state.params.customerId);
});



function getCustomer(customerId) {
  for (var i = 0; i < customers.length; i++) {
    if (customers[i].id == customerId) {
      return customers[i];
    }
  }
  return undefined;
}

var customers = [
  {
    name: 'Jack Reacher',
    plate: '1234',
    make: 'Honda',
    model: 'Accord',
    vin: '1234567890',
    visits: [{date: '01/01/16', type: "oil change"}, {date: '06/11/16', type: "tire rotation"}, {
      date: '08/01/16',
      type: "tune up"
    }],
    id: 1
  },
  {
    name: 'Joe Blow',
    plate: '9876',
    make: 'Honda',
    model: 'Accord',
    vin: '1234567890',
    visits: [{date: '01/01/16', type: "oil change"}, {date: '06/11/16', type: "tire rotation"}],
    id: 2
  }
];
