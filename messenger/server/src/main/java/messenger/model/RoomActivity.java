package messenger.model;

import messenger.controller.Router;
import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.User;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.RoomService;
import messenger.model.serverServices.UserKeeper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RoomActivity {
    private UserConnection userConnection;
    private RoomService roomService;
    private static final Logger logger = Logger.getLogger(RoomActivity.class);
    private UserKeeper userKeeper;
    /**
     * The field roomNow contain info about room where user is now
     */
    private Room roomNow;

    public RoomActivity(UserConnection userConnection, RoomService roomService, UserKeeper userKeeper) {
        this.userConnection = userConnection;
        this.roomService = roomService;
        this.userKeeper = userKeeper;
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

    public void sendOnlineUserList() {
        try {
            userConnection.getOut().write("<action>ONLINE_LIST</action>\n" + userKeeper.userListToString(getOnlineUser()) + "\n");
            userConnection.getOut().flush();
        }
        catch (IOException e) {
            logger.info("while sending list of online users to client",e);
        }

    }

    private List<User> getOnlineUser() {
        List<User> resultList = new ArrayList<>();
        //resultList.add(userConnection.getUser());

        for (UserConnection connection: Router.getInstense().getUserList()) {
            System.out.println(connection.getUser().getName());
            if( connection.getUser() != null  && !connection.getUser().getName().equals(userConnection.getUser().getName())) {
                resultList.add(connection.getUser());
            }
        }

        return resultList;
    }
}
