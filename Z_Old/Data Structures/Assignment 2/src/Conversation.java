
public class Conversation {
	public String ConversationGroupName;
	private Message Head;
	public Conversation Next;
	
	public String toString()
	{
		String retVal = ConversationGroupName+"\n";
		Message temp =Head;
		while(temp!=null)
		{
			retVal += temp.toString()+"\n";
			temp=temp.Next;
		}
		
		return retVal;
	}
}
