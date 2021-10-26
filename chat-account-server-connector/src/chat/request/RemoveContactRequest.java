package chat.request;

public class RemoveContactRequest {
    private int id;

    public RemoveContactRequest( int id ) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
