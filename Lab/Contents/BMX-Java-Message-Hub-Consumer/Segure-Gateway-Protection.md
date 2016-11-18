```
- Enter your Secure gateway destination
- Enable protection
- Collect credentials
- Update credentials on your java consumer yml
- Push
> If this was already pushed before, update via bluemix console, to ensure new values are reflected on the app
- Restart the java consumer
-
```



##### Edit a Destination
> - Enter in your gateway on the Service Dashboard.
> - Click on Add a destination
> - Select "On-Premesis" -> This will create a destination from a bluemix endpoint to a service on your OnPrem network.
> - Press Next
> - Provide the Resource Hostname and Port of the Resource you want to expose to bluemix.
> - Select the protocol **TCP** and press Next
> - On **What kind of authentication does your destination enforce?** select **None** and then press next
> - On **If you would like to make your destination private, add IP table rules below** you don't have fill this values for now, just press next.  
> - On **"What would you like to name this destination?"** provide a name for your destination, eg: "My OnPrem Server". Then press "finish"



##### Protecting your resource destination on Secure Gateway
> On your gateway page, where you have the list of destinations.
> Click on the gateway name, the grey big box.
> Expand the "advanced" area.
> Check the box at **"Restrict network access to cloud destination"**
> Now you gateway will only allow access for the incoming IPs and Ports allowed in the list.

![Instructions](/Lab/img/SGW_3_Step1_EnableProtectedEndpoint.gif)


> :memo: Check our [Java Message Hub Consumer](/Lab/Contents/BMX-Java-Message-Hub-Consumer/Readme.md) code, it has a special Java code to allow it to add itself on the gateway whitelist. As described [here](https://console.ng.bluemix.net/docs/services/SecureGateway/sg_023.html#sg_033).


#### Learning more about cloudfoundry apps and secure gateway IP Tables.

> :books: To learn more about this access [this link](https://console.ng.bluemix.net/docs/services/SecureGateway/sg_023.html#sg_033)

#### Troubleshooting Secure Gateway

##### How to clean you IP Tables
If for some reason you want to wipe out the list of IP/Ports from the Secure Gateway restricted list to prevent all servers to access your endpoint you can run the following command:

```bash
curl -X DELETE "https://sgmanager.ng.bluemix.net/v1/sgconfig/$GATEWAY_ID/destinations/$Ddestination_id/ipTableRule"    -H "Authorization: Bearer $SECURE_TOKEN" -H "Content-type:  application/json" -d     '{"all":true}'  -k


#For exmaple
curl -X DELETE "https://sgmanager.ng.bluemix.net/v1/sgconfig/mO6CsJObK7T_prod_ng/destinations/mO6CsJObK7T_rnS/ipTableRule"    -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9**longtoken**DnRkAed-m8x7xbiTxs6wGTgzeXL2Gv-sKzCov5Fcr5U" -H "Content-type:  application/json" -d     '{"all":true}'  -k
```

> By invoking a **DELETE** call to "https://sgmanager.ng.bluemix.net/v1/sgconfig/$GATEWAY_ID/destinations/$Ddestination_id/ipTableRule" using your Secure Token as the Bearer token with  the payload **'{"all":true}'** you will ask the selected destination to wipe its list of ips.
> To information used here it is the same one used on step 3, deploying your SecureGatewayAdapter.
