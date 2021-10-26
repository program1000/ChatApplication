package chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import chat.db.DbInMemory;
import chat.message.server.connector.MessageServerAccountConnectorInterface;
import chat.message.server.model.Conversation;
import chat.message.server.model.DelayedMessage;
import chat.message.server.model.User;
import chat.message.server.processor.ConversationProcessor;
import chat.message.server.processor.SecurityProcessor;
import chat.message.server.processor.AbstractComponent;
import chat.message.server.processor.AccountProcessor;

public class MessageServerZeroConfig {

    private DbInMemory db;
    
    private AccountProcessor accountProcessor;
    private ConversationProcessor conversationProcessor;
    private SecurityProcessor securityProcessor;
    
    public MessageServerZeroConfig() {
        db = new DbInMemory();
        
        accountProcessor = new AccountProcessor();
        conversationProcessor = new ConversationProcessor();
        securityProcessor = new SecurityProcessor();
        
        db.setUsers( new ArrayList<User>() );
        db.setConnectedSet( new HashSet<User>() );
        db.setConversions( new HashMap<Integer,Conversation>() );
        db.setUndeliveredMessages( new HashMap<Integer, List<DelayedMessage>>() );
        
        List<AbstractComponent> components = new ArrayList<AbstractComponent>();
        components.add( accountProcessor );
        components.add( conversationProcessor );
        for( AbstractComponent component : components ) {
            component.setDb( db );
            component.setSecurityProcessor( securityProcessor );
        }
    }

    public DbInMemory getDb() {
        return db;
    }

    public MessageServerAccountConnectorInterface getAccountProcessor() {
        return accountProcessor;
    }

    public ConversationProcessor getConversionProcessor() {
        return conversationProcessor;
    }
    
    public SecurityProcessor getSecurityProcessor() {
        return securityProcessor;
    }
}
