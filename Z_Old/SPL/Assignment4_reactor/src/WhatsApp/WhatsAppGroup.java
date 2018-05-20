package WhatsApp;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * I am error 418
 * @author amirarb
 *
 */
public class WhatsAppGroup implements WhatsAppSender {
	private Map<String, WhatsAppUser> usersByName;
	private Map<String, WhatsAppUser> usersByPhone;
	private String name;
	
	public WhatsAppGroup(String _name, Map<String, WhatsAppUser> _users) {
		name = _name;
		usersByName = _users;
		usersByPhone = new TreeMap<String, WhatsAppUser>();
		
		// Add the group to each of the users.
		for(Entry<String, WhatsAppUser> it : usersByName.entrySet()) {
			it.getValue().addGroup(this);
			usersByPhone.put(it.getValue().getPhoneNumber(), it.getValue());
		}
	}
	
	public Boolean hasUsers() {
		return !usersByName.isEmpty();
	}
	
	/**
	 * Add a new user to the group.
	 */
	public void addUser(WhatsAppUser user) {
		usersByName.put(user.getUserName(), user);
		usersByPhone.put(user.getPhoneNumber(), user);
		user.addGroup(this);
	}
	
	/**
	 * Turns the map of users to a string.
	 * @return
	 */
	public String getUsers() {
		StringBuilder string = new StringBuilder();
		
		for(Entry<String, WhatsAppUser> it : usersByName.entrySet()) {
			string.append(it.getValue().getPhoneNumber() + ",");
		}
		
		// Remove the last comma.
		string.deleteCharAt(string.length() - 1);
		
		return string.toString();
	}
	
	public String toString() {
		return name + ":" + getUsers();
	}
	
	/**
	 * Gets user by it's name.
	 * @param userName
	 */
	public Boolean userExists(String userName) {
		return (usersByName.get(userName) != null);
	}

	/**
	 * Gets user by it's name.
	 * @param userName
	 */
	public Boolean userPhoneExists(String userName) {
		return (usersByPhone.get(userName) != null);
	}
	
	
	/**
	 * Adds a message to each of the group's users' message queues.
	 */
	public void addMessage(WhatsAppMessageContainer whatsAppMessageContainer) {
		for(Entry<String, WhatsAppUser> it : usersByName.entrySet()) {
			it.getValue().addMessage(whatsAppMessageContainer);
		}
	}
	
	/**
	 * Remove a user from the group.
	 * @param userName
	 */
	public void removeUser(String userName) {
		if (userExists(userName)) {
			usersByPhone.remove(usersByName.get(userName).getPhoneNumber());
			usersByName.remove(userName);
		}
	}

	/**
	 * Remove a user from the group.
	 * @param userName
	 */
	public void removeUserByPhone(String phoneNumber) {
		if (usersByPhone.containsKey(phoneNumber)) {
			usersByName.remove(usersByPhone.get(phoneNumber).getUserName());
			usersByPhone.remove(phoneNumber);
		}
	}
	@Override
	public String getIdentifier() {
		return name;
	}
}
