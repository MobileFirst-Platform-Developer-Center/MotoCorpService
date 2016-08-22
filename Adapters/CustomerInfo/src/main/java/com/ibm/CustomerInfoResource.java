/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.ibm.mfp.adapter.api.ConfigurationAPI;
import com.ibm.mfp.adapter.api.OAuthSecurity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.HttpClient;
import org.apache.wink.json4j.utils.XML;
import org.xml.sax.SAXException;
import org.apache.http.util.EntityUtils;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.ByteArrayEntity;

import com.worklight.adapters.rest.api.WLServerAPI;
import com.worklight.adapters.rest.api.WLServerAPIProvider;


@OAuthSecurity(enabled=false)
@Api(value = "Sample Adapter Resource")
@Produces(MediaType.TEXT_PLAIN)
@Path("/customers")
public class CustomerInfoResource {

    private static CloseableHttpClient client;
    
    WLServerAPI api = WLServerAPIProvider.getWLServerAPI();
    
    public static void init() {
      
    }
    
    public CustomerInfoResource() {
    	if(client == null) {
    		client = HttpClientBuilder.create().build();
    	}
    }

	@ApiOperation(value = "Customers", notes = "Getting all the customer info.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "A JSONObject is returned") })
	@GET
	@Produces("application/json")
	@OAuthSecurity(enabled = false)
    public String getCustomers() throws Exception{
        String url = "http://cap-sg-prd-2.integration.ibmcloud.com:15330/customers";
        HttpGet request = new HttpGet(url); 
            CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity);
        return responseString;
    }
    
    @ApiOperation(value = "Customers", notes = "Posting new customer info.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "A JSONObject is returned") })
    @POST
	@Produces("application/json")
	@OAuthSecurity(enabled = false)
    public Response postCustomers() throws Exception{
        String url = "http://cap-sg-prd-2.integration.ibmcloud.com:15330/customers";
        String payload = "{\n    \"name\": \"Pete\",\n    \"plate\": \"EYW8\"\n}";
        
        HttpPost request = new HttpPost(url);
        request.addHeader("Content-Type","application/json");
        
        HttpEntity entity = new ByteArrayEntity(payload.getBytes("UTF-8"));
        request.setEntity(entity);

        HttpResponse response = client.execute(request);
        String result = EntityUtils.toString(response.getEntity());
        return Response.ok(result).build();
    }
		

}
