package com.st0x0ef.stellaris.fabric.systems.data;

import com.st0x0ef.stellaris.fabric.systems.data.network.BlockEntitySyncAllPacket;
import com.st0x0ef.stellaris.fabric.systems.data.network.BlockEntitySyncPacket;
import com.st0x0ef.stellaris.fabric.systems.data.network.EntitySyncAllPacket;
import com.st0x0ef.stellaris.fabric.systems.data.network.EntitySyncPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.Optional;

public class FabricDataLibClient {

    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(BlockEntitySyncPacket.TYPE, (payload, context) -> {
            context.player().level().getBlockEntity(payload.pos(), payload.blockEntityType()).ifPresent(entity -> {
                payload.syncData().updateTarget(entity);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(EntitySyncPacket.TYPE, (payload, context) -> {
            Optional.ofNullable(context.player().level().getEntity(payload.entityId())).ifPresent(entity -> {
                payload.syncData().updateTarget(entity);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(EntitySyncAllPacket.TYPE, (payload, context) -> {
            Optional.ofNullable(context.player().level().getEntity(payload.entityId())).ifPresent(entity -> {
                payload.syncData().forEach(data -> data.updateTarget(entity));
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(BlockEntitySyncAllPacket.TYPE, (payload, context) -> {
            context.player().level().getBlockEntity(payload.pos(), payload.blockEntityType()).ifPresent(entity -> {
                payload.syncData().forEach(data -> data.updateTarget(entity));
            });
        });
    }
}
