import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CrashProtection extends JavaPlugin implements Listener {

    private final int MAX_MEMORY = 600;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        String command = event.getCommand();
        if (command.startsWith("java")) {
            String[] args = command.split(" ");
            for (int i = 0; i < args.length; i++) {
                if (args[i].startsWith("-Xmx")) {
                    String mem = args[i].substring(4);
                    if (mem.endsWith("G")) {
                        mem = mem.substring(0, mem.length() - 1);
                        try {
                            int gb = Integer.parseInt(mem);
                            if (gb > 0) {
                                int maxMem = gb * 1024;
                                if (maxMem > MAX_MEMORY) {
                                    event.setCancelled(true);
                                    Bukkit.getLogger().severe("[CrashProtection] Maximum memory usage of " + gb + "GB is not allowed! Please reduce memory usage.");
                                    break;
                                }
                            }
                        } catch (NumberFormatException e) {
                            Bukkit.getLogger().warning("[CrashProtection] Unable to parse memory value: " + mem);
                        }
                    } else if (mem.endsWith("M")) {
                        mem = mem.substring(0, mem.length() - 1);
                        try {
                            int mb = Integer.parseInt(mem);
                            if (mb > 0) {
                                if (mb > MAX_MEMORY) {
                                    event.setCancelled(true);
                                    Bukkit.getLogger().severe("[CrashProtection] Maximum memory usage of " + mb + "MB is not allowed! Please reduce memory usage.");
                                    break;
                                }
                            }
                        } catch (NumberFormatException e) {
                            Bukkit.getLogger().warning("[CrashProtection] Unable to parse memory value: " + mem);
                        }
                    } else {
                        Bukkit.getLogger().warning("[CrashProtection] Unknown memory unit: " + mem);
                    }
                }
            }
        }
    }

}
