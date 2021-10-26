package chat.connector;

import java.util.ArrayList;
import java.util.List;

import chat.SecurityManagerInterface;
import chat.message.model.ContactInfo;
import chat.message.model.ErrorMessage;
import chat.message.model.ResponseMessage;
import chat.message.model.SessionMessage;
import chat.message.model.WarningMessage;

public class SecurityManagerStub implements SecurityManagerInterface {
    
    public final static String ERR_NO_LOGIN = "ERR";
    private boolean hasError;
    private boolean hasRemoved;
    private boolean hasClosed;
    private boolean hasOfflineTarget;
    private List<String> delayedMessages;
    private String receivedMessage;
    private String delayedMessage;
    
    public SecurityManagerStub() {
        hasError = false;
        hasRemoved = true;
        hasClosed = true;
        hasOfflineTarget = false;
    }
    
    public void setError() {
        hasError = true;
    }
    
    public void setRemovedFalse() {
        hasRemoved = false;
    }
    
    public void setClosedFalse() {
        hasClosed = false;
    }
    
    public void setOffLineTarget() {
        hasOfflineTarget = true;
    }
    
    public void setDelayedMessage( List<String> message ) {
        delayedMessages = message;
    }
    
    public ResponseMessage getNewSession( String sessionId ) {
        if ( hasError ) {
            return new ErrorMessage( ERR_NO_LOGIN );
        }
        return  new SessionMessage( "1", sessionId );
    }
    
    public ResponseMessage addContact( String viewId, String sessionId, String contactTokenId ) {
        if ( hasError ) {
            return new WarningMessage ( viewId+sessionId+contactTokenId );
        }
        return new ContactInfo( "1", viewId+sessionId+contactTokenId );
    }
    
    public boolean delContact( String viewId, String sessionId, String contactViewId ) {
        return hasRemoved;
    }
    
    public String createConversation( String viewId, List<String> contactIds ) {
        String contactId = contactIds.get( 0 );
        return "1"+contactId+viewId;
    }
    
    public boolean closeConversation( String viewId, String convViewId ) {
        return hasClosed;
    }
    
    public List<String> getDelayedMessages( String viewId ) {
        return delayedMessages;
    }
    
    public List<String> getConversationContactIds( String viewId, String convId ){
        List<String> result = new ArrayList<String>();
        result.add( "1"+viewId+convId );
        return result;
    }
    
    public List<String> getConversationUserIds( String viewId, String convId ) {
        List<String> result = new ArrayList<String>();
        if ( hasOfflineTarget == false ) {
          result.add( viewId );
        } else {
          result.add( viewId+"1" );
        }
        return result;
    }
    
    public String getContactViewId( String userViewId, String viewId ) {
        return "from"+viewId;
    }
    
    public void addDelayedMessage( String convId, String viewId, String userViewId, String message ) {
        delayedMessage = convId+viewId+userViewId+message;
    }
    
    public void addMessageToConversation( String convId, String viewId, String message ) {
        receivedMessage = convId+viewId+message;
    }
    
    public String getReceivedMessage() {
        return receivedMessage;
    }
    
    public String getDelayedMessage() {
        return delayedMessage;
    }
    
    

}
