app.controller('VisitCtrl', function($scope, $state, CRM){
  $scope.date = new Date();

  $scope.newVisit = function(){
    CRM.newVisit()
    alert($scope.date);
  };
});
