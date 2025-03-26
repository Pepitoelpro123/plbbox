package com.yourname.battlebox;

import com.yourname.battlebox.arena.ArenaManager;
import com.yourname.battlebox.config.ConfigManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class BattleBox extends JavaPlugin {
    
    @Getter
    private static BattleBox instance;
    
    @Getter
    private ConfigManager configManager;
    
    @Getter
    private ArenaManager arenaManager;

    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize managers
        this.configManager = new ConfigManager(this);
        this.arenaManager = new ArenaManager(this);
        
        // Load configuration
        this.saveDefaultConfig();
        
        getLogger().info("BattleBox plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        // Save all arena data
        if (this.arenaManager != null) {
            this.arenaManager.saveArenas();
        }
        
        getLogger().info("BattleBox plugin has been disabled!");
    }
}