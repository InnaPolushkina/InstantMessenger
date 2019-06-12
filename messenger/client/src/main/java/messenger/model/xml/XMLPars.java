package messenger.model.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * the class for parse xml
 * @author Inna
 */
public class XMLPars implements XML {

    public void parseMessage() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("client/src/main/java/messenger/model/xml/xmlFiles/Message.xml"));
        NodeList nodeList = document.getElementsByTagName("massage");

        // System.out.println(nodeList.item(0).getNodeName());

        Node node = nodeList.item(0);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String nick = element.getElementsByTagName("nick").item(0).getTextContent();
            String text = element.getElementsByTagName("text").item(0).getTextContent();
            Date date = new Date();
            System.out.println(date + " >> " + nick + " : " + text);
        }
    }

    public void createRoom() {

    }

    public void reg() {

    }

    public void auth() {

    }

    public void addUserToRoom() {

    }

    public void removeUserFromRoom() {

    }

    public void sendMessage() {

    }

    public void bun() {

    }

    public void unbun() {

    }

    public void mute() {

    }

    public void unmute() {

    }
}
