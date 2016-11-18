/**
 * Copyright 2016 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
