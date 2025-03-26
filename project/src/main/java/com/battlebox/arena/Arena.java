package com.battlebox.arena;

import com.battlebox.game.GameState;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.World;

@Data
@Builder
public class Arena {
    private final String name;
    private final World world;
    private final Location minBound;
    private final Location maxBound;
    private final Location woolCenter;
    private final int woolRadius;
    private final Location redSpawn;
    private final Location blueSpawn;
    private boolean enabled;
    private GameState state;
}