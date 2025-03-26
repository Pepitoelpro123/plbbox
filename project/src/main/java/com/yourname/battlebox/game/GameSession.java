package com.yourname.battlebox.game;

import com.yourname.battlebox.arena.Arena;
import com.yourname.battlebox.team.Team;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public final class GameSession {

    private final UUID gameId;
    private final Arena arena;
    private final Set<Team> teams;
    private final Set<UUID> players;
    @Setter
    private GameState state;
    private int roundTime;

    public GameSession(final Arena arena) {
        this.gameId = UUID.randomUUID();
        this.arena = arena;
        this.teams = new HashSet<>();
        this.players = new HashSet<>();
        this.state = GameState.WAITING;
        this.roundTime = 0;
    }

    public void addPlayer(final Player player) {
        this.players.add(player.getUniqueId());
    }

    public void removePlayer(final Player player) {
        this.players.remove(player.getUniqueId());
    }

    public void start() {
        this.state = GameState.IN_GAME;
        // Implementation coming in game mechanics phase
    }

    public void end() {
        this.state = GameState.ENDING;
        // Implementation coming in game mechanics phase
    }
}