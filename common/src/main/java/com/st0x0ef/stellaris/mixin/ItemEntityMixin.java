package com.st0x0ef.stellaris.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Unique
    ItemEntity stellaris$itemEntity = (ItemEntity) ((Object) this);

    @ModifyReturnValue(method = "getDefaultGravity", at = @At("RETURN"))
    private double modifyGravity(double original) {
        ResourceLocation dimension = stellaris$itemEntity.level().dimension().location();
        if (PlanetUtil.isPlanet(dimension) && Stellaris.CONFIG.gravityConfig.customItemGravity) {
            return Utils.MPS2ToMCG(PlanetUtil.getPlanet(dimension).gravity());
        }
        return original;
    }
}
