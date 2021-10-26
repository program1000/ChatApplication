package chat.model;

public class MessageServer {
    private String name;
    private String host;
    public MessageServer( String name, String host ) {
        this.name = name;
        this.host = host;
    }
    public String getName() {
        return name;
    }
    public void setName( String name ) {
        this.name = name;
    }
    public String getHost() {
        return host;
    }
    public void setHost( String host ) {
        this.host = host;
    }

}
