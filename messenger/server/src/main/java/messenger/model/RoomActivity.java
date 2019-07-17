package messenger.model;

import messenger.controller.Router;
import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.RoomService;

public class RoomActivity {
    private UserConnection userConnection;
    private RoomService roomService;

    public RoomActivity(UserConnection userConnection, RoomService roomService) {
        this.userConnection = userConnection;
        this.roomService = roomService;
    }

    public void createRoom(String roomData) {
        Room room = roomService.createRoom(roomData);
        //room.addUser(userConnection);
        Router.getInstense().getRoomList().add(room);
        System.out.println("new room created");
    }
}
