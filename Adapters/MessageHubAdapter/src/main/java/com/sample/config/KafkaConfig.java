/*
 * Copyright 2016 IBM Corp.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.sample.config;

import com.ibm.mfp.adapter.api.ConfigurationAPI;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import java.util.HashMap;
import java.util.Map;


public class KafkaConfig extends Configuration {
    private Map<String, String> options = new HashMap<String, String>();
    private static final String FLAG = "org.apache.kafka.common.security.plain.PlainLoginModule";

    public KafkaConfig(ConfigurationAPI config) {
        options.put("serviceName", "kafka");
        options.put("username", config.getPropertyValue("messagehub-username"));
        options.put("password", config.getPropertyValue("messagehub-password"));
    }

    @Override
    public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
        return new AppConfigurationEntry[]{
                new AppConfigurationEntry(KafkaConfig.FLAG, AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options)
        };
    }
}
