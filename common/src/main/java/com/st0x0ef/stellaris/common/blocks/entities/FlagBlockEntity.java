package com.st0x0ef.stellaris.common.blocks.entities;

import com.mojang.authlib.GameProfile;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class FlagBlockEntity extends BlockEntity {

    private ResolvableProfile profile;
    private DyeColor color = DyeColor.GRAY;

    public FlagBlockEntity( BlockPos pos, BlockState blockState) {
        this(BlockEntityRegistry.FLAG.get(), pos, blockState);
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

    }

    public ResolvableProfile getGameProfile() {
        return profile;
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
}
