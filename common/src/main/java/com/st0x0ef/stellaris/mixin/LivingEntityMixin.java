package com.st0x0ef.stellaris.mixin;

import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Unique
    LivingEntity stellaris$livingEntity = (LivingEntity) ((Object) this);

    @Inject(at = @At("HEAD"), method = "tick()V")
    private void tick(CallbackInfo ci){
        ResourceLocation dimension = stellaris$livingEntity.level().dimension().location();

        if (PlanetUtil.isPlanet(dimension)) {
            stellaris$livingEntity.getAttribute(Attributes.GRAVITY).setBaseValue(Utils.MPS2ToMCG(PlanetUtil.getPlanet(dimension).gravity()));
        } else {
            stellaris$livingEntity.getAttribute(Attributes.GRAVITY).setBaseValue(0.08);
        }
    }
}
