/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm;

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

@OAuthSecurity(enabled = false)
@Api(value = "Customer Information")
@Produces(MediaType.TEXT_PLAIN)
@Path("/customers")
public class CustomerInfoResource {

    // Define logger (Standard java.util.Logger)
    static Logger logger = Logger.getLogger(CustomerInfoResource.class.getName());
    //http://cap-sg-prd-2.integration.ibmcloud.com:15330/customers/
    //can change this to  your personal ip address
    //String baseURL = "http://localhost:9080/customers/";

    private static CloseableHttpClient client;

    WLServerAPI api = WLServerAPIProvider.getWLServerAPI();


    @Context
    AdaptersAPI adaptersAPI;


    // Inject the MFP configuration API:
    @Context
    ConfigurationAPI configApi;


    public static void init() {
    }

    public CustomerInfoResource() {
        if (client == null) {
            client = HttpClientBuilder.create().build();
        }
    }

    @ApiOperation(value = "Customers", notes = "Getting all the customer info.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A JSONObject is returned")})
    @GET
    @Produces("application/json")
    public Response getCustomers() throws Exception {
        //enable access to CRM - Secure Gateway
        validateSecureGatewayBridge();


        String url = configApi.getPropertyValue("onPremCRMAddress");
        HttpGet request = new HttpGet(url);
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity);

        return Response.ok(responseString).build();
    }


    @ApiOperation(value = "Customers", notes = "Post new customers")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A JSONObject is returned")})
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public Response newCustomer(JSONObject newCust) throws Exception {

        //enable access to CRM - Secure Gateway
        validateSecureGatewayBridge();

        String url = configApi.getPropertyValue("onPremCRMAddress");
        String payload = newCust.toString();
        //sample customer {"name": "Jack Reacher", "plate": "ETS-9876", "make": "Honda","model": "Accord","vin": "1234567890"}
        HttpPost request = new HttpPost(url);
        request.addHeader("Content-Type", "application/json");

        HttpEntity entity = new ByteArrayEntity(payload.getBytes("UTF-8"));
        request.setEntity(entity);

        HttpResponse response = client.execute(request);
        String result = EntityUtils.toString(response.getEntity());
        return Response.ok(result).build();
    }

    @ApiOperation(value = "Customer Appointment", notes = "Put new customer appointments")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A JSONObject is returned")})
    @PUT
    @Produces("application/json")
    @Consumes("application/json")
    public Response putsAppointments(JSONObject appointment) throws Exception {
        //enable access to CRM - Secure Gateway
        validateSecureGatewayBridge();


        String url = configApi.getPropertyValue("onPremCRMAddress");
        //String payload = "{\n    \"name\": \"Pete\",\n    \"plate\": \"EYW8\"\n}";
        String payload = appointment.toString();

        HttpPut request = new HttpPut(url);
        request.addHeader("Content-Type", "application/json");

        HttpEntity entity = new ByteArrayEntity(payload.getBytes("UTF-8"));
        request.setEntity(entity);

        HttpResponse response = client.execute(request);
        String result = EntityUtils.toString(response.getEntity());
        return Response.ok(result).build();
    }

    @ApiOperation(value = "Customer", notes = "Get customer by ID")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A JSONObject is returned")})
    @GET
    @Produces("application/json")
    @Path("/{id}")
    public Response getCustomerByID(@PathParam("id") Integer id) throws Exception {
        //enable access to CRM - Secure Gateway
        validateSecureGatewayBridge();

        String url = configApi.getPropertyValue("onPremCRMAddress") + id;
        HttpGet request = new HttpGet(url);
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity);

        return Response.ok(responseString).build();
    }

    @ApiOperation(value = "Customer", notes = "Get customer visits by custID")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A JSONObject is returned")})
    @GET
    @Produces("application/json")
    @Path("/{id}/visits")
    public Response getCustomerVisitsByID(@PathParam("id") Integer id) throws Exception {
        //enable access to CRM - Secure Gateway
        validateSecureGatewayBridge();

        String url = configApi.getPropertyValue("onPremCRMAddress") + id + "/visits";
        HttpGet request = new HttpGet(url);
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity);
        return Response.ok(responseString).build();
    }

    @ApiOperation(value = "Customer", notes = "Create new visit for customer by custID")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A JSONObject is returned")})
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/{id}/visits")
    public Response newVisit(JSONObject newVisit, @PathParam("id") Integer id) throws Exception {
        //enable access to CRM - Secure Gateway
        validateSecureGatewayBridge();


        String url = configApi.getPropertyValue("onPremCRMAddress") + id + "/visits/";
        String payload = newVisit.toString();
        //sample customer {"name": "Jack Reacher", "plate": "ETS-9876", "make": "Honda","model": "Accord","vin": "1234567890"}
        HttpPost request = new HttpPost(url);
        request.addHeader("Content-Type", "application/json");

        HttpEntity entity = new ByteArrayEntity(payload.getBytes("UTF-8"));
        request.setEntity(entity);

        HttpResponse response = client.execute(request);
        String result = EntityUtils.toString(response.getEntity());
        return Response.ok(result).build();
    }

    @ApiOperation(value = "Customer", notes = "Search customers by plate or id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A JSONObject is returned")})
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/search")
    public Response searchCustomers(JSONObject searchFilter) throws Exception {
        //enable access to CRM - Secure Gateway
        validateSecureGatewayBridge();


        String url = configApi.getPropertyValue("onPremCRMAddress") + "_search";
        //sample searchFilter = { "plate": "ETS-9876"}
        String payload = searchFilter.toString();

        HttpPost request = new HttpPost(url);
        request.addHeader("Content-Type", "application/json");

        HttpEntity entity = new ByteArrayEntity(payload.getBytes("UTF-8"));
        request.setEntity(entity);

        HttpResponse response = client.execute(request);
        String result = EntityUtils.toString(response.getEntity());
        return Response.ok(result).build();
    }

    /**
     * Check and Fix Secure gateway bridge
     *
     * @throws IOException
     */
    private void validateSecureGatewayBridge() throws IOException {
        logger.info("validateSecureGatewayBridge:");
        HttpUriRequest req = new HttpGet("/SecureGatewayAdapter/secure/updateFirewall");
        req.addHeader("Accept", "text/plain");
        HttpResponse response = adaptersAPI.executeAdapterRequest(req);
        logger.info("validateSecureGatewayBridge: done" + response.toString());
    }
}
