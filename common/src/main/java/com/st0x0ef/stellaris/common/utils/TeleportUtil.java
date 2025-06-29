package com.st0x0ef.stellaris.common.utils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;

public class TeleportUtil {
    public static void teleportToPlanet(Entity entity, ServerLevel level, Vec3 coords) {
        entity.changeDimension(new DimensionTransition(level, entity, arg -> {}));
        entity.setPos(coords.x, coords.y, coords.z);
    }
}
