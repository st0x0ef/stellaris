package com.st0x0ef.stellaris.platform.systems.core.storage.util;

import com.st0x0ef.stellaris.platform.systems.resources.Resource;
import com.st0x0ef.stellaris.platform.systems.resources.ResourceStack;
import com.st0x0ef.stellaris.platform.systems.resources.fluid.FluidResource;
import com.st0x0ef.stellaris.platform.systems.resources.fluid.ingredient.SizedFluidIngredient;
import com.st0x0ef.stellaris.platform.systems.resources.item.ItemResource;
import com.st0x0ef.stellaris.platform.systems.core.storage.base.*;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class TransferUtil {
    public static <T extends Resource> Optional<T> findResource(CommonStorage<T> container, Predicate<T> predicate) {
        for (int i = 0; i < container.size(); i++) {
            StorageSlot<T> slot = container.get(i);
            if (slot.getResource().isBlank()) continue;
            T resource = slot.getResource();
            if (predicate.test(resource)) {
                return Optional.of(resource);
            }
        }
        return Optional.empty();
    }

    public static <T extends Resource> Optional<T> findFirstResource(CommonStorage<T> container) {
        return findResource(container, resource -> true);
    }

    public static Predicate<ItemResource> byItemTag(TagKey<Item> tag) {
        return resource -> resource.is(tag);
    }

    public static Predicate<ItemResource> byIngredient(Ingredient ingredient) {
        return resource -> ingredient.test(resource.getCachedStack());
    }

    public static Predicate<FluidResource> byFluidTag(TagKey<Fluid> tag) {
        return resource -> resource.is(tag);
    }

    public static <T> long move(StorageIO<T> from, StorageIO<T> to, T resource, long amount, boolean simulate) {
        long extracted = from.extract(resource, amount, true);
        long inserted = to.insert(resource, extracted, true);
        if (extracted > 0 && inserted > 0) {
            if (inserted != extracted) {
                extracted = from.extract(resource, Math.min(extracted, inserted), true);
                inserted = to.insert(resource, Math.min(extracted, inserted), true);
            }
            if (extracted == inserted) {
                from.extract(resource, extracted, simulate);
                to.insert(resource, extracted, simulate);
                UpdateManager.batch(from, to);
                return extracted;
            }
        }
        return 0;
    }

    public static <T extends Resource> Tuple<T, Long> moveFiltered(CommonStorage<T> from, StorageIO<T> to, Predicate<T> filter, long amount, boolean simulate) {
        Optional<T> optional = findResource(from, filter);
        if (optional.isPresent()) {
            T resource = optional.get();
            long moved = move(from, to, resource, amount, simulate);
            return new Tuple<>(resource, moved);
        }
        return new Tuple<>(null, 0L);
    }

    public static <T extends Resource> Tuple<T, Long> moveAny(CommonStorage<T> from, StorageIO<T> to, long amount, boolean simulate) {
        for (int i = 0; i < from.size(); i++) {
            StorageSlot<T> slot = from.get(i);
            if (slot.getResource().isBlank()) continue;
            T resource = slot.getResource();
            long moved = move(from, to, resource, amount, simulate);
            if (moved > 0) {
                return new Tuple<>(resource, moved);
            }
        }
        return new Tuple<>(null, 0L);
    }

    public static <T extends Resource> void moveAll(CommonStorage<T> from, StorageIO<T> to, boolean simulate) {
        for (int i = 0; i < from.size(); i++) {
            StorageSlot<T> slot = from.get(i);
            if (slot.getResource().isBlank()) continue;
            T resource = slot.getResource();
            move(from, to, resource, Long.MAX_VALUE, simulate);
        }
    }

    public static long moveValue(ValueStorage from, ValueStorage to, long amount, boolean simulate) {
        long extracted = from.extract(amount, true);
        long inserted = to.insert(extracted, true);
        if (extracted > 0 && inserted > 0) {
            if (inserted != extracted) {
                extracted = from.extract(Math.min(extracted, inserted), true);
                inserted = to.insert(Math.min(extracted, inserted), true);
            }
            if (extracted == inserted) {
                from.extract(extracted, simulate);
                to.insert(extracted, simulate);
                UpdateManager.batch(from, to);
                return extracted;
            }
        }
        return 0;
    }

    public static <T extends Resource> long exchange(StorageIO<T> io, T oldresource, T newresource, long amount, boolean simulate) {
        long extracted = io.extract(oldresource, amount, false);
        if (extracted > 0) {
            long inserted = io.insert(newresource, extracted, true);
            if (extracted == inserted && !simulate) {
                io.insert(newresource, extracted, false);
            } else {
                io.insert(oldresource, extracted, false);
            }
            return extracted == inserted ? extracted : 0;
        }
        return 0;
    }

    public static <T extends Resource> long insertSlots(CommonStorage<T> container, T resource, long amount, boolean simulate) {
        return insertSubset(container, 0, container.size(), resource, amount, simulate);
    }

    public static <T extends Resource> long insertSubset(CommonStorage<T> container, int start, int end, T resource, long amount, boolean simulate) {
        long inserted = 0;
        for (int i = start; i < end; i++) {
            StorageSlot<T> slot = container.get(i);
            if (!slot.getResource().isBlank()) {
                inserted += slot.insert(resource, amount - inserted, simulate);
                if (inserted >= amount) {
                    return inserted;
                }
            }
        }
        for (int i = start; i < end; i++) {
            inserted += container.get(i).insert(resource, amount - inserted, simulate);
            if (inserted >= amount) {
                return inserted;
            }
        }
        return inserted;
    }

    public static <T extends Resource> long extractSlots(CommonStorage<T> container, T resource, long amount, boolean simulate) {
        return extractSubset(container, 0, container.size(), resource, amount, simulate);
    }

    public static <T extends Resource> long extractSubset(CommonStorage<T> container, int start, int end, T resource, long amount, boolean simulate) {
        long extracted = 0;
        for (int i = start; i < end; i++) {
            extracted += container.get(i).extract(resource, amount - extracted, simulate);
            if (extracted >= amount) {
                return extracted;
            }
        }
        return extracted;
    }

    public static <T extends Resource> void equalize(StorageSlot<T> slot, long amount) {
        T resource = slot.getResource();
        long current = slot.getAmount();
        if (current < amount) {
            slot.insert(resource, amount - current, false);
        } else if (current > amount) {
            slot.extract(resource, current - amount, false);
        }
    }

    public static <T extends Resource> long insertStack(CommonStorage<T> container, ResourceStack<T> stack, boolean simulate) {
        return insertSlots(container, stack.resource(), stack.amount(), simulate);
    }

    public static <T extends Resource> long extractStack(CommonStorage<T> container, ResourceStack<T> stack, boolean simulate) {
        return extractSlots(container, stack.resource(), stack.amount(), simulate);
    }

    @Nullable
    public static <T extends Resource> ResourceStack<T> extractPredicate(CommonStorage<T> container, Predicate<T> predicate, long amount, boolean simulate) {
        Set<T> resources = new HashSet<>();
        ResourceStack<T> stack = null;
        for (int i = 0; i < container.size(); i++) {
            StorageSlot<T> slot = container.get(i);
            if (slot.getResource().isBlank()) continue;
            T resource = slot.getResource();
            if (predicate.test(resource)) {
                if (resources.contains(resource)) continue;
                resources.add(resource);
                ResourceStack<T> newStack = new ResourceStack<>(resource, slot.extract(resource, amount, simulate));
                if (stack == null || newStack.amount() > stack.amount()) {
                    stack = newStack;
                    if (stack.amount() >= amount) {
                        break;
                    }
                }
            }
        }
        return stack;
    }

    public static ResourceStack<ItemResource> extractItem(CommonStorage<ItemResource> container, Predicate<ItemResource> predicate, long amount, boolean simulate) {
        return Optional.ofNullable(extractPredicate(container, predicate, amount, simulate)).orElse(ResourceStack.EMPTY_ITEM);
    }

    public static ResourceStack<FluidResource> extractFluid(CommonStorage<FluidResource> container, Predicate<FluidResource> predicate, long amount, boolean simulate) {
        return Optional.ofNullable(extractPredicate(container, predicate, amount, simulate)).orElse(ResourceStack.EMPTY_FLUID);
    }

    public static ResourceStack<FluidResource> extractFluid(CommonStorage<FluidResource> container, SizedFluidIngredient ingredient, boolean simulate) {
        return extractFluid(container, ingredient.ingredient(), ingredient.getAmount(), simulate);
    }
}
