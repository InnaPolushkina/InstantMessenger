package messenger.model;

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

public class MessageServiceImpl implements MessageService {
    @Override
    public void sendMessage(Message msg) {

    }

    @Override
    public Message parseMessage(String message) throws ParserConfigurationException, IOException, SAXException {
        Message msg = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(message)));
        NodeList nodeList = document.getElementsByTagName("massage");

        Node node = nodeList.item(0);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String nick = element.getElementsByTagName("nick").item(0).getTextContent();
            String text = element.getElementsByTagName("text").item(0).getTextContent();
            msg = new Message(text,new User(nick));
        }
        return msg;
    }
}
