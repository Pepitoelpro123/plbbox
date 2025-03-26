package com.yourname.battlebox.team;

import com.yourname.battlebox.BattleBox;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Getter
public final class TeamManager {

    private final BattleBox plugin;
    private final Map<UUID, Team> teams;

    public TeamManager(final BattleBox plugin) {
        this.plugin = plugin;
        this.teams = new HashMap<>();
        this.loadDefaultTeams();
    }

    private void loadDefaultTeams() {
        this.createTeam("Red Team", ChatColor.RED, DyeColor.RED);
        this.createTeam("Blue Team", ChatColor.BLUE, DyeColor.BLUE);
    }

    public void createTeam(final String name, final ChatColor color, final DyeColor woolColor) {
        final Team team = new Team(name, color, woolColor);
        this.teams.put(UUID.randomUUID(), team);
    }

    public Optional<Team> getPlayerTeam(final Player player) {
        return this.teams.values().stream()
                .filter(team -> team.getPlayers().contains(player.getUniqueId()))
                .findFirst();
    }

    public void joinTeam(final Player player, final Team team) {
        this.getPlayerTeam(player).ifPresent(currentTeam -> currentTeam.removePlayer(player));
        team.addPlayer(player);
    }

    public void leaveTeam(final Player player) {
        this.getPlayerTeam(player).ifPresent(team -> team.removePlayer(player));
    }
}