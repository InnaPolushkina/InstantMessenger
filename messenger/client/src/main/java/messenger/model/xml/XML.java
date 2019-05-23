package messenger.model.xml;

public interface XML {
    void createRoom();
    void reg();
    void auth();
    void addUserToRoom();
    void removeUserFromRoom();
    void sendMessage();
    void bun();
    void unbun();
    void mute();
    void unmute();
    void parseMessage();
}
