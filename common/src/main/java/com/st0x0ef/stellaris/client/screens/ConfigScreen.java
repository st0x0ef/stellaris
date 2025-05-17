package com.st0x0ef.stellaris.client.screens;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.config.CommonConfig;
import com.st0x0ef.stellaris.common.config.ConfigManager;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.SpacerElement;
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

    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Stellaris.MODID, "textures/item/engine_fan.png");
    private final Screen parent;
    private final GridLayout gridLayout;

    public int widgetHeight = 0;

    public ConfigScreen(Screen parent) {
        super(Component.literal("Stellaris Option"));
        this.parent = parent;
        this.gridLayout = new GridLayout();
    }

    @Override
    protected void init() {
        gridLayout.defaultCellSetting().paddingHorizontal(9).paddingBottom(4).alignHorizontallyCenter();
        GridLayout.RowHelper rowHelper = gridLayout.createRowHelper(2);

        Class<? extends CommonConfig> clazz = Stellaris.CONFIG.getClass();

        addFields(clazz.getFields(), rowHelper, Stellaris.CONFIG, 0);

        Button doneButton = Button.builder(CommonComponents.GUI_DONE, (button) -> this.onClose()).width(200).build();

        rowHelper.addChild(doneButton, 2, rowHelper.newCellSettings().paddingTop(10));

        gridLayout.arrangeElements();
        FrameLayout.alignInRectangle(gridLayout, 0, (this.height / 6 + 10) + widgetHeight, this.width, this.height, 0.5F, 0.0F);
        gridLayout.visitWidgets(this::addRenderableWidget);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY != 0) {
            this.gridLayout.visitWidgets((widget) -> {
                if (widget instanceof AbstractWidget abstractWidget) {
                    this.widgetHeight = (int) (scrollY * 10);
                    abstractWidget.setY(abstractWidget.getY() + this.widgetHeight);
                }
            });
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    private void addFields(Field[] fields, GridLayout.RowHelper rowHelper, Object object, int recursionDepth) {
        for (Field field : fields) {
            try {
                Object value = field.get(object);
                String name = field.getName();

                if (field.isAnnotationPresent(ConfigManager.InnerConfig.class)) {
                    addSeparator(rowHelper);

                    rowHelper.addChild(new StringWidget(Component.translatable("config.stellaris." + name).withStyle(ChatFormatting.BOLD), this.font));
                    rowHelper.addChild(new SpacerElement(32, 16));

                    addFields(field.getType().getFields(), rowHelper, field.get(object), recursionDepth + 1);

                    addSeparator(rowHelper);
                    continue;
                }


                rowHelper.addChild(new StringWidget(Component.translatable("config.stellaris." + name), this.font));
                addTypeWidget(field, object, value, Component.translatable("config.stellaris." + name + ".desc"), rowHelper);

            } catch (Exception e) {
                e.printStackTrace();
            }
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
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, -1);
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

    private void addSeparator(GridLayout.RowHelper rowHelper) {
        rowHelper.addChild(new SpacerElement(32, 8));
        rowHelper.addChild(new SpacerElement(32, 8));
    }

    private void addTypeWidget(Field field, Object configInstance, Object value, Component description, GridLayout.RowHelper rowHelper) {
        String fieldName = field.getName();

        if (value instanceof Boolean boolVal) {
            Checkbox checkbox = Checkbox.builder(Component.literal(fieldName), this.font)
                    .selected(boolVal)
                    .tooltip(Tooltip.create(description))
                    .onValueChange((box, val) -> {
                        try {
                            field.set(configInstance, val);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    })
                    .build();
            rowHelper.addChild(checkbox);
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

            rowHelper.addChild(editBox);
        }

        else {
            SpriteIconButton unsupported = stellarisConfigButton(20);
            unsupported.setTooltip(Tooltip.create(Component.literal("Unsupported field type")));
            rowHelper.addChild(unsupported);
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