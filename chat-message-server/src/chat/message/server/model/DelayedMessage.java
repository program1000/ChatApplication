package chat.message.server.model;

public class DelayedMessage extends Message {
    
    private int conversationId;
    
    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId( int conversationId ) {
        this.conversationId = conversationId;
    }

}
