#  Mobile Foundation - DashDB Adapter



## What you will learn on this guide

 - How to create a DashDB Service on Bluemix
 - How to create a DashDB adapter for Mobile Foundation
 - How to test this adapter

## Requirement of this guide

- [Mobile Foundation Setup](/Lab/Contents/MFP-Setup-Mobile-Foundation-on-Bluemix/Readme.md)


## Guide

```
4. DashDB Adapter (read cust/visit info)
    1. create DashDB Services
    2. Create tables (sql script) - TODO
    3. Populate dummy data (sql script) - TODO
    4. deploy adapter
    5. go over code snippets in dashdb adapter
    6. How to test that it works
```

*notes*
```
# DashDB Bluemix
- create the instance of DashDB(screenshot)_
- get credentials of DashDB service(screnshot)
- run a SQL to Script to Create the database tables(Customers and Vists)
> No initial data

# DashDB Adpater
- go to  the adapter folder 
- go to adapter.xml and update the default properties. (screenshots)
> provide a link on the docs how to do this via foundation console. 
- mfpdev adapter build 
- mfpdev adapter deploy or deploy manually (screenshots)
> you can only deploy manually due to a limitation of the size of the adpater. 

- how to test if the adapter deploy(swagger test - screenshot or video)
(we assume we can test)

- Swagger tests -> sample endpoint and sample result screen(200 - no results)

```



## Next guide

[NodeJS-CRM-OnPrem](/Lab/Contents/NodeJS-CRM-OnPrem/Readme.md)   
