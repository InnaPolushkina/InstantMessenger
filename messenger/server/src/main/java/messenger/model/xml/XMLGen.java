package messenger.model.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class XMLGen {
    public void reg(){

    }
    public void auth(){

    }
    public void createRoom(){

    }
    public void addUserToRoom(){

    }
    public void removeFromRoom(){

    }
    public void sendMassage(String massage, String name) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("massage");
            doc.appendChild(rootElement);
            Element firstname = doc.createElement("nick");
            firstname.appendChild(doc.createTextNode(name));
            rootElement.appendChild(firstname);
            Element text = doc.createElement("text");
            text.appendChild(doc.createTextNode(massage));
            rootElement.appendChild(text);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("src/main/java/messenger/model/xml/xmlFiles/SendMassage.xml"));
            transformer.transform(source, result);
            System.out.println("File saved!");
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
    public void ban(){

    }
    public void unban(){

    }
    public void mute(){

    }
    public void unmute(){

    }
}
