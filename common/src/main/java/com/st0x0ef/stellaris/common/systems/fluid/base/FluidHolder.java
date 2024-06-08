package com.st0x0ef.stellaris.common.systems.fluid.base;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.platform.systems.fluid.FluidConstants;
import com.st0x0ef.stellaris.platform.systems.fluid.FluidUtilHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * An object that holds a fluid with an amount and a tag.
 * Similar to {@link net.minecraft.world.item.ItemStack}, but for fluids.
 */
public interface FluidHolder {

    Codec<FluidHolder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.FLUID.byNameCodec().fieldOf("fluid").forGetter(FluidHolder::getFluid),
            Codec.LONG.fieldOf("millibuckets").orElse(1000L).forGetter(fluidHolder -> FluidConstants.toMillibuckets(fluidHolder.getFluidAmount())),
            CompoundTag.CODEC.optionalFieldOf("tag").forGetter(fluidHolder -> Optional.ofNullable(fluidHolder.getDataComponentPatch()))
    ).apply(instance, (fluid, millibuckets, compoundTag) -> FluidHolder.of(fluid, FluidConstants.fromMillibuckets(millibuckets), compoundTag.orElse(null))));

    /**
     * Creates a FluidHolder with the given Fluid and a default of 1 bucket.
     *
     * @param fluid The Fluid to set in the holder.
     * @return The FluidHolder with the specified Fluid.
     */
    static FluidHolder of(Fluid fluid) {
        return of(fluid, FluidConstants.getBucketAmount());
    }

    /**
     * Creates a FluidHolder with the specified Fluid and amount.
     *
     * @param fluid  The Fluid to set in the holder.
     * @param amount The amount of fluid to set in the holder.
     * @return The created FluidHolder.
     */
    static FluidHolder of(Fluid fluid, long amount) {
        return of(fluid, amount, null);
    }

    /**
     * Creates a FluidHolder with the specified Fluid, amount, and CompoundTag.
     *
     * @param fluid  The Fluid to set in the holder.
     * @param amount The amount of fluid to set in the holder.
     * @param dataComponentPatch The CompoundTag to set in the holder. IDK man
     * @return The created FluidHolder.
     */
    static FluidHolder of(Fluid fluid, long amount, @Nullable DataComponentPatch dataComponentPatch) {
        return FluidUtilHelper.of(fluid,amount,dataComponentPatch);
    }

    static FluidHolder ofMillibuckets(Fluid fluid, long millibuckets, @Nullable DataComponentPatch dataComponentPatch) {
        return FluidHolder.of(fluid, FluidConstants.fromMillibuckets(millibuckets), dataComponentPatch);
    }

    static FluidHolder ofMillibuckets(Fluid fluid, long millibuckets) {
        return FluidHolder.of(fluid, FluidConstants.fromMillibuckets(millibuckets), null);
    }

    /**
     * Creates a FluidHolder from a CompoundTag.
     *
     * @param tag The CompoundTag to create the FluidHolder from.
     * @return The created FluidHolder.
     */
    static FluidHolder fromCompound(CompoundTag tag) {
        FluidHolder fluid = of(Fluids.EMPTY, 0);
        fluid.deserialize(tag);
        return fluid;
    }

    /**
     * Returns an empty FluidHolder.
     *
     * @return The empty FluidHolder.
     */
    static FluidHolder empty() {
        return FluidUtilHelper.empty();
    }

    default void writeToBuffer(FriendlyByteBuf buffer) {
        buffer.writeOptional(Optional.ofNullable(this.isEmpty() ? null : this), (friendlyByteBuf, fluid) -> {
            friendlyByteBuf.writeVarInt(BuiltInRegistries.FLUID.getId(fluid.getFluid()));
            friendlyByteBuf.writeVarLong(fluid.getFluidAmount());
            friendlyByteBuf.writeNbt(fluid.getDataComponentPatch());
        });
    }

    static FluidHolder readFromBuffer(FriendlyByteBuf buffer) {
        return buffer.readOptional((friendlyByteBuf) -> {
            Fluid fluid = BuiltInRegistries.FLUID.byId(friendlyByteBuf.readVarInt());
            long amount = friendlyByteBuf.readVarLong();
            CompoundTag tag = friendlyByteBuf.readNbt();
            return FluidHolder.of(fluid, amount, tag);
        }).orElse(FluidHolder.empty());
    }

    /**
     * @return The {@link Fluid} in the holder.
     */
    Fluid getFluid();

    /**
     * Sets the {@link Fluid} in the holder.
     *
     * @param fluid The {@link Fluid} to set in the holder.
     */
    void setFluid(Fluid fluid);

    /**
     * @return The amount of fluid in the holder.
     */
    long getFluidAmount();

    /**
     * @return The amount of fluid in the holder in millibuckets.
     */
    default long getMillibuckets() {
        return FluidConstants.toMillibuckets(getFluidAmount());
    }

    /**
     * Sets the amount of fluid in the holder.
     *
     * @param amount The amount of fluid to set in the holder.
     */
    void setAmount(long amount);

    /**
     * @return The {@link CompoundTag} in the holder.
     */
    DataComponentPatch getDataComponentPatch();

    /**
     * Sets the {@link CompoundTag} in the holder.
     *
     * @param tag The {@link CompoundTag} to set in the holder.
     */
    void setCompound(CompoundTag tag);

    /**
     * @return True if the holder is empty, false otherwise.
     */
    boolean isEmpty();

    /**
     * Compares the {@link FluidHolder} to another {@link FluidHolder} ignoring the amount.
     *
     * @param fluidHolder The {@link FluidHolder} to compare to.
     * @return True if the given holder is equal to this holder, false otherwise.
     */
    boolean matches(FluidHolder fluidHolder);

    /**
     * @return A copy of the {@link FluidHolder}.
     */
    FluidHolder copyHolder();

    /**
     * @return A serialized version of the {@link FluidHolder} into a {@link CompoundTag}.
     */
    CompoundTag serialize();

    /**
     * Deserializes a {@link FluidHolder} from a given {@link CompoundTag}.
     *
     * @param tag The {@link CompoundTag} to deserialize from.
     */
    void deserialize(CompoundTag tag);

    /**
     * Does not set amount if the holder is empty.
     *
     * @param amount The amount to set in the copy.
     * @return A copy of the {@link FluidHolder} with the given amount.
     */
    default FluidHolder copyWithAmount(long amount) {
        FluidHolder copy = copyHolder();
        if (!copy.isEmpty()) copy.setAmount(amount);
        return copy;
    }

    @SuppressWarnings("deprecation")
    default Holder<Fluid> getFluidHolder() {
        return getFluid().builtInRegistryHolder();
    }

    @SuppressWarnings("deprecation")
    default boolean is(TagKey<Fluid> tagKey) {
        return getFluid().is(tagKey);
    }

    default boolean is(Fluid fluid) {
        return getFluid() == fluid;
    }

    default boolean is(Predicate<Holder<Fluid>> predicate) {
        return predicate.test(getFluidHolder());
    }

    default boolean is(Holder<Fluid> fluid) {
        return getFluidHolder() == fluid;
    }

}
