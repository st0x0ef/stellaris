package com.st0x0ef.stellaris.platform;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.msrandom.multiplatform.annotations.Actual;

public class TeleportUtilActual {

    @Actual
    public static void teleportToPlanet(Entity entity, ServerLevel level, int yPos) {
        Vec3 newPos = new Vec3(entity.getX(), yPos, entity.getZ());
        PortalInfo portalInfo = new PortalInfo(newPos, Vec3.ZERO, entity.yRotO, entity.xRotO);

        if (level != null) {
            FabricDimensions.teleport(entity, level, portalInfo);
        }
    }
}
