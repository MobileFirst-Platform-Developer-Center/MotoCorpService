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

app.controller('NewVisitCtrl', function($scope, $state, CRM, $ionicHistory){
  var id = $state.params.id;
  
  $scope.visit = {
    date: new Date(),
    type: null,
    comment: null
  };

  $scope.newVisit = function(){
    CRM.newVisit(id, {date: $scope.visit.date,type: $scope.visit.type,comment:$scope.visit.comment}).then(function(response){
      alert('Added New Visit');
      /* Custom analytics tracking how many new visits created in app */
      WL.Analytics.log({ NewVisit: '1' }, response);
      WL.Analytics.send();
      $ionicHistory.clearCache();
      $ionicHistory.goBack();
    }).fail(function(error){
      alert(error);
    });
  };
});
