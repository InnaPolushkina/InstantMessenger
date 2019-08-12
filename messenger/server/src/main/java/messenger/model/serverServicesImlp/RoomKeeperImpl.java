package messenger.model.serverServicesImlp;

import messenger.model.serverEntity.Room;
import messenger.model.serverEntity.UserConnection;
import messenger.model.serverServices.RoomKeeper;
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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class RoomKeeperImpl implements RoomKeeper {

    private String fileName;
    private static final Logger logger = Logger.getLogger(RoomKeeperImpl.class);

    public RoomKeeperImpl(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void saveRoomsInfo(Set<Room> rooms) {

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("rooms");
            document.appendChild(root);

            for (Room room : rooms) {
                Element elementRoom = document.createElement("room");
                elementRoom.setAttribute("name",room.getRoomName());
                elementRoom.setAttribute("admin",room.getAdmin());


                for (String user: room.getUserList()) {
                    Element elementUser = document.createElement("user");
                    elementUser.setAttribute("banned", String.valueOf(room.isUserBanned(user)));
                    elementUser.setTextContent(user);
                    elementRoom.appendChild(elementUser);
                }
                root.appendChild(elementRoom);

            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(fileName));
            transformer.setOutputProperty(OutputKeys.INDENT,"yes");
            transformer.transform(domSource, streamResult);
        }
        catch (ParserConfigurationException e) {
            logger.warn("while saving rooms data to xml file");
        }
        catch (TransformerException e) {
            logger.warn("while saving rooms data to xml file",e);
        }

    }

    @Override
    public Set<Room> loadRoomsInfo() {
       Set<Room> roomSet = new HashSet<>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(new InputSource(new FileReader(fileName)));

            Element root = document.getDocumentElement();

            NodeList nodeList = document.getElementsByTagName("room");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String roomName = element.getAttribute("name");
                    String admin = element.getAttribute("admin");
                    Room room = new Room(roomName);
                    room.setAdmin(admin);
                    NodeList usersNodeList = element.getElementsByTagName("user");
                    for (int j = 0; j < usersNodeList.getLength(); j++) {
                        Node userNode = usersNodeList.item(j);
                        if(userNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element elementUser = (Element) userNode;
                            String userName = elementUser.getTextContent();
                            boolean banned = Boolean.parseBoolean(elementUser.getAttribute("banned"));
                            room.getUserList().add(userName);
                            if(banned) {
                                room.getBanList().add(userName);
                            }
                        }
                    }
                    roomSet.add(room);

                }
            }
        }
        catch (ParserConfigurationException e) {
            logger.warn(e);
        }
        catch (FileNotFoundException e) {
            logger.warn(e);
        }
        catch (IOException e) {
            logger.warn(e);
        }
        catch (SAXException e) {
            logger.warn(e);
        }

        return roomSet;
    }


    @Override
    public String roomsToString(Set<Room> rooms, UserConnection user) {
        String result = null;
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("rooms");
            document.appendChild(root);

            for (Room room : rooms) {
                if( room.isUserInRoom(user) ) {
                    Element elementRoom = document.createElement("room");
                    elementRoom.setTextContent(room.getRoomName());
                    elementRoom.setAttribute("admin", room.getAdmin());
                    elementRoom.setAttribute("banned", String.valueOf(room.isUserBanned(user)));
                    root.appendChild(elementRoom);
                }

            }
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource domSource = new DOMSource(document);
            transformer.transform(domSource, new StreamResult(sw));
            result = sw.toString();
        } catch (TransformerException e) {
            logger.warn("when transformed list of user to xml");
        } catch (ParserConfigurationException e) {
            logger.warn("when parsing list of users to xml ");
        }
        return result;

    }
}
