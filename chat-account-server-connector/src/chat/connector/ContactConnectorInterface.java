package chat.connector;

import java.util.List;

import chat.request.NewContactRequest;
import chat.request.RemoveContactRequest;

public interface ContactConnectorInterface {
    
    final int NO_CONTACT_ID=-1;

    public String createContactInvite( String sessionId );
    
    public List<String> addContact( String sessionId, NewContactRequest request );
    
    public boolean delContact( String sessionId, RemoveContactRequest request );
    
    public List<String> getContacts( String sessionId );
    
    public String getGeneratedContactId( String sessionId, int contactId );
    
    public List<String> getGeneratedContactIds( String sessionId, List<Integer> contactList );
}
