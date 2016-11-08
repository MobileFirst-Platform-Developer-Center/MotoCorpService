WASLtpaRealmChallengeHandler = {};
WASLtpaRealmChallengeHandler = WL.Client.createChallengeHandler("WASLTPARealm");

WASLtpaRealmChallengeHandler.isCustomResponse = function (response) {
    EWD.busyIndicator.hideOnContainer($('.scene_login'));
    if (!response || response.responseText === null) {
        return false;
    }
    var indicatorIdx = response.responseText.search('j_security_check');

    if (indicatorIdx >= 0) {
        return true;
    }
    return false;
};

WASLtpaRealmChallengeHandler.handleFailure = function(res) {
    console.log("ERROR@@@@@"+JSON.stringify(res));
}
WASLtpaRealmChallengeHandler.processSuccess = function(res) {
    console.log("SUCCESS@@@"+JSON.stringify(res));
}

WASLtpaRealmChallengeHandler.handleChallenge = function (response) {
    var res = response.responseText;

    if (res.length <= 0) {
        EWD.busyIndicator.hideOnContainer($('.scene_login'));
        r.trigger('global.alert', dictionary.t("scene_login.credentials.failed"));
        return;
    }
};

WASLtpaRealmChallengeHandler.submitLoginFormCallback = function (response) {

    var isLoginFormResponse = WASLtpaRealmChallengeHandler.isCustomResponse(response);
    if (isLoginFormResponse) {
        WASLtpaRealmChallengeHandler.handleChallenge(response);
    } else {

        // Problems with submitSuccess() (why?). It is in worklight.js
        try {
            WASLtpaRealmChallengeHandler.submitSuccess();
        } catch(er){
            console.log("ERROR WASLtpaRealmChallengeHandler.submitSuccess" + JSON.stringify(er));            
        }
        WASLtpaRealmChallengeHandler.loginUser();

        $('#splashscreen').show();
        $('#splashscreen .message span').html(dictionary.greetings(null));
    }
};


WASLtpaRealmChallengeHandler.loginUser = function () {
    WL.Client.login("WASLTPARealm", {
        onSuccess: function (subject) {
            if (EWD.TOUCH_ID.get()) {
                EWD.TOUCH_ID.removeAll();
                EWD.TOUCH_ID.set();
            } else {
                EWD.TOUCH_ID.removeAll();
            }
            var LtpaToken = subject.responseJSON.WASLTPARealm.attributes.LtpaToken;
            
            function afterSetLtpaToken2Cookie() {
                WL.Client.getCookies().then(function () {
                    injectCookie().done(function () {
                        registerAllScenesAfterLogin();  //Registering scenes for loged user, and assigning homepage (dashboard) scene
                        EWD.TOUCH_ID.showOrHide();
                    });
                });
            }
            
            //Set LtpaToken cookie
            setLtpaToken(LtpaToken, function() {
                afterSetLtpaToken2Cookie();
            }, function() {
                //For android and WindowsPhone
                var cookieArgs = [];
                cookieArgs.push(createLtpaToken2CookieFromValue(LtpaToken));
                try {
                    injectCreatedCookieArgs(cookieArgs).done(function(){
                        afterSetLtpaToken2Cookie();
                    });
                } catch(e) {
                    console.log("Can't find injectCreatedCookieArgs" + JSON.stringify(e));
                }
            });
        }, onFailure: function() {
            r.trigger('global.alert', dictionary.t("scene_login.credentials.failed"));
        }
    });
}


WASLtpaRealmChallengeHandler.getCredentials = function () {

    var options = {};
    options.parameters = {
        j_username: "username", //test
        j_password: "password" // test
    };
    options.headers = {};
    WASLtpaRealmChallengeHandler.submitLoginForm('/j_security_check', options, WASLtpaRealmChallengeHandler.submitLoginFormCallback);
}




//... Action sheet calls this function on logout
function logout(buttonIndex) {

    if (buttonIndex == 1) {
        WL.App.showSplashScreen();
        WL.Client.logout('WASLTPARealm', {
            onSuccess: function() {
                EWD.invalidateCacheFromStorage();        
                WL.Client.getCookies().then(function(cookies) {
                    WL.Client.deleteCookie("JSESSIONID");
                    WL.Client.deleteCookie("LtpaToken");
                    WL.Client.deleteCookie("LtpaToken2");
                    WL.Client.reloadApp();
                });
            },
            onFailure: function(res) {
                //inform user something wrong
                //...
                //end
            }
        });
    }
}


$(document).ready(function () {
    'use strict';
    
    //start doing some stuff connected with showing scenes ... 
    // ....
    // end

    //get credentials from form after click button
    $(document).on("click", "#scene_login_action", function (e) {
        WASLtpaRealmChallengeHandler.getCredentials();
    });

    
    WL.Client.login("WASLTPARealm");
    
});