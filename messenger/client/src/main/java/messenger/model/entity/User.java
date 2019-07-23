package messenger.model.entity;

import java.io.Serializable;

/**
 * The class for contains info about user
 * @author Inna
 */
public class User implements Serializable {
    private String name;
    private int id;
    private boolean isOnline;
    private boolean isMuted;
    private boolean isBanned;


    public User(String name) {
        this.name = name;
    }

    public User() {
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public boolean isMuted() {
        return isMuted;
    }

    public void setMuted(boolean muted) {
        isMuted = muted;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", isOnline=" + isOnline +
                ", isMuted=" + isMuted +
                ", isBanned=" + isBanned +
                '}';
    }

}
