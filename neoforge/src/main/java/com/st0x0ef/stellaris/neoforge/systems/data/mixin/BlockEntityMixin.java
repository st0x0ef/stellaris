package com.st0x0ef.stellaris.neoforge.systems.data.mixin;

import com.st0x0ef.stellaris.neoforge.systems.data.network.BlockEntitySyncAllPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {

    @Shadow @Nullable public abstract Level getLevel();

    @Shadow public abstract BlockPos getBlockPos();

    @Inject(method = "getUpdatePacket", at = @At("HEAD"))
    private void csl_data$syncAllAttachments(CallbackInfoReturnable<Packet<ClientGamePacketListener>> cir) {
        if (this.getLevel() instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersTrackingChunk(serverLevel, new ChunkPos(this.getBlockPos()), BlockEntitySyncAllPacket.of((BlockEntity) (Object) this));
        }
    }
}
