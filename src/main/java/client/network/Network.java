package client.network;


import client.ChatHistory;
import client.history.ChatHistoryImpl;
import server.AuthException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import static total.MessagesText.*;


public class Network {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Thread receiverThread;

    private String login;

    private IncomeMessages incomeMessages;

    private ChatHistory<Message> history;

    public Network(String host, int port) throws IOException {

        socket = new Socket(host, port);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        /*receiverThread = new Thread(() -> {
            while (true) {
                try {
                    String msg = in.readUTF();
                    if (msg.startsWith("/"))
                        handleServerCommand(msg);
                    else {
                        String[] splitedMsg = msg.split(">", 2);
                        if (!splitedMsg[0].equals(login)) {
                            Message message = new Message(splitedMsg[0], splitedMsg[1]);


                            history.logMessage(message);
                            incomeMessages.handleMessage(message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });*/

    }


    public void authorize(String login, String password, String action) throws IOException, AuthException {

        String message;
        switch (action) {
            case "signup":
                message = String.format(REGISTRATION_MESSAGE, login, password);
                break;
            case "login":
                message = String.format(AUTH_MESSAGE, login, password);
                break;
            default:
                throw new IllegalArgumentException(action);
        }
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String authResponse = in.readUTF();

        if (authResponse.equals("/succeeded")) {
            this.login = login;
            try {
                history = new ChatHistoryImpl(login);
                for (Message msg : history.getLastMessages(MESSAGES_TO_SAVELOAD)) {
                    System.out.printf("%s: %s \n", msg.getUserName(), msg.getMessageText());
                    incomeMessages.handleMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Platform.runLater(() -> receiverThread.start());
            //receiverThread.setDaemon(true);
        } else {
            throw new AuthException(authResponse.split(" ", 2)[1]);
        }
    }

    public void requestOnlineUsersList() throws IOException {
        out.writeUTF(REQUEST_ONLINE_USERS);
    }

    public void sendAddressedMessage(String to, String message) throws IOException {
        //out.writeUTF(String.format(MessagesText.AUTH_MESSAGE, to, message));
    }

    private void handleServerCommand(String msg) {
        String[] preparedMsg = msg.split(" ", 2);
        String command = preparedMsg[0];
        if (preparedMsg[0].equals(USERS_LIST_COMMAND)) {
            incomeMessages.setOnlineUsersList(Arrays.asList(preparedMsg[1].split(" ")));
            return;
        }
        if (command.equals(USER_CAME_OFLINE_COMMAND)) {
            incomeMessages.userRemoved(preparedMsg[1]);
            return;
        }
        if (command.equals(USER_CAME_ONLINE_COMMAND)) {
            incomeMessages.userLogined(preparedMsg[1]);
            return;
        }
    }

    public void setIncomeMessages(IncomeMessages incomeMessages) {
        this.incomeMessages = incomeMessages;
    }

    public String getLogin() {
        return login;
    }
}
