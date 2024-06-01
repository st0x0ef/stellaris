package com.st0x0ef.stellaris.neoforge.systems.item;

import com.st0x0ef.stellaris.common.systems.item.SerializableContainer;
import com.st0x0ef.stellaris.common.systems.util.Serializable;
import com.st0x0ef.stellaris.neoforge.systems.AutoSerializable;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;

public class ItemContainerWrapper extends InvWrapper implements ICapabilityProvider<BlockEntity, Direction, IItemHandler>, AutoSerializable {

    private final SerializableContainer serializableContainer;

    public ItemContainerWrapper(SerializableContainer inv) {
        super(inv);
        this.serializableContainer = inv;
    }

    @Override
    public Serializable getSerializable() {
        return serializableContainer;
    }

    @Override
    public @Nullable IItemHandler getCapability(BlockEntity object, Direction object2) {
        return this;
    }
}
