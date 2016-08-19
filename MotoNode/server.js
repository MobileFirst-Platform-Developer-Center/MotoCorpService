var express = require('express');
var bodyParser = require('body-parser');
var fs = require('fs');

var PORT = 8080;
var app = express();

app.listen(PORT);
console.log('Running on http://localhost:' + PORT);

app.use(bodyParser.urlencoded({extended:true}));

//read the customer info file 
var customerDB = fs.readFileSync("customers.json");
var customers = JSON.parse(customerDB);

/* Main page */
app.get('/', function (req, res) {
  res.sendFile(__dirname + '/index.html');
});

/* Get customer info */
app.get('/customers', function (req, res) {
  res.send(customers);
});

/* Add a customer to the customerfile */
app.post('/customers', function(req,res){
  customers.push(req.body);
  console.log(customers);
});


