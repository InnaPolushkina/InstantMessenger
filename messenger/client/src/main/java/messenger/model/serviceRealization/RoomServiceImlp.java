package messenger.model.serviceRealization;

import messenger.model.entity.Room;
import messenger.model.entity.User;
import messenger.model.service.RoomService;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;


public class RoomServiceImlp implements RoomService {

    private static final Logger logger = Logger.getLogger(RoomServiceImlp.class);

    @Override
    public String createRoom(String roomName) {
        String newRoom = "<create>" + roomName + "</create>";
        return newRoom;
    }

    /**
     * the method adds user to room
     *
     * @param user contain object of Class User for adding to room
     * @param //room contain object of Class Room
     * @see Room
     * @see User
     */
    @Override
    public String addUserToRoom(User user) {
        String result = "<add><user>" + user.getName() + "</user></add>";
        return result;
    }

    /**
     * the method removes user from room
     *
     * @param user contain object of Class User for removing from room
     * @param room contain object of Class Room
     * @see User
     */
    @Override
    public void removeUserFromRoom(User user, Room room) {

    }

    @Override
    public String switchRoom(String roomName) {
        String result = "<switch>" + roomName + "</switch>";
        return result;
    }

    @Override
    public Room parseNotifyAddedToRoom(String msg) {
        Room result = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(msg)));
            NodeList nodeList = document.getElementsByTagName("room");
            Element element = (Element) nodeList.item(0);
            String roomName = element.getTextContent();
            result = new Room(roomName);
            return result;
        }catch (IOException e) {
            logger.warn("while parsing notification about addition to room ",e);
        }
        catch (ParserConfigurationException e) {
            logger.warn("while parsing notification about addition to room ",e);
        }
        catch (SAXException e) {
            logger.warn("while parsing notification about addition to room ",e);
        }

        return result;
    }
}
