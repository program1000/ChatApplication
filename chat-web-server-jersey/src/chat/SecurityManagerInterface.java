package chat;

import java.util.List;

import chat.message.model.ResponseMessage;

public interface SecurityManagerInterface {
    
    public static final String HEADER_SESSION="x-session-id";
    public static final String HEADER_ID="x-id";
    public static final String HEADER_CONV_ID="x-conv-id";
    
    public ResponseMessage getNewSession( String sessionId );
    
    public ResponseMessage addContact( String viewId, String sessionId, String contactTokenId );
    
    public boolean delContact( String viewId, String sessionId, String contactViewId );

    public String createConversation( String viewId, List<String> contactIds );

    public boolean closeConversation( String viewId, String convViewId );

    public List<String> getDelayedMessages( String viewId );

    public List<String> getConversationContactIds( String viewId, String convId );

    public List<String> getConversationUserIds( String viewId, String convId );

    public String getContactViewId( String userViewId, String viewId );

    public void addDelayedMessage( String convId, String viewId, String userViewId, String message );

    public void addMessageToConversation( String convId, String viewId, String message );

}
