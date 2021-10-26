package chat.connector;

import java.util.List;

import chat.request.NewContactRequest;
import chat.request.RemoveContactRequest;

public class ContactConnector {
    
    private ContactConnectorInterface contactConnector;
    
    public void setContactConnector( ContactConnectorInterface connector ) {
        contactConnector = connector;
    }
    
    public boolean isInited() {
        return contactConnector!=null;
    }
    
    public String getContactId( String sessionId ) {
        return contactConnector.createContactInvite( sessionId );
    }
    
    public List<String> addContact( String sessionId, String contactTokenId ) {
        NewContactRequest request = new NewContactRequest( contactTokenId );
        return contactConnector.addContact( sessionId, request );
    }
    
    public boolean delContact( String sessionId, int contactId ) {
        RemoveContactRequest request = new RemoveContactRequest( contactId );
        return contactConnector.delContact( sessionId, request );
    }
    
    public List<String> getContacts( String sessionId ) {
        return contactConnector.getContacts( sessionId );
    }
    
    public String getGeneratedContactId( String sessionId, int contactId ) {
        return contactConnector.getGeneratedContactId( sessionId, contactId );
    }
    
    public List<String> getGeneratedContactIds( String sessionId, List<Integer> contactList ) {
        return contactConnector.getGeneratedContactIds( sessionId, contactList );
    }

}
