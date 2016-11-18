#  Message Hub Consumer

The MessageHub consumer is a Java runtime app that consumes the topics "Customers" and "Visits" and posts (creates) them to the CRM.

## What you will learn on this guide

 - Create a Bluemix - Java runtime app
 - To allow access to a Secure Gateway protected Tunnel
 - How the Message Hub Consumer works
 - How to test the consumer

## Requirement of this guide

- [Secure Gateway tunnel setup](/Lab/Contents/BMX-SecureGateway/Readme.md)
- [Bluemix Message Hub Setup](/Lab/Contents/MFP-MessageHub-Adapter/Readme.md)
- Maven. To install visit Maven [Installation](https://maven.apache.org/install.html) page
- Setup [CloudFoundry CLI](https://console.ng.bluemix.net/docs/starters/install_cli.html)

## Guide

### Pushing the consumer application to Bluemix

>> **NOTE:** Before pushing the application to Bluemix using the cli, make sure the the route in the `manifest.yml` file is reserved or it's owned by you.

1 - Update the `manifest.yml` file with your route as `host` and give it a `name`
   - `${YOUR_MESSAGE_HUB_APY_KEY}`
   - `${YOUR_MESSAGE_HUB_USERNAME}`
   - `${YOUR_MESSAGE_HUB_PASSWORD}`
   - `${YOUR_CRM_DESTINATION}`
   > Make sure not to add a `/` to the end of your CRM destination which is found in your SecureGateway service
2 - To use protected SecureGateway Destination go to the [lab](Secure-Gateway-Protection.md)
3 - Build the application by running `mvn install`
4 - Deploy to Bluemix using `cf push`

### MessageHub Consumer - How it Works

In the `pom.xml` there are dependencies for the Apache Geronimo (`org.apache.geronimo.specs.geronimo-servlet_3.0_spec`), Java EE (`javax.javaee-web-api`), logger (`org.slf4j.slf4j-log4j12`), MessageHub Client (`org.apache.kafka.kafka-clients`), JSON (`org.json.json`) and Http Client (`com.squareup.okhttp3.okhttp`)

```xml
<dependencies>
    <dependency>
        <groupId>org.apache.geronimo.specs</groupId>
        <artifactId>geronimo-servlet_3.0_spec</artifactId>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>javax</groupId>
        <artifactId>javaee-web-api</artifactId>
        <version>6.0</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-log4j12</artifactId>
        <version>1.7.5</version>
    </dependency>
    <dependency>
        <groupId>org.apache.kafka</groupId>
        <artifactId>kafka-clients</artifactId>
        <version>0.10.0.0</version>
    </dependency>
    <dependency>
        <groupId>org.json</groupId>
        <artifactId>json</artifactId>
        <version>20160810</version>
    </dependency>
    <dependency>
        <groupId>com.squareup.okhttp3</groupId>
        <artifactId>okhttp</artifactId>
        <version>3.4.1</version>
    </dependency>
</dependencies>
```

### Consumer Instance and Enterprise Java Beans

> **NOTE:** This consumer instance uses Enterprise Java Beans (EJB) for more information visit http://www.oracle.com/technetwork/java/javaee/ejb/index.html

Only one instance of `ConsumerInstance` needs to be instantiated, therefore it's annotated as a singleton.

```
@Singleton
public class ConsumerInstance {
    // ...
}
```

Once the `ConsumerInstance` is instantiated, Secure Gateway is configured, the topics are added to `topics` array, and the consumer is initialized.

```
    public ConsumerInstance() {

		setupSecureGatewayFirewall();


        topics.add("new-customer");
        topics.add("new-visit");

        try {
            initConsumer(topics);
        } catch (EnvLoader.MissingConfigurationException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
```

To initialize the consumer, the environment variable `APP_CONFIG` is loaded. Then the new customer and new visit receivers are registered. Finally, the MessageHub consumer is instantiated and we subscribe to the topics.

```
    protected void initConsumer(List<String> topics) throws EnvLoader.MissingConfigurationException {
    	logger.info("properties" + System.getenv("APP_CONFIG") );
    	Properties properties = EnvLoader.load();

        logger.info("properties" + Arrays.toString(properties.values().toArray()));

        String crmEndpoint = properties.getProperty("crm-endpoint");

        receivers.put("new-customer", new CustomerMessageReceiver(crmEndpoint));
        receivers.put("new-visit", new CustomerVisitMessageReceiver(crmEndpoint));


        KafkaConfig kafkaConfig = new KafkaConfig();
        kafkaConfig.loadConfig(properties);

        javax.security.auth.login.Configuration.setConfiguration(kafkaConfig);

        MessageHubProperties messageHubProperties = MessageHubProperties.getInstance(properties);

        consumer = MessageHubConsumer.getInstance(messageHubProperties);
        consumer.subscribe(topics);
    }
```

For the `checkForIncomingMessagesEverySecond` method will run every second, therefore we use the EJB annotation `Schedule` as shown below.

If the consumer instance is not instantiated we try to instantiate it and exit the method. If the consumer instance is initialized then get get all the records and call the `handleMessage` helper function.

```
    @Schedule(hour = "*", minute = "*", second = "*/1", persistent = false)
    public void checkForIncomingMessagesEverySecond() {
        if (consumer == null) {
            try {
                logger.log(Level.INFO, "Initializing consumer instance");
                initConsumer(topics);
            } catch (EnvLoader.MissingConfigurationException e) {
                // consumer could not initialize due to missing `APP_CONFIG` env variable.
                // we are not repeating the error message displayed in the constructor
            }
            return;
        }

        for (ConsumerRecord<byte[], byte[]> message : consumer.poll(1000)) {
            logger.log(Level.INFO, "Received Message");
            logger.log(Level.INFO, "Topic: " + message.topic());
            logger.log(Level.INFO, "Payload: " + new String(message.value()));
            handleMessage(message.topic(), message.value());
        }
    }
```

The `handleMessage` function checks if there is any `MessageReceiver` registered for the specific topic and calls the `receiveMessage` function

```
    public void handleMessage(String topic, byte[] payload) {
        MessageReceiver receiver = receivers.get(topic);

        if (receiver == null) {
            logger.log(Level.WARNING, "No receiver found for topic=" + topic);
            return;
        }

        receiver.receiveMessage(payload);
    }
```

There are two receivers registered in this project by default `CustomerMessageReceiver` and `CustomerVisitMessageReceiver`. These receivers are responsible for getting the message to the CRM.

##### CustomerMessageReceiver

```
@Override
public void receiveMessage(byte[] payload) {
    try {

        logger.log(Level.INFO, "Sending New Customer Request");
        logger.log(Level.INFO, "Endpoint: " + endpoint + "/customers");
        logger.log(Level.INFO, "Payload: " + new String(payload));

        HttpClient.getInstance().post(endpoint + "/customers", new String(payload));
    } catch (IOException e) {
        logger.log(Level.SEVERE, e.getMessage());
    }
}
```

##### CustomerVisitMessageReceiver

```
@Override
public void receiveMessage(byte[] payload) {
    try {
        JSONObject json = new JSONObject(new String(payload));
        String requestUrl = endpoint + "/customers/" + json.optInt("CustomerId") + "/visits/";

        logger.log(Level.INFO, "Sending New Customer Visit Request");
        logger.log(Level.INFO, "Endpoint: " + requestUrl);
        logger.log(Level.INFO, "Payload: " + json.optJSONObject("CustomerVisit"));

        HttpClient.getInstance().post(requestUrl, json.optJSONObject("CustomerVisit"));

    } catch (JSONException | IOException e) {
        logger.log(Level.SEVERE, e.getMessage());
    }
}
```


## Testing

To test if the consumer is working, go into your MessageHub Adapter Swagger docs as you did in the MessageHub Adapter lab and create a new customer. If the consumer works, you will see logs like below in your CF app in your Bluemix instance of the consumer.

```bash
App/0[INFO ] Payload: {"Name":"string","LicensePlate":"string","Make":"string","Model":"string","VIN":"string"}2016-11-18T19:10:49.837-0200
App/0[INFO ] Endpoint: http://cap-sg-prd-2.integration.ibmcloud.com:16384/customers2016-11-18T19:10:49.837-0200
App/0[INFO ] Payload: {"Name":"string","LicensePlate":"string","Make":"string","Model":"string","VIN":"string"}2016-11-18T19:10:49.837-0200
App/0[INFO ] Received Message2016-11-18T19:10:49.837-0200
App/0[INFO ] Sending New Customer Request2016-11-18T19:10:49.837-0200
App/0[INFO ] Topic: new-customer2016-11-18T19:10:49.837-0200
```

## Next guide

[Protecting Secure Gateway Destination](/Lab/Contents/BMX-Java-Message-Hub-Consumer/Secure-Gateway-Protection.md)