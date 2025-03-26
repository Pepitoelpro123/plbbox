package com.yourname.battlebox.game;

import com.yourname.battlebox.BattleBox;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public final class GameManager {

    private final BattleBox plugin;
    private final Map<UUID, GameSession> gameSessions;

    public GameManager(final BattleBox plugin) {
        this.plugin = plugin;
        this.gameSessions = new HashMap<>();
    }

    public void createGame(final Player player) {
        // Implementation coming in game mechanics phase
    }

    public void endGame(final UUID gameId) {
        // Implementation coming in game mechanics phase
    }

    public void shutdown() {
        this.gameSessions.values().forEach(GameSession::end);
        this.gameSessions.clear();
    }
}