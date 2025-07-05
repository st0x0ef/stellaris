package com.st0x0ef.stellaris.mixin;

import com.st0x0ef.stellaris.common.commands.LaunchPadArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArgumentTypeInfos.class)
public class ArgumentTypeRegistryMixin {

    @Inject(method = "bootstrap", at = @At("HEAD"))
    private static void registerArgument(Registry<ArgumentTypeInfo<?, ?>> registry, CallbackInfoReturnable<ArgumentTypeInfo<?, ?>> cir) {
        ArgumentTypeInfos.register(registry, "launch_pad", LaunchPadArgument.class, SingletonArgumentInfo.contextFree(LaunchPadArgument::create));
    }
}
