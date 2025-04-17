package com.st0x0ef.stellaris.mixin.client;

import com.st0x0ef.stellaris.common.entities.vehicles.IVehicleEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class PlayerRiddingVehicleMixin  {

    @Shadow public abstract void setPose(Pose pose);

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setPose(Lnet/minecraft/world/entity/Pose;)V"), method = "startRiding(Lnet/minecraft/world/entity/Entity;Z)Z")
    private void setupAnim(Entity entity, boolean bl, CallbackInfoReturnable<Boolean> cir) {
        if(entity.getVehicle() instanceof IVehicleEntity vehicle) {
            setPose(vehicle.setPassengersRiding() ? Pose.SITTING : Pose.STANDING);
        }
    }
}
