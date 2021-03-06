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
import java.io.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class HistorySaver {
    //private  final String fileHistory = "client/src/main/java/messenger/model/db/history.xml";
    private  final String fileHistory = "client/src/main/history.xml";
    private  LocalDateTime lastOnlineDate;
    private static final Logger logger = Logger.getLogger(HistorySaver.class);

    /**
     * The method for saving info about all room where is user
     * @param roomSet rooms with data for saving
     * @param lastOnline data of last user disconnecting
     */
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
                elementRoom.setAttribute("admin",room.getAdmin().getName());
                elementRoom.setAttribute("deleted", String.valueOf(room.isDeleted()));
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

    /**
     * The method load from file all data from last user disconnect
     * @return rooms with saving data
     */
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
                room.setAdmin(new User(roomNode.getAttributes().getNamedItem("admin").getNodeValue()));
                room.setDeleted(Boolean.parseBoolean(roomNode.getAttributes().getNamedItem("deleted").getNodeValue()));
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
            try {
                PrintWriter writer = new PrintWriter(fileHistory, "UTF-8");
                LocalDateTime dateTime = LocalDateTime.of(2019, 8, 1, 12, 30, 45);
                writer.print("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                        "<history disconnect=\"" + dateTime.toString() + "\">\n" +
                        "<room admin=\"Server\" deleted=\"false\" name=\"Big chat\"/>\n" +
                        "</history>");
                writer.close();
            }
            catch (IOException ex) {
                logger.info(ex);
            }
        }
        catch (IOException e) {
            logger.warn(e);
        }
        catch (SAXException e) {
            logger.warn(e);
        }



        return roomSet;
    }

    /**
     * The getter for date of last user disconnection
     * @return last online date
     */
    public LocalDateTime getLastOnlineDate() {
        return lastOnlineDate;
    }
}
