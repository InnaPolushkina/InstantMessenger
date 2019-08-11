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
     * The method prepares for sending to server info about creating room
     * @param roomName contain String for name creating room
     * @return ready for sending string with data about new room in server format
     */
    String prepareCreateRoom(String roomName);

    /**
     * The method prepares for sending to server user info for add to some room
     * @param user contain object of Class User for adding to room
     * @see Room
     * @see User
     * @return ready for sending string with data about adding user in server format
     */
    String prepareAddUserToRoom(User user);

    /**
     * The method prepares for sending name of room for switching room at the server side
     * @param roomName name of room for switching
     * @return string with switching room data in server format
     */
    String prepareSwitchRoom(String roomName);

    /**
     * The method parse notification from server about adding user to room
     * @param msg string with notification
     * @return object of class Room
     * @see Room
     */
    Room parseNotifyAddedToRoom(String msg);

    /**
     * The method parses list of rooms to string
     * if room was deleted method don't parse it to string
     * @param rooms data of rooms
     * @param lastConnection date of last user disconnecting
     * @return string with data of rooms and date of user disconnection
     * @see Room
     */
    String parseRoomList(Set<Room> rooms, LocalDateTime lastConnection);

    /**
     * The method parses string from server with list of users who can be banned or unbanned
     * @param msg message from server with data
     * @return list of users
     * @see User
     */
    List<User> parseListForBanUnBan(String msg);

    /**
     * The method parses notification from server about deletion some room
     * @param data string with data
     * @return object of class Room
     * @see Room
     */
    Room parseDeletedRoom(String data);

    /**
     * The method prepares for sending to server data about delete room by admin of this room
     * @param roomName name of room for deleting
     * @return string with data about deleting room in server format
     */
    String prepareDeleteRoom(String roomName);

    /**
     * The method prepares for sending to server data about some room
     * @param roomName name of room
     * @return string with data about room in server format
     */
    String prepareForSendRoom(String roomName);

    /**
     * The method parses string with user list from some into List of Users
     * @see User
     * @param data string with list of users in server format
     * @return list of users
     */
    List<User> parseUserListFromRoom(String data);
}
