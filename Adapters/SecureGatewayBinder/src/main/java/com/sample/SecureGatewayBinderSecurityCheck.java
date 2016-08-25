/**
 *
 */
package com.sample;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.ibm.json.java.JSONObject;
import com.ibm.mfp.security.checks.base.UserAuthenticationSecurityCheck;
import com.ibm.mfp.server.registration.external.model.AuthenticatedUser;
import com.ibm.mfp.server.security.external.checks.SecurityCheckConfiguration;


/**
 * @author cesarlb
 *
 */
public class SecureGatewayBinderSecurityCheck extends UserAuthenticationSecurityCheck {
	 	private String userId, displayName;
	    private String errorMsg;
	    private boolean rememberMe = false;

	    private static HttpHost host;
	    private static CloseableHttpClient client;

	    
	    static Logger logger = Logger.getLogger(SecureGatewayBinderSecurityCheck.class.getName());
	    @Override
	    protected AuthenticatedUser createUser() {
	        return new AuthenticatedUser(userId, displayName, this.getName());
	    }

	    @Override
	    protected boolean validateCredentials(Map<String, Object> credentials) {
				  
	    	host = new HttpHost("sgmanager.ng.bluemix.net", 443, "https");
	    	logger.info("CF_INSTANCE_IP:" + System.getenv("CF_INSTANCE_IP"));
	    	logger.info("CF_INSTANCE_INDEX:" + System.getenv("CF_INSTANCE_INDEX"));
	    	logger.info("CF_INSTANCE_PORT:" + System.getenv("CF_INSTANCE_PORT"));
	    	logger.info("Security Token:" + getConfiguration().sgwToken);
	    	logger.info("gatewayId :" + getConfiguration().gatewayId);
	    	logger.info("destinationId:" + getConfiguration().destinationId);
	    	
	    	client = HttpClientBuilder.create().build();
	    	
	    	HttpPut put = new HttpPut("/v1/sgconfig/"+getConfiguration().gatewayId + "/destinations/"+ getConfiguration().destinationId + "/ipTableRule");
	    	put.addHeader("Content-Type", "application/json");
	    	put.addHeader("Authorization", "Bearer "+getConfiguration().sgwToken);
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
	            
	    		    	
	    	
	    	
	    	return true;				  				
	    }

	    @Override
	    protected Map<String, Object> createChallenge() {
	        Map challenge = new HashMap();
	        challenge.put("errorMsg",errorMsg);
	        challenge.put("remainingAttempts",getRemainingAttempts());
	        return challenge;
	    }

	    @Override
	    protected boolean rememberCreatedUser() {
	        return rememberMe;
	    }
	    
	    //get custom configs
	    @Override
	    protected SecureGatewayBinderConfig getConfiguration() {
	        return (SecureGatewayBinderConfig) super.getConfiguration();
	    }
	    @Override
	    public SecurityCheckConfiguration createConfiguration(Properties properties) {
	        return new SecureGatewayBinderConfig(properties);
	    }
}
