# MotoCorpService - Lab

The general idea of this lab it is the following:
> Imagine you are an Enterprise that owns car service centers. You want to equip service center employees with tablets, and build an app that will help them coordinate activities in the service center to improve service times and quality of service.

> More  detailed description [here](/Lab/Contents/Sample/overview.md).

![Lab 2 - Map](/Lab/img/Lab2-Map.png)



## guides

Checklist for completing


|   #    | Name/Link                                                                                                | Code  | Doc Author | Has Blocker(Doc) | Doc Review | Closed |
|:------:|:---------------------------------------------------------------------------------------------------------|:-----:|:----------:|:----------------:|:-----------|:-------|
|   1    | [Overview](/Lab/Contents/Sample/Readme.md)                                                               |   X   |     ?      |        ?         |            |        |
|   2    | [Quickstart](/Lab/Contents/Quickstart/Readme.md)                                                         |   X   |     ?      |        ?         |            |        |
|   3    | [MFP-Setup-Mobile-Foundation-on-Bluemix](/Lab/Contents/MFP-Setup-Mobile-Foundation-on-Bluemix/Readme.md) |   X   |    dora      |        no        |            |        |
|   4    | [MFP-Security-Implement-Login](/Lab/Contents/MFP-Security-Implement-Login/Readme.md)                     |   X   |    dora    |        no        |            |        |
|   5    | [MFP-MessageHub-Adapter](/Lab/Contents/MFP-MessageHub-Adapter/Readme.md)                                 |  R6   |    yoel    |        no        |            |        |
|   6    | [MFP-DashDB-Adapter](/Lab/Contents/MFP-DashDB-Adapter/Readme.md)                                         |   X   |     ?      |        no        |            |        |
|   7    | [NodeJS-CRM-OnPrem](/Lab/Contents/NodeJS-CRM-OnPrem/Readme.md)                                           |  R1   |     ?      |        no        |            |        |
|   8    | [BMX-SecureGateway](/Lab/Contents/BMX-SecureGateway/Readme.md)                                           |   -   |  cesarlb   |        no        |            |        |
|   9    | [BMX-Java-Message-Hub-Consumer](/Lab/Contents/BMX-Java-Message-Hub-Consumer/Readme.md)                   | R2-R7 |     -      |      yes-B1      |            |        |
|   10   | [MFP-Customer-Adapter](/Lab/Contents/MFP-Customer-Adapter/Readme.md)                                     |  R6   |     ?      |        B2        |            |        |
|   11   | [MFP-Ionic-MobileApp](/Lab/Contents/MFP-Ionic-MobileApp/Readme.md)                                       |  R4   |    rob     |        no        |            |        |
| NOT 12 | [Bluemix-Mobile-Analytics](/Lab/Contents/Bluemix-Mobile-Analytics/Readme.md)                             |  R5   |     ?      |       yes        |            |        |


```


@dora will be out at 9th, 10th  - 8th(deadline)
- Hard deadline: Nov, 11th
- Present to Carmel: Nov, 14th


```
B2: How deep we need to explain this code? Probably not so deep, for the sake of clarity.

R6: It is not confirmed if the adapter is posting data to the MessageHub(topic)

R7: It is possible the consumer is not working as expected

B1: we are not sure if the Consumer is working as expected, tests are failing.


- Environment and the you use
- Focus on Visits updates

Observations:
R1: *Someone* will verify the CRM - Test the *entire flow*, the CRM running locally. -> `(new R1) How to just test the CRM interfaces, not the entire flow. ()`
-> Postman/CURL -> see if the data appears on dashdb

R2: Code for Java to protect - Cesar, still pending.

R3: Confirm if the endpoint its ok for the CRM - done, the Yml update yoel - Done

R4: The app it is not working, the adapter integration it is not ok. (the new customer endpoint, it is not working)
  - Login works
  - Search works
  - DashDb Works
  - MessageHub not ok -> not working on Robs account pointing to doras account)
  -
R5: To investigate if foundation analytics is able to be used. - Not part of this delivery for now. Dora, needs to talk with ajay.

R6: To protected the Customer adapter - Pending


Raw Guides
```
1. Set up Mobile Foundation services
    1. setup the environment (ionic/foundation cli)
    2. deploy the full app
2. (Security) Implement Login
    1. login adapter (deploy adapter/console config, create a client secret)
        1. limitation of deploying adapter (size)
    2. relevant client side configurations (code snippets)
3. MessageHub Adapter
    1. Create MessageHub Service
    2. Java Adapter Producer (customer + visitor create)
    3. how it works (code snippets)
    4. How to test that it works (check in liberty logs or for not 500)
4. DashDB Adapter (read cust/visit info)
    1. create DashDB Services
    2. Create tables (sql script) - TODO
    3. Populate dummy data (sql script) - TODO
    4. deploy adapter
    5. go over code snippets in dashdb adapter
    6. How to test that it works
5. CRM
    1. npm install + run node app locally (behind firewall)
    2. Show common rest api urls (like get all customers)
    3. update DashDB config
    4. test CRM to create new customer/visit (curl/postman)
6. SecureGateway (for message hub consumer)
    1. Create SecureGateway instance on blue mix
    2. set up secure gateway
7. Message Hub Consumer
    1. Create Liberty cf
        1. Bind Liberty consumer to MessagHub Service
        2. talk about how to setup consumer
    2. Config with SecureGateway Firewall
    3. how it works (code snippets)
    4. How to test that it works
        1. calling the message hub adapter to create new visit/customer…should see new customer/visit in dashdb table)
8. Customer Adapter (Adapter Mashup)
    1. use customer adapter to call  message hub adapter
        1. code snippets
        2. test
    2. use customer adapter to call dash db adapter
        1. code snippets
        2. test
9. UI Integration (backend/frontent)
    1. show CRM service in ionic app (code snippets)
    2. show how to call that within the controllers (code snippets)
    3. show how it binds to the pages (code snippets)
    4. run the app
        1. create customer
        2. search customer
        3. create visit
        4. customer details -> visit
10. Analytics if you want (possible)

```
