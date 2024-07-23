package com.st0x0ef.stellaris.platform.systems.core.energy.lookup;

import com.st0x0ef.stellaris.platform.systems.core.storage.base.ValueStorage;
import com.st0x0ef.stellaris.platform.systems.lookup.BlockLookup;
import com.st0x0ef.stellaris.platform.systems.core.energy.wrappers.CommonEnergyStorage;
import com.st0x0ef.stellaris.platform.systems.core.energy.wrappers.NeoEnergyContainer;
import com.st0x0ef.stellaris.platform.systems.lookup.RegistryEventListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class EnergyBlockLookup implements BlockLookup<ValueStorage, Direction>, RegistryEventListener {
    public static final EnergyBlockLookup INSTANCE = new EnergyBlockLookup();
    private final List<Consumer<BlockRegistrar<ValueStorage, Direction>>> registrars = new ArrayList<>();

    private EnergyBlockLookup() {
        registerSelf();
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public @Nullable ValueStorage find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        IEnergyStorage storage = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, state, entity, direction);
        if (storage instanceof NeoEnergyContainer(ValueStorage container)) {
            return container;
        }
        return storage == null ? null : new CommonEnergyStorage(storage);
    }

    @Override
    public void onRegister(Consumer<BlockRegistrar<ValueStorage, Direction>> registrar) {
        registrars.add(registrar);
    }

    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept(new EventRegistrar(event)));
    }

    public static class EventRegistrar implements BlockLookup.BlockRegistrar<ValueStorage, Direction> {
        RegisterCapabilitiesEvent event;

        public EventRegistrar(RegisterCapabilitiesEvent event) {
            this.event = event;
        }

        @Override
        public void registerBlocks(BlockLookup.BlockGetter<ValueStorage, Direction> getter, Block... containers) {
            for (Block block : containers) {
                event.registerBlock(Capabilities.EnergyStorage.BLOCK, (level, pos, state, entity, direction) -> {
                    ValueStorage storage = getter.getContainer(level, pos, state, entity, direction);
                    return storage == null ? null : new NeoEnergyContainer(storage);
                }, block);
            }
        }

        @Override
        public void registerBlockEntities(BlockLookup.BlockEntityGetter<ValueStorage, Direction> getter, BlockEntityType<?>... containers) {
            for (BlockEntityType<?> blockEntity : containers) {
                event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, blockEntity, (entity, direction) -> {
                    ValueStorage storage = getter.getContainer(entity, direction);
                    return storage == null ? null : new NeoEnergyContainer(storage);
                });
            }
        }
    }
}
