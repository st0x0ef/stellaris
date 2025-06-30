package com.st0x0ef.stellaris.common.blocks.entities;

import com.mojang.authlib.GameProfile;
import com.st0x0ef.stellaris.common.menus.FlagUploadMenu;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.UUID;

public class FlagBlockEntity extends BaseContainerBlockEntity {

    private NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

    private ResolvableProfile profile;
    private DyeColor color = DyeColor.GRAY;

    public STATE flagState = STATE.PLAYER_HEAD;

    public FlagBlockEntity( BlockPos pos, BlockState blockState) {
        this(BlockEntityRegistry.FLAG.get(), pos, blockState);
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new FlagUploadMenu(containerId, inventory, this);
    }

    public FlagBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if(this.profile != null) {
            CompoundTag profileTag = new CompoundTag();
            profileTag.putString("Name", this.profile.gameProfile().getName());
            profileTag.putUUID("Id", this.profile.gameProfile().getId());
            tag.put("profile", profileTag);
        }
        tag.putInt("color", this.color.getId());
        flagState.toNBT(tag);

    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.stellaris.flag");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
        this.setChanged();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("profile")) {
            CompoundTag profileTag = tag.getCompound("profile");
            this.profile = new ResolvableProfile(new GameProfile(
                profileTag.getUUID("Id"),
                profileTag.getString("Name"))
            );
        }
        this.color = DyeColor.byId(tag.getInt("color"));
        this.flagState = STATE.fromNBT(tag);

    }

    public ResolvableProfile getGameProfile() {
        return profile;
    }

    public STATE getFlagState() {
        return flagState;
    }

    public DyeColor getColor() {
        return color;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithFullMetadata(registries);
    }

    @Override
    protected void applyImplicitComponents(BlockEntity.DataComponentInput componentInput) {
        super.applyImplicitComponents(componentInput);
        this.profile = componentInput.getOrDefault(DataComponents.PROFILE, new ResolvableProfile(new GameProfile(UUID.fromString("fe40f09c-fdaa-497f-8e2b-bed31180bfbd"), "TATHAN_06")));

    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(DataComponents.PROFILE, this.profile);
    }

    public void setDyeColor(DyeColor color) {
        this.color = color;
        this.setChanged();
    }

    public void setProfile(ResolvableProfile profile) {
        this.profile = profile;
        this.setChanged();
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    public void setFlagState(STATE flagState) {
        this.flagState = flagState;
    }

    public enum STATE {
        PLAYER_HEAD(true, false),
        CUSTOM_PNG(false, true);

        public final boolean playerHead;
        public final boolean customPng;

        STATE(boolean playerHead, boolean customPng) {
            this.playerHead = playerHead;
            this.customPng = customPng;
        }

        public void toNBT(CompoundTag tag) {
            tag.putBoolean("playerHead", playerHead);
            tag.putBoolean("customPng", customPng);
        }

        public static STATE fromNBT(CompoundTag tag) {
            boolean playerHead = tag.getBoolean("playerHead");
            boolean customPng = tag.getBoolean("customPng");
            for (STATE state : values()) {
                if (state.playerHead == playerHead && state.customPng == customPng) {
                    return state;
                }
            }
            throw new NoSuchElementException(); // or throw an exception
        }

        public static STATE fromValues(boolean playerHead, boolean customPng) {
            for (STATE state : values()) {
                if (state.playerHead == playerHead && state.customPng == customPng) {
                    return state;
                }
            }
            throw new NoSuchElementException("No STATE found for playerHead: " + playerHead + " and customPng: " + customPng);
        }
    }
}
