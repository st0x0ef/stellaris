package com.st0x0ef.stellaris.common.registry;

import net.minecraft.core.Registry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

    public class DamageSourseRegistry {
        public final Registry<DamageType> damageType;
        private final DamageSource Radiation;

        public DamageSourseRegistry (RegistryAccess arg, Registry<DamageType> damageType) {
            this.damageType = damageType;
            this.Radiation = this.source(DamageRegistry.RADIATION);
        }

            public DamageSource source(ResourceKey<DamageType> arg) {
                return new DamageSource(this.damageType.getHolderOrThrow(arg));
            }

            public DamageSource source(ResourceKey<DamageType> arg, @Nullable Entity arg2) {
                return new DamageSource(this.damageType.getHolderOrThrow(arg), arg2);
            }

            public DamageSource source(ResourceKey<DamageType> arg, @Nullable Entity arg2, @Nullable Entity arg3) {
                return new DamageSource(this.damageType.getHolderOrThrow(arg), arg2, arg3);
            }

            public DamageSource RADIATION() {
                return this.source(DamageRegistry.RADIATION);
            }

        }


