package chat.message.server.connector;

import chat.request.NewConversationUserRequest;

public class MessageServerAccountConnector {
    
    private MessageServerAccountConnectorInterface accountConnector;
    
    
    public void setAccountConnector( MessageServerAccountConnectorInterface accountConnector ) {
        this.accountConnector = accountConnector;
    }


    public boolean addUser(int userId) {
        NewConversationUserRequest request = new NewConversationUserRequest( userId );
        return accountConnector.addUser( request );
    }

}
