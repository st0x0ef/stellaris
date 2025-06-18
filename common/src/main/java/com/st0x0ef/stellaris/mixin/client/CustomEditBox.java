package com.st0x0ef.stellaris.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.st0x0ef.stellaris.client.screens.components.CustomTexture;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EditBox.class)
public abstract class CustomEditBox extends AbstractWidget implements CustomTexture {

    @Unique
    private WidgetSprites stellaris$sprites = new WidgetSprites(ResourceLocation.withDefaultNamespace("widget/text_field"), ResourceLocation.withDefaultNamespace("widget/text_field_highlighted"));

    public CustomEditBox(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }


    @WrapOperation(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/WidgetSprites;get(ZZ)Lnet/minecraft/resources/ResourceLocation;"))
    private ResourceLocation getResourcelocation(WidgetSprites instance, boolean enabled, boolean focused, Operation<ResourceLocation> original) {
        return stellaris$sprites.get(enabled, focused);
    }

    @Override
    public void setSprites(WidgetSprites sprites) {
        this.stellaris$sprites = sprites;
    }
}
