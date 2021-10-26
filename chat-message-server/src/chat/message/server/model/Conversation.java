package chat.message.server.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Conversation {
    
    int id;
    
    List<Message> messages;
    List<User> users;
    Set<Integer> closeSet;
    
    public Conversation() {
        messages = new ArrayList<Message>();
        closeSet = new HashSet<Integer>(); 
    }
    
    public int getId() {
        return id;
    }
    
    public void setId( int id ) {
        this.id = id;
    }
    
    public List<Message> getMessages() {
        return messages;
    }
    
    public void setMessages( List<Message> messages ) {
        this.messages = messages;
    }
    
    public void addMessage ( Message message ) {
        messages.add( message );
    }

    public List<User> getUsers() {
        return users;
    }
    
    public void setUsers( List<User> users ) {
        this.users = users;
    }

    public Set<Integer> getCloseSet() {
        return closeSet;
    }

}
