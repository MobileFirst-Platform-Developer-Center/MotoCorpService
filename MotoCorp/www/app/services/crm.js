app.factory('CRM', function () {

  return {
    test: function (query) {
      console.log("----test service");
    },
    search: function (query) {
      var req = new WLResourceRequest('/adapters/CustomerInfo/customers/search', WLResourceRequest.POST);
      req.setHeader('Content-type', 'application/json');
      return req.send(query).then(function (response) {
        return response.responseJSON;
      }, function (error) {
        return WLJQ.Deferred().reject(error.responseJSON).promise();
      });
    },
    getCustomer: function (id) {
      var req = new WLResourceRequest('/adapters/CustomerInfo/customers/' + id, WLResourceRequest.GET);

      return req.send().then(function (response) {
        return response.responseJSON;
      }, function (error) {
        return WLJQ.Deferred().reject(error.responseJSON).promise();
      });
    },
    newCustomer: function (customer) {
      var req = new WLResourceRequest('/adapters/CustomerInfo/customers/', WLResourceRequest.PUT);

      return req.send(customer).then(function (response) {
        return response.responseJSON;
      }, function (error) {
        return WLJQ.Deferred().reject(error.responseJSON).promise();
      });
    },
    newVisit: function (customerId, visit) {
      var req = new WLResourceRequest('/adapters/CustomerInfo/customers/' + customerId + '/visits/', WLResourceRequest.PUT);

      return req.send(visit).then(function (response) {
        return response.responseJSON;
      }, function (error) {
        return WLJQ.Deferred().reject(error.responseJSON).promise();
      });
    }
  };

});
