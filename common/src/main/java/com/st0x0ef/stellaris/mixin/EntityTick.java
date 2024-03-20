package com.st0x0ef.stellaris.mixin;

import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Entity.class)
public class EntityTick {

    @Inject(at = @At(value = "HEAD"), method = "tick")
    private void tick(CallbackInfo info) {
        Entity entity = (Entity) ((Object) this);

        /** Very Very Very Basic Gravity
         * It's here to test the planets registration with data packs
         * The Real gravity system will be made in 1.20.5
         */
        if(StellarisData.isPlanet(entity.level().dimension())) {
            entity.setNoGravity(StellarisData.getPlanet(entity.level().dimension()).oxygen());
        } else {
            entity.setNoGravity(false);
        }
    }


}
