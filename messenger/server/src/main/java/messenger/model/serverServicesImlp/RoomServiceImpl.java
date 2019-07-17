package messenger.model.serverServicesImlp;

import messenger.controller.Router;
import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.User;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.RoomService;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

public class RoomServiceImpl implements RoomService {

    private static final Logger logger = Logger.getLogger(RoomServiceImpl.class);

    @Override
    public Room createRoom(String roomData) {
        /*Room room = new Room(roomName);
        Router.getInstense().getRoomList().add(room);*/
        Room room = null;
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(roomData)));

            NodeList nodeList = document.getElementsByTagName("create");
            Element element =  (Element) nodeList.item(0);
            String roomName = element.getTextContent();
            room = new Room(roomName);
           /* Node node = nodeList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String roomName = element.getElementsByTagName("roomname").item(0).getTextContent();
                //String password = element.getElementsByTagName("password").item(0).getTextContent();
                room = new Room(roomName);
               // room.setRoomName(roomName);
                return room;
            }*/
        }
        catch (ParserConfigurationException e) {
            logger.warn("exception while parsing string with xml from client when one create new room ",e);
        }
        catch (SAXException e) {
            logger.warn("exception while parsing string with xml from client when one create new room ",e);
        }
        catch (IOException e) {
            logger.warn("exception while parsing string with xml from client when one create new room ",e);
        }


        return room;
    }

    @Override
    public void addUserToRoom(UserConnection user, Room room) {
        for (Room r: Router.getInstense().getRoomList()) {
            if(r.getRoomName().equals(room.getRoomName())) {
                r.addUser(user);
            }
        }
    }

    @Override
    public void removeUserFromRoom(UserConnection user, Room room) {
        for (Room r: Router.getInstense().getRoomList()) {
            if(r.getRoomName().equals(room.getRoomName())) {
                r.removeUser(user);
            }
        }
    }
}
