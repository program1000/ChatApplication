package chat.message.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ChatMessage extends SSEMessage {

    @XmlElement
    private String sender;

    @XmlElement
    private String content;
    
    @XmlElement
    private String convId;
    
    //needed for deserialization
    public ChatMessage() {}
    
    public ChatMessage( String sender, String content, String convId ) {
        this.sender = sender;
        this.content = content;
        this.convId = convId;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public String getConvId() {
        return convId;
    }
}

