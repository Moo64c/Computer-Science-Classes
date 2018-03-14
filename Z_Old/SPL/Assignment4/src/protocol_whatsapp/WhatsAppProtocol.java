package protocol_whatsapp;

import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import WhatsApp.WhatsAppGroup;
import WhatsApp.WhatsAppMessageContainer;
import WhatsApp.WhatsAppSender;
import WhatsApp.WhatsAppServer;
import WhatsApp.WhatsAppUser;
import protocol.ServerProtocol;
import protocol_http.HttpProtocol;
import tokenizer_http.HttpMessage;
import tokenizer_http.HttpMessage.REQUEST_TYPE;
import tokenizer_whatsapp.WhatsAppMessage;

public class WhatsAppProtocol extends HttpProtocol implements ServerProtocol<HttpMessage> {
    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private final int HTTP_SUCCESS_CODE = 200;
    private final int HTTP_FORBIDDEN_CODE = 403;
    private WhatsAppServer server;
	
    public WhatsAppProtocol(WhatsAppServer _server) {
    	server = _server;
    }
    
    @Override
	public HttpMessage processMessage(HttpMessage msg) {
		HttpMessage httpResponse = super.processMessage(msg);
		
		// If the message is an invalid HTTP message, return an error.
		if (httpResponse.getStatus() != HTTP_SUCCESS_CODE) {
			logger.warning("Bad http message: " + msg);
			return httpResponse;
		}
		
		HttpMessage response = processMessageFromURI(new WhatsAppMessage(msg));
		return response;
	}

	private WhatsAppMessage processMessageFromURI(WhatsAppMessage msg) {
		// Get URI.
		String URI = msg.getRequestURI();
		
		// Generic response variables.
		Map<String, String> headers = new TreeMap<String, String>();
		String messageBody = "";
		
		if (URI.equals("/login.jsp")){
			return login(msg, headers, messageBody);
		}
		
		// Check headers for a cookie in everything except login.
		String cookie = msg.getHeader("Cookie");
		if (cookie == null || cookie.indexOf("user_auth=") == -1) {
			logger.warning("Wrong or no cookie header");
			messageBody = "Incorrect login." + server.getBadCookie();
			return new WhatsAppMessage(headers,messageBody, HTTP_FORBIDDEN_CODE);
		}
		cookie = WhatsAppMessage.trim(cookie.substring(cookie.indexOf("user_auth=") + "user_auth=".length()));
		
		WhatsAppUser user =  server.getUserByCookie(cookie);
		if (user == null) {
			// Bad cookie. Can't perform action.
			messageBody = "Incorrect login." + server.getBadCookie();
			return new WhatsAppMessage(headers,messageBody, HTTP_FORBIDDEN_CODE);
		}
		
		if (URI.equals("/logout.jsp")) {
			return logout(msg, user, headers, messageBody);
		}
		if (URI.equals("/list.jsp")) {
			return list(msg, user, headers, messageBody);
		}
		if (URI.equals("/create_group.jsp")) {
			return createGroup(msg, user, headers, messageBody);
		}
		if (URI.equals("/send.jsp")) {
			return send(msg, user, headers, messageBody);
		}
		if (URI.equals("/add_user.jsp")) {
			return addUser(msg, user, headers, messageBody);
		}
		if (URI.equals("/remove_user.jsp")) {
			return removeUser(msg, user, headers, messageBody);
		}
		if (URI.equals("/queue.jsp")) {
			return queue(msg, user, headers, messageBody);
		}
		
		logger.warning("Bad URI: " + URI);
		return null;
	}
	
	/**
	 * Get user's message queue.
	 */
	private WhatsAppMessage queue(WhatsAppMessage msg, WhatsAppUser user, 
			Map<String, String> headers, String messageBody) {
		if ((msg.getRequestType() != REQUEST_TYPE.GET)) {
			// Illegal operation. Should be POST.
			return requestError(HTTP_FORBIDDEN_CODE);
		}
		
		messageBody = user.getMessages();
		return new WhatsAppMessage(headers, messageBody, HTTP_SUCCESS_CODE);
	}
	
	/**
	 * Remove user from group.
	 */
	private WhatsAppMessage removeUser(WhatsAppMessage msg, WhatsAppUser user, 
			Map<String, String> headers, String messageBody) {
		if ((msg.getRequestType() != REQUEST_TYPE.POST)) {
			// Illegal operation. Should be POST.
			return requestError(HTTP_FORBIDDEN_CODE);
		}
		String target = msg.getMessageParameter("Target");
		String phoneNumber = msg.getMessageParameter("User");
		
		if (target == null || phoneNumber == null) {
			//  Cannot remove, missing parameters.
			return requestError(336);
		}
		
		WhatsAppGroup targetGroup = server.getGroup(target);
		if (targetGroup == null || !targetGroup.userPhoneExists(phoneNumber)) {
			//	Target Does not exist.
			return requestError(769); 
		}

		if (!targetGroup.userExists(user.getUserName())) {
			//	Permission denied - removing user not in group.
			return requestError(668);
		}
		
		// Remove user from the group.
		targetGroup.removeUserByPhone(phoneNumber);
		messageBody = "Removed " + phoneNumber + " successfully.";
		if (!targetGroup.hasUsers()) {
			server.removeGroup(targetGroup);
			messageBody += "\nGroup " + targetGroup.getIdentifier() + " became empty and was deleted.";
		}
		
		return new WhatsAppMessage(headers, messageBody, HTTP_SUCCESS_CODE);
	}

	/**
	 * Add a user to a group.
	 */
	private WhatsAppMessage addUser(WhatsAppMessage msg, WhatsAppUser user, 
			Map<String, String> headers, String messageBody)  {
		if ((msg.getRequestType() != REQUEST_TYPE.POST)) {
			// Illegal operation. Should be POST.
			return requestError(HTTP_FORBIDDEN_CODE);
		}
		String target = msg.getMessageParameter("Target");
		String phoneNumber = msg.getMessageParameter("User");
		
		if (target == null || phoneNumber == null) {
			// Missing parameters.
			return requestError(242);
		}
		
		// Look for group.
		WhatsAppGroup targetGroup = server.getGroup(target);
		if (targetGroup == null) {
			//	Target Does not Exist.
			return requestError(770); 
		}
		
		if (targetGroup.userPhoneExists(phoneNumber)) {
			// Cannot add user, user already in group.
			return requestError(142);
		}
		
		if (!targetGroup.userExists(user.getUserName())) {
			//	Permission denied - adding user not in group.
			return requestError(669);
		}
		
		WhatsAppUser targetUser = server.getUserByPhone(phoneNumber);
		
		targetGroup.addUser(targetUser);
				
		messageBody = phoneNumber + " added to " + target + " group.";
		return new WhatsAppMessage(headers, messageBody, HTTP_SUCCESS_CODE);
	}

	private WhatsAppMessage send(WhatsAppMessage msg, WhatsAppUser user, 
			Map<String, String> headers, String messageBody)  {
		if ((msg.getRequestType() != REQUEST_TYPE.POST)) {
			// Illegal operation. Should be POST.
			return requestError(HTTP_FORBIDDEN_CODE);
		}
		
		String content = msg.getMessageParameter("Content");
		String rawType = msg.getMessageParameter("Type");
		String targetPhone = msg.getMessageParameter("Target");
		
		if (content == null || rawType == null || targetPhone == null) {
			// Missing parameters
			return requestError(711);
		}
		
		WhatsAppMessageContainer.WHATSAPP_MESSAGE_TYPE type = null;
		WhatsAppSender target = null;
		if (rawType.equals("Direct")) {
			type = WhatsAppMessageContainer.WHATSAPP_MESSAGE_TYPE.DIRECT;
			targetPhone = WhatsAppMessage.trim(targetPhone);
			target = server.getUserByPhone(targetPhone);
		}
		else if (rawType.equals("Group")) {
			type = WhatsAppMessageContainer.WHATSAPP_MESSAGE_TYPE.GROUP;
			target = server.getGroup(targetPhone);
		}
		else {
			// Invalid type.
			return requestError(836, rawType);
		}

		// Check that target exists.
		if (target == null) {
			// Target Does not Exist
			return requestError(771, targetPhone);
		}
		
		// Add the message to the queue.
		target.addMessage(new WhatsAppMessageContainer(user, content, type));
		
		// Respond to request.
		messageBody = "Sent successfully.";
		return new WhatsAppMessage(headers, messageBody, HTTP_SUCCESS_CODE);
	}
	
	/**
	 * Create a new group.
	 */
	private WhatsAppMessage createGroup(WhatsAppMessage msg, WhatsAppUser user,
			Map<String, String> headers, String messageBody)  {
		String groupName = msg.getMessageParameter("GroupName");
		String requestUsers = msg.getMessageParameter("Users");
		
		if ((msg.getRequestType() != REQUEST_TYPE.POST)) {
			// Illegal operation. Should be POST.
			return requestError(HTTP_FORBIDDEN_CODE);
		}
		
		if (groupName == null || requestUsers == null) {
			// Missing parameters.
			return requestError(675);
		}
		if (server.getGroup(groupName) != null) {
			// Group name taken.
			return requestError(511);
		}
		
		Map<String, WhatsAppUser> groupUsers = new TreeMap<String, WhatsAppUser>();
		WhatsAppUser currentUser = null;
		String[] userNames = requestUsers.split(","); 
		for (String name : userNames) {
			currentUser = server.getUserByName(name);
			if (currentUser == null) {
				// User was not found.
				return requestError(929, name);
			}
			// User was found.
			groupUsers.put(currentUser.getUserName(), currentUser);
		}
		// Added all the users, group name is available.
		
		// Create group.
		server.addGroup(groupName, new WhatsAppGroup(groupName, groupUsers));
		
		messageBody = "Group " + groupName + " created.";
		
		return new WhatsAppMessage(headers, messageBody, HTTP_SUCCESS_CODE);
	}
	
	/**
	 * List a group, all groups, or all users.
	 */
	private WhatsAppMessage list(WhatsAppMessage msg, WhatsAppUser user, 
			Map<String, String> headers, String messageBody)  {
		String listType = msg.getMessageParameter("List");

		if ((msg.getRequestType() != REQUEST_TYPE.POST)) {
			// Illegal operation. Should be POST.
			return requestError(HTTP_FORBIDDEN_CODE);
		}
		
		if (listType == null) {
			return requestError(273);
		}
		
		if (listType.equals("Group")) {
			String groupName = msg.getMessageParameter("GroupName");
			if (groupName == null) {
				return requestError(273);
			}
			
			WhatsAppGroup group = server.getGroup(groupName);
			if (group == null ){
				// Missing params.
				return requestError(999);
			}
			messageBody = group.getUsers();
		}
		if (listType.equals("Groups")) {
			// Dump all group names.
			messageBody = server.getAllGroups();
		}
		
		if (listType.equals("Users")) {
			// Dump all user names.
			messageBody = server.getAllUsers();
		}

		return new WhatsAppMessage(headers, messageBody, HTTP_SUCCESS_CODE);
	}

	/**
	 * Logout user.
	 */
	private WhatsAppMessage logout(WhatsAppMessage msg, WhatsAppUser user,  
			Map<String, String> headers, String messageBody)  {
		if ((msg.getRequestType() != REQUEST_TYPE.GET)) {
			// Illegal operation: request type, user name or phone number.
			return requestError(HTTP_FORBIDDEN_CODE);
		}
		
		// Just logout and send the goodbye message.
		messageBody = "Goodbye";
		server.removeUser(user);
		
		return new WhatsAppMessage(headers, messageBody, HTTP_SUCCESS_CODE);
	}
	
	/**
	 * Perform login.
	 */
	private WhatsAppMessage login(HttpMessage msg, Map<String, String> headers, String messageBody) {
		WhatsAppMessage appMsg = new WhatsAppMessage(msg);
		// Get login variables.
		String userName = appMsg.getMessageParameter("UserName");
		String phoneNumber = appMsg.getMessageParameter("Phone");

		if ((msg.getRequestType() != REQUEST_TYPE.POST)) {
			// Illegal operation: request type, user name or phone number.
			logger.warning("Failed logging in");
			return requestError(HTTP_FORBIDDEN_CODE);
		}
		
		if (userName == null || phoneNumber == null){
			return requestError(765);
		}
		// Login; generate a cookie.
		String cookie = server.addUser(userName, phoneNumber);
		headers.put("Set-Cookie", "user_auth=" + cookie);
		messageBody = "Welcome " + userName + "@" + phoneNumber;
		
		return new WhatsAppMessage(headers, messageBody, HTTP_SUCCESS_CODE);
	}
	

	/**
	 * Shorthand notation for any error except 929.
	 */
	private WhatsAppMessage requestError(int code) {
		return requestError(code, "");
	}
	
	/**
	 * Generates a error message to shorten the logic in response functions.
	 * @param code
	 *  Error code.
	 * @param name
	 *  Name of user for error  929.
	 * @return
	 *  WhatsAppMessage containing the error message.
	 */
	private WhatsAppMessage requestError(int code, String name) {
		int status = 200;
		Map<String, String> headers = new TreeMap<String,String>();
		// I hate when servers do this, but...
		String messageBody = "ERROR " + code + ": ";
		
		switch (code) {			
			case 142:
				messageBody += "Cannot add user, user already in group.";
				break;
			case 242:
				messageBody += "Cannot add user, missing parameters.";
				break;
			case 273:
			case 675:
				messageBody += "Missing parameters.";
				break;
			case 336:
				messageBody += "Cannot remove, missing parameters.";
				break;
			case HTTP_FORBIDDEN_CODE: 
				status = HTTP_FORBIDDEN_CODE;
				messageBody += "Forbidden or wrong request type.";
				break;
			case 511:
				messageBody += "Group name already taken.";
				break;
			case 668:
			case 669:
				messageBody = "Permission denied.";
				break;
			case 711:
				messageBody += "Cannot send, missing parameters.";
				break;
			case 765:
				messageBody += "Cannot login, missing parameters.";
				break;
			case 769:
			case 770:
			case 771:
				messageBody += "Target [" + name + "] does not exist.";
				break;
			case 836:
				messageBody += "Invalid type: [" + name + "].";
				break;
			case 929:
				messageBody += "Unknown User [" + name + "]. \n Should be a user name";
				break;
			case 999:
				messageBody += "No such group.";
				break;
			default: 
				messageBody += "Invalid error code.";
				break;
		}
		
		logger.warning("WhatsApp request error: " + messageBody);
		return new WhatsAppMessage(headers, messageBody, status);
	}
}
