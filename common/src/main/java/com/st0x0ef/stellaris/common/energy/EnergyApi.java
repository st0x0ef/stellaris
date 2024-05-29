package com.st0x0ef.stellaris.common.energy;

import com.st0x0ef.stellaris.common.energy.base.EnergyBlock;
import com.st0x0ef.stellaris.common.energy.base.EnergyContainer;
import com.st0x0ef.stellaris.common.energy.base.EnergyItem;
import com.st0x0ef.stellaris.common.energy.util.Updatable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public class EnergyApi {
    private static final Map<Supplier<BlockEntityType<?>>, EnergyBlock<?>> BLOCK_ENTITY_LOOKUP_MAP = new HashMap();
    private static final Map<Supplier<Block>, EnergyBlock<?>> BLOCK_LOOKUP_MAP = new HashMap();
    private static final Map<Supplier<Item>, EnergyItem<?>> ITEM_LOOKUP_MAP = new HashMap();
    private static Map<BlockEntityType<?>, EnergyBlock<?>> FINALIZED_BLOCK_ENTITY_LOOKUP_MAP = null;
    private static Map<Block, EnergyBlock<?>> FINALIZED_BLOCK_LOOKUP_MAP = null;
    private static Map<Item, EnergyItem<?>> FINALIZED_ITEM_LOOKUP_MAP = null;

    public EnergyApi() {
    }

    public static Map<BlockEntityType<?>, EnergyBlock<?>> getBlockEntityRegistry() {
        return FINALIZED_BLOCK_ENTITY_LOOKUP_MAP = EnergyMain.finalizeRegistration(BLOCK_ENTITY_LOOKUP_MAP, FINALIZED_BLOCK_ENTITY_LOOKUP_MAP);
    }

    public static Map<Block, EnergyBlock<?>> getBlockRegistry() {
        return FINALIZED_BLOCK_LOOKUP_MAP = EnergyMain.finalizeRegistration(BLOCK_LOOKUP_MAP, FINALIZED_BLOCK_LOOKUP_MAP);
    }

    public static Map<Item, EnergyItem<?>> getItemRegistry() {
        return FINALIZED_ITEM_LOOKUP_MAP = EnergyMain.finalizeRegistration(ITEM_LOOKUP_MAP, FINALIZED_ITEM_LOOKUP_MAP);
    }

    public static EnergyBlock<?> getEnergyBlock(Block block) {
        return getBlockRegistry().get(block);
    }

    public static EnergyBlock<?> getEnergyBlock(BlockEntityType<?> blockEntity) {
        return getBlockEntityRegistry().get(blockEntity);
    }

    public static EnergyItem<?> getEnergyItem(Item item) {
        return getItemRegistry().get(item);
    }

    public static <T extends EnergyContainer & Updatable> T getAPIEnergyContainer(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        EnergyBlock<?> getter = getEnergyBlock(state.getBlock());
        if (getter == null && entity != null) {
            getter = getEnergyBlock(entity.getType());
            EnergyBlock energyGetter;
            if (getter == null && entity instanceof EnergyBlock) {
                energyGetter = (EnergyBlock)entity;
                getter = energyGetter;
            }

            if (getter == null) {
                Block var7 = state.getBlock();
                if (var7 instanceof EnergyBlock) {
                    energyGetter = (EnergyBlock)var7;
                    getter = energyGetter;
                }
            }
        }

        return getter == null ? null : getter.getEnergyStorage(level, pos, state, entity, direction);
    }

    public static void registerEnergyBlockEntity(Supplier<BlockEntityType<?>> block, EnergyBlock<?> getter) {
        BLOCK_ENTITY_LOOKUP_MAP.put(block, getter);
    }

    @SafeVarargs
    public static void registerEnergyBlockEntity(EnergyBlock<?> getter, Supplier<BlockEntityType<?>>... blocks) {
        Supplier[] var2 = blocks;
        int var3 = blocks.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Supplier<BlockEntityType<?>> block = var2[var4];
            BLOCK_ENTITY_LOOKUP_MAP.put(block, getter);
        }

    }

    public static void registerEnergyBlock(Supplier<Block> block, EnergyBlock<?> getter) {
        BLOCK_LOOKUP_MAP.put(block, getter);
    }

    @SafeVarargs
    public static void registerEnergyBlock(EnergyBlock<?> getter, Supplier<Block>... blocks) {
        Supplier[] var2 = blocks;
        int var3 = blocks.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Supplier<Block> block = var2[var4];
            BLOCK_LOOKUP_MAP.put(block, getter);
        }

    }

    public static void registerEnergyItem(Supplier<Item> item, EnergyItem<?> getter) {
        ITEM_LOOKUP_MAP.put(item, getter);
    }

    @SafeVarargs
    public static void registerEnergyItem(EnergyItem<?> getter, Supplier<Item>... items) {
        Supplier[] var2 = items;
        int var3 = items.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Supplier<Item> item = var2[var4];
            ITEM_LOOKUP_MAP.put(item, getter);
        }

    }

    public static long distributeEnergyNearby(BlockEntity energyBlock, @Nullable Direction extractDirection, long amount) {
        return distributeEnergyNearby(energyBlock.getLevel(), energyBlock.getBlockPos(), extractDirection, amount);
    }

    public static long distributeEnergyNearby(BlockEntity energyBlock, long amount) {
        return distributeEnergyNearby(energyBlock.getLevel(), energyBlock.getBlockPos(), null, amount);
    }

    public static long distributeEnergyNearby(Level level, BlockPos energyPos, @Nullable Direction extractDirection, long amount) {
        EnergyContainer internalEnergy = EnergyContainer.of(level, energyPos, extractDirection);
        long amountToDistribute = internalEnergy.extractEnergy(amount, true);
        if (amountToDistribute == 0L) {
            return 0L;
        } else {
            List<EnergyContainer> list = Direction.stream().map((direction) -> {
                return EnergyContainer.of(level, energyPos.relative(direction), direction.getOpposite());
            }).filter(Objects::nonNull).sorted(Comparator.comparingLong((energyx) -> {
                return energyx.insertEnergy(amount, true);
            })).toList();
            int receiverCount = list.size();
            Iterator var10 = list.iterator();

            while(var10.hasNext()) {
                EnergyContainer energy = (EnergyContainer)var10.next();
                if (energy != null) {
                    long inserted = moveEnergy(internalEnergy, energy, amountToDistribute / (long)receiverCount, false);
                    amountToDistribute -= inserted;
                    --receiverCount;
                }
            }

            return amount - amountToDistribute;
        }
    }

    public static long moveEnergy(EnergyContainer from, EnergyContainer to, long amount, boolean simulate) {
        long extracted = from.extractEnergy(amount, true);
        long inserted = to.insertEnergy(extracted, true);
        long simulatedExtraction = from.extractEnergy(inserted, true);
        if (!simulate && inserted > 0L && simulatedExtraction == inserted) {
            from.extractEnergy(inserted, false);
            to.insertEnergy(inserted, false);
        }

        return Math.max(0L, inserted);
    }

    public static long moveEnergy(ItemStackHolder from, ItemStackHolder to, long amount, boolean simulate) {
        EnergyContainer fromEnergy = EnergyContainer.of(from);
        EnergyContainer toEnergy = EnergyContainer.of(to);
        return fromEnergy != null && toEnergy != null ? moveEnergy(fromEnergy, toEnergy, amount, simulate) : 0L;
    }

    public static long moveEnergy(BlockEntity from, BlockEntity to, long amount, boolean simulate) {
        EnergyContainer fromEnergy = EnergyContainer.of(from, null);
        EnergyContainer toEnergy = EnergyContainer.of(to, null);
        return fromEnergy != null && toEnergy != null ? moveEnergy(fromEnergy, toEnergy, amount, simulate) : 0L;
    }

    public static long moveEnergy(Level level, BlockPos fromPos, @Nullable Direction fromDirection, BlockPos toPos, @Nullable Direction toDirection, long amount, boolean simulate) {
        EnergyContainer fromEnergy = EnergyContainer.of(level, fromPos, fromDirection);
        EnergyContainer toEnergy = EnergyContainer.of(level, toPos, toDirection);
        return fromEnergy != null && toEnergy != null ? moveEnergy(fromEnergy, toEnergy, amount, simulate) : 0L;
    }

    public static long moveEnergy(BlockEntity from, Direction direction, ItemStackHolder to, long amount, boolean simulate) {
        EnergyContainer fromEnergy = EnergyContainer.of(from, direction);
        EnergyContainer toEnergy = EnergyContainer.of(to);
        return fromEnergy != null && toEnergy != null ? moveEnergy(fromEnergy, toEnergy, amount, simulate) : 0L;
    }

    public static long moveEnergy(ItemStackHolder from, BlockEntity to, Direction direction, long amount, boolean simulate) {
        EnergyContainer fromEnergy = EnergyContainer.of(from);
        EnergyContainer toEnergy = EnergyContainer.of(to, direction);
        return fromEnergy != null && toEnergy != null ? moveEnergy(fromEnergy, toEnergy, amount, simulate) : 0L;
    }
}
