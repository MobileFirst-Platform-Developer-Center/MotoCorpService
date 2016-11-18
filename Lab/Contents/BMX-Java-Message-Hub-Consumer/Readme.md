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

## Guide

### Pushing the consumer application to Bluemix

>> **NOTE:** Before pushing the application to Bluemix using the cli, make sure the the route in the `manifest.yml` file is reserved or it's owned by you.

1. Update the `manifest.yml` file with your route as `host` and give it a `name`
2. Update the `APP_CONFIG` with the credentials obtained when creating the MessageHub instance on Bluemix.
  The following properties only need to be changed if they don't match your credentials or if you want to use a specific TrustStore other than the default:
    - `kafka-rest-url`
    - `message-hub-servers`
    - `truststore-password`
    - `truststore-path`
    - `truststore-type`
3. **TODO:** @cesarlb Secure Gateway
4. Build the application by running `mvn install`
5. Deploy to Bluemix using `cf push`

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

> **TODO:**



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

- [Setup of access to a protected gateway](/Lab/Contents/BMX-Java-Message-Hub-Consumer/Segure-Gateway-Protection.md)

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
- build WAR:
  mvn -B package
- cf push
> Message Hub is a "public service" - to be confirmed if message hub is visible outside of your account.
- how to test MessageHub consumer?
-> sending our very special payload to the MessageHub Adapter at the guide(link to the guide)
-> CRM logs will show when new data arrived
->

*extra step - after the setup worked*
- adding protection secure gateway
- test the CRM enpoint via secure gateway(curl on CRM Setup - link to guide)


Troubleshooting:
- SGW endpoint is ok

- MessageHub - check logs from Consume Runtime for SEVERE on setup issues
-> check if there is proper logs:
https://github.ibm.com/cord-americas/MotoCorpService/blob/master/MessageHubConsumer/src/main/java/com/motorcorp/messagehub/consumer/ConsumerInstance.java



Side note: to add java code for secure gateway at `  public ConsumerInstance() ` of https://github.ibm.com/cord-americas/MotoCorpService/blob/master/MessageHubConsumer/src/main/java/com/motorcorp/messagehub/consumer/ConsumerInstance.java
```


## Next guide

[Protecting Secure Gateway Destination](/Lab/Contents/BMX-Java-Message-Hub-Consumer/Segure-Gateway-Protection.md)

[MFP Customer Adapter](/Lab/Contents/MFP-Customer-Adapter/Readme.md)
