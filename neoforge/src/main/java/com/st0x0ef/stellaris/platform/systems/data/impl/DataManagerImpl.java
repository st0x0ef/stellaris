package com.st0x0ef.stellaris.platform.systems.data.impl;

import com.st0x0ef.stellaris.platform.systems.data.DataManager;
import com.st0x0ef.stellaris.platform.systems.data.network.BlockEntitySyncPacket;
import com.st0x0ef.stellaris.platform.systems.data.network.EntitySyncPacket;
import com.st0x0ef.stellaris.platform.systems.data.sync.DataSyncSerializer;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public record DataManagerImpl<T>(Supplier<AttachmentType<T>> attachmentType,
                                 @Nullable Supplier<DataComponentType<T>> component,
                                 @Nullable Supplier<DataSyncSerializer<T>> syncer,
                                 Supplier<T> defaultValue) implements DataManager<T> {

    @Override
    public T get(Object dataHolder) {
        if (dataHolder instanceof AttachmentHolder holder) {
            return holder.getData(attachmentType);
        } else if (dataHolder instanceof DataComponentHolder holder) {
            return holder.getOrDefault(Objects.requireNonNull(componentType()), defaultValue.get());
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment holder");
        }
    }

    @Override
    public T getOrThrow(Object dataHolder) {
        if (dataHolder instanceof AttachmentHolder holder) {
            return holder.getExistingData(attachmentType).orElseThrow(() -> new NullPointerException(dataHolder + " is not initialized"));
        } else if (dataHolder instanceof DataComponentHolder holder) {
            return Optional.ofNullable(holder.get(Objects.requireNonNull(componentType()))).orElseThrow(() -> new NullPointerException(dataHolder + " is not initialized"));
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment holder");
        }
    }

    @Override
    public T getOrCreate(Object dataHolder, T data) {
        if (!has(dataHolder)) {
            set(dataHolder, data);
        }
        return get(dataHolder);
    }

    @Override
    public T set(Object dataHolder, T data) {
        return switch (dataHolder) {
            case AttachmentHolder holder -> holder.setData(attachmentType, data);
            case MutableDataComponentHolder holder -> holder.set(Objects.requireNonNull(componentType(),"Component type is null"), data);
            default -> throw new IllegalArgumentException(dataHolder + " is not an attachment holder");
        };
    }

    @Override
    public T remove(Object dataHolder) {
        return switch (dataHolder) {
            case AttachmentHolder holder -> holder.removeData(attachmentType);
            case MutableDataComponentHolder holder -> holder.remove(Objects.requireNonNull(componentType(), "Component type is null"));
            default -> throw new IllegalArgumentException(dataHolder + " is not an attachment holder");
        };
    }

    @Override
    public boolean has(Object dataHolder) {
        if (dataHolder instanceof AttachmentHolder holder) {
            return holder.hasData(attachmentType);
        } else if (dataHolder instanceof DataComponentHolder holder) {
            return holder.has(Objects.requireNonNull(componentType()));
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment holder");
        }
    }

    @Override
    public DataComponentType<T> componentType() {
        return component == null ? null : component.get();
    }

    public void updateTarget(AttachmentHolder target, T data) {
        if (syncer == null) return;
        if (target instanceof Entity entity && !entity.level().isClientSide()) {
            PacketDistributor.sendToPlayersTrackingEntity(entity, EntitySyncPacket.of(entity, syncer.get(), data));
        }

        if (target instanceof BlockEntity blockEntity && blockEntity.getLevel() instanceof ServerLevel level) {
            PacketDistributor.sendToPlayersTrackingChunk(level, new ChunkPos(blockEntity.getBlockPos()), BlockEntitySyncPacket.of(blockEntity, syncer.get(), data));
        }
    }
}
