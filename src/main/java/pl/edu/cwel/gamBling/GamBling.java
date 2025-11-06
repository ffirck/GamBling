package pl.edu.cwel.gamBling;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import pl.edu.cwel.gamBling.commands.Blackjack;
import pl.edu.cwel.gamBling.commands.OpenCase;
import pl.edu.cwel.gamBling.commands.Roulette;
import pl.edu.cwel.gamBling.commands.Slots;
import pl.edu.cwel.gamBling.listeners.BlackjackListener;
import pl.edu.cwel.gamBling.listeners.CaseOpenListener;
import pl.edu.cwel.gamBling.listeners.RoulettePlayListener;
import pl.edu.cwel.gamBling.listeners.SlotsPlayListener;

public final class GamBling extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getCommand("opencase").setExecutor(new OpenCase());
        this.getCommand("roulette").setExecutor(new Roulette());
        this.getCommand("slots").setExecutor(new Slots());
        this.getCommand("blackjack").setExecutor(new Blackjack());
        /*###############################################################*/
        getServer().getPluginManager().registerEvents(new CaseOpenListener(this), this);
        getServer().getPluginManager().registerEvents(new RoulettePlayListener(this), this);
        getServer().getPluginManager().registerEvents(new SlotsPlayListener(this), this);
        getServer().getPluginManager().registerEvents(new BlackjackListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static String getNameFromKey(String translationKey){
        String isolatedName = translationKey
                .replace("_", " ")
                .replace(".", "")
                .replace("minecraft", "")
                .replace("item", "")
                .replace("block", "");

        String[] words = isolatedName.split("\\s");
        StringBuilder name = new StringBuilder();
        for(String word : words){
            name
                    .append(Character.toTitleCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }

        return name.toString().trim();
    }

}
