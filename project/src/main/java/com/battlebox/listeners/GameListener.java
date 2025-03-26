package com.battlebox.listeners;

import com.battlebox.BattleBoxPlugin;
import com.battlebox.game.GameSession;
import com.battlebox.game.GameState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameListener implements Listener {
    
    private final BattleBoxPlugin plugin;
    
    public GameListener(BattleBoxPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        
        this.plugin.getGameManager().getPlayerGame(player).ifPresent(game -> {
            if (game.getState() != GameState.ACTIVE) {
                event.setCancelled(true);
                return;
            }
            
            if (block.getType() != Material.WHITE_WOOL) {
                event.setCancelled(true);
                return;
            }
            
            // TODO: Check if block is in wool center area and handle scoring
        });
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        
        this.plugin.getGameManager().getPlayerGame(player).ifPresent(game -> {
            // Clear drops and set respawn location
            event.getDrops().clear();
            // TODO: Handle respawn location and kit reapplication
        });
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        this.plugin.getTeamManager().leaveTeam(player);
        // TODO: Handle game cleanup if needed
    }
}