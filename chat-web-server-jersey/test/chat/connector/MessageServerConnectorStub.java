package chat.connector;

import chat.message.server.connector.MessageServerAccountConnector;

public class MessageServerConnectorStub extends MessageServerAccountConnector {
    
    private boolean canAddUser;
    private int userId;
    
    public MessageServerConnectorStub() {
        canAddUser = true;
    }
    
    public boolean addUser(int userId) {
        this.userId = userId;
        return canAddUser;
    }
    
    public int getUserId() {
        return userId;
    }

}
