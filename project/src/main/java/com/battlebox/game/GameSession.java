package com.battlebox.game;

import com.battlebox.arena.Arena;
import com.battlebox.team.Team;
import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@Data
public class GameSession {
    private final Arena arena;
    private final Team redTeam;
    private final Team blueTeam;
    private GameState state;
    private int redScore;
    private int blueScore;
    
    public GameSession(Arena arena, Team redTeam, Team blueTeam) {
        this.arena = arena;
        this.redTeam = redTeam;
        this.blueTeam = blueTeam;
        this.state = GameState.WAITING;
        this.redScore = 0;
        this.blueScore = 0;
    }
    
    public boolean hasPlayer(Player player) {
        return this.redTeam.hasPlayer(player) || this.blueTeam.hasPlayer(player);
    }
    
    public void broadcast(String message) {
        String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
        this.redTeam.getPlayers().forEach(uuid -> {
            Player player = arena.getWorld().getServer().getPlayer(uuid);
            if (player != null) {
                player.sendMessage(coloredMessage);
            }
        });
        this.blueTeam.getPlayers().forEach(uuid -> {
            Player player = arena.getWorld().getServer().getPlayer(uuid);
            if (player != null) {
                player.sendMessage(coloredMessage);
            }
        });
    }
    
    public void resetRound() {
        // Teleport players to their spawn points
        this.redTeam.getPlayers().forEach(uuid -> {
            Player player = arena.getWorld().getServer().getPlayer(uuid);
            if (player != null) {
                player.teleport(arena.getRedSpawn());
            }
        });
        this.blueTeam.getPlayers().forEach(uuid -> {
            Player player = arena.getWorld().getServer().getPlayer(uuid);
            if (player != null) {
                player.teleport(arena.getBlueSpawn());
            }
        });
        
        // Reset wool center
        // TODO: Implement wool center reset
    }
}