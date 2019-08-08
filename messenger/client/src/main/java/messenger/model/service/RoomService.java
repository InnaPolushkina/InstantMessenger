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
     * @see Room
     * @see User
     */
    String addUserToRoom(User user);

    /**
     * The method for parsing name of room for switching room at the server side
     * @param roomName name of room for switching
     * @return string with switching room in server format
     */
    String switchRoom(String roomName);

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
     * The method for parses message to server about deleting room where user is admin
     * @param roomName name of room for deleting
     * @return string with message to server
     */
    String deleteRoom(String roomName);

    String prepareForSendRoom(String roomName);

    List<User> parseUserListFromRoom(String data);
}
