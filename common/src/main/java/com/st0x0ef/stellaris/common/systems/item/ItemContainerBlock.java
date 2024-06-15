package com.st0x0ef.stellaris.common.systems.item;

public interface ItemContainerBlock {

    /**
     * @return A {@link SerializableContainer} that represents the inventory of this block.
     */
    SerializableContainer getContainer();
}
