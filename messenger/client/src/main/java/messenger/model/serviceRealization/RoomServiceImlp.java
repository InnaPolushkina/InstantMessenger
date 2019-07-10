package messenger.model.serviceRealization;

import messenger.model.entity.Room;
import messenger.model.entity.User;
import messenger.model.service.RoomService;
import org.apache.log4j.Logger;

public class RoomServiceImlp implements RoomService {

    private static final Logger logger = Logger.getLogger(RoomServiceImlp.class);

    /**
     * the method creates new room
     *
     * @param roomName contain String for name creating room
     */
    @Override
    public void createRoom(String roomName) {

    }

    /**
     * the method adds user to room
     *
     * @param user contain object of Class User for adding to room
     * @param room contain object of Class Room
     * @see Room
     * @see User
     */
    @Override
    public void addUserToRoom(User user, Room room) {

    }

    /**
     * the method removes user from room
     *
     * @param user contain object of Class User for removing from room
     * @param room contain object of Class Room
     * @see User
     */
    @Override
    public void removeUserFromRoom(User user, Room room) {

    }
}
