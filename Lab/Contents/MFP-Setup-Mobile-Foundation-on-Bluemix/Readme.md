#  Setting Up Foundation on Bluemix

In this tutorial, we will be showing how to 

## What you will learn in this guide

 - How to setup Mobile Foundation on Bluemix
 - How to setup Mobile Foundation CLI and configure it to your Foundation instance
 - How to setup the Ionic CLI


## Requirement of this guide

- No requirements


## Guide

1 - Instantiate [MobileFirst Foundation](https://console.ng.bluemix.net/catalog/services/mobile-foundation/) on Bluemix
2 - Install [Node](https://nodejs.org/en/) 
4 - Install [Ionic CLI](http://ionicframework.com/getting-started/)
```bash
npm install -g cordova ionic@1.X
```
> This project is based on ionic 1.X(to go on details on this - 1.6 or 1.7)
> TODO: to test how to install ionic


3 - Install the MobileFirst CLI
```bash
npm install -g mfpdev-cli
```

5 - Make sure to either by using JVM 1.7 or 1.8. You can check your java version in the CLI with
```bash
java -version
```
6 - Configure you mfpdev-cli to point to your Foundation Bluemix server

```bash
# We tested this at
#test 1
javac 1.7.0_80
mvpdev -v 8.0.0-2016080322
ionic 1.7.15
cordova 6.2.0

# test 2
Java version: 1.8.0_66
ionic 2.1.0
cordova 6.4.0
mfpdev 8.0.0-2016101416

# How to install cloud foundry?
(not required here)

how to install maven?
https://maven.apache.org/



```
   
# Reference
[mfp install](https://mobilefirstplatform.ibmcloud.com/tutorials/en/foundation/8.0/adapters/creating-adapters/#install-maven)

## Next guide

[MFP-Security-Implement-Login](/Lab/Contents/MFP-Security-Implement-Login/Readme.md)    
