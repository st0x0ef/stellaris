package com.st0x0ef.stellaris.mixin;

import com.st0x0ef.stellaris.common.network.packets.SyncPlanetMenuState;
import com.st0x0ef.stellaris.common.registry.EntityData;
import com.st0x0ef.stellaris.common.utils.CustomPlayerData;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerDataMixin extends LivingEntity implements CustomPlayerData {

    @Unique
    private boolean stellaris$isPlanetMenuOpened = false;

    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot slot);

    protected PlayerDataMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void stellaris$setPlanetMenuOpen(boolean open, Player player, boolean sync) {
        this.stellaris$isPlanetMenuOpened = open;
        if (sync && player instanceof ServerPlayer serverPlayer) NetworkManager.sendToPlayer(serverPlayer, new SyncPlanetMenuState(open));
    }

    @Override
    public boolean stellaris$isPlanetMenuOpen() {
        return stellaris$isPlanetMenuOpened;
    }
}
