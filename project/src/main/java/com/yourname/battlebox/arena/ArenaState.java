package com.yourname.battlebox.arena;

public enum ArenaState {
    SETUP,      // Arena is being set up
    WAITING,    // Waiting for players
    STARTING,   // Countdown to start
    IN_GAME,    // Game is in progress
    ENDING      // Game has ended
}