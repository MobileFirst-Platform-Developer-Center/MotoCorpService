## Overview
This car service center app demonstrates multiple Foundation features coupled with Bluemix services to create an app that works seamlessly for the customer.

> Latest [Release](https://github.ibm.com/cord-americas/MotoCorpService/releases)

## Architecture Diagram
![Iteration 2](https://github.ibm.com/cord-americas/MotoCorpService/blob/pictures/iter2.png)

## CORD Americas Unbreakable Team:

| Role | Name | Email | 
| -- | -- | -- | 
| Offering Manager | Carmel Schindelhaim | carmels@il.ibm.com |
| Manager | Jim McGarrahan | mcgarr@us.ibm.com |
| Scrum Master: Theodora Cheng | tcheng@us.ibm.com |
| Developer | Cesar Lourenco Botti Filho | cesarlb@br.ibm.com |
| Developer | Rob Puryear | sdpuryea@us.ibm.com |
| Developer | Yoel Nunez | ynunez@us.ibm.com |

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