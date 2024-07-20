package com.st0x0ef.stellaris.neoforge.systems.core.fluid.lookup;

import com.st0x0ef.stellaris.common.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.common.systems.lookup.BlockLookup;
import com.st0x0ef.stellaris.common.systems.resources.fluid.FluidResource;
import com.st0x0ef.stellaris.neoforge.systems.core.fluid.wrappers.CommonFluidContainer;
import com.st0x0ef.stellaris.neoforge.systems.core.fluid.wrappers.NeoFluidContainer;
import com.st0x0ef.stellaris.neoforge.systems.lookup.RegistryEventListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class FluidBlockLookup implements BlockLookup<CommonStorage<FluidResource>, Direction>, RegistryEventListener {
    public static final FluidBlockLookup INSTANCE = new FluidBlockLookup();
    private final List<Consumer<BlockRegistrar<CommonStorage<FluidResource>, Direction>>> registrars = new ArrayList<>();

    private FluidBlockLookup() {
        registerSelf();
    }

    @Override
    public @Nullable CommonStorage<FluidResource> find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        IFluidHandler handler = level.getCapability(Capabilities.FluidHandler.BLOCK, pos, state, entity, direction);

        if (handler instanceof NeoFluidContainer(CommonStorage<FluidResource> container)) {
            return container;
        }

        return handler == null ? null : new CommonFluidContainer(handler);
    }

    @Override
    public void onRegister(Consumer<BlockRegistrar<CommonStorage<FluidResource>, Direction>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept(new EventRegistrar(event)));
    }

    public record EventRegistrar(RegisterCapabilitiesEvent event) implements BlockRegistrar<CommonStorage<FluidResource>, Direction> {
        @Override
        public void registerBlocks(BlockGetter<CommonStorage<FluidResource>, Direction> getter, net.minecraft.world.level.block.Block... containers) {
            for (net.minecraft.world.level.block.Block block : containers) {
                event.registerBlock(Capabilities.FluidHandler.BLOCK, (level, pos, state, entity, direction) -> {
                    CommonStorage<FluidResource> container = getter.getContainer(level, pos, state, entity, direction);
                    return container == null ? null : new NeoFluidContainer(container);
                }, block);
            }
        }

        @Override
        public void registerBlockEntities(BlockEntityGetter<CommonStorage<FluidResource>, Direction> getter, BlockEntityType<?>... containers) {
            for (BlockEntityType<?> blockEntityType : containers) {
                event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, blockEntityType, (entity, direction) -> {
                    CommonStorage<FluidResource> container = getter.getContainer(entity, direction);
                    return container == null ? null : new NeoFluidContainer(container);
                });
            }
        }
    }
}
