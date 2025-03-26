package com.battlebox.team;

import lombok.Builder;
import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class Team {
    private final String name;
    private final ChatColor color;
    private final String prefix;
    @Builder.Default
    private final Set<UUID> players = new HashSet<>();
    private int score;
    
    public void addPlayer(Player player) {
        this.players.add(player.getUniqueId());
    }
    
    public void removePlayer(Player player) {
        this.players.remove(player.getUniqueId());
    }
    
    public boolean hasPlayer(Player player) {
        return this.players.contains(player.getUniqueId());
    }
    
    public int getPlayerCount() {
        return this.players.size();
    }
}