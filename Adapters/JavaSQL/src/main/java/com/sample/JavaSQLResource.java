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


@Path("/")
public class JavaSQLResource {
	/*
	 * For more info on JAX-RS see https://jax-rs-spec.java.net/nonav/2.0-rev-a/apidocs/index.html
	 */

	@Context
	ConfigurationAPI configurationAPI;

	@Context
	AdaptersAPI adaptersAPI;

	public Connection getSQLConnection() throws SQLException{
		// Create a connection object to the database
		JavaSQLApplication app = adaptersAPI.getJaxRsApplication(JavaSQLApplication.class);
		return app.dataSource.getConnection();
	}


	@POST
	public Response createUser(@FormParam("userId") String userId,
								@FormParam("firstName") String firstName,
								@FormParam("lastName") String lastName,
								@FormParam("password") String password)
										throws SQLException{

		Connection con = getSQLConnection();
		PreparedStatement insertUser = con.prepareStatement("INSERT INTO users (userId, firstName, lastName, password) VALUES (?,?,?,?)");

		try{
			insertUser.setString(1, userId);
			insertUser.setString(2, firstName);
			insertUser.setString(3, lastName);
			insertUser.setString(4, password);
			insertUser.executeUpdate();
			//Return a 200 OK
			return Response.ok().build();
		}
		catch (Exception E) {
			//Trying to create a user that already exists
			return Response.status(Status.CONFLICT).entity(E.getMessage()).build();
		}
		finally{
			//Close resources in all cases
			insertUser.close();
			con.close();
		}


	}

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

	@GET
	@OAuthSecurity(enabled=false)
	@Produces("application/json")
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

	@PUT
	@Path("/{userId}")
	public Response updateUser(@PathParam("userId") String userId,
								@FormParam("firstName") String firstName,
								@FormParam("lastName") String lastName,
								@FormParam("password") String password)
										throws SQLException{
		Connection con = getSQLConnection();
		PreparedStatement getUser = con.prepareStatement("SELECT * FROM CUSTOMERS WHERE 'CustomerID' = ?");

		try{
			getUser.setString(1, userId);
			ResultSet data = getUser.executeQuery();

			if(data.first()){
				PreparedStatement updateUser = con.prepareStatement("UPDATE users SET firstName = ?, lastName = ?, password = ? WHERE userId = ?");

				updateUser.setString(1, firstName);
				updateUser.setString(2, lastName);
				updateUser.setString(3, password);
				updateUser.setString(4, userId);

				updateUser.executeUpdate();
				updateUser.close();
				return Response.ok().build();


			} else{
				return Response.status(Status.NOT_FOUND).entity("User not found...").build();
			}
		}
		finally{
			//Close resources in all cases
			getUser.close();
			con.close();
		}

	}

	@DELETE
	@Path("/{userId}")
	public Response deleteUser(@PathParam("userId") String userId) throws SQLException{
		Connection con = getSQLConnection();
		PreparedStatement getUser = con.prepareStatement("SELECT * FROM users WHERE userId = ?");

		try{
			getUser.setString(1, userId);
			ResultSet data = getUser.executeQuery();

			if(data.first()){
				PreparedStatement deleteUser = con.prepareStatement("DELETE FROM users WHERE userId = ?");
				deleteUser.setString(1, userId);
				deleteUser.executeUpdate();
				deleteUser.close();
				return Response.ok().build();

			} else{
				return Response.status(Status.NOT_FOUND).entity("User not found...").build();
			}
		}
		finally{
			//Close resources in all cases
			getUser.close();
			con.close();
		}

	}

}
