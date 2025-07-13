package com.st0x0ef.stellaris.mixin;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityFallMixin {

    @Inject( method = "tick", at = @At("HEAD"))
    public void entityFalling(CallbackInfo ci) {
        Entity entity = (Entity)(Object)this;
        Level level = entity.level();
        Planet orbit = PlanetUtil.getPlanet(level.dimension().location());

        if(orbit != null && orbit.mainPlanet().isPresent()) {
            Planet mainPlanet = PlanetUtil.getPlanet(orbit.mainPlanet().get());
            if(mainPlanet != null && entity.getY() <= Stellaris.CONFIG.orbitTeleportationYCoord) {
                Vec3 coordinates = new Vec3(entity.getX(), Stellaris.CONFIG.rocketTpHeight, entity.getZ());

                Utils.changeDimensionWithVehicle(entity, mainPlanet, coordinates);
            }
        }
    }
}
