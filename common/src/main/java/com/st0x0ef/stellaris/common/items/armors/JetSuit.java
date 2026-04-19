package com.st0x0ef.stellaris.common.items.armors;

import com.fej1fun.potentials.fluid.UniversalFluidItemStorage;
import com.mojang.serialization.Codec;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.data_components.JetSuitComponent;
import com.st0x0ef.stellaris.common.keybinds.KeyVariables;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import com.st0x0ef.stellaris.common.registry.ItemsRegistry;
import com.st0x0ef.stellaris.common.utils.Utils;
import dev.architectury.fluid.FluidStack;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class JetSuit {
    public static class Suit extends AbstractSpaceArmor.Chestplate {
        public float spacePressTime = 0.0f;

        private int nextFuelCheckTick = 0;

        public Suit(Holder<ArmorMaterial> material, Properties properties) {
            super(material, Type.CHESTPLATE, properties, false);
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

        @Override
        public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
            super.inventoryTick(stack, level, entity, slotId, isSelected);

            if (entity instanceof Player player && player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof JetSuit.Suit) {
                ItemStack jetSuitItemStack = player.getItemBySlot(EquipmentSlot.CHEST);
                boolean isBoosting = false;

                /** JET SUIT FAST BOOST */
                if (player.isSprinting() && this.getMode(jetSuitItemStack) != ModeType.ELYTRA.getMode()
                        && this.getMode(jetSuitItemStack) != ModeType.DISABLED.getMode()) {
                    UniversalFluidItemStorage storage = getFluidTank(jetSuitItemStack);
                    if (!storage.getFluidInTank(1).isEmpty()) {
                        this.boost(player, 1.3, true);
                        isBoosting = true;
                    }
                }

                /** JET SUIT SLOW BOOST */
                if (player.zza > 0 && !player.isSprinting() && this.getMode(jetSuitItemStack) != ModeType.ELYTRA.getMode()
                        && this.getMode(jetSuitItemStack) != ModeType.DISABLED.getMode()) {
                    UniversalFluidItemStorage storage = getFluidTank(jetSuitItemStack);
                    if (!storage.getFluidInTank(1).isEmpty()) {
                        this.boost(player, 0.9, false);
                        isBoosting = true;
                    }
                }

                /** DRAIN FUEL IF BOOSTING */
                if (isBoosting) {
                    UniversalFluidItemStorage storage = getFluidTank(jetSuitItemStack);
                    if (nextFuelCheckTick <= 0) {
                        FluidStack currentFluid = storage.getFluidInTank(1);
                        storage.drain(FluidStack.create(currentFluid.getFluid(), 1), false);
                        nextFuelCheckTick = 20;
                    }
                    nextFuelCheckTick--;
                }

                switch (this.getMode(stack)) {
                    case 1 -> this.normalFlyModeMovement(player, jetSuitItemStack);
                    case 2 -> this.hoverModeMovement(player, jetSuitItemStack);
                    case 3 -> this.elytraModeMovement(player, jetSuitItemStack);
                }

                /** CALCULATE PRESS SPACE TIME */
                this.calculateSpacePressTime(player, jetSuitItemStack);
            }
        }

        private void normalFlyModeMovement(Player player, ItemStack stack) {
            if (!player.getAbilities().flying && !player.isPassenger() && Utils.isLivingInJetSuit(player)) {
                if (this.getMode(stack) == ModeType.NORMAL.getMode() && !player.hasEffect(MobEffects.SLOW_FALLING)) {
                    UniversalFluidItemStorage storage = getFluidTank(stack);

                    if (storage.getFluidInTank(1).isEmpty()) return;
                    if (KeyVariables.isHoldingJump(player)) {
                        if (nextFuelCheckTick > 0) {
                            player.moveRelative(1.2F, new Vec3(0, 0.1, 0));
                            player.resetFallDistance();
                            Utils.disableFlyAntiCheat(player, true);
                        } else if (storage.getFluidInTank(1).isEmpty()) {
                            player.moveRelative(1.2F, new Vec3(0, 0.1, 0));
                            player.resetFallDistance();
                            Utils.disableFlyAntiCheat(player, true);
                            nextFuelCheckTick = 20;
                        }
                        nextFuelCheckTick--;

                        if (!player.onGround()) {
                            Vec3 movement = Vec3.ZERO;

                            if (KeyVariables.isHoldingUp(player)) {
                                movement = movement.add(player.getLookAngle().scale(0.1));
                            }
                            if (KeyVariables.isHoldingDown(player)) {
                                movement = movement.add(player.getLookAngle().scale(-0.05));
                            }
                            if (KeyVariables.isHoldingLeft(player)) {
                                movement = movement.add(Vec3.directionFromRotation(0, player.getYRot() - 90).scale(0.1));
                            }
                            if (KeyVariables.isHoldingRight(player)) {
                                movement = movement.add(Vec3.directionFromRotation(0, player.getYRot() + 90).scale(0.1));
                            }

                            player.setDeltaMovement(player.getDeltaMovement().add(movement));

                        }
                        if (!player.level().isClientSide) {
                            Vec3 look = player.getLookAngle();
                            ServerLevel serverLevel = (ServerLevel) player.level();
                            Vec3 particlePos = player.position()
                                    .subtract(look.scale(0.75))
                                    .add(0, 0.25, 0);

                            serverLevel.sendParticles(ParticleTypes.FLASH,
                                    particlePos.x, particlePos.y, particlePos.z,
                                    1,
                                    0.15, 0.15, 0.15,
                                    0.05);
                        }
                    }
                }
            }
        }


        private void hoverModeMovement(Player player, ItemStack stack) {
            if (!player.getAbilities().flying && !player.isPassenger() && Utils.isLivingInJetSuit(player)) {
                if (this.getMode(stack) == ModeType.HOVER.getMode() && !player.hasEffect(MobEffects.SLOW_FALLING)) {
                    Vec3 vec3 = player.getDeltaMovement();
                    UniversalFluidItemStorage storage = getFluidTank(stack);
                    // Main movement logic
                    if (storage.getFluidInTank(1).isEmpty()) return;
                    if (!player.onGround() && !player.isInWater()) {
                        if (nextFuelCheckTick > 0) {
                            player.setDeltaMovement(vec3.x, vec3.y + 0.04, vec3.z);
                            player.resetFallDistance();
                            Utils.disableFlyAntiCheat(player, true);
                        } else if (!storage.getFluidInTank(1).isEmpty()) {
                            player.setDeltaMovement(vec3.x, vec3.y + 0.04, vec3.z);
                            player.resetFallDistance();
                            Utils.disableFlyAntiCheat(player, true);
                            nextFuelCheckTick = 20;
                        }

                        nextFuelCheckTick--;
                    }

                    // Move up
                    if (KeyVariables.isHoldingJump(player)) {
                        Utils.disableFlyAntiCheat(player, true);


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
                        if (!player.level().isClientSide) {
                            Vec3 look = player.getLookAngle();
                            ServerLevel serverLevel = (ServerLevel) player.level();
                            Vec3 particlePos = player.position()
                                    .subtract(look.scale(0.75))
                                    .add(0, 0.25, 0);

                            serverLevel.sendParticles(ParticleTypes.FLASH,
                                    particlePos.x, particlePos.y, particlePos.z,
                                    1,
                                    0.15, 0.15, 0.15,
                                    0.05);
                        }
                    }
                }
            }
        }

        private void elytraModeMovement(Player player, ItemStack stack) {
            if (!player.getAbilities().flying && !player.isPassenger() && Utils.isLivingInJetSuit(player)) {
                if (this.getMode(stack) == ModeType.ELYTRA.getMode() && !player.hasEffect(MobEffects.SLOW_FALLING)) {
                    UniversalFluidItemStorage storage = getFluidTank(stack);

                    if (nextFuelCheckTick > 0) {
                        nextFuelCheckTick--;
                    }

                    if (player.isFallFlying() && KeyVariables.isHoldingUp(player)) {
                        // Check fuel is in tank
                        if (!storage.getFluidInTank(1).isEmpty()) {
                            this.boost(player, 1.3, true);

                            // consume fuel
                            if (nextFuelCheckTick <= 0) {
                                FluidStack currentFluid = storage.getFluidInTank(1);
                                storage.drain(FluidStack.create(currentFluid.getFluid(), 1), false);
                                nextFuelCheckTick = 20;
                            }
                        }
                    }

                    if (!player.level().isClientSide) {
                        Vec3 look = player.getLookAngle();
                        ServerLevel serverLevel = (ServerLevel) player.level();
                        Vec3 particlePos = player.position()
                                .subtract(look.scale(0.75))
                                .add(0, 0.25, 0);

                        serverLevel.sendParticles(ParticleTypes.FLAME,
                                particlePos.x, particlePos.y, particlePos.z,
                                1,
                                0.15, 0.15, 0.15,
                                0.05);
                    }
                }
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

            /** NORMAL MODE */
            if (mode == ModeType.NORMAL.getMode()) {
                if (KeyVariables.isHoldingJump(player)) {
                    if (this.spacePressTime < 2.2F) {
                        this.spacePressTime = this.spacePressTime + 0.2F;
                    }
                } else if (this.spacePressTime > 0.0F) {
                    this.spacePressTime = this.spacePressTime - 0.2F;
                }
            }

            /** HOVER MODE */
            if (mode == ModeType.HOVER.getMode()) {
                if (!player.onGround() && this.spacePressTime < 0.6F) {
                    this.spacePressTime = this.spacePressTime + 0.2F;
                } else if (KeyVariables.isHoldingJump(player)) {
                    if (this.spacePressTime < 1.4F) {
                        this.spacePressTime = this.spacePressTime + 0.2F;
                        hoverModeMovement(player, itemStack);
                    }
                } else if (this.spacePressTime >= 0.6F) {
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
            }


        }

        public void boost(Player player, double boost, boolean sonicBoom) {
            Vec3 vec31 = player.getLookAngle();

            if ((Utils.isLivingInJetSuit(player) || Utils.isLivingInSpaceSuit(player)) && player.isFallFlying()) {
                Vec3 vec32 = player.getDeltaMovement();
                player.setDeltaMovement(vec32.add(vec31.x * 0.1D + (vec31.x * boost - vec32.x) * 0.5D, vec31.y * 0.1D + (vec31.y * boost - vec32.y) * 0.5D, vec31.z * 0.1D + (vec31.z * boost - vec32.z) * 0.5D));

                if (sonicBoom) {
                    Vec3 vec33 = player.getLookAngle().scale(6.5D);

                    if (player.level() instanceof ServerLevel) {
                        for (ServerPlayer p : ((ServerLevel) player.level()).getServer().getPlayerList().getPlayers()) {
                            ((ServerLevel) player.level()).sendParticles(p, ParticleTypes.FLAME, true, player.getX() - vec33.x, player.getY() - vec33.y, player.getZ() - vec33.z, 1, 0, 0, 0, 0.001);
                        }
                    }
                }
            }
        }

        @Override
        public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
            return Utils.isLivingInJetSuit(entity) && this.getMode(stack) == ModeType.ELYTRA.getMode();
        }

        @Override
        public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
            if (!entity.level().isClientSide) {
                int nextFlightTick = flightTicks + 1;
                if (nextFlightTick % 10 == 0) {
                    entity.gameEvent(GameEvent.ELYTRA_GLIDE);
                }
            }
            return true;
        }



        @Override
        public boolean tryToStartFallFlying(Player player) {
            if (!player.onGround() && !player.isFallFlying() && !player.isInWater() && !player.hasEffect(MobEffects.LEVITATION)) {
                ItemStack itemStack = player.getItemBySlot(EquipmentSlot.CHEST);
                if (itemStack.is(ItemsRegistry.JETSUIT_SUIT) && ElytraItem.isFlyEnabled(itemStack) && this.getMode(itemStack) == ModeType.ELYTRA.getMode()) {
                    player.startFallFlying();
                    return true;
                }
            }
            return false;
        }
    }


    public enum ModeType implements StringRepresentable {
        DISABLED(Component.translatable("general." + Stellaris.MODID + ".jet_suit_disabled_mode"), ChatFormatting.RED, 0),
        NORMAL(Component.translatable("general." + Stellaris.MODID + ".jet_suit_normal_mode"), ChatFormatting.GREEN, 1),
        HOVER(Component.translatable("general." + Stellaris.MODID + ".jet_suit_hover_mode"), ChatFormatting.GREEN, 2),
        ELYTRA(Component.translatable("general." + Stellaris.MODID + ".jet_suit_elytra_mode"), ChatFormatting.GREEN, 3),
        CREATIVE(Component.translatable("general." + Stellaris.MODID + ".jet_suit_creative_mode"), ChatFormatting.GREEN, 4);

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
            return switch (Integer.decode(string)) {
                case 1 -> NORMAL;
                case 2 -> HOVER;
                case 3 -> ELYTRA;
                case 4 -> CREATIVE;
                default -> DISABLED;
            };
        }
    }
}