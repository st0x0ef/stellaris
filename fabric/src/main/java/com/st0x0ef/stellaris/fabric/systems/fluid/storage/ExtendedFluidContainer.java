package com.st0x0ef.stellaris.fabric.systems.fluid.storage;

import com.st0x0ef.stellaris.common.systems.fluid.base.FluidSnapshot;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;

public abstract class ExtendedFluidContainer extends SnapshotParticipant<FluidSnapshot> {
    @Override
    abstract public void onFinalCommit();

    @Override
    abstract public FluidSnapshot createSnapshot();

    @Override
    abstract public void readSnapshot(FluidSnapshot snapshot);
}
