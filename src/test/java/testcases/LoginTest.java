package testcases;

import org.testng.annotations.Test;

import payloads.Payload;
import pojo.Login;
import routes.Routes;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class LoginTest extends BaseClass {
	
	@Test	
	public void testInvalidUserLogin() {
		
		Login login=Payload.loginPayload();
		
		given()
		    .contentType("application/json")
		    .body(login)		
		.when()
		    .post(Routes.Auth_Login)
		.then()
		    .log().all()
		   .statusCode(401)
		   .body(equalTo("username or password is incorrect"));		
		
	}
	
	@Test
	public void testValidUserLogin() {
		
		String username=configReader.getProperty("username");
		String password=configReader.getProperty("password");
		
		Login login=new Login(username, password);
		given()
		    .contentType("application/json")
		    .body(login)
		    .when()
		    .post(Routes.Auth_Login)
		    .then()
		    .log().all()
		    .statusCode(201)
		    .body("token", notNullValue());
		
	}

}
