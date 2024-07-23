package com.st0x0ef.stellaris.platform;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.msrandom.multiplatform.annotations.Expect;

public class TeleportUtil {

    // This method should be implemented in the fabric module
    // It should teleport the player to the planet
    // The planet should be selected from the planet selection menu
    @Expect
    public static void teleportToPlanet(Entity entity, ServerLevel level, int yPos);
}
