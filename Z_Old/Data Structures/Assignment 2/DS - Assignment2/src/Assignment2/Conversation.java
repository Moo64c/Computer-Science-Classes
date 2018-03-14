
public class Conversation {
    public String ConversationGroupName;
    public Conversation Next;
    public int conversationSize;
    private Message Head;

    /**
     * Generic constructor.
     */
    public Conversation(String name) {
        ConversationGroupName = name;
        conversationSize = 0;
    }

    /**
     * Iterator starter for the conversation
     *
     * @return first message in the conversation.
     */
    public Message getFirstMessage() {
        return Head;
    }

    /**
     * Adds a message to the conversation.
     *
     * @param text   Message text.
     * @param sender Message sender.
     */
    public void addMessage(String text, String sender) {
        Message messageIterator = Head;
        Message newMessage = new Message(text, sender);
        conversationSize++;

        if (messageIterator == null) {
            Head = newMessage;
            return;
        }
        while (messageIterator.Next != null) {
            messageIterator = messageIterator.Next;
        }
        messageIterator.Next = newMessage;
    }

    public String toString() {
        String retVal = ConversationGroupName + "\n";
        Message temp = Head;
        while (temp != null) {
            retVal += temp.toString() + "\n";
            temp = temp.Next;
        }

        return retVal;
    }
}
