package com.st0x0ef.stellaris.mixin;

import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public abstract class WaterBucketMixin extends Item {

    public WaterBucketMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "emptyContents", cancellable = true, at = @At(value = "HEAD"))
    public void freezeWater(Player player, Level level, BlockPos pos, BlockHitResult result, CallbackInfoReturnable<Boolean> cir) {
        if(PlanetUtil.isPlanet(level.dimension().location())) {
           Planet planet = PlanetUtil.getPlanet(level.dimension().location());
           if(planet.temperature() < 0) {
               level.setBlock(pos, Blocks.ICE.defaultBlockState(), 7);
               cir.setReturnValue(true);
           }
        }
    }

}
