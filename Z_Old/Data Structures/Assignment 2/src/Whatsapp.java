
public class Whatsapp {
	UserListNode UsersHead;
	Group GroupsHead;
	
		
	public void AddUser(String inUserName)
	{
		
	}
	
	public void CreateGroup(String inInitiator,String inGroupName)
	{
		
	}
	
	public void AddMemberToGroup(String invitorUser,String invitedUser,String inWelcmeText,String inGroupName)
	{
		
	}
	
	public void SendMessage(String inSender,String inText,String inGroupName)
	{
		
	}
	
	
	public void RemoveUserFromGroup(String inUserName,String inGroupName)
	{
		
	}
	
	public Message[] SearchMessageByText(String inUserName,String inText,String inGroupName)
	{
		return null;
	}
	
	
	public String GetSearchMessageByTextString(String inUserName,String inText,String inGroupName)
	{
		Message[] msgs= SearchMessageByText(inUserName, inText,inGroupName);
		String retVal = "";
		for(int i=0; i< msgs.length;i++)
		{
			retVal += msgs[i].toString()+"\n";
		}
		
		return retVal;
	}
	public String GetUsersList()
	{
		String retVal = "";
		UserListNode temp = UsersHead;
		while(temp != null)
		{
			retVal += temp.CurrUser.Name+"\n";
			temp=temp.Next;
		}
		
		return retVal;
	}
	
	public String GetGroupsList()
	{
		String retVal = "";
		Group temp = GroupsHead;
		while(temp != null)
		{
			retVal += temp.GroupName+"\n";
			temp=temp.Next;
		}
		return retVal;
	}
	
	public String GetConversationByUserAndGroup(String inUserName,String inGroupName)
	{
		UserListNode temp = UsersHead;
		while(temp != null)
		{
			if(temp.CurrUser.Name.equals(inUserName))
			{
				return temp.CurrUser.GetConversationByGroupName(inGroupName);
			}
			temp=temp.Next;
		}
		
		return null;
	}
	
	public String GetGroupDetails(String inGroupName)
	{
		Group temp = GroupsHead;
		while(temp != null)
		{
			if(temp.GroupName.equals(inGroupName))
			{
				return temp.toString();
			}
			
			temp=temp.Next;
		}
		
		return null;
	}
	
	public String GetUserConversationList(String inUserName)
	{
		UserListNode temp = UsersHead;
		while(temp != null)
		{
			if(temp.CurrUser.Name.equals(inUserName))
			{
				return temp.CurrUser.GetConversationList();
				
			}
			temp=temp.Next;
		}
		
		return null;
	}
	
}
