package com.battlebox.game;

import com.battlebox.BattleBoxPlugin;
import com.battlebox.arena.Arena;
import com.battlebox.team.Team;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class GameManager {
    
    private final BattleBoxPlugin plugin;
    @Getter
    private final Map<String, GameSession> games;
    
    public GameManager(BattleBoxPlugin plugin) {
        this.plugin = plugin;
        this.games = new HashMap<>();
    }
    
    public Optional<GameSession> createGame(Arena arena, Team teamRed, Team teamBlue) {
        if (this.games.containsKey(arena.getName())) {
            return Optional.empty();
        }
        
        GameSession game = new GameSession(arena, teamRed, teamBlue);
        this.games.put(arena.getName(), game);
        return Optional.of(game);
    }
    
    public Optional<GameSession> getGame(String arenaName) {
        return Optional.ofNullable(this.games.get(arenaName));
    }
    
    public Optional<GameSession> getPlayerGame(Player player) {
        return this.games.values().stream()
                .filter(game -> game.hasPlayer(player))
                .findFirst();
    }
    
    public void startGame(GameSession game) {
        game.setState(GameState.STARTING);
        
        new BukkitRunnable() {
            private int countdown = plugin.getConfig().getInt("game.countdown-time", 30);
            
            @Override
            public void run() {
                if (countdown <= 0) {
                    game.setState(GameState.ACTIVE);
                    startRound(game);
                    this.cancel();
                    return;
                }
                
                if (countdown <= 10 || countdown % 10 == 0) {
                    game.broadcast("&eGame starting in " + countdown + " seconds!");
                }
                
                countdown--;
            }
        }.runTaskTimer(this.plugin, 0L, 20L);
    }
    
    private void startRound(GameSession game) {
        game.resetRound();
        
        new BukkitRunnable() {
            private int timeLeft = plugin.getConfig().getInt("game.round-time", 180);
            
            @Override
            public void run() {
                if (game.getState() != GameState.ACTIVE) {
                    this.cancel();
                    return;
                }
                
                if (timeLeft <= 0) {
                    endRound(game);
                    this.cancel();
                    return;
                }
                
                if (timeLeft <= 10 || timeLeft % 30 == 0) {
                    game.broadcast("&eTime remaining: " + timeLeft + " seconds");
                }
                
                timeLeft--;
            }
        }.runTaskTimer(this.plugin, 0L, 20L);
    }
    
    private void endRound(GameSession game) {
        game.setState(GameState.ENDING);
        // Calculate scores and determine winner
        // TODO: Implement scoring system
        game.broadcast("&eRound ended!");
    }
}