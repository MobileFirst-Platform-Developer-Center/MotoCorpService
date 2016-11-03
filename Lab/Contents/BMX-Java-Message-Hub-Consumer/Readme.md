#  Message Hub Consumer




## What you will learn on this guide

 - Create a Bluemix - Java runtime app
 - To allow access to a Secure Gateway protected Tunnel
 - How the Message Hub Consumer works
 - How to test the consumer

## Requirement of this guide

- [Secure Gateway tunnel setup](/Lab/Contents/BMX-SecureGateway/Readme.md)
- [Bluemix Message Hub Setup](/Lab/Contents/MFP-MessageHub-Adapter/Readme.md)


## Guide

```
7. Message Hub Consumer
    1. Create Liberty cf
        1. Bind Liberty consumer to MessagHub Service
        2. talk about how to setup consumer
    2. Config with SecureGateway Firewall
    3. how it works (code snippets)
    4. How to test that it works
        1. calling the message hub adapter to create new visit/customer…should see new customer/visit in dashdb table)
```

Notes:
```

- go to the folder of the Consumer
- update to route properties

  - domain: mybluemix.net
  - name: JavaConsumer
  - host: javaconsumer
  - >to avoid error: The route javaconsumer.mybluemix.net is already in use.
- update the yml to add your MessageHub credentials(screenshots)
- update the yml to add your SecureGateway credentials(TBD)(screenshots)
- cf push
> Message Hub is a "public service" - to be confirmed if message hub is visible outside of your account.
- how to test MessageHub consumer?
-> sending our very special payload to the MessageHub Adapter at the guide(link to the guide)
-> CRM logs will show when new data arrived
->

*extra step - after the setup worked*
- adding protection secure gateway
- test the CRM enpoint via secure gateay(curl on CRM Setup - link ot guide)


Troubleshooting:
- SGW endpoint is ok

- MessageHub - check logs from Consume Runtime for SEVERE on setup issues
-> check if there is proper logs:
https://github.ibm.com/cord-americas/MotoCorpService/blob/master/MessageHubConsumer/src/main/java/com/motorcorp/messagehub/consumer/ConsumerInstance.java



Side note: to add java code for secure gateway at `  public ConsumerInstance() ` of https://github.ibm.com/cord-americas/MotoCorpService/blob/master/MessageHubConsumer/src/main/java/com/motorcorp/messagehub/consumer/ConsumerInstance.java
```


## Next guide

[MFP-Customer-Adapter](/Lab/Contents/MFP-Customer-Adapter/Readme.md)
