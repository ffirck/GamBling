package pl.edu.cwel.gamBling.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.edu.cwel.gamBling.GamBling;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class SlotsPlayListener implements Listener {

    private GamBling main;
    private final Inventory inv;

    public SlotsPlayListener(GamBling main){
        inv = Bukkit.createInventory(null, 45, "Rolling...");
        InitializeItems();
        this.main = main;
    }

    @EventHandler
    public void OnPlaySlots(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        if((e.getView().getTitle().equals("Slots"))
        || e.getView().getTitle().equals("Rolling...")
        || e.getView().getTitle().equals("You won!")
        || e.getView().getTitle().equals("You lost!"))
            e.setCancelled(true);

        if ((e.getView().getTitle().equals("Slots") && e.getSlot() == 22) || (e.getView().getItem(40).getType() == Material.MUSIC_DISC_CAT && e.getSlot() == 40)) {

            p.openInventory(inv);

            Roll();

            InitializeItems();

            RollColumn1(0);
            RollColumn2(50);
            RollColumn3(100);

            Bukkit.getScheduler().scheduleSyncDelayedTask(main, () ->
                    Results(p), 150L);

            for(int i = 0; i < 40; i++){
                int shiftAmount = i;
                Bukkit.getScheduler().scheduleSyncDelayedTask(main, () ->
                        RollColumn1(shiftAmount), 2L*i);
            }

            for(int i = 50; i < 90; i++){
                int shiftAmount = i;
                Bukkit.getScheduler().scheduleSyncDelayedTask(main, () ->
                        RollColumn2(shiftAmount), 2L*i - 80);
            }

            for(int i = 100; i < 140; i++){
                int shiftAmount = i;
                Bukkit.getScheduler().scheduleSyncDelayedTask(main, () ->
                        RollColumn3(shiftAmount), 2L*i - 160);
            }

        }

    }

    HashMap<Integer, ItemStack> rolledItems = new HashMap<>();

    public void Roll(){

        for(int i = 0; i < 150; i++) {
            int random = new Random().nextInt(0, 7);

            ItemStack rolledItem;

            switch(random){
                case 0:
                    rolledItem = itemStack(Material.GOLDEN_CARROT, "§r§e§l7", 7);
                    break;
                case 1:
                    rolledItem = itemStack(Material.DIAMOND, "§r§b§lJACKPOT");
                    break;
                case 2:
                    rolledItem = itemStack(Material.SWEET_BERRIES, "§r§4§lCHERRY");
                    break;
                case 3:
                    rolledItem = itemStack(Material.MELON, "§r§2§lWATERMELON");
                    break;
                case 4:
                    rolledItem = itemStack(Material.GOLDEN_APPLE, "§r§e§lLEMON");
                    break;
                case 5:
                    rolledItem = itemStack(Material.BEETROOT, "§r§c§lSTRAWBERRY");
                    break;
                case 6:
                    rolledItem = itemStack(Material.CHORUS_FRUIT, "§r§5§lPLUM");
                    break;
                default:
                    rolledItem = itemStack(Material.BARRIER, "§r§c§lERROR");
                    break;
            }

            rolledItems.put(i, rolledItem);

        }

    }

    public void Results(Player p){

        Inventory res;
        res = Bukkit.createInventory(null, 45, "You lost!");

        if(inv.getItem(21).getType() == inv.getItem(22).getType() && inv.getItem(22).getType() == inv.getItem(23).getType()){
            res = Bukkit.createInventory(null, 45, "You won!");
        }

        for(int i = 0; i < 45; i++){
            res.setItem(i, itemStack(Material.GRAY_STAINED_GLASS_PANE, " "));
        }

        //
        //  #####
        //  #   #
        //  #####
        //

        for(int i = 11; i < 16; i++){
            res.setItem(i, itemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
        }
        res.setItem(20, itemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
        res.setItem(24, itemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
        for(int i = 29; i < 34; i++){
            res.setItem(i, itemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
        }

        for(int i = 21; i < 24; i++) res.setItem(i, inv.getItem(i));

        res.setItem(40, itemStack(Material.MUSIC_DISC_CAT, "§a§kM §r§a§l→ PLAY AGAIN ← §r§a§kM"));

        p.openInventory(res);

    }

    public void InitializeItems(){

        for(int i = 0; i < 45; i++){
            inv.setItem(i, itemStack(Material.GRAY_STAINED_GLASS_PANE, " "));
            if((i - 2) % 9 == 0 || (i + 3) % 9 == 0) inv.setItem(i, itemStack(Material.BLACK_CANDLE, " "));
        }
        inv.setItem(20, itemStack(Material.BLACK_CARPET, " "));
        inv.setItem(24, itemStack(Material.BLACK_CARPET, " "));
    }

    public void RollColumn1(int shiftAmount){
        for(int i = 0; i < 5; i++){
            inv.setItem(i * 9 + 3, rolledItems.get(shiftAmount + i));
        }
    }

    public void RollColumn2(int shiftAmount){
        for(int i = 0; i < 5; i++){
            inv.setItem(i * 9 + 4, rolledItems.get(shiftAmount + i));
        }
    }

    public void RollColumn3(int shiftAmount){
        for(int i = 0; i < 5; i++){
            inv.setItem(i * 9 + 5, rolledItems.get(shiftAmount + i));
        }
    }

    ItemStack itemStack(Material mat, String name, Integer amount, String lore){
        if (mat == null){ mat = Material.AIR; }
        ItemStack item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();

        if(meta != null) {
            meta.setDisplayName(name);
            item.setAmount(amount);
            if(!lore.isEmpty()){
                meta.setLore(Arrays.asList(lore));
            }
            item.setItemMeta(meta);
        }

        return item;
    }

    ItemStack itemStack(Material mat, String name, Integer amount){
        if (mat == null){ mat = Material.AIR; }
        ItemStack item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();

        if(meta != null) {
            meta.setDisplayName(name);
            item.setAmount(amount);
            item.setItemMeta(meta);
        }

        return item;
    }

    ItemStack itemStack(Material mat, String name){
        if (mat == null){ mat = Material.AIR; }
        ItemStack item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();

        if(meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }

        return item;
    }

}
