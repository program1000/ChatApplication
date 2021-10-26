package chat.request;

public class AuthenticateRequest {

    private String user;
    private String pass;

    public AuthenticateRequest( String user, String pass ) {
        this.user = user;
        this.pass = pass;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

}
