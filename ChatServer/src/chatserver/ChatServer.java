package chatserver;


//Example 26 (updated)

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.Socket;
import java.net.ServerSocket;
import javafx.application.Application;
import javafx.stage.Stage;
import java.util.*;

public class ChatServer extends Application
{
    
    public static ArrayList<chatRoom> chatRoomsAvailable;
    
    static ArrayList<user> online = new  ArrayList<user>() ;
    static ArrayList<user> allusers = new ArrayList<user> ();
    static ArrayList<chatRoom> rooms=new ArrayList<chatRoom> ();
    private void save() {
        Gson gson = new Gson();
        Type type = new TypeToken < ArrayList < user >> () {}.getType();
        String json = gson.toJson(ChatServer.allusers, type);
        try {
            FileWriter fw = new FileWriter("users.json");
            fw.write(json);
            fw.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }
    @Override
    public void start(Stage stage) throws Exception {
        ServerSocket serverSocket = null;
        Socket socket = null;
        
        chatRoomsAvailable = new ArrayList<chatRoom>();
Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
    public void run() {
        try{
            save();
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }
}));
        try {
            serverSocket = new ServerSocket(3001);
        } catch (IOException e) {
            e.printStackTrace();

        }
        while (true) {
            try {
                System.out.println("LISTENING");
                socket = serverSocket.accept();
                System.out.println("DONE");
            } 
            catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            Gson gson=new Gson();
            Type type=new TypeToken<ArrayList<user>>(){}.getType();
            try (BufferedReader br = new BufferedReader(new FileReader("users.json"))) {
                allusers=new ArrayList(gson.fromJson(br,type));
            }catch(Exception e){
            }
            new EchoThread(socket).start();
        }
        
    }
    public static void main(String[] args) {
        launch(args);
    }
    
}
