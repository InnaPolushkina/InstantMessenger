package messenger.model.serverServices;

import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.User;
import messenger.model.serverEntity.UserConnection;

public interface RoomService {
    /**
     * the method creates new room
     * @param roomName contain String for name creating room
     */
    void createRoom(String roomName);

    /**
     * the method adds user to room
     * @param user contain object of Class User for adding to room
     * @param room contain object of Class Room
     * @see Room
     * @see User
     */
    void addUserToRoom(UserConnection user, Room room);

    /**
     * the method removes user from room
     * @param user contain object of Class User for removing from room
     * @param room contain object of Class Room
     * @see Room
     * @see User
     */
    void removeUserFromRoom(UserConnection user, Room room);
}
