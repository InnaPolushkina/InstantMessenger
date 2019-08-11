package messenger.model.serverServices;

import messenger.model.serverEntity.ClientAction;
import messenger.model.serverEntity.MessageServer;
import messenger.model.serverEntity.Room;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface MessageService {
    /**
     * The method parses message for sending to user
     * @param text message text
     * @param nameSender name of sender
     * @param roomRecipient name of room recipient
     * @return string with message
     */
    String createMessage(String text, String nameSender, String roomRecipient);

    /**
     * the method parses got string message from server
     * @param message string with message from server
     * @return object of class MessageServer
     * @throws ParserConfigurationException if can`t parse message
     * @throws IOException if can`t parse message
     * @throws SAXException if can`t parse message
     * @see MessageServer
     */
    MessageServer parseMessage(String message);

    /**
     * the method parses message with client action from string to object of ClientAction
     * @param clientAction have string with client action
     * @return object of enum ClientAction
     * @see ClientAction
     */
    ClientAction parseClientAction(String clientAction);

    /**
     * The method creates server action before sending
     * @param action server action
     * @return string with server action
     */
    String createServerAction(String action);

    /**
     * The method creates notify about adding user to room
     * @param room room where user was added
     * @return string with notify about adding to room
     */
    String createAddToRoomNotify(Room room);
}
