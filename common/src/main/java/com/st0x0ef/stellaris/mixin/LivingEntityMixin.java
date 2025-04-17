package com.st0x0ef.stellaris.mixin;

import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import com.st0x0ef.stellaris.common.data_components.SpaceSuitModules;
import com.st0x0ef.stellaris.common.oxygen.DimensionOxygenManager;
import com.st0x0ef.stellaris.common.oxygen.GlobalOxygenManager;
import com.st0x0ef.stellaris.common.registry.DamageSourceRegistry;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract ItemStack getItemBySlot(EquipmentSlot slot);

    @Shadow
    @Nullable
    public abstract AttributeInstance getAttribute(Holder<Attribute> attribute);

    @Shadow
    public abstract AttributeMap getAttributes();

    @Unique
    private final LivingEntity stellaris$livingEntity = (LivingEntity) (Object) this;

    @Unique
    private long stellaris$tickSinceLastOxygenCheck;

    @Unique
    private DimensionOxygenManager stellaris$oxygenManager;

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("HEAD"), method = "tick()V")
    private void tick(CallbackInfo ci) {
        if (firstTick) {
            if (!SpaceSuitModules.containsInModules(getItemBySlot(EquipmentSlot.CHEST), ItemsRegistry.MODULE_GRAVITY_NORMALIZER.get().getDefaultInstance())) {
                ResourceLocation stellaris$dimension = level().dimension().location();

                if (!stellaris$dimension.equals(StellarisData.OVERWORLD) && PlanetUtil.isPlanet(stellaris$dimension)) {
                    double stellaris$gravity = Utils.MPS2ToMCG(PlanetUtil.getPlanet(stellaris$dimension).gravity());

                    stellaris$trySetAttribute(Attributes.GRAVITY, stellaris$gravity);
                    stellaris$trySetAttribute(Attributes.SAFE_FALL_DISTANCE, 3.0 / (stellaris$gravity / 0.08));
                    stellaris$trySetAttribute(Attributes.FALL_DAMAGE_MULTIPLIER, stellaris$gravity / 0.08);
                }
            }
        }

        if (!level().isClientSide()) {
            if (stellaris$tickSinceLastOxygenCheck > 20) {
                if (stellaris$oxygenManager == null) {
                    stellaris$oxygenManager = GlobalOxygenManager.getInstance().getOrCreateDimensionManager((ServerLevel) level());
                }

                if (!stellaris$oxygenManager.breath(stellaris$livingEntity)) {
                    hurt(DamageSourceRegistry.of(level(), DamageSourceRegistry.OXYGEN), 2f);
                }

                stellaris$tickSinceLastOxygenCheck = 0;
            }

            stellaris$tickSinceLastOxygenCheck++;
        }
    }

    @Unique
    private void stellaris$trySetAttribute(Holder<Attribute> attribute, double value) {
        AttributeInstance attributeInstance = getAttribute(attribute);

        if (attributeInstance != null) {
            attributeInstance.setBaseValue(value);
        }
    }
}
