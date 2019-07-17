package messenger.model;

import messenger.controller.Router;
import messenger.model.serverEntity.MessageServer;
import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.MessageService;

import java.io.IOException;

public class SenderMessage {
    private MessageService messageService;
    private UserConnection userConnection;

    public SenderMessage(MessageService messageService, UserConnection userConnection) {
        this.messageService = messageService;
        this.userConnection = userConnection;
    }

    /**
     * The method sends message from user to room
     * @param message have string with data about message from user : who sent, recipient room, message text
     * @throws IOException if fall I/O exception
     */
    public void sendMessage(String message) throws IOException {
        MessageServer messageServer = messageService.parseMessage(message);
        //Router router = Router.getInstense();
        Room getter = messageServer.getRecipient();


        for (Room room: Router.getInstense().getRoomList()) {
            if(room.getRoomName().equals(getter.getRoomName())) {
                for (UserConnection userInRoom: room.getUserList()) {
                    userInRoom.getOut().write(message + "\n");
                    userInRoom.getOut().flush();
                    System.out.println("send msg to room " + getter.getRoomName());
                }
            }
        }
    }
}
