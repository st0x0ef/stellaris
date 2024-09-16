package com.st0x0ef.stellaris.neoforge.mixin;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.ConfigScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class MainMenuMixin extends Screen {
    protected MainMenuMixin(Component component) {
        super(component);
    }

//    @Inject(method = "init", at = @At(value = "TAIL"))
//    protected void init(CallbackInfo ci) {
//        int l = this.height / 4 + 48;
//
//        SpriteIconButton spriteIconButton = this.addRenderableWidget(stellaris$configButton((button) -> this.minecraft.setScreen(new ConfigScreen(this))));
//        spriteIconButton.setPosition(this.width / 2 - 124, l);
//    }
//
//    @Unique
//    private static SpriteIconButton stellaris$configButton(Button.OnPress onPress) {
//        return SpriteIconButton.builder(Component.translatable("screens.stellaris.config"), onPress, true).width(20).sprite(ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "icon/logo"), 16, 16).build();
//    }
}