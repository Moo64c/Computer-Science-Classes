package WhatsApp;

public interface WhatsAppSender {
	public String getIdentifier();
	public void addMessage(WhatsAppMessageContainer whatsAppMessageContainer);
}
