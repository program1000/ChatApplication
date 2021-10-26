package chat.message.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TokenMessage {
    
    @XmlElement
    public String requestToken;
    @XmlElement
    public String responseToken;
    
  //needed for deserialization
    public TokenMessage() {}
    
    public TokenMessage( String requestToken, String responseToken ) {
        this.requestToken = requestToken;
        this.responseToken = responseToken;
    }

}
