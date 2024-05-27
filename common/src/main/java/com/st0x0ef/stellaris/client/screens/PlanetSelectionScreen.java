package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.components.InvisibleButton;
import com.st0x0ef.stellaris.client.screens.components.LaunchButton;
import com.st0x0ef.stellaris.client.screens.components.ModifiedButton;
import com.st0x0ef.stellaris.client.screens.info.CelestialBody;
import com.st0x0ef.stellaris.client.screens.info.MoonInfo;
import com.st0x0ef.stellaris.client.screens.info.PlanetInfo;
import com.st0x0ef.stellaris.common.menus.PlanetSelectionMenu;
import com.st0x0ef.stellaris.common.network.NetworkRegistry;
import com.st0x0ef.stellaris.common.network.packets.TeleportEntity;
import com.st0x0ef.stellaris.common.registry.EntityData;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


@Environment(EnvType.CLIENT)
public class PlanetSelectionScreen extends AbstractContainerScreen<PlanetSelectionMenu> {

    public static final ResourceLocation HIGHLIGHTER_TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/gui/util/planet_highlighter.png");
    public static final ResourceLocation BLACK_TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/gui/util/black.png");

    public static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/gui/planet_selection.png");
    public static final ResourceLocation SCROLLER_TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/gui/util/scroller.png");

    public static final ResourceLocation SMALL_BUTTON_TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/gui/util/buttons/small_button.png");
    public static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/gui/util/buttons/button.png");
    public static final ResourceLocation LARGE_BUTTON_TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/gui/util/buttons/large_button.png");
    public static final ResourceLocation LAUNCH_BUTTON_TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/gui/util/buttons/launch_button.png");

    public static final ResourceLocation SMALL_MENU_LIST = new ResourceLocation(Stellaris.MODID, "textures/gui/util/planet_menu.png");
    public static final ResourceLocation LARGE_MENU_TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/gui/util/large_planet_menu.png");

    public static final List<CelestialBody> STARS = new ArrayList<>();
    public static final List<PlanetInfo> PLANETS = new ArrayList<>();
    public static final List<MoonInfo> MOONS = new ArrayList<>();

    public static final Component temperature = Component.translatable("text.stellaris.planetscreen.temperature");
    public static final Component gravity = Component.translatable("text.stellaris.planetscreen.gravity");
    public static final Component launch = Component.translatable("text.stellaris.planetscreen.launch");
    public static final Component oxygen = Component.translatable("text.stellaris.planetscreen.oxygen");
    public static final Component system = Component.translatable("text.stellaris.planetscreen.system");

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final long UPDATE_INTERVAL = 1L;

    private boolean isLaunching = false;
    private boolean showLargeMenu = false;

    private double offsetX = 0;
    private double offsetY = 0;

    private double lastMouseX;
    private double lastMouseY;
    private boolean dragging = false;

    private double zoomLevel = 1.0;
    private GLFWScrollCallback prevScrollCallback;

    private List<InvisibleButton> planetButtons = new ArrayList<InvisibleButton>();
    private List<InvisibleButton> moonButtons = new ArrayList<InvisibleButton>();
    private LaunchButton launchButton;

    public PlanetSelectionScreen(PlanetSelectionMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        startUpdating();
        this.imageWidth = 1200;
        this.imageHeight = 1600;
        this.inventoryLabelY = this.imageHeight - 110;
    }

    @Override
    protected void init() {
        super.init();
        getMenu().freeze_gui = false;
        centerSun();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        long windowHandle = Minecraft.getInstance().getWindow().getWindow();
        prevScrollCallback = GLFW.glfwSetScrollCallback(windowHandle, this::onMouseScroll);

        initializeAllButtons();
    }

    private void initializeAllButtons() {
        initializePlanetButtons();
        initializeMoonButtons();
        initializeLaunchButton();
    }

    private void initializePlanetButtons() {
        planetButtons.clear();
        for (PlanetInfo planet : PLANETS) {
            int planetWidth = (int) (planet.width * zoomLevel);
            int planetHeight = (int) (planet.height * zoomLevel);
            float planetX = (float) ((planet.orbitCenter.x + offsetX + planet.orbitRadius * Math.cos(planet.currentAngle) - planetWidth / 2) * zoomLevel);
            float planetY = (float) ((planet.orbitCenter.y + offsetY + planet.orbitRadius * Math.sin(planet.currentAngle) - planetHeight / 2) * zoomLevel);

            InvisibleButton button = new InvisibleButton(
                    (int) planetX, (int) planetY, planetWidth + 2, planetHeight + 2,
                    Component.literal(planet.name),
                    (btn) -> onPlanetButtonClick(planet),
                    () -> hoveredBody = planet
            );

            planetButtons.add(button);
            addRenderableWidget(button);
        }
    }

    private void initializeMoonButtons() {
        moonButtons.clear();
        for (MoonInfo moon : MOONS) {
            int moonWidth = (int) (moon.width * zoomLevel);
            int moonHeight = (int) (moon.height * zoomLevel);
            float moonX = (float) ((moon.orbitCenter.x + offsetX + moon.orbitRadius * Math.cos(moon.currentAngle) - moonWidth / 2) * zoomLevel);
            float moonY = (float) ((moon.orbitCenter.y + offsetY + moon.orbitRadius * Math.sin(moon.currentAngle) - moonHeight / 2) * zoomLevel);

            InvisibleButton button = new InvisibleButton(
                    (int) moonX, (int) moonY, moonWidth, moonHeight,
                    Component.literal(moon.name),
                    (btn) -> onMoonButtonClick(moon),
                    () -> hoveredBody = moon
            );
            moonButtons.add(button);
            addRenderableWidget(button);
        }
    }

    private void initializeLaunchButton() {
        int buttonWidth = 74;
        int buttonHeight = 20;
        int buttonX = (this.width - buttonWidth) / 4 + 100;
        int buttonY = (this.height - buttonHeight) / 4;
        launchButton = new LaunchButton(buttonX, buttonY, buttonWidth, buttonHeight, Component.literal("Launch"), (btn) -> onLaunchButtonClick());
        this.addRenderableWidget(launchButton);
        launchButton.visible = false;
    }

    private void startUpdating() {
        scheduler.scheduleAtFixedRate(this::updatePlanets, 0, UPDATE_INTERVAL, TimeUnit.MILLISECONDS);
    }

    private CelestialBody focusedBody = null;
    private CelestialBody hoveredBody = null;

    private void onPlanetButtonClick(PlanetInfo planet) {
        if (!showLargeMenu) {
            focusedBody = planet;
            centerOnBody(planet);
            showLargeMenu = true;
        }
    }

    private void onMoonButtonClick(MoonInfo moon) {
        if (!showLargeMenu) {
            focusedBody = moon;
            centerOnBody(moon);
            showLargeMenu = true;
        }
    }

    private void onLaunchButtonClick() {
        if (focusedBody != null) {
            showLargeMenu = false;

            tpToFocusedPlanet();
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);

        if (focusedBody != null) {
            centerOnBody(focusedBody);
        }

        drawOrbits();

        renderBodiesAndPlanets(graphics);
        renderHighlighter(graphics);
        renderLargeMenu(graphics);

        this.renderTooltip(graphics, mouseX, mouseY);
    }


    @Override
    protected void renderBg(GuiGraphics graphics, float var2, int var3, int var4) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        graphics.blit(BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
    }

    public void renderBodiesAndPlanets(GuiGraphics graphics) {
        Font font = Minecraft.getInstance().font;

        for (CelestialBody body : STARS) {
            float bodyX = (float) ((body.x + offsetX) * zoomLevel - (body.width / 2) * zoomLevel);
            float bodyY = (float) ((body.y + offsetY) * zoomLevel - (body.height / 2) * zoomLevel);

            int bodyWidth = (int) (body.width * zoomLevel);
            int bodyHeight = (int) (body.height * zoomLevel);

            graphics.blit(body.texture, (int) bodyX, (int) bodyY, 0, 0, bodyWidth, bodyHeight, bodyWidth, bodyHeight);

            int nameWidth = font.width(body.name);
            graphics.drawString(font, body.translatable, (int) (bodyX + bodyWidth / 2 - nameWidth / 2), (int) (bodyY + bodyHeight), 0xFFFFFF);
        }

        for (PlanetInfo planet : PLANETS) {
            CelestialBody orbitCenter = planet.orbitCenter;

            float orbitCenterX = (float) ((orbitCenter.x + offsetX) * zoomLevel);
            float orbitCenterY = (float) ((orbitCenter.y + offsetY) * zoomLevel);

            float planetX = (float) (orbitCenterX + planet.orbitRadius * zoomLevel * Math.cos(planet.currentAngle) - planet.width / 2 * zoomLevel);
            float planetY = (float) (orbitCenterY + planet.orbitRadius * zoomLevel * Math.sin(planet.currentAngle) - planet.height / 2 * zoomLevel);

            int planetWidth = (int) (planet.width * zoomLevel);
            int planetHeight = (int) (planet.height * zoomLevel);

            graphics.blit(planet.texture, (int) planetX, (int) planetY, 0, 0, planetWidth, planetHeight, planetWidth, planetHeight);

            int nameWidth = font.width(planet.name);
            graphics.drawString(font, planet.name, (int) (planetX + planetWidth / 2 - nameWidth / 2), (int) (planetY + planetHeight), 0xFFFFFF);

        }

        for (MoonInfo moon : MOONS) {
            float moonX = (float) ((moon.x + offsetX) * zoomLevel - (moon.width / 2) * zoomLevel);
            float moonY = (float) ((moon.y + offsetY) * zoomLevel - (moon.height / 2) * zoomLevel);

            int moonWidth = (int) (moon.width * zoomLevel);
            int moonHeight = (int) (moon.height * zoomLevel);

            graphics.blit(moon.texture, (int) moonX, (int) moonY, 0, 0, moonWidth, moonHeight, moonWidth, moonHeight);
        }

        initializePlanetButtons();
        initializeMoonButtons();
    }

    private int currentHighlighterFrame = 0;
    private final int totalHighlighterFrames = 31;

    private void renderHighlighter(GuiGraphics graphics) {
        if (!showLargeMenu) {
            CelestialBody bodyToHighlight = hoveredBody != null ? hoveredBody : focusedBody;
            if (bodyToHighlight != null) {
                int highlightWidth = (int) (bodyToHighlight.width * zoomLevel);
                int highlightHeight = (int) (bodyToHighlight.height * zoomLevel);
                float highlightX = (float) ((bodyToHighlight.x + offsetX) * zoomLevel - highlightWidth / 2);
                float highlightY = (float) ((bodyToHighlight.y + offsetY) * zoomLevel - highlightHeight / 2);

                currentHighlighterFrame = (currentHighlighterFrame + 1) % totalHighlighterFrames;

                int frameHeight = highlightHeight;
                int frameY = currentHighlighterFrame * frameHeight;

                graphics.blit(HIGHLIGHTER_TEXTURE, (int) highlightX, (int) highlightY, 0, frameY, highlightWidth, highlightHeight, highlightWidth, totalHighlighterFrames * frameHeight);
            }
        }
    }


    private void renderLargeMenu(GuiGraphics graphics) {
        if (showLargeMenu) {
            getMenu().freeze_gui = false;
            ResourceLocation CELESTIAL_BODY_TEXTURE = focusedBody.texture;

            Component CELESTIAL_BODY_NAME = focusedBody.translatable;

            Float CELESTIAL_BODY_TEMPERATURE = PlanetUtil.getPlanet(focusedBody.dimension).temperature();
            Float CELESTIAL_BODY_GRAVITY = PlanetUtil.getPlanet(focusedBody.dimension).temperature();
            Boolean CELESTIAL_BODY_OXYGEN = PlanetUtil.getPlanet(focusedBody.dimension).oxygen();
            String CELESTIAL_BODY_SYSTEM = PlanetUtil.getPlanet(focusedBody.dimension).system();

            Component systemTranslatable;

            Component temperatureV = null;
            Component gravityV = null;
            Component oxygenV = null;
            Component systemV = null;

            int oxygenColor = 0xFFFFF;
            int temperatureColor = 0xFFFFF;

            if (CELESTIAL_BODY_SYSTEM == null) {
                systemV = Component.literal(system + " : null");
            } else {
                systemTranslatable = Component.translatable(PlanetUtil.getPlanet(focusedBody.dimension).system());
                systemV = Component.literal(system.getString() + " : " + systemTranslatable.getString());
            }

            if (CELESTIAL_BODY_TEMPERATURE == null) {
                temperatureV = Component.literal(temperature.getString() + " : null");
            } else {
                temperatureV = Component.literal(temperature.getString() + " : " + PlanetUtil.getPlanet(focusedBody.dimension).temperature() + "°C");
            }

            if (CELESTIAL_BODY_OXYGEN == null ){
                oxygenV = Component.literal(oxygen.getString() + " : null");
            } else {
                oxygenV = Component.literal(oxygen.getString() + " : " + PlanetUtil.getPlanet(focusedBody.dimension).oxygen());
            }

            if (CELESTIAL_BODY_GRAVITY == null) {
                gravityV = Component.literal(gravity.getString() + " : null");
            } else {
                gravityV = Component.literal(gravity.getString() + " : " + PlanetUtil.getPlanet(focusedBody.dimension).gravity() + "m/s");
            }

            if (CELESTIAL_BODY_OXYGEN == true) {
                oxygenColor = Utils.getColorHexCode("Lime");
            } else {
                oxygenColor = Utils.getColorHexCode("Red");
            }

            if (CELESTIAL_BODY_TEMPERATURE >= 100) {
                temperatureColor = Utils.getColorHexCode("DarkRed");
            } else if (CELESTIAL_BODY_TEMPERATURE >= 0){
                temperatureColor = Utils.getColorHexCode("Lime");
            } else if (CELESTIAL_BODY_TEMPERATURE >= -100) {
                temperatureColor = Utils.getColorHexCode("Cyan");
            } else {
                temperatureColor = Utils.getColorHexCode("Blue");
            }

            int menuWidth = 215;
            int menuHeight = 177;

            int buttonWidth = 74;
            int buttonHeight = 20;

            int centerX = (this.width - menuWidth) / 2;
            int centerY = (this.height - menuHeight) / 2;

            int buttonX = centerX + buttonWidth / 2 - buttonWidth / 3 - buttonWidth / 15;
            int buttonY = centerY + buttonHeight / 2 + 1;

            int textX = buttonX + buttonWidth / 4 - 20;

            float alpha = 0.5f;

            launchButton.visible = true;
            launchButton.setPosition(buttonX, buttonY);

            graphics.drawString(font, launch, buttonX + buttonWidth / 4, buttonY + buttonHeight / 4 + 1, 0xFFFFFF);
            graphics.drawString(font, CELESTIAL_BODY_NAME, textX, buttonY + buttonHeight / 4 + 37, 0xFFFFFF, true);

            graphics.drawString(font, "--------------------", textX, buttonY + buttonHeight / 4 + 50, 0xFFFFFF, false);

            graphics.drawString(font, temperatureV, textX, buttonY + buttonHeight / 4 + 60, temperatureColor, false);
            graphics.drawString(font, gravityV, textX, buttonY + buttonHeight / 4 + 75, 0xFFFFFF, false);
            graphics.drawString(font, oxygenV, textX, buttonY + buttonHeight / 4 + 90, oxygenColor, false);

            graphics.drawString(font, systemV, textX, buttonY + buttonHeight / 4 + 105, 0xFFFFFF, false);
//            graphics.drawString(font, "temporary : null", textX, buttonY + buttonHeight / 4 + 120, 0xFFFFFF, false);
//            graphics.drawString(font, "temporary : null", textX, buttonY + buttonHeight / 4 + 135, 0xFFFFFF, false);

            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);

            RenderSystem.setShaderTexture(0, LARGE_MENU_TEXTURE);
            graphics.blit(LARGE_MENU_TEXTURE, centerX, centerY, 0, 0, menuWidth, menuHeight, menuWidth, menuHeight);

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            RenderSystem.setShaderTexture(0, BLACK_TEXTURE);
            graphics.blit(BLACK_TEXTURE, centerX + menuWidth - 64, centerY + menuHeight / 2 - 32, 0, 0, 48, 48, 48, 48);

            RenderSystem.setShaderTexture(0, CELESTIAL_BODY_TEXTURE);
            graphics.blit(CELESTIAL_BODY_TEXTURE, centerX + menuWidth - 46, centerY + menuHeight / 2 - 14, 0, 0, 12, 12, 12, 12);

            RenderSystem.disableBlend();
        } else {
            launchButton.visible = false;
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_Z) {
            tpToFocusedPlanet();
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public void tpToFocusedPlanet() {
        if(focusedBody != null) {
            NetworkRegistry.CHANNEL.sendToServer(new TeleportEntity(focusedBody.dimension, false));
        } else {
            Stellaris.LOG.error("Focused body is null");
        }
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    private void updatePlanets() {
        long time = Util.getMillis();
        if (!getMenu().freeze_gui) {
            for (PlanetInfo planet : PLANETS) {
                planet.updateAngle(time);
                planet.updatePosition();
            }
            for (MoonInfo moon : MOONS) {
                moon.updateAngle(time);
                moon.updatePosition();
            }
        }
    }

    public void drawOrbits() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        GL11.glLineWidth(2.0F);

        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

        for (PlanetInfo planet : PLANETS) {
            CelestialBody orbitCenter = planet.orbitCenter;

            float orbitCenterX = (float) ((orbitCenter.x + offsetX) * zoomLevel);
            float orbitCenterY = (float) ((orbitCenter.y + offsetY) * zoomLevel);

            renderOrbits(bufferBuilder, orbitCenterX, orbitCenterY, planet.orbitRadius * zoomLevel, 75, orbitCenter.orbitColor, 1.0F);
        }

        for (MoonInfo moon : MOONS) {
            CelestialBody orbitCenter = moon.orbitCenter;

            float orbitCenterX = (float) ((orbitCenter.x + offsetX) * zoomLevel);
            float orbitCenterY = (float) ((orbitCenter.y + offsetY) * zoomLevel);

            renderOrbits(bufferBuilder, orbitCenterX, orbitCenterY, moon.orbitRadius * zoomLevel, 75, 0x888888, 0.5F);
        }

        Tesselator.getInstance().end();

        RenderSystem.disableBlend();
    }

    public static void renderOrbits(BufferBuilder bufferBuilder, double centerX, double centerY, double radius, int sides, int color, float alphaL) {
        float red = ((color >> 16) & 0xFF) / 255.0F;
        float green = ((color >> 8) & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;

        double angleStep = 2.0 * Math.PI / sides;

        for (int i = 0; i < sides; i++) {
            double currentAngle = i * angleStep;
            double nextAngle = currentAngle + angleStep;

            double vertex1X = centerX + radius * Math.cos(currentAngle);
            double vertex1Y = centerY + radius * Math.sin(currentAngle);
            double vertex2X = centerX + radius * Math.cos(nextAngle);
            double vertex2Y = centerY + radius * Math.sin(nextAngle);

            bufferBuilder.vertex(vertex1X, vertex1Y, 0).color(red, green, blue, alphaL).endVertex();
            bufferBuilder.vertex(vertex2X, vertex2Y, 0).color(red, green, blue, alphaL).endVertex();
        }

    }

    private void centerSun() {
        float centerX = width / 2.0f;
        float centerY = height / 2.0f;
        findByNameStar("Sun").setPosition(centerX, centerY);
        offsetX = 0;
        offsetY = 0;
    }

    public static CelestialBody findByNameStar(String name) {
        for (CelestialBody body : PlanetSelectionScreen.STARS) {
            if (body.getName().equals(name)) {
                return body;
            }
        }
        return null;
    }

    public static PlanetInfo findByNamePlanet(String name) {
        for (PlanetInfo body : PlanetSelectionScreen.PLANETS) {
            if (body.getName().equals(name)) {
                return body;
            }
        }
        return null;
    }

    private void centerOnBody(CelestialBody body) {
        if (!isLaunching) {
            zoomLevel = 1.0;
        } else {
            zoomLevel = 1.6;
        }
        offsetX = ((body.x - width / 2.0)) * -1;
        offsetY = ((body.y - height / 2.0)) * -1;
    }

    private void onMouseScroll(long window, double scrollX, double scrollY) {
        double[] mouseX = new double[1];
        double[] mouseY = new double[1];

        GLFW.glfwGetCursorPos(window, mouseX, mouseY);

        if (this.minecraft != null && this.minecraft.player != null) {
            handleHotbarScroll(scrollY);

            if (this.minecraft.screen instanceof CreativeModeInventoryScreen) {
                CreativeModeInventoryScreen creativeScreen = (CreativeModeInventoryScreen) this.minecraft.screen;
                if (creativeScreen.mouseScrolled(mouseX[0], mouseY[0], scrollX, scrollY)) {
                    return;
                }
            }

            if (this.minecraft.screen instanceof PlanetSelectionScreen) {
                if (scrollY != 0) {
                    zoomLevel += scrollY * 0.02;
                    zoomLevel = Math.max(0.02, Math.min(zoomLevel, 2.0));
                }
            }
            }
    }

    private boolean handleHotbarScroll(double scrollY) {
        if (this.minecraft != null && this.minecraft.player != null) {
            int currentSlot = this.minecraft.player.getInventory().selected;
            int newSlot = currentSlot - (int) scrollY;

            newSlot = (newSlot + 9) % 9;

            if (newSlot != currentSlot) {
                this.minecraft.player.getInventory().selected = newSlot;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT || button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            dragging = true;
            lastMouseX = mouseX;
            lastMouseY = mouseY;
            if (showLargeMenu) {
                if (launchButton.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
                showLargeMenu = false;
                return true;
            } else {
                focusedBody = null;
                hoveredBody = null;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }


    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT || button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            dragging = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging) {
            offsetX += Utils.changeLastDigitToEven((mouseX - lastMouseX) / zoomLevel);
            offsetY += Utils.changeLastDigitToEven((mouseY - lastMouseY) / zoomLevel);
            lastMouseX = mouseX;
            lastMouseY = mouseY;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public PlanetSelectionMenu getMenu() {
        return this.menu;
    }

    @Override
    public void onClose() {
        if(getPlayer().getEntityData().get(EntityData.DATA_PLANET_MENU_OPEN)) {
            return;
        }
        super.onClose();
    }

    public Player getPlayer() {
        return menu.getPlayer();
    }
    public ModifiedButton addButton(int x, int y, int row, int width, int height, boolean rocketCondition,
                                    ModifiedButton.ButtonTypes type, List<String> list, ResourceLocation buttonTexture,
                                    ModifiedButton.ColorTypes colorType, Component title, Button.OnPress onPress) {
        return this.addRenderableWidget(new ModifiedButton(x, y, row, width, height, 0, 0, 0,
                rocketCondition, type, list, buttonTexture, colorType, width, height, onPress, title));
    }

}
