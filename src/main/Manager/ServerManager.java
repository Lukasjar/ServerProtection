import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServerManager {
    public static void sendServerInformation(String serverName) {
        try {
            PreparedStatement statement = DatabaseManager.getConnection().prepareStatement("SELECT * FROM servers WHERE name = ?");
            statement.setString(1, serverName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String address = resultSet.getString("address");
                String[] addressParts = address.split(":");
                String ipAddress = addressParts[0];
                int port = Integer.parseInt(addressParts[1]);

                Socket socket = new Socket(ipAddress,
