package messenger.model.entity;

/**
 * The class for contains info about user
 * @author Inna
 */
public class User {
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

    /* private Socket userSocket;
    private boolean isOnline;
    private BufferedReader in;
    private BufferedWriter out;
    private BufferedReader userMes;



    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public BufferedWriter getOut() {
        return out;
    }

    public void setOut(BufferedWriter out) {
        this.out = out;
    }

    public BufferedReader getUserMes() {
        return userMes;
    }

    public void setUserMes(BufferedReader userMes) {
        this.userMes = userMes;
    }

    private Map<Room, ArrayList<MessageServer>> chatText;



    public Socket getUserSocket() {
        return userSocket;
    }

    public void setUserSocket(Socket userSocket) {
        this.userSocket = userSocket;
    }

    public String getUserStatus() {
        if (isOnline) {
            return "Online";
        }
        else {
            return "Offline";
        }
    }
    public boolean isOnline() {
        return isOnline;
    }

    public Map<Room, ArrayList<MessageServer>> getChatText() {
        return chatText;
    }

    public void setChatText(Map<Room, ArrayList<MessageServer>> chatText) {
        this.chatText = chatText;
    }*/
}
