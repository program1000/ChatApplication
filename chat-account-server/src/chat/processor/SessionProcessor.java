package chat.processor;

import java.util.HashMap;
import java.util.Map;

import chat.ComponentInterface;
import chat.model.User;

public class SessionProcessor implements ComponentInterface{

	private Map<String,User> sessionMap;
	private SecurityProcessor securityProcessor;

	public SessionProcessor() {
		sessionMap = new HashMap<String, User>();
	}
	
	public void setSecurityProcessor( SecurityProcessor processor) {
	    securityProcessor = processor;
	}
	
	public String addUser( User user ) {
	    String sessionId = securityProcessor.generateSessionToken();
		sessionMap.put( sessionId, user);
		return sessionId;
	}
	
	public User getUserFromSession( String id ) {
		return sessionMap.get(id);
	}
	
	public int getUserIdFromSession( String id ) {
	    User user= sessionMap.get(id);
	    if ( user==null) {
	        return 0;
	    }
	    return user.getId();
	}
	
    @Override
    public boolean isInited() {
        return securityProcessor!=null;
    }
}
