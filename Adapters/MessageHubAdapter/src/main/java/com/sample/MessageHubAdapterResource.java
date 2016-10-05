/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.sample;

import java.io.IOException;
import java.util.logging.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.ibm.json.java.JSONObject;
import com.ibm.mfp.adapter.api.AdaptersAPI;
import com.ibm.mfp.adapter.api.ConfigurationAPI;
import com.ibm.mfp.adapter.api.OAuthSecurity;
import com.worklight.adapters.rest.api.WLServerAPI;
import com.worklight.adapters.rest.api.WLServerAPIProvider;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Sample Adapter Resource")
@Path("/resource")
public class MessageHubAdapterResource {
	   // Define logger (Standard java.util.Logger)
    static Logger logger = Logger.getLogger(MessageHubAdapterResource.class.getName());
    private static CloseableHttpClient client;

    public static void init() {
    }

    public MessageHubAdapterResource() {
        if (client == null) {
            client = HttpClientBuilder.create().build();
        }
    }

	/*
	 * Send MessageHub message
	 */
    @OAuthSecurity(enabled = false)
	@ApiOperation(value = "Customer", notes = "Create new visit for customer by custID")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A JSONObject is returned")})
    @POST
    @Consumes("application/json")
    @Path("/sendMessage")
    public Response sendMessage(JSONObject msgPayload) throws Exception {
        
        String url = "http://ourmessagehub.mybluemix.net/sendMessage";
        String payload = msgPayload.toString();
        HttpPost request = new HttpPost(url);
        request.addHeader("Content-Type", "application/json");

        HttpEntity entity = new ByteArrayEntity(payload.getBytes("UTF-8"));
        request.setEntity(entity);

        HttpResponse response = client.execute(request);
        String result = EntityUtils.toString(response.getEntity());
        return Response.ok(result).build();
    }

}

