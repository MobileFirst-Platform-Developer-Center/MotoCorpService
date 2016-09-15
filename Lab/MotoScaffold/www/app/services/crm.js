app.factory('CRM', function () {

  var activeCustomer = null;

  // Add the sendRequest function here


  return {
    test: function (query) {
      console.log("----test service");
    },
    getAllCustomers: function () {
      return sendRequest('/adapters/CustomerInfo/customers/', WLResourceRequest.GET);
    },
    search: function (query) {
      // Search function code goes here
    },
    getCustomer: function (id) {
      // View customer details code goes here
    },
    newCustomer: function (customer) {
      // Add new customer code goes here  
     },
    newVisit: function (customerId, visit) {
      // New visit code goes here
    }
  };
});
