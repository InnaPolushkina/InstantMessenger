package messenger.controller;


import messenger.model.User;
import messenger.model.xml.XMLPars;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;

/**
 * This class listen entering massages to socket and show its to user
 * @author Inna
 */
public class Listener extends Thread {

    private User user;
    private static final Logger logger = Logger.getLogger(Listener.class);

    /**
     * The public constructor for class Listener
     * @param socket for connect to server
     */
    public Listener(Socket socket) {
        user = new User();
        user.setUserSocket(socket);
    }

    /**
     * the method listens server streams and shows messages from server
     */
    @Override
    public void run() {
        /*System.out.println("Enter your name ");
        System.out.println("User " + user.getName() + " connected");*/

        while (true) {
            try {
                user.setIn(new BufferedReader(new InputStreamReader(user.getUserSocket().getInputStream())));
                showMessage();
            } catch (IOException ex) {
                System.out.println(ex);
                logger.error(ex);
                break;
            }
        }
    }

    /**
     * the method gets message from input stream and show message to user
     * @see XMLPars parseMessage()
     */
    private void showMessage() {
        try {
            String mess = user.getIn().readLine();
            if (!mess.equals(null)) {
                BufferedWriter writer = new BufferedWriter(new FileWriter("client/src/main/java/messenger/model/xml/xmlFiles/Message.xml"));
                writer.write(mess);
                logger.info("message was successfully saved !");
                writer.close();
            }

            XMLPars xmlPars = new XMLPars();
            xmlPars.parseMessage();

        }
        catch (ParserConfigurationException e) {
            logger.warn(e);
        }
        catch (IOException e) {
            logger.warn(e);
        }
        catch (SAXException e) {
            System.out.println("exception, it can be if right now start messenger ");
            logger.warn(e);
        }

    }

}