
public class User {
	public String Name;
	private Conversation ConversationsQueue;
	

	
	public String GetConversationByGroupName(String inGroupName)
	{
		Conversation temp = ConversationsQueue;
		while(temp!=null)
		{
			if(temp.ConversationGroupName.equals(inGroupName))
			{		
				return temp.toString();
			}
			temp=temp.Next;
		}
		
		return null;
	}
	
	
	public String GetConversationList()
	{
		String retVal = "";
		Conversation temp=ConversationsQueue;
		while(temp!=null)
		{
			retVal += temp.ConversationGroupName+"\n";
			temp=temp.Next;
		}
		return retVal;
	}
}

	