package messenger.model.serverServices;

import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.UserConnection;

import java.util.Set;

/**
 * The interface RoomKeeper contains methods for keeping info about rooms at the server
 */
public interface RoomKeeper {
    /**
     * The method saves at the server info about user in rooms
     * @param rooms rooms for saving
     * @see Room
     */
    void saveRoomsInfo(Set<Room> rooms);

    /**
     * The method loads from server info about users in rooms and transform info Set of rooms
     * @return Set of Room
     * @see Room
     */
    Set<Room> loadRoomsInfo();

    /**
     * The method parses into string info about rooms where is user
     * @param rooms set of rooms
     * @param user for searching info about rooms
     * @return ready for sending to client string with data about rooms of one client
     */
    String roomsToString(Set<Room> rooms, UserConnection user);
}
