import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IDCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("id")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                String playerID = PlayerIDGenerator.getPlayerID(player.getName());
                player.sendMessage("Your ID is: " + playerID);
            } else {