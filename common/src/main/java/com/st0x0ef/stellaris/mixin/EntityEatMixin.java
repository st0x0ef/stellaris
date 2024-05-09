package com.st0x0ef.stellaris.mixin;

import com.st0x0ef.stellaris.common.items.CanItem;
import com.st0x0ef.stellaris.common.registry.TagRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Player.class)
public abstract class EntityEatMixin extends LivingEntity {
    @Shadow public abstract void playSound(SoundEvent soundEvent, float f, float g);

    protected EntityEatMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At(value = "HEAD"), method = "eat", cancellable = true)
    public void eat(Level level, ItemStack food, CallbackInfoReturnable<ItemStack> cir) {
        LivingEntity entity = this;

        if (food.getComponents().has(DataComponents.FOOD)) {
            if(!PlanetUtil.isPlanet(level.dimension())) return;
            if(!PlanetUtil.hasOxygen(level.dimension()) && (!food.is(TagRegistry.SPACE_FOOD) || !(food.getItem() instanceof CanItem))) {
                //TODO : Stop the player from eating
                return;
            }

            level.playSound(null, this.getX(), this.getY(), this.getZ(), this.getEatingSound(food), SoundSource.NEUTRAL, 1.0F, 1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.4F);
            entity.eat(level, food);
            if (!(entity instanceof Player) || !((Player)entity).getAbilities().instabuild) {
                food.shrink(1);
            }



            this.gameEvent(GameEvent.EAT);
        }

        if (food.getComponents().has(DataComponents.POTION_CONTENTS)) {
            if (food.getComponents().get(DataComponents.POTION_CONTENTS).is(Potions.WATER)) {
                if (entity instanceof Player player) {
                    if (player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof CanItem can) {
                        player.getFoodData().eat(can.getFoodProperties().nutrition(), can.getFoodProperties().saturation());
                    }
                }
            }
        }

        cir.setReturnValue(food);
    }
}
