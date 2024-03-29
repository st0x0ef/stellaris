package com.st0x0ef.stellaris.common.energy;

import com.st0x0ef.stellaris.common.energy.base.BotariumEnergyBlock;
import com.st0x0ef.stellaris.common.energy.base.BotariumEnergyItem;
import com.st0x0ef.stellaris.common.energy.base.EnergyContainer;
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
    private static final Map<Supplier<BlockEntityType<?>>, BotariumEnergyBlock<?>> BLOCK_ENTITY_LOOKUP_MAP = new HashMap<>();
    private static final Map<Supplier<Block>, BotariumEnergyBlock<?>> BLOCK_LOOKUP_MAP = new HashMap<>();
    private static final Map<Supplier<Item>, BotariumEnergyItem<?>> ITEM_LOOKUP_MAP = new HashMap<>();

    private static Map<BlockEntityType<?>, BotariumEnergyBlock<?>> FINALIZED_BLOCK_ENTITY_LOOKUP_MAP = null;
    private static Map<Block, BotariumEnergyBlock<?>> FINALIZED_BLOCK_LOOKUP_MAP = null;
    private static Map<Item, BotariumEnergyItem<?>> FINALIZED_ITEM_LOOKUP_MAP = null;

    public static Map<BlockEntityType<?>, BotariumEnergyBlock<?>> getBlockEntityRegistry() {
        return FINALIZED_BLOCK_ENTITY_LOOKUP_MAP = Botarium.finalizeRegistration(BLOCK_ENTITY_LOOKUP_MAP, FINALIZED_BLOCK_ENTITY_LOOKUP_MAP);
    }

    public static Map<Block, BotariumEnergyBlock<?>> getBlockRegistry() {
        return FINALIZED_BLOCK_LOOKUP_MAP = Botarium.finalizeRegistration(BLOCK_LOOKUP_MAP, FINALIZED_BLOCK_LOOKUP_MAP);
    }

    public static Map<Item, BotariumEnergyItem<?>> getItemRegistry() {
        return FINALIZED_ITEM_LOOKUP_MAP = Botarium.finalizeRegistration(ITEM_LOOKUP_MAP, FINALIZED_ITEM_LOOKUP_MAP);
    }

    public static BotariumEnergyBlock<?> getEnergyBlock(Block block) {
        return getBlockRegistry().get(block);
    }

    public static BotariumEnergyBlock<?> getEnergyBlock(BlockEntityType<?> blockEntity) {
        return getBlockEntityRegistry().get(blockEntity);
    }

    public static BotariumEnergyItem<?> getEnergyItem(Item item) {
        return getItemRegistry().get(item);
    }

    /**
     * Retrieves the Botarium specific EnergyContainer object from a block entity or block. This method is used internally
     * by the Botarium API and should not be used by other mods.
     *
     * @param <T>       the type of EnergyContainer
     * @param level     the game level
     * @param pos       the position of the block
     * @param state     the block state
     * @param entity    the block entity (can be null)
     * @param direction the direction (can be null)
     * @return the API EnergyContainer object
     */
    public static <T extends EnergyContainer & Updatable> T getAPIEnergyContainer(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        BotariumEnergyBlock<?> getter = getEnergyBlock(state.getBlock());
        if (getter == null && entity != null) {
            getter = getEnergyBlock(entity.getType());

            if (getter == null && entity instanceof BotariumEnergyBlock<?> energyGetter) {
                getter = energyGetter;
            }

            if (getter == null && state.getBlock() instanceof BotariumEnergyBlock<?> energyGetter) {
                getter = energyGetter;
            }
        }
        if (getter == null) {
            return null;
        }
        return (T) getter.getEnergyStorage(level, pos, state, entity, direction);
    }

    public static void registerEnergyBlockEntity(Supplier<BlockEntityType<?>> block, BotariumEnergyBlock<?> getter) {
        BLOCK_ENTITY_LOOKUP_MAP.put(block, getter);
    }

    @SafeVarargs
    public static void registerEnergyBlockEntity(BotariumEnergyBlock<?> getter, Supplier<BlockEntityType<?>>... blocks) {
        for (Supplier<BlockEntityType<?>> block : blocks) {
            BLOCK_ENTITY_LOOKUP_MAP.put(block, getter);
        }
    }

    public static void registerEnergyBlock(Supplier<Block> block, BotariumEnergyBlock<?> getter) {
        BLOCK_LOOKUP_MAP.put(block, getter);
    }

    @SafeVarargs
    public static void registerEnergyBlock(BotariumEnergyBlock<?> getter, Supplier<Block>... blocks) {
        for (Supplier<Block> block : blocks) {
            BLOCK_LOOKUP_MAP.put(block, getter);
        }
    }

    public static void registerEnergyItem(Supplier<Item> item, BotariumEnergyItem<?> getter) {
        ITEM_LOOKUP_MAP.put(item, getter);
    }

    @SafeVarargs
    public static void registerEnergyItem(BotariumEnergyItem<?> getter, Supplier<Item>... items) {
        for (Supplier<Item> item : items) {
            ITEM_LOOKUP_MAP.put(item, getter);
        }
    }

    /**
     * Automatically transfers energy from an energy block to surrounding blocks
     *
     * @param energyBlock      A block entity that is an instance of {@link BotariumEnergyBlock}
     * @param extractDirection The direction to extract energy from the energy block
     * @param amount           The total amount that will be distributed as equally it can be. If one block cannot receive all the energy, it will be distributed evenly to the other blocks.
     * @return The amount of energy that was distributed
     */
    public static long distributeEnergyNearby(BlockEntity energyBlock, @Nullable Direction extractDirection, long amount) {
        return distributeEnergyNearby(energyBlock.getLevel(), energyBlock.getBlockPos(), extractDirection, amount);
    }

    /**
     * Automatically transfers energy from an energy block to surrounding blocks
     *
     * @param energyBlock A block entity that is an instance of {@link BotariumEnergyBlock}
     * @param amount      The total amount that will be distributed as equally it can be. If one block cannot receive all the energy, it will be distributed evenly to the other blocks.
     * @return The amount of energy that was distributed
     */
    public static long distributeEnergyNearby(BlockEntity energyBlock, long amount) {
        return distributeEnergyNearby(energyBlock.getLevel(), energyBlock.getBlockPos(), null, amount);
    }

    /**
     * Automatically transfers energy from an energy block to surrounding blocks
     *
     * @param level            The level of the energy block
     * @param energyPos        The position of the energy block
     * @param amount           The total amount that will be distributed as equally it can be. If one block cannot receive all the energy, it will be distributed evenly to the other blocks.
     * @param extractDirection The direction to extract energy from the energy block
     * @return The amount of energy that was distributed
     */
    public static long distributeEnergyNearby(Level level, BlockPos energyPos, @Nullable Direction extractDirection, long amount) {
        EnergyContainer internalEnergy = EnergyContainer.of(level, energyPos, extractDirection);
        long amountToDistribute = internalEnergy.extractEnergy(amount, true);
        if (amountToDistribute == 0) return 0;
        List<EnergyContainer> list = Direction.stream()
                .map(direction -> EnergyContainer.of(level, energyPos.relative(direction), direction.getOpposite()))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingLong(energy -> energy.insertEnergy(amount, true)))
                .toList();
        int receiverCount = list.size();
        for (EnergyContainer energy : list) {
            if (energy == null) continue;
            long inserted = moveEnergy(internalEnergy, energy, amountToDistribute / receiverCount, false);
            amountToDistribute -= inserted;
            receiverCount--;
        }
        return amount - amountToDistribute;
    }

    /**
     * Moves energy from one energy container to another
     *
     * @param from   The energy container to move energy from
     * @param to     The energy container to move energy to
     * @param amount The amount of energy to move
     * @return The amount of energy that was moved
     */
    public static long moveEnergy(EnergyContainer from, EnergyContainer to, long amount, boolean simulate) {
        long extracted = from.extractEnergy(amount, true);
        long inserted = to.insertEnergy(extracted, true);
        long simulatedExtraction = from.extractEnergy(inserted, true);
        if (!simulate && inserted > 0 && simulatedExtraction == inserted) {
            from.extractEnergy(inserted, false);
            to.insertEnergy(inserted, false);
        }
        return Math.max(0, inserted);
    }

    /**
     * Moves energy from one energy container to another
     *
     * @param from   The energy container to move energy from
     * @param to     The energy container to move energy to
     * @param amount The amount of energy to move
     * @return The amount of energy that was moved
     */
    public static long moveEnergy(ItemStackHolder from, ItemStackHolder to, long amount, boolean simulate) {
        EnergyContainer fromEnergy = EnergyContainer.of(from);
        EnergyContainer toEnergy = EnergyContainer.of(to);
        if (fromEnergy == null || toEnergy == null) return 0;
        return moveEnergy(fromEnergy, toEnergy, amount, simulate);
    }

    /**
     * Moves energy from one energy container to another
     *
     * @param from   The energy container to move energy from
     * @param to     The energy container to move energy to
     * @param amount The amount of energy to move
     * @return The amount of energy that was moved
     */
    public static long moveEnergy(BlockEntity from, BlockEntity to, long amount, boolean simulate) {
        EnergyContainer fromEnergy = EnergyContainer.of(from, null);
        EnergyContainer toEnergy = EnergyContainer.of(to, null);
        if (fromEnergy == null || toEnergy == null) return 0;
        return moveEnergy(fromEnergy, toEnergy, amount, simulate);
    }

    public static long moveEnergy(Level level, BlockPos fromPos, @Nullable Direction fromDirection, BlockPos toPos, @Nullable Direction toDirection, long amount, boolean simulate) {
        EnergyContainer fromEnergy = EnergyContainer.of(level, fromPos, fromDirection);
        EnergyContainer toEnergy = EnergyContainer.of(level, toPos, toDirection);
        if (fromEnergy == null || toEnergy == null) return 0;
        return moveEnergy(fromEnergy, toEnergy, amount, simulate);
    }

    /**
     * Moves energy from one energy container to another
     *
     * @param from   The energy container to move energy from
     * @param to     The energy container to move energy to
     * @param amount The amount of energy to move
     * @return The amount of energy that was moved
     */
    public static long moveEnergy(BlockEntity from, Direction direction, ItemStackHolder to, long amount, boolean simulate) {
        EnergyContainer fromEnergy = EnergyContainer.of(from, direction);
        EnergyContainer toEnergy = EnergyContainer.of(to);
        if (fromEnergy == null || toEnergy == null) return 0;
        return moveEnergy(fromEnergy, toEnergy, amount, simulate);
    }

    /**
     * Moves energy from one energy container to another
     *
     * @param from   The energy container to move energy from
     * @param to     The energy container to move energy to
     * @param amount The amount of energy to move
     * @return The amount of energy that was moved
     */
    public static long moveEnergy(ItemStackHolder from, BlockEntity to, Direction direction, long amount, boolean simulate) {
        EnergyContainer fromEnergy = EnergyContainer.of(from);
        EnergyContainer toEnergy = EnergyContainer.of(to, direction);
        if (fromEnergy == null || toEnergy == null) return 0;
        return moveEnergy(fromEnergy, toEnergy, amount, simulate);
    }
}
