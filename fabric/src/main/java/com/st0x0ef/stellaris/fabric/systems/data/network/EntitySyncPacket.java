package com.st0x0ef.stellaris.fabric.systems.data.network;

import com.st0x0ef.stellaris.fabric.systems.data.FabricDataLib;
import com.st0x0ef.stellaris.fabric.systems.data.sync.AttachmentData;
import com.st0x0ef.stellaris.fabric.systems.data.sync.DataSyncSerializer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public record EntitySyncPacket(int entityId, AttachmentData<?> syncData) implements CustomPacketPayload {
    public static final Type<EntitySyncPacket> TYPE = new Type<>(new ResourceLocation(FabricDataLib.MOD_ID, "entity"));
    public static final StreamCodec<RegistryFriendlyByteBuf, EntitySyncPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            EntitySyncPacket::entityId,
            FabricDataLib.SYNC_SERIALIZER_STREAM_CODEC,
            EntitySyncPacket::syncData,
            EntitySyncPacket::new
    );

    public static <T> EntitySyncPacket of(Entity entity, DataSyncSerializer<T> serializer, @Nullable T data) {
        return new EntitySyncPacket(entity.getId(), AttachmentData.of(serializer, data));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
