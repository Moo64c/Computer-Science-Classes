
public class Whatsapp {
    UserListNode UsersHead;
    UserListNode UsersTail; // An additional pointer to the last UserListNode in the list.
    Group GroupsHead;
    Group GroupsTail; // An additional pointer to the last group in the group linked list.

    /**
     * Adds a user to the list of users
     *
     * @param inUserName the name of the user to be added.
     */
    public void AddUser(String inUserName) {
        UserListNode newUserListNode = new UserListNode(new User(inUserName));

        if (this.UsersHead == null) {
            this.UsersHead = newUserListNode;
            this.UsersTail = this.UsersHead;
        }
        else {
            this.UsersTail.Next = newUserListNode;
            this.UsersTail = this.UsersTail.Next;
        }
    }

    /**
     * Creates a new group for conversation. The initiator user is automatically added to the group.
     * @param inInitiator the name of the initiating user.
     * @param inGroupName the name of the new group.
     */
    public void CreateGroup(String inInitiator, String inGroupName) {
        User initUser = UsersHead.findUser(inInitiator);
        Group newGroup = new Group(initUser, inGroupName);
        initUser.addConversation(inGroupName);

        if (this.GroupsHead == null) {
            GroupsHead = newGroup;
            GroupsTail = GroupsHead;
        }
        else {
            GroupsTail.Next = newGroup;
            GroupsTail = GroupsTail.Next;
        }
    }


    /**
     * Adds a new member to an existing group, and sends a welcome message.
     * @param invitorUser  the user who invited the new user
     * @param invitedUser  the new user which was added
     * @param inWelcmeText the text that will be sent in the message
     * @param inGroupName  the name of the group.
     */
    public void AddMemberToGroup(String invitorUser, String invitedUser, String inWelcmeText, String inGroupName) {
        // Find user and group.
        User userInvited = UsersHead.findUser(invitedUser);
        Group addingGroup = findGroup(inGroupName);

        // Add user to group and add a new conversation to the user.
        addingGroup.addUser(userInvited);
        userInvited.addConversation(inGroupName);

        // Send welcome message.
        SendMessage(invitorUser, inWelcmeText, inGroupName);
    }

    /**
     * Sends a message in a group.
     * @param inSender    User name that sent the message.
     * @param inText      Text to send.
     * @param inGroupName Group the message was sent in.
     */
    public void SendMessage(String inSender, String inText, String inGroupName) {
        // Find group.
        Group group = findGroup(inGroupName);
        if (group == null) {
            return;
        }


        // For every user in the group, add message to the conversation.
        UserListNode currentUserNode = group.getUserList();
        while (currentUserNode != null) {
            // Add message to the relevant conversation.
            currentUserNode.CurrUser.addMessage(inGroupName, inText, inSender);
            currentUserNode = currentUserNode.Next;
        }
    }

    /**
     * Auxiliary function - seeks the groups according to name.
     *
     * @param name Group to search for.
     * @return The group with name.
     */
    public Group findGroup(String name) {
        Group groupSearcher = GroupsHead;

        while ((groupSearcher != null) && (!(groupSearcher.GroupName.equals(name)))) {
            groupSearcher = groupSearcher.Next;
        }
        return groupSearcher;
    }


    /**
     * removes a selected user from a specific group.
     *
     * @param inUserName  the user to remove.
     * @param inGroupName the group that user is removed from.
     */
    public void RemoveUserFromGroup(String inUserName, String inGroupName) {
        Group removingGroup = findGroup(inGroupName);
        User removedUser = removingGroup.removeUser(inUserName);
        removedUser.removeConversation(inGroupName);

        if (removingGroup.isEmpty()) {
            removeGroup(inGroupName);
        }
    }

    /**
     * Auxiliary function - removes a Group from a list.
     *
     * @param inGroupName the name of the group to be removed.
     */
    public void removeGroup(String inGroupName) {
        Group iteratingGroup = GroupsHead;
        // edge case: if the user is in the first node,
        if (iteratingGroup.GroupName.equals(inGroupName)) {
            if (GroupsHead == GroupsTail) {
                GroupsHead = null;
                GroupsTail = null;
            }
            else {
                GroupsHead = GroupsHead.Next;
            }
            return;
        }

        // get the group node that is before the group for deleted.
        while (iteratingGroup.Next.GroupName.equals(inGroupName)) {
            iteratingGroup = iteratingGroup.Next;
        }

        // remove the group from the list.
        if (iteratingGroup.Next.equals(GroupsTail)) {
            GroupsTail = iteratingGroup;
        }
        iteratingGroup.Next = iteratingGroup.Next.Next;

    }

    /**
     * Search message by a text in the message.
     * @param inUserName User to check.
     * @param inText Text to search for.
     * @param inGroupName Conversation group.
     * @return Array of messages that were found.
     */
    public Message[] SearchMessageByText(String inUserName, String inText, String inGroupName) {
        // Find user.
        User user = UsersHead.findUser(inUserName);

        // Find conversation.
        Conversation conversation = user.GetConversationObjectByGroupName(inGroupName);
        if (conversation == null) {
            return new Message[0];
        }

        // Find relevant message in the conversation.
        Message[] foundMessages = new Message[conversation.conversationSize];
        Message messageIterator = conversation.getFirstMessage();
        int numberFound = 0;

        while (messageIterator != null) {
            if (messageIterator.toString().indexOf(inText) != -1) {
                // Found a message with the text.
                foundMessages[numberFound] = messageIterator;
                numberFound++;
            }
            messageIterator = messageIterator.Next;
        }

        // Shrink array size.
        Message[] shrunkFoundMessages = new Message[numberFound];
        for (int i = 0; i <= numberFound - 1; i++) {
            shrunkFoundMessages[i] = foundMessages[i];
        }

        return shrunkFoundMessages;
    }

    // Assignment provided functions.
    public String GetSearchMessageByTextString(String inUserName, String inText, String inGroupName) {
        Message[] msgs = SearchMessageByText(inUserName, inText, inGroupName);
        String retVal = "";
        for (int i = 0; i < msgs.length; i++) {
            retVal += msgs[i].toString() + "\n";
        }

        return retVal;
    }

    public String GetUsersList() {
        String retVal = "";
        UserListNode temp = UsersHead;
        while (temp != null) {
            retVal += temp.CurrUser.Name + "\n";
            temp = temp.Next;
        }

        return retVal;
    }

    public String GetGroupsList() {
        String retVal = "";
        Group temp = GroupsHead;
        while (temp != null) {
            retVal += temp.GroupName + "\n";
            temp = temp.Next;
        }
        return retVal;
    }

    public String GetConversationByUserAndGroup(String inUserName, String inGroupName) {
        UserListNode temp = UsersHead;
        while (temp != null) {
            if (temp.CurrUser.Name.equals(inUserName)) {
                return temp.CurrUser.GetConversationByGroupName(inGroupName);
            }
            temp = temp.Next;
        }

        return null;
    }

    public String GetGroupDetails(String inGroupName) {
        Group temp = GroupsHead;
        while (temp != null) {
            if (temp.GroupName.equals(inGroupName)) {
                return temp.toString();
            }

            temp = temp.Next;
        }

        return null;
    }

    public String GetUserConversationList(String inUserName) {
        UserListNode temp = UsersHead;
        while (temp != null) {
            if (temp.CurrUser.Name.equals(inUserName)) {
                return temp.CurrUser.GetConversationList();

            }
            temp = temp.Next;
        }

        return null;
    }

}
