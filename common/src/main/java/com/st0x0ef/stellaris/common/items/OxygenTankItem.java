package com.st0x0ef.stellaris.common.items;

import com.fej1fun.potentials.capabilities.Capabilities;
import com.fej1fun.potentials.fluid.ItemFluidStorage;
import com.fej1fun.potentials.fluid.UniversalFluidItemStorage;
import com.fej1fun.potentials.providers.FluidProvider;
import com.st0x0ef.stellaris.common.blocks.entities.machines.OxygenDistributorBlockEntity;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import com.st0x0ef.stellaris.common.registry.FluidRegistry;
import com.st0x0ef.stellaris.common.utils.capabilities.fluid.FluidUtil;
import dev.architectury.fluid.FluidStack;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OxygenTankItem extends Item implements FluidProvider.ITEM {

    private final int capacity;

    public OxygenTankItem(Item.Properties properties, int capacity) {
        super(properties.stacksTo(1));
        this.capacity = capacity;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.item.stellaris.oxygen_tank", getFluidTank(stack).getFluidInTank(0).getAmount(), getFluidTank(stack).getTankCapacity(0)).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        if (player.isShiftKeyDown()) {
            ItemStack tankStack = player.getItemInHand(usedHand);
            ItemStack chestplateStack = player.getItemBySlot(EquipmentSlot.CHEST);

            ItemFluidStorage storage = getFluidTank(tankStack);
            UniversalFluidItemStorage chestplateStorage = Capabilities.Fluid.ITEM.getCapability(chestplateStack);

            if (chestplateStorage == null || storage.getFluidInTank(0).isEmpty())
                return super.use(level, player, usedHand);

            long amountMoved = FluidUtil.moveFluid(storage, chestplateStorage, storage.getFluidInTank(0)).getAmount();

            if (amountMoved != 0)
                return InteractionResultHolder.success(tankStack);

        }

        return super.use(level, player, usedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockEntity block = context.getLevel().getBlockEntity(context.getClickedPos());
        if (block instanceof OxygenDistributorBlockEntity entity) {
            ItemFluidStorage storage = getFluidTank(context.getItemInHand());
            long amount = entity.addOxygen(storage.getFluidInTank(0).getAmount());
            storage.drain(storage.getFluidInTank(0).copyWithAmount(amount), false);
            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        UniversalFluidItemStorage storage = getFluidTank(stack);
        return (int) Mth.clamp(((storage.getFluidInTank(0).getAmount() + 1) * 13) / storage.getTankCapacity(0), 0, 13);

    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xA7E6ED;
    }

    @Override
    public @NotNull ItemFluidStorage getFluidTank(@NotNull ItemStack stack) {
        return new ItemFluidStorage(DataComponentsRegistry.FLUID_LIST.get(), stack, 1, capacity) {
            @Override
            public boolean isFluidValid(int tank, FluidStack stack) {
                return stack.getFluid().isSame(FluidRegistry.OXYGEN_STILL.get());
            }
        };
    }
}
