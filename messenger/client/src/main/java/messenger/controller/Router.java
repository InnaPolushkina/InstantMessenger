package messenger.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import messenger.model.*;
import messenger.model.exceptions.AuthException;
import messenger.model.exceptions.UserRegistrationException;
import messenger.view.ViewChat;
import messenger.view.ViewLogin;

import messenger.view.ViewRegister;
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
   private ViewRegister viewRegister;
   private static final Router instance = new Router();
   private UserRegistrationServiceImpl userRegistrationService;


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
        userRegistrationService = new UserRegistrationServiceImpl(this);
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
            logger.warn("while showing login scene ",e);
        }
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
            viewLogin.setErrorUserMessage("Can't connect to server");
        }

    }

    private void actions() {
        viewLogin.getLoginButton().setOnAction(event -> {

            String name = viewLogin.getUserName().getText().trim();
            String password = viewLogin.getUserPassword().getText().trim();
           // System.out.println(name + " " + password);
            user.setName(name);
            //sendMessage("<user><nick>" + name + "</nick><password>" + password + "</password></user>");
            login(name,password);
        });
        viewLogin.getRegisterButton().setOnAction(event -> {
            System.out.println("register" + viewLogin.getUserName().getText());
            viewRegister = new ViewRegister();
            FXMLLoader loader = new FXMLLoader();
            loader.setController(viewRegister);

            try {
                loader.setLocation(Router.class.getResource("/register.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            catch (IOException e) {
                logger.warn("while opening form for registration",e);
            }
        });
      // viewChat.getSendButton().setOnAction(event ->  sendMessage(viewChat.getMessageText()));


    }

    public Listener getListener() {
        return listener;
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
     * get response from server and return main form of messenger if user was authorized
     * @see UserRegistrationService
     */
    public void login(String name, String pass) {
        try {
            userRegistrationService.auth(name,pass);
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

        } catch (AuthException e) {
            logger.warn("User can't authorize",e);
            viewLogin.setErrorUserMessage("Name or password is`t true");
        }
    }

    public void register() {

    }

    /**
     * the method send string message to server
     * @param msg String message
     */
    public void sendMessage(String msg) {
        try {
            MessageService messageService = new MessageServiceImpl();
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.write(messageService.sendMessage(new Message(msg,user)) + "\n");
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
