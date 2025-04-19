package com.st0x0ef.stellaris.mixin;

import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.storage.SerializableChunkData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SerializableChunkData.class)
public abstract class ChunkSerializerMixin {
    @Unique
    private ChunkAccess stellaris$chunkAccess;

    /*@Inject(method = "parse", at = @At(value = "HEAD"))
    private static void readDataLevel(LevelHeightAccessor levelHeightAccessor, RegistryAccess registryAccess, CompoundTag compoundTag, CallbackInfoReturnable<SerializableChunkData> cir) {
        MixinChunkSerializer.stellaris$chunkAccess = registryAccess;

        if (tag.contains("oilLevel")) {
            instance.stellaris$setChunkOilLevel(tag.getInt("oilLevel").get());
        } else {
            instance.stellaris$setChunkOilLevel(OilUtils.getRandomOilLevel());
        }
    }

    @WrapOperation(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtUtils;addCurrentDataVersion(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;"))
    private CompoundTag writeOilLevel(CompoundTag compoundTag, Operation<CompoundTag> original) {
        CompoundTag compound = original.call(compoundTag);
        compound.putInt("oilLevel", this.stellaris$chunkAccess.stellaris$getChunkOilLevel());

        return compound;
    }*/
}