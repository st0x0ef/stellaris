package com.st0x0ef.stellaris.mixin;

import com.st0x0ef.stellaris.common.oxygen.EntityOxygen;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Entity.class)
public abstract class EntityTick {
    @Shadow public abstract Level level();

    @Shadow public abstract void tick();

    @Unique
    private long stellaris$lastOxygenCheck;

    @Inject(at = @At(value = "HEAD"), method = "tick")
    private void tick(CallbackInfo info) {
        Entity entity = (Entity) ((Object) this);

        long now = System.currentTimeMillis();
        if((now - stellaris$lastOxygenCheck) > 1000){
            EntityOxygen.tick(entity);
            stellaris$lastOxygenCheck = now;
        }
    }
}
