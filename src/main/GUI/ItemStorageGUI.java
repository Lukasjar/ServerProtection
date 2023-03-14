import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class ItemStorageGUI implements Listener {

    private static Plugin plugin;
    private static HashMap<Player, Inventory> inventories = new HashMap<>();

    public static void setPlugin(Plugin plugin) {
        ItemStorageGUI.plugin = plugin;
    }

    public static void openGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 27, "Item Storage");
        inventories.put(player, inventory);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.openInventory(inventory);
            }
        }.runTask(plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equals("Item Storage")) {
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem == null || clickedItem.getType() == Material.AIR) {
                return;
            }

            if (clickedItem.getItemMeta().getDisplayName().equals("Close")) {
                player.closeInventory();
            } else if (clickedItem.getItemMeta().getDisplayName().equals("Send to other server")) {
                // Insert code to send item data to other server here
            }
        }
    }

    public static void updateInventory(Player player, ItemStack[] contents) {
        Inventory inventory = inventories.get(player);
        inventory.setContents(contents);
    }

    public static void removeInventory(Player player) {
        inventories.remove(player);
    }

    public static ItemStack[] getInventory(Player player) {
        Inventory inventory = inventories.get(player);
        return inventory.getContents();
    }

    public static ItemStack createCloseButton() {
        ItemStack itemStack = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("Close");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack createSendButton() {
        ItemStack itemStack = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("Send to other server");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
