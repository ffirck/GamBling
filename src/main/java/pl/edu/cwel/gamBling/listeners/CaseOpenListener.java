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
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import pl.edu.cwel.gamBling.GamBling;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class CaseOpenListener implements Listener {

    private GamBling main;
    private final Inventory inv;

    public CaseOpenListener(GamBling main){
        inv = Bukkit.createInventory(null, 45, "Opening...");
        try {
            initializeItems(0);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        this.main = main;
    }

    @EventHandler
    public void OnOpenCase(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        if(e.getView().getTitle().equals("Case")
        || e.getView().getTitle().equals("Opening...")
        || (e.getView().getTitle().equals("Results")
        && (e.getSlot() != 22)))
            e.setCancelled(true);

        if (e.getView().getTitle().equals("Opening...") && e.getSlot() == 44) {
            Bukkit.getScheduler().cancelTasks(main);
            OpeningResults(p);
        }

        if (e.getView().getTitle().equals("Case") && e.getSlot() == 22) {

            RollItems();
            p.openInventory(inv);

            Bukkit.getScheduler().scheduleSyncDelayedTask(main, () ->
                    OpeningResults(p), (310L));

            for(int i = 1; i < 52; i++) {
                int offset = i;

                Bukkit.getScheduler().scheduleSyncDelayedTask(main, () ->
                {
                    try {
                        initializeItems(offset);
                    } catch (MalformedURLException ex) {
                        throw new RuntimeException(ex);
                    }
                }, (i * i / 10));
            }

        }
    }

    public void OpeningResults(Player p){
        Inventory res = Bukkit.createInventory(null, 45, "Results");

        for(int i = 0; i < 45; i++){
            res.setItem(i, itemStack(Material.GRAY_STAINED_GLASS_PANE, " ", ""));
        }

        //
        //   ###
        //   # #
        //   ###
        //

        for(int i = 12; i < 15; i++){
            res.setItem(i, itemStack(Material.BLACK_STAINED_GLASS_PANE, " ", ""));
        }
        res.setItem(21, itemStack(Material.BLACK_STAINED_GLASS_PANE, " ", ""));
        res.setItem(23, itemStack(Material.BLACK_STAINED_GLASS_PANE, " ", ""));
        for(int i = 30; i < 33; i++){
            res.setItem(i, itemStack(Material.BLACK_STAINED_GLASS_PANE, " ", ""));
        }

        res.setItem(22, itemStack(caseRarityMats.get(55), caseRarityNames.get(55), ""));

        p.openInventory(res);
    }

    HashMap<Integer, Material> caseRarityMats = new HashMap<>();
    HashMap<Integer, String> caseRarityNames = new HashMap<>();



    public void RollItems(){
        caseRarityMats.clear();
        caseRarityNames.clear();

        for(int i = 0; i < 100; i++){

            int random = new Random().nextInt(1000);

            int rarity = 0; // common chance 60%
            if(random > 994){
                rarity = 5; // mythic chance 0.45%
                if (new Random().nextInt(10) == 9) rarity = 6; // jackpot chance 0.1*mythic chance
            }
            if(random <= 994 && random > 979) rarity = 4; // legendary chance 1.5%
            if(random <= 979 && random > 949) rarity = 3; // epic chance 3%
            if(random <= 949 && random > 824) rarity = 2; // rare chance 12.5%
            if(random <= 824 && random > 599) rarity = 1; // uncommon chance 22.5%

            Material rarityMaterial;
            String rarityName = switch (rarity) {
                case 0 -> {
                    rarityMaterial = Material.WHITE_STAINED_GLASS_PANE;
                    yield "§r§f§lCOMMON";
                }
                case 1 -> {
                    rarityMaterial = Material.GREEN_STAINED_GLASS_PANE;
                    yield "§r§2§lUNCOMMON";
                }
                case 2 -> {
                    rarityMaterial = Material.BLUE_STAINED_GLASS_PANE;
                    yield "§r§9§lRARE";
                }
                case 3 -> {
                    rarityMaterial = Material.PURPLE_STAINED_GLASS_PANE;
                    yield "§r§5§lEPIC";
                }
                case 4 -> {
                    rarityMaterial = Material.YELLOW_STAINED_GLASS_PANE;
                    yield "§r§e§lLEGENDARY";
                }
                case 5 -> {
                    rarityMaterial = Material.RED_STAINED_GLASS_PANE;
                    yield "§r§c§lMYTHIC";
                }
                case 6 -> {
                    rarityMaterial = Material.DIAMOND;
                    yield "§r§f§lJACKPOT";
                }
                default -> {
                    rarityMaterial = Material.BARRIER;
                    yield "§r§4§lERROR";
                }
            };

            caseRarityMats.put(i, rarityMaterial);
            caseRarityNames.put(i, rarityName);
        }
    }

    public void initializeItems(int offset) throws MalformedURLException {
        for(int i = 0; i < 45; i++){
            inv.setItem(i, itemStack(Material.GRAY_STAINED_GLASS_PANE, " ", ""));
        }

        for(int i = 9; i < 36; i++){
            inv.setItem(i, itemStack(Material.BLACK_STAINED_GLASS_PANE, " ", ""));
        }

        inv.setItem(13, itemStack(Material.POINTED_DRIPSTONE, "↓", ""));

        ItemStack skip = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skipMeta = (SkullMeta) skip.getItemMeta();
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID(), "");
        PlayerTextures texture = profile.getTextures();
        texture.setSkin(new URL("http://textures.minecraft.net/texture/4ef356ad2aa7b1678aecb88290e5fa5a3427e5e456ff42fb515690c67517b8"));
        profile.setTextures(texture);
        skipMeta.setOwnerProfile(profile);
        skipMeta.setDisplayName("§r§a§l→ SKIP →");
        skip.setItemMeta(skipMeta);

        inv.setItem(44, skip);

        for(int i = 18; i < 27; i++){
            inv.setItem(i, itemStack(caseRarityMats.get(i - 18 + offset), caseRarityNames.get(i - 18 + offset), ""));
        }

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

}
