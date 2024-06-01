package com.st0x0ef.stellaris.neoforge.systems;

import com.st0x0ef.stellaris.common.systems.energy.EnergyApi;
import com.st0x0ef.stellaris.common.systems.energy.base.EnergyBlock;
import com.st0x0ef.stellaris.common.systems.energy.base.EnergyItem;
import com.st0x0ef.stellaris.common.systems.item.ItemContainerBlock;
import com.st0x0ef.stellaris.common.systems.item.base.BotariumItemBlock;
import com.st0x0ef.stellaris.neoforge.systems.energy.ForgeEnergyContainer;
import com.st0x0ef.stellaris.neoforge.systems.generic.NeoForgeCapsHandler;
import com.st0x0ef.stellaris.neoforge.systems.item.ForgeItemContainer;
import com.st0x0ef.stellaris.neoforge.systems.item.ItemContainerWrapper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BucketItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;

@SuppressWarnings({"unused", "CodeBlock2Expr"})
public class SystemsNeoForge {

    public static void init(IEventBus bus) {
        bus.addListener(SystemsNeoForge::registerEnergy);
        //bus.addListener(SystemsNeoForge::registerFluid); TODO fix this
        bus.addListener(SystemsNeoForge::registerItem);
        bus.addListener(NeoForgeCapsHandler::registerCapabilities);
    }

    public static void registerItem(RegisterCapabilitiesEvent event) {
        BuiltInRegistries.BLOCK_ENTITY_TYPE.forEach(blockEntityType -> {
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, blockEntityType, (blockEntity, object2) -> {
                if (blockEntity instanceof ItemContainerBlock itemContainerBlock) {
                    return new ItemContainerWrapper(itemContainerBlock.getContainer());
                }
                if (blockEntity instanceof BotariumItemBlock<?> itemBlock) {
                    return new ForgeItemContainer(itemBlock.getItemContainer(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, object2));
                }
                return null;
            });
        });

        BuiltInRegistries.BLOCK.stream().filter(block -> block instanceof BotariumItemBlock<?>).forEach(block -> {
            event.registerBlock(Capabilities.ItemHandler.BLOCK, (level, blockPos, blockState, blockEntity, direction) -> {
                if (blockState.getBlock() instanceof BotariumItemBlock<?> itemContainerBlock) {
                    return new ForgeItemContainer(itemContainerBlock.getItemContainer(level, blockPos, blockState, blockEntity, direction));
                }
                return null;
            }, block);
        });
    }

    public static void registerEnergy(RegisterCapabilitiesEvent event) {
        EnergyApi.getBlockEntityRegistry().forEach((blockEntityType, blockEnergyGetter1) -> {
            event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, blockEntityType, (blockEntity, direction) -> {
                return new ForgeEnergyContainer<>(blockEnergyGetter1.getEnergyStorage(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, direction));
            });
        });

        EnergyApi.getBlockRegistry().forEach((block, blockEnergyGetter) -> {
            event.registerBlock(Capabilities.EnergyStorage.BLOCK, (level, blockPos, blockState, blockEntity, direction) -> {
                return new ForgeEnergyContainer<>(blockEnergyGetter.getEnergyStorage(level, blockPos, blockState, blockEntity, direction));
            }, block);
        });

        EnergyApi.getItemRegistry().forEach((item, itemEnergyGetter) -> {
            event.registerItem(Capabilities.EnergyStorage.ITEM, (itemStack, unused) -> {
                var energyContainer = itemEnergyGetter.getEnergyStorage(itemStack);
                if (energyContainer != null) {
                    return new ForgeEnergyContainer<>(energyContainer);
                }
                return null;
            }, item);
        });

        BuiltInRegistries.BLOCK.stream().filter(block -> block instanceof EnergyBlock<?>).forEach(block -> {
            event.registerBlock(Capabilities.EnergyStorage.BLOCK, (level, blockPos, blockState, blockEntity, direction) -> {
                if (blockState.getBlock() instanceof EnergyBlock<?> energyBlock) {
                    return new ForgeEnergyContainer<>(energyBlock.getEnergyStorage(level, blockPos, blockState, blockEntity, direction));
                }
                return null;
            }, block);
        });

        BuiltInRegistries.BLOCK_ENTITY_TYPE.forEach(blockEntityType -> {
            event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, blockEntityType, (blockEntity, direction) -> {
                if (blockEntity instanceof EnergyBlock<?> energyBlock) {
                    return new ForgeEnergyContainer<>(energyBlock.getEnergyStorage(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, direction));
                }
                return null;
            });
        });

        BuiltInRegistries.ITEM.stream().filter(item -> item instanceof EnergyItem<?>).forEach(item -> {
            event.registerItem(Capabilities.EnergyStorage.ITEM, (itemStack, unused) -> {
                EnergyItem<?> energyItem = (EnergyItem<?>) item;
                return new ForgeEnergyContainer<>(energyItem.getEnergyStorage(itemStack));
            }, item);
        });
    }

// TODO fluid stuff
//
//    public static void registerFluid(RegisterCapabilitiesEvent event) {
//        FluidApi.getBlockEntityRegistry().forEach((blockEntityType, blockFluidGetter1) -> {
//            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, blockEntityType, (blockEntity, direction) -> {
//                return new ForgeFluidContainer<>(blockFluidGetter1.getFluidContainer(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, direction));
//            });
//        });
//
//        FluidApi.getBlockRegistry().forEach((block, blockFluidGetter) -> {
//            event.registerBlock(Capabilities.FluidHandler.BLOCK, (level, blockPos, blockState, blockEntity, direction) -> {
//                return new ForgeFluidContainer<>(blockFluidGetter.getFluidContainer(level, blockPos, blockState, blockEntity, direction));
//            }, block);
//        });
//
//        FluidApi.getItemRegistry().forEach((item, itemFluidGetter) -> {
//            event.registerItem(Capabilities.FluidHandler.ITEM, (itemStack, unused) -> {
//                var fluidContainer = itemFluidGetter.getFluidContainer(itemStack);
//                if (fluidContainer != null) {
//                    return new ForgeItemFluidContainer<>(fluidContainer);
//                }
//                return null;
//            }, item);
//        });
//
//        BuiltInRegistries.BLOCK_ENTITY_TYPE.forEach(blockEntityType -> {
//            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, blockEntityType, (blockEntity, direction) -> {
//                if (blockEntity instanceof BotariumFluidBlock<?> fluidBlock) {
//                    return new ForgeFluidContainer<>(fluidBlock.getFluidContainer(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, direction));
//                }
//                return null;
//            });
//        });
//
//        BuiltInRegistries.BLOCK.stream().filter(block -> block instanceof BotariumFluidBlock<?>).forEach(block -> {
//            event.registerBlock(Capabilities.FluidHandler.BLOCK, (level, blockPos, blockState, blockEntity, direction) -> {
//                if (blockState.getBlock() instanceof BotariumFluidBlock<?> fluidBlock) {
//                    return new ForgeFluidContainer<>(fluidBlock.getFluidContainer(level, blockPos, blockState, blockEntity, direction));
//                }
//                return null;
//            }, block);
//        });
//
//        BuiltInRegistries.ITEM.stream().filter(item -> item instanceof BotariumFluidItem<?>).forEach(item -> {
//            event.registerItem(Capabilities.FluidHandler.ITEM, (itemStack, unused) -> {
//                BotariumFluidItem<?> fluidHoldingItem = (BotariumFluidItem<?>) item;
//                return new ForgeItemFluidContainer<>(fluidHoldingItem.getFluidContainer(itemStack));
//            }, item);
//        });
//
//        BuiltInRegistries.ITEM.stream().filter(item -> item instanceof BucketItem).forEach(item -> {
//            event.registerItem(Capabilities.FluidHandler.ITEM, (itemStack, unused) -> new FluidBucketWrapper(itemStack), item);
//        });
//    }
}