package messenger.model;

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
    void addUserToRoom(User user, Room room);

    /**
     * the method removes user from room
     * @param user contain object of Class User for removing from room
     * @param room contain object of Class Room
     * @see Room
     * @see User
     */
    void removeUserFromRoom(User user, Room room);
}
