

## How to Enable Java Message Hub Consumer to Access a Protected Secure Gateway

This is a continuation of the [Secure Gateway Setup Guide](Lab/Contents/BMX-SecureGateway/Sample-Setup.md).

### What you will learn on this guide

- How to protect your secure gateway `destination`;
- How to configure your `java message hub consumer` to grant itself access to your destination automatically;
- How to clean your destination firewall table;

### Protecting your destination

- Go to your `gateway` dashboard at your secure gateway service and press on the gray area with your destination title;

  ![Instructions](/Lab/Contents/BMX-SecureGateway/img/SGW_21.png)

- Mark the check box `Restrict cloud access to this destination with iptable rules` and press `UPDATE DESTINATION`;

  ![Instructions](/Lab/Contents/BMX-SecureGateway/img/SGW_22.png)

After this point you will not be able to reach your gateway from your browser, the only machines able to reach it will be the ones with the IP and PORT on the iptables list;


### Updating your java message hub consumer properties

Your Message Hub Java Consumer has a yml file with all the properties need to setup your firewall access, they are part of the property `SGW_CONFIG`. [Manifest File](/MessageHubConsumer/manifest.yml)

Sample:
```
SGW_CONFIG: '{
                "destination-id":"${YOUR_SECURE_GATEWAY_DESTINATION_ID}",
                "api-host":"${YOUR_SECURE_GATEWAY_HOST_URL}",
                "gateway-id":"${YOUR_SECURE_GATEWAY_ID}",
                "sgw-token":"${YOUR_SECURE_GATEWAY_SECURE_TOKEN}"
             }'
```
> :warning: Observation: this file is very sensitive to tabs and space, if you are having problems when running the Message Hub Consumer ensure you have the same file layout as provided on this sample.

#### How to collected the required properties

`${YOUR_SECURE_GATEWAY_HOST_URL}` - Check table below, to know what address to use accordingly your `region`.

| Region         | Value                               |
|:---------------|:------------------------------------|
| US South       | https://sgmanager.ng.bluemix.net    |
| Sydney         | https://sgmanager.eu-gb.bluemix.net |
| United Kingdom | http://sgmanager.au-syd.bluemix.net |

`${YOUR_SECURE_GATEWAY_ID}` - To get this property access your `gateway` dashboard and press the `engine icon` in the top left, value of the `gateway ID` will be on the popup opened.

  ![Instructions](/Lab/Contents/BMX-SecureGateway/img/SGW_19.png)

  ![Instructions](/Lab/Contents/BMX-SecureGateway/img/SGW_20_2.png)

`${YOUR_SECURE_GATEWAY_SECURE_TOKEN}` -At the same popup you have the gateway ID, you also have your `secure token` on the line marked by the key icon, just press the clipboard icon to copy it.

  ![Instructions](/Lab/Contents/BMX-SecureGateway/img/SGW_20.png)

`${YOUR_SECURE_GATEWAY_DESTINATION_ID}` - To get the destination use go back to you `gateway` dashboard and press over the engine icon of your destination. Then, collect the property at the `destination id` field.

  ![Instructions](/Lab/Contents/BMX-SecureGateway/img/SGW_16_2.png)

  ![Instructions](/Lab/Contents/BMX-SecureGateway/img/SGW_17_2.png)


Once you collect all you can update your `manifest.yml`, it would be something like this:

```json
SGW_CONFIG: '{
                "destination-id":"7XdvxXADqW4_PLC",
                "api-host":"https://sgmanager.ng.bluemix.net",
                "gateway-id":"7XdvxXADqW4_prod_ng",
                "sgw-token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb25maWd1cmF0aW9uX2lkIjoiN1hkdnhYQURxVzRfcHJvZF9uZyIsInJlZ2lvbiI6InVzLXNvdXRoIiwiaWF0IjoxNDc5NDAyNzA4LCJleHAiOjE0ODcxNzg3MDh9.HWCLA-RbJvq3MT0dg-7IHiaA1fdEjXsPZaR9vdVVSKA"
             }'

```

#### Update you message hub properties

No that you have your connection properties for you secure gateway API, we can update the [Manifest File](/MessageHubConsumer/manifest.yml) on your Java Message Hub Consumer.

Just need to replace the place holder variables by your `SGW_CONFIG` properties.

 After data you can do a `cf push` to run your Message Hub Consumer on bluemix or just update your properties via your bluemix console.

If all worked fine, after java app starts to run on your `gateway destination` at table of the IPs you will see with a new IP that was set by the Java Message Consumer Consumer App.

  ![Instructions](/Lab/Contents/BMX-SecureGateway/img/SGW_23.png)

> :warning: If you already deployed your message hub consumer previously on your account, following pushes will only update the WAR file, so to update  the  `SGW_CONFIG` you will need to go to your bluemix console at the app properties and update this runtime variable and restart your app.


> :memo: Check our [Java Message Hub Consumer](/Lab/Contents/BMX-Java-Message-Hub-Consumer/Readme.md) code, it has a special Java code to allow it to add itself on the gateway whitelist. As described [here](https://console.ng.bluemix.net/docs/services/SecureGateway/sg_023.html#sg_033).


#### Learning more about cloudfoundry apps and secure gateway IP Tables.

> :books: To learn more about this access:
> <https://console.ng.bluemix.net/docs/services/SecureGateway/sg_023.html#sg_033>
> <https://console.ng.bluemix.net/apidocs/25?&language=node#introduction>

#### Troubleshooting Secure Gateway

##### How to clean you IP Tables
If for some reason you want to wipe out the list of IP/Ports from the Secure Gateway restricted list to prevent all servers to access your endpoint you can run the following command:

```bash
curl -X DELETE "https://sgmanager.ng.bluemix.net/v1/sgconfig/$GATEWAY_ID/destinations/$Ddestination_id/ipTableRule?all=true""    -H "Authorization: Bearer $SECURE_TOKEN" -H "Content-type:  application/json" -d     '{"all":true}'  -k


#For exmaple
curl -X DELETE "https://sgmanager.ng.bluemix.net/v1/sgconfig/7XdvxXADqW4_prod_ng/destinations/7XdvxXADqW4_PLC/ipTableRule?all=true"    -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb25maWd1cmF0aW9uX2lkIjoiN1hkdnhYQURxVzRfcHJvZF9uZyIsInJlZ2lvbiI6InVzLXNvdXRoIiwiaWF0IjoxNDc5NDAyNzA4LCJleHAiOjE0ODcxNzg3MDh9.HWCLA-RbJvq3MT0dg-7IHiaA1fdEjXsPZaR9vdVVSKA" -H "Content-type:  application/json" -d     '{"all":true}'  -k
```

> By invoking a **DELETE** call to "https://sgmanager.ng.bluemix.net/v1/sgconfig/$GATEWAY_ID/destinations/$Ddestination_id/ipTableRule" using your Secure Token as the Bearer token with  the payload **'{"all":true}'** you will ask the selected destination to wipe its list of ips.
> To information used here it is the same one used on step 3, deploying your SecureGatewayAdapter.




## References

<https://console.ng.bluemix.net/apidocs/25?&language=node#introduction>

## Next guide

[MFP-Customer-Adapter](/Lab/Contents/MFP-Customer-Adapter/Readme.md)
