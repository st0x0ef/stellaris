package com.st0x0ef.stellaris.platform;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.msrandom.multiplatform.annotations.Actual;
import net.neoforged.neoforge.common.util.ITeleporter;

import java.util.function.Function;

public class TeleportUtilActual {

    @Actual
    public static void teleportToPlanet(Entity entity, ServerLevel level, int yPos) {
        Vec3 newPos = new Vec3(entity.getX(), yPos, entity.getZ());
        PortalInfo portalInfo = new PortalInfo(newPos, Vec3.ZERO, entity.yRotO, entity.xRotO);

        entity.changeDimension(level, new ITeleporter() {
            @Override
            public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
                Vec3 pos = new Vec3(entity.position().x, yPos, entity.position().z);

                return new PortalInfo(pos, Vec3.ZERO, entity.getYRot(), entity.getXRot());
            }

            @Override
            public boolean isVanilla() {
                return false;
            }

            @Override
            public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
                return false;
            }
        });
    }


}
