package com.st0x0ef.stellaris.mixin;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.data.planets.StellarisData;
import com.st0x0ef.stellaris.common.items.CanItem;
import com.st0x0ef.stellaris.common.registry.TagRegistry;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Player.class)
public abstract class EntityEatMixin extends LivingEntity {
    protected EntityEatMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At(value = "HEAD"), method = "eat")
    public void eat(Level level, ItemStack food, CallbackInfoReturnable<ItemStack> cir) {
        LivingEntity entity = (LivingEntity) ((Object) this);

        if (food.isEdible()) {
            if(!StellarisData.isPlanet(level.dimension())) return;
            Stellaris.LOG.error("Oxygen : " + StellarisData.getPlanet(level.dimension()).oxygen());
            if(StellarisData.getPlanet(level.dimension()).oxygen() == false && (!food.is(TagRegistry.SPACE_FOOD) || !(food.getItem() instanceof CanItem))) {
                Stellaris.LOG.error("Can't eat : " + (!food.is(TagRegistry.SPACE_FOOD)) + " " + !(food.getItem() instanceof CanItem));
                //TODO : Stop the player from eating
                return;
            }

            level.playSound((Player)null, this.getX(), this.getY(), this.getZ(), this.getEatingSound(food), SoundSource.NEUTRAL, 1.0F, 1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.4F);
            entity.addEatEffect(food, level, this);
            if (!(entity instanceof Player) || !((Player)entity).getAbilities().instabuild) {
                food.shrink(1);
            }

            this.gameEvent(GameEvent.EAT);
        }

        cir.setReturnValue(food);
    }


}
