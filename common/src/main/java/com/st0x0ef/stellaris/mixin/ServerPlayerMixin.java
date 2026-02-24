package com.st0x0ef.stellaris.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.st0x0ef.stellaris.common.oxygen.GlobalOxygenManager;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.portal.DimensionTransition;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends EntityMixin {

	@WrapMethod(method = "changeDimension") // needs to be implemented for the ServerPlayer class too, as ServerPlayer#changeDimension is not calling super#changeDimension
	public @Nullable Entity changeDimension(DimensionTransition transition, Operation<Entity> original) {
		stellaris$oxygenManager = GlobalOxygenManager.getInstance().getOrCreateDimensionManager(transition.newLevel());
		Entity entity = original.call(transition);
		if (entity instanceof LivingEntity living)
			Utils.handleGravityChange(living, transition.newLevel());
		return entity;
	}
}
