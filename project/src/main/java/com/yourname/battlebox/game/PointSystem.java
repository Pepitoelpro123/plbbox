package com.yourname.battlebox.game;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public class PointSystem {
    private static PointSystem instance;
    
    private int pointsPerKill;
    private int pointsForWinning;
    private int pointsForWoolPlacement;
    
    private final Map<UUID, Integer> playerPoints;
    
    public PointSystem() {
        instance = this;
        this.pointsPerKill = 2;
        this.pointsForWinning = 4;
        this.pointsForWoolPlacement = 1;
        this.playerPoints = new HashMap<>();
    }
    
    public static PointSystem getInstance() {
        if (instance == null) {
            instance = new PointSystem();
        }
        return instance;
    }
    
    public void awardKillPoints(Player killer) {
        UUID killerId = killer.getUniqueId();
        playerPoints.merge(killerId, pointsPerKill, Integer::sum);
    }
    
    public void awardWinPoints(Team team, WinCondition condition) {
        int points = condition == WinCondition.WOOL_COMPLETION ? 
            pointsForWoolPlacement : pointsForWinning;
            
        // Award points to all team members
        GameManager.getInstance().getTeamPlayers(team).forEach(playerId -> 
            playerPoints.merge(playerId, points, Integer::sum)
        );
    }
    
    public int getPlayerPoints(UUID playerId) {
        return playerPoints.getOrDefault(playerId, 0);
    }
    
    public void resetPoints() {
        playerPoints.clear();
    }
}