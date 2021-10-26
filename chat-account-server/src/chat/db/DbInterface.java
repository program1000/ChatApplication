package chat.db;

import java.util.List;
import java.util.Map;

import chat.model.Contact;
import chat.model.Group;
import chat.model.MessageServer;
import chat.model.User;

public interface DbInterface {
    
    public static final String DEFAULT_SERVERS = "DEF"; 

    public User getUser( String name, String pass );
    public User hasUser( int id );
    
    public void setUsers( List<User> newUsers );
    public void setContacts( Map<User, List<Contact>> newContactMap );
    public void setGroups( Map<User, List<Group>> newGroupMap );
    public void setMessageServers( Map<User, List<MessageServer>> newMessageServerMap );
    public void setDefaultMessageServers( Map<String, List<MessageServer>> newMessageServerMap );
    
    public boolean addUser( User user );
    public boolean addContact( User user, Contact contact );
    public boolean addGroup( User user, Group group );
    public boolean addContactToGroup( User user, int groupId, int contactId );
    public boolean addMessageServer( User user, MessageServer server );
    
    public List<Contact> getContacts( User user );
    public List<MessageServer> getMessageServers( User user );
    public List<MessageServer> getDefaultMessageServers( String key );
        
    public boolean delContact( User user, int contactId );
}
