#  OnPrem - NodeJS Mock CRM

This CRM is a NodeJS mockup of a potential on-premise CRM for demo purposes.
The CRM is updated when the MessageHub consumer calls the rest api's to create the new customers and visits.
It then syncs with DashDB by immediately writing the new customers and visits to DashDB.

## What you will learn on this guide

 - How to setup DashDB config in the Mock CRM
 - How to run the Mock CRM on your Machine
 - How to test CRM

## Requirement of this guide

- [DashDB Setup on Bluemix](/Lab/Contents/MFP-DashDB-Adapter/Readme.md)

## Guide

### Configure OnPrem CRM

1 - Open the `dasdhdb-credentials.json` file
```bash
cd MotoCorpService/CRM
```

2 - Set DashDB credentials as found in your Bluemix instance
![dashdb-credentials](dashdb-credentials.png)

### Running the NodeJS CRM locally

1 - Install any dependencies
```bash
npm install
````

2 - Run the CRM locally
```bash
node app.js
````

> To use SecureGateway later in the lab, it is recommended to run the CRM behind a firewall.

### Testing the CRM

In the command line, you can test the CRM.

1 - To create a customer

```bash
curl -X POST -H "Content-Type: application/json" -d '{ "Name": "John Doe", "LicensePlate": "ABC-123","Make":"Honda", "Model":"CRV", "VIN":"12345667" }' "http://localhost:8000/customers/" -vv
```

Go into your DashDB Customer table to confirm a customer record was created.

![customer table](customer-table.png)

2 - To create a visit

```bash
curl -X POST -H "Content-Type: application/json" -d '{  "date": "2016-06-23",  "type": "oil visit",  "comment": "hello this is a comment"}' "http://localhost:8000/customers/1/visits" -vv
```

Go into your DashDB Customer table to confirm a visit record was created.

![visit table](visit-table.png)

3 - To list all customers

```bash
curl -X GET http://localhost:8000/customers -vv
```

4 - To list all visits

```bash
curl -X GET http://localhost:8000/customers/1/visits/ -vv
```

## Next guide

[BMX-SecureGateway](/Lab/Contents/BMX-SecureGateway/Readme.md) 
