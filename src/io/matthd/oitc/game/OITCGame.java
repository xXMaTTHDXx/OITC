package io.matthd.oitc.game;

import io.matthd.cytosis.chat.ChatUtils;
import io.matthd.cytosis.game.Game;
import io.matthd.cytosis.game.GameSettings;
import io.matthd.cytosis.game.GameUtil;
import io.matthd.cytosis.team.Team;
import io.matthd.oitc.OITC;
import net.minecraft.server.v1_9_R1.ChatTypeAdapterFactory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import sun.misc.MessageUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Matt on 2016-03-13.
 */
public class OITCGame extends Game {

    private Map<UUID, Integer> playerKills = new HashMap<>();

    public OITCGame(String name, int minSize, int maxSize, GameSettings settings, List<Team> teams) {
        super(name, minSize, maxSize, settings, teams);
    }

    private int getKills(UUID uuid){
        return playerKills.get(uuid);
    }

    @Override
    public boolean shouldContinue() {
        return this.getTeam("Players").getPlayers().size() >= 2;
    }

    @Override
    public void onLobbyJoin(Player player) {
        player.teleport(getLobby());
    }

    @Override
    public Location getLobby() {
        return GameUtil.getLobby();
    }

    @Override
    public void onLobbyQuit(Player player) {
        if(!shouldContinue()){
            stop(true);
        }
    }

    @Override
    public void onJoin(Player player) {

    }

    @Override
    public void onLobby(Player player) {
        player.teleport(getLobby());
    }

    @Override
    public void onQuit(Player player) {
        if(!shouldContinue()){
            stop(true);
        }
    }

    @Override
    public void onDeath(Player player, PlayerDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        playerKills.put(killer.getUniqueId(), getKills(killer.getUniqueId()) + 1);
        ChatUtils.messageToAll(this, player.getName() + " was killed by: " + killer.getName() + "!");

        e.getDrops().clear();

        player.spigot().respawn();
        player.getInventory().addItem(new ItemStack(Material.BOW));
        player.getInventory().addItem(new ItemStack(Material.ARROW));

        if(getKills(killer.getUniqueId()) >= 20){
            ChatUtils.messageToAll(this, killer.getName() + " has won! Sending to lobby in 10 seconds.");
            new BukkitRunnable() {
                public void run(){
                    stop(false);
                }
            }.runTaskLater(OITC.getInstance(), 20*10L);
        }
    }

    @Override
    public void onFinish(boolean b) {
        //TODO You would give coins or whatever junk here
        lobby();
    }

    @Override
    public int getGameMessageInterval() {
        return 10;
    }
}
