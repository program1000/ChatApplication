package chat.connector;

public class ContactConnectorStub extends ContactConnector {
    
    
    
    public String getContactId( String sessionId ) {
        return sessionId+"1";
    }

}
