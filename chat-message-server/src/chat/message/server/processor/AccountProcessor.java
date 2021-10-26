package chat.message.server.processor;

import chat.message.server.connector.MessageServerAccountConnectorInterface;
import chat.message.server.model.User;
import chat.request.NewConversationUserRequest;

public class AccountProcessor extends AbstractComponent implements MessageServerAccountConnectorInterface {
    
    public boolean addUser( NewConversationUserRequest request ) {
        User user = new User();
        user.setId( request.getId() );
        return db.addUser( user );
    }

}
