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
    private HistoryMessage historyMessage;

    /**
     * The constructor of this class
     * @param messageService object of class that implements interface MessageServise
     * @see MessageService
     * @param historyMessage object of class HistoryMessage
     * @see HistoryMessage
     */
    public SenderMessage(MessageService messageService, HistoryMessage historyMessage) {
        this.messageService = messageService;
        this.historyMessage = historyMessage;
    }

    /**
     * The method sends message from user to room
     * @param message have string with data about message from user : who sent, recipient room, message text
     * @throws IOException if fall I/O exception
     */
    public void sendMessage(String message) throws IOException {
        MessageServer messageServer = messageService.parseMessage(message);
        Room getter = messageServer.getRecipient();


        for (Room room: Router.getInstense().getRoomList()) {
            if(room.getRoomName().equals(getter.getRoomName())) {
                for (String name: room.getUserList()) {
                    UserConnection userInRoom = Router.getInstense().getUserConnectionByName(name);
                    if(userInRoom.getUser().isOnline()) {
                        userInRoom.sendMessage(messageService.createServerAction("SEND_MSG") + message + "\n");
                    }
                }
            }
        }
        historyMessage.addMessageToStory(message);
    }
}
