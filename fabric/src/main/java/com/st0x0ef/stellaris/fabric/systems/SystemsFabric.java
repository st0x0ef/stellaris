package com.st0x0ef.stellaris.fabric.systems;

import com.st0x0ef.stellaris.common.systems.energy.EnergyApi;
import com.st0x0ef.stellaris.common.systems.energy.base.EnergyBlock;
import com.st0x0ef.stellaris.common.systems.energy.base.EnergyItem;
import com.st0x0ef.stellaris.common.systems.item.ItemContainerBlock;
import com.st0x0ef.stellaris.common.systems.item.base.BotariumItemBlock;
import com.st0x0ef.stellaris.fabric.systems.energy.FabricBlockEnergyContainer;
import com.st0x0ef.stellaris.fabric.systems.energy.FabricItemEnergyContainer;
import com.st0x0ef.stellaris.fabric.systems.item.FabricItemContainer;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.impl.transfer.item.InventoryStorageImpl;
import team.reborn.energy.api.EnergyStorage;

@SuppressWarnings("UnstableApiUsage")
public class SystemsFabric {

    public void init() {

        EnergyStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> {
            if (blockEntity instanceof EnergyBlock<?> attachment) {
                var container = attachment.getEnergyStorage(world, pos, state, blockEntity, context);
                return container == null ? null : new FabricBlockEnergyContainer<>(container);
            } else if (state.getBlock() instanceof EnergyBlock<?> attachment) {
                var container = attachment.getEnergyStorage(world, pos, state, blockEntity, context);
                return container == null ? null : new FabricBlockEnergyContainer<>(container);
            } else {
                EnergyBlock<?> blockEnergyGetter = EnergyApi.getEnergyBlock(state.getBlock());
                if (blockEnergyGetter != null) {
                    var container = blockEnergyGetter.getEnergyStorage(world, pos, state, blockEntity, context);
                    if (container != null) {
                        return new FabricBlockEnergyContainer<>(container);
                    }
                }
                if (blockEntity != null) {
                    EnergyBlock<?> entityEnergyGetter = EnergyApi.getEnergyBlock(blockEntity.getType());
                    if (entityEnergyGetter == null) return null;
                    var entityContainer = entityEnergyGetter.getEnergyStorage(world, pos, state, blockEntity, context);
                    return entityContainer == null ? null : new FabricBlockEnergyContainer<>(entityContainer);
                }
            }
            return null;
        });

        EnergyStorage.ITEM.registerFallback((itemStack, context) -> {
            if (itemStack.getItem() instanceof EnergyItem<?> attachment) {
                var energyStorage = attachment.getEnergyStorage(itemStack);
                return energyStorage == null ? null : new FabricItemEnergyContainer<>(context, itemStack, energyStorage);
            } else {
                EnergyItem<?> itemEnergyGetter = EnergyApi.getEnergyItem(itemStack.getItem());
                if (itemEnergyGetter == null) return null;
                var container = itemEnergyGetter.getEnergyStorage(itemStack);
                return container == null ? null : new FabricItemEnergyContainer<>(context, itemStack, container);
            }
        });

//        FluidStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> { TODO fluid
//            if (blockEntity instanceof FluidBlock<?> attachment) {
//                var container = attachment.getFluidContainer(world, pos, state, blockEntity, context);
//                return container == null ? null : new FabricBlockFluidContainer<>(container);
//            } else if (state.getBlock() instanceof FluidBlock<?> attachment) {
//                var container = attachment.getFluidContainer(world, pos, state, blockEntity, context);
//                return container == null ? null : new FabricBlockFluidContainer<>(container);
//            } else {
//                var blockEnergyGetter = FluidApi.getFluidBlock(state.getBlock());
//                if (blockEnergyGetter != null) {
//                    var container = blockEnergyGetter.getFluidContainer(world, pos, state, blockEntity, context);
//                    if (container != null) {
//                        return new FabricBlockFluidContainer<>(container);
//                    }
//                }
//                if (blockEntity != null) {
//                    var entityEnergyGetter = FluidApi.getFluidBlock(blockEntity.getType());
//                    if (entityEnergyGetter == null) return null;
//                    var entityContainer = entityEnergyGetter.getFluidContainer(world, pos, state, blockEntity, context);
//                    return entityContainer == null ? null : new FabricBlockFluidContainer<>(entityContainer);
//                }
//            }
//            return null;
//        });
//
//
//        FluidStorage.ITEM.registerFallback((itemStack, context) -> {
//            if (itemStack.getItem() instanceof FluidItem<?> attachment) {
//                var fluidContainer = attachment.getFluidContainer(itemStack);
//                return fluidContainer == null ? null : new FabricItemFluidContainer<>(context, fluidContainer);
//            } else {
//                var itemFluidGetter = FluidApi.getFluidItem(itemStack.getItem());
//                if (itemFluidGetter == null) return null;
//                var container = itemFluidGetter.getFluidContainer(itemStack);
//                return container == null ? null : new FabricItemFluidContainer<>(context, container);
//            }
//        });

        ItemStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> {
            if (blockEntity instanceof ItemContainerBlock energyContainer) {
                return InventoryStorageImpl.of(energyContainer.getContainer(), context);
            }
            return null;
        });

        ItemStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> {
            if (blockEntity instanceof BotariumItemBlock<?> attachment) {
                var container = attachment.getItemContainer(world, pos, state, blockEntity, context);
                return container == null ? null : new FabricItemContainer(container);
            } else if (state.getBlock() instanceof BotariumItemBlock<?> attachment) {
                var container = attachment.getItemContainer(world, pos, state, blockEntity, context);
                return container == null ? null : new FabricItemContainer(container);
            }
            return null;
        });

    }
}