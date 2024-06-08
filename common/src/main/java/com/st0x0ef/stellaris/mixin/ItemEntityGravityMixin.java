package com.st0x0ef.stellaris.mixin;

import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;


@Mixin(ItemEntity.class)
public abstract class ItemEntityGravityMixin {

    @Unique
    ItemEntity stellaris$itemEntity = (ItemEntity) ((Object) this);

    /**
     * @author TATHAN
     * @reason rewrite gravity system
     */
    @Overwrite
    public double getDefaultGravity() {
        ResourceKey<Level> dimension = stellaris$itemEntity.level().dimension();
        if (PlanetUtil.isPlanet(dimension)) {
            return (Utils.MPS2ToMCG(PlanetUtil.getPlanet(dimension).gravity()) % 2);
        } else {
            return 0.04;
        }
    }

}

