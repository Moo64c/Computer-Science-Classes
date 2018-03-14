
public class Main {
	public static void main(String[] args) 
	{
		Whatsapp wa = new Whatsapp();
		wa.AddUser("John");
		wa.AddUser("Paul");
		wa.AddUser("George");
		wa.AddUser("Ringo");
		wa.AddUser("Yoko");
		wa.AddUser("Martin");
		wa.CreateGroup("John","Beatles");
		wa.AddMemberToGroup("John","George","Hey George","Beatles");
		wa.AddMemberToGroup("George","Ringo","Hey Ringo","Beatles");
		wa.SendMessage("John","I like Yoko","Beatles");
		wa.CreateGroup("John","John&Yoko");
		System.out.println("***This the current users list");
		System.out.println(wa.GetUsersList());
		System.out.println("***This the current groups list");
		System.out.println(wa.GetGroupsList());
		System.out.println("***These are the current details of the group - John&Yoko");
		System.out.println(wa.GetGroupDetails("John&Yoko"));
		wa.AddMemberToGroup("John","Yoko","Love me do","John&Yoko");
		System.out.println("***This is the current converstaion at John's phone in the group - John&Yoko");
		System.out.println(wa.GetConversationByUserAndGroup("John","John&Yoko"));
		System.out.println("***This is the current converstaion at John's phone in the group - Beatles");
		System.out.println(wa.GetConversationByUserAndGroup("John","Beatles"));
		System.out.println("***This is the current converstaion list at John's phone");
		System.out.println(wa.GetUserConversationList("John"));
		System.out.println("***These are all the messages containing the text 'He' in John's phone in the group - Beatles");
		System.out.println(wa.GetSearchMessageByTextString("John", "He","Beatles"));
		wa.RemoveUserFromGroup("John","Beatles");
		wa.RemoveUserFromGroup("George","Beatles");
		System.out.println("***This the current groups list");
		System.out.println(wa.GetGroupsList());
		wa.RemoveUserFromGroup("Ringo","Beatles");
		System.out.println("***This the current groups list");
		System.out.println(wa.GetGroupsList());
	}
	
}
