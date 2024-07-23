package com.st0x0ef.stellaris.platform.systems.lookup.impl;

import com.st0x0ef.stellaris.platform.systems.lookup.BlockLookup;
import com.st0x0ef.stellaris.platform.systems.lookup.RegistryEventListener;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NeoBlockLookup<T, C> implements BlockLookup<T, C>, RegistryEventListener {
    private final List<Consumer<BlockLookup.BlockRegistrar<T, C>>> registrars = new ArrayList<>();
    private final BlockCapability<T, C> capability;

    public NeoBlockLookup(BlockCapability<T, C> capability) {
        this.capability = capability;
    }

    public NeoBlockLookup(ResourceLocation id, Class<T> type, Class<C> contextType) {
        this(BlockCapability.create(id, type, contextType));
    }

    @Override
    public @Nullable T find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable C direction) {
        return level.getCapability(capability, pos, state, entity, direction);
    }

    @Override
    public void onRegister(Consumer<BlockRegistrar<T, C>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept(new EventRegistrar(event)));
    }

    public class EventRegistrar implements BlockRegistrar<T, C> {
        private final RegisterCapabilitiesEvent event;

        public EventRegistrar(RegisterCapabilitiesEvent event) {
            this.event = event;
        }

        @Override
        public void registerBlocks(BlockGetter<T, C> getter, Block... blocks) {
            event.registerBlock(capability, getter::getContainer, blocks);
        }

        @Override
        public void registerBlockEntities(BlockEntityGetter<T, C> getter, BlockEntityType<?>... blocks) {
            for (BlockEntityType<?> block : blocks) {
                event.registerBlockEntity(capability, block, getter::getContainer);
            }
        }
    }
}