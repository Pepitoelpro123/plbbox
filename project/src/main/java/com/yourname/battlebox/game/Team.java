package com.yourname.battlebox.game;

import org.bukkit.ChatColor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Team {
    RED(ChatColor.RED + "Red Team"),
    BLUE(ChatColor.BLUE + "Blue Team");
    
    private final String displayName;
}