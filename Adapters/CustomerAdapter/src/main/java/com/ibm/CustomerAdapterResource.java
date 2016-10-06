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
	 * "<server address>/mfp/api/adapters/CustomerAdapter/resource/greet/{name}"
	 */

	@ApiOperation(value = "Query Parameter Example", notes = "Example of passing query parameters to a resource. Returns a greeting containing the name that was passed in the query parameter.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Greeting message returned") })
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/greet")
	public String helloUser(
			@ApiParam(value = "Name of the person to greet", required = true) @QueryParam("name") String name) {
		return "Hello " + name + "!";
	}

	/*
	 * Path for method:
	 * "<server address>/mfp/api/adapters/CustomerAdapter/resource/{path}/"
	 */

	@ApiOperation(value = "Multiple Parameter Types Example", notes = "Example of passing parameters using 3 different methods: path parameters, headers, and form parameters. A JSON object containing all the received parameters is returned.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "A JSON object containing all the received parameters returned.") })
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{path}")
	public Map<String, String> enterInfo(
			@ApiParam(value = "The value to be passed as a path parameter", required = true) @PathParam("path") String path,
			@ApiParam(value = "The value to be passed as a header", required = true) @HeaderParam("Header") String header,
			@ApiParam(value = "The value to be passed as a form parameter", required = true) @FormParam("form") String form) {
		Map<String, String> result = new HashMap<String, String>();

		result.put("path", path);
		result.put("header", header);
		result.put("form", form);

		return result;
	}

	/*
	 * Path for method:
	 * "<server address>/mfp/api/adapters/CustomerAdapter/resource/prop"
	 */

	@ApiOperation(value = "Configuration Example", notes = "Example usage of the configuration API. A property name is read from the query parameter, and the value corresponding to that property name is returned.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Property value returned."),
			@ApiResponse(code = 404, message = "Property value not found.") })
	@GET
	@Path("/prop")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getPropertyValue(
			@ApiParam(value = "The name of the property to lookup", required = true) @QueryParam("propertyName") String propertyName) {
		// Get the value of the property:
		String value = configApi.getPropertyValue(propertyName);
		if (value != null) {
			// return the value:
			return Response
					.ok("The value of " + propertyName + " is: " + value)
					.build();
		} else {
			return Response.status(Status.NOT_FOUND)
					.entity("No value for " + propertyName + ".").build();
		}

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

		String JavaSQLURL = "/JavaSQL/getAllUsers";
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
	@Path("/newVisit")
	@OAuthSecurity(enabled = false)
	public Response newVisit(JSONObject visit) throws IOException{

		String MessageHubURL = "/MessageHubAdapter/resource/sendMessage";
		HttpPost req = new HttpPost(MessageHubURL);
		req.addHeader("Content-Type", "application/json");

		String payload = visit.toString();
		HttpEntity entity = new ByteArrayEntity(payload.getBytes("UTF-8"));
        req.setEntity(entity);

		HttpResponse response = adaptersAPI.executeAdapterRequest(req);
		JSONObject jsonObj = adaptersAPI.getResponseAsJSON(response);

		return Response.ok(jsonObj.get("responseText")).build();
	}


}
