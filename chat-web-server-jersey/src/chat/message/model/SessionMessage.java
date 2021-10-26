package chat.message.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SessionMessage extends ResponseMessage{

    @XmlElement
    private String userId;

    @XmlElement
    private String sessionId;
    
    @XmlElement
    private List<ContactInfo> contacts;
    
    public SessionMessage() {}

    public SessionMessage( String userId, String sessionId ) {
        this.userId = userId;
        this.sessionId = sessionId;
        contacts = new ArrayList<ContactInfo>();
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public void addContact( ContactInfo contact ) {
        contacts.add( contact );
    }

}
