import server.Server;
import server.User;

import java.util.ArrayList;

public class StartServerApp {

    private static final int PORT = 12321;



    public static void main(String[] args)  {
        new Server().start(PORT);
    }
}
