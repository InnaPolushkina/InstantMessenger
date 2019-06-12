package messenger.model.xml;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface XML {
    void createRoom();
    void reg();
    void auth();
    void addUserToRoom();
    void removeUserFromRoom();
    void sendMessage();
    void bun();
    void unbun();
    void mute();
    void unmute();

    /**
     * the method for parsing messages from xml and write text content into console
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * exceptions trows if parsing have some errors
     */
    void parseMessage() throws ParserConfigurationException, IOException, SAXException;
}
