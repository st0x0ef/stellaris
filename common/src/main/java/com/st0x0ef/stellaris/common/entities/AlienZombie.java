package com.st0x0ef.stellaris.common.entities;

import com.st0x0ef.stellaris.common.entities.alien.Alien;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class AlienZombie extends Monster implements RangedAttackMob {
	public AlienZombie(EntityType<? extends Monster> type, Level world) {
		super(type, world);
		this.xpReward = 5;
	}

	public static AttributeSupplier.Builder setCustomAttributes() {
		return Mob.createMobAttributes()
		.add(Attributes.MOVEMENT_SPEED, 0.3)
		.add(Attributes.MAX_HEALTH, 20)
		.add(Attributes.ATTACK_DAMAGE, 3);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false));
		this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
		this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.8));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Player.class, false, false));
		this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, Alien.class, false, false));
		this.goalSelector.addGoal(1, new RangedAttackGoal(this, 1.25, 20, 15));
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource p_33034_) {
		return SoundEvents.PILLAGER_HURT;
	}

	@Override
	public SoundEvent getDeathSound() {
		return SoundEvents.PILLAGER_DEATH;
	}

	@Override
	public void performRangedAttack(LivingEntity livingEntity, float f) {
		ItemStack itemStack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW)));
		AbstractArrow abstractArrow = this.getArrow(itemStack, f);
		double d = livingEntity.getX() - this.getX();
		double e = livingEntity.getY(0.3333333333333333) - abstractArrow.getY();
		double g = livingEntity.getZ() - this.getZ();
		double h = Math.sqrt(d * d + g * g);
		abstractArrow.shoot(d, e + h * (double)0.2f, g, 1.6f, 14 - this.level().getDifficulty().getId() * 4);
		this.playSound(SoundEvents.GLASS_BREAK, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
		this.level().addFreshEntity(abstractArrow);
	}

	protected AbstractArrow getArrow(ItemStack itemStack, float f) {
		return ProjectileUtil.getMobArrow(this, itemStack, f);
	}

	@Override
	public boolean checkSpawnRules(LevelAccessor p_21686_, MobSpawnType p_21687_) {
		BlockState blockState = level().getBlockState(new BlockPos((int)this.getX(), (int)this.getY() - 1, (int)this.getZ()));

		if (blockState.is(Blocks.LAVA) || blockState.is(Blocks.AIR)) {
			return false;
		}

		return super.checkSpawnRules(p_21686_, p_21687_);
	}

	private boolean ALIEN_ZOMBIE_SPAWN = true;
	@Override
	public void tick() {
		super.tick();
		if (!ALIEN_ZOMBIE_SPAWN) {
			if (!this.level().isClientSide) {
				this.remove(RemovalReason.DISCARDED);
			}
		}
	}
}