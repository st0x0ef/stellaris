package com.st0x0ef.stellaris.mixin.compats;

import com.st0x0ef.stellaris.common.compats.ModCompat;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
@ModCompat.MixinCompat(modid = "mekanism")
public class MekanismCompat {



}
