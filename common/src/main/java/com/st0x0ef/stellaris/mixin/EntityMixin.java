package com.st0x0ef.stellaris.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.st0x0ef.stellaris.common.oxygen.DimensionOxygenManager;
import com.st0x0ef.stellaris.common.oxygen.GlobalOxygenManager;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
public abstract class EntityMixin {

	@Unique
	protected DimensionOxygenManager stellaris$oxygenManager;

	@Shadow
	protected boolean firstTick;

	@Shadow
	public abstract Level level();

	@WrapMethod(method = "changeDimension")
	public @Nullable Entity changeDimension(DimensionTransition transition, Operation<Entity> original) {
		stellaris$oxygenManager = GlobalOxygenManager.getInstance().getOrCreateDimensionManager(transition.newLevel());
		Entity entity = original.call(transition);
		if (entity instanceof LivingEntity living)
			Utils.handleGravityChange(living, transition.newLevel());
		return entity;
	}
}
