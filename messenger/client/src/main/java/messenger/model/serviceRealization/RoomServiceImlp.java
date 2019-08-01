package messenger.model.serviceRealization;

import messenger.model.entity.Room;
import messenger.model.entity.User;
import messenger.model.service.RoomService;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class RoomServiceImlp implements RoomService {

    private static final Logger logger = Logger.getLogger(RoomServiceImlp.class);

    @Override
    public String createRoom(String roomName) {
        String newRoom = "<create>" + roomName + "</create>";
        return newRoom;
    }

    @Override
    public String addUserToRoom(User user) {
        String result = "<add><user>" + user.getName() + "</user></add>";
        return result;
    }

    @Override
    public void removeUserFromRoom(User user, Room room) {

    }

    @Override
    public String switchRoom(String roomName) {
        String result = "<goToRoom>" + roomName + "</goToRoom>";
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
            String admin = element.getAttribute("admin");
            Room room = new Room(roomName);
            room.setAdmin(new User(admin));
            result = room;

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

    @Override
    public String parseRoomList(Set<Room> rooms, LocalDateTime lastConnection) {
        String result;
        StringBuilder stringBuilder = new StringBuilder();
        String s = "<rooms after = \"" + lastConnection + "\">";
        stringBuilder.append(s);
        for (Room room: rooms) {
            stringBuilder.append("<room admin = \"");
            stringBuilder.append(room.getAdmin().getName());
            stringBuilder.append("\">");
            stringBuilder.append(room.getRoomName());
            stringBuilder.append("</room>");
        }
        stringBuilder.append("</rooms>");
        result = stringBuilder.toString();
        return result;
    }

    @Override
    public List<User> parseListForBanUnBan(String msg) {
        List<User> result = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(msg)));
            NodeList nodeList = document.getElementsByTagName("user");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String nameUser = element.getTextContent();
                    result.add(new User(nameUser));
                }
            }


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
