package messenger.model;

import messenger.controller.Router;
import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.User;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.RoomService;

public class RoomActivity {
    private UserConnection userConnection;
    private RoomService roomService;
    /**
     * The field roomNow contain info about room where user is now
     */
    private Room roomNow;

    public RoomActivity(UserConnection userConnection, RoomService roomService) {
        this.userConnection = userConnection;
        this.roomService = roomService;

    }

    /**
     * The method sets new value to roomNow
     * @param roomNowData have string with data from client about changing room
     * method calls method changeRoom() from RoomService interface for parsing string from client to object of class Room
     */
    public void setRoomNow(String roomNowData) {
        Room switchedRoom = roomService.changeRoom(roomNowData);
        for (Room room: Router.getInstense().getRoomList()) {
            if(room.getRoomName().equals(switchedRoom.getRoomName())) {
               roomNow = switchedRoom;
            }
        }
    }

    public void createRoom(String roomData) {
        Room room = roomService.createRoom(roomData);
        room.addUser(userConnection);
        Router.getInstense().getRoomList().add(room);
        System.out.println("new room created");

        roomNow = room;
    }

    public void addUserToRoom(String data) {
        User addedUser = roomService.addUserToRoom(data);
        UserConnection connectionAdd = null;
        for (UserConnection conn: Router.getInstense().getUserList()) {
            if(conn.getUser().getName().equals(addedUser.getName())) {
                connectionAdd = conn;
            }
        }
        for (Room r: Router.getInstense().getRoomList()) {
            if(r.getRoomName().equals(roomNow.getRoomName()) && !r.getUserList().contains(connectionAdd)) {
                r.addUser(connectionAdd);
            }
        }
    }
}
