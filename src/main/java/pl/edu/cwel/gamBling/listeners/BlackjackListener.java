package pl.edu.cwel.gamBling.listeners;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import pl.edu.cwel.gamBling.GamBling;

import java.util.*;

import static pl.edu.cwel.gamBling.GamBling.getNameFromKey;
import static pl.edu.cwel.gamBling.commands.Blackjack.bjBetInv;

public class BlackjackListener implements Listener {

    private GamBling main;
    private final Inventory inv;

    public BlackjackListener(GamBling main){
        inv = Bukkit.createInventory(null, 54, "Blackjack");
        InitializeItems();
        this.main = main;
    }

    int score = 0;
    int dealerScore = 0;
    int round = 0;
    int dealerRound = 0;
    boolean cooldown = false;

    public List<ItemStack> deck = Deck();

    ItemStack bet = itemStack(Material.BARRIER, "Placeholder");

    @EventHandler
    public void OnClick(InventoryClickEvent e){

        // correct inventory check
        Player p = (Player) e.getWhoClicked();
        if((e.getView().getTitle().equals("Blackjack"))
        || (e.getView().getTitle().equals("Blackjack - Bet")
        && e.getSlot() != 20 && e.getClickedInventory() != p.getInventory())
        || e.getView().getTitle().equals("You won! - Blackjack")
        || e.getView().getTitle().equals("You drew! - Blackjack")
        || e.getView().getTitle().equals("You lost! - Blackjack"))
            e.setCancelled(true);

        // after clicking the item that starts the game
        if(e.getView().getTitle().equals("Blackjack - Bet") && Objects.requireNonNull(e.getView().getItem(22)).getType() == Material.DIAMOND_SWORD && e.getSlot() == 22
                && Objects.requireNonNull(e.getView().getItem(20)).getType() != Material.AIR){
            bet = bjBetInv.getItem(20);

            Collections.shuffle(deck);
            InitializeItems();
            p.openInventory(inv);
        }

        //hit
        if (e.getView().getTitle().equals("Blackjack") && Objects.requireNonNull(e.getView().getItem(13)).getType() == Material.DIAMOND_SWORD && e.getSlot() == 13 && !cooldown) {

            if(score < 21 && dealerScore < 21) {

                cooldown = true;

                if(score > 10){
                    PlayerRound(p);
                    if(score < 21) Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> DealerRound(p), 10L);
                }

                while (score <= 10) {
                    PlayerRound(p);
                }

                while (dealerScore <= 10) {
                    if(score < 21) {
                        DealerRound(p);
                    } else break;
                }
            }
        }

        //stand
        if(e.getView().getTitle().equals("Blackjack") && Objects.requireNonNull(e.getView().getItem(40)).getType() == Material.SHIELD && e.getSlot() == 40 && !cooldown){
            cooldown = true;
            DealerRound(p);
            if(dealerScore < 21) Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> Results(p, score, dealerScore), 50L);
        }

        //play again
        if(e.getView().getTitle().contains("! - Blackjack") && Objects.requireNonNull(e.getView().getItem(39)).getType() == Material.MUSIC_DISC_CAT && e.getSlot() == 39){
            p.performCommand("blackjack");
        }

        //play again same bet
        if(e.getView().getTitle().contains("! - Blackjack") && Objects.requireNonNull(e.getView().getItem(41)).getType() == Material.MUSIC_DISC_CHIRP && e.getSlot() == 41){

            if(bet != null && p.getInventory().contains(bet)){
                p.getInventory().removeItem(bet);

                Collections.shuffle(deck);
                InitializeItems();
                p.openInventory(inv);
            }

        }

    }

    public void PlayerRound(Player p){
        ItemStack drawnCard = deck.getFirst();
        score += drawnCard.getAmount();
        round += 1;

        deck.removeFirst();

        // putting the item in the correct place (because the card gui
        // is 2x4
        int slot = switch (round) {
            case 1 -> 9;
            case 2 -> 10;
            case 3 -> 18;
            case 4 -> 19;
            case 5 -> 27;
            case 6 -> 28;
            case 7 -> 36;
            case 8 -> 37;
            default -> 9;
        };

        inv.setItem(slot, drawnCard);

        // game end (player's score at or over 21)
        if (score >= 21) Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> Results(p, score, dealerScore), 50L);
    }

    public void DealerRound(Player p){

        cooldown = false;

        // the dealer will hit if:
        //
        // the player's score is at or above 15,
        // AND the dealer's score is less than 15
        // OR it hits a random 50% chance
        // AND the dealer's score is less than 17
        //
        // OR
        //
        // the player's score is bigger than the dealer's
        // AND the dealer's score is less than 17
        //
        // otherwise it will stand
        if((((score >= 15 && dealerScore < 15) || new Random().nextInt(2) > 0) && dealerScore < 17) || (score > dealerScore && dealerScore < 17)) {

            ItemStack drawnCard = deck.getFirst();
            dealerScore += drawnCard.getAmount();
            dealerRound += 1;

            deck.removeFirst();

            // correct slot
            int slot = switch (dealerRound) {
                case 1 -> 16;
                case 2 -> 17;
                case 3 -> 25;
                case 4 -> 26;
                case 5 -> 34;
                case 6 -> 35;
                case 7 -> 43;
                case 8 -> 44;
                default -> 16;
            };

            inv.setItem(slot, drawnCard);
        } else {
            if(round > 2) Results(p, score, dealerScore);
        }

        // game end (dealer's score at or over 21)
        if (dealerScore >= 21) Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> Results(p, score, dealerScore), 50L);
    }

    // list of 52 items (the deck)
    public List<ItemStack> Deck(){

        List<ItemStack> deck = new ArrayList<>();

        int cardValue = 1;

        for(int i = 0; i < 52; i++){
            ItemStack is = new ItemStack(Material.WHITE_BANNER);
            BannerMeta meta = (BannerMeta) is.getItemMeta();

            String cardColor = "";

            // spades
            if(i % 4 == 0){
                cardColor = "§8§l";
                meta.addPattern(new Pattern(DyeColor.BLACK, PatternType.RHOMBUS));
                meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.TRIANGLE_BOTTOM));
                meta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRAIGHT_CROSS));
                meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));
                meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.TRIANGLES_TOP));
                is.setItemMeta(meta);

                cardValue += 1;
            }

            // clubs
            if(i % 4 == 1){
                cardColor = "§8§l";
                is = new ItemStack(Material.BLACK_BANNER);
                meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.CURLY_BORDER));
                meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.CREEPER));
                meta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRAIGHT_CROSS));
                meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM));
                meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
                meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));
                is.setItemMeta(meta);
            }

            // diamonds
            if(i % 4 == 2){
                cardColor = "§c§l";
                meta.addPattern(new Pattern(DyeColor.RED, PatternType.RHOMBUS));
                is.setItemMeta(meta);
            }

            // hearts
            if(i % 4 == 3){
                cardColor = "§c§l";
                meta.addPattern(new Pattern(DyeColor.RED, PatternType.RHOMBUS));
                meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.TRIANGLE_TOP));
                is.setItemMeta(meta);
            }

            if(cardValue <= 10) {
                is.setAmount(cardValue);
                meta.setDisplayName(cardColor + cardValue);
            } else {
                // handling cards over 10
                is.setAmount(10);
                switch (cardValue){
                    case 11:
                        // jack
                        meta.setDisplayName(cardColor + "J");
                        break;
                    case 12:
                        // queen
                        meta.setDisplayName(cardColor + "Q");
                        break;
                    case 13:
                        // king
                        meta.setDisplayName(cardColor + "K");
                        break;
                    case 14:
                        // ace
                        is.setAmount(11);
                        meta.setDisplayName(cardColor + "A");
                        break;
                }
            }

            is.setItemMeta(meta);
            deck.add(is);

        }

        return deck;

    }

    public void Results(Player p, int scoreResult, int dealerScoreResult){

        cooldown = false;

        score = 0;
        dealerScore = 0;
        round = 0;
        dealerRound = 0;

        deck = Deck();
        Collections.shuffle(deck);

        // you lose by default, it gets overridden if you win or tie
        Inventory res;
        res = Bukkit.createInventory(null, 45, "You lost! - Blackjack");

        ItemStack result = itemStack(bet.getType(), "§r§cYou lost!");
        ItemMeta resultMeta = result.getItemMeta();

        String resultName = getNameFromKey(result.getTranslationKey());

        if(scoreResult == dealerScoreResult){
            // tie
            res = Bukkit.createInventory(null, 45, "You drew! - Blackjack");

            p.sendMessage("§b§lDRAW! §rYou got returned: " + bet.getAmount() + "§7x §r§b" + resultName + "§r!");
            p.getInventory().addItem(bet);

            resultMeta.setDisplayName("§r§lYou got returned §r§7" + bet.getAmount() + "§8x §r§7" + resultName);

        } else if(scoreResult == 21 || dealerScoreResult > 21 || (scoreResult > dealerScoreResult && scoreResult <= 21)){
            // win if:
            // the player's score is 21,
            // OR the dealer's score is over 21,
            //
            // OR
            //
            // the player's score is bigger than the dealer's,
            // AND if the player's score is less or equal to 21
            // (in case the game ends by standing)
            res = Bukkit.createInventory(null, 45, "You won! - Blackjack");

            p.getInventory().addItem(bet);
            p.getInventory().addItem(bet);

            int amt = 2 * bet.getAmount();

            resultMeta.setDisplayName("§r§lYou won: §r§7" + amt + "§8x §r§7" + resultName);
            p.sendMessage("§a§lWIN! §rYou got " + amt + "§7x §r§b" + resultName + "§r!");
        } else {
            p.sendMessage("§c§lLOSS! §rYou lost " + bet.getAmount() + "§7x §r§b" + resultName + "§r!");
        }

        // results GUI
        for(int i = 0; i < 45; i++){
            res.setItem(i, itemStack(Material.GRAY_STAINED_GLASS_PANE, " "));
        }

        // black pane pattern
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

        result.setItemMeta(resultMeta);
        res.setItem(22, result);

        // play again, play again same bet
        res.setItem(39, itemStack(Material.MUSIC_DISC_CAT, "§a§kM §r§a§l→ PLAY AGAIN ← §r§a§kM"));
        res.setItem(41, itemStack(Material.MUSIC_DISC_CHIRP, "§c§kM §r§c§l→ PLAY AGAIN (SAME BET) ← §r§c§kM"));

        p.openInventory(res);

    }

    // game GUI
    public void InitializeItems(){
        for(int i = 0; i < 54; i++){
            inv.setItem(i, itemStack(Material.GRAY_STAINED_GLASS_PANE, " "));

            if((i - 2) % 9 == 0 || (i + 3) % 9 == 0) inv.setItem(i, itemStack(Material.BLACK_STAINED_GLASS_PANE, " "));

            if(i == 13) inv.setItem(13, itemStack(Material.DIAMOND_SWORD, "§r§a§lHIT"));
            if(i == 40) inv.setItem(40, itemStack(Material.SHIELD, "§r§b§lSTAND"));
        }
        for(int i = 9; i < 45; i++){
            if(i % 9 == 0 || (i - 1) % 9 == 0 || (i + 1) % 9 == 0 || (i + 2) % 9 == 0) inv.setItem(i, new ItemStack(Material.AIR));
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
