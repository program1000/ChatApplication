package chat.request;

public class AddMessageToConversationRequest {
    private int senderId;
    private int conversationId;
    private String content;
    
    public AddMessageToConversationRequest( int senderId, int conversationId, String content ) {
        this.senderId = senderId;
        this.conversationId = conversationId;
        this.content = content;
    }

    public int getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public int getConversationId() {
        return conversationId;
    }


}
