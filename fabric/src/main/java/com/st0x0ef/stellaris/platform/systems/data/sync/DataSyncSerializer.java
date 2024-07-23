package com.st0x0ef.stellaris.platform.systems.data.sync;

import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

@SuppressWarnings("UnstableApiUsage")
public interface DataSyncSerializer<T> {
    AttachmentType<T> getAttachmentType();
    StreamCodec<? super RegistryFriendlyByteBuf, T> getCodec();

    static <T> DataSyncSerializer<T> create(AttachmentType<T> attachmentType, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        return new SimpleDataSyncSerializer<>(attachmentType, codec);
    }

    AttachmentData<T> decode(RegistryFriendlyByteBuf buf);

    record SimpleDataSyncSerializer<T>(AttachmentType<T> attachmentType, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) implements DataSyncSerializer<T> {

        @Override
        public AttachmentType<T> getAttachmentType() {
            return attachmentType;
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, T> getCodec() {
            return codec;
        }

        @Override
        public AttachmentData<T> decode(RegistryFriendlyByteBuf buf) {
            if (buf.readBoolean()) {
                return AttachmentData.of(this, codec.decode(buf));
            } else {
                return AttachmentData.of(this, null);
            }
        }
    }
}
