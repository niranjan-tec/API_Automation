package testcases;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.testng.annotations.Test;

import io.restassured.module.jsv.JsonSchemaValidator;
import routes.Routes;

public class SchemaTest extends BaseClass {
	
	//Test to validate the schema of the user response
	
	@Test(priority=1)
	public void testUserSchema()
	{
		String userId = configReader.getProperty("userId");
		
		given()
		 .pathParam("id", userId)
		 .when()
		  .get(Routes.GET_USER_BY_ID)
		.then()
		//Validate user schema
		 .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("userSchema.json"));
			
	}
	
	//Test to validate the schema of the product response
	
	@Test(priority=2)
	    void testProductSchema() {    
	    
	        int productID=configReader.getIntProperty("productId");
	        
	         given()
	         .pathParam("id",productID)
	        .when()
	         .get(Routes.GET_PRODUCT_BY_ID)
	        .then()
	         .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("productSchema.json"));
	         
	    }
	 
	 //Test to validate the schema of the cart response
		@Test(priority=3)
	 	 public void testGetCartById() {
	     	int cartId = configReader.getIntProperty("cartId");
	         given()
	             .pathParam("id", cartId)
	             .when()
	                 .get(Routes.GET_CART_BY_ID)
	             .then()
	                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("cartSchema.json"));
	              
	     }
	
	

}
