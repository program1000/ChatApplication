package chat.security;

import chat.model.User;

public class ZeroSecurity implements SecurityInterface{
    
    private int sessionIdSeq;
    
    public ZeroSecurity() {
        sessionIdSeq=1;
    }

    @Override
    public String generateSessionToken() {
        return Integer.toString( sessionIdSeq++ );
    }
    
    public String generateContactInviteToken( User user ) {
        return "C"+user.getId();
    }
    
    public int getContactIdfromInviteToken( String token ) {
        return Integer.valueOf( token.substring( 1 ) );
    }
    
    public String generateContactId( User user, int contactId ) {
        return "U"+user.getId()+"C"+contactId;
    }
    
//    public int getContactId( User user, String contactId ) {
//        int index = Integer.toString( user.getId()).length()+2;
//        return Integer.valueOf( contactId.substring( index ) );
//    }

}
