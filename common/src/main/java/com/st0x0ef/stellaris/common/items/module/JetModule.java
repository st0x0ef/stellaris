package com.st0x0ef.stellaris.common.items.module;

import com.fej1fun.potentials.capabilities.Capabilities;
import com.fej1fun.potentials.fluid.UniversalFluidStorage;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.data_components.JetSuitComponent;
import com.st0x0ef.stellaris.common.items.armors.JetSuit;
import com.st0x0ef.stellaris.common.keybinds.KeyVariables;
import com.st0x0ef.stellaris.common.module.Module;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import com.st0x0ef.stellaris.common.registry.FluidRegistry;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import com.st0x0ef.stellaris.common.utils.Utils;
import dev.architectury.fluid.FluidStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Set;
import java.util.function.Supplier;

public class JetModule extends SpaceSuitModuleItem {

    public JetModule(Properties properties) {
        super(properties);
    }

    @Override
    public MutableComponent displayName() {
        return Component.translatable("spacesuit.stellaris.jet_module");
    }

    @Override
    public <T extends Item & Module> Supplier<T> getAsItem() {
        return (Supplier<T>) ItemsRegistry.MODULE_JET;
    }

    @Override
    public Set<Module> requires() {
        return Set.of(ItemsRegistry.MODULE_FUEL.get());
    }

    @Override
    public int renderStackedGui(GuiGraphics graphics, DeltaTracker deltaTracker, Player player, ItemStack stack, int y) {
        Component modeText = getModeType(stack).getComponent();
        ChatFormatting chatFormatting = getModeType(stack).getChatFormatting();

        /** TEXT */
        Font font = Minecraft.getInstance().font;
        Component text = Component.translatable("general." + Stellaris.MODID + ".jet_suit_mode").append(": ").withStyle(chatFormatting).append(modeText.copy().withStyle(ChatFormatting.GRAY));
        graphics.drawString(font, text, 5, y, 0xFFFFFF);
        y += Minecraft.getInstance().font.lineHeight;
        return y;
    }

    //stolen from jetsuit
    public float spacePressTime;
    public int getMode(ItemStack itemStack) {
        return itemStack.getOrDefault(DataComponentsRegistry.JET_SUIT_COMPONENT.get(), new JetSuitComponent(JetSuit.ModeType.DISABLED)).type().getMode();
    }

    public JetSuit.ModeType getModeType(ItemStack itemStack) {
        return switch (this.getMode(itemStack)) {
            case 1 -> JetSuit.ModeType.NORMAL;
            case 2 -> JetSuit.ModeType.HOVER;
            case 3 -> JetSuit.ModeType.ELYTRA;
            default -> JetSuit.ModeType.DISABLED;
        };
    }


//        @Override
//        public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
//            return Utils.isLivingInJetSuit(entity) && this.getMode(stack) == ModeType.ELYTRA.getMode();
//        }
//
//        @Override
//        public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
//            if (!entity.level().isClientSide) {
//                int nextFlightTick = flightTicks + 1;
//                if (nextFlightTick % 10 == 0) {
//                    entity.gameEvent(GameEvent.ELYTRA_GLIDE);
//                }
//            }
//
//            return true;
//        }



    public void tick(ItemStack stack, Level level, Player player) {
        UniversalFluidStorage storage = Capabilities.Fluid.ITEM.getCapability(stack);
        if (storage == null || storage.getFluidInTank(1).isEmpty()) return;

        /** JET SUIT FAST BOOST */
        if (player.isSprinting()) {
            this.boost(player, 1.3, true);
        }

        /** JET SUIT SLOW BOOST */
        if (player.zza > 0 && !player.isSprinting()) {
            this.boost(player, 0.9, false);
        }

        switch (this.getMode(stack)) {
            case 1 -> this.normalFlyModeMovement(player, stack);
            case 2 -> this.hoverModeMovement(player, stack);
            case 3 -> this.elytraModeMovement(player, stack);
        }

        /** CALCULATE PRESS SPACE TIME */
        this.calculateSpacePressTime(player, stack);
    }

    private void normalFlyModeMovement(Player player, ItemStack stack) {
        if (KeyVariables.isHoldingJump(player)) {
            player.moveRelative(1.2F, new Vec3(0, 0.1, 0));
            player.resetFallDistance();
            Utils.disableFlyAntiCheat(player, true);
        }

        if (!player.onGround()) {
            if (KeyVariables.isHoldingUp(player)) {
                player.moveRelative(1.0F, new Vec3(0, 0, 0.03));
            } else if (KeyVariables.isHoldingDown(player)) {
                player.moveRelative(1.0F, new Vec3(0, 0, -0.03));
            }
        }

        if (!player.onGround()) {
            if (KeyVariables.isHoldingRight(player)) {
                player.moveRelative(1.0F, new Vec3(-0.03, 0, 0));
            } else if (KeyVariables.isHoldingLeft(player)) {
                player.moveRelative(1.0F, new Vec3(0.03, 0, 0));
            }
        }
    }
    private void hoverModeMovement(Player player, ItemStack stack) {
        Vec3 vec3 = player.getDeltaMovement();

        // Main movement logic
        if (!player.onGround() && !player.isInWater()) {
            player.setDeltaMovement(vec3.x, vec3.y + 0.04, vec3.z);
            player.resetFallDistance();
            Utils.disableFlyAntiCheat(player, true);

            UniversalFluidStorage storage = Capabilities.Fluid.ITEM.getCapability(stack);
            if (storage == null) return;
            storage.drain(FluidStack.create(FluidRegistry.FUEL_STILL.get(), 2), false);
        }

        // Move up
        if (KeyVariables.isHoldingJump(player)) {
            Utils.disableFlyAntiCheat(player, true);
        }

        // Move down
        if (player.isCrouching()) {
            player.moveRelative(0.05F, new Vec3(0, -0.08, 0));
        }

        // Move forward and backward
        if (!player.onGround()) {
            if (KeyVariables.isHoldingUp(player)) {
                player.moveRelative(0.1F, new Vec3(0, 0, 0.1));
            } else if (KeyVariables.isHoldingDown(player)) {
                player.moveRelative(0.1F, new Vec3(0, 0, -0.1));
            }
        }

        // Move sideways
        if (!player.onGround()) {
            if (KeyVariables.isHoldingRight(player)) {
                player.moveRelative(0.1F, new Vec3(-0.1, 0, 0));
            } else if (KeyVariables.isHoldingLeft(player)) {
                player.moveRelative(0.1F, new Vec3(0.1, 0, 0));
            }
        }
    }

    private void elytraModeMovement(Player player, ItemStack stack) {
        if (player.isSprinting() && !player.onGround()) {
            player.startFallFlying();
            Utils.disableFlyAntiCheat(player, true);

            UniversalFluidStorage storage = Capabilities.Fluid.ITEM.getCapability(stack);
            if (storage == null) return;
            storage.drain(FluidStack.create(FluidRegistry.FUEL_STILL.get(), 2), false);
        } else if (player.isSprinting() && player.onGround() && KeyVariables.isHoldingJump(player)) {
            player.moveTo(player.getX(), player.getY() + 2, player.getZ());
        } else if (player.isCrouching() && player.isFallFlying()) {
            player.stopFallFlying();
        }

        if (isGliding) {
            player.setDeltaMovement(player.getDeltaMovement().x(), 0, player.getDeltaMovement().z());
            if (!player.onGround()) {
                player.setDeltaMovement(player.getDeltaMovement().x(), -0.1D, player.getDeltaMovement().z());
            } else {
                isGliding = false;
                player.stopFallFlying();
            }
        }
    }


    public void switchJetSuitMode(ItemStack itemStack) {
        JetSuitComponent jetSuitComponent;
        if (this.getMode(itemStack) < 3) {
            jetSuitComponent = new JetSuitComponent(JetSuit.ModeType.fromInt(this.getMode(itemStack) + 1));
        } else {
            jetSuitComponent = new JetSuitComponent(JetSuit.ModeType.fromInt(0));
        }
        itemStack.set(DataComponentsRegistry.JET_SUIT_COMPONENT.get(), jetSuitComponent);

    }

    private boolean isGliding = false;

    public void calculateSpacePressTime(Player player, ItemStack itemStack) {
        if (Utils.isLivingInJetSuit(player)) {
            int mode = this.getMode(itemStack);

            if (mode == JetSuit.ModeType.ELYTRA.getMode()) {
                if (KeyVariables.isHoldingUp(player) && player.isFallFlying()) {
                    if (player.isSprinting()) {
                        if (this.spacePressTime < 2.8F) {
                            this.spacePressTime += 0.2F;
                        }
                    } else {
                        if (this.spacePressTime < 2.2F) {
                            this.spacePressTime += 0.2F;
                        }
                    }
                    if (this.spacePressTime >= 2.0F && !isGliding) {
                        startGliding(player);
                    }
                } else if (this.spacePressTime > 0.0F) {
                    this.spacePressTime -= 0.2F;
                    if (this.spacePressTime <= 0.0F) {
                        isGliding = false;
                    }
                }
            }
        }
    }

    private void startGliding(Player player) {
        isGliding = true;
        player.stopFallFlying();
        player.setDeltaMovement(player.getDeltaMovement().x(), 0.0D, player.getDeltaMovement().z());
    }

    public void boost(Player player, double boost, boolean sonicBoom) {
        Vec3 vec31 = player.getLookAngle();

        if (Utils.isLivingInJetSuit(player) && player.isFallFlying()) {
            Vec3 vec32 = player.getDeltaMovement();
            player.setDeltaMovement(vec32.add(vec31.x * 0.1D + (vec31.x * boost - vec32.x) * 0.5D, vec31.y * 0.1D + (vec31.y * boost - vec32.y) * 0.5D, vec31.z * 0.1D + (vec31.z * boost - vec32.z) * 0.5D));

            if (sonicBoom) {
                Vec3 vec33 = player.getLookAngle().scale(6.5D);

                if (player.level() instanceof ServerLevel) {
                    for (ServerPlayer p : ((ServerLevel) player.level()).getServer().getPlayerList().getPlayers()) {
                        ((ServerLevel) player.level()).sendParticles(p, ParticleTypes.FLASH, true, player.getX() - vec33.x, player.getY() - vec33.y, player.getZ() - vec33.z, 1, 0, 0, 0, 0.001);
                    }
                }
            }
        }
    }
}
