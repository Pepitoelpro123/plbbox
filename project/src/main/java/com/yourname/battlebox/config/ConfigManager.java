package com.yourname.battlebox.config;

import com.yourname.battlebox.BattleBox;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public final class ConfigManager {

    private final BattleBox plugin;
    private final FileConfiguration config;

    public ConfigManager(final BattleBox plugin) {
        this.plugin = plugin;
        this.plugin.saveDefaultConfig();
        this.config = this.plugin.getConfig();
    }

    public void reloadConfig() {
        this.plugin.reloadConfig();
    }

    public int getRoundDuration() {
        return this.config.getInt("settings.round-duration", 180);
    }

    public int getMinPlayersPerTeam() {
        return this.config.getInt("settings.min-players-per-team", 1);
    }

    public int getMaxPlayersPerTeam() {
        return this.config.getInt("settings.max-players-per-team", 4);
    }

    public int getCountdownDuration() {
        return this.config.getInt("settings.countdown-duration", 30);
    }

    public int getKillPoints() {
        return this.config.getInt("settings.points.kill", 50);
    }

    public int getWinPoints() {
        return this.config.getInt("settings.points.win", 100);
    }

    public int getParticipationPoints() {
        return this.config.getInt("settings.points.participation", 10);
    }
}