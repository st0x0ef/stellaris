//package com.st0x0ef.stellaris.common.systems.core.storage.impl;
//
//import com.st0x0ef.stellaris.common.systems.core.storage.base.CommonStorage;
//import com.st0x0ef.stellaris.common.systems.core.storage.base.StorageSlot;
//import com.st0x0ef.stellaris.common.systems.core.storage.base.UpdateManager;
//import com.st0x0ef.stellaris.common.systems.resources.Resource;
//import org.jetbrains.annotations.NotNull;
//
//public class AggregateContainer<T extends Resource> implements CommonStorage<T>, UpdateManager<Object[]> { add if needed
//    private final CommonStorage<T>[] containers;
//    private final int[] indexOffsets;
//    private final int slotCount;
//
//    @SafeVarargs
//    public AggregateContainer(CommonStorage<T>... containers) {
//        this.containers = containers;
//        this.indexOffsets = new int[containers.length];
//        int index = 0;
//        for (int i = 0; i < indexOffsets.length; i++) {
//            index += containers[i].size();
//            indexOffsets[i] = index;
//        }
//        this.slotCount = index;
//    }
//
//    @Override
//    public int size() {
//        return slotCount;
//    }
//
//    @Override
//    public @NotNull StorageSlot<T> get(int index) {
//        for (int i = 0; i < containers.length; i++) {
//            if (index < indexOffsets[i]) {
//                return containers[i].get(index - indexOffsets[i]);
//            }
//        }
//        throw new IndexOutOfBoundsException();
//    }
//
//    @Override
//    public long insert(T resource, long amount, boolean simulate) {
//        long inserted = 0;
//        for (CommonStorage<T> container : containers) {
//            inserted += container.insert(resource, amount - inserted, simulate);
//            if (inserted >= amount) {
//                break;
//            }
//        }
//        return inserted;
//    }
//
//    @Override
//    public long extract(T resource, long amount, boolean simulate) {
//        long extracted = 0;
//        for (CommonStorage<T> container : containers) {
//            extracted += container.extract(resource, amount - extracted, simulate);
//            if (extracted >= amount) {
//                break;
//            }
//        }
//        return extracted;
//    }
//
//    @Override
//    public Object[] createSnapshot() {
//        var snapshots = new Object[containers.length];
//        for (int i = 0; i < containers.length; i++) {
//            if (containers[i] instanceof UpdateManager) {
//                snapshots[i] = ((UpdateManager<?>) containers[i]).createSnapshot();
//            }
//        }
//        return snapshots;
//    }
//
//    @Override
//    public void readSnapshot(Object[] snapshot) {
//        for (int i = 0; i < containers.length; i++) {
//            if (containers[i] instanceof UpdateManager<?> manager) {
//                UpdateManager.forceRead(manager, snapshot[i]);
//            }
//        }
//    }
//
//    @Override
//    public void update() {
//        UpdateManager.batch(containers);
//    }
//}
