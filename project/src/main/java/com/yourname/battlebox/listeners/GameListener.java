package com.yourname.battlebox.listeners;

import com.yourname.battlebox.BattleBox;
import com.yourname.battlebox.events.GameEndEvent;
import com.yourname.battlebox.events.GameStartEvent;
import com.yourname.battlebox.events.PlayerJoinGameEvent;
import com.yourname.battlebox.events.PlayerLeaveGameEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class GameListener implements Listener {
    
    private final BattleBox plugin;
    
    public GameListener(final BattleBox plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onGameStart(final GameStartEvent event) {
        // Implementation coming in game mechanics phase
    }
    
    @EventHandler
    public void onGameEnd(final GameEndEvent event) {
        // Implementation coming in game mechanics phase
    }
    
    @EventHandler
    public void onPlayerJoinGame(final PlayerJoinGameEvent event) {
        // Implementation coming in game mechanics phase
    }
    
    @EventHandler
    public void onPlayerLeaveGame(final PlayerLeaveGameEvent event) {
        // Implementation coming in game mechanics phase
    }
}