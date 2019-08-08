package messenger.model.serverServices;

import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.User;

import java.util.List;


public interface RoomService {
    /**
     * the method creates new room
     * @param roomData contain String for name creating room
     */
    Room createRoom(String roomData);

    /**
     * the method parse user data for adding to room
     * @see Room
     * @see User
     */
    User addUserToRoom(/*UserConnection user, Room room*/ String data);

    /**
     * the method switches room for user
     * @param roomName have name for going to need room
     * @return Room where user is now
     */
    Room changeRoom(String roomName);

    /**
     * The method parses list of rooms
     * @param data string from user with room data
     * @return list of rooms
     */
    List<Room> parseListOfRooms(String data);

    /**
     * The method parses for sending to user list of user, who can be banned or unbanned
     * @param list
     * @return
     */
    String parseListUserForBan(List<User> list);

    /**
     * The method parses data from user about deleting room
     * @param data data about deleting room
     * @return name of room for deleting
     */
    String deleteRoom(String data);

    /**
     * The method parses for sending notification about deletion room
     * @param roomName name of deleted room
     * @return string with notification about deleted room
     */
    String deletedRoomNotification(String roomName);

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
