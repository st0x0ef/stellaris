package com.st0x0ef.stellaris.common.items;


import com.st0x0ef.stellaris.common.entities.IceShardArrowEntity;
import com.st0x0ef.stellaris.common.registry.EntityRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class IceShardArrow extends ArrowItem {
    public IceShardArrow(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack itemStack, LivingEntity livingEntity) {
        return new IceShardArrowEntity(level, livingEntity, itemStack);
    }
}
