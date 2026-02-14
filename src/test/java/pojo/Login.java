package pojo;

public class Login {
	
	public String username;
	public String password;
	
	public Login(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	// Getters and Setters
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

}
