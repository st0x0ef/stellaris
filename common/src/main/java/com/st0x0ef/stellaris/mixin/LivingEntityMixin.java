package com.st0x0ef.stellaris.mixin;

import com.st0x0ef.stellaris.common.oxygen.DimensionOxygenManager;
import com.st0x0ef.stellaris.common.oxygen.GlobalOxygenManager;
import com.st0x0ef.stellaris.common.registry.DamageSourceRegistry;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Unique
    LivingEntity stellaris$livingEntity = (LivingEntity) ((Object) this);

    @Unique
    private long stellaris$tickSinceLastOxygenCheck;

    @Unique
    private DimensionOxygenManager stellaris$oxygenManager;

    @Inject(at = @At("HEAD"), method = "tick()V")
    private void tick(CallbackInfo ci){
        Utils.handleGravityChange(stellaris$livingEntity);

        if (!stellaris$livingEntity.level().isClientSide()) {
            if (stellaris$tickSinceLastOxygenCheck > 20) {
                if (stellaris$oxygenManager == null) {
                    stellaris$oxygenManager = GlobalOxygenManager.getInstance().getOrCreateDimensionManager((ServerLevel) stellaris$livingEntity.level());
                }

                if(!stellaris$oxygenManager.getLevel().dimension().equals(stellaris$livingEntity.level().dimension())) {
                    stellaris$oxygenManager = GlobalOxygenManager.getInstance().getOrCreateDimensionManager((ServerLevel) stellaris$livingEntity.level());
                }

                if (!stellaris$oxygenManager.breath(stellaris$livingEntity)) {
                    stellaris$livingEntity.hurt(DamageSourceRegistry.of(stellaris$livingEntity.level(), DamageSourceRegistry.OXYGEN), 2f);
                }

                stellaris$tickSinceLastOxygenCheck = 0;
            }

            stellaris$tickSinceLastOxygenCheck++;
        }
    }
}
