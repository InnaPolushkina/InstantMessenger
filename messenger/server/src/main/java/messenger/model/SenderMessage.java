package messenger.model;

import messenger.controller.Router;
import messenger.model.serverEntity.MessageServer;
import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.MessageService;

import java.io.IOException;

/**
 * The class for handling sending messages
 */
public class SenderMessage {
    private MessageService messageService;
    private UserConnection userConnection;
    private HistoryMessage historyMessage;

    /**
     * The constructor of this class
     * @param messageService object of class that implements interface MessageServise
     * @see MessageService
     * @param userConnection connection with user
     * @param historyMessage object of class HistoryMessage
     * @see HistoryMessage
     */
    public SenderMessage(MessageService messageService, UserConnection userConnection, HistoryMessage historyMessage) {
        this.messageService = messageService;
        this.userConnection = userConnection;
        this.historyMessage = historyMessage;
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
                /*for (UserConnection userInRoom: room.getUserList()) {
                    if(userInRoom.getUser().isOnline()) {
                        userInRoom.getOut().write(messageService.createServerAction("SEND_MSG") + message + "\n");
                        userInRoom.getOut().flush();
                    }
                }*/

                for (String name: room.getUserList()) {
                    UserConnection userInRoom = Router.getInstense().getUserConnectionByName(name);
                    if(userInRoom.getUser().isOnline()) {
                        userInRoom.getOut().write(messageService.createServerAction("SEND_MSG") + message + "\n");
                        userInRoom.getOut().flush();
                    }
                }
            }
        }
        historyMessage.addMessageToStory(message);
    }
}
