package com.yourname.battlebox.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WinCondition {
    WOOL_COMPLETION("Complete the wool pattern in the center"),
    TEAM_ELIMINATION("Eliminate the enemy team");
    
    private final String description;
}