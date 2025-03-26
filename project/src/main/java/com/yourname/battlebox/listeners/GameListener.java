package com.yourname.battlebox.listeners;

import com.yourname.battlebox.BattleBox;
import com.yourname.battlebox.game.GameManager;
import com.yourname.battlebox.game.GameState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class GameListener implements Listener {
    
    private final BattleBox plugin;
    private final GameManager gameManager;
    
    public GameListener(BattleBox plugin) {
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (gameManager.getGameState() != GameState.IN_GAME) return;
        
        Player player = event.getPlayer();
        Material blockType = event.getBlock().getType();
        
        // Check if the placed block is wool
        if (blockType == Material.WHITE_WOOL || 
            blockType == Material.RED_WOOL || 
            blockType == Material.BLUE_WOOL) {
            gameManager.handleWoolPlacement(player, event.getBlock().getLocation());
        }
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (gameManager.getGameState() != GameState.IN_GAME) return;
        
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        
        // Award points to killer if it was a player kill
        if (killer != null) {
            plugin.getPointSystem().awardKillPoints(killer);
        }
        
        // Check win conditions after death
        gameManager.checkWinConditions();
    }
}