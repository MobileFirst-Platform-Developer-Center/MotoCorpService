#  OnPrem - NodeJS Mock CRM

This CRM is a NodeJS mockup of a potential on-premise CRM for demo purposes.

## What you will learn on this guide

 - How to setup DashDB config on CRM Mockup
 - How to run the Mockup CRM on your Machine
 - How to test CRM

## Requirement of this guide

- [DashDB Setup on Bluemix](/Lab/Contents/MFP-DashDB-Adapter/Readme.md)

## Guide

### Configure DashDB CRM

1 - Go to dasdhdb-credentials.json
```bash
cd MotoCorpService/CRM
open dashdb-credentials.json
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
curl -X PUT -H "Content-Type: application/json" -d '{ "name": "New Name - B", "LicensePlate": "ABC-123", "Make":"Honda", "Model":"CRV", "VIN":"12345667" }' "http://localhost:8000/customers/" -vv
```

2 - To create a visit

```bash
curl -X POST -H "Content-Type: application/json" -d '{  "date": "2016-06-23",  "type": "oil visit",  "comment": "hello this is a comment"}' "http://localhost:8000/customers/1/visits" -vv
```

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
