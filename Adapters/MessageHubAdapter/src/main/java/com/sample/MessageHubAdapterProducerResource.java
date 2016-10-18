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
package com.sample;

import com.ibm.json.java.JSONObject;
import com.ibm.mfp.adapter.api.ConfigurationAPI;
import com.ibm.mfp.adapter.api.OAuthSecurity;
import com.sample.config.KafkaConfig;
import com.sample.config.KafkaRestConfig;
import com.sample.config.MessageHubProperties;
import com.sample.util.MessageHubProducer;
import com.sample.util.MessageHubREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.http.client.HttpResponseException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Api(value = "MessageHub Adapter Producer")
@Path("/resource")
@Consumes("application/json")
@OAuthSecurity(enabled = false)
public class MessageHubAdapterProducerResource {
    private static final String NEW_CUSTOMER_TOPIC = "new-customer";
    private static final String NEW_VISIT_TOPIC = "new-visit";

    private final KafkaProducer<byte[], byte[]> producer;
    private final MessageHubREST messageHubREST;

    public MessageHubAdapterProducerResource(@Context ConfigurationAPI configurationAPI) {
        KafkaConfig kafkaConfig = new KafkaConfig();
        kafkaConfig.loadConfig(configurationAPI);

        javax.security.auth.login.Configuration.setConfiguration(kafkaConfig);


        KafkaRestConfig kafkaRestConfig = new KafkaRestConfig();
        kafkaRestConfig.loadConfig(configurationAPI);
        messageHubREST = MessageHubREST.getInstance(kafkaRestConfig);


        MessageHubProperties messageHubProperties = MessageHubProperties.getInstance(configurationAPI);
        producer = MessageHubProducer.getInstance(messageHubProperties);
    }

    @ApiOperation(value = "Customer", notes = "Forwards new customer record to MessageHub")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A JSONObject is returned")})
    @POST
    @Path("/newCustomer")
    public Response newCustomer(JSONObject customer) throws Exception {
        createTopicIfNeeded(MessageHubAdapterProducerResource.NEW_CUSTOMER_TOPIC);

        String messageKey = Integer.toString((int)(Math.random()*10000F));

        producer.send(produce(MessageHubAdapterProducerResource.NEW_CUSTOMER_TOPIC, messageKey, customer.toString()));

        return Response.ok().build();
    }

    @ApiOperation(value = "Customer", notes = "Forwards new visit record to MessageHub")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A JSONObject is returned")})
    @POST
    @Consumes("application/json")
    @Path("/{id}/newVisit")
    public Response newCustomerVisit(@PathParam("id") String id, JSONObject customerVisit) throws Exception {
        createTopicIfNeeded(MessageHubAdapterProducerResource.NEW_VISIT_TOPIC);

        JSONObject newVisit = new JSONObject();

        newVisit.put("CustomerId", id);
        newVisit.put("CustomerVisit", customerVisit);

        producer.send(produce(MessageHubAdapterProducerResource.NEW_VISIT_TOPIC, id, newVisit.toString()));

        return Response.ok().build();
    }

    protected void createTopicIfNeeded(String topic) {
        ArrayList topics = messageHubREST.getCurrentTopics();

        if(!topics.contains(topic)) {
            try {
                messageHubREST.createTopic(topic);
            } catch (HttpResponseException e) {
                // 422 means the topic already exists
                if(e.getStatusCode() != 422) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected ProducerRecord<byte[], byte[]> produce(String topic, String key, String payload) {
        return new ProducerRecord<byte[], byte[]>(topic, key.getBytes(), payload.getBytes());
    }
}

