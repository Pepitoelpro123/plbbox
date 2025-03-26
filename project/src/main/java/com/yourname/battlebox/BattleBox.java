package com.yourname.battlebox;

import com.yourname.battlebox.game.GameManager;
import com.yourname.battlebox.game.PointSystem;
import com.yourname.battlebox.listeners.GameListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class BattleBox extends JavaPlugin {
    
    @Getter
    private static BattleBox instance;
    
    @Getter
    private GameManager gameManager;
    
    @Getter
    private PointSystem pointSystem;

    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize systems
        this.gameManager = new GameManager(this);
        this.pointSystem = new PointSystem();
        
        // Register listeners
        new GameListener(this);
        
        getLogger().info("BattleBox plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("BattleBox plugin has been disabled!");
    }
}