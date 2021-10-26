package chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import chat.connector.AccountConnector;
import chat.connector.ConnectorRegister;
import chat.connector.ContactConnector;
import chat.connector.ContactConnectorInterface;
import chat.message.model.ContactInfo;
import chat.message.model.ErrorMessage;
import chat.message.model.ResponseMessage;
import chat.message.model.SessionMessage;
import chat.message.model.WarningMessage;
import chat.message.server.connector.MessageServerConnectorRegister;
import chat.message.server.connector.MessageServerConversationConnector;

public class SecurityManager implements SecurityManagerInterface {
    
    private final AccountConnector accountConnector = ConnectorRegister.getAccountConnector();
    private final ContactConnector contactConnector = ConnectorRegister.getContactConnector();
    private final MessageServerConversationConnector conversationConnector = MessageServerConnectorRegister.getConversationConnector();
    private static SecurityManagerInterface instance;
    private final Pattern sessionPattern =  Pattern.compile("\\d+");
    private final Map<String, Integer> viewIdMap;
    private final Map<Integer, String> idMap;
    private final Map<Integer, Map<Integer, String>> userContactIdMap;
    private final Map<Integer, Map<String, Integer>> userContactViewIdMap;
    private final Map<String, Integer> convViewIdMap;
    private final Map<Integer, String> convIdMap;
    
    private final String ERR_NO_LOGIN="Cannot authenticate user, username and password do not match!";

    private SecurityManager() {
        viewIdMap = new HashMap<String, Integer>();
        idMap = new HashMap<Integer, String>();
        userContactIdMap = new HashMap<Integer, Map<Integer,String>>();
        userContactViewIdMap = new HashMap<Integer, Map<String,Integer>>();
        convViewIdMap = new HashMap<String, Integer>();
        convIdMap = new HashMap<Integer, String>();
    }
    
    public static SecurityManagerInterface get() {
        if ( instance==null ) {
            instance = new SecurityManager();
        }
        return instance;
    }
    
    public static void set( SecurityManagerInterface securityManager ) {
        instance = securityManager;
    }
    
    private int getId( String viewId ) {
        Integer testId = viewIdMap.get( viewId );
        if ( testId==null ) {
            // error
        }
        return testId;
    }
    
    //session, conversation, contact
    public ResponseMessage getNewSession( String sessionId ) {
        Matcher matcher = sessionPattern.matcher(sessionId);
        if ( matcher.matches() == false) {
            //return nologin
            return new ErrorMessage( ERR_NO_LOGIN );
        }
        int id = accountConnector.getUserIdFromSessionId( sessionId );
        String viewId = conversationConnector.generateUserId( id );
        addId( id, viewId );
        SessionMessage result = new SessionMessage( viewId, sessionId );
        Map<Integer,String> contactIdMap = addContacts( sessionId, result, contactConnector.getContacts( sessionId ) );
        Map<String,Integer> contactViewIdMap = new HashMap<String, Integer>();
        contactIdMap.forEach( (contactId,contactViewId) -> { contactViewIdMap.put(contactViewId, contactId);});
        userContactIdMap.put( id, contactIdMap );
        userContactViewIdMap.put( id, contactViewIdMap );
        return result;
    }
    
    private Map<Integer,String> addContacts( String sessionId, SessionMessage message, List<String> contactsList ) {
        int l = contactsList.size();
        List<Integer> contactIds = new ArrayList<Integer>(); 
        for( int i=0; i< l;i+=2) {
            contactIds.add( Integer.valueOf( contactsList.get( i ) ) );
        }
        List<String> contactViewIds = contactConnector.getGeneratedContactIds( sessionId, contactIds );
        Map<Integer,String> contactIdMap = new HashMap<Integer, String>();
        for( int i=0,j=0; i< l;i+=2,j++) {
            int contactId = contactIds.get( j );
            String contactViewId = contactViewIds.get( j );
            String contactName = contactsList.get( i+1 );
            contactIdMap.put( contactId, contactViewId );
            message.addContact( new ContactInfo( contactViewId, contactName ) );
        }
        return contactIdMap;
    }
    
    //conversation
    public List<String> getDelayedMessages( String viewId ) {
        int id = getId( viewId );
        List<String> messages = conversationConnector.getDelayedMessages( id );
        if ( messages !=null ) {
            int len = messages.size();
            Map<Integer,String> contactIdMap = userContactIdMap.get( id );
            for ( int i =0; i<len; i+=3 ) {
                messages.set( i, contactIdMap.get( Integer.valueOf( messages.get( i/3 ) ) ) );
                int convId = Integer.valueOf( messages.get( i+1 ) );
                String convViewId = convIdMap.get( convId );
                if ( convViewId == null ) {
                    convViewId = conversationConnector.generateConversationId( convId );
                    addConvId( convId, convViewId );
                }
                messages.set( i+1, convViewId );
            }
        }
        return messages;
    }
    
    //conversation
    public List<String> getConversationUserIds( String viewId, String convViewId ) {
        int id = getId( viewId );
        int convId = convViewIdMap.get( convViewId );
        List<Integer> userIds = conversationConnector.getConversationUserIds( id, convId );
        return userIds.stream().map( userId -> {
            String userViewId = idMap.get( userId );
            if ( userViewId==null ) {
                userViewId = conversationConnector.generateUserId( userId );
                addId( userId, userViewId );
            }
            return userViewId;}).collect(Collectors.toList());
    }
    
    public String getContactViewId( String targetViewId, String senderViewId ) {
        int targetId = getId( targetViewId );
        int senderId = getId( senderViewId );
        Map<Integer,String> contactIdMap = userContactIdMap.get( targetId );
        return contactIdMap.get( senderId );
    }
    
    private void addId( int userId, String userViewId ) {
        viewIdMap.put( userViewId, userId );
        idMap.put( userId, userViewId );
    }
    
    //conversation
    public void addDelayedMessage( String convViewId, String senderViewId, String targetViewId, String message) {
        int targetId = getId( targetViewId );
        int senderId = getId( senderViewId );
        int convId = convViewIdMap.get( convViewId );
        conversationConnector.addDelayedMessage( convId, senderId, targetId, message );
    }

    //conversation
    public void addMessageToConversation( String convViewId, String viewId, String message ) {
        int senderId = getId( viewId );
        int convId = convViewIdMap.get( convViewId );
        conversationConnector.addMessageToConversation( convId, senderId, message );
    }
    
    //conversation
    public List<String> getConversationContactIds( String viewId, String convViewId ) {
        List<String> viewIds = getConversationUserIds( viewId, convViewId );
        return viewIds.stream().map( contactId -> { return getContactViewId( viewId, contactId);} ).collect(Collectors.toList());
    }

    //conversation
    public String createConversation( String viewId, List<String> contactIds ) {
        int id = getId( viewId );
        Map<String,Integer> contactViewIdMap = userContactViewIdMap.get( id );
        if ( contactViewIdMap==null || contactIds.size() > contactViewIdMap.size() ) {
            //error
        }
        List<Integer> contactIdList = contactIds.stream().map( contactViewId -> { return contactViewIdMap.get( contactViewId ); }).collect( Collectors.toList() );
        List<Integer> idList = new ArrayList<Integer>();
        idList.add( id );
        idList.addAll( contactIdList );
        int convId = conversationConnector.newConversation( idList );
        String convViewId = conversationConnector.generateConversationId( convId );
        addConvId( convId, convViewId );
        return convViewId;
    }
    
    public boolean closeConversation( String viewId, String convViewId ) {
        int id = getId( viewId );
        int convId = convViewIdMap.get( convViewId );
        boolean hasRemoved =  conversationConnector.closeConversation( id, convId );
        if ( hasRemoved ) {
            convViewIdMap.remove( convViewId );
            convIdMap.remove( convId );
        }
        return hasRemoved;
    }
    
    private void addConvId( int convId, String convViewId ) {
        convViewIdMap.put( convViewId, convId );
        convIdMap.put( convId, convViewId );
    }
    
    //contact
    public ResponseMessage addContact( String viewId, String sessionId, String contactTokenId ) {
        List<String> contact = contactConnector.addContact( sessionId, contactTokenId );
        int contactId = Integer.valueOf( contact.get( 0 ) );
        String name = contact.get( 1 );
        if ( contactId == ContactConnectorInterface.NO_CONTACT_ID ) {
            return new WarningMessage(  name );
        }
        String contactViewId = contactConnector.getGeneratedContactId( sessionId, contactId );
        contact.set( 0, contactViewId );
        int id = getId( viewId );
        Map<String,Integer> contactViewIdMap = userContactViewIdMap.get( id );
        contactViewIdMap.put( contactViewId, contactId );
        Map<Integer,String> contactIdMap = userContactIdMap.get( id );
        contactIdMap.put( contactId, contactViewId );
        return new ContactInfo( contactViewId, name );
    }
    
    //contact
    public boolean delContact( String viewId, String sessionId, String contactViewId ) {
        int id = getId( viewId );
        Map<String,Integer> contactViewIdMap = userContactViewIdMap.get( id );
        int contactId = contactViewIdMap.remove( contactViewId );
        Map<Integer,String> contactIdMap = userContactIdMap.get( id );
        contactIdMap.remove( contactId );
        return contactConnector.delContact( sessionId, contactId );
    }

    
}
