package chat.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chat.ComponentInterface;
import chat.model.Contact;
import chat.model.Group;
import chat.model.MessageServer;
import chat.model.User;

public class DbProxy implements ComponentInterface, DbInterface {
	
	private List<User> users;
	private Map<User, List<Contact>> contactMap;
	private Map<User, List<Group>> groupMap;
	private Map<String, List<MessageServer>> defaultServerMap;
	private Map<User, List<MessageServer>> serverMap;
	private int userSeq;
	private int groupSeq;
	
	public DbProxy() {
	    userSeq=1;
	}

	private boolean hasUser( String name, String pass ) {
		for( User user: users) {
			if( user.getName().equals(name) && user.getPass().equals(pass) ) {
				return true;
			}
		}
		return false;
	}
	
	public User getUser( String name, String pass ) {
		for( User user: users) {
			if( user.getName().equals(name) && user.getPass().equals(pass) ) {
				return user;
			}
		}
		return null;
	}
	
	public User hasUser( int id ) {
		for( User user: users) {
			if( user.getId()== id ) {
				return user;
			}
		}
		return null;
	}
	
	public void setUsers( List<User> newUsers ) {
		users = newUsers;
	}
	
	public void setContacts( Map<User, List<Contact>> newContactMap ) {
		contactMap = newContactMap;
	}
	
	public void setGroups( Map<User, List<Group>> newGroupMap ) {
		groupMap = newGroupMap;
	}
	
    public void setMessageServers( Map<User, List<MessageServer>> newMessageServerMap ) {
        serverMap = newMessageServerMap;
    }
    
    public void setDefaultMessageServers( Map<String, List<MessageServer>> newMessageServerMap ) {
        defaultServerMap = newMessageServerMap;
    }
	
	public boolean addUser( User user ) {
		if (hasUser(user.getName(), user.getPass())) {
			return false;
		}
		user.setId( userSeq++ );
		users.add(user);
		return true;
	}

	public boolean addContact( User user, Contact contact ) {
		List<Contact> contacts = contactMap.get( user );
		if( contacts == null) {
			contacts = new ArrayList<Contact>();
			contactMap.put( user, contacts );
			contacts.add(contact);
			return true;
		}
		if (hasContact( contacts, contact )) {
			return false;
		}
		contacts.add(contact);
		return true;
	}
	
	private boolean hasContact( List<Contact> contacts, Contact contact ) {
		for( Contact c : contacts) {
			if( c.getId() == contact.getId() ) {
				return true;
			}
		}
		return false;
	}
	
	public boolean delContact( User user, int contactId ) {
	    List<Contact> contacts = contactMap.get( user );
	    Contact contact = getContact( contacts, contactId );
	    if ( contact==null ) {
	        return false;
	    }
	    contacts.remove( contact );
	    return true;
	}
	
	public List<Contact> getContacts( User user) {
	    return contactMap.get( user);
	}
	
	public boolean addGroup( User user, Group group ) {
		List<Group> groups = groupMap.get( user );
		if( groups == null) {
			groups = new ArrayList<Group>();
			group.setId( groupSeq++ );
			groups.add(group);
			groupMap.put( user, groups );
			return true;
		}
        if (hasGroup( groups, group )) {
			return false;
		}
        group.setId( groupSeq++ );
		groups.add( group );
		return true;
	}
	
	private boolean hasGroup( List<Group> groups, Group group ) {
		for( Group g : groups) {
			if( g.getName() == group.getName() ) {
				return true;
			}
		}
		return false;
	}
	
	public boolean addContactToGroup( User user, int groupId, int contactId ) {
		List<Group> groups = groupMap.get( user );
		Group group = getGroup( groups, groupId );
		if ( group==null ) {
			return false;
		}
		List<Contact> contacts = contactMap.get(user);
		Contact contact = getContact( contacts, contactId );
		if ( contact == null ) {
			return false;
		}
		
		group.getContacts().add(contact);
		
		return true;
	}
	
	private Group getGroup( List<Group> groups, int groupId ) {
		for( Group g : groups ) {
			if( g.getId() == groupId ) {
				return g;
			}
		}
		return null;
	}
	
    @Override
    public boolean addMessageServer( User user, MessageServer server ) {
        List<MessageServer> servers = serverMap.get( user );
        if( servers == null) {
            servers = new ArrayList<MessageServer>();
            servers.add(server);
            serverMap.put( user, servers );
            return true;
        }
        if ( hasMessageServer( servers, server ) ) {
            return false;
        }
        servers.add( server );
        return true;
    }
    
    private boolean hasMessageServer( List<MessageServer> servers, MessageServer server ) {
        for( MessageServer s : servers) {
            if( s.getHost() == server.getHost() ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<MessageServer> getMessageServers( User user ) {
        List<MessageServer> servers = serverMap.get( user );
        return servers;
    }
    
    @Override
    public List<MessageServer> getDefaultMessageServers( String key ) {
        List<MessageServer> servers = defaultServerMap.get( key );
        return servers;
    }
	
	private Contact getContact( List<Contact> contacts, int contactId ) {
		for( Contact c : contacts) {
			if( c.getId() == contactId ) {
				return c;
			}
		}
		return null;
	}

	@Override 
	public boolean isInited() {
		return users!=null&&contactMap!=null&&groupMap!=null&&serverMap!=null&&defaultServerMap!=null;
	}


	
}
 