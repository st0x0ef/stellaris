package com.st0x0ef.stellaris.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public class TeleportUtil {
    @ExpectPlatform
    public static void teleportToPlanet(Entity entity, ServerLevel level, int yPos) {

        throw new AssertionError();
    }
}
