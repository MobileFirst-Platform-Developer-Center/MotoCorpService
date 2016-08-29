var express = require('express');
var bodyParser = require('body-parser');
var fs = require('fs');
var _ = require('underscore');

var PORT = 8080;
var app = express();

// app.use(bodyParser.urlencoded({extended:true}));
app.use(bodyParser.json());

app.listen(PORT);
console.log('Running on http://localhost:' + PORT);


//read the customer info file
var customerDB = fs.readFileSync("./customers.json");
var customers = JSON.parse(customerDB);

function saveCustomers(newCustomers) {
	// save new customer info
	var customerFile = './customers.json';
	fs.writeFile(customerFile, JSON.stringify(newCustomers, null, 4), function (err) {
		if (err) {
			console.log(err);
		} else {
			console.log("JSON saved to " + customerFile);
		}
	});
	//customers = fs.readFileSync("./customers.json");
}

/* Main page */
app.get('/', function (req, res) {
	// res.sendFile(__dirname + '/index.html');
	res.send(customers);
});

/* Get all customers */
app.get('/customers', function (req, res) {
	res.send(customers);
});

/* Add a customer to the customer file */
app.post('/customers', function (req, res) {
	var body = _.pick(req.body, 'name', 'plate', 'make', 'model', 'vin');

	if (!_.isString(body.name) || !_.isString(body.plate)) {
		return res.status(400).send();
	}
	body.id = customers.length + 1;
	// add new customer to the customer array
	customers.push(body);
	// save new customer file
	saveCustomers(customers);
	// display all customers
	res.json(customers);
});

/* Get specific customer */
app.get('/customers/:id', function (req, res) {
	var customerId = parseInt(req.params.id, 10);
	var matchedCustomer = _.findWhere(customers, {id: customerId});
	// if you found a match, return the customer, if not, send 404
	if (matchedCustomer) {
		res.send(matchedCustomer);
	} else {
		res.status(404).send();
	}
});

app.get('/customers/:id/visits/', function (req, res) {
	var customerId = parseInt(req.params.id, 10);
	var matchedCustomer = _.findWhere(customers, {id: customerId});
	// if you found a match, return the customer, if not, send 404
	if (matchedCustomer) {
		//if visit is missing create one
		res.send((_.defaults(matchedCustomer, {visits: []})).visits);
	} else {
		res.status(404).send("Customer not found");
	}
});

app.get('/customers/:customerId/visits/:visitId', function (req, res) {
	var customerId = parseInt(req.params.customerId, 10); //why the default 10?
	var visitId = parseInt(req.params.visitId, 10); //why the default 10?
	var payloadDelta = req.body;
	var customerIdx = _.findIndex(customers, {id: customerId});


		if (customerIdx < 0 ) {
			res.status(404).send("Customer not found");
		} else {
			var visitIdx = _.findIndex(customers[customerIdx].visits, {id: visitId});
			if(visitIdx <0 ) {
				res.status(404).send("Visit not found");
			} else {
				var customerVisits = (_.defaults(customers[customerIdx], {visits: []})).visits;
				res.json(customerVisits[visitIdx]);
			}
		}

});


/* Delete customer */
app.delete('/customers/:id', function (req, res) {
	var customerId = parseInt(req.params.id, 10);
	var matchedCustomer = _.findWhere(customers, {id: customerId});

	if (!matchedCustomer) {
		res.status(404).json({"Error": "No customer found"});
	} else {
		customers = _.without(customers, matchedCustomer);
		// save new customer file
		saveCustomers(customers);
		// display all customers - success is 200
		res.sendStatus(200);
	}
});

app.put('/customers/:customerId/visits/:visitId', function (req, res) {
	var customerId = parseInt(req.params.customerId, 10); //why the default 10?
	var visitId = parseInt(req.params.visitId, 10); //why the default 10?
	var payloadDelta = req.body;
	var customerIdx = _.findIndex(customers, {id: customerId});
	updateOrCreateCustomerVisit(customerId, visitId, payloadDelta);
	res.json(_.findWhere(customers[customerIdx].visits, {id: visitId}));
});

app.post('/customers/:customerId/visits', function (req, res) {
	var customerId = parseInt(req.params.customerId, 10); //why the default 10?
	var payloadVisit = req.body;
	var customerIdx = _.findIndex(customers, {id: customerId});

  //if customer Visits are undefined - fix that
  var customerVisits = (_.defaults(customers[customerIdx], {visits: []})).visits;
  //create a new  visit id if it is a new object - generate new ID
  var currentVisit = _.defaults(payloadVisit, {id: (customerVisits.length+1)});

	updateOrCreateCustomerVisit(customerId, currentVisit.id, currentVisit);
	res.json(_.findWhere(customers[customerIdx].visits, {id: currentVisit.id}));
});

function updateOrCreateCustomerVisit(customerId, visitId, payloadDelta ){
	var matchedCustomerIdx = _.findIndex(customers, {id: customerId});
	if (matchedCustomerIdx < 0) {
		return res.status(404).send("Customer not found");
	}
	var customerVisits = (_.defaults(customers[matchedCustomerIdx], {visits: []})).visits;
	var visitIdx = _.findIndex(customerVisits, {id: visitId});
		if(visitIdx <0 ){
			//new array
			customers[matchedCustomerIdx].visits.push( _.extend(payloadDelta,{id: visitId}));
		} else {
			//visit exist:
			var currentVisit = customers[matchedCustomerIdx].visits[visitIdx];
			customers[matchedCustomerIdx].visits[visitIdx]=  _.extend(currentVisit,payloadDelta);
		}
		saveCustomers(customers);
}


/* Update customer - accept delta updates */
app.put('/customers/:id', function (req, res) {
	var customerId = parseInt(req.params.id, 10);
	var matchedCustomerIdx = _.findIndex(customers, {id: customerId});
	if (matchedCustomerIdx < 0) {
		return res.status(404).send();
	}
	var payloadDelta = req.body;
	//extend handles the nested scenario.
	customers[matchedCustomerIdx] = _.extend(customers[matchedCustomerIdx], payloadDelta);
	// save the new customers to file
	saveCustomers(customers);
	res.json(_.findWhere(customers, {id: customerId}));
	// current customer not all


	//Required evaluation do not makes much sense if it is an update.
	// removed
	//var required = ['name', 'plate'];
	/*
	delete payload.id;

	for (var key in payload) {
		var isRequired = required.indexOf(key);

		if (isRequired >= 0 && _.isEmpty(value)) {
			return res.status(400).send("Missing `" + key + "`.");
		}
	}
	*/
	// replace the matchedCustomer with the ammendedCustomer
	//_.extend(matchedCustomer, amendedCustomer);


});

app.post('/customers/_search', function (req, res) {
	var payloadSearch = req.body;
	return res.json(_.filter(customers, payloadSearch));
});
