package com.st0x0ef.stellaris.platform.systems;

import com.st0x0ef.stellaris.common.systems.generic.base.BlockContainerLookup;
import com.st0x0ef.stellaris.common.systems.generic.base.EntityContainerLookup;
import com.st0x0ef.stellaris.common.systems.generic.base.ItemContainerLookup;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

public class LookupApi {

    @ExpectPlatform
    public static <T, C> BlockContainerLookup<T, C> createBlockLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        throw new NotImplementedException();
    }

    public static <T> BlockContainerLookup<T, @Nullable Direction> createBlockLookup(ResourceLocation name, Class<T> typeClass) {
        return createBlockLookup(name, typeClass, Direction.class);
    }

    @ExpectPlatform
    public static <T, C> ItemContainerLookup<T, C> createItemLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        throw new NotImplementedException();
    }

    public static <T> ItemContainerLookup<T, Void> createItemLookup(ResourceLocation name, Class<T> typeClass) {
        return createItemLookup(name, typeClass, Void.class);
    }

    @ExpectPlatform
    public static <T, C> EntityContainerLookup<T, C> createEntityLookup(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        throw new NotImplementedException();
    }

    public static <T> EntityContainerLookup<T, Void> createEntityLookup(ResourceLocation name, Class<T> typeClass) {
        return createEntityLookup(name, typeClass, Void.class);
    }

    public static <T> EntityContainerLookup<T, @Nullable Direction> createAutomationEntityLookup(ResourceLocation name, Class<T> typeClass) {
        return createEntityLookup(name, typeClass, Direction.class);
    }
}
