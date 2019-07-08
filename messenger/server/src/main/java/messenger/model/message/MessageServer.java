package messenger.model.message;

import messenger.model.User;

import java.io.Serializable;

public class MessageServer implements Serializable {
    private User user;
    private String text;

    public MessageServer(User user, String text) {
        this.user = user;
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
