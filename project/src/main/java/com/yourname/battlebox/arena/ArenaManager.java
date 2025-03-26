package com.yourname.battlebox.arena;

import com.yourname.battlebox.BattleBox;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.BoundingBox;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public final class ArenaManager {

    private final BattleBox plugin;
    private final Map<UUID, Arena> arenas;

    public ArenaManager(final BattleBox plugin) {
        this.plugin = plugin;
        this.arenas = new HashMap<>();
    }

    public Optional<Arena> getArena(final UUID arenaId) {
        return Optional.ofNullable(this.arenas.get(arenaId));
    }

    public Collection<Arena> getAvailableArenas() {
        return this.arenas.values().stream()
                .filter(arena -> arena.isSetup() && 
                               arena.isEnabled() && 
                               arena.getState() == ArenaState.READY)
                .collect(Collectors.toList());
    }

    public void createArena(final String name, final Location center, final Location corner1, final Location corner2) {
        if (!center.getWorld().equals(corner1.getWorld()) || !center.getWorld().equals(corner2.getWorld())) {
            throw new IllegalArgumentException("All locations must be in the same world");
        }

        final Arena arena = Arena.builder()
                .name(name)
                .id(UUID.randomUUID())
                .world(center.getWorld())
                .center(center)
                .teamSpawns(new HashMap<>())
                .buildHeight(this.plugin.getConfigManager().getConfig().getInt("arena.build-height", 5))
                .centerBlockType(Material.valueOf(this.plugin.getConfigManager().getConfig().getString("arena.default-block", "WOOL")))
                .centerSize(this.plugin.getConfigManager().getConfig().getInt("arena.center-size", 3))
                .bounds(new BoundingBox(
                    Math.min(corner1.getX(), corner2.getX()),
                    Math.min(corner1.getY(), corner2.getY()),
                    Math.min(corner1.getZ(), corner2.getZ()),
                    Math.max(corner1.getX(), corner2.getX()),
                    Math.max(corner1.getY(), corner2.getY()),
                    Math.max(corner1.getZ(), corner2.getZ())
                ))
                .enabled(false)
                .state(ArenaState.SETUP)
                .build();

        this.arenas.put(arena.getId(), arena);
    }

    public void deleteArena(final UUID arenaId) {
        final Arena arena = this.arenas.get(arenaId);
        if (arena != null && arena.getState() != ArenaState.IN_USE) {
            this.arenas.remove(arenaId);
        }
    }

    public void setArenaState(final UUID arenaId, final ArenaState state) {
        this.getArena(arenaId).ifPresent(arena -> arena.setState(state));
    }

    public boolean setTeamSpawn(final UUID arenaId, final String teamName, final Location location) {
        final Optional<Arena> arenaOpt = this.getArena(arenaId);
        if (arenaOpt.isPresent() && arenaOpt.get().isInArena(location)) {
            Arena arena = arenaOpt.get();
            arena.getTeamSpawns().put(teamName, location);
            return true;
        }
        return false;
    }

    public boolean setSpectatorSpawn(final UUID arenaId, final Location location) {
        return this.getArena(arenaId)
                .filter(arena -> arena.isInArena(location))
                .map(arena -> {
                    try {
                        Arena.class.getDeclaredField("spectatorSpawn").set(arena, location);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .orElse(false);
    }
}