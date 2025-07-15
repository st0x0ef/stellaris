package com.st0x0ef.stellaris.client.screens.components;

import com.fej1fun.potentials.fluid.UniversalFluidStorage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.st0x0ef.stellaris.common.utils.Utils;
import dev.architectury.hooks.fluid.FluidStackHooks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class FluidGaugeWidget extends AbstractWidget {

    private static final TextureAtlasSprite WATER_STILL_SPRITE = FluidStackHooks.getStillTexture(Fluids.WATER);

    private final int tank;
    protected final Supplier<? extends UniversalFluidStorage> fluidStorage;
    private Fluid currFluid = null;
    private int fluidColor = 1;

    protected TextureAtlasSprite sprite;
    protected @Nullable ResourceLocation overlaySprite;
    protected final GaugeWidget.Direction4 DIRECTION;

    protected int imageWidth;
    protected int imageHeight;


    public FluidGaugeWidget(int x, int y, int width, int height, Component message, Supplier<? extends UniversalFluidStorage> fluidStorage, int tank, GaugeWidget.Direction4 direction) {
        super(x, y, width, height, message);
        this.fluidStorage = fluidStorage;
        this.tank = tank;
        this.overlaySprite = null;

        this.DIRECTION = direction;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        UniversalFluidStorage fluids = fluidStorage.get();
        Fluid fluid = fluids.getFluidInTank(tank).getFluid();
        if (fluid != currFluid || currFluid == null) {
            currFluid = fluid;
            updateLiquid(currFluid);
        }

        long amount = fluids.getFluidInTank(tank).getAmount();
        long capacity = fluids.getTankCapacity(tank);

        RenderSystem.setShaderColor((float)(fluidColor >> 16 & 255) / 255.0F, (float)(fluidColor >> 8 & 255) / 255.0F, (float)(fluidColor & 255) / 255.0F, (float)(fluidColor >> 24 & 255) / 255.0F);
        switch (DIRECTION) {
            case DOWN_UP -> {
                int i = Mth.ceil(getProgress(amount, capacity) * (getHeight() - 1));
                for (int j = 0; j < width / imageWidth; j++) {
                    guiGraphics.blitSprite(sprite, imageWidth, getHeight(), 0, getHeight() - i, getX() + imageWidth * j, getY() + getHeight() - i,0, imageWidth, i);
                }
                int x = width % imageWidth;
                if (x > 0) {
                    guiGraphics.blitSprite(sprite, x, getHeight(), 0, getHeight() - i, getX() + width - x, getY() + getHeight() - i,0, x, i);
                }
            }
            case UP_DOWN -> {
                int i = Mth.ceil(getProgress(amount, capacity) * (getHeight() - 1));
                for (int j = 0; j < width / imageWidth; j++) {
                    guiGraphics.blitSprite(sprite, imageWidth, getHeight(), 0, 0, getX() + imageWidth * j, getY(),0, imageWidth, i);
                }
                int x = width % imageWidth;
                if (x > 0) {
                    guiGraphics.blitSprite(sprite, x, getHeight(), 0, 0, getX() + width - x, getY(),0, x, i);
                }
            }
            case LEFT_RIGHT -> {
                int i = Mth.ceil(getProgress(amount, capacity) * (getWidth() - 1));
                for (int j = 0; j < height / imageHeight; j++) {
                    guiGraphics.blitSprite(sprite, getWidth(), imageHeight, 0, 0, getX(), getY() + imageHeight * j,0, i, imageHeight);
                }
                int y = height % imageHeight;
                if (y > 0) {
                    guiGraphics.blitSprite(sprite, getWidth(), y, 0, 0, getX(), getY() + height - y,0, i, y);
                }
            }
            case RIGHT_LEFT -> {
                int i = Mth.ceil(getProgress(amount, capacity) * (getWidth() - 1));
                for (int j = 0; j < height / imageHeight; j++) {
                    guiGraphics.blitSprite(sprite, getWidth(), imageHeight, getWidth() - i, 0, getX() + getWidth() - i, getY() + imageHeight * j,0, i, imageHeight);
                }
                int y = height % imageHeight;
                if (y > 0) {
                    guiGraphics.blitSprite(sprite, getWidth(), y, getWidth() - i, 0, getX() + getWidth() - i, getY() + height - y,0, i, y);
                }
            }
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.overlaySprite != null) {
            guiGraphics.blitSprite(overlaySprite, getX(), getY(), width, height);
        }
    }


    public void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY, Font font) {
        this.renderTooltips(graphics, mouseX, mouseY, font, list -> {
        });
    }

    public void renderTooltips(GuiGraphics graphics, int mouseX, int mouseY, Font font, Consumer<List<Component>> components) {
        UniversalFluidStorage fluids = fluidStorage.get();
        long amount = fluids.getFluidInTank(tank).getAmount();
        long capacity = fluids.getTankCapacity(tank);

        String GaugeComponent = getMessage().getString() + " : " + amount + " / " + capacity;
        Component capacityComponent;

        if (amount >= capacity) {
            capacityComponent = Utils.getMessageComponent(GaugeComponent, "Lime");
        }
        else if (amount <= 0) {
            capacityComponent = Utils.getMessageComponent(GaugeComponent, "Red");
        }
        else {
            capacityComponent = Utils.getMessageComponent(GaugeComponent, "Orange");
        }

        List<Component> components1 = new ArrayList<>();
        components.accept(components1);
        components1.addFirst(capacityComponent);
        if (mouseX >= this.getX() && mouseX <= this.getX() + width && mouseY >= this.getY() && mouseY <= this.getY() + this.height) {
            graphics.renderComponentTooltip(font, components1, mouseX, mouseY);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }



    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    protected double getProgress(Long amount, Long capacity) {
        return Mth.clamp((double) amount / (double) capacity, 0.0D, 1.0D);
    }

    public FluidGaugeWidget setOverlaySprite(@Nullable ResourceLocation overlaySprite) {
        this.overlaySprite = overlaySprite;
        return this;
    }

    private void updateLiquid(Fluid fluid) {
        TextureAtlasSprite sprite = FluidStackHooks.getStillTexture(fluid);
        if (sprite == null || fluid == Fluids.WATER)
            sprite = WATER_STILL_SPRITE;
        fluidColor = FluidStackHooks.getColor(fluid);

        assert sprite != null;
        SpriteContents contents = sprite.contents();

        this.imageHeight = contents.height();
        this.imageWidth = contents.width();

        this.sprite = sprite;
    }

}
