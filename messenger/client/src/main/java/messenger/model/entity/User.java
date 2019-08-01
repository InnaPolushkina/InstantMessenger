package messenger.model.entity;


/**
 * The class for contains info about user
 * @author Inna
 */
public class User {
    private String name;
    private boolean isBanned;

    /**
     * The constructor of this class
     * @param name name of user
     */
    public User(String name) {
        this.name = name;
    }

    /**
     * The empty constructor of this class
     */
    public User() {
    }

    /**
     * The getter for name of user
     * @return name of user
     */
    public String getName() {
        return name;
    }

    /**
     * The setter for name of user
     * @param name name of user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The setter for banned status of user
     * @param banned banned status of user
     */
    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    /**
     * The method for converting object of this class to string
     * @return String with data of user
     */
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", isBanned=" + isBanned +
                '}';
    }

}
