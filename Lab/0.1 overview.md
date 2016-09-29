# Introduction

Imagine you are an Enterprise that owns car service centers. You want to equip service center employees with tablets, and build an app that will help them coordinate activities in the service center to improve service times and quality of service.

One important element in this solution it is to allow your employees to have access to the most accurate data about your customer. As a customer can engage with your Enterprise via multiple channels.

![Demo Map](/Lab/img/Overview-CRM.png)

> CRM: For this example we will use the CRM idea to be central place to store/retrieve the most updated information about a customer information and to open and post tickets [Read more about here](https://en.wikipedia.org/wiki/Customer_relationship_management)


So through this example we will explore a possible architecture that would allow a Enterprise that already has an OnPrem CRM solution, to expose the CRM data to a mobile app for its Service Centers employees, so they can deliver better customer service.

This Example will cover two main ways to do this:

- **Online/Synchronous**: When you have a mobile app reading and writing to the backend via the CRM apis, as a traditional OLAP database. In this scenario, it would require more from the CRM solution to support peaks and valleys of demand from the service center customer flows or the other supported channels.  


- **Streams/Asynchronous**: When you have a mobile app reading from a cache data repository updated as frequently as possible, and writing via an messaging system. This allows the CRM to consume updates in a more controlled flow, mitigating the peaks and valleys of a service center working day. So if required, more resource can be added to the CRM to support more load trough the day, to keep data updated with less latency.

Topics that will be explored on this solution:

- **MobileApp**:
  - User Authentication: Login/Logout of the app & user identity. More details at the [Security Lab](/Lab/5.%20security.md)
  - Exposing APIs to your mobile clients. More details in the  [Adapters Lab](/Lab/3.%20adapters.md)
  - Easy Update of your API parameters(Zero Code Conf). More details in the [Config API at Adapters Lab](/Lab/3.%20adapters.md#configuration-api)
  - API Protection: Only an authorized user can consume a given service, exposed to the mobile app.
  - Exploring Ionic for User Interface (mobile app)
  - Collecting app usage metrics
  - App Lifecycle Management
  - Exploring Bluemix based deployment of MobileFirst Platform


- **Backend**:
  - Mockup CRM: An NodeJS based Application that will act as a fake CRM to allow you easily replicate the content of this sample, and      mimic an on-prem system of record.
  - Secure Gateway: A set of guides on how to expose your OnPrem CRM to a bluemix Based service in way that can be only be consumed by your Bluemix based apps.
  - DashDB: Using this data store service to act as a mirror of some data in your CRM to reduce the pressure of consuming it online from your onPrem Enviroment and also improve the responsiveness of your mobile app.
  - MessageHub: A service to allow a better pipeline to allow a more asynchronous data propagation of the data from your mobile app to an OnPrem CRM.


# Scenario Considerations

- Replacing a desktop user interface to a system to a mobile based can bring the following challenges to be handled:
  - **More user interaction, more pressure on backend systems**: [MobileFirst Adapters](https://mobilefirstplatform.ibmcloud.com/tutorials/en/foundation/8.0/adapters/) are a great help to manage the pressure sent to your backend systems.

  - **Mobile users want apps to be responsive**: [Ionic](http://ionicframework.com/) is a great front end framework to build a good user interface that integrates well with MobileFirst Foundation. MobileFirst Foundation allows you to use any front end framework you choose.

  - **Once you have a mobile app Available, users will demand improvements and new features**: [MobileFirst Console](https://mobilefirstplatform.ibmcloud.com/tutorials/en/foundation/8.0/setting-up-your-development-environment/console/) and features like [MobileFirst Direct Update](https://mobilefirstplatform.ibmcloud.com/tutorials/en/foundation/8.0/using-the-mfpf-sdk/direct-update/) will help you keep your app development/delivery cycle at pace with user demands.

  - **Understanding your mobile app users**: [MobileFirst Analytics](https://mobilefirstplatform.ibmcloud.com/tutorials/en/foundation/8.0/analytics/) will give you insights into what users are doing with your app, and in this lab we will explore how to leverage this tool.

  - **A mobile app is one of many channels through which users can interact with your backend systems**: In these labs we will explore [IBM DashDB](https://console.ng.bluemix.net/catalog/services/dashdb/) and [IBM MessageHub](https://console.ng.bluemix.net/catalog/services/message-hub/) to act as part of a pipeline to build a very responsive mobile app. Allowing service centers to have the most up to date info as possible to act on, and also alleviate the pressure on the backend system.

  - **Enterprise Mobile Apps are usually served by a combination of cloud services & backend systems that may be installed in the Enterprise Data Center**: This example will also explore [IBM Secure Gateway](https://console.ng.bluemix.net/catalog/services/secure-gateway/) to allow MobileFirst on Bluemix to host APIs that will propagate changes to an OnPrem System, while allowing only the MobileFirst mobile app servers able to reach the OnPrem system from Bluemix. This protects exposure of your OnPrem services, a standard hybrid cloud scenario.
