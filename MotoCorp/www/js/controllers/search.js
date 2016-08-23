var app = angular.module('starter.controllers', []);


app.controller('AppCtrl', function($scope, $ionicModal, $timeout) {

  // With the new view caching in Ionic, Controllers are only called
  // when they are recreated or on app start, instead of every page change.
  // To listen for when this page is active (for example, to refresh data),
  // listen for the $ionicView.enter event:
  //$scope.$on('$ionicView.enter', function(e) {
  //});

  // Form data for the login modal
  $scope.loginData = {};

  // Create the login modal that we will use later
  $ionicModal.fromTemplateUrl('pages/signin.html', {
    scope: $scope
  }).then(function(modal) {
    $scope.modal = modal;
  });

  // Triggered in the login modal to close it
  $scope.closeLogin = function() {
    $scope.modal.hide();
  };

  // Open the login modal
  $scope.login = function() {
    $scope.modal.show();
  };

  // Perform the login action when the user submits the login form
  $scope.doLogin = function() {
    console.log('Doing login', $scope.loginData);

    // Simulate a login delay. Remove this and replace with your login
    // code if using a login system
    $timeout(function() {
      $scope.closeLogin();
    }, 1000);
  };
});

function getCustomer(customerId) {
  for (var i = 0; i < customers.length; i++) {
    if (customers[i].id == customerId) {
      return customers[i];
    }
  }
  return undefined;
}


app.controller('SearchCtrl', function($scope){
  // this is where we change where we get the customers from
  $scope.customers = customers;
  $scope.data = {};
  $scope.found = {};

  $scope.searchPlate = function(plate){ 
    for (var i = 0; i < customers.length; i++) {
    if (customers[i].plate == plate) {
      $scope.found = customers[i];
      return $scope.found;
     }
    }
  };
});

var customers = [
    { name: 'Jack Reacher', plate: '1234', make: 'Honda', model: 'Accord', 
    vin: '1234567890', visits: [{date: '01/01/16', type: "oil change"}, {date: '06/11/16', type: "tire rotation"}, {date: '08/01/16', type:"tune up"}], id: 1 },
    { name: 'Joe Blow', plate: '9876', make: 'Honda', model: 'Accord', 
    vin: '1234567890', visits: [{date: '01/01/16', type: "oil change"}, {date: '06/11/16', type: "tire rotation"}], id: 2 }
  ];

app.controller('CustomerCtrl', function($scope,$state){
  $scope.customer = getCustomer($state.params.customerId);
  $scope.newCustomer = function(){
    alert("New Customer Created");
  };

});

app.controller('VisitCtrl', function($scope,$state){
  $scope.date = new Date();

  $scope.newVisit = function(){
    alert($scope.date);
  };

  $scope.test = null;

  $scope.getAdapter = function(){
    var resourceRequest = new WLResourceRequest("/adapters/CustomerInfo/customers",
                WLResourceRequest.GET
            );
    resourceRequest.send().then(
                  function(response){
                     $scope.test = JSON.parse(response.responseText);
                     $scope.$apply(); 
                  },
                  function(error){
                      console.log(error);
                  }
              );
  }; // end getAdapter


});



