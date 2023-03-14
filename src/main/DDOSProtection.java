import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class DDOSProtection extends JavaPlugin implements Listener {

    private final Map<String, String> playerIPs = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        String playerIP = player.getAddress().getAddress().getHostAddress();

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
}
