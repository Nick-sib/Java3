package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class User {
    private int ID = -1;
    private String Name = "??";
    private String Token = "";
    public Boolean isInChat = false;

    Thread receiverThread;

    static DataOutputStream out;
    static DataInputStream in;

    public User(int id, String name, String token) {
        ID = id;
        Name = name;
        Token = token;
    }

    public User(Socket candidateSocket) throws IOException{
        out = new DataOutputStream(candidateSocket.getOutputStream());
        in = new DataInputStream(candidateSocket.getInputStream());
        System.out.println("NEW User created");



    }

    public void start() {
        receiverThread = new Thread(() -> {
            while (true) {
                try {
                    System.out.println("1");
                    String message = in.readUTF();
                    System.out.printf("User %s incomeMessage: %s\n", Name, message);

                    /*final String[] preparedMsg = in.readUTF().split(" ");
                    System.out.println(preparedMsg[0]);*/

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        receiverThread.setDaemon(true);
        receiverThread.start();
    }

    public void setName(String name, int id, String token) {
        if (id == ID) {
            Name = name;
            Token = token;
        }
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getToken() {
        return Token;
    }
}
