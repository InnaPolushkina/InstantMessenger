package messenger.model.serverServices;

import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.User;
import messenger.model.serverEntity.UserConnection;

import java.util.List;


public interface RoomService {
    /**
     * the method creates new room
     * @param roomData contain String for name creating room
     */
    Room createRoom(String roomData);

    /**
     * the method adds user to room
     * @param //user contain object of Class User for adding to room
     * @param //room contain object of Class Room
     * @see Room
     * @see User
     */
    User addUserToRoom(/*UserConnection user, Room room*/ String data);

    /**
     * the method removes user from room
     * @param user contain object of Class User for removing from room
     * @param room contain object of Class Room
     * @see Room
     * @see User
     */
    void removeUserFromRoom(UserConnection user, Room room);

    /**
     * the method switches room for user
     * @param roomName have name for going to need room
     * @return Room where user is now
     */
    Room changeRoom(String roomName);

    List<String> parseListOfRooms(String data);

    String parseListUserForBan(List<User> list);

    String parseListUserForUnBan(List<User> list);
}
