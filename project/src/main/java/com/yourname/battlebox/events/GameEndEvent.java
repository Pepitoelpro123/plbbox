package com.yourname.battlebox.events;

import com.yourname.battlebox.game.GameSession;
import com.yourname.battlebox.team.Team;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Optional;

@Getter
public final class GameEndEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final GameSession gameSession;
    private final Optional<Team> winningTeam;

    public GameEndEvent(final GameSession gameSession, final Team winningTeam) {
        this.gameSession = gameSession;
        this.winningTeam = Optional.ofNullable(winningTeam);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}