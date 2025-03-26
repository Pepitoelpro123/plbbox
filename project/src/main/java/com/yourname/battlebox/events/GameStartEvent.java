package com.yourname.battlebox.events;

import com.yourname.battlebox.game.GameSession;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public final class GameStartEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final GameSession gameSession;

    public GameStartEvent(final GameSession gameSession) {
        this.gameSession = gameSession;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}