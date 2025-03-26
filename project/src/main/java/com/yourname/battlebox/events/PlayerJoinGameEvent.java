package com.yourname.battlebox.events;

import com.yourname.battlebox.game.GameSession;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public final class PlayerJoinGameEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final GameSession gameSession;
    private final Player player;

    public PlayerJoinGameEvent(final GameSession gameSession, final Player player) {
        this.gameSession = gameSession;
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}