AcmeMotors MessageHub Consumer
==============================

This application on an environment variable `APP_CONFIG` which contains a configuration JSON object as follows:

```
{
   "kafka-rest-url":"https://${YOUR-MESSAGEHUB-KAFKA-REST-ENDPOINT}.messagehub.services.us-south.bluemix.net:443",
   "messagehub-api-key":"${YOUR-MESSAGEHUB-API-KEY}",
   "messagehub-username":"${YOUR-MESSAGEHUB-USERNAME}",
   "messagehub-password":"${YOUR-MESSAGEHUB-PASSWORD}",
   "message-hub-servers":"${YOUR-MESSAGEHUB-SERVERS}",
   "truststore-path":"${YOUR-KESYSTORE-PATH}",
   "truststore-password":"${YOUR-KEYSTORE-PASSWORD}",
   "truststore-type":"${YOUR-KEYSTORE-TYPE}",
   "crm-endpoint":"https://${YOUR-CRM-ENDPOINT}"
}
```

Running on Bluemix
==================

1. `mvn install` build the war

> **NOTE**: check the `manifest.yml` file and make sure the host matches your Bluemix's container host

2. `cf push` deploy to Bluemix
