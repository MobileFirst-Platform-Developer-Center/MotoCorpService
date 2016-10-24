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

import java.util.Properties;

public class MessageHubProperties implements ConfigurationLoader {

    private static MessageHubProperties instance;

    public static MessageHubProperties getInstance(Properties config) {
        if (instance == null) {
            instance = new MessageHubProperties();
            instance.loadConfig(config);
        }

        return instance;
    }

    private Properties props = new Properties();

    private MessageHubProperties() {
        props.put("key.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");

        props.put("client.id", "message-hub-java-consumer");
        props.put("enable.auto.commit", false);
        props.put("auto.offset.reset", "latest");

        props.put("security.protocol", "SASL_SSL");
        props.put("sasl.mechanism", "PLAIN");
        props.put("ssl.protocol", "TLSv1.2");
        props.put("ssl.enabled.protocols", "TLSv1.2");

        props.put("ssl.endpoint.identification.algorithm", "HTTPS");
    }

    public Properties getConfig() {
        return props;
    }

    public void loadConfig(Properties config) {
        props.put("bootstrap.servers", config.getProperty("message-hub-servers"));
        props.put("ssl.truststore.location", config.getProperty("truststore-path"));
        props.put("ssl.truststore.password", config.getProperty("truststore-password"));
        props.put("ssl.truststore.type", config.getProperty("truststore-type"));
    }
}
