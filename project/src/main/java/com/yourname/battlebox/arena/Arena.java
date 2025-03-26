package com.yourname.battlebox.arena;

import com.yourname.battlebox.BattleBox;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.*;

@Getter
@Setter
@SerializableAs("BattleBoxArena")
public class Arena implements ConfigurationSerializable {
    private final String id;
    private String name;
    private World world;
    private Location centerLocation;
    private Location cornerA;
    private Location cornerB;
    private Location spectatorSpawn;
    private final Map<String, Location> teamSpawns;
    private ArenaState state;
    private int minPlayers;
    private int maxPlayers;
    private int centerSize;
    private Material centerBlockType;
    
    public Arena(String name, World world) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.world = world;
        this.state = ArenaState.SETUP;
        this.teamSpawns = new HashMap<>();
        this.minPlayers = BattleBox.getInstance().getConfig().getInt("game.min-players", 2);
        this.maxPlayers = BattleBox.getInstance().getConfig().getInt("game.max-players", 16);
        this.centerSize = BattleBox.getInstance().getConfig().getInt("arena.center-size", 3);
        this.centerBlockType = Material.valueOf(
            BattleBox.getInstance().getConfig().getString("arena.default-block", "WOOL")
        );
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", this.id);
        data.put("name", this.name);
        data.put("world", this.world.getName());
        data.put("centerLocation", this.centerLocation);
        data.put("cornerA", this.cornerA);
        data.put("cornerB", this.cornerB);
        data.put("spectatorSpawn", this.spectatorSpawn);
        data.put("teamSpawns", this.teamSpawns);
        data.put("minPlayers", this.minPlayers);
        data.put("maxPlayers", this.maxPlayers);
        data.put("centerSize", this.centerSize);
        data.put("centerBlockType", this.centerBlockType.name());
        return data;
    }

    public static Arena deserialize(Map<String, Object> data) {
        World world = BattleBox.getInstance().getServer().getWorld((String) data.get("world"));
        Arena arena = new Arena((String) data.get("name"), world);
        arena.setCenterLocation((Location) data.get("centerLocation"));
        arena.setCornerA((Location) data.get("cornerA"));
        arena.setCornerB((Location) data.get("cornerB"));
        arena.setSpectatorSpawn((Location) data.get("spectatorSpawn"));
        arena.setMinPlayers((Integer) data.get("minPlayers"));
        arena.setMaxPlayers((Integer) data.get("maxPlayers"));
        arena.setCenterSize((Integer) data.get("centerSize"));
        arena.setCenterBlockType(Material.valueOf((String) data.get("centerBlockType")));
        
        @SuppressWarnings("unchecked")
        Map<String, Location> teamSpawns = (Map<String, Location>) data.get("teamSpawns");
        if (teamSpawns != null) {
            arena.getTeamSpawns().putAll(teamSpawns);
        }
        
        return arena;
    }

    public boolean isSetup() {
        return this.centerLocation != null &&
               this.cornerA != null &&
               this.cornerB != null &&
               this.spectatorSpawn != null &&
               !this.teamSpawns.isEmpty();
    }

    public boolean isInArena(Location location) {
        if (location.getWorld() != this.world) return false;
        
        double minX = Math.min(cornerA.getX(), cornerB.getX());
        double minY = Math.min(cornerA.getY(), cornerB.getY());
        double minZ = Math.min(cornerA.getZ(), cornerB.getZ());
        double maxX = Math.max(cornerA.getX(), cornerB.getX());
        double maxY = Math.max(cornerA.getY(), cornerB.getY());
        double maxZ = Math.max(cornerA.getZ(), cornerB.getZ());
        
        return location.getX() >= minX && location.getX() <= maxX &&
               location.getY() >= minY && location.getY() <= maxY &&
               location.getZ() >= minZ && location.getZ() <= maxZ;
    }

    public boolean setTeamSpawn(String teamId, Location location) {
        if (!isInArena(location)) {
            return false;
        }
        this.teamSpawns.put(teamId, location);
        return true;
    }

    public Optional<Location> getTeamSpawn(String teamId) {
        return Optional.ofNullable(this.teamSpawns.get(teamId));
    }

    public boolean removeTeamSpawn(String teamId) {
        return this.teamSpawns.remove(teamId) != null;
    }

    public Set<Block> getCenterBlocks() {
        Set<Block> blocks = new HashSet<>();
        if (this.centerLocation == null) return blocks;

        int radius = (this.centerSize - 1) / 2;
        World world = this.centerLocation.getWorld();
        int centerX = this.centerLocation.getBlockX();
        int centerY = this.centerLocation.getBlockY();
        int centerZ = this.centerLocation.getBlockZ();

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                blocks.add(world.getBlockAt(centerX + x, centerY, centerZ + z));
            }
        }

        return blocks;
    }

    public boolean isCenterComplete(String teamColor) {
        return getCenterBlocks().stream()
            .allMatch(block -> block.getType() == this.centerBlockType && 
                     block.getData() == TeamColor.valueOf(teamColor).getWoolData());
    }

    public void reset() {
        // Reset center blocks to air
        getCenterBlocks().forEach(block -> block.setType(Material.AIR));
        
        // Reset arena state
        this.state = ArenaState.WAITING;
    }
}