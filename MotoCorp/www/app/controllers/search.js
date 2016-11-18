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

app.controller('SearchCtrl', function ($scope, $state, Auth, CRM) {
  $scope.results = [];
  $scope.search = {
    "text":"",
    "type":"plate"
  };

  $scope.addCustomer = function(){
    $state.go('app.new-customer');
  };

  $scope.searchPlate = function () {
    $scope.search.text = this.query;

// This sends the search payload to the search function at the CRM service.
    CRM.search($scope.search).then(function (results) {
      if(results.length === 0) {
        alert('No match found');
      } else {
        $scope.results = results;
        $scope.$apply();
      }
    }).fail(function (error) {
      alert(error);
    }); 
  };
});
