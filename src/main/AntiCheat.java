import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AntiCheat implements Listener {

    private Plugin plugin;
    private Set<UUID> cheaters;

    public AntiCheat(Plugin plugin) {
        this.plugin = plugin;
        this.cheaters = new HashSet<>();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Check if the player has already been flagged as a cheater
        if (cheaters.contains(player.getUniqueId())) {
            event.setJoinMessage(null); // Hide join message for cheaters
            player.kickPlayer("You have been detected using a hacked client. Please remove any hacks/mods and try again.");
            return;
        }

        // Check for hacked client use
        new BukkitRunnable() {
            @Override
            public void run() {
                // Check if the player has any suspicious plugins installed
                boolean isCheater = player.getListeningPluginChannels().contains("MC|Brand")
                        && player.getListeningPluginChannels().contains("MC|BSign")
                        && player.getListeningPluginChannels().contains("MC|BOpen")
                        && player.getListeningPluginChannels().contains("MC|TrList")
                        && player.getListeningPluginChannels().contains("REGISTER")
                        && player.getListeningPluginChannels().contains("FML");
                if (isCheater) {
                    event.setJoinMessage(null); // Hide join message for cheaters
                    player.kickPlayer("You have been detected using a hacked client. Please remove any hacks/mods and try again.");
                    cheaters.add(player.getUniqueId()); // Flag the player as a cheater
                }
            }
        }.runTaskLater(plugin, 20L); // Wait 1 second before checking for hacks
    }
}
