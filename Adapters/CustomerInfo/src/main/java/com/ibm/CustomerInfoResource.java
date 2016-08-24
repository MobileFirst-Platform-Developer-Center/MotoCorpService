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
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;

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
import org.apache.http.client.methods.HttpPut;

import com.worklight.adapters.rest.api.WLServerAPI;
import com.worklight.adapters.rest.api.WLServerAPIProvider;
import com.ibm.json.java.JSONObject;

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
    
    @ApiOperation(value = "Customers", notes = "Post new customers")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "A JSONObject is returned") })
	@POST
	@Produces("application/json")
	@Consumes("application/json")
	@OAuthSecurity(enabled = false)
    public Response newCustomer( 
    			JSONObject newCust
    		) throws Exception{
        
		String url = "http://cap-sg-prd-2.integration.ibmcloud.com:15330/customers/";
		String payload = newCust.toString();
        //sample customer {"name": "Jack Reacher", "plate": "ETS-9876", "make": "Honda","model": "Accord","vin": "1234567890"}
        HttpPost request = new HttpPost(url);
        request.addHeader("Content-Type","application/json");
        
        HttpEntity entity = new ByteArrayEntity(payload.getBytes("UTF-8"));
        request.setEntity(entity);

        HttpResponse response = client.execute(request);
        String result = EntityUtils.toString(response.getEntity());
        return Response.ok(result).build();
    }
    
    @ApiOperation(value = "Customer Appointment", notes = "Put new customer appointments")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "A JSONObject is returned") })
    @PUT
	@Produces("application/json")
	@Consumes("application/json")
	@OAuthSecurity(enabled = false)
    public Response putsAppointments( JSONObject appointment
    		) throws Exception{
        
		String url = "http://cap-sg-prd-2.integration.ibmcloud.com:15330/customers";
        //String payload = "{\n    \"name\": \"Pete\",\n    \"plate\": \"EYW8\"\n}";
		String payload = appointment.toString();
        
        HttpPut request = new HttpPut(url);
        request.addHeader("Content-Type","application/json");
        
        HttpEntity entity = new ByteArrayEntity(payload.getBytes("UTF-8"));
        request.setEntity(entity);

        HttpResponse response = client.execute(request);
        String result = EntityUtils.toString(response.getEntity());
        return Response.ok(result).build();
    }
    
    @ApiOperation(value = "Customer", notes = "Get customer by ID")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "A JSONObject is returned") })
    @GET
	@Produces("application/json")
	@Path("/custID")
	@OAuthSecurity(enabled = false)
    public String getCustomerByID( @QueryParam("custID") String custID
    		) throws Exception{
        String url = "http://cap-sg-prd-2.integration.ibmcloud.com:15330/customers/" + custID;
        HttpGet request = new HttpGet(url); 
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity);
        return responseString;
    }
    
    @ApiOperation(value = "Customer", notes = "Get customer visits by custID")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "A JSONObject is returned") })
    @GET
	@Produces("application/json")
	@Path("/custID/visits")
	@OAuthSecurity(enabled = false)
    public String getCustomerVisitsByID( @QueryParam("custID") String custID
    		) throws Exception{
        String url = "http://cap-sg-prd-2.integration.ibmcloud.com:15330/customers/" + custID + "/visits";
        HttpGet request = new HttpGet(url); 
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity);
        return responseString;
    }
    
    @ApiOperation(value = "Customer", notes = "Search customers by plate or id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "A JSONObject is returned") })
    @POST
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/search")
	@OAuthSecurity(enabled = false)
    public Response searchCustomers( JSONObject searchFilter
    		) throws Exception{
        
		String url = "http://cap-sg-prd-2.integration.ibmcloud.com:15330/customers/_search";
        //sample searchFilter = { "plate": "ETS-9876"}
		String payload = searchFilter.toString();
        
        HttpPost request = new HttpPost(url);
        request.addHeader("Content-Type","application/json");
        
        HttpEntity entity = new ByteArrayEntity(payload.getBytes("UTF-8"));
        request.setEntity(entity);

        HttpResponse response = client.execute(request);
        String result = EntityUtils.toString(response.getEntity());
        return Response.ok(result).build();
    }
    
    @ApiOperation(value = "Customer", notes = "Create new visit for customer by custID")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "A JSONObject is returned") })
    @POST
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/newVisit")
	@OAuthSecurity(enabled = false)
    public Response newVisit( 
    			JSONObject newVisit,
    			@QueryParam("custID") String custID
    		) throws Exception{
        
		String url = "http://cap-sg-prd-2.integration.ibmcloud.com:15330/customers/" + custID + "/visits/";
		String payload = newVisit.toString();
        //sample customer {"name": "Jack Reacher", "plate": "ETS-9876", "make": "Honda","model": "Accord","vin": "1234567890"}
        HttpPost request = new HttpPost(url);
        request.addHeader("Content-Type","application/json");
        
        HttpEntity entity = new ByteArrayEntity(payload.getBytes("UTF-8"));
        request.setEntity(entity);

        HttpResponse response = client.execute(request);
        String result = EntityUtils.toString(response.getEntity());
        return Response.ok(result).build();
    }
}
