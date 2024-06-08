package com.st0x0ef.stellaris.common.systems.fluid;

import com.st0x0ef.stellaris.common.systems.SystemsMain;
import com.st0x0ef.stellaris.common.systems.fluid.base.FluidBlock;
import com.st0x0ef.stellaris.common.systems.fluid.base.FluidContainer;
import com.st0x0ef.stellaris.common.systems.fluid.base.FluidHolder;
import com.st0x0ef.stellaris.common.systems.fluid.base.FluidItem;
import com.st0x0ef.stellaris.common.systems.item.ItemStackHolder;
import com.st0x0ef.stellaris.common.systems.util.Updatable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class FluidApi {

    // region Fluid Container Registration
    private static final Map<Supplier<BlockEntityType<?>>, FluidBlock<?>> BLOCK_ENTITY_LOOKUP_MAP = new HashMap<>();
    private static final Map<Supplier<Block>, FluidBlock<?>> BLOCK_LOOKUP_MAP = new HashMap<>();
    private static final Map<Supplier<Item>, FluidItem<?>> ITEM_LOOKUP_MAP = new HashMap<>();

    private static Map<BlockEntityType<?>, FluidBlock<?>> FINALIZED_BLOCK_ENTITY_LOOKUP_MAP = null;
    private static Map<Block, FluidBlock<?>> FINALIZED_BLOCK_LOOKUP_MAP = null;
    private static Map<Item, FluidItem<?>> FINALIZED_ITEM_LOOKUP_MAP = null;

    public static Map<BlockEntityType<?>, FluidBlock<?>> getBlockEntityRegistry() {
        return FINALIZED_BLOCK_ENTITY_LOOKUP_MAP = SystemsMain.finalizeRegistration(BLOCK_ENTITY_LOOKUP_MAP, FINALIZED_BLOCK_ENTITY_LOOKUP_MAP);
    }

    public static Map<Block, FluidBlock<?>> getBlockRegistry() {
        return FINALIZED_BLOCK_LOOKUP_MAP = SystemsMain.finalizeRegistration(BLOCK_LOOKUP_MAP, FINALIZED_BLOCK_LOOKUP_MAP);
    }

    public static Map<Item, FluidItem<?>> getItemRegistry() {
        return FINALIZED_ITEM_LOOKUP_MAP = SystemsMain.finalizeRegistration(ITEM_LOOKUP_MAP, FINALIZED_ITEM_LOOKUP_MAP);
    }

    public static FluidBlock<?> getFluidBlock(Block block) {
        return getBlockRegistry().get(block);
    }

    public static FluidBlock<?> getFluidBlock(BlockEntityType<?> blockEntity) {
        return getBlockEntityRegistry().get(blockEntity);
    }

    public static FluidItem<?> getFluidItem(Item item) {
        return getItemRegistry().get(item);
    }

    /**
     * Retrieves the Botarium specific FluidContainer object from a block entity or block. This method is used internally
     * by the Botarium API and should not be used by other mods.
     *
     * @param <T>       the type of FluidContainer
     * @param level     the game level
     * @param pos       the position of the block
     * @param state     the block state
     * @param entity    the block entity (can be null)
     * @param direction the direction (can be null)
     * @return the API FluidContainer object
     */
    public static <T extends FluidContainer & Updatable> T getAPIFluidContainer(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        FluidBlock<?> getter = getFluidBlock(state.getBlock());
        if (getter == null && entity != null) {
            getter = getFluidBlock(entity.getType());

            if (getter == null && entity instanceof FluidBlock<?> fluidGetter) {
                getter = fluidGetter;
            }

            if (getter == null && state.getBlock() instanceof FluidBlock<?> fluidGetter) {
                getter = fluidGetter;
            }
        }
        if (getter == null) return null;
        return (T) getter.getFluidContainer(level, pos, state, entity, direction);
    }

    public static void registerFluidBlockEntity(Supplier<BlockEntityType<?>> block, FluidBlock<?> getter) {
        BLOCK_ENTITY_LOOKUP_MAP.put(block, getter);
    }

    @SafeVarargs
    public static void registerFluidBlockEntity(FluidBlock<?> getter, Supplier<BlockEntityType<?>>... blocks) {
        for (Supplier<BlockEntityType<?>> block : blocks) {
            BLOCK_ENTITY_LOOKUP_MAP.put(block, getter);
        }
    }

    public static void registerFluidBlock(Supplier<Block> block, FluidBlock<?> getter) {
        BLOCK_LOOKUP_MAP.put(block, getter);
    }

    @SafeVarargs
    public static void registerFluidBlock(FluidBlock<?> getter, Supplier<Block>... blocks) {
        for (Supplier<Block> block : blocks) {
            BLOCK_LOOKUP_MAP.put(block, getter);
        }
    }

    public static void registerFluidItem(Supplier<Item> item, FluidItem<?> getter) {
        ITEM_LOOKUP_MAP.put(item, getter);
    }

    @SafeVarargs
    public static void registerFluidItem(FluidItem<?> getter, Supplier<Item>... items) {
        for (Supplier<Item> item : items) {
            ITEM_LOOKUP_MAP.put(item, getter);
        }
    }

    /**
     * Moves energy from one energy container to another
     *
     * @param from   The energy container to move energy from
     * @param to     The energy container to move energy to
     * @param amount The amount of energy to move
     * @return The amount of energy that was moved
     */
    public static long moveFluid(FluidContainer from, FluidContainer to, FluidHolder amount, boolean simulate) {
        FluidHolder extracted = from.extractFluid(amount, true);
        long inserted = to.insertFluid(extracted, true);
        FluidHolder toInsert = amount.copyWithAmount(inserted);
        FluidHolder simulatedExtraction = from.extractFluid(toInsert, true);
        if (!simulate && inserted > 0 && simulatedExtraction.getFluidAmount() == inserted) {
            from.extractFluid(toInsert, false);
            to.insertFluid(toInsert, false);
        }
        return Math.max(0, inserted);
    }

    /**
     * Moves energy from one energy container to another
     *
     * @param from        The energy container to move energy from
     * @param to          The energy container to move energy to
     * @param fluidHolder The fluidHolder to move
     * @return The amount of fluid that was moved
     */
    public static long moveFluid(ItemStackHolder from, ItemStackHolder to, FluidHolder fluidHolder, boolean simulate) {
        FluidContainer fromFluid = FluidContainer.of(from);
        FluidContainer toFluid = FluidContainer.of(to);
        if (fromFluid == null || toFluid == null) return 0;
        return moveFluid(fromFluid, toFluid, fluidHolder, simulate);
    }

    /**
     * Moves energy from one energy container to another
     *
     * @param from        The energy container to move energy from
     * @param to          The energy container to move energy to
     * @param fluidHolder The amount of energy to move
     * @return The amount of energy that was moved
     */
    public static long moveFluid(BlockEntity from, BlockEntity to, FluidHolder fluidHolder, boolean simulate) {
        FluidContainer fromFluid = FluidContainer.of(from, null);
        FluidContainer toFluid = FluidContainer.of(to, null);
        if (fromFluid == null || toFluid == null) return 0;
        return moveFluid(fromFluid, toFluid, fluidHolder, simulate);
    }

    public static long moveFluid(Level level, BlockPos fromPos, @Nullable Direction fromDirection, BlockPos toPos, @Nullable Direction toDirection, FluidHolder fluidHolder, boolean simulate) {
        FluidContainer fromFluid = FluidContainer.of(level, fromPos, fromDirection);
        FluidContainer toFluid = FluidContainer.of(level, toPos, toDirection);
        if (fromFluid == null || toFluid == null) return 0;
        return moveFluid(fromFluid, toFluid, fluidHolder, simulate);
    }

    /**
     * Moves energy from one energy container to another
     *
     * @param from        The energy container to move energy from
     * @param to          The energy container to move energy to
     * @param fluidHolder The amount of energy to move
     * @return The amount of energy that was moved
     */
    public static long moveFluid(BlockEntity from, Direction direction, ItemStackHolder to, FluidHolder fluidHolder, boolean simulate) {
        FluidContainer fromFluid = FluidContainer.of(from, direction);
        FluidContainer toFluid = FluidContainer.of(to);
        if (fromFluid == null || toFluid == null) return 0;
        return moveFluid(fromFluid, toFluid, fluidHolder, simulate);
    }

    /**
     * Moves energy from one energy container to another
     *
     * @param from        The energy container to move energy from
     * @param to          The energy container to move energy to
     * @param fluidHolder The amount of energy to move
     * @return The amount of energy that was moved
     */
    public static long moveFluid(ItemStackHolder from, BlockEntity to, Direction direction, FluidHolder fluidHolder, boolean simulate) {
        FluidContainer fromFluid = FluidContainer.of(from);
        FluidContainer toFluid = FluidContainer.of(to, direction);
        if (fromFluid == null || toFluid == null) return 0;
        return moveFluid(fromFluid, toFluid, fluidHolder, simulate);
    }
}
