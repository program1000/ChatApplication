package chat.request;

public class AddContactToGroupRequest {
	private int groupId;
	private int contactId;

	public AddContactToGroupRequest(int groupId, int contactId) {
		this.groupId = groupId;
		this.contactId = contactId;
	}

	public int getGroupId() {
		return groupId;
	}
	
	public int getContactId() {
		return contactId;
	}
	
}
