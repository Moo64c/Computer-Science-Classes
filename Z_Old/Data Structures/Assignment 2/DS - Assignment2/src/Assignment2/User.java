
public class User {
    public String Name;
    private Conversation ConversationsQueue;

    /**
     * Generic constructor.
     */
    public User(String name) {
        Name = name;
    }

    /**
     * Adds a message to a conversation the user is in.
     *
     * @param groupName Name of the group the message is being sent for.
     * @param text      Text of the message.
     * @param sender    Sender of the message.
     */
    public void addMessage(String groupName, String text, String sender) {
        Conversation conversation = ConversationsQueue;
        Conversation previousConversation = null;
        boolean found = false;

        if (conversation.ConversationGroupName.equals(groupName)) {
            conversation.addMessage(text, sender);
            found = true;
        }

        while (conversation != null && !found) {
            if (conversation.ConversationGroupName.equals(groupName)) {
                // Add the message.
                conversation.addMessage(text, sender);
                // Move the conversation to the top of the list.
                if (previousConversation != null) {
                    previousConversation.Next = conversation.Next;
                }
                conversation.Next = ConversationsQueue;
                ConversationsQueue = conversation;

                // Stop searching.
                found = true;
            }
            else {
                // Run next iteration.
                previousConversation = conversation;
                conversation = conversation.Next;
            }
        }
    }

    /**
     * Get a conversation by the group name.
     *
     * @param groupName Name of the group to look for.
     * @return Conversation
     *         Group's conversation.
     */
    public Conversation GetConversationObjectByGroupName(String groupName) {
        Conversation conversationIterator = ConversationsQueue;
        while (conversationIterator != null) {
            if (conversationIterator.ConversationGroupName.equals(groupName)) {
                return conversationIterator;
            }
            conversationIterator = conversationIterator.Next;
        }

        return conversationIterator;
    }

    /**
     * Auxiliary function - adds a conversation to the users conversation list.
     *
     * @param conversationName the conversation to add.
     */
    public void addConversation(String conversationName) {
        Conversation newConversation = new Conversation(conversationName);

        Conversation firstConversation = ConversationsQueue;
        ConversationsQueue = newConversation;
        newConversation.Next = firstConversation;
    }

    /**
     * Auxiliary function - removes a conversation from a users conversations.
     *
     * @param conversationName the name of conversation which will be removed.
     */
    public void removeConversation(String conversationName) {
        Conversation iteratingConversation = ConversationsQueue;
        if (iteratingConversation == null) {
            return;
        }
        // edge case: if the user is in the first node,
        if (conversationName.equals(iteratingConversation.ConversationGroupName)) {
            ConversationsQueue = ConversationsQueue.Next;
            return;
        }

        // get the group node that is before the group for deleted.
        while ((iteratingConversation.Next != null) && !(iteratingConversation.Next.ConversationGroupName.equals(conversationName))) {
            iteratingConversation = iteratingConversation.Next;
        }

        // remove the group from the list.
        iteratingConversation.Next = iteratingConversation.Next.Next;
    }

    // Assignment provided functions.
    public String GetConversationByGroupName(String inGroupName) {
        Conversation temp = ConversationsQueue;
        while (temp != null) {
            if (temp.ConversationGroupName.equals(inGroupName)) {
                return temp.toString();
            }
            temp = temp.Next;
        }

        return null;
    }

    public String GetConversationList() {
        String retVal = "";
        Conversation temp = ConversationsQueue;
        while (temp != null) {
            retVal += temp.ConversationGroupName + "\n";
            temp = temp.Next;
        }
        return retVal;
    }

}
