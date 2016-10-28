#  Mobile Foundation - MessageHub Adapter


## What you will learn on this guide

- How to create a Message Hub Service on Bluemix
- How to create a Message Hub adapter for Mobile Foundation
- How to test this adapter

## Requirement of this guide

- [Mobile Foundation Setup](/Lab/Contents/MFP-Setup-Mobile-Foundation-on-Bluemix/Readme.md)


## Guide

```
3. MessageHub Adapter
    1. Create MessageHub Service
    2. Java Adapter Producer (customer + visitor create)
    3. how it works (code snippets)
    4. How to test that it works (check in liberty logs or for not 500)
```

Testing adapter:
```
curl -X POST --header 'Content-Type: application/json' --header 'Accept: */*' -d '{
 "Name": "Jane Doe 3",
 "LicensePlate": "ABC4321",
 "Make": "Honda",
 "Model": "Accord",
 "VIN": "ZKSNKUS89SKJS"
}' 'https://dorasmobile-1-cw-server.mybluemix.net/mfp/api/adapters/MessageHubAdapter/resource/newCustomer'


```



## Next guide


[MFP-DashDB-Adapter](/Lab/Contents/MFP-DashDB-Adapter/Readme.md)  
