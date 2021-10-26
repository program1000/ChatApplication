package chat.message.server.connector;

import java.util.List;

import chat.request.AddDelayedMessageToConversationRequest;
import chat.request.AddMessageToConversationRequest;
import chat.request.CloseConversationRequest;
import chat.request.GetDelayedMessagesRequest;
import chat.request.GetUsersFromConversationRequest;
import chat.request.NewConversationRequest;

public interface MessageServerConversationConnectorInterface {
    
    public int newConversation( NewConversationRequest request );
    
    public List<Integer> getConversationUserIds( GetUsersFromConversationRequest request );
    
    public boolean addMessageToConversation( AddMessageToConversationRequest request);
    
    public void addDelayedMessage( AddDelayedMessageToConversationRequest request );
    
    public List<String> getDelayedMessages( GetDelayedMessagesRequest request );
    
    public String generateUserId( int userId );
    
    public String generateConversationId( int convId );
    
    public boolean closeConversation( CloseConversationRequest request );

}
