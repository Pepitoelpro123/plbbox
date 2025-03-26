package com.yourname.battlebox.arena;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Builder
public final class Arena {

    private final String name;
    private final UUID id;
    private final World world;
    private final Location center;
    private final Map<String, Location> teamSpawns;
    private final Location spectatorSpawn;
    private final int buildHeight;
    private final BoundingBox bounds;
    private final Material centerBlockType;
    private final int centerSize;
    private boolean enabled;
    @Builder.Default
    private ArenaState state = ArenaState.SETUP;

    public Arena(final String name, final UUID id, final World world, final Location center,
                final Map<String, Location> teamSpawns, final Location spectatorSpawn,
                final int buildHeight, final boolean enabled, final BoundingBox bounds,
                final Material centerBlockType, final int centerSize, final ArenaState state) {
        this.name = name;
        this.id = id;
        this.world = world;
        this.center = center;
        this.teamSpawns = new HashMap<>(teamSpawns);
        this.spectatorSpawn = spectatorSpawn;
        this.buildHeight = buildHeight;
        this.enabled = enabled;
        this.bounds = bounds;
        this.centerBlockType = centerBlockType;
        this.centerSize = centerSize;
        this.state = state != null ? state : ArenaState.SETUP;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public void setState(final ArenaState state) {
        this.state = state;
    }

    public boolean isSetup() {
        return this.center != null && 
               !this.teamSpawns.isEmpty() && 
               this.spectatorSpawn != null && 
               this.bounds != null;
    }

    public boolean isInArena(final Location location) {
        return this.bounds.contains(location.toVector());
    }

    public boolean isInCenterRegion(final Location location) {
        if (!isInArena(location)) {
            return false;
        }

        int radius = this.centerSize / 2;
        return Math.abs(location.getBlockX() - this.center.getBlockX()) <= radius &&
               Math.abs(location.getBlockZ() - this.center.getBlockZ()) <= radius &&
               location.getBlockY() >= this.center.getBlockY() &&
               location.getBlockY() <= this.center.getBlockY() + this.buildHeight;
    }
}