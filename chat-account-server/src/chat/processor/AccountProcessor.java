package chat.processor;

import chat.connector.AccountConnectorInterface;
import chat.model.User;
import chat.request.AuthenticateRequest;
import chat.request.NewUserRequest;

public class AccountProcessor extends AbstractComponent implements AccountConnectorInterface {
	
	
	@Override
    public String createUser( NewUserRequest request ) {
		User user = new User(request.getUser(), request.getPass());
		if ( !db.addUser( user ) ) {
			// error
			return AccountConnectorInterface.NO_SESSION_ID;
		}
		return sessionProcessor.addUser(user);
	}
	
	@Override
    public String authenticate( AuthenticateRequest request ) {
		User user = db.getUser(request.getUser(), request.getPass());
		if ( user == null ) {
			// error
		    return AccountConnectorInterface.NO_SESSION_ID;
		}
		return sessionProcessor.addUser(user);
	}
	
	@Override
	public int getUserIdFromSessionId( String sessionId ) {
	    return sessionProcessor.getUserIdFromSession( sessionId );
	}



}
