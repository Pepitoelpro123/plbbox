package com.yourname.battlebox.arena;

import com.yourname.battlebox.BattleBox;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ArenaManager {
    private final BattleBox plugin;
    @Getter
    private final Map<String, Arena> arenas;
    private final File arenasFile;
    private FileConfiguration arenasConfig;

    public ArenaManager(BattleBox plugin) {
        this.plugin = plugin;
        this.arenas = new HashMap<>();
        this.arenasFile = new File(plugin.getDataFolder(), "arenas.yml");
        this.loadArenas();
    }

    public Optional<Arena> createArena(String name, World world) {
        if (arenas.values().stream().anyMatch(arena -> arena.getName().equalsIgnoreCase(name))) {
            return Optional.empty();
        }

        Arena arena = new Arena(name, world);
        arenas.put(arena.getId(), arena);
        saveArenas();
        return Optional.of(arena);
    }

    public boolean deleteArena(String id) {
        Arena arena = arenas.get(id);
        if (arena != null && arena.getState() != ArenaState.IN_GAME) {
            arenas.remove(id);
            saveArenas();
            return true;
        }
        return false;
    }

    public Optional<Arena> getArena(String id) {
        return Optional.ofNullable(arenas.get(id));
    }

    public Optional<Arena> getArenaByName(String name) {
        return arenas.values().stream()
            .filter(arena -> arena.getName().equalsIgnoreCase(name))
            .findFirst();
    }

    public Set<Arena> getAvailableArenas() {
        return arenas.values().stream()
            .filter(arena -> arena.getState() == ArenaState.WAITING)
            .collect(Collectors.toSet());
    }

    public boolean setArenaCorners(String id, Location cornerA, Location cornerB) {
        Optional<Arena> optionalArena = getArena(id);
        if (!optionalArena.isPresent()) return false;

        Arena arena = optionalArena.get();
        if (cornerA.getWorld() != cornerB.getWorld() || 
            cornerA.getWorld() != arena.getWorld()) {
            return false;
        }

        arena.setCornerA(cornerA);
        arena.setCornerB(cornerB);
        saveArenas();
        return true;
    }

    public void loadArenas() {
        if (!arenasFile.exists()) {
            plugin.saveResource("arenas.yml", false);
        }

        arenasConfig = YamlConfiguration.loadConfiguration(arenasFile);
        
        if (arenasConfig.contains("arenas")) {
            arenasConfig.getConfigurationSection("arenas").getKeys(false).forEach(key -> {
                Arena arena = (Arena) arenasConfig.get("arenas." + key);
                if (arena != null) {
                    arenas.put(arena.getId(), arena);
                }
            });
        }
    }

    public void saveArenas() {
        arenasConfig = new YamlConfiguration();
        arenas.forEach((id, arena) -> arenasConfig.set("arenas." + id, arena));
        
        try {
            arenasConfig.save(arenasFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save arenas to " + arenasFile);
            e.printStackTrace();
        }
    }

    public void resetArena(String id) {
        getArena(id).ifPresent(Arena::reset);
    }
}