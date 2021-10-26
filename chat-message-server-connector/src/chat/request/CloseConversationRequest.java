package chat.request;

public class CloseConversationRequest {

    private int userId;
    private int convId;

    public CloseConversationRequest( int userId, int convId ) {
        this.userId = userId;
        this.convId = convId;
    }

    public int getUserId() {
        return userId;
    }

    public int getConvId() {
        return convId;
    }
    
}
