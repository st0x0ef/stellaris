package com.st0x0ef.stellaris.mixin;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.oxygen.GlobalOxygenManager;
import com.st0x0ef.stellaris.common.registry.DamageSourceRegistry;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {

	@Unique
    private final LivingEntity stellaris$livingEntity = (LivingEntity) (Object) this;

    @Unique
    private long stellaris$tickSinceLastOxygenCheck;

	@Shadow
	public abstract boolean hurt(DamageSource source, float amount);

    @Inject(at = @At("HEAD"), method = "tick()V")
    private void tick(CallbackInfo ci) {
        if (firstTick && level() instanceof ServerLevel serverLevel)
            Utils.handleGravityChange(stellaris$livingEntity, serverLevel);

        if (level() instanceof ServerLevel serverLevel && Stellaris.CONFIG.oxygenConfig.enableOxygenSystem) {
            if (stellaris$tickSinceLastOxygenCheck > Stellaris.CONFIG.oxygenConfig.oxygenCheckInterval) {
                if (stellaris$oxygenManager == null) {
                    stellaris$oxygenManager = GlobalOxygenManager.getInstance().getOrCreateDimensionManager(serverLevel);
                }

                if(!stellaris$oxygenManager.getLevel().dimension().equals(stellaris$livingEntity.level().dimension())) {
                    stellaris$oxygenManager = GlobalOxygenManager.getInstance().getOrCreateDimensionManager(serverLevel);
                }

                if (!stellaris$oxygenManager.breath(stellaris$livingEntity)) {
                    hurt(DamageSourceRegistry.of(level(), DamageSourceRegistry.OXYGEN), Stellaris.CONFIG.oxygenConfig.oxygenDamage);
                }

                stellaris$tickSinceLastOxygenCheck = 0;
            }

            stellaris$tickSinceLastOxygenCheck++;
        }
    }
}
