package com.st0x0ef.stellaris.fabric.systems.data.network;

import com.st0x0ef.stellaris.fabric.systems.data.FabricDataLib;
import com.st0x0ef.stellaris.fabric.systems.data.sync.AttachmentData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public record EntitySyncAllPacket(int entityId, List<AttachmentData<?>> syncData) implements CustomPacketPayload {
    public static final Type<EntitySyncAllPacket> TYPE = new Type<>(new ResourceLocation(FabricDataLib.MOD_ID, "entity_all"));
    public static final StreamCodec<RegistryFriendlyByteBuf, EntitySyncAllPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            EntitySyncAllPacket::entityId,
            ByteBufCodecs.collection(ArrayList::new, FabricDataLib.SYNC_SERIALIZER_STREAM_CODEC),
            EntitySyncAllPacket::syncData,
            EntitySyncAllPacket::new
    );

    public static EntitySyncAllPacket of(Entity entity) {
        return new EntitySyncAllPacket(entity.getId(), AttachmentData.getAllSyncData(entity));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
