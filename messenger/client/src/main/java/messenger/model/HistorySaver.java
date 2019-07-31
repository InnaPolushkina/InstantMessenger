package messenger.model;

import messenger.model.entity.Message;
import messenger.model.entity.Room;
import messenger.model.entity.User;
import messenger.model.entity.UserServerConnection;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class HistorySaver {
    private  final String fileHistory = "client/src/main/java/messenger/model/db/history.xml";
    private  LocalDateTime lastOnlineDate;
    private static final Logger logger = Logger.getLogger(HistorySaver.class);

    public void saveHistory(Set<Room> roomSet, LocalDateTime lastOnline) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("history");
            root.setAttribute("disconnect",lastOnline.toString());
            document.appendChild(root);
            for (Room room: roomSet) {
                Element elementRoom = document.createElement("room");
                elementRoom.setAttribute("name",room.getRoomName());
                for (Message message:room.getMessageSet()) {
                    Element messageElement = document.createElement("message");
                    messageElement.setAttribute("sender",message.getUserSender().getName());
                    messageElement.appendChild(document.createTextNode(message.getText()));
                    elementRoom.appendChild(messageElement);
                }
                root.appendChild(elementRoom);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(fileHistory));
            transformer.setOutputProperty(OutputKeys.INDENT,"yes");
            transformer.transform(domSource, streamResult);
        }
        catch (ParserConfigurationException e) {
            logger.warn("while saving history messages to file",e);
        }
        catch (TransformerException e) {
            logger.warn("while saving history messages to file",e);
        }
    }


    public Set<Room> loadHistory() {
        Set<Room> roomSet = new HashSet<>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(new InputSource(new FileReader(fileHistory)));

            Element root = document.getDocumentElement();
            lastOnlineDate = LocalDateTime.parse(root.getAttribute("disconnect"));

            NodeList nodeList = document.getElementsByTagName("room");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node roomNode = nodeList.item(i);
                Room room = new Room(roomNode.getAttributes().getNamedItem("name").getNodeValue());
                //if(room.getRoomName().equals("Big chat")) {
                    room.setAdmin(new User("Server"));
                //}
                if(roomNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element roomElement = (Element) roomNode;
                    NodeList messageNodeList = roomElement.getElementsByTagName("message");
                    for (int j = 0; j < messageNodeList.getLength() ; j++) {
                        Node messageNode = messageNodeList.item(j);
                        Element messageElement = (Element) messageNode;
                        String sender = messageElement.getAttribute("sender");
                        String text = messageElement.getTextContent();
                        Message message = new Message(text,new User(sender));
                        room.addMessageToRoom(message);
                    }
                }
                roomSet.add(room);
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

    public LocalDateTime getLastOnlineDate() {
        return lastOnlineDate;
    }
}
