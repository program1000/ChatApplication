package chat.message.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoginMessage {

    @XmlElement
    private String name;
    
    @XmlElement
    private String pass;
    
    public LoginMessage() {}

    public LoginMessage( String name, String pass ) {
        this.name = name;
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }
    
    
}
