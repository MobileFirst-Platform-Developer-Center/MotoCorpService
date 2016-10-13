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

import com.ibm.mfp.adapter.api.ConfigurationAPI;
import com.ibm.mfp.adapter.api.OAuthSecurity;

import com.ibm.mfp.adapter.api.AdaptersAPI;
import com.ibm.json.java.JSONObject;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import java.io.IOException;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.util.EntityUtils;
import javax.ws.rs.*;


@Api(value = "Sample Adapter Resource")
@Path("/resource")
public class CustomerAdapterResource {
	/*
	 * For more info on JAX-RS see
	 * https://jax-rs-spec.java.net/nonav/2.0-rev-a/apidocs/index.html
	 */

	// Define logger (Standard java.util.Logger)
	static Logger logger = Logger.getLogger(CustomerAdapterResource.class.getName());

	// Inject the MFP configuration API:
	@Context
	ConfigurationAPI configApi;

	@Context
	AdaptersAPI adaptersAPI;

	/*
	 * Path for method:
	 * "<server address>/mfp/api/adapters/CustomerAdapter/resource"
	 */

	@ApiOperation(value = "Returns 'Hello from resource'", notes = "A basic example of a resource returning a constant string.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Hello message returned") })
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getResourceData() {
		// log message to server log
		logger.info("Logging info message...");

		return "Hello from resource";
	}


	/*
	 * Path for method:
	 * "<server address>/mfp/api/adapters/CustomerAdapter/resource/customers"
	 */

	 // Calls the SQL Adapter to get all customers
	@ApiOperation(value = "Unprotected Resource", notes = "Example of an unprotected resource, this resource is accessible without a valid token.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "A constant string is returned") })
	@GET
	@Path("/customers")
	@Produces(MediaType.APPLICATION_JSON)
	@OAuthSecurity(enabled = false)
	public Response customers()  throws IOException{

		String JavaSQLURL = "/DashDB/getAllUsers";
		HttpUriRequest req = new HttpGet(JavaSQLURL);
		HttpResponse response = adaptersAPI.executeAdapterRequest(req);
		JSONObject jsonObj = adaptersAPI.getResponseAsJSON(response);

		return Response.ok(jsonObj.get("responseText")).build();
	}


	/*
	 * Path for method:
	 * "<server address>/mfp/api/adapters/CustomerAdapter/resource/newCustomer"
	 */

	 // POST a new customer to MessageHub
	@ApiOperation(value = "Unprotected Resource", notes = "Example of an unprotected resource, this resource is accessible without a valid token.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "A constant string is returned") })
	@POST
	@Consumes("application/json")
	@Path("/newCustomer")
	@OAuthSecurity(enabled = false)
	public Response newCustomer(JSONObject customer) throws IOException{

		String MessageHubURL = "/MessageHubAdapter/resource/sendMessage";
		HttpPost req = new HttpPost(MessageHubURL);
		req.addHeader("Content-Type", "application/json");

		String payload = customer.toString();
		HttpEntity entity = new ByteArrayEntity(payload.getBytes("UTF-8"));
        req.setEntity(entity);

		HttpResponse response = adaptersAPI.executeAdapterRequest(req);
		JSONObject jsonObj = adaptersAPI.getResponseAsJSON(response);

		return Response.ok(jsonObj.get("responseText")).build();
	}

	/*
	 * Path for method:
	 * "<server address>/mfp/api/adapters/CustomerAdapter/resource/newVisit"
	 */

	 // POST a new visit to MessageHub
	@ApiOperation(value = "Unprotected Resource", notes = "Example of an unprotected resource, this resource is accessible without a valid token.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "A constant string is returned") })
	@POST
	@Consumes("application/json")
    @Path("/{id}/newVisit")
	@OAuthSecurity(enabled = false)
	public Response newVisit(JSONObject visit, Integer id) throws IOException{

        String MessageHubURL = "/MessageHubAdapter/resource/{id}/newVisit";
		HttpPost req = new HttpPost(MessageHubURL);
		req.addHeader("Content-Type", "application/json");

		String payload = visit.toString();
		HttpEntity entity = new ByteArrayEntity(payload.getBytes("UTF-8"));
        req.setEntity(entity);

		HttpResponse response = adaptersAPI.executeAdapterRequest(req);
		JSONObject jsonObj = adaptersAPI.getResponseAsJSON(response);

		return Response.ok(jsonObj.get("responseText")).build();
	}


    //TODO
    /*
	 * Path for method:
	 * "<server address>/mfp/api/adapters/CustomerAdapter/resource/searchCustomer"
	 */

     // POST a to do a search on DashDB
	@ApiOperation(value = "Unprotected Resource", notes = "Example of an unprotected resource, this resource is accessible without a valid token.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "A constant string is returned") })
	@POST
	@Path("/searchCustomer")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	@OAuthSecurity(enabled = false)
	public Response searchCustomer(JSONObject searchPayload)  throws IOException{

		String JavaSQLURL = "/DashDB/customer";
		HttpPost req = new HttpPost(JavaSQLURL);
		req.addHeader("Content-Type", "application/json");
		
		String payload = searchPayload.toString();
		HttpEntity entity = new ByteArrayEntity(payload.getBytes("UTF-8"));
        req.setEntity(entity);

		HttpResponse response = adaptersAPI.executeAdapterRequest(req);
		JSONObject jsonObj = adaptersAPI.getResponseAsJSON(response);

		return Response.ok(jsonObj.get("responseText")).build();
	}
    
    //TODO
    /*
	 * Path for method:
	 * "<server address>/mfp/api/adapters/CustomerAdapter/resource/{id}/getCustomerInfo"
	 */

    // GET customer payload by id with all customer info and visits
    // {id}/getCustomerInfo
}
