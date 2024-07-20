package com.st0x0ef.stellaris.fabric.systems.data.network;

import com.st0x0ef.stellaris.fabric.systems.data.FabricDataLib;
import com.st0x0ef.stellaris.fabric.systems.data.sync.AttachmentData;
import com.st0x0ef.stellaris.fabric.systems.data.sync.DataSyncSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

public record BlockEntitySyncPacket(BlockPos pos, BlockEntityType<?> blockEntityType, AttachmentData<?> syncData) implements CustomPacketPayload {
    public static final Type<BlockEntitySyncPacket> TYPE = new Type<>(new ResourceLocation(FabricDataLib.MOD_ID, "block_entity"));
    public static final StreamCodec<RegistryFriendlyByteBuf, BlockEntitySyncPacket> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            BlockEntitySyncPacket::pos,
            ByteBufCodecs.registry(Registries.BLOCK_ENTITY_TYPE),
            BlockEntitySyncPacket::blockEntityType,
            FabricDataLib.SYNC_SERIALIZER_STREAM_CODEC,
            BlockEntitySyncPacket::syncData,
            BlockEntitySyncPacket::new
    );

    public static <T> BlockEntitySyncPacket of(BlockEntity entity, DataSyncSerializer<T> serializer, @Nullable T data) {
        return new BlockEntitySyncPacket(entity.getBlockPos(), entity.getType(), AttachmentData.of(serializer, data));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
