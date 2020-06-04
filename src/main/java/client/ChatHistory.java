package client;

import java.util.List;

public interface ChatHistory<T> {
    void logMessage(T message);
    List<T> getAllMessages();
    List<T> getLastMessages(int messagesCount);
}
