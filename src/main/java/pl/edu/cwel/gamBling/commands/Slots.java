package pl.edu.cwel.gamBling.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Slots implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command c, String label, String[] args) {

        if (sender instanceof Player){
            Player p = (Player) sender;
            inv = Bukkit.createInventory(null, 45, "Slots");
            initializeItems();

            p.openInventory(inv);
        }

        return true;
    }

    private Inventory inv;

    public static ItemStack getOpenItem(){
        ItemStack item = new ItemStack(Material.GOLDEN_CARROT, 7);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a§kM §r§a§l→ PLAY ← §r§a§kM");
        item.setItemMeta(meta);

        return item;
    }

    public void initializeItems(){

        for(int i = 0; i < 45; i++){
            inv.setItem(i, itemStack(Material.GRAY_STAINED_GLASS_PANE, " "));
        }

        //
        //   ###
        //   # #
        //   ###
        //

        for(int i = 12; i < 15; i++){
            inv.setItem(i, itemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
        }
        inv.setItem(21, itemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
        inv.setItem(23, itemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
        for(int i = 30; i < 33; i++){
            inv.setItem(i, itemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
        }

        inv.setItem(22, getOpenItem());
    }

    ItemStack itemStack(Material mat, String name){
        ItemStack item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();

        if(meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }

        return item;
    }

}
