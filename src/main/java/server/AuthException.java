package server;

public class AuthException extends Exception {
    public AuthException(String reason) {
        super(reason);
    }
}
