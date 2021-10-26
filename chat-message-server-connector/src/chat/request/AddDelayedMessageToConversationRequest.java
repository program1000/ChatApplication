package chat.request;

public class AddDelayedMessageToConversationRequest {
    private int senderId;
    private int targetId;
    private int conversationId;
    private String content;
    
    public AddDelayedMessageToConversationRequest( int senderId, int targetId, int conversationId, String content ) {
        this.senderId = senderId;
        this.targetId = targetId;
        this.conversationId = conversationId;
        this.content = content;
    }

    public int getSenderId() {
        return senderId;
    }
    
    public int getTargetId() {
        return targetId;
    }

    public String getContent() {
        return content;
    }

    public int getConversationId() {
        return conversationId;
    }
}
