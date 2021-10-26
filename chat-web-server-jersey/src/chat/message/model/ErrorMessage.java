package chat.message.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorMessage extends ResponseMessage {

    @XmlElement
    private String reason;
    
    public ErrorMessage() {}

    public ErrorMessage( String reason ) {
        this.reason = reason;
    }
    
    public String getReason() {
        return reason;
    }
}
