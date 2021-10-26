package chat.request;

public class NewConversationUserRequest {
    
    private int id;

    public NewConversationUserRequest( int id ) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
