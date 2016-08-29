app.controller('CustomerCtrl', function ($scope, $state, CRM) {
  var id = $state.params.id;

  $scope.addVisit = function () {
    $state.go('app.customer.visit', {id: id});
  };

  CRM.getCustomer(id).then(function (customer) {
    $scope.customer = customer;
    $scope.$apply();
  }).fail(function (error) {
    alert(error);
  });
});
