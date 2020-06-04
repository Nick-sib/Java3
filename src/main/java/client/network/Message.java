package client.network;

import java.time.LocalDateTime;

public class Message {
    private LocalDateTime created;
    private String userName;
    private String messageText;

    /*public Message(String userName, String messageText) {
        this.userName = userName;
        this.messageText = messageText;
        //created = LocalDateTime.now();
    }*/

    public Message(LocalDateTime created, String user, String messageText) {
        this.created = created;
        this.userName = user;
        this.messageText = messageText;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessageText() {
        return messageText;
    }
}
