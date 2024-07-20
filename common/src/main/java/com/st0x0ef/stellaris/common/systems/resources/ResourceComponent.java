package com.st0x0ef.stellaris.common.systems.resources;

import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.PatchedDataComponentMap;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class ResourceComponent implements Resource, DataComponentHolder {
    protected final DataComponentMap components;
    private DataComponentPatch dataPatch;

    protected ResourceComponent(DataComponentMap components) {
        this.components = components;
    }

    public DataComponentPatch getDataPatch() {
        DataComponentPatch patch = dataPatch;
        if (patch == null) {
            patch = dataPatch = components instanceof PatchedDataComponentMap ? ((PatchedDataComponentMap) components).asPatch() : DataComponentPatch.EMPTY;
        }
        return patch;
    }

    @Override
    public @NotNull DataComponentMap getComponents() {
        return components;
    }

    public boolean componentsMatch(DataComponentPatch other) {
        return Objects.equals(getDataPatch(), other);
    }
}
