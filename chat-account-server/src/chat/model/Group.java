package chat.model;

import java.util.ArrayList;
import java.util.List;

public class Group {

	private int id;
	private String name;


	private List<Contact> contacts;
	
	public Group() {
		contacts = new ArrayList<Contact>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Contact> getContacts() {
		return contacts;
	}
}
