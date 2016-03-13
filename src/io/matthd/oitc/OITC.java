package io.matthd.oitc;

import io.matthd.cytosis.game.GameUtil;
import io.matthd.cytosis.team.Team;
import io.matthd.oitc.game.OITCGame;
import io.matthd.oitc.game.OITCSettings;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.UUID;

/**
 * Created by Matt on 2016-03-13.
 */
public class OITC extends JavaPlugin {

    private static OITC instance;

    public void onEnable(){
        instance = this;

        Team[] teams = new Team[] {
                new Team("Player") {
                    @Override
                    public Location getSpawn() {
                        return GameUtil.getSpawn(name);
                    }

                    @Override
                    public void setSpawn(Location location) {
                        return;
                    }

                    @Override
                    public void add(UUID uuid) {
                        getPlayers().add(uuid);
                        Player pl = Bukkit.getPlayer(uuid);
                        pl.sendMessage(ChatColor.GREEN + "Welcome to the " + name + " team!");
                    }

                    @Override
                    public void remove(UUID uuid) {
                        getPlayers().remove(uuid);
                    }

                    @Override
                    public void start() {
                        for(Player pl : Bukkit.getOnlinePlayers()){
                            pl.getInventory().addItem(new ItemStack(Material.BOW));
                            pl.getInventory().addItem(new ItemStack(Material.ARROW));
                        }
                    }

                    @Override
                    public void stop() {
                        for(Player pl : Bukkit.getOnlinePlayers()){
                            pl.getInventory().clear();
                        }
                    }
                }
        };

        new OITCGame("OITC", 2, 10, new OITCSettings(), Arrays.asList(teams));
    }

    public void onDisable(){
        instance = null;
    }

    public static OITC getInstance() {
        return instance;
    }
}
