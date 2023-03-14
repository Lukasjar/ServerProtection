import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.net.InetAddress;
import java.util.UUID;

public class PlayerInfoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("Usage: /playerinfo <player>");
            return true;
        }

        Player target = sender.getServer().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found.");
            return true;
        }

        UUID uuid = target.getUniqueId();
        String country = GeoIPUtils.getCountry(uuid);
        InetAddress ipAddress = GeoIPUtils.getIPAddress(uuid);
        String joinDate = JoinDateUtils.getFormattedJoinDate(uuid);
        boolean isMultiAccount = AntiDDOS.getInstance().isMultiAccount(uuid);

        sender.sendMessage("Player information for " + target.getName() + ":");
        sender.sendMessage("- Country: " + country);
        sender.sendMessage("- IP address: " + ipAddress.getHostAddress());
        sender.sendMessage("- First join date: " + joinDate);
        sender.sendMessage("- Multi-account usage: " + (isMultiAccount ? "yes" : "no"));

        return true;
    }
}
