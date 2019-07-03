package messenger.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import messenger.model.Room;
import messenger.model.User;
import messenger.view.ViewChat;
import messenger.view.ViewLogin;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Set;


/**
 * The main controller class of client side
 * @see Listener
 * @author Inna
 */
public class Router {
    private User user;
    private Set<Room> roomList;
    private Listener listener;
    private Socket socket;
    private static final Logger logger = Logger.getLogger(Router.class);
   // private ViewLogin mainView ;
   private Stage stage ;
   private ViewLogin viewLogin;
   private ViewChat viewChat;
   private static final Router instance = new Router();


   public static Router getInstance() {
       return instance;
   }
   //private SendMessage sendMessage;
    //private ViewController viewController;



    /*
     * the start method of client side
     * @param args
     */
   /* public static void main(String[] args) {
        new Router();
    }*/

    /**
     * the constructor of class Router
     * connects client with server
     */
    private Router() {
        viewLogin = new ViewLogin();
        viewChat = new ViewChat(this);
        stage = new Stage();
        user = new User();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(viewLogin);
        try {
            loader.setLocation(Router.class.getResource("/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            logger.info("show login scene ");
        }
        catch (IOException e) {
            logger.warn("while showing login scene " + e);
        }

        System.out.println("Write your nick name");
        try {

            socket = new Socket("localhost", 2020);
            listener = new Listener(socket,viewChat);
            //listener.start();
           // sendMessage = new SendMessage();
            //sendMessage.start();
            logger.info("start client ");
            actions();
        }
        catch (IOException ex) {
            logger.error(ex);
        }

    }

    private void actions() {
        viewLogin.getLoginButton().setOnAction(event -> {

            String name = viewLogin.getUserName().getText().trim();
            String password = viewLogin.getUserPassword().getText().trim();
           // System.out.println(name + " " + password);
            user.setName(name);
            sendMessage(name);
            while (true) {
                if (login()) {

                    listener.start();

                    FXMLLoader loader = new FXMLLoader();
                    // viewChat = new ViewChat();
                    loader.setController(viewChat);

                    try {
                        loader.setLocation(Router.class.getResource("/mainView.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                        viewChat.setUserName(name);
                        logger.info("show main scene ");

                    } catch (IOException e) {
                        logger.warn("while showing main scene " + e);
                    }
                    break;
                } else {
                    viewLogin.setErrorUserMessage("Name or password is`t true");
                }
            }
        });
        viewLogin.getRegisterButton().setOnAction(event -> {
            System.out.println("register" + viewLogin.getUserName().getText());
        });
      // viewChat.getSendButton().setOnAction(event ->  sendMessage(viewChat.getMessageText()));


    }


    public void createRoom(User[] users) {
        Room room = new Room();
        for(int i = 0; i <users.length; i++ ) {
            // room.addUser(users[i]);
        }
        roomList.add(room);
    }

    public void leaveRoom(Room room) {
        for (Room r: roomList) {
            if(r.equals(room)) {
                //r.removeUser(user);
            }
        }
    }

    /**
     * the method for authorization user
     * get response from server and return boolean result
     * @return <p>true if user enter correct name and password and server have info about him,else return false,</p>
     * <p>if while parsing response from server fall IOException return false</p>
     */
    public boolean login() {
        try {
            String login = listener.messageFromServer();
            boolean result = Boolean.parseBoolean(login);
            return  result;
        } catch (IOException e) {
            logger.warn(e);
        }
        return false;
    }

    public void register() {

    }

    /**
     * the method send string message to server
     * @param msg String message
     */
    public void sendMessage(String msg) {
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.write(msg + "\n");
            out.flush();
        } catch (IOException e) {
            logger.info(e);
        }
    }

    /*
    /**
     * the inner class SendMessage for send user messages to server
     * class create new thread in app for parallel sending and writing messages to/from socket
     */
  /*class SendMessage extends Thread {
       @Override
       public void run() {
           while(true) {
               try {
                   BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                   BufferedReader message = new BufferedReader(new InputStreamReader(System.in));
                   String mes = message.readLine();
                   out.write(mes + "\n");
                   out.flush();
               } catch (IOException e) {
                   System.out.println(e);
                   break;
               }
           }

       }
   }*/


    public Set<User> getUserList(Room room) {
        Set<User> res = null;
        for (Room r: roomList) {
            if(r.equals(room)) {
                //res = r.getUsersList();
            }
        }
        return res;
    }

    public void bunUser(User user, Room room, boolean bunStatus) {

    }

    public void muteUser(User user, Room room, int time, boolean muteStatus) {

    }
}
