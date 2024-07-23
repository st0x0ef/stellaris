package com.st0x0ef.stellaris.client.particles;


import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;

public class VenusRainParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    private final float angularVelocity;
    private final float angularAcceleration;
    public VenusRainParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(world, x, y, z);
        this.spriteSet = spriteSet;
        this.quadSize *= 1F;
        this.hasPhysics = true;
        this.angularVelocity = 0.1F;
        this.angularAcceleration = 0.01F;
        this.xd *= 0.3D;
        this.yd = Math.random() * 0.2D + 0.1D;
        this.zd *= 0.3D;
        this.setSize(0.01F, 0.01F);
        this.gravity = 0.06F;
        this.age = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
        this.pickSprite(spriteSet);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age-- <= 0) {
            this.remove();
        } else {
            this.yd -= this.gravity;
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.98D;
            this.yd *= 0.98D;
            this.zd *= 0.98D;
            if (this.onGround) {
                if (Math.random() < 0.5D) {
                    this.remove();
                }

                this.xd *= 0.7D;
                this.zd *= 0.7D;
            }

            BlockPos blockpos = new BlockPos((int)this.x, (int)this.y, (int)this.z);
            double d0 = Math.max(this.level.getBlockState(blockpos).getCollisionShape(this.level, blockpos).max(Direction.Axis.Y, this.x - (double)blockpos.getX(), this.z - (double)blockpos.getZ()), this.level.getFluidState(blockpos).getHeight(this.level, blockpos));
            if (d0 > 0.0D && this.y < (double)blockpos.getY() + d0) {
                this.remove();
            }
        }
    }

        public static class ParticleFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public ParticleFactory(SpriteSet  spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new VenusRainParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}
