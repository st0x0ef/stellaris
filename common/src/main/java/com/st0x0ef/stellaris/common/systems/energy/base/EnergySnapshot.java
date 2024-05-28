package com.st0x0ef.stellaris.common.systems.energy.base;

import com.st0x0ef.stellaris.platform.systems.energy.EnergyContainer;

/**
 * A snapshot of a {@link EnergyContainer} that can be loaded into another container or used to restore a container to a previous state.
 */
public interface EnergySnapshot {

    /**
     * Loads the snapshot into the given container.
     *
     * @param container The container to load the snapshot into.
     */
    void loadSnapshot(EnergyContainer container);
}
