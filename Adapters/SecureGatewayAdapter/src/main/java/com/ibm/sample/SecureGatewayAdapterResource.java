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

package com.ibm.sample;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.ibm.json.java.JSONObject;
import com.ibm.mfp.adapter.api.ConfigurationAPI;
import com.ibm.mfp.adapter.api.OAuthSecurity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Sample Adapter Resource")
@Path("/secure")
public class SecureGatewayAdapterResource {
	/*
	 * For more info on JAX-RS see
	 * https://jax-rs-spec.java.net/nonav/2.0-rev-a/apidocs/index.html
	 */

	// Define logger (Standard java.util.Logger)
	static Logger logger = Logger.getLogger(SecureGatewayAdapterResource.class.getName());

	// Inject the MFP configuration API:
	@Context
	ConfigurationAPI configApi;


    private static HttpHost host;
    private static CloseableHttpClient client;

	/*
	 * Path for method:
	 * "<server address>/mfp/api/adapters/SecureGatewayAdapter/resource/unprotected"
	 */

	@ApiOperation(value = "Unprotected Resource", notes = "Example of an unprotected resource, this resource is accessible without a valid token.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "A constant string is returned") })
	@GET
	@Path("/updateFirewall")
	@Produces(MediaType.TEXT_PLAIN)
	@OAuthSecurity(enabled = false)
	public String updateFirewall() {
		
    	host = new HttpHost("sgmanager.ng.bluemix.net", 443, "https");
    	logger.info("CF_INSTANCE_IP:" + System.getenv("CF_INSTANCE_IP"));
    	logger.info("CF_INSTANCE_INDEX:" + System.getenv("CF_INSTANCE_INDEX"));
    	logger.info("CF_INSTANCE_PORT:" + System.getenv("CF_INSTANCE_PORT"));    	    	
    	logger.info("Security Token:" + configApi.getPropertyValue("sgwToken"));
    	logger.info("gatewayId :" + configApi.getPropertyValue("gatewayId"));
    	logger.info("destinationId:" + configApi.getPropertyValue("destinationId"));
    	
    	client = HttpClientBuilder.create().build();
    	
    	HttpPut put = new HttpPut("/v1/sgconfig/"+ configApi.getPropertyValue("gatewayId") + "/destinations/"+ configApi.getPropertyValue("destinationId") + "/ipTableRule");
    	put.addHeader("Content-Type", "application/json");
    	put.addHeader("Authorization", "Bearer "+configApi.getPropertyValue("sgwToken"));
    	//put.addHeader("Accept", "application/json");
    	 JSONObject keyArg = new JSONObject();
            keyArg.put("src", System.getenv("CF_INSTANCE_IP"));
            keyArg.put("app", "MobileDev-bo-Server");
            // + System.getenv("CF_INSTANCE_INDEX")
            StringEntity input;
            try {
                input = new StringEntity(keyArg.toString());
                put.setEntity(input);
                logger.info("data:" + keyArg.toString());
                HttpResponse response = client.execute(host,put);
                logger.info(response.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                logger.warning("StringEntity - issues!");	                
            } catch (ClientProtocolException e) {
            	logger.warning("ClientProtocolException - issues!");
				e.printStackTrace();
			} catch (IOException e) {
				logger.warning("IOException - issues!");
				e.printStackTrace();
			}        
    	return "firewall updated";				  				
	}
}
