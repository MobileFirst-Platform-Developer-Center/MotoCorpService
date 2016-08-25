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

//var mfpServer = "http://localhost:9080";
require.config({
	'paths': {
		'ibmmfpfanalytics': 'node_modules/ibm-mfp-web-sdk/lib/analytics/ibmmfpfanalytics',
		'mfp': 'node_modules/ibm-mfp-web-sdk/ibmmfpf',
		'challengehandler': 'ChallengeHandler'
	}
});

require(['ibmmfpfanalytics','mfp', 'challengehandler'], function(wlanalytics, WL, CH) {
    var wlInitOptions = {
        mfpContextRoot : '/mfp', // "mfp" is the default context root in the MobileFirst Developer Kit
        applicationId : 'com.sample.pincodeweb'
    };

    WL.Client.init(wlInitOptions).then (
        function() {
            document.getElementById("getBalance").addEventListener('click', getBalance);
			CH.init();
    });


    function getBalance() {
        var resourceRequest = new WLResourceRequest("/adapters/ResourceAdapter/balance",WLResourceRequest.GET);

        resourceRequest.send().then(
            function(response) {
                WL.Logger.debug("resourceRequest.send success: " + response.responseText);
                document.getElementById("balanceLabel").innerHTML = response.responseText;
            },
            function(response) {
                WL.Logger.debug("resourceRequest.send success: " + response.errorMsg);
                document.getElementById("balanceLabel").innerHTML = response.errorMsg;
            }
        );
    }
});
