package messenger.model.serverEntity;

/**
 * The class contains methods for working with messages data
 */
public class MessageServer {
    private String text;
    private Room recipient;

    /**
     * The constructor of this class
     * @param text
     */
    public MessageServer( String text) {
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
