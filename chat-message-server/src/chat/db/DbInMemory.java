package chat.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import chat.message.server.model.Conversation;
import chat.message.server.model.DelayedMessage;
import chat.message.server.model.User;

public class DbInMemory {
    
    private List<User> users;
    private int conversionSeq;
    private Set<User> connectedSet;
    private Map<Integer,Conversation> conversions;
    private Map<Integer, List<DelayedMessage>> undeliveredMessages;
    
    public DbInMemory() {
        conversionSeq = 1;
    }
    
    public void setUsers( List<User> users ) {
        this.users = users;
    }

    public void setConnectedSet( Set<User> connectedSet ) {
        this.connectedSet = connectedSet;
    }

    public void setConversions( Map<Integer, Conversation> conversions ) {
        this.conversions = conversions;
    }
    
    public void setUndeliveredMessages( Map<Integer, List<DelayedMessage>> undeliveredMessages ) {
        this.undeliveredMessages = undeliveredMessages;
    }

    public boolean addUser( User user ) {
        if( hasUser( user ) ) {
            return false;
        }
        users.add( user );
        return true;
    }
    
    private boolean hasUser( User user) {
        int id = user.getId();
        for( User u : users ) {
            if ( u.getId()== id ) {
                return true;
            }
        }
        return false;
    }
    
    public List<User> getUsers( List<Integer> ids) {
        User u=null;
        List<User> result = new ArrayList<User>();
        for( int id : ids ) {
            u = getUser( id );
            if ( u==null ) {
                return null;
            }
            result.add( u );
        }
        return result;
    }
    
    public User getUser( int id ) {
        for( User u : users ) {
            if ( u.getId()== id ) {
                return u;
            }
        }
        return null;
    }
    
    public void addConversation( Conversation conversation ) {
        int id = generateConversationId();
        conversions.put( id, conversation );
        conversation.setId( id );
    }
    
    private int generateConversationId() {
        return conversionSeq++;
    }
    
    public Conversation getConversation( int id ) {
        return conversions.get( id );
    }
    
    public List<DelayedMessage> getDelayedMessageForUser( int userId ) {
        return undeliveredMessages.get( userId );
    }
    
    public void removeDelayedMessageForUser( int userId ) {
        undeliveredMessages.remove( userId );
    }
    
    public void addDelayedMessage( int targetId, DelayedMessage message ) {
        List<DelayedMessage> messages = undeliveredMessages.get( targetId );
        if( messages==null ) {
            messages = new ArrayList<DelayedMessage>();
            undeliveredMessages.put( targetId, messages );
        }
        messages.add( message );
    }

    public void updateConversation( Conversation conversation ) {
        // NO OP
    }
    
    public int getDelayedMessageCountForConversation( int convId ) {
        if ( undeliveredMessages.isEmpty()) {
            return 0;
        }
        Collection<List<DelayedMessage>> messages = undeliveredMessages.values();
        return messages.stream().flatMap( Collection::stream ).reduce( 0, (partial,message)-> partial+ (message.getConversationId()==convId?1:0), Integer::sum );
    }

    public void deleteConversation( int convId ) {
       conversions.remove( convId );
        
    }
    

}
