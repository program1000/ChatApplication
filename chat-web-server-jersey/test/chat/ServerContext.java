package chat;

public class ServerContext {
    
    private static ServerContext instance;
    private ServerInterface server;
    
    private ServerContext() {
        
    }
    
    public static ServerContext getInstance() {
        if ( instance==null ) {
            instance = new ServerContext();
        }
        return instance;
    }

    public void setServer( ServerInterface server ) {
        this.server = server;
    }
    
    public ServerInterface getServer() {
        return server;
    }
    

}
