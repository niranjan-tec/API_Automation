package payloads;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.github.javafaker.Faker;

import pojo.Address;
import pojo.Cart;
import pojo.CartProduct;
import pojo.Geolocation;
import pojo.Login;
import pojo.Name;
import pojo.Product;
import pojo.User;

public class Payload {

	private static final Faker faker=new Faker();
	private static final String categories[]= {"electronics", "furniture", "clothing", "books", "beauty"};
	
	private static final Random random=new Random();
	
		
	//Product
	public static Product productPayload()
	{
		String name=faker.commerce().productName();
		double price=Double.parseDouble(faker.commerce().price());
		String description=faker.lorem().sentence();
		String imageUrl="https://i.pravatar.cc/100";
		String category=categories[random.nextInt(categories.length)];
		
		new Product(name, price, description, imageUrl, category);
		return new Product(name, price, description, imageUrl, category);
	}
	
	//User
	
	public static User userPayload()
	{
		//first name and last name
		String firstName=faker.name().firstName();
		String lastName=faker.name().lastName();		
		Name name=new Name(firstName, lastName);
		
		//address and geolocation
		String lat=faker.address().latitude();
		String lng=faker.address().longitude();
		Geolocation location=new Geolocation(lat, lng);
		
		String city=faker.address().city();
		String street=faker.address().streetName();
		int number=faker.number().numberBetween(1, 100);
		String zipcode=faker.address().zipCode();
		Address address=new Address(city, street, number, zipcode, location);
		
		//user details
		String email=faker.internet().emailAddress();
		String username=faker.name().username();
		String password=faker.internet().password(8, 16);
		String phone=faker.phoneNumber().cellPhone();
		
		return new User(email, username, password, name, address, phone);
		
		
		
		 
	}

	//Cart
	public static Cart cartPayload(int userId) {
        List<CartProduct> products = new ArrayList<>();
        
        
        // Adding one random product to the cart
        int productId = random.nextInt(100);
        int quantity = random.nextInt(10) + 1;
               
        CartProduct cartProduct= new CartProduct(productId, quantity);
        products.add(cartProduct);

        
        //new Date()  ----> Returns date like  Wed Feb 19 13:17:45 IST 202
        // We need to convert this to "yyyy-MM-dd" format in String 
        
         SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);// Define output date format
         String date = outputFormat.format(new Date());//Converting to String
	    
        return new Cart(userId, date, products);
    }
	
	
	//Login-Authentication
	
	
	public static Login loginPayload()
	{
		
		String username=faker.name().username();
		String password=faker.internet().password();		
		Login login= new Login(username, password);
		return login;		
		
	}
}