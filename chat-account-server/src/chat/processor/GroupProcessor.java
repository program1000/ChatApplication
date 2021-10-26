package chat.processor;

import chat.model.Group;
import chat.model.User;
import chat.request.AddContactToGroupRequest;
import chat.request.CreateGroupRequest;

public class GroupProcessor extends AbstractComponent {
	
	public boolean createGroup( User user, CreateGroupRequest request ) {
		Group group = new Group();
		group.setName(request.getName());
		return db.addGroup(user, group);
	}
	
	public boolean addContactToGroup( User user, AddContactToGroupRequest request ) {
		return db.addContactToGroup(user, request.getGroupId(), request.getContactId() );
	}
}
