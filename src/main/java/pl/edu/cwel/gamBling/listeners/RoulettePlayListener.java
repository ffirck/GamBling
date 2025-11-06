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
import java.util.Objects;
import java.util.Random;

import static pl.edu.cwel.gamBling.commands.Roulette.betInv;

public class RoulettePlayListener implements Listener {

    private GamBling main;
    private final Inventory inv;

    public RoulettePlayListener(GamBling main){
        inv = Bukkit.createInventory(null, 54, "Rolling...");
        initializeItems();
        this.main = main;
    }

    @EventHandler
    public void OnPlayRoulette(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        if((e.getView().getTitle().equals("Roulette") && (e.getSlot() != 20 && e.getClickedInventory() != p.getInventory()))
        || e.getView().getTitle().equals("Rolling...")
        || e.getView().getTitle().equals("You won! - Roulette")
        || e.getView().getTitle().equals("You lost! - Roulette"))
            e.setCancelled(true);

        if (e.getView().getTitle().equals("Roulette") && e.getSlot() == 15 && e.getClickedInventory() != p.getInventory()) {

            ItemStack previous = betInv.getItem(24);

            Material betColor = previous.getType() == Material.RED_CONCRETE ? Material.BLACK_CONCRETE : Material.RED_CONCRETE;
            if (previous.getAmount() == 8) betColor = Material.RED_CONCRETE;
            if (previous.getAmount() == 20) betColor = Material.BLACK_CONCRETE;
            betInv.setItem(24, itemStack(betColor, "§rBET§7: §f§l" + (previous.getAmount() + 1), (previous.getAmount() + 1), "§r§8Payout: §720§8x"));

            if(previous.getType() == Material.GREEN_CONCRETE) betInv.setItem(24, itemStack(Material.BLACK_CONCRETE, "§rBET§7: §8§lBLACK", "§r§8Payout: §72§8x"));
            if(previous.getItemMeta().getDisplayName().contains("§8§lBLACK")) betInv.setItem(24, itemStack(Material.RED_CONCRETE, "§rBET§7: §4§lRED", "§r§8Payout: §72§8x"));
            if(previous.getItemMeta().getDisplayName().contains("§4§lRED")) betInv.setItem(24, itemStack(Material.BLACK_CONCRETE, "§rBET§7: §f§l1", "§r§8Payout: §720§8x"));

            if (previous.getAmount() >= 24) betInv.setItem(24, itemStack(Material.GREEN_CONCRETE, "§rBET§7: §2§lGREEN", "§r§8Payout: §710§8x"));

        } else if (e.getView().getTitle().equals("Roulette") && e.getSlot() == 33 && e.getClickedInventory() != p.getInventory()) {

            ItemStack previous = betInv.getItem(24);

            Material betColor = previous.getType() == Material.RED_CONCRETE ? Material.BLACK_CONCRETE : Material.RED_CONCRETE;
            if (previous.getAmount() == 9) betColor = Material.RED_CONCRETE;
            if (previous.getAmount() == 21) betColor = Material.BLACK_CONCRETE;
            if (previous.getAmount() != 1) betInv.setItem(24, itemStack(betColor, "§rBET§7: §f§l" + (previous.getAmount() - 1), (previous.getAmount() - 1), "§r§8Payout: §720§8x"));

            if(previous.getType() == Material.GREEN_CONCRETE) betInv.setItem(24, itemStack(Material.RED_CONCRETE, "§rBET§7: §f§l24", 24, "§r§8Payout: §720§8x"));
            if(previous.getItemMeta().getDisplayName().contains("§8§lBLACK")) betInv.setItem(24, itemStack(Material.GREEN_CONCRETE, "§rBET§7: §2§lGREEN", "§r§8Payout: §710§8x"));
            if(previous.getItemMeta().getDisplayName().contains("§4§lRED")) betInv.setItem(24, itemStack(Material.BLACK_CONCRETE, "§rBET§7: §8§lBLACK", "§r§8Payout: §72§8x"));
            if(!previous.getItemMeta().getDisplayName().contains("§8§lBLACK")
                && previous.getType() == Material.BLACK_CONCRETE
                && previous.getAmount() == 1) betInv.setItem(24, itemStack(Material.RED_CONCRETE, "§rBET§7: §4§lRED", "§r§8Payout: §72§8x"));

        }

        if (e.getView().getTitle().equals("Roulette") && e.getSlot() == 22 && e.getView().getItem(20).getType() != Material.AIR) {

            p.openInventory(inv);
            initializeItems();

            int shiftAmount = new Random().nextInt(40,67);

            Bukkit.getScheduler().scheduleSyncDelayedTask(main, () ->
                    Results(p), (shiftAmount*shiftAmount/10+40));

            for(int i = 0; i < shiftAmount; i++) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {

                    ItemStack tempItem = inv.getItem(53);

                    for (int slot = 53; slot >= 0; slot--){

                        if((slot % 9 == 0) && slot != 0){
                            inv.setItem(slot, inv.getItem(slot - 9));
                        }
                        if(slot > 45){
                            inv.setItem(slot, inv.getItem(slot - 1));
                        }

                    }
                    for (int slot = 0; slot < 54; slot++){

                        if(slot < 8){
                            inv.setItem(slot, inv.getItem(slot + 1));
                        }
                        if(((slot + 1) % 9 == 0) && slot != 53){
                            inv.setItem(slot, inv.getItem(slot + 9));
                        }

                    }

                    inv.setItem(44, tempItem);

                }, (i * i / 10));
            }

        }

        if(e.getView().getTitle().contains("! - Roulette") && Objects.requireNonNull(e.getView().getItem(40)).getType() == Material.MUSIC_DISC_CAT && e.getSlot() == 40){
            p.performCommand("roulette");
        }

    }

    public void Results(Player p){

        ItemStack bet = betInv.getItem(20);
        ItemMeta resultMeta = bet.getItemMeta();

        Inventory res;
        res = Bukkit.createInventory(null, 45, "You lost! - Roulette");

        if((betInv.getItem(24).getItemMeta().getDisplayName().contains("§8§lBLACK") && inv.getItem(49).getType() == Material.BLACK_CONCRETE)
            || (betInv.getItem(24).getItemMeta().getDisplayName().contains("§4§lRED") && inv.getItem(49).getType() == Material.RED_CONCRETE)) {
            p.getInventory().addItem(bet);
            p.getInventory().addItem(bet);
            int amt = 2 * bet.getAmount();

            if(inv.getItem(49).getAmount() == betInv.getItem(24).getAmount()) {
                for(int i = 0; i < 18; i++){
                    p.getInventory().addItem(bet);
                }
                amt = 20 * bet.getAmount();
            }

            resultMeta.setDisplayName("§r§lYou won: §r§7" + amt + "§8x §r§7" + bet.getType());

            res = Bukkit.createInventory(null, 45, "You won! - Roulette");

        } else if (inv.getItem(49).getAmount() == betInv.getItem(24).getAmount() && betInv.getItem(24).getType() != Material.GREEN_CONCRETE) {
            for(int i = 0; i < 20; i++){
                p.getInventory().addItem(bet);
            }
            int amt = 20 * bet.getAmount();

            resultMeta.setDisplayName("§r§lYou won: §r§7" + amt + "§8x §r§7" + bet.getType());

            res = Bukkit.createInventory(null, 45, "You won! - Roulette");
        }
        if (inv.getItem(49).getType() == Material.GREEN_CONCRETE && inv.getItem(24).getType() == Material.GREEN_CONCRETE) {
            for(int i = 0; i < 10; i++){
                p.getInventory().addItem(bet);
            }
            int amt = 10 * bet.getAmount();

            resultMeta.setDisplayName("§r§lYou won: §r§7" + amt + "§8x §r§7" + bet.getType());

            res = Bukkit.createInventory(null, 45, "You won! - Roulette");
        }

        for(int i = 0; i < 45; i++){
            res.setItem(i, itemStack(Material.GRAY_STAINED_GLASS_PANE, " "));
        }

        //
        //   ###
        //   # #
        //   ###
        //

        for(int i = 12; i < 15; i++){
            res.setItem(i, itemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
        }
        res.setItem(21, itemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
        res.setItem(23, itemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
        for(int i = 30; i < 33; i++){
            res.setItem(i, itemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
        }

        res.setItem(22, inv.getItem(49));
        res.getItem(22).setItemMeta(resultMeta);

        res.setItem(40, itemStack(Material.MUSIC_DISC_CAT, "§a§kM §r§a§l→ PLAY AGAIN ← §r§a§kM"));

        p.openInventory(res);
    }

    public void initializeItems(){
        for(int i = 0; i < 54; i++){

            if(i % 2 == 0 && i < 10){
                inv.setItem(i, new ItemStack(Material.BLACK_CONCRETE, i + 1));
            }
            if((i + 1) % 2 == 0 && i < 10){
                inv.setItem(i, new ItemStack(Material.RED_CONCRETE, i + 1));
            }
            switch(i){
                case 18 -> inv.setItem(i, new ItemStack(Material.BLACK_CONCRETE, 23));
                case 26 -> inv.setItem(i, new ItemStack(Material.BLACK_CONCRETE, 10));
                case 36 -> inv.setItem(i, new ItemStack(Material.BLACK_CONCRETE, 21));
                case 44 -> inv.setItem(i, new ItemStack(Material.BLACK_CONCRETE, 12));

                case 9 -> inv.setItem(i, new ItemStack(Material.RED_CONCRETE, 24));
                case 17 -> inv.setItem(i, new ItemStack(Material.RED_CONCRETE, 9));
                case 27 -> inv.setItem(i, new ItemStack(Material.RED_CONCRETE, 22));
                case 35 -> inv.setItem(i, new ItemStack(Material.RED_CONCRETE, 11));
            }
            if(i % 2 == 0 && i > 44){
                inv.setItem(i, new ItemStack(Material.BLACK_CONCRETE, 66 - i));
            }
            if((i + 1) % 2 == 0 && i > 44){
                inv.setItem(i, new ItemStack(Material.RED_CONCRETE, 66 - i));
            }
        }

        inv.setItem(8, itemStack(Material.GREEN_CONCRETE, "§2§lGREEN"));
        inv.setItem(45, itemStack(Material.GREEN_CONCRETE, "§2§lGREEN"));


        //
        //
        //  #####
        //  #####
        //
        //
        for(int i = 20; i < 25; i++){
            inv.setItem(i, itemStack(Material.GRAY_STAINED_GLASS_PANE, " "));
        }
        for(int i = 29; i < 34; i++){
            inv.setItem(i, itemStack(Material.GRAY_STAINED_GLASS_PANE, " "));
        }

        //
        // #######
        // #     #
        // #     #
        // #######
        //

        for(int i = 10; i < 17; i++){
            inv.setItem(i, itemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
        }
        for(int i = 37; i < 44; i++){
            inv.setItem(i, itemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
        }
        for(int i = 19; i < 35; i++){
            if(((i - 1) % 9 == 0) || ((i + 2) % 9 == 0)) inv.setItem(i, itemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
        }

        inv.setItem(40, itemStack(Material.HEAVY_CORE, "§r§7§lBALL"));

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

    ItemStack itemStack(Material mat, String name, String lore){
        if (mat == null){ mat = Material.AIR; }
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
