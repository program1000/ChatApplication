package chat.message.server.connector;

import chat.request.NewConversationUserRequest;

public interface MessageServerAccountConnectorInterface {

    public boolean addUser( NewConversationUserRequest request );
}
