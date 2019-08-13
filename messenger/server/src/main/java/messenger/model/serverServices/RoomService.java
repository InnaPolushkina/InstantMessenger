package messenger.model.serverServices;

import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.User;

import java.util.List;

/**
 * The interface for servicing messages between client and server regarding activity in room
 */
public interface RoomService {
    /**
     * The method parses string from client with data for creating new room
     * @param roomData contain String with data for new room
     */
    Room parseNewRoomData(String roomData);

    /**
     * The method parses user data for adding to room
     * @see Room
     * @see User
     */
    User parseUserForAddToRoom(String data);

    /**
     * The method parses string from client with data for switching room
     * @param data have string from client
     * @return object of class Room with data for switching room
     */
    Room parseRoomForSwitch(String data);

    /**
     * The method parses list of rooms
     * @param data string from user with room data
     * @return list of rooms
     */
    List<Room> parseListOfRooms(String data);

    /**
     * The method prepares for sending to admin of room list of users, who can be banned in room
     * @param list user for banning
     * @return ready string for sending to client with list of user for banning in room
     */
    String prepareListUserForBan(List<User> list);

    /**
     * The method parses data from user about deleting room
     * @param data data about deleting room
     * @return name of room for deleting
     */
    String parseRoomNameForDelete(String data);

    /**
     * The method parses for sending notification about deletion room
     * @param roomName name of deleted room
     * @return string with notification about deleted room
     */
    String prepareDeletedRoomNotification(String roomName);

    /**
     * The method prepares users from some room for sending
     * @param room room where from users will be preparing for sending
     * @return string with data about users in room
     */
    String prepareUsersForSending(Room room);

    /**
     * The method for parsing message from client with room name
     * @param data message from client with room name
     * @return room name
     */
    String parseRoomName(String data);
}
