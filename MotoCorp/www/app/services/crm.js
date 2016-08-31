app.factory('CRM', function () {

  var activeCustomer = null;

  function sendRequest(path, method, payload) {
    var req = new WLResourceRequest(path, method);
    req.setHeader('Content-type', 'application/json');
    return req.send(payload).then(function (response) {
      return response.responseJSON;
    }, function (error) {
      return WLJQ.Deferred().reject(error.responseText).promise();
    });
  }

  return {
    test: function (query) {
      console.log("----test service");
    },
    getAllCustomers: function () {
      return sendRequest('/adapters/CustomerInfo/customers/', WLResourceRequest.GET);
    },
    search: function (query) {
      return sendRequest('/adapters/CustomerInfo/customers/search', WLResourceRequest.POST, query)
    },
    getCustomer: function (id) {
      return sendRequest('/adapters/CustomerInfo/customers/' + id, WLResourceRequest.GET).then(function(customer){
        activeCustomer = customer;

        return activeCustomer;
      });
    },
    newCustomer: function (customer) {
      return sendRequest('/adapters/CustomerInfo/customers/', WLResourceRequest.POST, customer);
    },
    newVisit: function (customerId, visit) {

      return sendRequest('/adapters/CustomerInfo/customers/' + customerId + '/visits/', WLResourceRequest.POST, visit).then(function (response) {
        if (activeCustomer != null) {
          if (!(activeCustomer.visits instanceof Array)) {
            activeCustomer.visits = [];
          }

          activeCustomer.visits.push(visit);
        }

        return response;

      });
    }
  };
});
