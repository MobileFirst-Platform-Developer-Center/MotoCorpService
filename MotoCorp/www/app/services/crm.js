app.factory('CRM', function () {

  return {
    search: function (query) {
      var req = WLResourceRequest('/adapters/CustomerInfo/customers', WLResourceRequest.POST);

      return req.send(query).then(function (response) {
        return response.responseJSON;
      }, function (error) {
        return WLJQ.Deferred().reject(error.responseJSON).promise();
      });
    },
    getCustomer: function (id) {
      var req = WLResourceRequest('/adapters/CustomerInfo/customers/' + id, WLResourceRequest.GET);

      return req.send().then(function (response) {
        return response.responseJSON;
      }, function (error) {
        return WLJQ.Deferred().reject(error.responseJSON).promise();
      });
    },
    newCustomer: function (customer) {
      var req = WLResourceRequest('/adapters/CustomerInfo/customers/', WLResourceRequest.PUT);

      return req.send(customer).then(function (response) {
        return response.responseJSON;
      }, function (error) {
        return WLJQ.Deferred().reject(error.responseJSON).promise();
      });
    },
    newVisit: function (customerId, visit) {
      var req = WLResourceRequest('/adapters/CustomerInfo/customers/' + customerId + '/visits/', WLResourceRequest.PUT);

      return req.send(visit).then(function (response) {
        return response.responseJSON;
      }, function (error) {
        return WLJQ.Deferred().reject(error.responseJSON).promise();
      });
    }
  };

});
