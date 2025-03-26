package com.yourname.battlebox.team;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public final class Team {

    private final String name;
    private final ChatColor color;
    private final DyeColor woolColor;
    private final Set<UUID> players;
    private int points;

    public Team(final String name, final ChatColor color, final DyeColor woolColor) {
        this.name = name;
        this.color = color;
        this.woolColor = woolColor;
        this.players = new HashSet<>();
        this.points = 0;
    }

    public void addPlayer(final Player player) {
        this.players.add(player.getUniqueId());
    }

    public void removePlayer(final Player player) {
        this.players.remove(player.getUniqueId());
    }

    public void addPoints(final int points) {
        this.points += points;
    }

    public boolean isFull(final int maxPlayers) {
        return this.players.size() >= maxPlayers;
    }

    public boolean isEmpty() {
        return this.players.isEmpty();
    }
}