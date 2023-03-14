import java.util.HashMap;
import java.util.Random;

public class PlayerIDGenerator {
    private static HashMap<String, String> playerIDs = new HashMap<String, String>();

    public static String generatePlayerID(String playerName) {
        String playerID = "";
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random rand = new Random();
        for (int i = 0; i < 6; i++) {
            playerID += characters.charAt(rand.nextInt(characters.length()));
        }
        playerIDs.put(playerName, playerID);
        return playerID;
    }

    public static String getPlayerID(String playerName) {
        if (playerIDs.containsKey(playerName)) {
            return playerIDs.get(playerName);
        } else {
            String playerID = generatePlayerID(playerName);
            return playerID;
        }
    }
}
