package chat.request;

public class GetDefaultMessageServerRequest {
    private String name;

    public GetDefaultMessageServerRequest( String name ) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
