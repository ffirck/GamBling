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
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.UUID;

public class Roulette implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command c, String label, String[] args) {

        if (sender instanceof Player){
            Player p = (Player) sender;
            betInv = Bukkit.createInventory(null, 45, "Roulette");
            try {
                initializeItems();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            p.openInventory(betInv);
        }

        return true;
    }

    public static Inventory betInv;

    public static ItemStack getPlayItem(){
        ItemStack item = new ItemStack(Material.HEAVY_CORE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a§kM §r§a§l→ PLAY ← §r§a§kM");
        item.setItemMeta(meta);

        return item;
    }

    // bet GUI
    public void initializeItems() throws MalformedURLException {

        for(int i = 0; i < 45; i++){
            betInv.setItem(i, itemStack(Material.GRAY_STAINED_GLASS_PANE, " ", ""));
        }

        // black stained glass panes pattern
        //
        //   ###
        //   # #
        //   ###
        //
        for(int i = 12; i < 15; i++){
            betInv.setItem(i, itemStack(Material.BLACK_STAINED_GLASS_PANE, " ", ""));
        }
        betInv.setItem(21, itemStack(Material.BLACK_STAINED_GLASS_PANE, " ", ""));
        betInv.setItem(23, itemStack(Material.BLACK_STAINED_GLASS_PANE, " ", ""));
        for(int i = 30; i < 33; i++){
            betInv.setItem(i, itemStack(Material.BLACK_STAINED_GLASS_PANE, " ", ""));
        }

        // bet slot
        betInv.setItem(20, new ItemStack(Material.AIR));

        // change bet color/number item (next)
        ItemStack upArrow = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta upArrowMeta = (SkullMeta) upArrow.getItemMeta();
        PlayerProfile profileUp = Bukkit.createPlayerProfile(UUID.randomUUID(), "");
        PlayerTextures textureUp = profileUp.getTextures();
        textureUp.setSkin(new URL("http://textures.minecraft.net/texture/b221da4418bd3bfb42eb64d2ab429c61decb8f4bf7d4cfb77a162be3dcb0b927"));
        profileUp.setTextures(textureUp);
        upArrowMeta.setOwnerProfile(profileUp);
        upArrowMeta.setDisplayName("§r§aNEXT");
        upArrow.setItemMeta(upArrowMeta);

        // change bet color/number item (previous)
        ItemStack downArrow = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta downArrowMeta = (SkullMeta) downArrow.getItemMeta();
        PlayerProfile profileDown = Bukkit.createPlayerProfile(UUID.randomUUID(), "");
        PlayerTextures textureDown = profileDown.getTextures();
        textureDown.setSkin(new URL("http://textures.minecraft.net/texture/3b83bbccf4f0c86b12f6f79989d159454bf9281955d7e2411ce98c1b8aa38d8"));
        profileDown.setTextures(textureDown);
        downArrowMeta.setOwnerProfile(profileDown);
        downArrowMeta.setDisplayName("§r§aPREVIOUS");
        downArrow.setItemMeta(downArrowMeta);

        betInv.setItem(15, upArrow);
        betInv.setItem(24, itemStack(Material.GREEN_CONCRETE, "§rBET§7: §2§lGREEN", "§r§8Payout: §710§8x"));
        betInv.setItem(33, downArrow);

        // the item that starts the game after being clicked
        betInv.setItem(22, getPlayItem());
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
