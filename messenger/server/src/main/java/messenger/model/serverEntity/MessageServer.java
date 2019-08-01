package messenger.model.serverEntity;

/**
 * The class contains methods for working with messages data
 */
public class MessageServer {
    private User sender;
    private String text;
    private Room recipient;

    /**
     * The constructor of this class
     * @param sender
     * @param text
     */
    public MessageServer(User sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    /**
     * The getter for message text
     * @return text of message
     */
    public String getText() {
        return text;
    }

    /**
     * The setter for message text
     * @param text text of message
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * The getter for room recipient message
     * @return room
     */
    public Room getRecipient() {
        return recipient;
    }

    /**
     * The setter for recipient of message
     * @param recipient room recipient
     */
    public void setRecipient(Room recipient) {
        this.recipient = recipient;
    }
}
