package com.st0x0ef.stellaris.common.registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

    public interface DamageRegistry {

        ResourceKey<DamageType> RADIATION = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("radiation"));


        static void bootstrap(BootstapContext<DamageType> bootstapContext) {
            bootstapContext.register(RADIATION, new DamageType("radiation", 0.0F));




        }
    }