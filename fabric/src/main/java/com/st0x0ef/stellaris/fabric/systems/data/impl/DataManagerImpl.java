package com.st0x0ef.stellaris.fabric.systems.data.impl;

import com.st0x0ef.stellaris.common.systems.data.DataManager;
import com.st0x0ef.stellaris.fabric.systems.data.network.BlockEntitySyncPacket;
import com.st0x0ef.stellaris.fabric.systems.data.network.EntitySyncPacket;
import com.st0x0ef.stellaris.fabric.systems.data.sync.DataSyncSerializer;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public record DataManagerImpl<T>(AttachmentType<T> attachmentType,
                                 @Nullable DataComponentType<T> componentType,
                                 @Nullable DataSyncSerializer<T> syncer) implements DataManager<T> {
    @Override
    public T get(Object dataHolder) {
        if (dataHolder instanceof AttachmentTarget target) {
            return target.getAttachedOrCreate(attachmentType);
        } else if (dataHolder instanceof DataComponentHolder holder) {
            return holder.getOrDefault(Objects.requireNonNull(componentType, "Data manager was not built with a component type"), attachmentType.initializer().get());
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment target");
        }
    }

    @Override
    public T getOrThrow(Object dataHolder) {
        if (dataHolder instanceof AttachmentTarget target) {
            return target.getAttachedOrThrow(this.attachmentType);
        } else if (dataHolder instanceof DataComponentHolder holder) {
            return Optional.ofNullable(holder.get(Objects.requireNonNull(componentType, "Data manager was not built with a component type"))).orElseThrow(() -> new RuntimeException(dataHolder + " is not initialized"));
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment target");
        }
    }

    @Override
    public T getOrCreate(Object dataHolder, T data) {
        if (dataHolder instanceof AttachmentTarget target) {
            return target.getAttachedOrSet(this.attachmentType, data);
        } else if (dataHolder instanceof DataComponentHolder holder) {
            return holder.getOrDefault(Objects.requireNonNull(componentType, "Data manager was not built with a component type"), data);
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment target");
        }
    }

    @Override
    public T set(Object dataHolder, T data) {
        return switch (dataHolder) {
            case AttachmentTarget holder -> {
                T t = holder.setAttached(attachmentType, data);
                updateTarget(holder, data);
                yield t;
            }
            case ItemStack holder -> holder.set(Objects.requireNonNull(componentType(), "Data manager was not built with a component type"), data);
            default -> throw new IllegalArgumentException(dataHolder + " is not an attachment holder");
        };
    }

    @Override
    public T remove(Object dataHolder) {
        return switch (dataHolder) {
            case AttachmentTarget holder -> {
                T t = holder.removeAttached(attachmentType);
                updateTarget(holder, null);
                yield t;
            }
            case ItemStack holder -> holder.remove(Objects.requireNonNull(componentType()));
            default -> throw new IllegalArgumentException(dataHolder + " is not an attachment holder");
        };
    }

    @Override
    public boolean has(Object dataHolder) {
        if (dataHolder instanceof AttachmentTarget target) {
            return target.hasAttached(this.attachmentType);
        } else if (dataHolder instanceof DataComponentHolder holder) {
            return holder.has(Objects.requireNonNull(componentType, "Data manager was not built with a component type"));
        } else {
            throw new IllegalArgumentException(dataHolder + " is not an attachment target");
        }
    }

    private void updateTarget(AttachmentTarget target, @Nullable T data) {
        if (syncer == null) return;
        if (target instanceof Entity entity && !entity.level().isClientSide()) {
            PlayerLookup.tracking(entity).forEach(player -> {
                ServerPlayNetworking.send(player, EntitySyncPacket.of(entity, syncer, data));
            });
        }

        if (target instanceof BlockEntity blockEntity && blockEntity.hasLevel() && !blockEntity.getLevel().isClientSide()) {
            PlayerLookup.tracking(blockEntity).forEach(entity -> {
                ServerPlayNetworking.send(entity, BlockEntitySyncPacket.of(blockEntity, syncer, data));
            });
        }
    }
}
