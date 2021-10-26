package chat.connector;

public class AccountServerConnectorStub extends AccountConnector{
    
    private String requestUser;
    private String requestPass;
    private String canAddUser;
    private String canAuthenticateUser;
    
    public AccountServerConnectorStub() {
        canAddUser = "1";
        canAuthenticateUser = "1";
    }
    
    public void setHasUser() {
        canAddUser = AccountConnectorInterface.NO_SESSION_ID;
    }
    
    public void setFailAuthenticateUser() {
        canAuthenticateUser = AccountConnectorInterface.NO_SESSION_ID;
    }
    
    public String addUser(String user, String pass) {
        requestUser = user;
        requestPass = pass;
        return canAddUser;
    }
    
    public String authenticateUser( String user, String pass) {
        requestUser = user;
        requestPass = pass;
        return canAuthenticateUser;
    }
    
    public String getUser() {
        return requestUser;
    }
    
    public String getPass() {
        return requestPass;
    }
    
    public int getUserIdFromSessionId( String sessionId ) {
        return 1;
    }

}
