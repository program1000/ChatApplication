package chat.message.model;

import javax.xml.bind.annotation.XmlElement;

public class ContactInfo extends ResponseMessage {
    @XmlElement
    private String id;
    @XmlElement
    private String name;
    
    public ContactInfo() {}

    public ContactInfo( String id, String name ) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
