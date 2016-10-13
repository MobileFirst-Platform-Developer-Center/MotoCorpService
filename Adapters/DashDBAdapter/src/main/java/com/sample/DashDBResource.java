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

package com.sample;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;
import com.ibm.mfp.adapter.api.AdaptersAPI;
import com.ibm.mfp.adapter.api.ConfigurationAPI;
import com.ibm.mfp.adapter.api.OAuthSecurity;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.sql.*;
import java.util.Objects;

@Path("/")
public class DashDBResource {
	/*
	 * For more info on JAX-RS see https://jax-rs-spec.java.net/nonav/2.0-rev-a/apidocs/index.html
	 */

	@Context
	ConfigurationAPI configurationAPI;

	@Context
	AdaptersAPI adaptersAPI;

	public Connection getSQLConnection() throws SQLException{
		// Create a connection object to the database
		DashDBApplication app = adaptersAPI.getJaxRsApplication(DashDBApplication.class);
		return app.dataSource.getConnection();
	}

	// This gets a customer by a customer ID
	@GET
	@OAuthSecurity(enabled=false)
	@Produces("application/json")
	@Path("/{userId}")
	public Response getUser(@PathParam("userId") String userId) throws SQLException{
		Connection con = getSQLConnection();
		PreparedStatement getUser = con.prepareStatement("SELECT * FROM CUSTOMERS WHERE \"CustomerID\" = ?");

		try{
			JSONObject result = new JSONObject();

			getUser.setInt(1, Integer.parseInt(userId));
			ResultSet data = getUser.executeQuery();

			if(data.next()){
				result.put("CustomerID", data.getInt("CustomerID"));
				result.put("Name", data.getString("Name"));
				result.put("LicensePlate", data.getString("LicensePlate"));
				result.put("Make", data.getString("Make"));
				result.put("Model", data.getString("Model"));
				result.put("Vin", data.getString("Vin"));

				return Response.ok(result).build();

			} else{
				return Response.status(Status.NOT_FOUND).entity("User not found...").build();
			}

		}
		catch(Exception E) {
			E.printStackTrace();
		}
		finally{
			//Close resources in all cases
			getUser.close();
			con.close();
		}
		return Response.ok().build();
	}

	// This gets all customers
	@GET
	@OAuthSecurity(enabled=false)
	@Produces("application/json")
	@Path("/getAllUsers")
	public Response getAllUsers() throws SQLException{
		JSONArray results = new JSONArray();
		Connection con = getSQLConnection();
		PreparedStatement getAllUsers = con.prepareStatement("SELECT * FROM CUSTOMERS");
		ResultSet data = getAllUsers.executeQuery();

		while(data.next()){
			JSONObject item = new JSONObject();
			item.put("CustomerID", data.getInt("CustomerID"));
			item.put("Name", data.getString("Name"));
			item.put("LicensePlate", data.getString("LicensePlate"));
			item.put("Make", data.getString("Make"));
			item.put("Model", data.getString("Model"));
			item.put("VIN", data.getString("VIN"));

			results.add(item);
		}

		getAllUsers.close();
		con.close();

		return Response.ok(results).build();
	}

	// [In-Progress][GET] Get customer by licenseplate with all related visits
	@GET
	@OAuthSecurity(enabled=false)
	@Produces("application/json")
	@Path("/{plate}/Customer")
	public Response getUserWithVisits(@PathParam("plate") String plate) throws SQLException{
		Integer customerId = -1;

		Connection con = getSQLConnection();

		// get customers
		PreparedStatement getUser = con.prepareStatement("SELECT * FROM CUSTOMERS WHERE \"LicensePlate\" = ?");

		try{
			JSONObject result = new JSONObject();

			getUser.setString(1, plate);
			ResultSet data = getUser.executeQuery();

			if(data.next()){
				customerId = data.getInt("CustomerId");

				result.put("CustomerID", data.getInt("CustomerID"));
				result.put("Name", data.getString("Name"));
				result.put("LicensePlate", data.getString("LicensePlate"));
				result.put("Make", data.getString("Make"));
				result.put("Model", data.getString("Model"));
				result.put("Vin", data.getString("Vin"));

				// get visits
				JSONArray customerVisits = new JSONArray();
				PreparedStatement getVisits = con.prepareStatement("Select \"Date\", \"Type\" , \"Comments\" from VISITS WHERE \"CustomerID\" = ?");
				getVisits.setInt(1, customerId);
				ResultSet visits = getVisits.executeQuery();

				while (visits.next()){

					// build each visit
						JSONObject visit = new JSONObject();
						visit.put("Date", visits.getString("Date"));
						visit.put("Type", visits.getString("Type"));
						visit.put("Comments", visits.getString("Comments") );
						customerVisits.add(visit);
				}
				result.put("Visits", customerVisits);

				return Response.ok(result).build();

			} else{
				return Response.status(Status.NOT_FOUND).entity("User information not found...").build();
			}

		}
		catch(Exception E) {
			E.printStackTrace();

		}
		finally{
			//Close resources in all cases
			getUser.close();
			con.close();
		}
		return Response.ok().build();
	}

	// [TODO][POST] Search customer using parameter passed
	@POST
	@OAuthSecurity(enabled=false)
	@Consumes("application/json")
	@Path("/customer")
	public Response searchCustomer(JSONObject msgPayload) throws SQLException{
//		{
//			"type":"name",
//			"text":"ABC-123"
//		}

		String type = msgPayload.get("type").toString();
		String text = msgPayload.get("text").toString();
		PreparedStatement searchCustomer;

		Connection con = getSQLConnection();

		//plate,name,vin -> returns name/licenseplate
		if (Objects.equals(type, new String("plate"))) {
			searchCustomer = con.prepareStatement("SELECT \"Name\",\"LicensePlate\" FROM CUSTOMERS WHERE \"LicensePlate\" = ?");

		} else if (Objects.equals(type, new String("VIN"))) {
			searchCustomer = con.prepareStatement("SELECT \"Name\",\"LicensePlate\" FROM CUSTOMERS WHERE \"VIN\" = ?");

		} else if (Objects.equals(type, new String("Name"))) {
			searchCustomer = con.prepareStatement("SELECT \"Name\",\"LicensePlate\" FROM CUSTOMERS WHERE \"Name\" = ?");
		} else {
			return Response.status(Status.NOT_FOUND).entity("User information not found...").build();
		}

		searchCustomer.setString(1, text);

		try{
			JSONObject result = new JSONObject();
			ResultSet data = searchCustomer.executeQuery();

			if(data.next()){
				result.put("Name", data.getString("Name"));
				result.put("LicensePlate", data.getString("LicensePlate"));
				return Response.ok(result).build();
			} else{
				return Response.status(Status.NOT_FOUND).entity("User information not found...").build();
			}

		}
		catch(Exception E) {
			E.printStackTrace();

		}
		finally{
			searchCustomer.close();
			con.close();
		}
		return Response.ok().build();
	}
}
