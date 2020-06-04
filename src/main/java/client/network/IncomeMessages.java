package client.network;

import java.util.List;

public interface IncomeMessages {

    void userLogined(String userName);
    void userRemoved(String userName);
    void handleMessage(Message message);
    void setOnlineUsersList(List<String> users);
}
