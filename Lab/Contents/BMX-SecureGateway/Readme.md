#  Bluemix - Setup of Secure Gateway Tunnel

```
6. SecureGateway (for message hub consumer)
    1. Create SecureGateway instance on blue mix
    2. set up secure gateway
```


## What you will learn on this guide

 - `What is secure gateway?` - ok
 - `When to use secure gateway?` - ok
 - `How to create a gateway?`
 - `Why do you need a gateway client On-Prem?`
 - How to run a Security Gateway Client On-Prem
 - `What is a Secure Gateway Endpoint on Bluemix?`
 - How to setup an Secure Gateway Endpoint on Bluemix
 - How to protect the Secure Gateway Tunnel between Bluemix and On-Prem -> Link
 - How to test the Tunnel

## Requirement of this guide

- [On Prem NodeJS CRM](/Lab/Contents/NodeJS-CRM-OnPrem/Readme.md)


## Guide

### Secure Gateway Overview

#### What is IBM Secure Gateway?

It is a service available on bluemix that allows you to access On-Prem Services of your data center(eg: an web service) on a IBM Bluemix Application(eg: [NodeJS](https://console.ng.bluemix.net/catalog/starters/sdk-for-nodejs/) or [Java Runtime](https://console.ng.bluemix.net/catalog/starters/liberty-for-java/)).  

Another possibility is to access a Bluemix Application on your On-Prem Daatacenter, but this second use case will not be explored on this guide. 

> [read more here](https://console.ng.bluemix.net/docs/services/SecureGateway/secure_gateway.html)
> [what is On-Prem?](https://en.wikipedia.org/wiki/On-premises_software)

#### When to use IBM Secure Gateway?

On this guide we will explore an IBM Secure Gateway as an way to expose our sample CRM API that will run on your machine(not on bluemix) and allow our  [BMX-Java-Message-Hub-Consumer](/Lab/Contents/BMX-Java-Message-Hub-Consumer/Readme.md) to send updates commands to the CRM.

#### How IBM Secure Gateway works? 

To expose an On-Prem Service(our CRM) to Bluemix we will need the following elements:
- An `Gateway` on Bluemix Secure Gateway;
- An `Gateway Client` running on your On-Prem network ;
- An `Endpoint on Secure Gateway` on Bluemix pointing to your On-Prem Software;
- An `On-Prem Service` running, for this something that listen a TCP or UDP port to serve some content;


The general setup is the following:

1. Create an [IBM Secure Gateway](https://console.ng.bluemix.net/catalog/services/secure-gateway/) on your account;
2. Add an `Gateway` on it;
3. Run an `Gateway Client` on your network to allow your On-Prem network to be exposed to bluemix network.

> *Don't Panic!:*  The gateway client has an internal firewall, so you will have control on what is exposed. 

4. 

After integrating your ui with your backend and applying mobile security, time to tackle the next hurdle - firewalls!
Many of your users will not be on the same network as your sensitive customer information (i.e. on-prem CRM) and you do not want to open up those resources for anyone on the Internet to be able to access.

IBM Bluemix Secure Gateway service allows you to securely and easily tunnel through these firewalls so that your end users can access the resources they need.

This lab will cover how can you explore IBM Bluemix Secure Gateway service to call an OnPrem service from your Bluemix application.

#### 1. Scenario explored on this sample

![Demo Map](/Lab/img/SGW_MAP.png)
> Remove Map

In this Lab we will connect to a CRM Mockup(See: :pushpin:  **Link to CRM Setup** ) that is running in a nodeJS server that is not in bluemix environment and not exposed to the web.

To allow our Mobile Foundation server on bluemix to reach the REST API of our CRM Mockup from bluemix app we will use Secure Gateway to bridge the OnPrem Server and the Bluemix App, by:

1 - Exposing CRM REST API to the web(all internet users - public access)

2 - To protect the CRM API to be only visible by the Mobile Foundation Runtime running on Bluemix (restricted access)



> :bulb: **TIP:**  This could be used when you have a backend system that is not available at the internet and you need to access it from a bluemix service.


> :bulb: **TIP:**  This could also  be used when you are developing an app and want expose your local test version to another developer or team via bluemix or via internet.

#### 2. Creating a gateway to allow cloud to reach on OnPrem data

> **Before Proceding:**   This tutorial will require you have a service to be exposed in a tcp port, then expose it via Secure Gateway Bluemix service. To help you with that, we provide the CRM Mockup as a sample of service to be exposed.


To create a gateway and expose a destination through it the steps are:

##### 2.1 - Add Secure Gateway on bluemix account

> In your bluemix account, add a new service and select **"Secure Gateway"** and fill with default values and press create.

![Instructions](/Lab/img/SGW_Setup01.gif)

### 2.2 - Creating an Gateway

> Once you have a Secure Gateway service on your account let's add an gateway:

>  - Probably this is your first gateway, then just press the button "+ Add Gateway" on the service dashboard.

>  - You can use the default properties or customize as desired, for now let's keep default values.

>  - Press "Add Gateway"

![Instructions](/Lab/img/SGW_Setup02-CreateGateway.gif)

> :memo: **What is a gateway?:**  
> The gateway is a collection of destinations, it has secure token that allows it to be customized by the users.

##### 2.3 - Running the Secure Gateway Docker Client

>  - On the top right of the secure gateway dashboard, press on "add client".
>  - Select docker
>  - press on the clipboard icon
>  - past on your terminal command line and press run.

&nbsp;
>**Observation:**   To this to run properly you need to have docker installed and ready to run on your machine.

![Instructions](/Lab/img/SGW_Setup03-AddingClient.gif)

&nbsp;
> :memo: **Why do I need the client?**
>
>  Secure Gateway is a unidirectional bridge between 2 networks. To allow it to work properly you need a client that is in an "OnPrem" network to allow the Bluemix environment to reach it. So, for this example we will have a endpoint on bluemix that will receive the requests and tunnel it to the Secure Gateway client tp call the resource in the OnPrem network.

  ![Demo Map](/Lab/img/SGW_Client.png)
> The Secure Gateway client and the service/resource you want to reach in the OnPrem network do not need to be on the same machine, as long as the client machine can reach the service desired, it will do the proper routing to it.

##### 2.4 - Creating a Destination
> - Enter in your gateway on the Service Dashboard.
> - Click on Add a destination
> - Select "On-Premesis" -> This will create a destination from a bluemix endpoint to a service on your OnPrem netowrk.
> - Press Next
> - Provide the Resource Hostname and Port of the Resource you want to expose to bluemix.
> - Select the protocol **TCP** and press Next
> - On **What kind of authentication does your destination enforce?** select **None** and then press next
> - On **If you would like to make your destination private, add IP table rules below** you don't have fill this values for now, just press next.  
> - On **"What would you like to name this destination?"** provide a name for your destination, eg: "My OnPrem Server". Then press "finish"

> **Note:** Resource Hostname is the IP or Hostname which the Secure Gateway client will use to reach the resource you want to expose. In this Lab it is the CRM Mockup host IP(probably your machine ip) and the port is the port your nodeJS is exposing.

&nbsp;
![Instructions](/Lab/img/SGW_Setup04-AddingDestination.gif)

> :memo: **What is a destination?**
>
> It is a endpoint on bluemix that will receive the calls from the cloud-side(Bluemix) and tunnel it in to your OnPrem resource, in this example, the CRM Mockup.




##### 2.5 - Testing your OnPrem Service(optional)

Execute the curl below to test your OnPrem resource, to ensure there is something been responded when the tunnel works.
  ```bash
  curl <Resource Hostname>:<port>
  #For Example: (this is the resource ip on the OnPrem network)
  curl 192.168.99.100:49160
  ```


##### 2.6 - Defining the ACL List

Once you start your client endpoint, as we did on the **step 3** you will see a console open, on this console, if you press "S" you will see a list of rules(your ACL list).
> On the Secure Gateway Console, type:
>
> **A IP:PORT**
>
> or
>
> **A HOSTNAME:PORT**
>
> eg: A 192.168.99.100:49160
>
> This IP/PORT it is the address of your resource on the OnPrem Network, as we tested on **Step 5**
> For help on the console press "help" and a list of commands will be presented to you.

![Instructions](/Lab/img/SGW_Setup04-AddingDestination.gif)


> :memo: **What is the ACL List?**
>
> The ACL it is a local firewall on the client side of the tunnel, in this case the OnPrem side, that allows you to create rules to allow or block some server addresses and ports.
&nbsp;

> :memo: **How do pre-load it on my docker container?**
>
> For that you can start the client container sharing a folder with your host machine that contains an ACL file or create your own container that extends the secure gateway client one, adding your ACL file during its build process.  
> Sample acl.txt content:
```txt
acl allow 192.168.99.100:49160
```

```bash
#then to allow a server to be reach

#full version - all flags
docker run -it -v /ACLList/Folder/OnHost/:/var/settings/  --name sgw_client ibmcom/secure-gateway-client $GATEWAY_ID --sectoken eyJhbGciOiJIUz--along-token---sKzCov5Fcr5U --F /var/settings/acl.txt
```


Congratulations we now have a OnPrem Service exposed to the web, allowing it to also be reached by Bluemix Applications. In the next topic we will cover how to to restrict the access to this service, for just bluemix apps.


#### 3. Restricting the access to the gateway - MFP on bluemix


##### 3.1 - Protecting your resource destination on Secure Gateway
> On your gateway page, where you have the list of destinations.
> Click on the gateway name, the grey big box.
> Expand the "advanced" area.
> Check the box at **"Restrict network access to cloud destination"**
> Now you gateway will only allow access for the incoming IPs and Ports allowed in the list.

![Instructions](/Lab/img/SGW_3_Step1_EnableProtectedEndpoint.gif)


> :memo: Link to the Consumer guide to setup protection.

### 3.4 - Learning more about cloudfoundry apps and secure gateway IP Tables.

> :books: To learn more about this access [this link](https://console.ng.bluemix.net/docs/services/SecureGateway/sg_023.html#sg_033)

## 4. Troubleshooting Secure Gateway

### How to clean you IP Tables
If for some reason you want to wipe out the list of IP/Ports from the Secure Gateway restricted list to prevent all servers to access your endpoint you can run the following command:

```bash
curl -X DELETE "https://sgmanager.ng.bluemix.net/v1/sgconfig/$GATEWAY_ID/destinations/$Ddestination_id/ipTableRule"    -H "Authorization: Bearer $SECURE_TOKEN" -H "Content-type:  application/json" -d     '{"all":true}'  -k


#For exmaple
curl -X DELETE "https://sgmanager.ng.bluemix.net/v1/sgconfig/mO6CsJObK7T_prod_ng/destinations/mO6CsJObK7T_rnS/ipTableRule"    -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9**longtoken**DnRkAed-m8x7xbiTxs6wGTgzeXL2Gv-sKzCov5Fcr5U" -H "Content-type:  application/json" -d     '{"all":true}'  -k
```

> By invoking a **DELETE** call to "https://sgmanager.ng.bluemix.net/v1/sgconfig/$GATEWAY_ID/destinations/$Ddestination_id/ipTableRule" using your Secure Token as the Bearer token with  the payload **'{"all":true}'** you will ask the selected destination to wipe its list of ips.
> To information used here it is the same one used on step 3, deploying your SecureGatewayAdapter.



## Acknowledgement:
 Some of the icons used on this page are Designed by Freepik and distributed by Flaticon.




# Extra References:

https://console.ng.bluemix.net/docs/services/SecureGateway/sg_023.html#sg_023

https://developer.ibm.com/bluemix/2015/03/27/bluemix-secure-gateway-yes-can-get/

http://www.ibm.com/developerworks/library/se-connect-data-center-with-bluemix-secure-gateway-service/index.html

https://docs.cloudfoundry.org/running/managing-cf/logging.html

https://developer.ibm.com/bluemix/2015/11/11/secure-gateway-in-production-part1/

https://developer.ibm.com/bluemix/2015/11/18/secure-gateway-in-production-part2/

https://developer.ibm.com/bluemix/2015/04/17/securing-destinations-tls-bluemix-secure-gateway/

https://developer.ibm.com/bluemix/2015/04/07/reaching-enterprise-backend-bluemix-secure-gateway-via-sdk-api/

https://developer.ibm.com/bluemix/2015/04/01/reaching-enterprise-backend-bluemix-secure-gateway/





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
