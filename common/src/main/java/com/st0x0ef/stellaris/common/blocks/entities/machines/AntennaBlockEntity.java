package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.st0x0ef.stellaris.common.blocks.entities.ImplementedInventory;
import com.st0x0ef.stellaris.common.launchpads.LaunchPad;
import com.st0x0ef.stellaris.common.menus.AntennaMenu;
import com.st0x0ef.stellaris.common.network.packets.LaunchPadsOperations;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AntennaBlockEntity extends BaseContainerBlockEntity implements ImplementedInventory, TickingBlockEntity {

    public int launchPadId = -1;
    private NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);


    public AntennaBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.ANTENNA.get(), blockPos, blockState);
    }


    @Override
    protected @NotNull Component getDefaultName() {
        return Component.literal("Launch Pad Creator");
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new AntennaMenu(containerId, inventory, this, this.launchPadId);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public void setChanged() {
        if (this.level != null) {
            this.level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            super.setChanged();
        }
    }

    @Override
    public void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);

        this.launchPadId = compoundTag.getInt("LaunchPadId");
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.items, provider);

    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        compoundTag.putInt("launchPadId", this.launchPadId);
        ContainerHelper.saveAllItems(compoundTag, this.items, provider);

    }

    public void setLaunchPad(LaunchPad launchPad, boolean create) {
        if (create) {
            NetworkManager.sendToServer(new LaunchPadsOperations(launchPad, "add"));
        }
        this.launchPadId = launchPad.id();
        NetworkManager.sendToServer(new LaunchPadsOperations(launchPad, "setLaunchPad"));
    }

    @Override
    public void tick() {

    }

}