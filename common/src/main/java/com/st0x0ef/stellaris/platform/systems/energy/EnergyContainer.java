package com.st0x0ef.stellaris.platform.systems.energy;

import com.st0x0ef.stellaris.common.systems.energy.base.EnergySnapshot;
import com.st0x0ef.stellaris.common.systems.item.ItemStackHolder;
import com.st0x0ef.stellaris.common.systems.util.Serializable;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Clearable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

public interface EnergyContainer extends Serializable, Clearable {

    /**
     * Retrieves an instance of EnergyContainer for the given level, position, state, entity, and direction.
     * This method can be used to retrieve EnergyContainers from Botarium and any other mod that uses the modloader's Energy API.
     *
     * @param level     The level of the EnergyContainer.
     * @param pos       The position of the EnergyContainer.
     * @param state     The block state of the EnergyContainer.
     * @param entity    The block entity associated with the EnergyContainer (can be null).
     * @param direction The direction of the EnergyContainer (can be null).
     * @return An instance of EnergyContainer. Returns null if the block does not hold energy.
     */
    @Nullable
    static EnergyContainer of(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return EnergyContainerHelper.of(level,pos,state,entity,direction);
    }

    /**
     * Retrieves an instance of EnergyContainer for the given level, position, and direction.
     * This method can be used to retrieve EnergyContainers from Botarium and any other mod that uses the modloader's Energy API.
     *
     * @param level     The level of the EnergyContainer.
     * @param pos       The position of the EnergyContainer.
     * @param direction The direction of the EnergyContainer (can be null).
     * @return An instance of EnergyContainer. Returns null if the block does not hold energy.
     */
    @Nullable
    static EnergyContainer of(Level level, BlockPos pos, @Nullable Direction direction) {
        return EnergyContainer.of(level, pos, null, null, direction);
    }

    /**
     * Retrieves an instance of EnergyContainer for the given block entity and direction.
     * This method can be used to retrieve EnergyContainers from Botarium and any other mod that uses the modloader's Energy API.
     *
     * @param block     The block entity associated with the EnergyContainer.
     * @param direction The direction of the EnergyContainer (can be null).
     * @return An instance of EnergyContainer. Returns null if the block does not hold energy.
     */
    @Nullable
    static EnergyContainer of(BlockEntity block, @Nullable Direction direction) {
        return EnergyContainer.of(block.getLevel(), block.getBlockPos(), block.getBlockState(), block, direction);
    }

    /**
     * Retrieves an instance of EnergyContainer for the given item stack holder.
     * On Fabric, the item stack holder's internal stack will be mutated, and you should replace the item stack
     * inside the container inside with the stack from {@link ItemStackHolder#getStack()}.
     * This method can be used to retrieve EnergyContainers from Botarium and any other mod that uses the modloader's Energy API.
     *
     * @param holder The item stack holder.
     * @return An instance of EnergyContainer. Returns null if the item stack does not hold energy.
     */
    @Nullable
    static EnergyContainer of(ItemStackHolder holder) {
        return EnergyContainerHelper.of(holder);
    }

    /**
     * Checks if an ItemStack holds energy.
     * This method can be used to check ItemStacks from Botarium and any other mod that uses the modloader's Energy API.
     *
     * @param stack The ItemStack to check.
     * @return True if the ItemStack holds energy, false otherwise.
     */
    static boolean holdsEnergy(ItemStack stack) {
        return EnergyContainerHelper.holdsEnergy(stack);
    }

    /**
     * Checks if the given block at the specified position and state holds energy.
     * This method can be used to check blocks from Botarium and any other mod that uses the modloader's Energy API.
     * <p>
     * NOTE: While Fabric and NeoForge support attaching energy to blocks, MinecraftForge does not. This means that while Botarium
     * does support energy blocks on Fabric and NeoForge, it does not support them on Forge, and this method will always
     * return false on a block without a block entity on MinecraftForge.
     *
     * @param level     The level of the block.
     * @param pos       The position of the block.
     * @param state     The block state.
     * @param entity    The block entity associated with the block (can be null).
     * @param direction The direction of the block (can be null).
     * @return True if the block holds energy, false otherwise.
     */
    static boolean holdsEnergy(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return EnergyContainerHelper.holdsEnergy(level,pos,state,entity,direction);
    }

    /**
     * Checks if the given block at the specified position and state holds energy.
     * This method can be used to check blocks from Botarium and any other mod that uses the modloader's Energy API.
     * <p>
     * NOTE: While Fabric and NeoForge support attaching energy to blocks, MinecraftForge does not. This means that while Botarium
     * does support energy blocks on Fabric and NeoForge, it does not support them on Forge, and this method will always
     * return false on a block without a block entity on MinecraftForge.
     *
     * @param level     The level of the block.
     * @param pos       The position of the block.
     * @param direction The direction of the block (can be null).
     * @return True if the block holds energy, false otherwise.
     */
    static boolean holdsEnergy(Level level, BlockPos pos, @Nullable Direction direction) {
        return EnergyContainer.holdsEnergy(level, pos, null, null, direction);
    }

    /**
     * Checks if the given block entity holds energy.
     * This method can be used to check block entities from Botarium and any other mod that uses the modloader's Energy API.
     *
     * @param block     The block entity to check.
     * @param direction The direction of the block entity (can be null).
     * @return True if the block entity holds energy, false otherwise.
     */
    static boolean holdsEnergy(BlockEntity block, @Nullable Direction direction) {
        return EnergyContainer.holdsEnergy(block.getLevel(), block.getBlockPos(), block.getBlockState(), block, direction);
    }

    /**
     * Inserts a given amount of energy into the container.
     *
     * @param maxAmount The amount to be inserted into the container.
     * @param simulate  If true, the container will not be modified.
     * @return The amount of energy that was added to the container.
     */
    long insertEnergy(long maxAmount, boolean simulate);

    /**
     * An internal version of {@link #insertEnergy(long, boolean)} that is used by mod authors
     * looking to directly interact with their own containers.
     * <p>
     * You should not call this method for other mod's containers, instead use {@link #insertEnergy(long, boolean)}.
     */
    default long internalInsert(long amount, boolean simulate) {
        return insertEnergy(amount, simulate);
    }

    /**
     * Extracts a given amount of energy into the container.
     *
     * @param maxAmount The amount to be extracted from the container.
     * @param simulate  If true, the container will not be modified.
     * @return The amount of energy that was removed from the container.
     */
    long extractEnergy(long maxAmount, boolean simulate);

    /**
     * An internal version of {@link #extractEnergy(long, boolean)} that is used by mod authors
     * looking to directly interact with their own containers.
     * <p>
     * You should not call this method for other mod's containers, instead use {@link #extractEnergy(long, boolean)}.
     */
    default long internalExtract(long amount, boolean simulate) {
        return extractEnergy(amount, simulate);
    }

    /**
     * Sets a given amount of energy in the container.
     *
     * @param energy The amount of energy to set in the container.
     */
    void setEnergy(long energy);

    /**
     * @return The amount of energy in the container.
     */
    long getStoredEnergy();

    /**
     * @return The maximum amount of energy that can be stored in the container.
     */
    long getMaxCapacity();

    /**
     * @return The maximum amount of energy that can be inserted into the container at a time.
     */
    long maxInsert();

    /**
     * @return The maximum amount of energy that can be extracted from the container at a time.
     */
    long maxExtract();

    /**
     * @return Whether the container allows for energy to be inserted.
     */
    boolean allowsInsertion();

    /**
     * @return Whether the container allows for energy to be extracted.
     */
    boolean allowsExtraction();

    /**
     * @return A {@link EnergySnapshot} of the given state of the {@link EnergyContainer}.
     */
    EnergySnapshot createSnapshot();

    /**
     * Sets the {@link EnergyContainer} with the given state of {@link EnergySnapshot}.
     *
     * @param snapshot The {@link EnergySnapshot} to set the {@link EnergyContainer} to.
     */
    default void readSnapshot(EnergySnapshot snapshot) {
        snapshot.loadSnapshot(this);
    }

}
