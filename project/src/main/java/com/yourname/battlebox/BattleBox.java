package com.yourname.battlebox;

import com.yourname.battlebox.config.ConfigManager;
import com.yourname.battlebox.game.GameManager;
import com.yourname.battlebox.arena.ArenaManager;
import com.yourname.battlebox.team.TeamManager;
import com.yourname.battlebox.listeners.GameListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class BattleBox extends JavaPlugin {
    
    private static BattleBox instance;
    private ConfigManager configManager;
    private GameManager gameManager;
    private ArenaManager arenaManager;
    private TeamManager teamManager;

    @Override
    public void onEnable() {
        instance = this;
        this.loadManagers();
        this.registerListeners();
        this.getLogger().info("BattleBox has been enabled!");
    }

    @Override
    public void onDisable() {
        if (this.gameManager != null) {
            this.gameManager.shutdown();
        }
        this.getLogger().info("BattleBox has been disabled!");
    }

    public static BattleBox getInstance() {
        return instance;
    }

    private void loadManagers() {
        this.configManager = new ConfigManager(this);
        this.arenaManager = new ArenaManager(this);
        this.teamManager = new TeamManager(this);
        this.gameManager = new GameManager(this);
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new GameListener(this), this);
    }
}