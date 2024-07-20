package com.st0x0ef.stellaris.fabric.systems.data;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.fabric.systems.data.network.BlockEntitySyncAllPacket;
import com.st0x0ef.stellaris.fabric.systems.data.network.BlockEntitySyncPacket;
import com.st0x0ef.stellaris.fabric.systems.data.network.EntitySyncAllPacket;
import com.st0x0ef.stellaris.fabric.systems.data.network.EntitySyncPacket;
import com.st0x0ef.stellaris.fabric.systems.data.sync.AttachmentData;
import com.st0x0ef.stellaris.fabric.systems.data.sync.DataSyncSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class FabricDataLib {
    public static final String MOD_ID = Stellaris.MODID;
    public static final ResourceKey<Registry<DataSyncSerializer<?>>> SYNC_SERIALIZERS_KEY = ResourceKey.createRegistryKey(new ResourceLocation(MOD_ID, "sync_serializers"));
    public static final Registry<DataSyncSerializer<?>> SYNC_SERIALIZERS = FabricRegistryBuilder.createSimple(SYNC_SERIALIZERS_KEY).buildAndRegister();
    public static StreamCodec<RegistryFriendlyByteBuf, AttachmentData<?>> SYNC_SERIALIZER_STREAM_CODEC = new StreamCodec<>() {
        @Override
        public void encode(RegistryFriendlyByteBuf object, AttachmentData<?> object2) {
            object2.encode(object);
        }

        @Override
        public AttachmentData<?> decode(RegistryFriendlyByteBuf object) {
            ResourceLocation key = object.readResourceLocation();
            DataSyncSerializer<?> serializer = SYNC_SERIALIZERS.get(key);
            if (serializer == null) {
                throw new IllegalStateException("Unknown sync serializer: " + key);
            }
            return serializer.decode(object);
        }
    };


    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(BlockEntitySyncPacket.TYPE, BlockEntitySyncPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(EntitySyncPacket.TYPE, EntitySyncPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(BlockEntitySyncAllPacket.TYPE, BlockEntitySyncAllPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(EntitySyncAllPacket.TYPE, EntitySyncAllPacket.CODEC);

        EntityTrackingEvents.START_TRACKING.register((entity, player) -> {
            EntitySyncAllPacket entitySyncAllPacket = EntitySyncAllPacket.of(entity);
            ServerPlayNetworking.send(player, entitySyncAllPacket);
        });

    }
}
