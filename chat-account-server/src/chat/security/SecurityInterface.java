package chat.security;

import chat.model.User;

public interface SecurityInterface {
    
    public String generateSessionToken();
    
    public String generateContactInviteToken( User user );
    
    public int getContactIdfromInviteToken( String token );
    
    public String generateContactId( User user, int contactId );

}
