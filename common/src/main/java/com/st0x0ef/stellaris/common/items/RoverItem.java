package com.st0x0ef.stellaris.common.items;

import com.st0x0ef.stellaris.common.data_components.RoverComponent;
import com.st0x0ef.stellaris.common.entities.vehicles.RoverEntity;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RoverItem extends Item {
    public RoverItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        BlockState state = context.getLevel().getBlockState(pos);
        InteractionHand hand = context.getHand();
        ItemStack itemStack = context.getItemInHand();

        if (context.getLevel() instanceof ServerLevel level) {
            BlockPlaceContext blockplacecontext = new BlockPlaceContext(context);
            BlockPos blockpos = blockplacecontext.getClickedPos();
            Vec3 vec3 = Vec3.upFromBottomCenterOf(blockpos, -0.6f);
            AABB aabb = EntityRegistry.ROVER.get().getDimensions().makeBoundingBox(vec3.x(), vec3.y(), vec3.z());

            if (level.noCollision(aabb)) {
                /** POS */
                int x = pos.getX();
                int y = pos.getY();
                int z = pos.getZ();

                RoverEntity rocket = this.getRocket(context.getLevel(), itemStack);
                rocket.setPos(pos.getX() + 0.5D,  pos.getY() + 1.0D, pos.getZ() + 0.5D);

                if (level.addFreshEntity(rocket)) {
                    /** ITEM REMOVE */
                    if (!player.getAbilities().instabuild) {
                        player.setItemInHand(hand, ItemStack.EMPTY);
                    }
                    level.addFreshEntity(rocket);
                    /** PLACE SOUND */
                    this.rocketPlaceSound(pos, level);

                    return InteractionResult.SUCCESS;
                }

            }
        }

        return super.useOn(context);
    }

    public RoverEntity getRocket(Level level, ItemStack stack) {
        RoverEntity rocket = new RoverEntity(null, level);
        return rocket;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        RoverComponent rocketComponent = stack.get(DataComponentsRegistry.ROVER_COMPONENT.get());
        if(rocketComponent == null) return;
        tooltipComponents.add(Component.translatable("tooltip.item.stellaris.rocket.fuel", rocketComponent.fuel()).withStyle(ChatFormatting.GRAY));
    }


    public void rocketPlaceSound(BlockPos pos, Level world) {
        world.playSound(null, pos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 1,1);
    }


    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        RoverComponent roverComponent = stack.get(DataComponentsRegistry.ROVER_COMPONENT.get());
        return 13 * roverComponent.fuel() / roverComponent.getTankCapacity();
    }

    @Override
    public int getBarColor(ItemStack stack) {
        RoverComponent roverComponent = stack.get(DataComponentsRegistry.ROVER_COMPONENT.get());
        return switch (roverComponent.getMotorUpgrade().getFuelType()) {
            case FUEL -> 0xA7E6ED;
            case HYDROGEN -> 0x00d8ff;
            case RADIOACTIVE -> 0x00c12f;
            case null -> 0xA7E6ED;

        };
    }

}
