## Team

This project is been developed by the CORD Americas **Unbreakable Team**

### Unbreakable Team:

* Offering Manager: Carmel Schindelhaim, carmels@il.ibm.com
* Manager: Jim McGarrahan, mcgarr@us.ibm.com
* Scrum Master: Theodora Cheng, tcheng@us.ibm.com
* Developer: Cesar Lourenco Botti Filho, cesarlb@br.ibm.com
* Developer: Rob Puryear, sdpuryea@us.ibm.com
* Developer: Yoel Nunez, ynunez@us.ibm.com

### Previous Projects:

[Acme-Motors App](https://ibm.box.com/s/3j2r89z8acn4qdq6a08v7918idizifnb)
> Features:
> - Car Company Customer App
> - Mobile Foundation integration with [Mobile Application Content Manager](https://console.ng.bluemix.net/catalog/services/mobile-application-content-manager/)
> - Demo setup automation on bluemix
> - Ionic Hybrid App with MFP v8.0

## Project Overview

To explore the scenario which an Enterprise which owns car service centers wants to leverage Mobile Foundation and IBM Bluemix to expose its OnPrem CRM to its Services Centers employees via a Mobile App.

Considerations:
- OnPrem CRM should not be available in the public cloud.
- OnPrem CRM already servers the company internally, so performance should impacts should be considered on this new channel on interaction.

[For details check here](/Lab/0.Overview.md)


# Scenarios Covered on the Labs

| Lab                  | Status           | Goal                                                                                                                                                                                                                                    |
|:---------------------|:-----------------|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Lab01 - Online       | **Final Review** | To Explore the integration of Secure Gateway as safe tunnel to allow online access to an OnPrem CRM Solution via REST APIs                                                                                                              |
| Lab02 - Asynchronous | *Development*    | Extension of Lab01 Assets to Add MessageHub to allow Asynchronous OnPrem CRM Data update and DashDB as On Cloud CRM data cache, main goal to explore alternatives to improve app responsiveness without too much pressure of OnPrem CRM |


## Lab 1 - Online Scenario:

On this first Demo Tutorial, it is covered a initial integration of the following elements:

![Lab 01 - Data Flow](/Lab/img/architecture.png)

| Asset                        | Assets       | Description                                                                                 |
|:-----------------------------|:-------------|:--------------------------------------------------------------------------------------------|
| **Mockup NodeJS OnPrem CRM** | Code & Guide | Available to be ran as Local NodeJS App or a Docker Container.                              |
| **Secure Gateway Setup**     | Guide        | A step-by-step guide to a IBM Secure Gateway on Bluemix Account and connect with this demo  |
| **Ionic + MFP V8.0**         | Code & Guide | Main steps to add MFP V8.0 into a blank Ionic App                                           |
| **Java Adapter to Consume**  | Code & Guide | Sample Adapter to Consume the CRM OnPrem Data, that supports integration with SecureGateway |
| **Login Integration**        | Code & Guide | How  add Login/Logout function in a Ionic App integrated with MFP Authentication APIs       |
| **Secure Gateway**           | Code & Guide | How to allow MFP on Bluemix to setup firewall rules to protect a Secure Gateway Endpoint    |
| **Analytics**                | Code & Guide | Adding Custom Analytics on an Ionic App                                                     |



- [For details check here](/Lab/1.%20introduction.md)

- [Quick start Guide](Lab/2.%20quick-start.md)

- [Lab Guides](/Lab/)

![Mini Demo](/Lab/img/demo.gif)


## Lab 2 - Asynchronous Scenario:


Main goal of this scenario it is to add:
- **DashDB**: Using this data store service to act as a mirror of some data in your CRM to allow, reduce the pressure of consuming it online from your onPrem Environment and also improving the responsiveness to your MobileApp users.
- **MessageHub**: A service to allow a better pipeline to allow a more asynchronous data propagation of the data from your MobileApp to an OnPrem CRM.



*Additional features and scenario constrains still under discussion. At the moment this scenario it is in development.*

> *List of assets pending*

For the [scenario overview click here](/Lab/0.Overview.md) and [for more info here](https://github.ibm.com/cord-americas/MotoCorpService/wiki/Milestone-3)


# References:

- [Mobile Foundation](https://console.ng.bluemix.net/catalog/services/mobile-foundation/)
- [IBM Secure Gateway](https://console.ng.bluemix.net/catalog/services/secure-gateway/)
- [IBM DashDB](https://console.ng.bluemix.net/catalog/services/dashdb/)
- [IBM MessageHub](https://console.ng.bluemix.net/catalog/services/message-hub/)
- [Ionic](http://ionicframework.com/)
