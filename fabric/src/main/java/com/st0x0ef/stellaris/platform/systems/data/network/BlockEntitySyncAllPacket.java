package com.st0x0ef.stellaris.platform.systems.data.network;

import com.st0x0ef.stellaris.platform.systems.data.FabricDataLib;
import com.st0x0ef.stellaris.platform.systems.data.sync.AttachmentData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;

public record BlockEntitySyncAllPacket(BlockPos pos, BlockEntityType<?> blockEntityType, List<AttachmentData<?>> syncData) implements CustomPacketPayload {
    public static final Type<BlockEntitySyncAllPacket> TYPE = new Type<>(new ResourceLocation(FabricDataLib.MOD_ID, "block_entity_all"));
    public static final StreamCodec<RegistryFriendlyByteBuf, BlockEntitySyncAllPacket> CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            BlockEntitySyncAllPacket::pos,
            ByteBufCodecs.registry(Registries.BLOCK_ENTITY_TYPE),
            BlockEntitySyncAllPacket::blockEntityType,
            ByteBufCodecs.collection(ArrayList::new, FabricDataLib.SYNC_SERIALIZER_STREAM_CODEC),
            BlockEntitySyncAllPacket::syncData,
            BlockEntitySyncAllPacket::new
    );

    public static BlockEntitySyncAllPacket of(BlockEntity entity) {
        return new BlockEntitySyncAllPacket(entity.getBlockPos(), entity.getType(), AttachmentData.getAllSyncData(entity));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
