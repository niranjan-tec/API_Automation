package testcases;

import org.testng.annotations.Test;

import io.restassured.response.Response;
import pojo.Product;
import routes.Routes;
import utils.ConfigReader;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;


public class ProductTest extends BaseClass {
    int productID;
    
    /**
     * Verify that the GET /products endpoint returns HTTP 200 and a non-empty list.
     * Input: none
     * Expected output: status 200 and body size() > 0
     * Side-effects: none
     */
    @Test(priority=1)
    void testGetAllProducts() {
        
         given()
         
        .when()
         .get(Routes.GET_ALL_PRODUCTS)
        .then()
         .statusCode(200)
         .body("size()", greaterThan(0))
         .log().all();
        
    }
    
    /**
     * Verify GET /products/{id} returns the correct product when queried with an ID from config.
     * Input: productId read from configuration (configReader)
     * Expected output: status 200 and the returned "id" equals the requested id
     * Side-effects: none
     */
    @Test(priority=2)
    void testGetSingleProductById() {    
    
        int productID=configReader.getIntProperty("productId");
        
         given()
         .pathParam("id",productID)
        .when()
         .get(Routes.GET_PRODUCT_BY_ID)
        .then()
         .statusCode(200)
         .body("id", equalTo(productID))
         .log().all();
         
    }
    
    /**
     * Verify GET /products?limit={limit} returns exactly `limit` products.
     * Input: limit read from configuration (productLimit)
     * Expected output: status 200 and response list size equals limit
     * Side-effects: none
     */
    @Test(priority=3)
    	void testGetProductsWithLimit() {
        
        int limit=configReader.getIntProperty("productLimit");
        
         given()
         .pathParam("limit",limit)
         .when()
         .get(Routes.GET_PRODUCTS_WITH_LIMIT)
         .then()
         .statusCode(200)
         .body("size()", equalTo(limit))
         .log().all();
         
         
        }
    
    /**
     * Retrieve products sorted in descending order and verify the product IDs are in descending order.
     * Input: path parameter order=desc
     * Expected output: status 200 and IDs sorted in descending order according to helper `isSortedDesceding()`
     * Side-effects: none
     */
    @Test(priority=4)
    	public void testGetSortedProducts()
    	{
    		Response response=given()
    			.pathParam("order", "desc")
    		.when()
    			.get(Routes.GET_PRODUCTS_SORTED)
    		.then()
    			.statusCode(200)
    			.extract().response();
    		
    		List<Integer> productIds=response.jsonPath().getList("id", Integer.class);
    		 assertThat(isSortedDesceding(productIds), is(true));
    		 System.out.println("Product IDs in Descending order: " + productIds);
    	}
    
    
    	
    	/**
    	 * Retrieve products sorted in ascending order and verify the product IDs are in ascending order.
    	 * Input: path parameter order=asc
    	 * Expected output: status 200 and IDs sorted in ascending order according to helper `isSortedAsceding()`
    	 * Side-effects: none
    	 */
    @Test(priority=5)
    		public void testGetSortedProductsAsc()
    		{
    			Response response=given()
    				.pathParam("order", "asc")
    			.when()
    				.get(Routes.GET_PRODUCTS_SORTED)
    			.then()
    				.statusCode(200)
    				
    				.extract().response();
    			
    			List<Integer> productIds=response.jsonPath().getList("id", Integer.class);
    			 assertThat(isSortedAsceding(productIds), is(true));
    			 System.out.println("Product IDs in Ascending order: " + productIds);
    		}
    
    	/**
    	 * Verify GET /products/categories returns a non-empty list of categories.
    	 * Input: none
    	 * Expected output: status 200 and list size > 0
    	 * Side-effects: none
    	 */
    @Test(priority=6)
    		public void testGetAllCategories()
    		{
    			given()
    			.when()
    			 .get(Routes.GET_ALL_CATEGORIES)
    			.then()
    			 .statusCode(200)
    			 .body("size()", greaterThan(0))
    			 .log().all();
    		}
    		
    	/**
    	 * Verify GET /products/category/{category} returns products only from that category.
    	 * Input: category path parameter (here: "electronics")
    	 * Expected output: status 200, non-empty list, and every item's "category" equals the requested category
    	 * Side-effects: none
    	 */
    	@Test(priority=7)
    			public void testGetProductsByCategory()
    			{
    						
    				given()
    				 .pathParam("category", "electronics")
    				.when()
    				 .get(Routes.GET_PRODUCTS_BY_CATEGORY)
    				.then()
    				 .statusCode(200)
    				 .body("size()", greaterThan(0))
    				 .body("category", everyItem(equalTo("electronics")))
    				 .log().all();
    			}
    		
    	/**
    	 * Create a new product using a payload from `payloads.Payload.productPayload()`.
    	 * Input: Product object from payload builder
    	 * Expected output: status 201 and returned product fields match the payload
    	 * Side-effects: creates a product on the server and stores its id in `productID` field for later tests
    	 */
    	@Test(priority=8)
    			public void testCreateProduct()
    			{
    				Product productPayload=payloads.Payload.productPayload();
                productID =given()
    				 .header("Content-Type", "application/json")
    				 .body(productPayload)
    				 .when()
    				 .post(Routes.CREATE_PRODUCT)
    				 .then()
    				 .statusCode(201)
    				 .body("title", equalTo(productPayload.getTitle()))
    				 .body("price", equalTo((float)productPayload.getPrice()))
    				 .body("description", equalTo(productPayload.getDescription()))
    				 .body("image", equalTo(productPayload.getImage()))
    				 .body("category", equalTo(productPayload.getCategory()))
    				 .log().all()
    				 .extract().jsonPath().getInt("id");
                
                   	System.out.println("Created Product ID: " + productID);
    				
    			}
    			
    			
    		/**
    		 * Update the previously-created product using `productID`.
    		 * Input: productID (path param) and product payload
    		 * Expected output: status 200 and returned fields match the payload
    		 * Side-effects: modifies the product on the server
    		 */
    	@Test(priority=9)
    		public void testUpdateProduct()
    			{
    				Product productPayload=payloads.Payload.productPayload();
    				
    				given()
    				 .header("Content-Type", "application/json")
    				 .pathParam("id", productID)
    				 .body(productPayload)
    				 .when()
    				 .put(Routes.UPDATE_PRODUCT)
    				 .then()
    				 .statusCode(200)
    				 .body("title", equalTo(productPayload.getTitle()))
    				 .body("price", equalTo((float)productPayload.getPrice()))
    				 .body("description", equalTo(productPayload.getDescription()))
    				 .body("image", equalTo(productPayload.getImage()))
    				 .body("category", equalTo(productPayload.getCategory()))
    				 .log().all();
    			}
    	  
    	/**
    	 * Delete the product created earlier using `productID` and verify HTTP 200.
    	 * Input: productID (path param)
    	 * Expected output: status 200
    	 * Side-effects: removes the product from the server
    	 */
    @Test(priority=10)
    			public void testDeleteProduct() {
    				
    				given()
    				 .pathParam("id", productID)
    				.when()
    				 .delete(Routes.DELETE_PRODUCT)
    				.then()
    				 .statusCode(200)
    				 .log().all();
    	    }
    				
}