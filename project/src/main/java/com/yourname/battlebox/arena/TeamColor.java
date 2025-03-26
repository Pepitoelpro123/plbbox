package com.yourname.battlebox.arena;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

@Getter
public enum TeamColor {
    RED(ChatColor.RED, DyeColor.RED),
    BLUE(ChatColor.BLUE, DyeColor.BLUE),
    GREEN(ChatColor.GREEN, DyeColor.GREEN),
    YELLOW(ChatColor.YELLOW, DyeColor.YELLOW);

    private final ChatColor chatColor;
    private final DyeColor dyeColor;
    private final byte woolData;

    TeamColor(ChatColor chatColor, DyeColor dyeColor) {
        this.chatColor = chatColor;
        this.dyeColor = dyeColor;
        this.woolData = dyeColor.getWoolData();
    }

    public String getFormattedName() {
        return this.chatColor + this.name();
    }
}