package com.st0x0ef.stellaris.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.st0x0ef.stellaris.common.oil.OilUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.storage.ChunkSerializer;
import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChunkSerializer.class)
public class MixinChunkSerializer {

    @WrapOperation(method = "read", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/ChunkAccess;setLightCorrect(Z)V"))
    private static void readDataLevel(ChunkAccess instance, boolean lightCorrect, Operation<Void> original, ServerLevel level, PoiManager poiManager, RegionStorageInfo regionStorageInfo, ChunkPos pos, CompoundTag tag) {
        original.call(instance, lightCorrect);

        if (tag.contains("oilLevel")) {
            instance.stellaris$setChunkOilLevel(tag.getInt("oilLevel"));
        } else {
            instance.stellaris$setChunkOilLevel(OilUtils.getRandomOilLevel());
        }
    }

    @WrapOperation(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtUtils;addCurrentDataVersion(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;"))
    private static CompoundTag writeOilLevel(CompoundTag tag, Operation<CompoundTag> original, ServerLevel level, ChunkAccess chunk) {
        CompoundTag compound = original.call(tag);
        compound.putInt("oilLevel", chunk.stellaris$getChunkOilLevel());

        return compound;
    }
}