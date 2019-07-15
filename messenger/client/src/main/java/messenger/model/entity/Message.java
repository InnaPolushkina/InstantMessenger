package messenger.model.entity;

/**
 * the class contains methods for working with messages
 * @author Inna
 */
public class Message {
    private String text;
    private User user;

    public Message(String text, User user) {
        this.text = text;
        this.user = user;
    }

    public Message() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Message from " + user.getName() + ": \n " + text;
        /*return "Message {" +
                "text='" + text + '\'' +
                ", user=" + user.getName() +
                '}';*/
    }
}
