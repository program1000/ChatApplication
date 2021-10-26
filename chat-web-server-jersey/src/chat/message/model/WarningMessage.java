package chat.message.model;

import javax.xml.bind.annotation.XmlElement;

public class WarningMessage extends ResponseMessage {
    @XmlElement
    private String warning;
    
    public WarningMessage() {}

    public WarningMessage( String warning ) {
        this.warning = warning;
    }

    public String getWarning() {
        return warning;
    }
}
