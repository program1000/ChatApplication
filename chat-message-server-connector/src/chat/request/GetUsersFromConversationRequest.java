package chat.request;

public class GetUsersFromConversationRequest {

    private int userId;
    private int conversationId;

    public GetUsersFromConversationRequest( int userId, int conversationId ) {
        this.userId = userId;
        this.conversationId = conversationId;
    }

    public int getUserId() {
        return userId;
    }

    public int getConversationId() {
        return conversationId;
    }

}
