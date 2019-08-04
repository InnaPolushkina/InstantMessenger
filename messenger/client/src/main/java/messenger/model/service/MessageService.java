package messenger.model.service;

import messenger.model.entity.Message;
import messenger.model.entity.ServerAction;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Interface MessageService contains methods for service messages between client and server
 * @author Inna
 */
public interface MessageService {
    /**
     * the method parses message from client for sending
     * @param msg have object of class Message
     * @return string in format for reading server
     */
    String sendMessage(Message msg);

    /**
     * the method parses action from client for sending
     * @param action have string with client action
     * @return string in format for reading server
     */
    String sendAction(String action);

    /**
     * the method parses got string message from server
     * @param message string with message from server
     * @return object of class MessageServer
     * @throws ParserConfigurationException if can`t parse message
     * @throws IOException if can`t parse message
     * @throws SAXException if can`t parse message
     * @see Message
     */
    Message parseMessage(String message) throws ParserConfigurationException, IOException, SAXException;

    /**
     * The method parses action from server
     * @param action string with action from server
     * @return object of enum ServerAction
     * @see ServerAction
     */
    ServerAction parseServerAction(String action);
}
