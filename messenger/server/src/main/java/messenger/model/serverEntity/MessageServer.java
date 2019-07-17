package messenger.model.serverEntity;



import java.io.Serializable;

public class MessageServer implements Serializable {
    private User sender;
    private String text;
    private Room recipient;

    public MessageServer(User sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Room getRecipient() {
        return recipient;
    }

    public void setRecipient(Room recipient) {
        this.recipient = recipient;
    }
}
