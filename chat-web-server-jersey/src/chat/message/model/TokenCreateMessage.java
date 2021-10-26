package chat.message.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TokenCreateMessage extends SSEMessage {
    
    @XmlElement
    public String token;
    
    //needed for deserialization
    public TokenCreateMessage() {}
    
    public TokenCreateMessage( String token ) {
        this.token = token;
    }

}
