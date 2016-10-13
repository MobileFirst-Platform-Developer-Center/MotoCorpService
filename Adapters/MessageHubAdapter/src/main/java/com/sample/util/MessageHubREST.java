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

package com.sample.util;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;
import com.ibm.mfp.adapter.api.ConfigurationAPI;
import com.sample.config.KafkaRestConfig;
import org.apache.http.HttpException;
import org.apache.http.client.HttpResponseException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MessageHubREST {

    private static MessageHubREST instance;

    public static MessageHubREST getInstance(KafkaRestConfig config) {
        if (instance == null) {
            instance = new MessageHubREST(config);
        }

        return instance;
    }

    private KafkaRestConfig config;
    private SSLContext sslContext;

    private MessageHubREST(KafkaRestConfig config) {
        this.config = config;


        try {
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public void createTopic(String topic) throws HttpResponseException {
        JSONObject object = new JSONObject();
        object.put("name", topic);


        try {
            this.request("POST", "/admin/topics", object.toString());
        } catch (HttpResponseException e) {
            if (e.getStatusCode() != 422) {
                throw e;
            }
        }
    }

    public ArrayList<String> getCurrentTopics() {

        ArrayList<String> topicList = new ArrayList<String>();

        try {
            JSONArray topics = JSONArray.parse(request("GET", "/admin/topics"));

            for (Object topic : topics) {
                topicList.add(((JSONObject) topic).get("name").toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return topicList;
    }


    protected String request(String method, String path) throws HttpResponseException {
        return request(method, path, null);
    }

    protected String request(String method, String path, String body) throws HttpResponseException {
        HttpsURLConnection connection = null;
        int responseCode = 0;

        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        try {

            URL url = new URL(config.getEndpoint() + path);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(sslContext.getSocketFactory());
            connection.setDoOutput(true);
            connection.setRequestMethod(method);

            // Apply headers, in this case, the API key and Kafka content type.
            connection.setRequestProperty("X-Auth-Token", config.getApiKey());
            connection.setRequestProperty("Content-Type", "application/json");

            if (body != null) {
                // Send the request, writing the body data
                // to the output stream.
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(body);
                wr.close();
            }

            responseCode = connection.getResponseCode();

            // Retrieve the response, transform it, then
            // return it to the caller.
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }

            rd.close();

            return response.toString();
        } catch (Exception e) {
            throw new HttpResponseException(responseCode, e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
