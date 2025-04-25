package com.st0x0ef.stellaris.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.components.*;
import com.st0x0ef.stellaris.client.screens.helper.ScreenHelper;
import com.st0x0ef.stellaris.client.screens.info.*;
import com.st0x0ef.stellaris.client.screens.etc.StarMovement;
import com.st0x0ef.stellaris.client.screens.etc.Trail;
import com.st0x0ef.stellaris.client.screens.record.PSystemRecord;
import com.st0x0ef.stellaris.client.screens.windows.SpaceStationWindow;
import com.st0x0ef.stellaris.common.data.planets.Planet;
import com.st0x0ef.stellaris.common.data.recipes.SpaceStationRecipe;
import com.st0x0ef.stellaris.common.data.recipes.SpaceStationRecipesManager;
import com.st0x0ef.stellaris.common.entities.vehicles.RocketEntity;
import com.st0x0ef.stellaris.common.menus.PlanetSelectionMenu;
import com.st0x0ef.stellaris.common.network.packets.OpenMilkyWayMenuPacket;
import com.st0x0ef.stellaris.common.network.packets.PlaceStationPacket;
import com.st0x0ef.stellaris.common.network.packets.TeleportEntityToPlanetPacket;
import com.st0x0ef.stellaris.common.registry.EntityData;
import com.st0x0ef.stellaris.common.registry.TranslatableRegistry;
import com.st0x0ef.stellaris.common.utils.PlanetUtil;
import com.st0x0ef.stellaris.common.utils.Utils;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWScrollCallback;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static com.st0x0ef.stellaris.common.utils.Utils.isHoveredOnSprite;

@Environment(EnvType.CLIENT)
public class PlanetSelectionScreen extends BaseWindowScreen<PlanetSelectionMenu> {

    public static final ResourceLocation HIGHLIGHTER_TEXTURE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/planet_highlighter.png");
    public static final ResourceLocation BLACK_TEXTURE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/black.png");

    public static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/planet_selection.png");
    public static final ResourceLocation SCROLLER_TEXTURE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/scroller.png");

    public static final ResourceLocation SMALL_BUTTON_TEXTURE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/small_button.png");
    public static final ResourceLocation BUTTON_TEXTURE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/button.png");
    public static final ResourceLocation LARGE_BUTTON_TEXTURE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/large_button.png");
    public static final ResourceLocation LAUNCH_BUTTON_TEXTURE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/launch_button.png");
    public static final ResourceLocation NEXT_BUTTON = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/next_button.png");
    public static final ResourceLocation BACK_BUTTON = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/back_button.png");


    public static final ResourceLocation SMALL_MENU_LIST = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/planet_menu.png");
    public static final ResourceLocation LARGE_MENU_TEXTURE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/large_planet_menu.png");
    public static final ResourceLocation LARGE_MENU_TEXTURE_RED = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/large_planet_menu_red.png");

    public static final List<CelestialBody> STARS = new ArrayList<>();
    public static final List<PlanetInfo> PLANETS = new ArrayList<>();
    public static final List<MoonInfo> MOONS = new ArrayList<>();
    public static final List<PSystemInfo> PSYSTEMS = new ArrayList<>();


    public static final Component temperature = Component.translatable("text.stellaris.planetscreen.temperature");
    public static final Component gravity = Component.translatable("text.stellaris.planetscreen.gravity");
    public static final Component launch = Component.translatable("text.stellaris.planetscreen.launch");
    public static final Component oxygen = Component.translatable("text.stellaris.planetscreen.oxygen");
    public static final Component system = Component.translatable("text.stellaris.planetscreen.system");
    public static final Component error_message = Component.translatable("text.stellaris.planetscreen.error_message");


    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final long UPDATE_INTERVAL = 1L;

    private boolean isLaunching = false;
    private boolean showLargeMenu = false;
    private boolean showHelpMenu = true;
    private boolean showSpaceStationMenu = false;

    LaunchButton launchButton;

    private double offsetX = 0;
    private double offsetY = 0;

    private double lastMouseX;
    private double lastMouseY;
    public boolean dragging = false;

    public boolean isPausePressed = false;
    private boolean isShiftPressed = false;
    private boolean isWheelButtonDown = false;
    public boolean isPlanetScreenOpened;

    public static CelestialBody focusedBody = null;
    public static CelestialBody hoveredBody = null;

    private double zoomLevel = 1.0;
    private double targetZoomLevel = 1.0;

    private double targetOffsetX = 0;
    private double targetOffsetY = 0;

    private GLFWScrollCallback prevScrollCallback;

    public ArrayList<ArrayList<TexturedButton>> planetsListButton = new ArrayList<>();
    public int currentPage = 0;
    public ArrayList<TexturedButton> spaceStationButtons = new ArrayList<>();

    private final List<InvisibleButton> planetButtons = new ArrayList<>();
    private final List<InvisibleButton> moonButtons = new ArrayList<>();

    public ArrayList<SpaceStationRecipesManager.SpaceStationRecipeState> spaceStationRecipeStates = new ArrayList<>();

    private SpaceStationWindow spaceStationWindow;

    public PlanetSelectionScreen(PlanetSelectionMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        this.imageWidth = 1200;
        this.imageHeight = 1600;
        this.inventoryLabelY = this.imageHeight - 110;
        initializeLaunchButton();
    }

    @Override
    protected void init() {
        super.init();
        getMenu().freeze_gui = false;

        this.spaceStationWindow = new SpaceStationWindow(300,200, Component.literal("eee"), this);

        addRenderableWidget(this.spaceStationWindow);
        this.spaceStationWindow.changeVisibility(false);

        centerSun();
        isPlanetScreenOpened = true;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        prevScrollCallback = GLFW.glfwSetScrollCallback(Minecraft.getInstance().getWindow().getWindow(), this::onMouseScroll);
        initializeAllButtons();
        initSpaceStationButtons();

        zoomLevel = 1;
        targetZoomLevel = 1;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBg(graphics, partialTicks, mouseX, mouseY);
        super.render(graphics, mouseX, mouseY, partialTicks);

        if (!isPausePressed) {
            updatePlanets();
        }
        updateZoomAndOffsetAnimation();
        if (focusedBody != null) {
            centerOnBody(focusedBody);
        }
        if (!showLargeMenu || !showSpaceStationMenu) {
            launchButton.visible = false;
        }

        renderHelp(graphics);
         drawOrbits();
        drawTrails();

        renderBodiesAndPlanets(graphics);

        hoveredBody = null;

        for (MoonInfo moon : MOONS) {
            int moonWidth = (int) (moon.width * zoomLevel);
            int moonHeight = (int) (moon.height * zoomLevel);
            float moonX = (float) ((moon.x + offsetX) * zoomLevel - moonWidth / 2);
            float moonY = (float) ((moon.y + offsetY) * zoomLevel - moonHeight / 2);

            if (mouseX >= moonX && mouseX <= moonX + moonWidth &&
                    mouseY >= moonY && mouseY <= moonY + moonHeight) {
                hoveredBody = moon;
                break;
            }
        }

        if (hoveredBody == null) {
            for (PlanetInfo planet : PLANETS) {
                int planetWidth = (int) (planet.width * zoomLevel);
                int planetHeight = (int) (planet.height * zoomLevel);
                float planetX = (float) ((planet.orbitCenter.x + offsetX + planet.orbitRadius * Math.cos(planet.currentAngle) - planetWidth / 2) * zoomLevel);
                float planetY = (float) ((planet.orbitCenter.y + offsetY + planet.orbitRadius * Math.sin(planet.currentAngle) - planetHeight / 2) * zoomLevel);

                if (mouseX >= planetX && mouseX <= planetX + planetWidth &&
                        mouseY >= planetY && mouseY <= planetY + planetHeight) {
                    hoveredBody = planet;
                    break;
                }
            }
        }

        if (hoveredBody != null) {
            updateHighlighterPosition(graphics, hoveredBody);
        }
        if (focusedBody != null) {
            updateHighlighterPosition(graphics, focusedBody);
        }

        if (focusedBody != null) { renderLargeMenu(graphics); };
        renderSpaceStation(graphics, mouseX, mouseY, partialTicks);

        initTop(graphics, mouseX, mouseY);
        etc();


        this.renderTooltip(graphics, mouseX, mouseY);
    }

    /** just some stuffs group for common things**/
    public void etc() {
        if (focusedBody == findByNameMoon("stellaris:deimos") || focusedBody == findByNameMoon("stellaris:phobos")) focusedBody = findByNamePlanet("stellaris:mars");
    }

    public boolean canLaunch(Planet planet) {
        if (this.getMenu().getForceCanGoTo()) return true;

        Player player = this.getPlayer();
        if (player == null) {
            return false;
        }
        Entity vehicle = player.getVehicle();
        if (vehicle instanceof RocketEntity rocket) {
            if (PlanetUtil.isPlanet(player.level().dimension().location())) {
                return rocket.canGoTo(PlanetUtil.getPlanet(player.level().dimension().location()), planet);
            }
            return rocket.canGoTo(PlanetUtil.getPlanet(Level.OVERWORLD.location()), planet);
        }
        return false;
    }


    private void initializeAllButtons() {
        initializePlanetButtons();
        initializeMoonButtons();
        initializeLaunchButton();

    }

    private void initializePlanetButtons() {
        planetButtons.clear();
    }

    private void initializeMoonButtons() {
        moonButtons.clear();
    }

    private void initializeLaunchButton() {
        int buttonWidth = 74;
        int buttonHeight = 20;
        int buttonX = (this.width - buttonWidth) / 4 + 100;
        int buttonY = (this.height - buttonHeight) / 4;

        launchButton = new LaunchButton(buttonX, buttonY, buttonWidth, buttonHeight, Component.literal("Launch"), (btn) -> onLaunchButtonClick());
        launchButton.setButtonTexture(
                ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/launch_button.png"),
                ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/launch_button_hovered.png")
        );
        launchButton.setMessage(launch);
        this.addRenderableWidget(launchButton);

        launchButton.visible = false;
    }

    private int leftArrowX, rightArrowX, arrowY, arrowWidth = 25, arrowHeight = 25;
    private int upArrowX, downArrowX, verticalArrowY;
    private final int verticalArrowWidth = 20, verticalArrowHeight = 20;
    private final int textArrowWidth = 10;

    int galaxyWidth = 21;
    int galaxyHeight = 12;
    int galaxyX = 4;
    int galaxyY = 4;

    private void initTop(GuiGraphics graphics, int mouseX, int mouseY) {
        ResourceLocation topBarTexture = ResourceLocation.fromNamespaceAndPath(
                Stellaris.MODID, "textures/gui/util/planet_selection_bar.png");

        Font font = Minecraft.getInstance().font;

        int tgWidth = 240;
        int tgHeight = 32;
        int tgX = (this.width - tgWidth) / 2;
        int tgY = this.height - 48;

        int infoWidth = 12;
        int infoHeight = 12;
        int infoX = this.width - 20;
        int infoY = 4;

        leftArrowX = tgX + 7;
        rightArrowX = tgX + tgWidth - arrowWidth - 7;
        arrowY = tgY + (tgHeight - arrowHeight) / 2;

        upArrowX = tgX + tgWidth + 10;
        downArrowX = tgX - verticalArrowWidth - 10;
        verticalArrowY = tgY + (tgHeight - verticalArrowHeight) / 2;

        boolean infoHovering = isHoveredOnSprite(infoX, infoY, infoWidth, infoHeight, mouseX, mouseY);
        boolean galaxyHovering = isHoveredOnSprite(galaxyX, galaxyY, galaxyWidth, galaxyHeight, mouseX, mouseY);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(50 / 255f, 69 / 255f, 163 / 255f, 1.0f);
        RenderSystem.setShaderTexture(0, topBarTexture);
        graphics.blit(topBarTexture, tgX, tgY, 0, 0, tgWidth, tgHeight, tgWidth, tgHeight);
        graphics.blit(ResourceLocation.fromNamespaceAndPath(
                        Stellaris.MODID, "textures/gui/util/" + (infoHovering ? "planet_selection_info_button_hover.png" : "planet_selection_info_button.png")),
                infoX, infoY, 0, 0, infoWidth, infoHeight, infoWidth, infoHeight);
        graphics.blit(ResourceLocation.fromNamespaceAndPath(
                        Stellaris.MODID, "textures/gui/util/" + (galaxyHovering ? "planet_selection_galaxy_button_hover.png" : "planet_selection_galaxy_button.png")),
                galaxyX, galaxyY, 0, 0, galaxyWidth, galaxyHeight, galaxyWidth, galaxyHeight);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        boolean arrowHoveringLeft = isHoveredOnSprite(leftArrowX, arrowY, arrowWidth, arrowHeight, mouseX, mouseY);
        boolean arrowHoveringRight = isHoveredOnSprite(rightArrowX, arrowY, arrowWidth, arrowHeight, mouseX, mouseY);

        graphics.blit(ResourceLocation.fromNamespaceAndPath(
                        Stellaris.MODID, "textures/gui/util/" + (arrowHoveringLeft ? "planet_selection_arrow_left_hover.png" : "planet_selection_arrow_left.png")),
                leftArrowX, arrowY, 0, 0, arrowWidth, arrowHeight, arrowWidth, arrowHeight);

        graphics.blit(ResourceLocation.fromNamespaceAndPath(
                        Stellaris.MODID, "textures/gui/util/" + (arrowHoveringRight ? "planet_selection_arrow_right_hover.png" : "planet_selection_arrow_right.png")),
                rightArrowX, arrowY, 0, 0, arrowWidth, arrowHeight, arrowWidth, arrowHeight);

        Component bodyName = focusedBody != null ? focusedBody.getTranslatable() : Component.literal("X");
        int nameWidth = font.width(bodyName);
        int nameX = tgX + (tgWidth / 2) - (nameWidth / 2);
        int nameY = tgY + (tgHeight / 2) - (font.lineHeight / 2);
        graphics.drawString(font, bodyName, nameX, nameY, 0xFFFFFF, true);

        if (infoHovering) {
            List<Component> tooltipLines = List.of(
                    Component.translatable("text.stellaris.planetscreen.space"),
                    Component.translatable("text.stellaris.planetscreen.z"),
                    Component.translatable("text.stellaris.planetscreen.arrows")
            );
            graphics.renderTooltip(Minecraft.getInstance().font, tooltipLines, Optional.empty(), mouseX, mouseY);
        }
        if (galaxyHovering) {
            List<Component> tooltipLines = List.of(
                    Component.translatable("text.stellaris.planetscreen.returntogalaxy")
            );
            graphics.renderTooltip(Minecraft.getInstance().font, tooltipLines, Optional.empty(), mouseX, mouseY);
        }
    }

    private void onLaunchButtonClick() {
        if (focusedBody != null && focusedBody.dimension != null) {
            if (canLaunch(PlanetUtil.getPlanet(focusedBody.dimension))) {

                if(focusedBody.spaceStation && !showSpaceStationMenu) {
                    this.showSpaceStationMenu = true;
                } else {
                    tpToFocusedPlanet();
                }

            } else {
                if (PlanetUtil.getPlanet(focusedBody.dimension).name().equals("Earth")) {
                    tpToFocusedPlanet();
                }
            }
            showLargeMenu = false;
        }
    }

    private void updateZoomAndOffsetAnimation() {
        double smoothing = 0.05;
        zoomLevel += (targetZoomLevel - zoomLevel) * smoothing;
        offsetX += (targetOffsetX - offsetX) * smoothing;
        offsetY += (targetOffsetY - offsetY) * smoothing;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        graphics.blit(BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
    }

    public void renderBodiesAndPlanets(GuiGraphics graphics) {
        renderStars(graphics);
        renderPlanets(graphics);
        renderMoons(graphics);
        initializePlanetButtons();
        initializeMoonButtons();
    }

    private void renderHelp(GuiGraphics graphics) {
//        if(showHelpMenu) {
//            graphics.drawCenteredString(this.font, Component.translatable("text.stellaris.planetscreen.press_space"), this.width/2, this.height - 20 , 16777212);
//            graphics.drawCenteredString(this.font, Component.translatable("text.stellaris.planetscreen.arrows"), this.width/2, this.height - 10 , 16777212);
//
//        }

    }

    private void renderStars(GuiGraphics graphics) {
        Font font = Minecraft.getInstance().font;
        for (CelestialBody star : STARS) {
            if (!isInCurrentGalaxy(star)) continue;

            float bodyX = (float) ((star.x + offsetX) * zoomLevel - (star.width / 2) * zoomLevel);
            float bodyY = (float) ((star.y + offsetY) * zoomLevel - (star.height / 2) * zoomLevel);

            int bodyWidth = (int) (star.width * zoomLevel);
            int bodyHeight = (int) (star.height * zoomLevel);

            graphics.blit(star.texture, (int) bodyX, (int) bodyY, 0, 0, bodyWidth, bodyHeight, bodyWidth, bodyHeight);

            int nameWidth = font.width(star.getTranslatable());
            graphics.drawString(font, star.getTranslatable(), (int) (bodyX + (float) bodyWidth / 2 - (float) nameWidth / 2), (int) (bodyY + bodyHeight), 0xFFFFFF);
        }
    }

    private void renderPlanets(GuiGraphics graphics) {
        Font font = Minecraft.getInstance().font;
        for (PlanetInfo planet : PLANETS) {
            if (!isInCurrentGalaxy(planet)) continue;

            CelestialBody orbitCenter = planet.orbitCenter;

            float orbitCenterX = (float) ((orbitCenter.x + offsetX) * zoomLevel);
            float orbitCenterY = (float) ((orbitCenter.y + offsetY) * zoomLevel);

            float planetX = (float) (orbitCenterX + planet.orbitRadius * zoomLevel * Math.cos(planet.currentAngle) - planet.width / 2 * zoomLevel);
            float planetY = (float) (orbitCenterY + planet.orbitRadius * zoomLevel * Math.sin(planet.currentAngle) - planet.height / 2 * zoomLevel);

            int planetWidth = (int) (planet.width * zoomLevel);
            int planetHeight = (int) (planet.height * zoomLevel);

            ScreenHelper.drawTexturewithRotation(graphics, planet.texture, (int) planetX, (int) planetY, 0, 0, planetWidth, planetHeight, planetWidth, planetHeight, (float) planet.currentAngle);

            int nameWidth = font.width(planet.name);
            graphics.drawString(font, planet.getTranslatable(), (int) (planetX + (float) planetWidth / 2 - (float) nameWidth / 2), (int) (planetY + planetHeight), 0xFFFFFF);
        }
    }

    private void renderMoons(GuiGraphics graphics) {
        for (MoonInfo moon : MOONS) {
            if (!isInCurrentGalaxy(moon)) continue;

            float moonX = (float) ((moon.x + offsetX) * zoomLevel - (moon.width / 2) * zoomLevel);
            float moonY = (float) ((moon.y + offsetY) * zoomLevel - (moon.height / 2) * zoomLevel);

            int moonWidth = (int) (moon.width * zoomLevel);
            int moonHeight = (int) (moon.height * zoomLevel);

            ScreenHelper.drawTexturewithRotation(graphics, moon.texture, (int) moonX, (int) moonY, 0, 0, moonWidth, moonHeight, moonWidth, moonHeight, (float) moon.currentAngle);
        }
    }

    private int currentHighlighterFrame = 0;
    private final int totalHighlighterFrames = 30;

    private void renderLargeMenu(GuiGraphics graphics) {
        if (showLargeMenu && !showSpaceStationMenu) {
            getMenu().freeze_gui = false;
            ResourceLocation CELESTIAL_BODY_TEXTURE = focusedBody.texture;

            Component CELESTIAL_BODY_NAME = focusedBody.getTranslatable();

            Planet planet = PlanetUtil.getPlanet(focusedBody.dimension);

            if (planet == null) return;

            float CELESTIAL_BODY_TEMPERATURE = planet.temperature();
            float CELESTIAL_BODY_GRAVITY = planet.gravity();
            boolean CELESTIAL_BODY_OXYGEN = planet.oxygen();
            String CELESTIAL_BODY_SYSTEM = planet.system();

            Component systemTranslatable;

            Component temperatureV;
            Component gravityV;
            Component oxygenV;
            Component systemV;

            int oxygenColor;
            int temperatureColor;

            if (CELESTIAL_BODY_SYSTEM == null) {
                systemV = Component.literal(system + " : null");
            } else {
                systemTranslatable = Component.translatable(CELESTIAL_BODY_SYSTEM);
                systemV = Component.literal(system.getString() + " : " + systemTranslatable.getString());
            }

            temperatureV = Component.literal(temperature.getString() + " : " + planet.temperature() + "°C");

            oxygenV = Component.literal(oxygen.getString() + " : " + CELESTIAL_BODY_OXYGEN);

            gravityV = Component.literal(gravity.getString() + " : " + CELESTIAL_BODY_GRAVITY + "m/s");

            if (CELESTIAL_BODY_OXYGEN) {
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

            if(focusedBody.spaceStation) {
                launchButton.setTooltip(Tooltip.create(SpaceStationRecipesManager.getTotalTooltip(spaceStationRecipeStates)));
            }

            launchButton.visible = true;
            launchButton.setPosition(buttonX, buttonY);


            float alpha = 0.5f;

            RenderSystem.disableBlend();

            graphics.drawString(font, CELESTIAL_BODY_NAME, textX, buttonY + buttonHeight / 4 + 37, 0xFFFFFF, true);

            graphics.drawString(font, "￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣￣", textX, buttonY + buttonHeight / 4 + 50, 0xFFFFFF, true);

            graphics.drawString(font, temperatureV, textX, buttonY + buttonHeight / 4 + 60, temperatureColor, true);
            graphics.drawString(font, oxygenV, textX, buttonY + buttonHeight / 4 + 90, oxygenColor, true);
            graphics.drawString(font, systemV, textX, buttonY + buttonHeight / 4 + 105, 0xFFFFFF, true);

            if (focusedBody instanceof PlanetInfo && getMoonsCount((PlanetInfo) focusedBody) > 0) {
                graphics.drawString(font, TranslatableRegistry.MOONS.getString() + " : " + getMoonsCount((PlanetInfo) focusedBody), textX, buttonY + buttonHeight / 4 + 120, 0xD3D3D3, true);
            }

            if (getPlayer().getServer() != null) {
                graphics.drawString(font, TranslatableRegistry.PLAYERS.getString() + " : " + Utils.getPlayerCountInDimension(getPlayer().getServer(), focusedBody.dimension), textX, buttonY + buttonHeight / 4 + 135, 0xFFFFFF, false);
            }

            if (canLaunch(planet)) {
                graphics.drawString(font, gravityV, textX, buttonY + buttonHeight / 4 + 75, Utils.getColorHexCode("White"), true);

                RenderSystem.enableBlend();
                RenderSystem.setShader(GameRenderer::getPositionTexShader);

                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);

                RenderSystem.setShaderTexture(0, LARGE_MENU_TEXTURE);
                graphics.blit(LARGE_MENU_TEXTURE, centerX, centerY, 0, 0, menuWidth, menuHeight, menuWidth, menuHeight);

            } else {
                if (Objects.equals(focusedBody.name, "Earth")) {
                    graphics.drawString(font, gravityV, textX, buttonY + buttonHeight / 4 + 75, Utils.getColorHexCode("White"), true);

                    RenderSystem.enableBlend();
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);

                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);

                    RenderSystem.setShaderTexture(0, LARGE_MENU_TEXTURE);
                    graphics.blit(LARGE_MENU_TEXTURE, centerX, centerY, 0, 0, menuWidth, menuHeight, menuWidth, menuHeight);
                } else {
                    graphics.drawString(font, gravityV, textX, buttonY + buttonHeight / 4 + 75, Utils.getColorHexCode("Orange"), true);

                    RenderSystem.enableBlend();
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);

                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);

                    RenderSystem.setShaderTexture(0, LARGE_MENU_TEXTURE_RED);
                    graphics.blit(LARGE_MENU_TEXTURE_RED, centerX, centerY, 0, 0, menuWidth, menuHeight, menuWidth, menuHeight);
                    launchButton.setButtonTexture(
                            ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/no_launch_button.png"),
                            ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/no_launch_button_hovered.png")
                    );
                }
            }

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            RenderSystem.setShaderTexture(0, BLACK_TEXTURE);
            graphics.blit(BLACK_TEXTURE, centerX + menuWidth - 64, centerY + menuHeight / 2 - 32, 0, 0, 48, 48, 48, 48);

            RenderSystem.setShaderTexture(0, CELESTIAL_BODY_TEXTURE);
            if (focusedBody.width != focusedBody.height) {
                if (Math.min((int) focusedBody.width, (int) focusedBody.height) == focusedBody.width) {
                    float rate = focusedBody.height / focusedBody.width;
                    graphics.blit(CELESTIAL_BODY_TEXTURE, centerX + menuWidth - 46, centerY + menuHeight / 2 - 14, 0, 0, 12, (int) (12 * rate), 12, (int) (12 * rate));
                } else {
                    float rate = focusedBody.width / focusedBody.height;
                    graphics.blit(CELESTIAL_BODY_TEXTURE, centerX + menuWidth - 46, centerY + menuHeight / 2 - 14, 0, 0, (int) (12 * rate), 12, (int) (12 * rate), 12);
                }
            } else {
                graphics.blit(CELESTIAL_BODY_TEXTURE, centerX + menuWidth - 46, centerY + menuHeight / 2 - 14, 0, 0, 12, 12, 12, 12);
            }

            RenderSystem.disableBlend();
        } else {
            launchButton.visible = false;
        }
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_Z) {
            if (focusedBody != null) {
                if (canLaunch(PlanetUtil.getPlanet(focusedBody.dimension))) {
                    showLargeMenu = !showLargeMenu;
                }
            }
        }
        else if (keyCode == GLFW.GLFW_KEY_H) {
            showHelpMenu = !showHelpMenu;
        } else if (keyCode == GLFW.GLFW_KEY_SPACE || keyCode == GLFW.GLFW_KEY_X) {
            isPausePressed = !isPausePressed;
        } else if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            isShiftPressed = true;
        } else if (keyCode == GLFW.GLFW_KEY_RIGHT) {
            if (focusedBody == null) {
                focusedBody = findByNameStar("stellaris:sun");
            } else if (focusedBody instanceof PlanetInfo || focusedBody instanceof MoonInfo) {
                focusedBody = getNextBodyByDistance(focusedBody);
            } else if (focusedBody instanceof CelestialBody) {
                focusedBody = getNextStarByDistance(focusedBody);
            }

            if (focusedBody != null && !isInCurrentGalaxy(focusedBody)) {
                focusedBody = null;
                return true;
            }
            if (focusedBody != null) centerOnBody(focusedBody);
        } else if (keyCode == GLFW.GLFW_KEY_LEFT) {
            if (focusedBody == null) {
                focusedBody = findByNameStar("stellaris:sun");
            } else if (focusedBody instanceof PlanetInfo || focusedBody instanceof MoonInfo) {
                focusedBody = getPreviousBodyByDistance(focusedBody);
            } else if (focusedBody instanceof CelestialBody) {
                focusedBody = getPreviousStarByDistance(focusedBody);
            }

            if (focusedBody != null && !isInCurrentGalaxy(focusedBody)) {
                focusedBody = null;
                return true;
            }
            if (focusedBody != null) centerOnBody(focusedBody);
        }
        else if (keyCode == GLFW.GLFW_KEY_UP) {
            if (focusedBody instanceof MoonInfo moon) {
                focusedBody = moon.orbitCenter;
            } else if (focusedBody instanceof PlanetInfo planet) {
                focusedBody = planet.orbitCenter;
            }

            if (focusedBody != null && !isInCurrentGalaxy(focusedBody)) {
                focusedBody = null;
                return true;
            }
            centerOnBody(focusedBody);
        } else if (keyCode == GLFW.GLFW_KEY_DOWN) {
            if (focusedBody instanceof PlanetInfo planet) {
                focusedBody = getMoonsByDistance(planet);
            } else if (focusedBody instanceof CelestialBody star) {
                List<PlanetInfo> planetsInSystem = PLANETS.stream()
                        .filter(p -> p.orbitCenter == star)
                        .sorted(Comparator.comparingDouble(p -> p.orbitRadius))
                        .toList();
                if (!planetsInSystem.isEmpty()) {
                    focusedBody = planetsInSystem.get(0);
                }
            }

            if (focusedBody != null && !isInCurrentGalaxy(focusedBody)) {
                focusedBody = null;
                return true;
            }
            centerOnBody(focusedBody);
        } else if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            if (showSpaceStationMenu) {
                showSpaceStationMenu = false;
                showLargeMenu = true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private CelestialBody getNextBodyByDistance(CelestialBody currentBody) {
        if (currentBody == null) return null;

        if (currentBody instanceof PlanetInfo planetInfo) {
            List<PlanetInfo> bodies = new ArrayList<>(PLANETS);
            bodies.sort(Comparator.comparingDouble(b -> b.orbitRadius));

            for (int i = 0; i < bodies.size(); i++) {
                if (bodies.get(i) == currentBody) {
                    for (int j = i + 1; j < bodies.size(); j++) {
                        if (bodies.get(j).orbitCenter == planetInfo.orbitCenter) {
                            return bodies.get(j);
                        }
                    }
                }
            }
        } else if (currentBody instanceof MoonInfo moonInfo) {
            List<MoonInfo> bodies = new ArrayList<>(MOONS);
            bodies.sort(Comparator.comparingDouble(b -> b.orbitRadius));

            for (int i = 0; i < bodies.size(); i++) {
                if (bodies.get(i) == currentBody) {
                    for (int j = i + 1; j < bodies.size(); j++) {
                        if (bodies.get(j).orbitCenter == moonInfo.orbitCenter) {
                            return bodies.get(j);
                        }
                    }
                }
            }
        } else if (STARS.contains(currentBody)) {
            List<PlanetInfo> bodies = new ArrayList<>(PLANETS);
            bodies.sort(Comparator.comparingDouble(b -> b.orbitRadius));

            for (PlanetInfo planet : bodies) {
                if (planet.orbitCenter == currentBody) {
                    return planet;
                }
            }
        }

        return currentBody;
    }
    private CelestialBody getPreviousBodyByDistance(CelestialBody currentBody) {
        if (currentBody == null) return null;

        if (currentBody instanceof PlanetInfo planetInfo) {
            List<PlanetInfo> bodies = new ArrayList<>(PLANETS);
            bodies.sort(Comparator.comparingDouble(b -> b.orbitRadius));

            for (int i = bodies.size() - 1; i >= 0; i--) {
                if (bodies.get(i) == currentBody) {
                    for (int j = i - 1; j >= 0; j--) {
                        if (bodies.get(j).orbitCenter == planetInfo.orbitCenter) {
                            return bodies.get(j);
                        }
                    }
                }
            }
        } else if (currentBody instanceof MoonInfo moonInfo) {
            List<MoonInfo> bodies = new ArrayList<>(MOONS);
            bodies.sort(Comparator.comparingDouble(b -> b.orbitRadius));

            for (int i = bodies.size() - 1; i >= 0; i--) {
                if (bodies.get(i) == currentBody) {
                    for (int j = i - 1; j >= 0; j--) {
                        if (bodies.get(j).orbitCenter == moonInfo.orbitCenter) {
                            return bodies.get(j);
                        }
                    }
                }
            }
        } else if (STARS.contains(currentBody)) {
            List<PlanetInfo> bodies = new ArrayList<>(PLANETS);
            bodies.sort(Comparator.comparingDouble(b -> b.orbitRadius));

            for (int i = bodies.size() - 1; i >= 0; i--) {
                if (bodies.get(i).orbitCenter == currentBody) {
                    return bodies.get(i);
                }
            }
        }

        return currentBody;
    }


    private CelestialBody getMoonsByDistance(PlanetInfo currentBody) {
        MoonInfo smallestOrbitMoon = null;
        for (MoonInfo moon : MOONS) {
            if (moon.orbitCenter.equals(currentBody)) {
                if (smallestOrbitMoon == null || moon.orbitRadius < smallestOrbitMoon.orbitRadius) {
                    smallestOrbitMoon = moon;
                }
            }
        }
        return smallestOrbitMoon != null ? smallestOrbitMoon : currentBody;
    }

    private CelestialBody getNextStarByDistance(CelestialBody currentBody) {
        List<CelestialBody> stars = new ArrayList<>(STARS);
        stars.sort(Comparator.comparing(CelestialBody::getId));
        for (int i = 0; i < stars.size(); i++) {
            if (stars.get(i) == currentBody) {
                return stars.get((i + 1) % stars.size());
            }
        }
        return currentBody;
    }

    private CelestialBody getPreviousStarByDistance(CelestialBody currentBody) {
        List<CelestialBody> stars = new ArrayList<>(STARS);
        stars.sort(Comparator.comparing(CelestialBody::getId));
        for (int i = 0; i < stars.size(); i++) {
            if (stars.get(i) == currentBody) {
                return stars.get((i - 1 + stars.size()) % stars.size());
            }
        }
        return currentBody;
    }

    private int getMoonsCount(PlanetInfo currentBody) {
        int i = 0;

        for (MoonInfo moon : MOONS) {
            if (moon.orbitCenter.equals(currentBody)) {
                i++;
            }
        }

        return i;
    }

    public void tpToFocusedPlanet() {
        if (focusedBody != null) {

            NetworkManager.sendToServer(new TeleportEntityToPlanetPacket(focusedBody.dimension));
            long windowHandle = Minecraft.getInstance().getWindow().getWindow();
            prevScrollCallback = GLFW.glfwSetScrollCallback(windowHandle, Minecraft.getInstance().mouseHandler::onScroll);
        } else {
            Stellaris.LOG.error("Focused body is null");
        }
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            isShiftPressed = false;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    private void updatePlanets() {
        long time = Util.getMillis();
        if (!getMenu().freeze_gui || !isPausePressed) {
            for (PlanetInfo planet : PLANETS) {
                planet.updateAngle(time);
                planet.updatePosition();
                planet.trail.addPosition(planet.x, planet.y);
            }

            for (MoonInfo moon : MOONS) {
                moon.updateAngle(time);
                moon.updatePosition();
                moon.trail.addPosition(moon.x, moon.y);
            }

            final double G = 10000;
            final double dt = 0.1;

            for (PSystemInfo system : PSYSTEMS) {
                List<StarMovement> stars = new ArrayList<>();

                for (PSystemRecord.StarPosition sp : system.stars) {
                    CelestialBody star = findByNameStar(sp.id());
                    if (star != null) {
                        stars.add(new StarMovement(star, star.getWidth() / 30));
                    }
                }

                double totalMass = 0, centerX = 0, centerY = 0;
                for (StarMovement s : stars) {
                    totalMass += s.mass;
                    centerX += s.body.x * s.mass;
                    centerY += s.body.y * s.mass;
                }
                centerX /= totalMass;
                centerY /= totalMass;

                for (StarMovement s : stars) {
                    if (!s.initialized) {
                        double dx = s.body.x - centerX;
                        double dy = s.body.y - centerY;
                        double dist = Math.sqrt(dx * dx + dy * dy);
                        if (dist != 0) {
                            double v = Math.sqrt(G * totalMass / dist);
                            s.vx = -v * dy / dist;
                            s.vy = v * dx / dist;
                        }
                        s.initialized = true;
                    }
                }

                for (StarMovement s : stars) {
                    s.ax = 0;
                    s.ay = 0;
                }

                for (int i = 0; i < stars.size(); i++) {
                    StarMovement a = stars.get(i);
                    for (int j = 0; j < stars.size(); j++) {
                        if (i == j) continue;
                        StarMovement b = stars.get(j);

                        double dx = b.body.x - a.body.x;
                        double dy = b.body.y - a.body.y;
                        double distSq = dx * dx + dy * dy + 0.01;
                        double dist = Math.sqrt(distSq);

                        double force = G * b.mass / distSq;

                        a.ax += force * dx / dist;
                        a.ay += force * dy / dist;
                    }
                }

                double boundaryRadius = 300;

                for (StarMovement s : stars) {
                    s.vx += s.ax * dt;
                    s.vy += s.ay * dt;

                    s.body.x += s.vx * dt;
                    s.body.y += s.vy * dt;

                    double dx = s.body.x - centerX;
                    double dy = s.body.y - centerY;
                    double dist = Math.sqrt(dx * dx + dy * dy);

                    if (dist > boundaryRadius) {
                        double forceBack = 0.05 * (dist - boundaryRadius);
                        s.vx -= forceBack * dx / dist * dt;
                        s.vy -= forceBack * dy / dist * dt;
                    }

                    s.body.trail.addPosition(s.body.x, s.body.y);
                    updateChildPositions(s.body, time);
                }
            }
        }
    }



    public void drawTrails() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tesselator tesselator = Tesselator.getInstance();

        for (CelestialBody star : STARS) {
            if (!isInCurrentGalaxy(star)) continue;
            renderTrail(tesselator, star.trail, 0xFFFFFF, 0.5F);
        }

        RenderSystem.disableBlend();
    }

    public void renderTrail(Tesselator tesselator, Trail trail, int color, float alpha) {
        List<float[]> positions = trail.getPositions();
        if (positions.size() < 2) return;

        float red = ((color >> 16) & 0xFF) / 255.0F;
        float green = ((color >> 8) & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;

        BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);

        for (float[] pos : positions) {
            float x = (float) ((pos[0] + offsetX) * zoomLevel);
            float y = (float) ((pos[1] + offsetY) * zoomLevel);
            bufferBuilder.addVertex(x, y, 0).setColor(red, green, blue, alpha);
        }

        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
    }


    private void updateChildPositions(CelestialBody orbitCenter, long time) {
        for (PlanetInfo planet : PLANETS) {
            if (planet.orbitCenter == orbitCenter) {
                planet.updateAngle(time);
                planet.updatePosition();
                updateMoonPositions(planet, time);
            }
        }
    }

    private void updateMoonPositions(PlanetInfo planet, long time) {
        for (MoonInfo moon : MOONS) {
            if (moon.orbitCenter == planet) {
                moon.updateAngle(time);
                moon.updatePosition();
            }
        }
    }

    public void drawOrbits() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tesselator = Tesselator.getInstance();

        for (PlanetInfo planet : PLANETS) {
            if (!isInCurrentGalaxy(planet)) continue;
            if (!isInCurrentGalaxy(planet.orbitCenter)) continue;
            CelestialBody orbitCenter = planet.orbitCenter;

            float orbitCenterX = (float) ((orbitCenter.x + offsetX) * zoomLevel);
            float orbitCenterY = (float) ((orbitCenter.y + offsetY) * zoomLevel);

            renderOrbits(tesselator, orbitCenterX, orbitCenterY, planet.orbitRadius * zoomLevel, 75, orbitCenter.orbitColor, 1.0F);
        }

        for (MoonInfo moon : MOONS) {
            if (!isInCurrentGalaxy(moon)) continue;
            if (!isInCurrentGalaxy(moon.orbitCenter)) continue;
            CelestialBody orbitCenter = moon.orbitCenter;

            float orbitCenterX = (float) ((orbitCenter.x + offsetX) * zoomLevel);
            float orbitCenterY = (float) ((orbitCenter.y + offsetY) * zoomLevel);

            renderOrbits(tesselator, orbitCenterX, orbitCenterY, moon.orbitRadius * zoomLevel, 75, 0x888888, 0.5F);
        }

        RenderSystem.disableBlend();
    }

    public static void renderOrbits(Tesselator tesselator, double centerX, double centerY, double radius, int sides, int color, float alphaL) {
        float red = ((color >> 16) & 0xFF) / 255.0F;
        float green = ((color >> 8) & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;

        float angleStep = (float) (2.0 * Math.PI / sides);

        BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

        for (int i = 0; i < sides; i++) {
            float currentAngle = i * angleStep;
            float nextAngle = currentAngle + angleStep;

            float vertex1X = (float) (centerX + radius * Math.cos(currentAngle));
            float vertex1Y = (float) (centerY + radius * Math.sin(currentAngle));
            float vertex2X = (float) (centerX + radius * Math.cos(nextAngle));
            float vertex2Y = (float) (centerY + radius * Math.sin(nextAngle));

            bufferBuilder.addVertex(vertex1X, vertex1Y, 0).setColor(red, green, blue, alphaL);
            bufferBuilder.addVertex(vertex2X, vertex2Y, 0).setColor(red, green, blue, alphaL);
        }

        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
    }

    private void centerSun() {
        float centerX = width / 2.0f;
        float centerY = height / 2.0f;
        CelestialBody sun = findByNameStar(GalaxyScreen.findByNameGalaxy(getMenu().getGalaxyId()).getCenterStar());
        if (sun != null) {
            sun.setPosition(centerX, centerY);
        } else {
            Stellaris.LOG.error("center sun is null");
        }
        offsetX = 0;
        offsetY = 0;
    }

    private boolean isInCurrentGalaxy(CelestialBody body) {
        for (PSystemInfo system : PSYSTEMS) {
            if (!system.getParent().equals(menu.getGalaxyId())) continue;

            for (PSystemRecord.StarPosition sp : system.stars) {
                if (sp.id().equals(body.getId())) return true;
            }

            for (PlanetInfo planet : PLANETS) {
                if (planet.getId().equals(body.getId())) {
                    for (PSystemRecord.StarPosition sp : system.stars) {
                        if (planet.orbitCenter.getId().equals(sp.id())) return true;
                    }
                }
            }

            for (MoonInfo moon : MOONS) {
                if (moon.getId().equals(body.getId())) {
                    if (moon.orbitCenter instanceof PlanetInfo planet) {
                        for (PSystemRecord.StarPosition sp : system.stars) {
                            if (planet.orbitCenter.getId().equals(sp.id())) return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static CelestialBody findByNameStar(String id) {
        for (CelestialBody body : PlanetSelectionScreen.STARS) {
            if (body.getId().equals(id)) {
                return body;
            }
        }
        Stellaris.LOG.warn("Star not found : {}", id);
        return null;
    }

    public static PlanetInfo findByNamePlanet(String id) {
        for (PlanetInfo body : PlanetSelectionScreen.PLANETS) {
            if (body.getId().equals(id)) {
                return body;
            }
        }
        return null;
    }

    public static MoonInfo findByNameMoon(String id) {
        for (MoonInfo body : PlanetSelectionScreen.MOONS) {
            if (body.getId().equals(id)) {
                return body;
            }
        }
        return null;
    }

    public void centerOnBody(CelestialBody body) {
        if (body == null) return;

        zoomLevel = 1.0;
        targetOffsetX = ((body.x - width / 2.0)) * -1;
        targetOffsetY = ((body.y - height / 2.0)) * -1;
    }


    public void onMouseScroll(long window, double scrollX, double scrollY) {
        double[] mouseX = new double[1];
        double[] mouseY = new double[1];

        GLFW.glfwGetCursorPos(window, mouseX, mouseY);

        if (this.minecraft != null && this.minecraft.player != null) {
            if (this.minecraft.screen != null) {
                handleHotbarScroll(scrollY);
            }

            if (this.minecraft.screen instanceof CreativeModeInventoryScreen creativeScreen) {
                if (creativeScreen.mouseScrolled(mouseX[0], mouseY[0], scrollX, scrollY)) {
                    return;
                }
            }

            if (this.minecraft.screen instanceof PlanetSelectionScreen) {
                if (scrollY != 0) {
                    double screenX = this.width / 4.0;
                    double screenY = this.height / 4.0;

                    double beforeZoomWorldX = screenX / zoomLevel - offsetX;
                    double beforeZoomWorldY = screenY / zoomLevel - offsetY;

                    targetZoomLevel += scrollY * 0.1;
                    targetZoomLevel = Math.max(0.2, Math.min(targetZoomLevel, 1.2));

                    double afterZoomWorldX = screenX / targetZoomLevel - offsetX;
                    double afterZoomWorldY = screenY / targetZoomLevel - offsetY;

                    double offsetAdjustX = (beforeZoomWorldX - afterZoomWorldX);
                    double offsetAdjustY = (beforeZoomWorldY - afterZoomWorldY);

                    targetOffsetX += offsetAdjustX;
                    targetOffsetY += offsetAdjustY;
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

    private int centerArrowX;

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
            isWheelButtonDown = true;
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT || button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            dragging = true;
            lastMouseX = mouseX;
            lastMouseY = mouseY;

            boolean isArrowHovered = mouseX >= leftArrowX && mouseX <= leftArrowX + arrowWidth &&
                    mouseY >= arrowY && mouseY <= arrowY + arrowHeight;

            if (mouseX >= leftArrowX && mouseX <= leftArrowX + arrowWidth &&
                    mouseY >= arrowY && mouseY <= arrowY + arrowHeight) {
                if (focusedBody == null) {
                    focusedBody = findByNamePlanet("stellaris:earth");
                } else if (focusedBody instanceof PlanetInfo || focusedBody instanceof MoonInfo) {
                    focusedBody = getPreviousBodyByDistance(focusedBody);
                } else {
                    focusedBody = getPreviousStarByDistance(focusedBody);
                }

                if (!isInCurrentGalaxy(focusedBody)) focusedBody = null;

                Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 1.0F);
                if (focusedBody != null) centerOnBody(focusedBody);
                return true;
            }

            if (mouseX >= rightArrowX && mouseX <= rightArrowX + arrowWidth &&
                    mouseY >= arrowY && mouseY <= arrowY + arrowHeight) {
                if (focusedBody == null) {
                    focusedBody = findByNamePlanet("stellaris:earth");
                } else if (focusedBody instanceof PlanetInfo || focusedBody instanceof MoonInfo) {
                    focusedBody = getNextBodyByDistance(focusedBody);
                } else {
                    focusedBody = getNextStarByDistance(focusedBody);
                }

                if (!isInCurrentGalaxy(focusedBody)) focusedBody = null;

                Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 1.0F);
                if (focusedBody != null) centerOnBody(focusedBody);
                return true;
            }

            if (isHoveredOnSprite(galaxyX, galaxyY, galaxyWidth, galaxyHeight, mouseX, mouseY)) {
                Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 1.0F);
                NetworkManager.sendToServer(new OpenMilkyWayMenuPacket());
            }

            if (showLargeMenu || showSpaceStationMenu) {
                if (launchButton.mouseClicked(mouseX, mouseY, button)) return true;

                if (!showSpaceStationMenu) {
                    showLargeMenu = false;
                    return true;

                }
            } else {
                focusedBody = null;
                hoveredBody = null;

                for (MoonInfo moon : MOONS) {
                    if (!isInCurrentGalaxy(moon)) continue;

                    double mx = moon.orbitCenter.x + offsetX + moon.orbitRadius * Math.cos(moon.currentAngle);
                    double my = moon.orbitCenter.y + offsetY + moon.orbitRadius * Math.sin(moon.currentAngle);

                    int moonWidth = (int) (moon.width * zoomLevel);
                    int moonHeight = (int) (moon.height * zoomLevel);

                    float moonX = (float) ((mx - moon.width / 2) * zoomLevel);
                    float moonY = (float) ((my - moon.height / 2) * zoomLevel);

                    if (mouseX >= moonX && mouseX <= moonX + moonWidth &&
                            mouseY >= moonY && mouseY <= moonY + moonHeight &&
                            moon.clickable) {

                        focusedBody = moon;
                        showLargeMenu = true;

                        double cx = mx - width / 2.0;
                        double cy = my - height / 2.0;

                        targetOffsetX = -cx;
                        targetOffsetY = -cy;

                        return true;
                    }
                }

                for (PlanetInfo planet : PLANETS) {
                    if (!isInCurrentGalaxy(planet)) continue;

                    double px = planet.orbitCenter.x + offsetX + planet.orbitRadius * Math.cos(planet.currentAngle);
                    double py = planet.orbitCenter.y + offsetY + planet.orbitRadius * Math.sin(planet.currentAngle);

                    int planetWidth = (int) (planet.width * zoomLevel);
                    int planetHeight = (int) (planet.height * zoomLevel);

                    float planetX = (float) ((px - planet.width / 2) * zoomLevel);
                    float planetY = (float) ((py - planet.height / 2) * zoomLevel);

                    if (mouseX >= planetX && mouseX <= planetX + planetWidth &&
                            mouseY >= planetY && mouseY <= planetY + planetHeight) {

                        focusedBody = planet;
                        showLargeMenu = true;

                        double cx = px - width / 2.0;
                        double cy = py - height / 2.0;

                        targetZoomLevel = 1.0;
                        targetOffsetX = -cx;
                        targetOffsetY = -cy;

                        return true;
                    }
                }
            }

            if (!showLargeMenu && !showSpaceStationMenu && !isArrowHovered && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                focusedBody = null;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }




    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT || button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            dragging = false;
        }
        if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
            isWheelButtonDown = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging) {
            focusedBody = null;
            if (isWheelButtonDown) {
                double rotationSpeed = 0.005;
                double deltaAngle = deltaX * rotationSpeed;

                for (PlanetInfo planet : PLANETS) {
                    planet.currentAngle += deltaAngle;
                    planet.updatePosition();
                }
                for (MoonInfo moon : MOONS) {
                    moon.currentAngle += deltaAngle;
                    moon.updatePosition();
                }
            } else {
                double dx = (mouseX - lastMouseX) / zoomLevel;
                double dy = (mouseY - lastMouseY) / zoomLevel;

                offsetX += dx;
                offsetY += dy;
                targetOffsetX = offsetX;
                targetOffsetY = offsetY;

                lastMouseX = mouseX;
                lastMouseY = mouseY;
            }
        }


        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }


    private void updateHighlighterPosition(GuiGraphics graphics, CelestialBody body) {
        int highlightWidth = (int) (body.width * zoomLevel);
        int highlightHeight = (int) (body.height * zoomLevel);

        float highlightX, highlightY;
        if (body instanceof PlanetInfo planet) {
            double px = planet.orbitCenter.x + offsetX + planet.orbitRadius * Math.cos(planet.currentAngle);
            double py = planet.orbitCenter.y + offsetY + planet.orbitRadius * Math.sin(planet.currentAngle);
            highlightX = (float) ((px - planet.width / 2) * zoomLevel);
            highlightY = (float) ((py - planet.height / 2) * zoomLevel);
        } else if (body instanceof MoonInfo moon) {
            double mx = moon.orbitCenter.x + offsetX + moon.orbitRadius * Math.cos(moon.currentAngle);
            double my = moon.orbitCenter.y + offsetY + moon.orbitRadius * Math.sin(moon.currentAngle);
            highlightX = (float) ((mx - moon.width / 2) * zoomLevel);
            highlightY = (float) ((my - moon.height / 2) * zoomLevel);
        } else {
            highlightX = (float) ((body.x + offsetX) * zoomLevel - highlightWidth / 2);
            highlightY = (float) ((body.y + offsetY) * zoomLevel - highlightHeight / 2);
        }

        currentHighlighterFrame = (currentHighlighterFrame + 1) % totalHighlighterFrames;
        int frameY = currentHighlighterFrame * highlightHeight;

        float currentAngle = body instanceof PlanetInfo
                ? (float) ((PlanetInfo) body).currentAngle
                : body instanceof MoonInfo
                ? (float) ((MoonInfo) body).currentAngle
                : 0;

        ScreenHelper.drawTexturewithRotation(graphics, HIGHLIGHTER_TEXTURE, (int) highlightX, (int) highlightY,
                0, frameY, highlightWidth, highlightHeight, highlightWidth, totalHighlighterFrames * highlightHeight, currentAngle);
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
        long windowHandle = Minecraft.getInstance().getWindow().getWindow();
        prevScrollCallback = GLFW.glfwSetScrollCallback(windowHandle, Minecraft.getInstance().mouseHandler::onScroll);

        super.onClose();
    }

    /** Space Station **/

    //We only check one time if the player have the recipes because normally he can't get item during the screen


    private void initSpaceStationButtons() {
        spaceStationButtons.clear();
        AtomicInteger height = new AtomicInteger(1);
        for (SpaceStationRecipesManager.SpaceStationRecipeState spaceStationRecipeState : spaceStationRecipeStates) {
            SpaceStationRecipe recipe = spaceStationRecipeState.recipe;
            int buttonWidth = 90;
            int buttonHeight = 20;

            int centerX = (this.width - 215) / 2;
            int centerY = (this.height - 177) / 2;

            int buttonX = centerX + buttonWidth / 2 - buttonWidth / 3 - buttonWidth / 15;
            int buttonY = centerY + buttonHeight / 2 + 12;

            if (spaceStationButtons.size() == 5) {
                buttonX += buttonWidth + 10;
                height.set(1);
            }

            TexturedButton button = new TexturedButton(
                    buttonX, buttonY, buttonWidth, buttonHeight,
                    Component.translatable(String.valueOf(recipe.location())),
                    (btn) -> onSpaceStationButtonClick(spaceStationRecipeState)
            );

            if (spaceStationRecipeState.isUnlocked) {
                button.tex(
                        ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/launch_button.png"),
                        ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/launch_button_hovered.png")
                );
            } else {
                button.tex(
                        ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/button.png"),
                        ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/gui/util/buttons/button.png")
                );
            }

            button.setPosition(buttonX, buttonY + height.getAndAdd(1) * 25);

            button.setTooltip(Tooltip.create(recipe.getTooltip(this.getPlayer())));
            button.visible = false;
            spaceStationButtons.add(button);
            this.addRenderableWidget(button);

        }
    }

    private void renderSpaceStation(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        if(spaceStationWindow == null)return;


        if(spaceStationWindow.visible != showSpaceStationMenu) {
            spaceStationWindow.changeVisibility(showSpaceStationMenu);
        }

        spaceStationWindow.render(guiGraphics, mouseX, mouseY, partialTick);

    }

    public void onSpaceStationButtonClick(SpaceStationRecipesManager.SpaceStationRecipeState stationRecipeState) {
        if(stationRecipeState.isUnlocked && focusedBody != null) {

            tpToFocusedPlanet();
            NetworkManager.sendToServer(new PlaceStationPacket(focusedBody.dimension, stationRecipeState.recipe));
        }
    }

    public Player getPlayer() {
        return menu.getPlayer();
    }


}
