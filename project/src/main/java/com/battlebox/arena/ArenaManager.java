package com.battlebox.arena;

import com.battlebox.BattleBoxPlugin;
import com.battlebox.game.GameState;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ArenaManager {
    
    @Getter
    private final Map<String, Arena> arenas;
    private final BattleBoxPlugin plugin;
    
    public ArenaManager(BattleBoxPlugin plugin) {
        this.plugin = plugin;
        this.arenas = new HashMap<>();
        this.loadArenas();
    }
    
    private void loadArenas() {
        ConfigurationSection arenasSection = this.plugin.getConfig().getConfigurationSection("arenas");
        if (arenasSection == null) {
            return;
        }
        
        for (String arenaName : arenasSection.getKeys(false)) {
            ConfigurationSection arenaSection = arenasSection.getConfigurationSection(arenaName);
            if (arenaSection == null) {
                continue;
            }
            
            String worldName = arenaSection.getString("world");
            World world = this.plugin.getServer().getWorld(worldName);
            if (world == null) {
                this.plugin.getLogger().warning("World not found for arena: " + arenaName);
                continue;
            }
            
            ConfigurationSection bounds = arenaSection.getConfigurationSection("bounds");
            ConfigurationSection woolCenter = arenaSection.getConfigurationSection("wool-center");
            ConfigurationSection spawns = arenaSection.getConfigurationSection("spawns");
            
            if (bounds == null || woolCenter == null || spawns == null) {
                this.plugin.getLogger().warning("Invalid configuration for arena: " + arenaName);
                continue;
            }
            
            Arena arena = Arena.builder()
                    .name(arenaSection.getString("name", arenaName))
                    .world(world)
                    .minBound(this.locationFromSection(bounds.getConfigurationSection("min"), world))
                    .maxBound(this.locationFromSection(bounds.getConfigurationSection("max"), world))
                    .woolCenter(this.locationFromSection(woolCenter.getConfigurationSection("location"), world))
                    .woolRadius(woolCenter.getInt("radius", 3))
                    .redSpawn(this.locationFromSection(spawns.getConfigurationSection("red-team"), world))
                    .blueSpawn(this.locationFromSection(spawns.getConfigurationSection("blue-team"), world))
                    .enabled(arenaSection.getBoolean("enabled", true))
                    .state(GameState.WAITING)
                    .build();
            
            this.arenas.put(arenaName, arena);
        }
    }
    
    private Location locationFromSection(ConfigurationSection section, World world) {
        if (section == null) {
            return null;
        }
        
        return new Location(
                world,
                section.getDouble("x"),
                section.getDouble("y"),
                section.getDouble("z"),
                (float) section.getDouble("yaw", 0),
                (float) section.getDouble("pitch", 0)
        );
    }
    
    public Optional<Arena> getArena(String name) {
        return Optional.ofNullable(this.arenas.get(name));
    }
    
    public void saveArena(Arena arena) {
        this.arenas.put(arena.getName(), arena);
        // TODO: Save to configuration
    }
    
    public void deleteArena(String name) {
        this.arenas.remove(name);
        // TODO: Remove from configuration
    }
}