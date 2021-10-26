package chat.message.server.model;

public class User {
    
    enum Status {OFFLINE,ONLINE};

    int id;
    Status status;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus( Status status ) {
        this.status = status;
    }
    
    public boolean equals( Object user ) {
        if ( user==null || user instanceof User==false  ) {
            return false;
        }
        return id==((User)user).id;
    }
}
