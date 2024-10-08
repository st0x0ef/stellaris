package com.st0x0ef.stellaris.common.armors;

import com.mojang.serialization.Codec;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.blocks.entities.machines.FluidTankHelper;
import com.st0x0ef.stellaris.common.data_components.JetSuitComponent;
import com.st0x0ef.stellaris.common.keybinds.KeyVariables;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class JetSuit {
    public static final long MAX_FUEL_CAPACITY = FluidTankHelper.convertFromNeoMb(3000);

    public static class Suit extends AbstractSpaceArmor.Chestplate {
        public float spacePressTime;

        public Suit(Holder<ArmorMaterial> material, Properties properties) {
            super(material, Type.CHESTPLATE, properties);
        }

        public int getMode(ItemStack itemStack) {
            return itemStack.get(DataComponentsRegistry.JET_SUIT_COMPONENT.get()).type().getMode();
        }

        public ModeType getModeType(ItemStack itemStack) {
            return switch (this.getMode(itemStack)) {
                case 1 -> ModeType.NORMAL;
                case 2 -> ModeType.HOVER;
                case 3 -> ModeType.ELYTRA;
                default -> ModeType.DISABLED;
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

        public void onArmorTick(ItemStack stack, Level level, Player player) {
            super.onArmorTick(stack, level, player);

            if (getFuel(stack) <= 0) return;

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
                addFuel(stack, -2);
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
                addFuel(stack, -2);
            } else if (player.isSprinting() && player.onGround() && KeyVariables.isHoldingJump(player)) {
                player.moveTo(player.getX(), player.getY() + 2, player.getZ());
            }
        }


        public void switchJetSuitMode(ItemStack itemStack) {
            JetSuitComponent jetSuitComponent;
            if (this.getMode(itemStack) < 3) {
                jetSuitComponent = new JetSuitComponent(ModeType.fromInt(this.getMode(itemStack) + 1));
            } else {
                jetSuitComponent = new JetSuitComponent(ModeType.fromInt(0));
            }
            itemStack.set(DataComponentsRegistry.JET_SUIT_COMPONENT.get(), jetSuitComponent);

        }

        public void calculateSpacePressTime(Player player, ItemStack itemStack) {
            int mode = this.getMode(itemStack);

            if (Utils.isLivingInJetSuit(player)) {

                /** NORMAL MODE */
                if (mode == ModeType.NORMAL.getMode()) {
                    if (KeyVariables.isHoldingJump(player)) {
                        if (this.spacePressTime < 2.2F) {
                            this.spacePressTime = this.spacePressTime + 0.2F;
                        }
                    }
                    else if (this.spacePressTime > 0.0F) {
                        this.spacePressTime = this.spacePressTime - 0.2F;
                    }
                }

                /** HOVER MODE */
                if (mode == ModeType.HOVER.getMode()) {
                    if (!player.onGround() && this.spacePressTime < 0.6F) {
                        this.spacePressTime = this.spacePressTime + 0.2F;
                    }
                    else if (KeyVariables.isHoldingJump(player)) {
                        if (this.spacePressTime < 1.4F) {
                            this.spacePressTime = this.spacePressTime + 0.2F;
                            hoverModeMovement(player,itemStack);
                        }
                    }
                    else if (this.spacePressTime >= 0.6F) {
                        this.spacePressTime = this.spacePressTime - 0.2F;
                    }

                }

                /** ELYTRA MODE */
                if (mode == ModeType.ELYTRA.getMode()) {
                    if (KeyVariables.isHoldingUp(player) && player.isFallFlying()) {
                        if (player.isSprinting()) {
                            if (this.spacePressTime < 2.8F) {
                                this.spacePressTime = this.spacePressTime + 0.2F;
                            }
                        } else {
                            if (this.spacePressTime < 2.2F) {
                                this.spacePressTime = this.spacePressTime + 0.2F;
                            }
                        }
                    }
                    else if (this.spacePressTime > 0.0F) {
                        this.spacePressTime = this.spacePressTime - 0.2F;
                    }
                }
            }
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

    public enum ModeType implements StringRepresentable {
        DISABLED(Component.translatable("general." + Stellaris.MODID + ".jet_suit_disabled_mode"), ChatFormatting.RED, 0),
        NORMAL(Component.translatable("general." + Stellaris.MODID + ".jet_suit_normal_mode"), ChatFormatting.GREEN, 1),
        HOVER(Component.translatable("general." + Stellaris.MODID + ".jet_suit_hover_mode"), ChatFormatting.GREEN, 2),
        ELYTRA(Component.translatable("general." + Stellaris.MODID + ".jet_suit_elytra_mode"), ChatFormatting.GREEN, 3);

        private final int mode;
        private final ChatFormatting chatFormatting;
        private final Component component;

        public static final Codec<ModeType> CODEC = StringRepresentable.fromEnum(ModeType::values);


        ModeType(Component component, ChatFormatting chatFormatting, int mode) {
            this.mode = mode;
            this.chatFormatting = chatFormatting;
            this.component = component;
        }

        public ChatFormatting getChatFormatting() {
            return chatFormatting;
        }

        public Component getComponent() {
            return component;
        }

        public int getMode() {
            return this.mode;
        }

        @Override
        public String getSerializedName() {
            return String.valueOf(this.mode);
        }


        public static ModeType fromInt(int integer) {
           return fromString(Integer.toString(integer));
        }

        public static ModeType fromString(String string) {
            Stellaris.LOG.error("From String : {}", Integer.decode(string));

            return switch (Integer.decode(string)) {
               case 0 -> DISABLED;
               case 1 -> NORMAL;
               case 2 -> HOVER;
               case 3 -> ELYTRA;
               default -> DISABLED;
           };
        }
    }
}