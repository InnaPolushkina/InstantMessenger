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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class RoomServiceImpl implements RoomService {

    private static final Logger logger = Logger.getLogger(RoomServiceImpl.class);

    @Override
    public Room parseNewRoomData(String roomData) {
        Room room = null;
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(roomData)));

            NodeList nodeList = document.getElementsByTagName("create");
            Element element =  (Element) nodeList.item(0);
            String roomName = element.getTextContent();
            room = new Room(roomName);
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
    public User parseUserForAddToRoom(/*UserConnection user, Room room*/ String data) {
        User result = null;
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(data)));

            NodeList nodeList = document.getElementsByTagName("add");
            Node node = nodeList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String userName = element.getElementsByTagName("user").item(0).getTextContent();
                result = new User(userName);
            }
        }
        catch (ParserConfigurationException e) {
            logger.warn("exception while parsing string with xml from client when one add new client to room ",e);
        }
        catch (SAXException e) {
            logger.warn("exception while parsing string with xml from client when one add new client to room ",e);
        }
        catch (IOException e) {
            logger.warn("exception while parsing string with xml from client when one add new client to room ",e);
        }
        return result;
    }


    public Room parseRoomForSwitch(String data){
        Room result = null;
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(data)));

            NodeList nodeList = document.getElementsByTagName("goToRoom");
            Element element = (Element)nodeList.item(0);
            String nameChangedRoom = element.getTextContent();
            result = new Room(nameChangedRoom);
        }
        catch (ParserConfigurationException e) {
            logger.warn("exception while parsing string with xml from client when one switch on rooms ",e);
        }
        catch (SAXException e) {
            logger.warn("exception while parsing string with xml from client when one switch on rooms ",e);
        }
        catch (IOException e) {
            logger.warn("exception while parsing string with xml from client when one switch on rooms ",e);
        }

        return result;
    }

    @Override
    public List<Room> parseListOfRooms(String data) {
        List<Room> roomList = new ArrayList<>();
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(data)));

            NodeList nodeList = document.getElementsByTagName("room");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String roomName = element.getTextContent();
                    String roomAdmin = element.getAttribute("admin");
                    Room room = new Room(roomName);
                    room.setAdmin(roomAdmin);
                    roomList.add(room);
                   // roomNameList.add(roomName);
                }
            }
        }
        catch (ParserConfigurationException e) {
            logger.warn("exception while parsing string with list of rooms from client",e);
        }
        catch (SAXException e) {
            logger.warn("exception while parsing string with list of rooms from client",e);
        }
        catch (IOException e) {
            logger.warn("exception while parsing string with list of rooms from client",e);
        }
        return roomList;
    }

    @Override
    public String prepareListUserForBan(List<User> list) {
        String s = "<listForBan>";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(s);
        stringBuilder.append(parseList(list));
        stringBuilder.append("</listForBan>");
        return stringBuilder.toString();
    }

   /* @Override
    public String parseListUserForUnBan(List<User> list) {
        String s = "<listForUnBan>";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(s);
        stringBuilder.append(parseList(list));
        stringBuilder.append("</listForUnBan>");
        return stringBuilder.toString();
    }*/

    private String parseList(List<User> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (User user: list) {
            stringBuilder.append("<user>");
            stringBuilder.append(user.getName());
            stringBuilder.append("</user>");
        }
        return stringBuilder.toString();
    }

    @Override
    public String parseRoomNameForDelete(String data) {
        String  result = null;
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(data)));

            NodeList nodeList = document.getElementsByTagName("delete");
            Element element = (Element)nodeList.item(0);
            result = element.getTextContent();
        }
        catch (ParserConfigurationException e) {
            logger.warn("exception while parsing string with xml from client when he delete room ",e);
        }
        catch (SAXException e) {
            logger.warn("exception while parsing string with xml from client when he delete room ",e);
        }
        catch (IOException e) {
            logger.warn("exception while parsing string with xml from client when he delete room ",e);
        }

        return result;
    }

    @Override
    public String prepareDeletedRoomNotification(String roomName) {
        return "<deleted>" + roomName + "</deleted>";
    }

    @Override
    public String prepareUsersForSending(Room room) {
        String result = null;
        String admin = room.getAdmin();

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("room");
            root.setAttribute("admin",admin);
            document.appendChild(root);

            for (String user: room.getUserList()) {
                Element element = document.createElement("user");
                element.setAttribute("banned", String.valueOf(room.isUserBanned(user)));
                element.setAttribute("online", String.valueOf(Router.getInstense().getUserConnectionByName(user).getUser().isOnline()));
                element.setTextContent(user);
                root.appendChild(element);
            }
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource domSource = new DOMSource(document);
            transformer.transform(domSource, new StreamResult(sw));
            result = sw.toString();

        }
        catch (TransformerException e) {
            logger.warn("when transformed list of user to xml");
        } catch (ParserConfigurationException e) {
            logger.warn("when parsing list of users to xml ");
        }
        return result;
    }

    @Override
    public String parseRoomName(String data) {
        String result = null;
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(data)));

            NodeList nodeList = document.getElementsByTagName("room");
            Element element = (Element)nodeList.item(0);
            result = element.getTextContent();

        }
        catch (ParserConfigurationException e) {
            logger.warn("exception while parsing string with xml from client when one switch on rooms ",e);
        }
        catch (SAXException e) {
            logger.warn("exception while parsing string with xml from client when one switch on rooms ",e);
        }
        catch (IOException e) {
            logger.warn("exception while parsing string with xml from client when one switch on rooms ",e);
        }

        return result;
    }
}
