package messenger.model.service;

import messenger.model.entity.Room;
import messenger.model.entity.User;

/**
 * Interface RoomService contains methods for service rooms in messenger
 * @author Inna
 */
public interface RoomService {
    /**
     * the method creates new room
     * @param roomName contain String for name creating room
     */
    String  createRoom(String roomName);

    /**
     * the method adds user to room
     * @param user contain object of Class User for adding to room
     * @param room contain object of Class Room
     * @see Room
     * @see User
     */
    void addUserToRoom(User user, Room room);

    /**
     * the method removes user from room
     * @param user contain object of Class User for removing from room
     * @param room contain object of Class Room
     * @see User
     */
    void removeUserFromRoom(User user, Room room);

}
