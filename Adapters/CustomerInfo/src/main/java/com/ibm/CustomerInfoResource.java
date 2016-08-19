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

import org.apache.http.HttpHost;
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

import com.worklight.adapters.rest.api.WLServerAPI;
import com.worklight.adapters.rest.api.WLServerAPIProvider;


@OAuthSecurity(enabled=false)
@Api(value = "Sample Adapter Resource")
@Path("/customers")
public class CustomerInfoResource {
    
    WLServerAPI api = WLServerAPIProvider.getWLServerAPI();
   	
    @ApiOperation(value = "Query Parameter Example", notes = "Example of passing query parameters to a resource. Returns a greeting containing the name that was passed in the query parameter.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Greeting message returned") })
	@GET
	@Produces("application/json")
    public String getCustomers(){
    
        HttpClientBuilder builder = HttpClientBuilder.create();
        HttpClient httpClient = builder.build();


        //String credentials =  "basic-auth-username:password";

        //HttpGet get = new HttpGet("https://localhost:8080/customers");
        HttpGet get = new HttpGet("");
        get.addHeader("Content-Type","application/json");
        //get.addHeader("Authorization", "Basic " + new String(Base64.encodeBase64(credentials.getBytes())));

        try {
            httpClient.execute(get);
        } catch (IOException ignored) {}
        
        return "holla";
    }
}
