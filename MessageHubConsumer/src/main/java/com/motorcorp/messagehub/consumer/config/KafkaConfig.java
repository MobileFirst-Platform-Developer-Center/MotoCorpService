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
package com.motorcorp.messagehub.consumer.config;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class KafkaConfig extends Configuration implements ConfigurationLoader {
    private Map<String, String> options = new HashMap<String, String>();
    private static final String FLAG = "org.apache.kafka.common.security.plain.PlainLoginModule";

    public KafkaConfig() {
        options.put("serviceName", "kafka");
    }

    @Override
    public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
        return new AppConfigurationEntry[]{
                new AppConfigurationEntry(KafkaConfig.FLAG, AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options)
        };
    }

    public void loadConfig(Properties config) {
        options.put("username", config.getProperty("messagehub-username"));
        options.put("password", config.getProperty("messagehub-password"));
    }
}