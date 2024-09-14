package com.st0x0ef.stellaris.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.st0x0ef.stellaris.common.oil.OilUtils;
import com.st0x0ef.stellaris.common.oxygen.GlobalOxygenManager;
import com.st0x0ef.stellaris.common.oxygen.OxygenRoom;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.storage.ChunkSerializer;
import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicInteger;

@Mixin(ChunkSerializer.class)
public class MixinChunkSerializer {

    @WrapOperation(method = "read", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/ChunkAccess;setLightCorrect(Z)V"))
    private static void readDataLevel(ChunkAccess instance, boolean lightCorrect, Operation<Void> original, ServerLevel level, PoiManager poiManager, RegionStorageInfo regionStorageInfo, ChunkPos pos, CompoundTag tag) {
        original.call(instance, lightCorrect);
        if (instance.stellaris$getChunkOilLevel() == 0) {
            instance.stellaris$setChunkOilLevel(OilUtils.getRandomOilLevel());

        } else {
            instance.stellaris$setChunkOilLevel(tag.getInt("oilLevel"));
        }
    }

    @Inject(method = "write", at= @At(value = "TAIL"))
    private static void writeDataLevel(ServerLevel level, ChunkAccess chunk, CallbackInfoReturnable<CompoundTag> cir, @Local(name = "compoundTag") CompoundTag compoundTag) {
        compoundTag.putInt("oilLevel", chunk.stellaris$getChunkOilLevel());
    }
}