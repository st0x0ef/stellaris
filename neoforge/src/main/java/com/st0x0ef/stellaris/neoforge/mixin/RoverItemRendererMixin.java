package com.st0x0ef.stellaris.neoforge.mixin;

import com.st0x0ef.stellaris.client.renderers.entities.vehicle.rover.RoverItemRenderer;
import com.st0x0ef.stellaris.common.items.RoverItem;
import com.st0x0ef.stellaris.common.registry.ItemRendererRegistry;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Consumer;

@Mixin(RoverItem.class)
public class RoverItemRendererMixin extends Item {

    public RoverItemRendererMixin(Properties properties) {
        super(properties);
    }

    @Unique
    public void stellaris$initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            public RoverItemRenderer getCustomRenderer() {
                return ItemRendererRegistry.ROVER_ITEM_RENDERER.get();
            }
        });
    }
}
