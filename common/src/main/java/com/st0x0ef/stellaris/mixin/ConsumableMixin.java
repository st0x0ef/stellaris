package com.st0x0ef.stellaris.mixin;

import com.st0x0ef.stellaris.common.registry.TagRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Consumable.class)
public class ConsumableMixin {
    @Inject(at = @At(value = "HEAD"), method = "canConsume", cancellable = true)
    private void cancelEat(LivingEntity livingEntity, ItemStack food, CallbackInfoReturnable<Boolean> cir) {
        if(livingEntity.level() instanceof ServerLevel serverLevel && !PlanetUtil.hasOxygenAt(serverLevel, livingEntity.getOnPos()) && !food.is(TagRegistry.SPACE_FOOD)) {
            cir.setReturnValue(false);
        }
    }
}
