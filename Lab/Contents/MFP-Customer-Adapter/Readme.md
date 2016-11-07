#  Customer Adapter

> TODO: Show turning off scope
> TODO: Add security to customer adapter and clean up comments, etc

The purpose of the customer adapter is to call the MessageHub and DashDB adapter. This is so that the 

## What you will learn in this guide

 - How to create a adapter that calls other adapters

> To learn more please see our getting started labs [here](https://mobilefirstplatform.ibmcloud.com/tutorials/en/foundation/8.0/adapters/advanced-adapter-usage-mashup/)

## Requirements of this guide

- [MessageHub Adapter](/Lab/Contents/MFP-MessageHub-Adapter/Readme.md)
- [DashDB Adapter](/Lab/Contents/MFP-DashDB-Adapter/Readme.md)
- [Login Security Test](/Lab/Contents/MFP-Security-Implement-Login/Readme.md)  

## Guide

### Deploying the Customer Adapter

1 - Go to where the adapter is located
```bash
cd MotoCorpService/Adapters/CustomerAdapter
```

2 - Build the adapter
```bash
mfpdev adapter build
```

3 - Deploy the adapter by uploading the `CustomerAdapter.adapter` file to the mfp console.

### Customer Adapter - How it Works

1 - Security

```java
@OAuthSecurity(scope = "user-restricted")
```

2 - Posting to MessagHub adapter
Post to the url of the procedure you are calling and execute it using the AdaptersAPI
```java
public Response newCustomer(JSONObject customer) throws IOException{

	String MessageHubURL = "/MessageHubAdapter/resource/newCustomer";
	HttpPost req = new HttpPost(MessageHubURL);
	req.addHeader("Content-Type", "application/json");

	String payload = customer.toString();
	HttpEntity entity = new ByteArrayEntity(payload.getBytes("UTF-8"));
    req.setEntity(entity);

	HttpResponse response = adaptersAPI.executeAdapterRequest(req);
	JSONObject jsonObj = adaptersAPI.getResponseAsJSON(response);

	return Response.ok(jsonObj).build();
}
```

3 - Getting from DashDB adapter
Get to the url of the procedure you are calling and execute it using the AdaptersAPI
```java
public Response customers()  throws IOException{

	String JavaSQLURL = "/DashDB/getAllUsers";
	HttpUriRequest req = new HttpGet(JavaSQLURL);
	HttpResponse response = adaptersAPI.executeAdapterRequest(req);
	JSONObject jsonObj = adaptersAPI.getResponseAsJSON(response);

	return Response.ok(jsonObj.get("responseText")).build();
}
```

### Testing the Customer Adapter

To test the customer adapter, you can go into Swagger and turn off the security and use username "test" and password "test" from the confidential client you create in the security login lab.

1 - How to turn off scope using confidential client
> TODO: Gif here

2 - Testing with MessageHub
You should see a 200 response status showing that you posted a new customer or visit to the MessageHub adapter.

> TODO: Screenshot swagger here

3 - Testing with DashDB
You should be able to see any customer or visit records (if there are any) in the response.

> TODO: Screenshot swagger here

## Next guide

[MFP-Ionic-MobileApp](/Lab/Contents/MFP-Ionic-MobileApp/Readme.md)
