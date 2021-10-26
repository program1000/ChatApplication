package chat.processor;

import chat.ComponentInterface;
import chat.db.DbInterface;

public abstract class AbstractComponent implements ComponentInterface {

    protected DbInterface db;
    protected SessionProcessor sessionProcessor;
    protected SecurityProcessor securityProcessor;
    
    public void setDb( DbInterface newDb) {
        db = newDb;
    }
    
    public void setSessionProcessor( SessionProcessor processor ) {
        sessionProcessor = processor;
    }
    
    public void setSecurityProcessor( SecurityProcessor processor ) {
        securityProcessor = processor;
    }
    
    @Override
    public boolean isInited() {
        return db!=null && sessionProcessor!=null && securityProcessor!=null;
    }
}
