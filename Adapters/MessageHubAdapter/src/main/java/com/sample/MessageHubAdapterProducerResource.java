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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.json.java.JSONObject;
import com.ibm.mfp.adapter.api.ConfigurationAPI;
import com.ibm.mfp.adapter.api.OAuthSecurity;
import com.sample.config.KafkaConfig;
import com.sample.config.KafkaRestConfig;
import com.sample.config.MessageHubProperties;
import com.sample.models.Customer;
import com.sample.models.CustomerVisit;
import com.sample.util.MessageHubProducer;
import com.sample.util.MessageHubREST;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.http.client.HttpResponseException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Api(value = "MessageHub Adapter Producer")
@Path("/resource")
@Consumes("application/json")
@Produces("application/json")
@OAuthSecurity(enabled = false)
public class MessageHubAdapterProducerResource {
    private static final String NEW_CUSTOMER_TOPIC = "new-customer";
    private static final String NEW_VISIT_TOPIC = "new-visit";

    private final KafkaProducer<byte[], byte[]> producer;
    private final MessageHubREST messageHubREST;
    private  final ObjectMapper objectMapper;

    public MessageHubAdapterProducerResource(@Context ConfigurationAPI configurationAPI) {
        KafkaConfig kafkaConfig = new KafkaConfig();
        kafkaConfig.loadConfig(configurationAPI);

        javax.security.auth.login.Configuration.setConfiguration(kafkaConfig);


        KafkaRestConfig kafkaRestConfig = new KafkaRestConfig();
        kafkaRestConfig.loadConfig(configurationAPI);
        messageHubREST = MessageHubREST.getInstance(kafkaRestConfig);


        MessageHubProperties messageHubProperties = MessageHubProperties.getInstance(configurationAPI);
        producer = MessageHubProducer.getInstance(messageHubProperties);

        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @ApiOperation(value = "Customer", notes = "Forwards new customer record to MessageHub")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A JSONObject is returned")})
    @POST
    @Path("/newCustomer")
    public Response newCustomer(Customer customer) throws Exception {
        createTopicIfNeeded(MessageHubAdapterProducerResource.NEW_CUSTOMER_TOPIC);

        String messageKey = Integer.toString((int)(Math.random()*10000F));

        byte[] payload  = objectMapper.writeValueAsBytes(customer);

        producer.send(produce(MessageHubAdapterProducerResource.NEW_CUSTOMER_TOPIC, messageKey, payload));

        return okResponse();
    }

    @ApiOperation(value = "Customer", notes = "Forwards new visit record to MessageHub")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A JSONObject is returned")})
    @POST
    @Consumes("application/json")
    @Path("/{id}/newVisit")
    public Response newCustomerVisit(@PathParam("id") String id, CustomerVisit customerVisit) throws Exception {
        createTopicIfNeeded(MessageHubAdapterProducerResource.NEW_VISIT_TOPIC);

        JSONObject newVisit = new JSONObject();

        newVisit.put("CustomerId", id);
        newVisit.put("CustomerVisit", objectMapper.writerWithType(JSONObject.class));

        producer.send(produce(MessageHubAdapterProducerResource.NEW_VISIT_TOPIC, id, newVisit.toString().getBytes()));

        return okResponse();
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

    protected ProducerRecord<byte[], byte[]> produce(String topic, String key, byte[] payload) {
        return new ProducerRecord<byte[], byte[]>(topic, key.getBytes(), payload);
    }

    protected Response okResponse() {
        JSONObject response = new JSONObject();
        response.put("published", true);

        return Response.ok(response).build();
    }
}

