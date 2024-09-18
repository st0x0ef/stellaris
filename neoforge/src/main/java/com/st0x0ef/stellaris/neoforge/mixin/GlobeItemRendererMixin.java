package com.st0x0ef.stellaris.neoforge.mixin;

import com.st0x0ef.stellaris.common.items.GlobeItem;
import com.st0x0ef.stellaris.common.registry.ItemRendererRegistry;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Consumer;

@Mixin(GlobeItem.class)
public class GlobeItemRendererMixin extends GlobeItem {

    public GlobeItemRendererMixin(Block block, ResourceLocation texture, Properties properties) {
        super(block, texture, properties);
    }

    @Override
    @SuppressWarnings("removal")
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return ItemRendererRegistry.GLOBE_ITEM_RENDERER.setTexture(GlobeItemRendererMixin.this.getTexture());
            }
        });
    }
}
