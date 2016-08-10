# MotorCorpService

This car service center app demonstrates multiple Foundation features coupled with Bluemix services to create an app that works seamlessly for the customer.

**Scenario**: Motor Service Corp. developing an app for Customer Service Reps & Mechanics to improve customer service at service centers

**Adapters**:
Mash up data from CRM, Automotive DB, and Scheduling DB to show mechanic next appointments, customer info, and info about their cars.

**Security**:
Allow easy repetitive access to app with touch ID

**Offline Sync**:
Mechanic can continue to access next work orders, and record work details while working on a car, even if he loses connectivity temporarily

**Connectivity to Backend from the Cloud**:
Uses secure gateway to open a secure communication channel to on-prem CRM

**App Responsiveness**:
Read & Write to CRM with DashDB for Caching, and Message Hub for queueing

**Live Update**:
One app for 2 types of employees - mechanic & Customer service rep. The app has shared functionality between the two (around appointments), and some differences (mechanics file details of their work)

--------------------------------

## Contacts

* Scrum Master: Theodora Cheng, tcheng@us.ibm.com
* Manager: Jim McGarrahan, mcgarr@us.ibm.com
* Offering Manager: Carmel Schindelhaim, carmels@il.ibm.com

--------------------------------

## Development Environment Setup

1. Install mfpdev cli
2. Install [node.js](https://nodejs.org/en/)
2. Install ionic on the command line `$ npm install -g cordova ionic`
3. `git clone` this repository
4. `$ cd MotoCorp` into the app folder
5. `$ ionic platform add ios` add the ios platform
6. `$ ionic build ios` build ios platform
7. `$ ionic emulate ios` emulate the app

--------------------------------

## Pushing Changes

1.  Make sure this meets the definition of done (in task)
2.  Do a code review if necessary
3. `git status` to see what pages needed to be changes
4. `git commit -m "(#1) issue number as well as signifiant message`
5. `git push` to your branch
6.  Do a pull request into master branch when all necessary changes are added
7.  Review task with team to close the issue
