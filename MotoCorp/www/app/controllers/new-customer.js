/**
 * Copyright 2016 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
  };
});