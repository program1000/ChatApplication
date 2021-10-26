package chat.message.server.connector;

import java.util.List;

import chat.request.AddDelayedMessageToConversationRequest;
import chat.request.AddMessageToConversationRequest;
import chat.request.CloseConversationRequest;
import chat.request.GetDelayedMessagesRequest;
import chat.request.GetUsersFromConversationRequest;
import chat.request.NewConversationRequest;

public class MessageServerConversationConnector {
    
    private MessageServerConversationConnectorInterface conversationConnector;

    public void setConversationConnector( MessageServerConversationConnectorInterface conversationConnector ) {
        this.conversationConnector = conversationConnector;
    }
    
    public int newConversation( List<Integer> ids ) {
        NewConversationRequest request = new NewConversationRequest( ids );
        return conversationConnector.newConversation( request );       
    }
    
    public List<Integer> getConversationUserIds( int userId, int convId ) {
        GetUsersFromConversationRequest request = new GetUsersFromConversationRequest( userId, convId );
        return conversationConnector.getConversationUserIds( request );
    }
    
    public void addMessageToConversation( int conversationId, int senderId, String content ) {
        AddMessageToConversationRequest request = new AddMessageToConversationRequest( senderId, conversationId, content );
        conversationConnector.addMessageToConversation( request );
    }
    
    public void addDelayedMessage( int conversationId, int senderId, int targetId, String content ) {
        AddDelayedMessageToConversationRequest request = new AddDelayedMessageToConversationRequest( senderId, targetId, conversationId, content );
        conversationConnector.addDelayedMessage( request );
    }
    
    public List<String> getDelayedMessages( int userId ) {
        GetDelayedMessagesRequest request = new GetDelayedMessagesRequest( userId );
        return conversationConnector.getDelayedMessages( request );
    }
    
    public String generateUserId( int userId ) {
        return conversationConnector.generateUserId( userId );
    }
    
    public String generateConversationId( int convId ) {
        return conversationConnector.generateConversationId( convId );
    }
    
    public boolean closeConversation( int userId, int convId ) {
        CloseConversationRequest request = new CloseConversationRequest( userId, convId );
        return conversationConnector.closeConversation( request );
    }

}
