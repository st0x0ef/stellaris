package com.st0x0ef.stellaris.platform.systems.core.item.lookup;

import com.st0x0ef.stellaris.platform.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.platform.systems.lookup.BlockLookup;
import com.st0x0ef.stellaris.platform.systems.resources.item.ItemResource;
import com.st0x0ef.stellaris.platform.systems.core.item.wrappers.CommonItemContainer;
import com.st0x0ef.stellaris.platform.systems.core.item.wrappers.NeoItemHandler;
import com.st0x0ef.stellaris.platform.systems.lookup.RegistryEventListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class ItemBlockLookup implements BlockLookup<CommonStorage<ItemResource>, Direction>, RegistryEventListener {
    public static final ItemBlockLookup INSTANCE = new ItemBlockLookup();
    private final List<Consumer<BlockRegistrar<CommonStorage<ItemResource>, Direction>>> registrars = new ArrayList<>();

    private ItemBlockLookup() {
        registerSelf();
    }

    @Override
    public @Nullable CommonStorage<ItemResource> find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, state, entity, direction);
        if (handler instanceof NeoItemHandler(CommonStorage<ItemResource> container)) {
            return container;
        }
        return handler == null ? null : new CommonItemContainer(handler);
    }

    @Override
    public void onRegister(Consumer<BlockRegistrar<CommonStorage<ItemResource>, Direction>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept(new EventRegistrar(event)));
    }

    public record EventRegistrar(RegisterCapabilitiesEvent event) implements BlockRegistrar<CommonStorage<ItemResource>, Direction> {
        @Override
        public void registerBlocks(BlockGetter<CommonStorage<ItemResource>, Direction> getter, net.minecraft.world.level.block.Block... containers) {
            for (net.minecraft.world.level.block.Block block : containers) {
                event.registerBlock(Capabilities.ItemHandler.BLOCK, (level, pos, state, entity, direction) -> {
                    CommonStorage<ItemResource> container = getter.getContainer(level, pos, state, entity, direction);
                    return container == null ? null : new NeoItemHandler(container);
                }, block);
            }
        }

        @Override
        public void registerBlockEntities(BlockEntityGetter<CommonStorage<ItemResource>, Direction> getter, BlockEntityType<?>... containers) {
            for (BlockEntityType<?> blockEntityType : containers) {
                event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, blockEntityType, (entity, direction) -> {
                    CommonStorage<ItemResource> container = getter.getContainer(entity, direction);
                    return container == null ? null : new NeoItemHandler(container);
                });
            }
        }
    }
}
