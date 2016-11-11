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
      return sendRequest('/adapters/CustomerAdapter/resource/customers', WLResourceRequest.GET);
    },
    search: function (query) {
      return sendRequest('/adapters/CustomerAdapter/resource/searchCustomer', WLResourceRequest.POST, query);
    },
    getCustomer: function (plate) {
      return sendRequest('/adapters/CustomerAdapter/resource/customers/' + plate, WLResourceRequest.GET);
    },
    newCustomer: function (customer) {
      return sendRequest('/adapters/CustomerAdapter/resource/newCustomer', WLResourceRequest.POST, customer);
    },
    newVisit: function (customerId, visit) {
      return sendRequest('/adapters/CustomerAdapter/resource/' + customerId + '/newVisit', WLResourceRequest.POST, visit).then(function (response) {
        if (activeCustomer !== null) {
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
