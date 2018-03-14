
public class Message {
	public String SenderName;
	public String Text;
	public Message Next;
		
	public String toString()
	{
		return SenderName+":"+Text;
	}
}
