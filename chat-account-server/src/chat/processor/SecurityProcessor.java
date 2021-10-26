package chat.processor;

import chat.model.User;
import chat.security.SecurityInterface;
import chat.security.ZeroSecurity;

public class SecurityProcessor implements SecurityInterface{
    
    private SecurityInterface handler;
    
    public SecurityProcessor() {
        handler = new ZeroSecurity();
    }
    
    public SecurityProcessor( SecurityInterface handler) {
        this.handler = handler;
    }

    @Override
    public String generateSessionToken() {
        return handler.generateSessionToken();
    }
    
    @Override
    public String generateContactInviteToken( User user ) {
        return handler.generateContactInviteToken( user );
    }
    
    @Override
    public int getContactIdfromInviteToken( String token ) {
        return handler.getContactIdfromInviteToken( token );
    }

    @Override
    public String generateContactId( User user, int contactId ) {
        return handler.generateContactId( user, contactId );
    }

}
