package messenger.model.entity;

/**
 * the class contains methods for working with messages
 * @author Inna
 */
public class Message {
    private String text;
    private User userSender;
    private Room roomRecipient;

    public Message(String text, User userSender, Room roomRecipient) {
        this.text = text;
        this.userSender = userSender;
        this.roomRecipient = roomRecipient;
    }

    public Message(String text, User userSender) {
        this.text = text;
        this.userSender = userSender;
    }

    public Message() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUserSender() {
        return userSender;
    }

    public void setUserSender(User userSender) {
        this.userSender = userSender;
    }

    @Override
    public String toString() {
        return "Message from " + userSender.getName() + ": \n " + text;
        /*return "Message {" +
                "text='" + text + '\'' +
                ", userSender=" + userSender.getName() +
                '}';*/
    }
}
