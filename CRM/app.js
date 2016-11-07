var _ = require('underscore'),
	fs = require('fs'),
	cfenv = require('cfenv'),
	ibmdb = require('ibm_db'),
	express = require('express'),
	DashDB = require('./modules/dashdb'),
	bodyParser = require('body-parser');


var dashDBConfig = (function () {
	if (process.env['VCAP_SERVICES']) {
		try {
			var services = JSON.parse(process.env['VCAP_SERVICES']);

			if (services['dashDB'] instanceof Array) {
				return services['dashDB'][0]['credentials'];
			}
		} catch (e) {
			console.warn('[WARNING] DashDB service is not bound to the application, no synchronization will occur.')
		}

	}

	return require('./dashdb-credentials.json');
})();

var app = express(),
    port = 8000,
    appEnv = null,
    dashDB = new DashDB(ibmdb, dashDBConfig);

try {
    appEnv = cfenv.getAppEnv(); // get the app environment from Cloud Foundry
    port = appEnv.port;
} catch (e){
    console.log('Running locally');
}

app.use(bodyParser.json());


// start server on the specified port and binding host
app.listen(port, '0.0.0.0', function () {
	// print a message when the server starts listening
	console.log("server starting on " + (appEnv ?  appEnv.url : 'localhost:'+port));
});


var customers = [];

try {
	customers = require('./customers.json');
} catch (e) {
	// error loading `customers.json`
	console.error('Couldn\'t load `customers.json`', e);
}

/* Main page */
app.get('/', function (req, res) {
	res.send(customers);
});


function saveCustomers(newCustomers) {
	// save new customer info
	var customerFile = './customers.json';

	fs.writeFile(customerFile, JSON.stringify(newCustomers, null, 4), function (err) {
		if (err) {
			console.error(err);
		} else {
			console.log("JSON saved to " + customerFile);
		}
	});
}

/* Get all customers */
app.get('/customers', function (req, res) {
	res.send(customers);
});

/* Add a customer to the customer file */
app.post('/customers', function (req, res) {
	var body = _.pick(req.body, 'Name', 'LicensePlate', 'Make', 'Model', 'VIN');

	if (!_.isString(body.Name) || !_.isString(body.LicensePlate)) {
		return res.status(400).send();
	}
	body.id = customers.length + 1;
	// add new customer to the customer array
	customers.push(body);

	// synchronize data with dashdb
	dashDB.connect().then(function () {
		return dashDB.create('CUSTOMERS', {
			CustomerID: body.id,
			Name: body.Name,
			LicensePlate: body.LicensePlate,
			Make: body.Make,
			Model: body.Model,
			VIN: body.VIN
		});
	}).then(function () {
		console.log('[SUCCESS] Customer saved to dashdb');
	}).catch(function (error) {
		console.error('[ERROR] A problem occurred while synchronizing data with dashdb', error);
	});

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


	if (customerIdx < 0) {
		res.status(404).send("Customer not found");
	} else {
		var visitIdx = _.findIndex(customers[customerIdx].visits, {id: visitId});
		if (visitIdx < 0) {
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
	var currentVisit = _.defaults(payloadVisit, {id: (customerVisits.length + 1)});

	dashDB.connect().then(function () {
		return dashDB.create('VISITS', {
			VISITID: currentVisit.id,
			CustomerID: customerId,
			Date: payloadVisit.date,
			Type: payloadVisit.type,
			Comments: payloadVisit.comment
		});
	}).then(function () {
		console.log('[SUCCESS] Customer saved to dashdb');
	}).catch(function (error) {
		console.error('[ERROR] A problem occurred while synchronizing data with dashdb', error);
	});


	updateOrCreateCustomerVisit(customerId, currentVisit.id, currentVisit);
	res.json(_.findWhere(customers[customerIdx].visits, {id: currentVisit.id}));
});

function updateOrCreateCustomerVisit(customerId, visitId, payloadDelta) {
	var matchedCustomerIdx = _.findIndex(customers, {id: customerId});
	if (matchedCustomerIdx < 0) {
		return res.status(404).send("Customer not found");
	}
	var customerVisits = (_.defaults(customers[matchedCustomerIdx], {visits: []})).visits;
	var visitIdx = _.findIndex(customerVisits, {id: visitId});
	if (visitIdx < 0) {
		//new array
		customers[matchedCustomerIdx].visits.push(_.extend(payloadDelta, {id: visitId}));
	} else {
		//visit exist:
		var currentVisit = customers[matchedCustomerIdx].visits[visitIdx];
		customers[matchedCustomerIdx].visits[visitIdx] = _.extend(currentVisit, payloadDelta);
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

});

app.post('/customers/_search', function (req, res) {
	var payloadSearch = req.body;
	return res.json(_.filter(customers, payloadSearch));
});

app.post('/message', function (req, res) {
	console.log(req.body);
	res.json(req.body);
});
