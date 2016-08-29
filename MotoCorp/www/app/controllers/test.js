app.controller('TestCtrl', function($scope, $state , CRM){
  $scope.customer1 = "";
  $scope.newCustomer = "";
  $scope.newVisit = "";
  $scope.customerAll = "";

/* Get Customer with an id of 1 */
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

/* Get All Customers */
  $scope.customerAll = function(){
     CRM.getAllCustomers().then(function (results) {
      if(results.length === 0) {
        alert('No match found');
      } else {
        $scope.customerAll = results;
        $scope.$apply();
      }
    }).fail(function (error) {
      alert(error);
    });
  };

  /* Create a new customer with inputs */
  $scope.newCustomer = function(){
     CRM.newCustomer({name: this.name, plate: this.plate}).then(function (results) {
      if(results.length === 0) {
        alert('No match found');
      } else {
        $scope.newCustomer = results;
        $scope.$apply();
      }
    }).fail(function (error) {
      alert(error);
    });
  };

/* Create a new visit to id 1 */
  $scope.newVisit = function(){

// this works in swagger - {"date":"10/26/2016","type":"New Oil Change"}

     CRM.newVisit(3,{date: this.date,type: this.type}).then(function (results) {
      if(results.length === 0) {
        alert('No match found');
      } else {
        $scope.newVisit = results;
        $scope.$apply();
      }
    }).fail(function (error) {
      alert(error);
    });
  };

  // $scope.$on('$ionicView.enter', function() {
  //   CRM.test();
  // });

});
