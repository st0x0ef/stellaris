package com.st0x0ef.stellaris.common.utils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.DimensionTransition;

public class TeleportUtil {
    public static void teleportToPlanet(Entity entity, ServerLevel level, int yPos) {
        entity.changeDimension(new DimensionTransition(level, entity, arg -> {}));
    }
}
