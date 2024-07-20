package com.st0x0ef.stellaris.common.systems.core.impl.vanilla;

import com.st0x0ef.stellaris.common.systems.core.item.util.ItemStorageData;
import com.st0x0ef.stellaris.common.systems.core.storage.base.UpdateManager;
import com.st0x0ef.stellaris.common.systems.resources.ResourceStack;
import net.minecraft.world.Container;

public class WrappedVanillaContainer extends AbstractVanillaContainer implements UpdateManager<ItemStorageData> {
    public WrappedVanillaContainer(Container container) {
        super(container);
    }

    @Override
    public ItemStorageData createSnapshot() {
        return ItemStorageData.of(this);
    }

    @Override
    public void readSnapshot(ItemStorageData snapshot) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            container.setItem(i, ResourceStack.toItemStack(snapshot.stacks().get(i)));
        }
    }

    @Override
    public void update() {
        container.setChanged();
    }
}
