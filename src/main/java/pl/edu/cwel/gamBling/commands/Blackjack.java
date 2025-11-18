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

public class Blackjack implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command c, String label, String[] args) {

        if (sender instanceof Player){
            Player p = (Player) sender;
            bjBetInv = Bukkit.createInventory(null, 45, "Blackjack - Bet");
            initializeItems();

            p.openInventory(bjBetInv);
        }

        return true;
    }

    public static Inventory bjBetInv;


    // bet GUI
    public void initializeItems(){

        for(int i = 0; i < 45; i++){
            bjBetInv.setItem(i, itemStack(Material.GRAY_STAINED_GLASS_PANE, " "));
        }

        // black stained glass panes
        //
        //   ###
        //   # #
        //   ###
        //
        for(int i = 12; i < 15; i++){
            bjBetInv.setItem(i, itemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
        }
        bjBetInv.setItem(21, itemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
        bjBetInv.setItem(23, itemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
        for(int i = 30; i < 33; i++){
            bjBetInv.setItem(i, itemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
        }

        // bet slot
        bjBetInv.setItem(20, new ItemStack(Material.AIR));

        // the item that starts the game after being clicked
        bjBetInv.setItem(22, itemStack(Material.DIAMOND_SWORD, "§a§kM §r§a§l→ PLAY ← §r§a§kM"));

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
