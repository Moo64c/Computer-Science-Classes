
public class Message {
    public String SenderName;
    public String Text;
    public Message Next;

    /**
     * Generic constructor.
     */
    public Message(String text, String sender) {
        Text = text;
        SenderName = sender;
    }

    // Assignment provided functions.
    public String toString() {
        return SenderName + ":" + Text;
    }
}
