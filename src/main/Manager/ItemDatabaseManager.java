import java.sql.*;

public class ItemDatabaseManager {
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

    public static void insertItemData(String itemName, byte[] itemData, String serverName) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO items (item_name, item_data, server_name) VALUES (?, ?, ?)");
            statement.setString(1, itemName);
            statement.setBytes(2, itemData);
            statement.setString(3, serverName);
            statement.executeUpdate();
            System.out.println("Item data inserted into database.");
        } catch (SQLException e) {
            System.out.println("Failed to insert item data into database: " + e.getMessage());
        }
    }

    public static void updateItemData(String itemName, byte[] itemData, String serverName) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE items SET item_data = ? WHERE item_name = ? AND server_name = ?");
            statement.setBytes(1, itemData);
            statement.setString(2, itemName);
            statement.setString(3, serverName);
            statement.executeUpdate();
            System.out.println("Item data updated in database.");
        } catch (SQLException e) {
            System.out.println("Failed to update item data in database: " + e.getMessage());
        }
    }

    public static byte[] getItemData(String itemName, String serverName) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT item_data FROM items WHERE item_name = ? AND server_name = ?");
            statement.setString(1, itemName);
            statement.setString(2, serverName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBytes("item_data");
            }
        } catch (SQLException e) {
            System.out.println("Failed to get item data from database: " + e.getMessage());
