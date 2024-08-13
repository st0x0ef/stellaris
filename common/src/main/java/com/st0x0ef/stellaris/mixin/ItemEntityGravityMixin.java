package com.st0x0ef.stellaris.mixin;

import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(ItemEntity.class)
public abstract class ItemEntityGravityMixin {
    // TODO : remake the gravity system
    /*@Overwrite
    public double getDefaultGravity() {
        ResourceLocation dimension = stellaris$itemEntity.level().dimension().location();
        if (PlanetUtil.isPlanet(dimension)) {
            return (Utils.MPS2ToMCG(PlanetUtil.getPlanet(dimension).gravity()) % 2);
        } else {
            return 0.04;
        }
    }*/
}

