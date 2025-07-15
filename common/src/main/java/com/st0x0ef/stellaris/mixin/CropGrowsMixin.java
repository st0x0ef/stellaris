package com.st0x0ef.stellaris.mixin;

import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.oxygen.GlobalOxygenManager;
import com.st0x0ef.stellaris.common.registry.TagRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CropBlock.class)
public class CropGrowsMixin {

    /**
     * Cancels crop growth if the planet does not have oxygen and the crop is not tagged as alien crops.
     * This is to prevent crops from growing in environments without oxygen.
     */
    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void cancelCropGrowth(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        boolean hasOxygen = GlobalOxygenManager.getInstance().getOrCreateDimensionManager(level).hasOxygenAt(pos);
        Planet planet = PlanetUtil.getPlanet(level.dimension().location());

        if(planet != null && !planet.oxygen() && !state.is(TagRegistry.ALIEN_CROPS) && !hasOxygen) {
            ci.cancel();
        }
    }

    /**
     * Cancels crop placement if the planet does not have oxygen and the crop is not tagged as alien crops.
     * This is to prevent crops from being placed in environments without oxygen.
     */
    @Inject(method = "mayPlaceOn", at = @At("HEAD"), cancellable = true)
    private void cancelCropPlacement(BlockState state, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {

        if(level instanceof ServerLevel serverLevel) {
            boolean hasOxygen = GlobalOxygenManager.getInstance().getOrCreateDimensionManager(serverLevel).hasOxygenAt(pos);
            Planet planet = PlanetUtil.getPlanet(serverLevel.dimension().location());

            if(planet != null && !planet.oxygen() && !state.is(TagRegistry.ALIEN_CROPS) && !hasOxygen) {
                cir.setReturnValue(false);
            }
        }
    }
}
