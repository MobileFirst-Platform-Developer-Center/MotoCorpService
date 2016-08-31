/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.com.ibm.models.*;
import com.ibm.util.HttpRequestUtil;
import io.swagger.annotations.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import com.ibm.mfp.adapter.api.AdaptersAPI;
import com.ibm.mfp.adapter.api.ConfigurationAPI;
import com.ibm.mfp.adapter.api.OAuthSecurity;

@Api(value = "Customer Information")
@Consumes("application/json")
@Produces(MediaType.APPLICATION_JSON)
@OAuthSecurity(scope = "user-restricted")
@Path("/customers")
public class CustomerInfoResource {
    private static Logger logger = Logger.getLogger(CustomerInfoResource.class.getName());
    private HttpRequestUtil httpRequestUtil;
    private ObjectMapper objectMapper;

    public CustomerInfoResource(@Context ConfigurationAPI configApi, @Context AdaptersAPI adaptersAPI) throws URISyntaxException {
        httpRequestUtil = new HttpRequestUtil(configApi.getPropertyValue("onPremCRMAddress"));
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {
            //enable access to CRM - Secure Gateway
            validateSecureGatewayBridge(adaptersAPI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "List Customers", notes = "List containing all the customers in the CRM is returned")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A array containing all the customers", response = Customer.class, responseContainer = "List")})
    @GET
    public Response getCustomers() throws Exception {
        String response = httpRequestUtil.get("");

        return Response.ok(response).build();
    }


    @ApiOperation(value = "Create Customer", notes = "Creates a new customer record in the CRM")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A customer object of the newly created record")})
    @POST
    public Response newCustomer(Customer customer) throws Exception {
        byte[] payload = objectMapper.writeValueAsBytes(customer);

        String response = httpRequestUtil.post("", payload);

        return Response.ok(response).build();
    }

    @ApiOperation(value = "Get Customer Details", notes = "Get customer by ID")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "A customer object is returned", response = Customer.class)})
    @GET
    @Path("/{id}")
    public Response getCustomerByID(@PathParam("id") Integer id) throws Exception {

        String body = httpRequestUtil.get(id + "");

        return Response.ok(body).build();
    }

    @ApiOperation(value = "List Customer Visits", notes = "List containing the visits related to a specific customer is returned")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "A array containing the visits for the matched customer", response = CustomerVisit.class, responseContainer = "List")
    })
    @GET
    @Path("/{id}/visits")
    public Response getCustomerVisitsByID(@PathParam("id") Integer id) throws Exception {

        String response = httpRequestUtil.get(id + "/visits");

        return Response.ok(response).build();
    }

    @ApiOperation(value = "Create Customer Visit", notes = "Creates a new visit for the matching customer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "A customer visit object of the newly created record", response = CustomerVisit.class)
    })
    @POST
    @Path("/{id}/visits")
    public Response newVisit(CustomerVisit newVisit, @PathParam("id") Integer id) throws Exception {
        byte[] payload = objectMapper.writeValueAsBytes(newVisit);

        String body = httpRequestUtil.post(id + "/visits/", payload);

        return Response.ok(body).build();
    }

    @ApiOperation(value = "Search Customers", notes = "Search customers by name, plate, or VIN")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "An array of matching customers", response = Customer.class, responseContainer = "List")
    })
    @POST
    @Path("/search")
    public Response searchCustomers(SearchFilter filter) throws Exception {

        byte[] payload = objectMapper.writeValueAsBytes(filter);

        String body = httpRequestUtil.post("_search", payload);

        return Response.ok(body).build();
    }

    /**
     * Check and Fix Secure gateway bridge
     *
     * @throws IOException
     */
    private void validateSecureGatewayBridge(AdaptersAPI adaptersAPI) throws IOException {
        logger.info("validateSecureGatewayBridge:");
        HttpUriRequest req = new HttpGet("/SecureGatewayAdapter/secure/updateFirewall");
        req.addHeader("Accept", "text/plain");
        HttpResponse response = adaptersAPI.executeAdapterRequest(req);
        logger.info("validateSecureGatewayBridge: done" + response.toString());
    }
}
