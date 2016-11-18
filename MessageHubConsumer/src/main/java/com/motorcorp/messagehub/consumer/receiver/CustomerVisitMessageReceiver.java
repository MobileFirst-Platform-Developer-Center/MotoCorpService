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
package com.motorcorp.messagehub.consumer.receiver;

import com.motorcorp.messagehub.consumer.util.HttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerVisitMessageReceiver implements MessageReceiver {
    private String endpoint;

    public CustomerVisitMessageReceiver(String endpoint) {
        this.endpoint = endpoint;
    }

    static Logger logger = Logger.getLogger(CustomerMessageReceiver.class.getName());

    @Override
    public void receiveMessage(byte[] payload) {
        try {
            JSONObject json = new JSONObject(new String(payload));
            String requestUrl = endpoint + "/customers/" + json.optInt("CustomerId") + "/visits/";

            logger.log(Level.INFO, "Sending New Customer Visit Request");
            logger.log(Level.INFO, "Endpoint: " + requestUrl);
            logger.log(Level.INFO, "Payload: " + json.optJSONObject("CustomerVisit"));

            HttpClient.getInstance().post(requestUrl, json.optJSONObject("CustomerVisit"));

        } catch (JSONException | IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
