package com.st0x0ef.stellaris.common.utils;

import com.st0x0ef.stellaris.common.entities.vehicles.LanderEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;

public class TeleportUtil {
    public static void teleportToPlanet(Entity entity, ServerLevel level, Vec3 coords) {
        Vec3 landerOffset = Vec3.ZERO;
        if (entity instanceof LanderEntity) {
            landerOffset = entity.getBoundingBox().getCenter().subtract(entity.getX(), entity.getY(), entity.getZ());
        }

        entity.changeDimension(new DimensionTransition(level, entity, arg -> {}));
        entity.setPos(coords.x + landerOffset.x, coords.y + landerOffset.y, coords.z + landerOffset.z);
    }
}
