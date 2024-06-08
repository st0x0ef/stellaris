package com.st0x0ef.stellaris.common.systems.fluid.base;

import com.st0x0ef.stellaris.common.systems.item.ItemStackHolder;
import com.st0x0ef.stellaris.common.systems.util.Serializable;
import com.st0x0ef.stellaris.platform.systems.fluid.FluidUtilHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Clearable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings({"BooleanMethodIsAlwaysInverted", "DataFlowIssue"})
public interface FluidContainer extends Serializable, Clearable {

    /**
     * Retrieves an instance of FluidContainer for the given level, position, state, entity, and direction.
     * This method can be used to retrieve FluidContainers from Botarium and any other mod that uses the modloader's Fluid API.
     *
     * @param level     The level of the FluidContainer.
     * @param pos       The position of the FluidContainer.
     * @param state     The block state of the FluidContainer.
     * @param entity    The block entity associated with the FluidContainer (can be null).
     * @param direction The direction of the FluidContainer (can be null).
     * @return An instance of FluidContainer. Returns null if the block does not hold fluid.
     */
    @Nullable
    static FluidContainer of(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return FluidUtilHelper.of(level,pos,state,entity,direction);
    }

    /**
     * Retrieves an instance of FluidContainer for the given level, position, and direction.
     * This method can be used to retrieve FluidContainers from Botarium and any other mod that uses the modloader's Fluid API.
     *
     * @param level     The level of the FluidContainer.
     * @param pos       The position of the FluidContainer.
     * @param direction The direction of the FluidContainer (can be null).
     * @return An instance of FluidContainer. Returns null if the block does not hold fluid.
     */
    @Nullable
    static FluidContainer of(Level level, BlockPos pos, @Nullable Direction direction) {
        return FluidContainer.of(level, pos, null, null, direction);
    }

    /**
     * Retrieves an instance of FluidContainer for the given block entity and direction.
     * This method can be used to retrieve FluidContainers from Botarium and any other mod that uses the modloader's Fluid API.
     *
     * @param block     The block entity associated with the FluidContainer.
     * @param direction The direction of the FluidContainer (can be null).
     * @return An instance of FluidContainer. Returns null if the block does not hold fluid.
     */
    @Nullable
    static FluidContainer of(BlockEntity block, @Nullable Direction direction) {
        return FluidContainer.of(block.getLevel(), block.getBlockPos(), block.getBlockState(), block, direction);
    }

    /**
     * Retrieves an instance of FluidContainer for the given item stack.
     * This method can be used to retrieve FluidContainers from Botarium and any other mod that uses the modloader's Fluid API.
     *
     * @param holder The item stack associated with the FluidContainer.
     * @return An instance of FluidContainer. Returns null if the item does not hold fluid.
     */
    @Nullable
    static ItemFluidContainer of(ItemStackHolder holder) {
        return FluidUtilHelper.of(holder);
    }

    /**
     * Checks if the given item stack holds fluid.
     *
     * @param stack The item stack to check.
     * @return Whether the given item stack holds fluid.
     */
    static boolean holdsFluid(ItemStack stack) {
        return FluidUtilHelper.holdsFluid(stack);
    }

    /**
     * Checks if the given block holds fluid.
     *
     * @param level     The level of the block.
     * @param pos       The position of the block.
     * @param state     The block state of the block.
     * @param entity    The block entity associated with the block (can be null).
     * @param direction The direction of the block (can be null).
     * @return Whether the given block holds fluid.
     */
    static boolean holdsFluid(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return FluidUtilHelper.holdsFluid(level,pos,state,entity,direction);
    }

    /**
     * Checks if the given block holds fluid.
     *
     * @param level     The level of the block.
     * @param pos       The position of the block.
     * @param direction The direction of the block (can be null).
     * @return Whether the given block holds fluid.
     */
    static boolean holdsFluid(Level level, BlockPos pos, @Nullable Direction direction) {
        return FluidContainer.holdsFluid(level, pos, null, null, direction);
    }

    /**
     * Checks if the given block entity holds fluid.
     *
     * @param block     The block entity to check.
     * @param direction The direction of the block entity (can be null).
     * @return Whether the given block entity holds fluid.
     */
    static boolean holdsFluid(BlockEntity block, @Nullable Direction direction) {
        return FluidContainer.holdsFluid(block.getLevel(), block.getBlockPos(), block.getBlockState(), block, direction);
    }

    /**
     * Inserts a {@link FluidHolder} into the container.
     *
     * @param fluid    The {@link FluidHolder} to be inserted into the container.
     * @param simulate If true, the container will not be modified.
     * @return The amount of fluid that was added to the container.
     */
    long insertFluid(FluidHolder fluid, boolean simulate);

    /**
     * An internal version of {@link #insertFluid(FluidHolder, boolean)} that is used by mod authors
     * looking to directly interact with their own containers.
     * <p>
     * You should not call this method for other mod's containers, instead use {@link #insertFluid(FluidHolder, boolean)}.
     */
    default long internalInsert(FluidHolder fluids, boolean simulate) {
        return insertFluid(fluids, simulate);
    }

    /**
     * Extracts a {@link FluidHolder} from the container.
     *
     * @param fluid    The {@link FluidHolder} to be extracted from the container.
     * @param simulate If true, the container will not be modified.
     * @return The {@link FluidHolder} that was extracted from the container.
     */
    FluidHolder extractFluid(FluidHolder fluid, boolean simulate);

    /**
     * An internal version of {@link #extractFluid(FluidHolder, boolean)} that is used by mod authors
     * looking to directly interact with their own containers.
     * <p>
     * You should not call this method for other mod's containers, instead use {@link #extractFluid(FluidHolder, boolean)}.
     */
    default FluidHolder internalExtract(FluidHolder fluid, boolean simulate) {
        return extractFluid(fluid, simulate);
    }

    /**
     * Sets a given {@link FluidHolder} to a slot in the container.
     *
     * @param slot  The slot to set the fluid in.
     * @param fluid The {@link FluidHolder} to set in the slot.
     */
    void setFluid(int slot, FluidHolder fluid);

    /**
     * @return A {@link List} of {@link FluidHolder} in the container.
     */
    List<FluidHolder> getFluids();

    /**
     * @return The amount of slots in the container.
     */
    int getSize();

    /**
     * @return Whether or not the container is empty.
     */
    boolean isEmpty();

    /**
     * @return A copy of the container.
     */
    FluidContainer copy();

    /**
     * @param tankSlot The slot to get the capacity of.
     * @return The capacity of the given slot.
     */
    long getTankCapacity(int tankSlot);

    /**
     * Sets the container to the same values as the given container.
     *
     * @param container The container to copy the fluids from.
     */
    void fromContainer(FluidContainer container);

    /**
     * Extracts a fluid from one {@link FluidHolder} into another.
     * This is deprecated, please use {@link #extractFromSlot(int slot, FluidHolder toExtract, boolean simulate)} instead.
     *
     * @param fluidHolder The {@link FluidHolder} to extract from.
     * @param toExtract   The {@link FluidHolder} to insert into. With amount clamped between 0-fluid.getFluidAmount().
     * @param snapshot    A runnable that will be called before the extraction happens.
     * @return The amount of fluid that was extracted.
     */
    @Deprecated(forRemoval = true)
    long extractFromSlot(FluidHolder fluidHolder, FluidHolder toExtract, Runnable snapshot);

    default long extractFromSlot(int slot, FluidHolder toExtract, boolean simulate) {
        return extractFromSlot(getFluids().get(slot), toExtract, () -> {
        });
    }

    /**
     * @return Whether can be inserted into.
     */
    boolean allowsInsertion();

    /**
     * @return Whether can be extracted from.
     */
    boolean allowsExtraction();

    /**
     * @param fluidHolder The {@link FluidHolder} to check if can be inserted into.
     * @return Whether the given {@link FluidHolder} can be inserted into.
     */
    default boolean isFluidValid(int slot, FluidHolder fluidHolder) {
        return allowsInsertion();
    }

    /**
     * @return A {@link FluidSnapshot} of the given state of the {@link FluidContainer}.
     */
    FluidSnapshot createSnapshot();

    /**
     * Sets the {@link FluidContainer} with the given state of {@link FluidSnapshot}.
     *
     * @param snapshot The {@link FluidSnapshot} to set the {@link FluidContainer} to.
     */
    default void readSnapshot(FluidSnapshot snapshot) {
        snapshot.loadSnapshot(this);
    }

    default FluidHolder getFirstFluid() {
        return getFluids().get(0);
    }
}
