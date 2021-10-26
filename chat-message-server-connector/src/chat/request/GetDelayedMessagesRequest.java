package chat.request;

public class GetDelayedMessagesRequest {
    
    private int userId;

    public GetDelayedMessagesRequest( int userId ) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

}
