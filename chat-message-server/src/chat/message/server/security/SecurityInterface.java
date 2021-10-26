package chat.message.server.security;

import chat.message.server.model.Conversation;
import chat.message.server.model.User;

public interface SecurityInterface {

    public String generateConversationId( Conversation conversation);

    public String generateUserId( User user );

}
