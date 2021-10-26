package chat.request;

public class NewUserRequest {
	private String user;
	private String pass;
	
	public String getUser() {
		return user;
	}

	public String getPass() {
		return pass;
	}

	public NewUserRequest(String user, String pass) {
		this.user = user;
		this.pass = pass;
	}
}
