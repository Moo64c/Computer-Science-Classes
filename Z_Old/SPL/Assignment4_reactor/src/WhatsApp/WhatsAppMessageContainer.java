package WhatsApp;

/**
 * Immutable object to hold message information. 
 * @author amirarb
 */

public final class WhatsAppMessageContainer {
	public enum WHATSAPP_MESSAGE_TYPE {DIRECT, GROUP};
	WhatsAppSender sender;
	String message;
	WHATSAPP_MESSAGE_TYPE type;
	
	public WhatsAppMessageContainer(WhatsAppSender _sender, String _message, WHATSAPP_MESSAGE_TYPE _type) {
		message = _message;
		sender = _sender;
		type = _type;
	}
	
	/**
	 * Retrieves the message, formatted with sender's user name.
	 */
	public String getMessage() {
		return "From: " + sender.getIdentifier() + "\nMsg:" + message; 
	}
}
