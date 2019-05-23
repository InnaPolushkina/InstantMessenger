package messenger.controller;

import messenger.model.Room;
import messenger.model.User;
import messenger.view.View;

import java.io.*;
import java.net.Socket;
import java.util.Set;

public class Router {
    private static User user = new User();
    private Set<Room> roomList;
    //private Listener listener;
    static Socket socket;

    public static void main(String[] args) {
        new Router();
    }


    public Router() {

        View.showLog("Start client  . . . ");
        System.out.println("Write something");
        try {
            socket = new Socket("localhost", 2020);
            Listener listener = new Listener(socket);
            listener.start();
            new SendMessage().start();
        }
        catch (IOException ex) {
            //logger.error(ex);
            ex.printStackTrace();
        }
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

    public void login() {

    }

    public void register() {

    }

   /* public void sendMessage() {


    }*/
   class SendMessage extends Thread {
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
   }

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
