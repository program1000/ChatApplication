package chat.request;

public class CreateGroupRequest {
	
	private String name;
	
	public CreateGroupRequest( String name ) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}
