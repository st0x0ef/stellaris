package com.st0x0ef.stellaris.mixin;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.oxygen.DimensionOxygenManager;
import com.st0x0ef.stellaris.common.oxygen.GlobalOxygenManager;
import com.st0x0ef.stellaris.common.registry.DamageSourceRegistry;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Unique
    private final LivingEntity stellaris$livingEntity = (LivingEntity) (Object) this;

    @Unique
    private long stellaris$tickSinceLastOxygenCheck;

    @Unique
    private DimensionOxygenManager stellaris$oxygenManager;

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("HEAD"), method = "tick()V")
    private void tick(CallbackInfo ci) {
        if (firstTick)
            Utils.handleGravityChange(stellaris$livingEntity, level());

        if (!stellaris$livingEntity.level().isClientSide()) {

            if (stellaris$tickSinceLastOxygenCheck > 20) {
                if (stellaris$oxygenManager == null) {
                    stellaris$oxygenManager = GlobalOxygenManager.getInstance().getOrCreateDimensionManager((ServerLevel) level());
                }

                if(!stellaris$oxygenManager.getLevel().dimension().equals(stellaris$livingEntity.level().dimension())) {
                    stellaris$oxygenManager = GlobalOxygenManager.getInstance().getOrCreateDimensionManager((ServerLevel) level());
                }

                if (!stellaris$oxygenManager.breath(stellaris$livingEntity)) {
                    hurt(DamageSourceRegistry.of(level(), DamageSourceRegistry.OXYGEN), Stellaris.CONFIG.oxygenDamage);
                }

                stellaris$tickSinceLastOxygenCheck = 0;
            }

            stellaris$tickSinceLastOxygenCheck++;
        }
    }

    @Override
    public @Nullable Entity changeDimension(DimensionTransition transition) {
        stellaris$oxygenManager = GlobalOxygenManager.getInstance().getOrCreateDimensionManager((ServerLevel) level());

        return super.changeDimension(transition);
    }
}
