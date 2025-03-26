package com.battlebox.team;

import com.battlebox.BattleBoxPlugin;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TeamManager {
    
    @Getter
    private final Map<String, Team> teams;
    private final BattleBoxPlugin plugin;
    
    public TeamManager(BattleBoxPlugin plugin) {
        this.plugin = plugin;
        this.teams = new HashMap<>();
        this.loadTeams();
    }
    
    private void loadTeams() {
        ConfigurationSection teamsSection = this.plugin.getConfig().getConfigurationSection("teams");
        if (teamsSection == null) {
            return;
        }
        
        for (String teamName : teamsSection.getKeys(false)) {
            ConfigurationSection teamSection = teamsSection.getConfigurationSection(teamName);
            if (teamSection == null) {
                continue;
            }
            
            Team team = Team.builder()
                    .name(teamSection.getString("name", teamName))
                    .color(ChatColor.valueOf(teamSection.getString("color", "WHITE")))
                    .prefix(teamSection.getString("prefix", "&f"))
                    .build();
            
            this.teams.put(teamName, team);
        }
    }
    
    public Optional<Team> getTeam(String name) {
        return Optional.ofNullable(this.teams.get(name));
    }
    
    public Optional<Team> getPlayerTeam(Player player) {
        return this.teams.values().stream()
                .filter(team -> team.hasPlayer(player))
                .findFirst();
    }
    
    public void joinTeam(Player player, Team team) {
        // Remove player from current team if they're in one
        this.getPlayerTeam(player).ifPresent(currentTeam -> currentTeam.removePlayer(player));
        
        // Add player to new team
        team.addPlayer(player);
    }
    
    public void leaveTeam(Player player) {
        this.getPlayerTeam(player).ifPresent(team -> team.removePlayer(player));
    }
}