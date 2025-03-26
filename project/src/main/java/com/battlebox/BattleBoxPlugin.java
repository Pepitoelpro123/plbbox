package com.battlebox;

import com.battlebox.arena.ArenaManager;
import com.battlebox.commands.BattleBoxCommand;
import com.battlebox.game.GameManager;
import com.battlebox.listeners.GameListener;
import com.battlebox.team.TeamManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class BattleBoxPlugin extends JavaPlugin {
    
    @Getter
    private static BattleBoxPlugin instance;
    
    @Getter
    private ArenaManager arenaManager;
    
    @Getter
    private TeamManager teamManager;
    
    @Getter
    private GameManager gameManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Load configuration
        this.saveDefaultConfig();
        
        // Initialize managers
        this.arenaManager = new ArenaManager(this);
        this.teamManager = new TeamManager(this);
        this.gameManager = new GameManager(this);
        
        // Register commands
        this.getCommand("battlebox").setExecutor(new BattleBoxCommand(this));
        
        // Register events
        this.getServer().getPluginManager().registerEvents(new GameListener(this), this);
        
        this.getLogger().info("BattleBox has been enabled!");
    }
    
    @Override
    public void onDisable() {
        // Save data and clean up
        
        this.getLogger().info("BattleBox has been disabled!");
    }
}