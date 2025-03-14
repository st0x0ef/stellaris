package com.st0x0ef.stellaris.common.blocks.machines;

import com.fej1fun.potentials.energy.UniversalEnergyStorage;
import com.fej1fun.potentials.providers.EnergyProvider;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.common.blocks.entities.machines.BaseEnergyContainerBlockEntity;
import com.st0x0ef.stellaris.common.blocks.entities.machines.PowerBankEntity;
import com.st0x0ef.stellaris.common.blocks.entities.machines.RocketStationEntity;
import com.st0x0ef.stellaris.common.registry.BlockEntityRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PowerBankBlock extends BaseMachineBlock {

    public final short tier;

    public PowerBankBlock(Properties properties, short tier) {
        super(properties);
        this.tier = tier;
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return BlockEntityRegistry.POWER_BANK.get();
    }

    @Override
    public boolean hasTicker(Level level) {
        return !level.isClientSide;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PowerBankEntity(pos, state, tier);
    }

    @Override
    protected @NotNull MapCodec<? extends PowerBankBlock> codec() {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                propertiesCodec(),
                Codec.SHORT.fieldOf("tier").forGetter(bank -> bank.tier)
        ).apply(instance, PowerBankBlock::new));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (stack.getItem() instanceof EnergyProvider.ITEM provider) {
            UniversalEnergyStorage energyStorage = provider.getEnergy(stack);
            BlockEntity be = level.getBlockEntity(pos);
            if (energyStorage != null)
                if (be instanceof BaseEnergyContainerBlockEntity energyBlock)
                    energyBlock.getEnergy(null).setEnergyStored(energyStorage.getEnergy());

        }
    }
}
