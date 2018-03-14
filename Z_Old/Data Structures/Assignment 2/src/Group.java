
public class Group 
{
	public String GroupName;
	private UserListNode Head;
	public Group Next;
	
	public String toString()
	{
		String retVal = GroupName+"\n";
		UserListNode temp = Head;
		while(temp != null)
		{
			retVal += temp.CurrUser.Name+"\n";
			temp=temp.Next;
		}
		return retVal;
	}
}