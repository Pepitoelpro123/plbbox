package com.yourname.battlebox.game;

import com.yourname.battlebox.BattleBox;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class GameManager {
    
    @Getter
    private static GameManager instance;
    private final BattleBox plugin;
    
    @Getter
    private GameState gameState;
    
    @Getter
    private int roundTime; // Time in seconds
    private static final int ROUND_DURATION = 180; // 3 minutes in seconds
    
    private Map<UUID, Team> playerTeams;
    private Map<Team, Integer> teamWoolPlaced;
    private Location woolCheckLocation; // Center location to check wool placement
    
    public GameManager(BattleBox plugin) {
        instance = this;
        this.plugin = plugin;
        this.gameState = GameState.WAITING;
        this.roundTime = ROUND_DURATION;
        this.playerTeams = new HashMap<>();
        this.teamWoolPlaced = new HashMap<>();
    }
    
    public void startRound() {
        if (gameState != GameState.WAITING) {
            return;
        }
        
        gameState = GameState.IN_GAME;
        roundTime = ROUND_DURATION;
        
        // Reset wool placement counters
        teamWoolPlaced.clear();
        for (Team team : Team.values()) {
            teamWoolPlaced.put(team, 0);
        }
        
        // Reset points for new round
        plugin.getPointSystem().resetPoints();
        
        new BukkitRunnable() {
            @Override
            public void run() {
                if (gameState != GameState.IN_GAME) {
                    this.cancel();
                    return;
                }
                
                roundTime--;
                checkWinConditions();
                
                if (roundTime <= 0) {
                    endRound();
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Run every second
    }
    
    public void checkWinConditions() {
        // Check wool completion
        for (Team team : Team.values()) {
            if (hasCompletedWoolPattern(team)) {
                declareWinner(team, WinCondition.WOOL_COMPLETION);
                return;
            }
        }
        
        // Check team elimination
        for (Team team : Team.values()) {
            if (isTeamEliminated(team)) {
                Team winningTeam = getOpposingTeam(team);
                declareWinner(winningTeam, WinCondition.TEAM_ELIMINATION);
                return;
            }
        }
    }
    
    private boolean hasCompletedWoolPattern(Team team) {
        // Get required wool count from config or set default
        int requiredWool = 5; // This should be configurable
        return teamWoolPlaced.getOrDefault(team, 0) >= requiredWool;
    }
    
    private boolean isTeamEliminated(Team team) {
        return getTeamAlivePlayers(team).isEmpty();
    }
    
    private List<Player> getTeamAlivePlayers(Team team) {
        return playerTeams.entrySet().stream()
            .filter(entry -> entry.getValue() == team)
            .map(entry -> plugin.getServer().getPlayer(entry.getKey()))
            .filter(player -> player != null && player.isOnline() && !player.isDead())
            .toList();
    }
    
    public Set<UUID> getTeamPlayers(Team team) {
        return playerTeams.entrySet().stream()
            .filter(entry -> entry.getValue() == team)
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
    }
    
    private Team getOpposingTeam(Team team) {
        return team == Team.RED ? Team.BLUE : Team.RED;
    }
    
    public void handleWoolPlacement(Player player, Location location) {
        if (gameState != GameState.IN_GAME) return;
        
        Team team = playerTeams.get(player.getUniqueId());
        if (team == null) return;
        
        // Check if wool was placed in valid location
        if (isValidWoolLocation(location)) {
            teamWoolPlaced.merge(team, 1, Integer::sum);
            checkWinConditions();
        }
    }
    
    private boolean isValidWoolLocation(Location location) {
        // This should check if the location is within the designated wool placement area
        // For now, we'll just check if it's near the woolCheckLocation
        return woolCheckLocation != null && 
               location.getWorld() == woolCheckLocation.getWorld() && 
               location.distance(woolCheckLocation) <= 5;
    }
    
    private void declareWinner(Team team, WinCondition winCondition) {
        if (gameState != GameState.IN_GAME) return;
        
        // Award points to winning team
        plugin.getPointSystem().awardWinPoints(team, winCondition);
        
        // Announce winner
        plugin.getServer().broadcastMessage(
            team.getDisplayName() + " wins by " + winCondition.getDescription() + "!"
        );
        
        endRound();
    }
    
    public void endRound() {
        gameState = GameState.ENDING;
        // Cleanup and prepare for next round
        resetGame();
    }
    
    public void resetGame() {
        gameState = GameState.WAITING;
        roundTime = ROUND_DURATION;
        playerTeams.clear();
        teamWoolPlaced.clear();
    }
    
    public void setWoolCheckLocation(Location location) {
        this.woolCheckLocation = location;
    }
    
    public void registerPlayer(Player player, Team team) {
        playerTeams.put(player.getUniqueId(), team);
    }
    
    public void unregisterPlayer(Player player) {
        playerTeams.remove(player.getUniqueId());
    }
    
    public Team getPlayerTeam(UUID playerId) {
        return playerTeams.get(playerId);
    }
}