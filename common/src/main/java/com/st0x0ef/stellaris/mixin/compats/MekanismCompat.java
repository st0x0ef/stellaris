package com.st0x0ef.stellaris.mixin.compats;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.compats.ModCompat;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ModCompat.MixinCompat(modid = "mekanism")
@Mixin(Item.class)
public class MekanismCompat {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void test(Item.Properties properties, CallbackInfo ci) {
        Stellaris.LOG.error("eee");
    }

}
