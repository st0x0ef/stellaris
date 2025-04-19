package com.st0x0ef.stellaris.client.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ExplodeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

@Environment(EnvType.CLIENT)
public class RocketParticle extends ExplodeParticle {

    private RocketParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        gravity = 2.5F;
    }

    @Override
    public void tick() {
        super.tick();
        yd -= 0.004D + 0.04D * gravity;
    }

    @Environment(EnvType.CLIENT)
    public record Provider(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new RocketParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
        }
    }

    @Environment(EnvType.CLIENT)
    public record SmallProvider(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            RocketParticle particle = new RocketParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
            particle.scale(0.5f);
            return particle;
        }
    }
}