app.factory('CRM', function () {

  var activeCustomer = null;

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
        activeCustomer = response.responseJSON;

        return activeCustomer;
      }, function (error) {
        return WLJQ.Deferred().reject(error.responseText).promise();
      });
    },
    newCustomer: function (customer) {
      var req = new WLResourceRequest('/adapters/CustomerInfo/customers/', WLResourceRequest.POST);
      req.setHeader('Content-type', 'application/json');

      return req.send(customer).then(function (response) {
        return response.responseJSON;
      }, function (error) {
        return WLJQ.Deferred().reject(error.responseText).promise();
      });
    },
    newVisit: function (customerId, visit) {
      var req = new WLResourceRequest('/adapters/CustomerInfo/customers/' + customerId + '/visits/', WLResourceRequest.POST);
      req.setHeader('Content-type', 'application/json');
      return req.send(visit).then(function (response) {
        if (activeCustomer != null) {
          if(!(activeCustomer.visits instanceof Array)) {
            activeCustomer.visits = [];
          }

          activeCustomer.visits.push(visit);
        }

        return response.responseJSON;
      }, function (error) {
        return WLJQ.Deferred().reject(error.responseText).promise();
      });
    }
  }
    ;

});
