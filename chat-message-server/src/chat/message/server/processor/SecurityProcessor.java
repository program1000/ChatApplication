package chat.message.server.processor;

import chat.message.server.model.Conversation;
import chat.message.server.model.User;
import chat.message.server.security.SecurityInterface;
import chat.message.server.security.ZeroSecurity;

public class SecurityProcessor implements SecurityInterface{
    
    private SecurityInterface handler;
    
    public SecurityProcessor() {
        handler = new ZeroSecurity();
    }
    
    public SecurityProcessor( SecurityInterface handler ) {
        this.handler = handler;
    }

    @Override
    public String generateConversationId( Conversation conversation) {
        return handler.generateConversationId(conversation);
    }

    @Override
    public String generateUserId( User user ) {
        return handler.generateUserId( user );
    }
    

}
