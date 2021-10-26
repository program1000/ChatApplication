package chat.message.server.processor;

import chat.db.DbInMemory;

public abstract class AbstractComponent {
    
    DbInMemory db;
    SecurityProcessor securityProcessor;
    
    public void setDb( DbInMemory db ) {
        this.db = db;
    }
    
    public void setSecurityProcessor( SecurityProcessor processor ) {
        securityProcessor = processor;
    }

}
