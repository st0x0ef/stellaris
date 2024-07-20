package com.st0x0ef.stellaris.neoforge.systems.data.sync;

import com.st0x0ef.stellaris.neoforge.systems.data.NeoDataLib;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public record AttachmentData<T>(DataSyncSerializer<T> serializer, @Nullable T data) {
    public static <T> AttachmentData<T> of(DataSyncSerializer<T> serializer, T data) {
        return new AttachmentData<>(serializer, data);
    }

    public static <T> AttachmentData<T> of(AttachmentHolder target, DataSyncSerializer<T> serializer) {
        return new AttachmentData<>(serializer, target.getData(serializer.getAttachmentType()));
    }

    public void encode(RegistryFriendlyByteBuf buf) {
        ResourceLocation key = NeoDataLib.SYNC_SERIALIZERS.getKey(serializer);
        if (key == null) {
            throw new IllegalStateException("Unknown sync serializer: " + serializer);
        }
        buf.writeResourceLocation(key);
        if (data == null) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            serializer.getCodec().encode(buf, (T) data);
        }
    }

    public void updateTarget(AttachmentHolder target) {
        if (data == null) {
            target.removeData(serializer.getAttachmentType());
        } else {
            target.setData(serializer.getAttachmentType(), data);
        }
    }

    public static List<AttachmentData<?>> getAllSyncData(AttachmentHolder target) {
        List<AttachmentData<?>> syncData = new ArrayList<>();
        for (DataSyncSerializer<?> serializer : NeoDataLib.SYNC_SERIALIZERS) {
            syncData.add(AttachmentData.of(target, serializer));
        }
        return syncData;
    }
}