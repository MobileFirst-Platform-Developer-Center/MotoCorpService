#  Bluemix - Setup of Secure Gateway Tunnel

```
6. SecureGateway (for message hub consumer)
    1. Create SecureGateway instance on blue mix
    2. set up secure gateway
```


## What you will learn on this guide

 - How to setup Secure Gateway Endpoint on Bluemix
 - How to Setup a Security Gateway Client On-Prem
 - How to protect the Secure Gateway Tunnel
 - How to test the Tunnel

## Requirement of this guide

- [On Prem NodeJS CRM](/Lab/Contents/NodeJS-CRM-OnPrem/Readme.md)


## Guide

```
6. SecureGateway (for message hub consumer)
    1. Create SecureGateway instance on blue mix
    2. set up secure gateway
```

Notes:
```
- check you have docker
- setup docker
- instatiate the service SGW on bluemix
> you can only have one per account
- setup a new gateway
- run docker SGW client locally
- setup local ACL list(add your CRM local IP)
- setup an endpoint (destination)
> point the destination to your CRM nodeJs
> ensure protection is disabled(screenshot)
- test secure gateway
```



## Next guide

[BMX-Java-Message-Hub-Consumer](/Lab/Contents/BMX-Java-Message-Hub-Consumer/Readme.md)    
