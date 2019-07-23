package messenger.model.entity;

import java.io.Serializable;

/**
 * the class contains methods for working with messages
 * @author Inna
 */
public class Message implements Serializable {
    private String text;
    private User userSender;
    private String nameRoomRecipient;

    public Message(String text, User userSender, String nameRoomRecipient) {
        this.text = text;
        this.userSender = userSender;
        this.nameRoomRecipient = nameRoomRecipient;
    }

    public Message(String text, User userSender) {
        this.text = text;
        this.userSender = userSender;
    }

    public Message(String text, String nameRoomRecipient) {
        this.text = text;
        this.nameRoomRecipient = nameRoomRecipient;
    }

    public Message() {
    }

    public String getNameRoomRecipient() {
        return nameRoomRecipient;
    }

    public void setNameRoomRecipient(String nameRoomRecipient) {
        this.nameRoomRecipient = nameRoomRecipient;
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
