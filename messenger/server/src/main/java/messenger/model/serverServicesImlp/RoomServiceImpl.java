package messenger.model.serverServicesImlp;

import messenger.controller.Router;
import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.RoomService;

public class RoomServiceImpl implements RoomService {

    @Override
    public void createRoom(String roomName) {
        Room room = new Room(roomName);
        Router.getInstense().getRoomList().add(room);
    }

    @Override
    public void addUserToRoom(UserConnection user, Room room) {
        for (Room r: Router.getInstense().getRoomList()) {
            if(r.getRoomName().equals(room.getRoomName())) {
                r.addUser(user);
            }
        }
    }

    @Override
    public void removeUserFromRoom(UserConnection user, Room room) {
        for (Room r: Router.getInstense().getRoomList()) {
            if(r.getRoomName().equals(room.getRoomName())) {
                r.removeUser(user);
            }
        }
    }
}
