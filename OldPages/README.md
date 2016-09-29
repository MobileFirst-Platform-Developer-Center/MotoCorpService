# MotorCorpService

This car service center app demonstrates multiple Foundation features coupled with Bluemix services to create an app that works seamlessly for the customer.

- **Scenario**: Motor Service Corp. developing an app for Customer Service Reps & Mechanics to improve customer service at service centers
- **Adapters**: Mash up data from CRM, Automotive DB, and Scheduling DB to show mechanic next appointments, customer info, and info about their cars.
- **Security**: Allow easy repetitive access to app with touch ID
- **Offline Sync**: Mechanic can continue to access next work orders, and record work details while working on a car, even if he loses connectivity temporarily
- **Connectivity to Backend from the Cloud**: Uses secure gateway to open a secure communication channel to on-prem CRM
- **App Responsiveness**: Read & Write to CRM with DashDB for Caching, and Message Hub for queueing
- **Live Update**:
One app for 2 types of employees - mechanic & Customer service rep. The app has shared functionality between the two (around appointments), and some differences (mechanics file details of their work)

--------------------------------

## Architecture Diagram

Blue Arrows: MFP Device > Server Calls

Red Arrows: MFP Adapter > Backend Calls

![architecture](https://github.ibm.com/cord-americas/MotoCorpService/blob/pictures/architecture.png)

--------------------------------

## Contacts

* Scrum Master: Theodora Cheng, tcheng@us.ibm.com
* Manager: Jim McGarrahan, mcgarr@us.ibm.com
* Offering Manager: Carmel Schindelhaim, carmels@il.ibm.com
* Developer: Cesar Lourenco Botti Filho, cesarlb@br.ibm.com
* Developer: Rob Puryear, sdpuryea@us.ibm.com
* Developer: Yoel Nunez, ynunez@us.ibm.com

--------------------------------

## Development Environment Setup

1. Install [mfpdev cli](npm install -g mfpdev-cli) `$ npm install -g mfpdev-cli`
2. Install [node.js](https://nodejs.org/en/)
2. Install [ionic](http://ionicframework.com/getting-started/) on the command line `$ npm install -g cordova ionic`
3. `$ git clone git@github.ibm.com:cord-americas/MotoCorpService.git` this repository
4. `$ cd MotoCorp` into the app folder
5. `$ cordova platform add ios` add the ios platform
6. `$ cordova plugin add cordova-plugin-mfp` add the mfp plugin
7. `$ cordova build ios; cordova emulate ios` build ios platform and emulate

--------------------------------

## Pushing Changes

1.  Make sure this meets the definition of done (in task)
2.  Do a code review
3.  Check your branch with `$git branch`
3. `$ git status` to see what files were changes
4. `$ git add` relevant files
4. `$ git commit -m "issue number as well as significant message (i.e. #1 adding jsonstore integration) "`
5. `$ git push` to your branch
6.  Do a pull request into master branch when all necessary changes are added
7.  Review task with team to close the issue

-------------------------------

# Mobile Foundation Code

## /Adapters

The adapters to be used by the Mobile Foundation main app

## /MotoCorp

Ionic App that is the user-interface of the Mobile app that will run on a user device

# Non-foundation

##  /onPremSimulator
On Prem - MotorCorp CRM simulator, a docker container that hosts a nodeJS server to be run on a non-bluemix machine. To allow the demonstration of security gateway.

##  /MotoNode
The nodeJS in development that will be hosted by onPremSimulator. 