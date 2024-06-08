package com.st0x0ef.stellaris.fabric.systems.fluid.holder;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

@SuppressWarnings("UnstableApiUsage")
public interface ManualSyncing {
    default void setChanged(TransactionContext transaction) {
    }
}
