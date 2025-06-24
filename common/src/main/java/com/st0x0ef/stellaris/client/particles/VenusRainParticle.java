package com.st0x0ef.stellaris.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

@Environment(EnvType.CLIENT)
public class VenusRainParticle extends TextureSheetParticle {

    public VenusRainParticle(ClientLevel world, double x, double y, double z, SpriteSet spriteSet) {
        super(world, x, y, z);
        quadSize *= 1F;
        hasPhysics = true;
        speedUpWhenYMotionIsBlocked = true;
        xd *= 0.3;
        yd = random.nextDouble() * 0.2 + 0.1;
        zd *= 0.3;
        setSize(0.01F, 0.01F);
        gravity = 0.06F;
        lifetime = (int) (8 / (random.nextDouble() * 0.8 + 0.2));
        pickSprite(spriteSet);
    }

    @Override
    public void tick() {
        super.tick();

        if (onGround) {
            remove();
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public record Provider(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new VenusRainParticle(level, x, y, z, spriteSet);
        }
    }
}
