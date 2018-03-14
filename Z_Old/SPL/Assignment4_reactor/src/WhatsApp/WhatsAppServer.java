package WhatsApp;

import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Hold Whatsapp client & group information.
 * @author amirarb
 */
public class WhatsAppServer {
    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	protected Map<UUID, WhatsAppUser> usersByUUID;
	protected Map<String, WhatsAppUser> usersByName;
	protected Map<String, WhatsAppUser> usersByPhone;
	protected Map<String, WhatsAppGroup> groups;
	
	public WhatsAppServer() {
		usersByUUID = new TreeMap<UUID, WhatsAppUser>();
		usersByName = new TreeMap<String, WhatsAppUser>();
		usersByPhone = new TreeMap<String, WhatsAppUser>();
		groups = new TreeMap<String, WhatsAppGroup>();
	}
	
	
	/**
	 * Get a group from the map.
	 * @return 
	 */
	public WhatsAppGroup getGroup(String name) {
		return groups.get(name);
	}
	
	/**
	 * Add a new group to the server.
	 * @param group
	 */
	public void addGroup(String name, WhatsAppGroup group) {
		groups.put(name, group);
	}
	
	/**
	 * Add a new user to the server.
	 * @param userName
	 * @param phoneNumber
	 * @return
	 *  Cookie hash.
	 */
	public String addUser(String userName, String phoneNumber) {
		UUID id = UUID.nameUUIDFromBytes(phoneNumber.getBytes());
		WhatsAppUser user = new WhatsAppUser(userName, phoneNumber, id);
		usersByUUID.put(id, user);
		usersByName.put(userName, user);
		usersByPhone.put(phoneNumber, user);
		return id.toString();
	}
	
	/**
	 * Gets a user from cookie, if exists.
	 * @param cookie
	 * @return WhatsAppUser
	 *  User specified from the cookie.
	 */
	public WhatsAppUser getUserByCookie(String cookie) {
		logger.info("Getting user from cookie: [" + cookie + "]");
		UUID id = UUID.fromString(cookie);
		return usersByUUID.get(id);
	}
	
	/**
	 * Gets a user from it's name. 
	 * @return WhatsAppUser
	 *  User specified from the name.
	 */
	public WhatsAppUser getUserByName(String userName) {
		logger.info("looking for user by user name: [" + userName + "]");
		return usersByName.get(userName);
	}

	/**
	 * Gets a user from it's phone number. 
	 * @return WhatsAppUser
	 *  User specified from the phone number.
	 */
	public WhatsAppUser getUserByPhone(String phoneNumber) {
		logger.info("looking for user by phone number: [" + phoneNumber + "]");
		return usersByPhone.get(phoneNumber);
	}
	
	/**
	 * Remove user from the server.
	 * @param cookie
	 */
	public void removeUser(WhatsAppUser user) {
		user.exitGroups(this);
		usersByUUID.remove(user.getId());
	}
	
	/**
	 * Generate string display all users.
	 */
	public String getAllUsers() {
		StringBuilder string = new StringBuilder();
		
		for(Entry<UUID, WhatsAppUser> it : usersByUUID.entrySet()) {
			string.append(it.getValue().toString() + "\n");
		}
		
		return string.toString();
	}
	/**
	 * Generate string display all groups.
	 */
	public String getAllGroups() {
		StringBuilder string = new StringBuilder();
		
		for(Entry<String, WhatsAppGroup> it : groups.entrySet()) {
			string.append(it.getValue().toString() + "\n");
		}
		
		return string.toString();
	}
	
	/**
	 * Removes a group from the server.
	 */
	public void removeGroup(WhatsAppGroup targetGroup) {
		groups.remove(targetGroup.getIdentifier());
	}
	
	/**
	 * Most important function. DO NOT REMOVE!
	 */
	public String getBadCookie() {
		String yummy = "\n" + 
			"                .---. .---. \n" + 
			"                :     : o   :    me want cookie! \n" +
			"           _..-:   o :     :-.._     \n" + 
			"       .-''  '  `---' `---' \"   ``-. \n" +   
			"     .'   \"   '  \"  .    \"  . '  \"  `. \n"  +
			"    :   '.---.,,.,...,.,.,.,..---.  ' ; \n" +
			"    `. \" `.                     .' \" .' \n" +
			"     `.  '`.                   .' ' .' \n" +
			"      `.    `-._           _.-' \"  .'  .----. \n" +
			"        `. \"    '\"--...--\"'  . ' .'  .'  o   `. \n" +
			"        .'`-._'    \" .     \" _.-'`. :       o  : \n" +
			"     .'      ```--.....--'''    ' `:_ o       : \n" +
			"    .'    \"     '         \"     \"   ; `.;\";\";\";' \n" +
			"   ;         '       \"       '     . ; .' ; ; ; \n" +
			"  ;     '         '       '   \"    .'      .-' \n"+
			"  '  \"     \"   '      \"           \"    _.-' \n";
		return yummy;
	}

}
