import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String HOST = "localhost";
    private static final String DATABASE = "plugin_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";

    private static Connection connection;

    public static void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + HOST + "/" + DATABASE + "?useSSL=false", USERNAME, PASSWORD);
            System.out.println("Connected to database.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to database: " + e.getMessage());
        }
    }

    public static void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Disconnected from database.");
            } catch (SQLException e) {
                System.out.println("Failed to disconnect from database: " + e.getMessage());
            }
        }
    }

    public static void insertServer(String name, String address) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO servers (name, address) VALUES (?, ?)");
            statement.setString(1, name);
            statement.setString(2, address);
            statement.executeUpdate();
            System.out.println("Server information inserted into database.");
        } catch (SQLException e) {
            System.out.println("Failed to insert server information into database: " + e.getMessage());
        }
    }
}
