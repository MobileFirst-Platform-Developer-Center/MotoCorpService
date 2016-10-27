
# Running sample:


Test server: mobiledev-bo-server.mybluemix.net(MobileDev)
```bash
git clone git@github.ibm.com:cord-americas/MotoCorpService.git
# Clone repository:
cd MotoCorpService/
cd Adapters/
cd CustomerAdapter/
mfpdev adapter build
mfpdev adapter deploy
#got the un expected response.
cd ..
cd DashDBAdapter/
mfpdev adapter build
mfpdev adapter deploy
# Error deploying: [ERROR] Failed to execute goal com.ibm.mfp:adapter-maven-plugin:8.0.0:deploy (default-cli) on project DashDB: Error accessing https://mobiledev-bo-server.mybluemix.net:443/mfpadmin/management-apis/2.0/runtimes/mfp/adapters: HTTP/1.1 500 Error. Check properties details in pom.xml -> [Help 1]
# this adapter deploy only works via console.
cd ..
cd MessageHubAdapter/
mfpdev adapter build
mfpdev adapter deploy
#[ERROR] Failed to execute goal com.ibm.mfp:adapter-maven-plugin:8.0.0:deploy (default-cli) on project MessageHubAdapter: Error accessing https://mobiledev-bo-server.mybluemix.net:443/mfpadmin/management-apis/2.0/runtimes/mfp/adapters: HTTP/1.1 500 Error. Check properties details in pom.xml -> [Help 1]
# this adapter deploy only works via console.
```

Testing MessageHubAdapter: (/resource/newCustomer)

```js
{
   "LicensePlate": "ABC-123",
   "Model": "Accord",
   "Make": "2015",
   "VIN": "21312411",
   "Name": "Dora Cheng"
 }
```

```bash
cd ..
cd SecureGatewayAdapter/
mfpdev adapter build
mfpdev adapter deploy
#Deploy worked as expected

cd ..
cd UserLogin
mfpdev adapter build
mfpdev adapter deploy
#Deploy worked as expected

```

```bash
cd ../..
cd MotoCorp/
cordova plugin add cordova-plugin-mfp
ionic platform add ios
mfpdev app register
ionic build ios
ionic emulate ios
#App fails to login
#It seems to be a issue on MFP due to cli issue that seems to be already solved by PMR
# 8.0.0.0-IF2016082221 -> Testing
```


Debugging:
```
open ./platforms/ios/HelloCordova.xcodeproj
#manual play

cordova emulate ios


mfpdev app preview ios -type mbs

```

Error:
```
Uncaught Exception: Error: [$injector:modulerr] Failed to instantiate module ng due to:
this is null
$$SanitizeUriProvider@http://localhost:10081/ios/www/lib/ionic/js/ionic.bundle.js:30845:3
```

Testing:
```
npm install npm@3.9.x
sudo npm install mfpdev-cli -g
sudo npm update mfpdev-cli -g
#http://stackoverflow.com/questions/39823260/error-installing-mfpdev-cli


npm install --cache ./.cache mfpdev-cli.tgz -g

npm install


#to solve the issue to install
sudo npm uninstall -g mfpdev-cli
sudo npm install mfpdev-cli -g
# new command
 mfpdev adapter build all

ionic platform remove ios
ionic platform add ios
cordova plugin remove cordova-plugin-mfp
 cordova plugin add cordova-plugin-mfp

 mfpdev app register
 ionic build ios
 ionic emulate ios
mfpdev app preview ios -type mbs
open ./platforms/ios/HelloCordova.xcodeproj
 cordova plugin list
 ionic build ios
```

http://stackoverflow.com/questions/38902169/mobilefirst-v8-and-bootstrap-angular-js-v1-5-3-error


## To Dos:
- to fix "CustomerAdapter" pom.xml
- To document that server, requires 3GB of memory, not sure why?
-

Checklist:
- Mobile App: Yes
- Login Adapter: No review required
- CustomerAdapter : New guide
- MessageHub Adapter: New Guide
  - Setup MessageHub Guide: New Guide
- How to consume a topic on message with NodeJS
- SecureGateway:(Update - Review)
  - How to setup the gateway
  - how protect the tunnel (nodeJS - optional)
  - How to open the gateway client

- (nodeJS) How to run our Mockup CRM: New guide
  - (nodeJS) how to send data to DashDB: New Guide
- How to setup DashDB
  - how to create the tables
  - how to provision on bluemix
-
