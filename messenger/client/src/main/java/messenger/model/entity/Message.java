package messenger.model.entity;


/**
 * the class contains methods for working with messages
 * @author Inna
 */
public class Message {
    private String text;
    private User userSender;
    private String nameRoomRecipient;

    /**
     * The constructor of this class
     * @param text messages text
     * @param userSender sender of message
     * @param nameRoomRecipient room where is messages from
     */
    public Message(String text, User userSender, String nameRoomRecipient) {
        this.text = text;
        this.userSender = userSender;
        this.nameRoomRecipient = nameRoomRecipient;
    }

    /**
     * The constructor of this class
     * @param text messages text
     * @param userSender sender of message
     */
    public Message(String text, User userSender) {
        this.text = text;
        this.userSender = userSender;
    }

    /**
     * The getter for room where is messages from
     * @return
     */
    public String getNameRoomRecipient() {
        return nameRoomRecipient;
    }

    /**
     * The getter for text of message
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * The setter for message text
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * The getter for sender of message
     * @return
     */
    public User getUserSender() {
        return userSender;
    }

    /**
     * The method for converting object of this class to String
     * @return string with message data
     */
    @Override
    public String toString() {
        return "Message from " + userSender.getName() + ": \n " + text;
    }
}
