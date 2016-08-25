package com.sample;

import java.util.Properties;


import com.ibm.mfp.security.checks.base.UserAuthenticationSecurityCheckConfig;

public class SecureGatewayBinderConfig extends UserAuthenticationSecurityCheckConfig {
    public String sgwToken;
    public String gatewayId;
    public String destinationId;

    public SecureGatewayBinderConfig(Properties properties) {
        //Make sure to load the parent properties
        super(properties);

        //Load the pinCode property
        sgwToken = getStringProperty("sgwToken", properties, "_blank");
        gatewayId = getStringProperty("gatewayId", properties, "_blank");
        destinationId = getStringProperty("destinationId", properties, "_blank");
    }

}
