app.factory('CRM', function () {

  return {
    test: function (query) {
      console.log("----test service");
    },
    getAllCustomers: function () {
      var req = new WLResourceRequest('/adapters/CustomerInfo/customers/', WLResourceRequest.GET);

      return req.send().then(function (response) {
        return response.responseJSON;
      }, function (error) {
        return WLJQ.Deferred().reject(error.responseJSON).promise();
      });
    },
    search: function (query) {
      var req = new WLResourceRequest('/adapters/CustomerInfo/customers/search', WLResourceRequest.POST);
      req.setHeader('Content-type', 'application/json');
      return req.send(query).then(function (response) {
        return response.responseJSON;
      }, function (error) {
        return WLJQ.Deferred().reject(error.responseText).promise();
      });
    },
    getCustomer: function (id) {
      var req = new WLResourceRequest('/adapters/CustomerInfo/customers/' + id, WLResourceRequest.GET);
      req.setHeader('Content-type', 'application/json');

      return req.send().then(function (response) {
        return response.responseJSON;
      }, function (error) {
        return WLJQ.Deferred().reject(error.responseText).promise();
      });
    },
    newCustomer: function (customer) {
      var req = new WLResourceRequest('/adapters/CustomerInfo/customers/', WLResourceRequest.PUT);
      req.setHeader('Content-type', 'application/json');

      return req.send(customer).then(function (response) {
        return response.responseJSON;
      }, function (error) {
        return WLJQ.Deferred().reject(error.responseText).promise();
      });
    },
    newVisit: function (customerId, visit) {
<<<<<<< HEAD
      var req = new WLResourceRequest('/adapters/CustomerInfo/customers/' + customerId + '/visits/', WLResourceRequest.POST);
      req.setHeader('Content-type', 'application/json');
=======
      console.log("Customer id is : " + customerId);
      console.log("Visit is : " + visit);
      var req = new WLResourceRequest('/adapters/CustomerInfo/customers/' + customerId + '/visits/', WLResourceRequest.PUT);
      req.setHeader('Content-type', 'application/json');

>>>>>>> 7dd6c0c001a082b5d45662fcd41597c9b226f1cd
      return req.send(visit).then(function (response) {
        return response.responseJSON;
      }, function (error) {
        return WLJQ.Deferred().reject(error.responseText).promise();
      });
    }
  };

});
