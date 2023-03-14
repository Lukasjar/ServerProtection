import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class AntiVPN extends JavaPlugin implements Listener {

    private DatabaseReader reader;
    private final Map<String, String> playerIPs = new HashMap<>();
    private final String[] blockedCountries = {"A1", "A2", "O1"}; // add more blocked countries here

    @Override
    public void onEnable() {
        try {
            File database = new File(getDataFolder(), "GeoLite2-Country.mmdb");
            reader = new DatabaseReader.Builder(database).build();
            getServer().getPluginManager().registerEvents(this, this);
        } catch (IOException e) {
            getLogger().severe("Could not load GeoIP database. Disabling AntiVPN plugin.");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        String playerIP = player.getAddress().getAddress().getHostAddress();

        // Check for blocked countries
        try {
            InetAddress ipAddress = InetAddress.getByName(playerIP);
            CountryResponse response = reader.country(ipAddress);
            Country country = response.getCountry();
            String countryCode = country.getIsoCode();
            if (countryCode != null && countryCode.length() == 2) {
                for (String blockedCountry : blockedCountries) {
                    if (countryCode.equals(blockedCountry)) {
                        event.setJoinMessage(null);
                        player.kickPlayer("VPN usage is not allowed on this server.");
                        return;
                    }
                }
            }
        } catch (Exception e) {
            getLogger().warning("Could not check player IP for VPN usage: " + e.getMessage());
        }

        // Check for VPN usage
        if (isVPN(playerIP)) {
            event.setJoinMessage(null);
            player.kickPlayer("VPN usage is not allowed on this server.");
            return;
        }

        // Check for multiple accounts
        if (playerIPs.containsValue(playerIP)) {
            event.setJoinMessage(null);
            player.kickPlayer("You are not allowed to join with multiple accounts.");
        } else {
            playerIPs.put(playerName, playerIP);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        playerIPs.remove(playerName);
    }

    private boolean isVPN(String ipAddress) {
        // Implement your VPN detection code here
        // You can use a third-party VPN
	  // For example, here is a method that uses the ipqualityscore.com API to check for VPN usage
    try {
        URL url = new URL("https://ipqualityscore.com/api/json/ip/<your API key>/<your IP address>");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject json = new JSONObject(response.toString());
        boolean isProxy = json.getBoolean("proxy");
        return isProxy;
    } catch (Exception e) {
        getLogger().warning("Could not check player IP for VPN usage: " + e.getMessage());
        return false;
    }
}

