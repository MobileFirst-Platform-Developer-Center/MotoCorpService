/**
* Copyright 2016 IBM Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

define(['mfp'], function(WL) {

    function init() {
        var PinCodeChallengeHandler = WL.Client.createSecurityCheckChallengeHandler("SecureGatewayBinderSecurityCheck");

        PinCodeChallengeHandler.handleChallenge = function(challenge) {
            var msg = "";

            // Create the title string for the prompt
            if(challenge.errorMsg !== null){
                msg =  challenge.errorMsg + "\n";
            }
            else{
                msg = "This data requires a PIN code.\n";
            }
            msg += "Remaining attempts: " + challenge.remainingAttempts;

            // Display a prompt for user to enter the pin code
            var pinCode = prompt(msg, "");


            if(pinCode){ // calling submitChallengeAnswer with the entered value
                PinCodeChallengeHandler.submitChallengeAnswer({"pin":pinCode});
            }
            else{ // calling cancel in case user pressed the cancel button
                PinCodeChallengeHandler.cancel();
            }
        };

        // handleFailure
        PinCodeChallengeHandler.handleFailure = function(error) {
            WL.Logger.debug("Challenge Handler Failure!");
            if(error.failure !== null && error.failure !== undefined){
               alert(error.failure);
            }
            else {
               alert("Unknown error");
            }
        };
    }

    return {
        init: init
    };
});
