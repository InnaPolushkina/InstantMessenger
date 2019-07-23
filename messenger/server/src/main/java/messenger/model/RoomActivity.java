package messenger.model;

import messenger.controller.Router;
import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.User;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.RoomService;
import org.apache.log4j.Logger;

import java.io.IOException;


public class RoomActivity {
    private UserConnection userConnection;
    private RoomService roomService;
    private static final Logger logger = Logger.getLogger(RoomActivity.class);
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
            if(r.getRoomName().equals(roomNow.getRoomName()) && !r.getUserList().contains(connectionAdd) && connectionAdd != null) {
                r.addUser(connectionAdd);
                System.out.println("user successfully added");
                try {
                    connectionAdd.getOut().write("<action>ADDED_TO_ROOM</action>\n<room>" + r.getRoomName() + "</room>\n");
                    connectionAdd.getOut().flush();
                }catch (IOException e) {
                    logger.warn("while sending notify to user about addition to room ",e);
                }
               /* try {
                    connectionAdd.getOut().write("<add>" + r.getRoomName() + "</add> " + "\n");
                    connectionAdd.getOut().flush();
                }
                catch (IOException e) {
                   // System.out.println(e);
                   //logger.warn(e);
                }*/
            }
            else {
                logger.warn("can't add offline user to chat");
            }
        }


    }

    /*public void getOnlineUsers(UserKeeper userKeeper) {
        List<User> userList = new ArrayList<>();
        for (UserConnection connection: Router.getInstense().getUserList()) {
            userList.add(connection.getUser());
            System.out.println("online user" + connection.getUser().getName() + "added to list");
        }
        String result = userKeeper.userListToString(userList);
        System.out.println("user list parse for sending to client :\n" + result);
        try {
            userConnection.getOut().write(result + "\n");
            userConnection.getOut().flush();
            System.out.println(result);
        }catch (IOException e) {
            logger.warn("while send list of all online users to client ",e);
        }
    }*/
}
