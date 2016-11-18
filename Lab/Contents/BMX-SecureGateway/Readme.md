#  Bluemix - Setup of Secure Gateway Tunnel

Here we will do an overview of Bluemix Secure Gateway Service and then a step-by-step guide on how to set it up for this lab. 

## What you will learn in this guide

 - What is secure gateway
 - How to create a gateway
 - How to run a Security Gateway Client OnPrem
 - How to setup a Secure Gateway Destination on Bluemix
 - How to protect the Secure Gateway Tunnel between Bluemix and On-Prem will be covered on [this other guide](/Lab/Contents/BMX-Java-Message-Hub-Consumer/Segure-Gateway-Protection.md)
 - How to test the just create gateway

## Requirement of this guide

- [On Prem NodeJS CRM](/Lab/Contents/NodeJS-CRM-OnPrem/Readme.md)


## Guide

### Secure Gateway Overview

#### What is IBM Secure Gateway?

It is a service available on Bluemix that allows you to access OnPrem Services of your data center(eg: an web service) on a IBM Bluemix Application(eg: [NodeJS](https://console.ng.bluemix.net/catalog/starters/sdk-for-nodejs/) or [Java Runtime](https://console.ng.bluemix.net/catalog/starters/liberty-for-java/)).  

Another possibility is to access a Bluemix Application on your OnPrem Datacenter, but this second use case will not be explored on this guide.

- Read more about Secure Gateway [here](https://console.ng.bluemix.net/docs/services/SecureGateway/secure_gateway.html)

- What is OnPrem? - Read about it [here](https://en.wikipedia.org/wiki/On-premises_software)

#### When to use IBM Secure Gateway?

Here we will explore an IBM Secure Gateway as a way to expose our sample CRM API that will run on your machine(not on Bluemix) and allow our  [BMX-Java-Message-Hub-Consumer](/Lab/Contents/BMX-Java-Message-Hub-Consumer/Readme.md) to send updates commands to the CRM.

#### How Does IBM Secure Gateway work?

To expose an OnPrem Service(our CRM) to Bluemix we will need the following elements:
- A `Gateway` on Bluemix Secure Gateway;
- A `Gateway Client` running on your OnPrem network(eg: your machine);
- A `Destination on Secure Gateway` on Bluemix pointing to your On-Prem Software;
- An `OnPrem Service` running, for this something that listen a TCP or UDP port to serve some content;


#### How IBM Secure Gateway setup works?

The general setup is the following:

1. Create an [IBM Secure Gateway](https://console.ng.bluemix.net/catalog/services/secure-gateway/) on your account;
1. Add an `Gateway` to it;
1. Run an `Gateway Client` on your network to allow your On-Prem network to be exposed to bluemix network;
1. Expose on the `Gateway Client ACL` the service(Host and Port) you want to expose, for details [here](https://console.ng.bluemix.net/docs/services/SecureGateway/sg_010.html#sg_010);
1. Create an `Destination` on your `Gateway` on your IBM Secure Gateay on Bluemix ;
1. (optional) Protect the `Destination` to be accessed by only pre-autorized IPs  - [Details Here](https://console.ng.bluemix.net/docs/services/SecureGateway/sg_023.html#sg_033)
1. (optional) Provide access to your `Destination` to the consumer of your `Destination` endpoint.  - [Details Here](https://console.ng.bluemix.net/docs/services/SecureGateway/sg_023.html#sg_033)


> *Don't worry* the gateway client has an internal firewall, so you will have control on what is exposed. And also, you can protect the Bluemix Secure Gateway `destination` with a set of layers of protections.

### Step by Step Guide to setup Secure Gateway for the CRM Sample

[Secure Gateway Setup Guide](/Lab/Contents/BMX-SecureGateway/Sample-Setup.md).

## Next guide

[BMX-Java-Message-Hub-Consumer](/Lab/Contents/BMX-Java-Message-Hub-Consumer/Readme.md)    
