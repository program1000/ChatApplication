package chat.message.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ConversationMessage {
    
    @XmlElement
    private List<String> contactIds;
    
    public ConversationMessage() {}

    public ConversationMessage( List<String> contactIds ) {
        this.contactIds = contactIds;
    }

    public List<String> getContactIds() {
        return contactIds;
    }

}
