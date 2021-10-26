package chat.message.server.security;

import chat.message.server.model.Conversation;
import chat.message.server.model.User;

public class ZeroSecurity implements SecurityInterface{
    
    //private int conversationSeq;
    
    public ZeroSecurity() {
        //conversationSeq=0;
    }
    
    public String generateConversationId( Conversation conversation) {
        return "V"+conversation.getId();
    }
    
//    public int getConversationId( String conversationId ) {
//       return Integer.valueOf( conversationId.substring( 1 ) );
//    }
    
    public String generateUserId( User user ) {
        return "I"+user.getId();
    }
    
//    public int getUserId( String id ) {
//        return Integer.valueOf( id.substring( 1 ) );
//    }

}
