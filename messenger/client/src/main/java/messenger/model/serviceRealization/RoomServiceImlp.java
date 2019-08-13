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
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class RoomServiceImlp implements RoomService {

    private static final Logger logger = Logger.getLogger(RoomServiceImlp.class);

    @Override
    public String prepareCreateRoom(String roomName) {
        String newRoom = "<create>" + roomName + "</create>";
        return newRoom;
    }

    @Override
    public String prepareAddUserToRoom(User user) {
        String result = "<add><user>" + user.getName() + "</user></add>";
        return result;
    }

    @Override
    public String prepareSwitchRoom(String roomName) {
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
    public String prepareRoomListForGettingHistory(Set<Room> rooms, LocalDateTime lastConnection) {
        String result;
        StringBuilder stringBuilder = new StringBuilder();
        String s = "<rooms after = \"" + lastConnection + "\">";
        stringBuilder.append(s);
        for (Room room: rooms) {
            if(!room.isDeleted()) {
                stringBuilder.append("<room admin = \"");
                stringBuilder.append(room.getAdmin().getName());
                stringBuilder.append("\">");
                stringBuilder.append(room.getRoomName());
                stringBuilder.append("</room>");
            }
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

    @Override
    public Room parseDeletedRoom(String data) {
        Room result = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(data)));
            NodeList nodeList = document.getElementsByTagName("deleted");
            Element element = (Element) nodeList.item(0);
            String roomName = element.getTextContent();
            Room room = new Room(roomName);
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
    public String prepareDeleteRoom(String roomName) {
        return "<delete>" + roomName + "</delete>";
    }


    @Override
    public String prepareForSendRoom(String roomName) {
        return "<room>" + roomName + "</room>";
    }

    @Override
    public List<User> parseUserListFromRoom(String data) {
        List<User> list = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(data)));

            Element root = document.getDocumentElement();
            String adminName = root.getAttribute("admin");

            NodeList nodeList = document.getElementsByTagName("user");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String nameUser = element.getTextContent();
                    boolean banned = Boolean.parseBoolean(element.getAttribute("banned"));
                    boolean online = Boolean.parseBoolean(element.getAttribute("online"));
                    User user = new User(nameUser);
                    user.setBanned(banned);
                    user.setOnline(online);
                    if(nameUser.equals(adminName)) {
                        user.setAdmin(true);
                    }

                    list.add(user);
                }
            }
        }
        catch (IOException e) {
            logger.warn("while parsing user list from room",e);
        }
        catch (ParserConfigurationException e) {
            logger.warn("while parsing user list from room",e);
        }
        catch (SAXException e) {
            logger.warn("while parsing user list from room",e);
        }
        return list;
    }

    @Override
    public Set<Room> parseUserRoomsFromServer(String data) {
        Set<Room> rooms = new HashSet<>();

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(new StringReader(data)));

            Element root = document.getDocumentElement();

            NodeList nodeList = root.getElementsByTagName("room");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node roomNode = nodeList.item(i);
                if(roomNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) roomNode;
                    String roomName = element.getTextContent();
                    String admin = element.getAttribute("admin");
                    boolean banned = Boolean.parseBoolean(element.getAttribute("banned"));
                    boolean deleted = Boolean.parseBoolean(element.getAttribute("deleted"));
                    Room room = new Room(roomName);
                    room.setDeleted(deleted);
                    room.setAdmin(new User(admin));
                    room.setBanned(banned);
                    rooms.add(room);
                }
            }
        }
        catch (IOException e) {
            logger.warn("while parsing rooms list",e);
        }
        catch (ParserConfigurationException e) {
            logger.warn("while parsing rooms list",e);
        }
        catch (SAXException e) {
            logger.warn("while parsing rooms list",e);
        }

        return rooms;
    }
}
