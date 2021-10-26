package chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import chat.connector.AccountConnectorInterface;
import chat.db.DbInterface;
import chat.db.DbProxy;
import chat.model.Contact;
import chat.model.Group;
import chat.model.MessageServer;
import chat.model.User;
import chat.processor.AbstractComponent;
import chat.processor.AccountProcessor;
import chat.processor.ContactProcessor;
import chat.processor.GroupProcessor;
import chat.processor.MessageServerProcessor;
import chat.processor.SecurityProcessor;
import chat.processor.SessionProcessor;

public class ZeroConfig implements ComponentInterface{
	private DbInterface db;
	private AccountProcessor accountProcessor;
	private ContactProcessor contactProcessor;
	private GroupProcessor groupProcessor;
	private MessageServerProcessor messageServerProcessor;
	private SessionProcessor sessionProcessor;
	private SecurityProcessor securityProcessor;
	
	public ZeroConfig() {
		db = new DbProxy();
		sessionProcessor = new SessionProcessor();
		accountProcessor = new AccountProcessor();
		contactProcessor = new ContactProcessor();
		groupProcessor = new GroupProcessor();
		messageServerProcessor = new MessageServerProcessor();
		securityProcessor = new SecurityProcessor();
		
		
		db.setUsers( new ArrayList<User>() );
		db.setContacts(new HashMap<User, List<Contact>>());
		db.setGroups(new HashMap<User, List<Group>>());
		db.setMessageServers( new HashMap<User, List<MessageServer>>() );
		db.setDefaultMessageServers( new HashMap<String, List<MessageServer>>() );
		
		List<AbstractComponent> components = new ArrayList<AbstractComponent>();
		components.add( accountProcessor );
		components.add( contactProcessor );
		components.add( groupProcessor );
		components.add( messageServerProcessor );
		
		for( AbstractComponent component : components) {
		    component.setDb( db );
		    component.setSessionProcessor( sessionProcessor );
		    component.setSecurityProcessor( securityProcessor );
		}
		sessionProcessor.setSecurityProcessor( securityProcessor );
	}
	
	public boolean isInited() {
		List<ComponentInterface> components = new ArrayList<ComponentInterface>();
		components.add((ComponentInterface) db);
		components.add( accountProcessor );
		components.add( contactProcessor );
		components.add( groupProcessor );
		components.add( messageServerProcessor );
		components.add( sessionProcessor );
		for(ComponentInterface comp: components) {
			if (!comp.isInited()) {
				return false;
			}
		}
		return true;
		
	}

	public DbInterface getDb() {
		return db;
	}

	public AccountConnectorInterface getAccountProcessor() {
		return accountProcessor;
	}

	public ContactProcessor getContactProcessor() {
		return contactProcessor;
	}

	public GroupProcessor getGroupProcessor() {
		return groupProcessor;
	}
	
    public MessageServerProcessor getMessageServerProcessor() {
        return messageServerProcessor;
    }

	public SessionProcessor getSessionProcessor() {
		return sessionProcessor;
	}
	
	public SecurityProcessor getSecurityProcessor() {
	    return securityProcessor;
	}

}
