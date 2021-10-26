package chat.connector;

import chat.request.AuthenticateRequest;
import chat.request.NewUserRequest;

public class AccountConnector {
    
    private AccountConnectorInterface accountConnector;
    
    public void setAccountConnector( AccountConnectorInterface connector ) {
        accountConnector = connector;
    }
    
    public boolean isInited() {
        return accountConnector!=null;
    }
    
    public String addUser(String user, String pass) {
        NewUserRequest request = new NewUserRequest( user, pass );
        return accountConnector.createUser( request );
    }
    
    public String authenticateUser( String user, String pass ) {
        AuthenticateRequest request = new AuthenticateRequest( user, pass );
        return accountConnector.authenticate( request );
    }
    
    public int getUserIdFromSessionId( String sessionId ) {
        return accountConnector.getUserIdFromSessionId( sessionId );
    }

}
