app.controller('SearchCtrl', function ($scope, $state, Auth) {
  // this is where we change where we get the customers from
  $scope.customers = customers;
  $scope.data = {};
  $scope.found = {};

  $scope.searchPlate = function (plate) {
    for (var i = 0; i < customers.length; i++) {
      if (customers[i].plate == plate) {
        $scope.found = customers[i];
        return $scope.found;
      }
    }
  };

});

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
