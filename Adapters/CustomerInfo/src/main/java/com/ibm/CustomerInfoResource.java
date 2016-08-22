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
@Produces(MediaType.TEXT_PLAIN)
@Path("/customers")
public class CustomerInfoResource {

    private static CloseableHttpClient client;
	private static HttpHost host;


    public static void init() {
      client = HttpClientBuilder.create().build();
      host = new HttpHost("mobilefirstplatform.ibmcloud.com");
    }
    
    WLServerAPI api = WLServerAPIProvider.getWLServerAPI();
    
    /*
	 * Path for method:
	 * "<server address>/mfp/api/adapters/teste/resource/unprotected"
	 */

	@ApiOperation(value = "Unprotected Resource", notes = "Example of an unprotected resource, this resource is accessible without a valid token.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "A constant string is returned") })
	@GET
	@Path("/unprotected")
	@Produces(MediaType.TEXT_PLAIN)
	@OAuthSecurity(enabled = false)
	public String unprotected() {
		return "Hello from unprotected resource!";
	}
     
    /*
	 * Path for method:
	 * "<server address>/mfp/api/adapters/teste/resource/unprotected"
	 */

	@ApiOperation(value = "Unprotected Resource", notes = "Example of an unprotected resource, this resource is accessible without a valid token.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "A constant string is returned") })
	@GET
	@Path("/unprotectedGET")
	@Produces("application/json")
	@OAuthSecurity(enabled = false)
    public void get(@Context HttpServletResponse response, @QueryParam("tag") String tag)
        throws IOException, IllegalStateException, SAXException {
      if(tag!=null && !tag.isEmpty()){
        execute(new HttpGet("/blog/atom/"+ tag +".xml"), response);
      }
      else{
        execute(new HttpGet("/feed.xml"), response);
      }
    }
    
    
    	public void execute(HttpUriRequest req, HttpServletResponse resultResponse)
			throws IOException,
			IllegalStateException, SAXException {
		HttpResponse RSSResponse = client.execute(host, req);
		ServletOutputStream os = resultResponse.getOutputStream();
		if (RSSResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
			resultResponse.addHeader("Content-Type", "application/json");
			String json = XML.toJson(RSSResponse.getEntity().getContent());
			os.write(json.getBytes(Charset.forName("UTF-8")));

		}else{
			resultResponse.setStatus(RSSResponse.getStatusLine().getStatusCode());
			RSSResponse.getEntity().getContent().close();
			os.write(RSSResponse.getStatusLine().getReasonPhrase().getBytes());
		}
		os.flush();
		os.close();
	}

}
