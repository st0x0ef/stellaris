package com.st0x0ef.stellaris.mixin;

import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.entities.CustomLightningBolt;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class StormyPlanetMixin {

    @Inject(at = @At(value = "HEAD"), method = "tickChunk")
    public void spawnMoreLightningBolt(LevelChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        ServerLevel level = (ServerLevel) (Object) this;
        PlanetUtil.ifPlanet(level.dimension().location(), planet -> {
            if (planet.stormParameters().isPresent()) {
                Planet.StormParameters parameters = planet.stormParameters().get();
                ChunkPos chunkPos = chunk.getPos();

                if (level.random.nextInt(parameters.lightningFrequency()) == 0) {
                    BlockPos pos = level.findLightningTargetAround(level.getBlockRandomPos(chunkPos.getMinBlockX(), 0, chunkPos.getMinBlockZ(), 15));
                    CustomLightningBolt lightningBolt = EntityRegistry.CUSTOM_LIGHTNING_BOLT.get().create(level);

                    if (lightningBolt != null) {
                        lightningBolt.setCustomColor(parameters.lightningColor());
                        lightningBolt.moveTo(Vec3.atBottomCenterOf(pos));
                        lightningBolt.setVisualOnly(false);
                        level.addFreshEntity(lightningBolt);
                    }
                }
            }
        });
    }
}