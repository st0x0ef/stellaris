package com.st0x0ef.stellaris.neoforge.mixin;

import dev.architectury.registry.registries.forge.RegistrarManagerImpl;
import net.minecraft.core.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RegistrarManagerImpl.RegistrarImpl.class)
public interface RegistrarMixin {
    @Accessor
    <T> Registry<T> getDelegate();
}
