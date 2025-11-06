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

import java.util.Arrays;

public class OpenCase implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command c, String label, String[] args) {

        if (sender instanceof Player){
            Player p = (Player) sender;
            inv = Bukkit.createInventory(null, 45, "Case");
            initializeItems();

            p.openInventory(inv);
        }

        return true;
    }

    private Inventory inv;

    public static ItemStack getOpenItem(){
        ItemStack item = new ItemStack(Material.ENDER_CHEST, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§kM §r§l→ OPEN ← §r§kM");
        item.setItemMeta(meta);

        return item;
    }

    public void initializeItems(){

        for(int i = 0; i < 45; i++){
            inv.setItem(i, itemStack(Material.GRAY_STAINED_GLASS_PANE, " ", ""));
        }

        //
        //   ###
        //   # #
        //   ###
        //

        for(int i = 12; i < 15; i++){
            inv.setItem(i, itemStack(Material.BLACK_STAINED_GLASS_PANE, " ", ""));
        }
        inv.setItem(21, itemStack(Material.BLACK_STAINED_GLASS_PANE, " ", ""));
        inv.setItem(23, itemStack(Material.BLACK_STAINED_GLASS_PANE, " ", ""));
        for(int i = 30; i < 33; i++){
            inv.setItem(i, itemStack(Material.BLACK_STAINED_GLASS_PANE, " ", ""));
        }

        inv.setItem(22, getOpenItem());
    }

    ItemStack itemStack(Material mat, String name, String lore){
        ItemStack item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();

        if(meta != null) {
            meta.setDisplayName(name);
            if(!lore.isEmpty()){
                meta.setLore(Arrays.asList(lore));
            }
            item.setItemMeta(meta);
        }

        return item;
    }

}
