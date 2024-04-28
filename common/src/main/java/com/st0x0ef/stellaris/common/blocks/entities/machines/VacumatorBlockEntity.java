package com.st0x0ef.stellaris.common.blocks.entities.machines;

import com.mojang.serialization.MapCodec;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.blocks.entities.ImplementedInventory;
import com.st0x0ef.stellaris.common.blocks.machines.BaseEnergyBlock;
import com.st0x0ef.stellaris.common.energy.base.EnergyContainer;
import com.st0x0ef.stellaris.common.energy.impl.WrappedBlockEnergyContainer;
import com.st0x0ef.stellaris.common.items.CanItem;
import com.st0x0ef.stellaris.common.menus.RocketStationMenu;
import com.st0x0ef.stellaris.common.menus.SolarPanelMenu;
import com.st0x0ef.stellaris.common.menus.VacumatorMenu;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import com.st0x0ef.stellaris.common.registry.TagRegistry;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VacumatorBlockEntity extends BaseContainerBlockEntity implements ImplementedInventory {

    private NonNullList<ItemStack> items;
    private List<Integer> inputSlots = List.of(0, 1, 2);


    public VacumatorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(EntityRegistry.VACUMATOR_ENTITY.get(), blockPos, blockState);
        this.items = NonNullList.withSize(5, ItemStack.EMPTY);

    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new VacumatorMenu(i, inventory, this);
    }

    @Override
    public void setChanged() {
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        super.setChanged();
    }
    @Override
    public void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.items, provider);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        ContainerHelper.saveAllItems(compoundTag, this.items, provider);
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Vacumator");
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return false;
    }


    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        this.items = nonNullList;
    }

    public void tick() {
        if(level.isClientSide()) return;

        if(canCraft()) {
            craft();
            setChanged();
        }
    }

    public boolean canCraft() {
        if(getItem(0).getItem() instanceof CanItem) {
            return  isFood(getItem(1)) && getItem(2).is(Items.GLASS_BOTTLE) && getItem(3).isEmpty() && getItem(4).isEmpty();
        }

        return false;
    }

    public void craft() {
        CanItem can = (CanItem) getItem(0).getItem();
        ItemStack food = getItem(1);

        ItemStack result = new ItemStack(can);
        CanItem resultCanItem = (CanItem) result.getItem();

        if (resultCanItem.addFoodIfPossible(food)) {
            for (int i : inputSlots) {
                removeItem(i, 1);
            }

            ItemStack potionResult = new ItemStack(Items.POTION);
            potionResult.getComponents().get(DataComponents.POTION_CONTENTS).withPotion(Potions.WATER);

            setItem(3, result);
            setItem(4, potionResult);
        }
    }

    public static boolean isFood(ItemStack food) {
        return food.has(DataComponents.FOOD);
    }
}