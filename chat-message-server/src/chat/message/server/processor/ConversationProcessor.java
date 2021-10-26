package chat.message.server.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import chat.message.server.connector.MessageServerConversationConnectorInterface;
import chat.message.server.model.Conversation;
import chat.message.server.model.DelayedMessage;
import chat.message.server.model.Message;
import chat.message.server.model.User;
import chat.request.AddDelayedMessageToConversationRequest;
import chat.request.AddMessageToConversationRequest;
import chat.request.CloseConversationRequest;
import chat.request.GetDelayedMessagesRequest;
import chat.request.GetUsersFromConversationRequest;
import chat.request.NewConversationRequest;

public class ConversationProcessor extends AbstractComponent implements MessageServerConversationConnectorInterface{
    
    public int newConversation( NewConversationRequest request ) {
        Conversation conversation = new Conversation();
        conversation.setUsers( db.getUsers( request.getIds() ) );
        conversation.getCloseSet().addAll( request.getIds() );
        db.addConversation( conversation );
        return conversation.getId();
    }
    
    public List<Integer> getConversationUserIds( GetUsersFromConversationRequest request ) {
        int id = request.getUserId();
        int convId = request.getConversationId();
        Conversation conversation = db.getConversation( convId );
        User user = db.getUser( id );
        return conversation.getUsers().stream().filter( convUser-> convUser.equals( user )==false )
                .map( convUser-> {return convUser.getId();} ).collect(Collectors.toList());
    }

    public boolean addMessageToConversation( AddMessageToConversationRequest request ) {
        int convId = request.getConversationId();
        Conversation conversation = db.getConversation( convId );
        if (conversation==null) {
            return false;
        }
        Message message = new Message();
        message.setUserId( request.getSenderId() );
        message.setContent( request.getContent() );
        conversation.addMessage( message );
        db.updateConversation( conversation );
        return true;
    }
    
    public void addDelayedMessage( AddDelayedMessageToConversationRequest request ) {
        DelayedMessage message = new DelayedMessage();
        message.setUserId( request.getSenderId() );
        message.setContent( request.getContent() );
        int conversationId = request.getConversationId();
        message.setConversationId( conversationId );
        db.addDelayedMessage( request.getTargetId(), message );
    }
    
    public List<String> getDelayedMessages( GetDelayedMessagesRequest request ) {
        int userId = request.getUserId();
        List<DelayedMessage> messages = db.getDelayedMessageForUser( userId );
        if ( messages == null ) {
            return null;
        }
        db.removeDelayedMessageForUser( userId );
        List<String> result = new ArrayList<String>(messages.size());
        for( DelayedMessage message : messages ) {
            result.add( Integer.toString( message.getUserId() ) );
            result.add( Integer.toString( message.getConversationId() ) );
            result.add( message.getContent() );
        }
        return result;
    }
    
    public String generateUserId( int userId ) {
        User user = db.getUser( userId );
        return securityProcessor.generateUserId( user );
    }
    
    public String generateConversationId( int convId ) {
        Conversation conversation = db.getConversation( convId );
        return securityProcessor.generateConversationId( conversation );
    }

    @Override
    public boolean closeConversation( CloseConversationRequest request ) {
        int convId = request.getConvId();
        Conversation conversation = db.getConversation( convId );
        if( conversation.getMessages().isEmpty() ) {
            db.deleteConversation( convId );
            return true;
        }
        Set<Integer> closeSet = conversation.getCloseSet();
        closeSet.remove( request.getUserId() );
        if ( closeSet.isEmpty()==false ) {
            return false;
        }
        int count = db.getDelayedMessageCountForConversation( convId );
        if (count==0) {
            db.deleteConversation( convId );
            return true;
        }
        return false;
    }

}
