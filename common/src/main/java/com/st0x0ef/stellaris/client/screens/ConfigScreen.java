package com.st0x0ef.stellaris.client.screens;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.client.screens.components.ConfigList;
import com.st0x0ef.stellaris.client.screens.components.StateButton;
import com.st0x0ef.stellaris.common.config.CommonConfig;
import com.st0x0ef.stellaris.common.config.ConfigManager;
import com.st0x0ef.stellaris.common.utils.ResourceLocationUtils;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;


@Environment(EnvType.CLIENT)
public class ConfigScreen extends Screen {

    public static final ResourceLocation TEXTURE = ResourceLocationUtils.texture("/item/engine_fan");
    private final Screen parent;
    public final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    protected ConfigList configList;

    public ConfigScreen(Screen parent) {
        super(Component.literal("Stellaris Option"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.addTitle();
        this.addFooter();

        configList = this.layout.addToContents(new ConfigList(this.minecraft, this.width, this));

        Class<? extends CommonConfig> clazz = Stellaris.CONFIG.getClass();

        addFields(clazz.getFields(), Stellaris.CONFIG, configList, 0);

        this.layout.visitWidgets(this::addRenderableWidget);
        this.layout.arrangeElements();
    }

    protected void addTitle() {
        this.layout.addTitleHeader(this.title, this.font);
    }

    protected void addFooter() {
        this.layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, (button) -> this.onClose()).width(200).build());
    }

    private void addFields(Field[] fields, Object object, ConfigList configList, int recursionDepth) {
        for (Field field : fields) {
            try {
                Object value = field.get(object);
                String name = field.getName();

                if (field.isAnnotationPresent(ConfigManager.InnerConfig.class)) {
                    configList.addBig(new StringWidget(Component.translatable("config.stellaris." + name).withStyle(ChatFormatting.BOLD), this.font));

                    addFields(field.getType().getFields(), field.get(object),  configList, recursionDepth + 1);
                    continue;
                }

                configList.addSmall(new StringWidget(Component.translatable("config.stellaris." + name), this.font), addTypeWidget(field, object, value, Component.translatable("config.stellaris." + name + ".desc")));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void repositionElements() {
        this.layout.arrangeElements();
        if (this.configList != null) {
            this.configList.updateSize(this.width, this.layout);
        }
    }

    @Override
    public void onClose() {
        saveConfig();
        this.playToast(Component.literal("Config Saved"), Component.literal("The Stellaris config has been saved"));
        this.minecraft.setScreen(this.parent);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);
    }

    @Override
    public void removed() {
        this.minecraft.options.save();
    }

    public void playToast(Component title, Component description) {
        this.minecraft.getToasts().addToast(new SystemToast(
                SystemToast.SystemToastId.PERIODIC_NOTIFICATION,
                title,
                description
        ));
    }

    private AbstractWidget addTypeWidget(Field field, Object configInstance, Object value, Component description) {
        String fieldName = field.getName();

        if (value instanceof Boolean boolVal) {

            StateButton button = new StateButton(0, 0, 150, 20, Component.literal("StateButton"), boolVal);
            button.setTooltip(Tooltip.create(description));
            return button;
        }

        else if (value instanceof Number || value instanceof String) {
            EditBox editBox = new EditBox(this.font, 100, 15, Component.literal(fieldName));
            editBox.setTooltip(Tooltip.create(description));
            editBox.setValue(value.toString());

            editBox.setResponder(str -> {
                try {
                    Object converted = convertValue(str, field.getType());
                    field.set(configInstance, converted);
                } catch (Exception ignored) {}
            });
            return editBox;
        } else {
            SpriteIconButton unsupported = stellarisConfigButton(20);
            unsupported.setTooltip(Tooltip.create(Component.literal("Unsupported field type")));
            return unsupported;
        }
    }

    private Object convertValue(String str, Class<?> type) {
        return switch (type.getSimpleName()) {
            case "int", "Integer" -> Integer.parseInt(str);
            case "long", "Long" -> Long.parseLong(str);
            case "double", "Double" -> Double.parseDouble(str);
            case "float", "Float" -> Float.parseFloat(str);
            default -> str;
        };
    }

    private void saveConfig() {
        Path configPath = Platform.getConfigFolder().resolve("stellaris-config.json");

        try (Writer writer = Files.newBufferedWriter(configPath)) {
            Stellaris.GSON.toJson(Stellaris.CONFIG, CommonConfig.class, writer);
        } catch (Exception e) {
            e.printStackTrace();
            playToast(Component.literal("Config Error"), Component.literal("Failed to save Stellaris config"));
        }
    }

    private SpriteIconButton stellarisConfigButton(int i) {
        return SpriteIconButton.builder(Component.literal("Config"), (button) -> {
            Path path = Path.of(Platform.getConfigFolder() + "/stellaris.json");
            Util.getPlatform().openUri(path.toUri());
        }, true).width(i).sprite(TEXTURE, 16, 16).build();
    }
}