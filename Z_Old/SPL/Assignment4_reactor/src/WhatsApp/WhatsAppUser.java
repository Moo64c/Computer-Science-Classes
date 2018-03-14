package WhatsApp;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Map.Entry;

/**
 * Holds user information.
 * @author amirarb
 */
public class WhatsAppUser implements WhatsAppSender {
	private String userName;
	private String phoneNumber;
	private UUID id;
	private Queue<WhatsAppMessageContainer> messages;
	private Map<String, WhatsAppGroup> groups;
	
	public WhatsAppUser(String _userName, String _phoneNumber, UUID _id) {
		setUserName(_userName);
		setPhoneNumber(_phoneNumber);
		setId(_id);
		messages = new LinkedList<WhatsAppMessageContainer>();
		groups = new TreeMap<String, WhatsAppGroup>();
	}
	
	public synchronized String getMessages() {
		StringBuilder string = new StringBuilder();
		
		// Append the messages to each other.
		while(!messages.isEmpty()) {
			WhatsAppMessageContainer currentMessage = messages.remove();
			string.append(currentMessage.getMessage());
		}
		
		return string.toString();
	}
	
	public synchronized void addMessage(WhatsAppMessageContainer message) {
		messages.add(message);
	}
	
	public void addGroup(WhatsAppGroup group) {
		groups.put(group.getIdentifier(), group);
	}
	
	/**
	 * Remove self from all the groups currently in.
	 */
	public void exitGroups(WhatsAppServer server) {
		for(Entry<String, WhatsAppGroup> it : groups.entrySet()) {
			it.getValue().removeUser(this.getIdentifier());
			
			if (!it.getValue().hasUsers()) {
				// Removed the last user.
				server.removeGroup(it.getValue());
			}
		}
	}
	
	/**
	 * Display the user as [userName]@[phoneNumber].
	 */
	public String getIdentifier() {
		return getPhoneNumber();
	}
	
	/**
	 * Display the user as [userName]@[phoneNumber].
	 */
	public String toString() {
		return userName + "@" + phoneNumber;
	}
	
	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
}
