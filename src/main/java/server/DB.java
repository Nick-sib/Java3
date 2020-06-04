package server;

import java.sql.*;

public class DB {
    private final Connection conn;// = DriverManager.getConnection("jdbc:sqlite:DBchat.db");

    public DB() throws Exception {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:DBchat.db");
        System.out.println("Database \"DBchat.db\" is connected");
        /*conn.prepareStatement("create table Users_Data(\n" +
                "    id integer primary key autoincrement,\n" +
                "    nick varchar(10),\n" +
                "    token varchar(21)\n" +
                ");").execute();*/

        /*PreparedStatement UserQuare =
                conn.prepareStatement("select id\n " +
                                          "from Users_Data ud\n " +
                                          "where ud.nick = ?");
        UserQuare.setString(1, "userName");
        ResultSet loginResult = UserQuare.executeQuery();*/


    }


    public int checkUser(String userName, String pass) {
        try {
            PreparedStatement UserQuare =
                    conn.prepareStatement("select id\n" +
                            "from Users_Data ud\n" +
                            "where ud.nick = ? and ud.token = ?;");
            UserQuare.setString(1, userName);
            UserQuare.setString(2, pass);
            ResultSet loginResult = UserQuare.executeQuery();
            if (loginResult.isAfterLast()) {
                return -1;
            } else {
                loginResult.next();
                return loginResult.getInt(1);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return -1;
        }
    }

    public int checkUser(String userName) {
        try {
            PreparedStatement UserQuare =
                    conn.prepareStatement("select id\n" +
                                              "from Users_Data ud\n" +
                                              "where ud.nick = ?;");
            UserQuare.setString(1, userName);
            ResultSet loginResult = UserQuare.executeQuery();
            if (loginResult.isAfterLast()) {
                return -1;
            } else {
                loginResult.next();
                return loginResult.getInt(1);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return -1;
        }
    }

    public int insertUser(String userName, String token) {

        try {
            PreparedStatement UserQuare =
                    conn.prepareStatement("insert into Users_Data (nick, token) values\n" +
                                              "    (?, ?)");
            UserQuare.setString(1, userName);
            UserQuare.setString(2, token);
            UserQuare.execute();

            return checkUser(userName);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

}
