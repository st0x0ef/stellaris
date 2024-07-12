package com.st0x0ef.stellaris.common.oxygen;

import com.st0x0ef.stellaris.common.armors.JetSuit;
import com.st0x0ef.stellaris.common.registry.DamageSourceRegistry;
import com.st0x0ef.stellaris.common.registry.TagRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public class EntityOxygen {

    public static void tick(LivingEntity entity) {
        if (PlanetUtil.hasOxygen(entity.level().dimension().location()) || entity.getType().is(TagRegistry.ENTITY_NO_OXYGEN_NEEDED_TAG)) return;

        if (entity instanceof LivingEntity livingEntity && Utils.isLivingInJetSuit(livingEntity)) {
            JetSuit.Suit suit = (JetSuit.Suit) livingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem();
            if (!suit.oxygenContainer.removeOxygenStored(false)) {
                if (!OxygenManager.hasOxygenAt(entity.level(), entity.getOnPos())) {
                    entity.hurt(DamageSourceRegistry.of(entity.level(), DamageSourceRegistry.OXYGEN), 0.5f);
                }
            }
        } else if (!OxygenManager.hasOxygenAt(entity.level(), entity.getOnPos())) {
            entity.hurt(DamageSourceRegistry.of(entity.level(), DamageSourceRegistry.OXYGEN), 0.5f);
        }
    }
}
