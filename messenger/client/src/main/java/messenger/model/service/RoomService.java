package messenger.model.service;

import messenger.model.entity.Room;
import messenger.model.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
     * @param //room contain object of Class Room
     * @see Room
     * @see User
     */
    String addUserToRoom(User user);

    /**
     * the method removes user from room
     * @param user contain object of Class User for removing from room
     * @param room contain object of Class Room
     * @see User
     */
    void removeUserFromRoom(User user, Room room);

    /**
     * The method for parsing name of room for switching room at the server side
     * @param roomName name of room for switching
     * @return string with switching room in server format
     */
    String switchRoom(String roomName);

    //List<User> parseOnlineUsers(String listOnline);

    /**
     * The method parse notification from server about adding user to room
     * @param msg string with notification
     * @return object of class Room
     * @see Room
     */
    Room parseNotifyAddedToRoom(String msg);

    String parseRoomList(Set<Room> rooms, LocalDateTime lastConnection);
}
