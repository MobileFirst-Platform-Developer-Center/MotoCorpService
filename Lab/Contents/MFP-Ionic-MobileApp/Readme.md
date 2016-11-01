#  Mobile Foundation - Ioinic Mobile App



## What you will learn on this guide

 - Understand key points on Integration of Mobile Foundation on a Ionic Mobile App
 - Understand how to use the Login Security Check
 - Understand how to call and adapter from your MobileApp Ionic
 - How to deploy the App on this Guide
 - How to test the Mobile App part of this tutorial

## Requirement of this guide

- [Login Security Test](/Lab/Contents/MFP-Security-Implement-Login/Readme.md)  
- [Customer Adapter](/Lab/Contents/MFP-Customer-Adapter/Readme.md)


## Guide

```
9. UI Integration (backend/frontent)
    1. show CRM service in ionic app (code snippets)
    2. show how to call that within the controllers (code snippets)
    3. show how it binds to the pages (code snippets)
    4. run the app
        1. create customer
        2. search customer
        3. create visit
        4. customer details -> visit
```

Notes:
```bash
Go to the MobileApp folder
cd MotoCorpService/MotoCorp/
- Add the ios plaform
ionic platform add ios
- Add the MobileFirst Plugin
cordova plugin add cordova-plugin-mfp

- point you default server to bluemix
- if the server is not default , register must point to give server using 'mybluemixmfp'


6 - Register the application
mfpdev app register mybluemixmfp

7 - Build the ios platform. You will need to do this every time you make a change in the app.
# ionic build ios -> not sure if works
Do this instead: 
 - cordova build 
 - cordova emulate

Simulate the app: 
- Login in:
- user and password: "demo"/"demo"
- create a new customer
- search with no filter and no text - you will see the new customer
- can also add a customer visit to a customer and see result

```


## Next guide

[Bluemix-Mobile-Analytics](/Lab/Contents/Bluemix-Mobile-Analytics/Readme.md)  
