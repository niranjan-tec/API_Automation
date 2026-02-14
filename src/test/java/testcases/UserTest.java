package testcases;

import routes.Routes;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;

import io.restassured.response.Response;
import pojo.User;

public class UserTest extends BaseClass { 
	
	
	//GET_ALL_USERS
	
	@Test(priority=1)
	public void testGetAllUsers()
	{
		
		given()
		 .when()
		  .get(Routes.GET_ALL_USERS)
		.then()
		  .statusCode(200)
		  .body("size()", greaterThan(0))
		  .body("[0].id", notNullValue())
		  .log().all();
			
	}
	
	//GET_PRODUCT_BY_ID
	
	@Test(priority=2)
	public void testGetUserById()
	{
		String userId = configReader.getProperty("userId");
		
		given()
		 .pathParam("id", userId)
		 .when()
		  .get(Routes.GET_USER_BY_ID)
		.then()
		  .statusCode(200)
		  .body("id", equalTo(Integer.parseInt(userId)))
		  .body("email", notNullValue())
		  .body("username", notNullValue())
		  .log().all();
			
	}
	
	//GET_USERS_WITH_LIMIT
	
	@Test(priority=3)
	public void testGetUsersWithLimit()
	{
		String limit = configReader.getProperty("userLimit");
		
		given()
		 .pathParam("limit", limit)
		 .when()
		  .get(Routes.GET_USERS_WITH_LIMIT)
		.then()
		  .statusCode(200)
		  .body("size()", equalTo(Integer.parseInt(limit)))
		  .body("[0].id", notNullValue())
		  .log().all();
			
	}
		
    //GET_USERS_SORTED : Descending order by ID and verify the order is correct
	
	@Test(priority=4)
	public void testGetUsersSortedDesc()
	{
		
	Response response=  given()
		 .pathParam("order", "desc")
		 .when()
		  .get(Routes.GET_USERS_SORTED)
		.then()
		  .statusCode(200)
		  .body("size()", greaterThan(0))
		  .log().all()
	      .extract().response();
	   
	     List <Integer> userID = response.jsonPath().getList("id", Integer.class);
	     assertThat(isSortedDesceding(userID), is(true));
			
	}
	
	
	//GET_USERS_SORTED : Ascending order by ID and verify the order is correct
	
	@Test(priority=5)
	public void testGetUsersSortedAsc()
	{
		Response response=  given()
			 .pathParam("order", "asc")
			 .when()
			  .get(Routes.GET_USERS_SORTED)
			.then()
			  .statusCode(200)
			  .body("size()", greaterThan(0))
			  .log().all()
		      .extract().response();
		   
		     List <Integer> userID = response.jsonPath().getList("id", Integer.class);
		     assertThat(isSortedAsceding(userID), is(true));
	}
	
	//CREATE_USER
	@Test(priority=6)
	public void testCreateUser() {
		User payload = payloads.Payload.userPayload();
		
		int userId =given()
		   .header("Content-Type", "application/json")
		   .body(payload)
		.when()
		   .post(Routes.CREATE_USER)
		.then()
		   .statusCode(201)
		   .body("id", notNullValue())
		   .log().all()
		   .extract().jsonPath().getInt("id");
		
		System.out.println("Created User ID: " + userId);
	 }
	
	//UPDATE_USER
	
	@Test(priority=7)
	public void testUpdateUser() {
		String Id = configReader.getProperty("userId");
		User updateUser = payloads.Payload.userPayload();	
		given()
		   .header("Content-Type", "application/json")
		   .pathParam("id", Id)
		   .body(updateUser)
		.when()
		   .put(Routes.UPDATE_USER)
		.then()
		   .statusCode(200)
		   .body("username", equalTo(updateUser.getUsername()))
		   .body("email", equalTo(updateUser.getEmail()))
		   .log().all();
		  
	}
	
	//DELETE_USER
	@Test(priority=8)
	public void testDeleteUser() {
		String Id = configReader.getProperty("userId");
		
		given()
		 .pathParam("id", Id)
		 .when()
		  .delete(Routes.DELETE_USER)
		.then()
		  .statusCode(200)
		  .body("id", equalTo(Integer.parseInt(Id)))
		  .log().all();
			
	}
	
}
