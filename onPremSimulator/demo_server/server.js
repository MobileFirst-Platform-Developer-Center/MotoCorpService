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

function saveCustomers(newCustomers){
  // save new customer info
  var customerFile = './customers.json';
  fs.writeFile(customerFile, JSON.stringify(newCustomers, null, 4), function(err) {
      if(err) {
        console.log(err);
      } else {
        console.log("JSON saved to " + customerFile);
      }
  });
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
app.post('/customers', function(req,res){
  var body = _.pick(req.body, 'name', 'plate');

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
app.get('/customers/:id', function(req,res){
  var customerId = parseInt(req.params.id,10);
  var matchedCustomer = _.findWhere(customers,{id:customerId});
  // if you found a match, return the customer, if not, send 404
  if(matchedCustomer){
    res.send(matchedCustomer);
  }else{
    res.status(404).send();
  }
});

/* Delete customer */
app.delete('/customers/:id', function(req, res){
  var customerId = parseInt(req.params.id,10);
  var matchedCustomer = _.findWhere(customers,{id:customerId});

	if (!matchedCustomer) {
		res.status(404).json({"Error": "No customer found"});
	} else {
		customers = _.without(customers, matchedCustomer);
    // save new customer file
    saveCustomers(customers);
    // display all customers
		res.json(customers);
	}
});

/* Update customer */
app.put('/customers/:id', function (req, res) {
	var customerId = parseInt(req.params.id, 10);
	var matchedCustomer = _.findWhere(customers, {id: customerId});
	var body = _.pick(req.body, 'name', 'plate');
	var ammendedCustomer = {};
	if (!matchedCustomer) {
		return res.status(404).send();
	}
	if (body.hasOwnProperty('name') && _.isString(body.name) && body.name.trim().length > 0) {
		ammendedCustomer.name = body.name;
	} else if (body.hasOwnProperty('name')) {
		return res.status(400).send();
	}
	if (body.hasOwnProperty('plate') && _.isString(body.plate) && body.plate.trim().length > 0) {
		ammendedCustomer.plate = body.plate;
	} else if (body.hasOwnProperty('plate')) {
		return res.status(400).send();
	}
  // replace the matchedCustomer with the ammendedCustomer
	_.extend(matchedCustomer, ammendedCustomer);
  // save the new customers to file
  saveCustomers(customers);
  // return all customers
	res.json(customers);
});
