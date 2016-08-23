app.controller('VisitCtrl', function($scope, $state){
  $scope.date = new Date();

  $scope.newVisit = function(){
    alert($scope.date);
  };
});
