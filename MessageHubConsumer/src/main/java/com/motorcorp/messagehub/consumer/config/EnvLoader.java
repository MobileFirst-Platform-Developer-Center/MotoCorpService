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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Properties;

public class EnvLoader {

    public static Properties load() throws MissingConfigurationException {

        try {
            String configString = System.getenv("APP_CONFIG");

            if(configString == null) {
                throw new JSONException("missing environment variable");
            }

            JSONObject config = new JSONObject(configString.trim());

            Properties props = new Properties();

            Iterator keys = config.keys();

            while(keys.hasNext()) {
                String key = (String) keys.next();
                props.put(key, config.optString(key));
            }

            return props;

        } catch (JSONException e) {
            throw new MissingConfigurationException("Missing `APP_CONFIG` environment variable");
        }
    }

    public static class MissingConfigurationException extends Exception {

        public MissingConfigurationException(String message) {
            super(message);
        }
    }
}
