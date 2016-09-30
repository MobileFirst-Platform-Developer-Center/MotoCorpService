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

      $ionicHistory.goBack();
    }).fail(function(error){
      alert(error);
    });
  };
});
