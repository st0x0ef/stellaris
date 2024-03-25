package com.st0x0ef.stellaris.fabric.mixin;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.fabric.client.ConfigScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CommonButtons;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.screens.LanguageSelectScreen;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class MainMenuMixin extends Screen {
    protected MainMenuMixin(Component component) {
        super(component);
    }

    @Inject(method = "init", at = @At(value = "TAIL"))
    protected void init(CallbackInfo ci) {
        int l = this.height / 4 + 48;


        SpriteIconButton spriteIconButton = (SpriteIconButton)this.addRenderableWidget(stellarisConfigButton(20, (button) -> {
            this.minecraft.setScreen(new ConfigScreen(this));
        }, true));
        spriteIconButton.setPosition(this.width / 2 - 124, l);
    }

    private static SpriteIconButton stellarisConfigButton(int i, Button.OnPress onPress, boolean bl) {
        Component component = bl ? Component.translatable("screens.stellaris.config") : Component.translatable("screens.stellaris.config");
        return SpriteIconButton.builder(component, onPress, bl).width(i).sprite(new ResourceLocation("icon/accessibility"), 15, 15).build();
    }
}
