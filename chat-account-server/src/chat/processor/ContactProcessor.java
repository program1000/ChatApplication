package chat.processor;

import java.util.ArrayList;
import java.util.List;

import chat.connector.ContactConnectorInterface;
import chat.model.Contact;
import chat.model.User;
import chat.request.NewContactRequest;
import chat.request.RemoveContactRequest;

public class ContactProcessor extends AbstractComponent implements ContactConnectorInterface {
	
	public String createContactInvite( String sessionId ) {
	    User user = sessionProcessor.getUserFromSession( sessionId );
		return securityProcessor.generateContactInviteToken( user );
	}
	
	public List<String> addContact( String sessionId, NewContactRequest request ) {
	    User user = sessionProcessor.getUserFromSession( sessionId );
	    int contactId = securityProcessor.getContactIdfromInviteToken( request.getId() );
	    Contact contact = null;
	    if ( user.getId()==contactId) {
	        //error try to add user as contact
	        contact = new Contact( "Cannot add user as contact!", null );
            contact.setId( NO_CONTACT_ID );
	    } else {
		    User contactUser = db.hasUser( contactId );
		    if ( contactUser==null) {
			    //error non existing user
		        contact = new Contact( "Contact does not exist!", null );
		        contact.setId( NO_CONTACT_ID );
		    } else {
		        contact = new Contact( contactUser.getName(), contactUser.getName() );
                contact.setId( contactId );
                if ( db.addContact( user, contact ) ==false ) {
                   //error trying to add existing contact user
                   contact = new Contact( "Contact already added!", null );
                   contact.setId( NO_CONTACT_ID );
	           }
		   }
	    }
		List<Contact> contacts= new ArrayList<Contact>();
		contacts.add( contact );
		return serializeContacts( user, contacts );
	}
	
	public boolean delContact( String sessionId, RemoveContactRequest request ) {
	    User user = sessionProcessor.getUserFromSession( sessionId );
	    int contactId = request.getId();
	    return db.delContact( user, contactId );
	}
	
	public List<String> getContacts( String sessionId ) {
	    User user = sessionProcessor.getUserFromSession( sessionId );
	    List<Contact> contacts = db.getContacts( user);
	    if ( contacts==null ) {
	        return new ArrayList<String>();
	    }
	    return serializeContacts( user, contacts );
	}
	
	public String getGeneratedContactId( String sessionId, int contactId ) {
	    User user = sessionProcessor.getUserFromSession( sessionId );
	    return securityProcessor.generateContactId( user, contactId );
	}
	
	public List<String> getGeneratedContactIds( String sessionId, List<Integer> contactList ) {
	    User user = sessionProcessor.getUserFromSession( sessionId );
	    List<String> idList = new ArrayList<String>();
	    contactList.forEach( contactId -> {
	         idList.add(  securityProcessor.generateContactId( user, contactId ) );
	    } );
	    return idList;
	}
	
	private List<String> serializeContacts( User user, List<Contact> contacts ) {
	    List<String> result = new ArrayList<String>();
	    for( Contact contact : contacts ) {
	        result.add( Integer.toString( contact.getId() ) );
            result.add( contact.getName() );
	    }
	    return result;
	}
	
}
