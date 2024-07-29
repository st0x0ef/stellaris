package com.st0x0ef.stellaris.common.compats;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.registry.BlocksRegistry;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedList;
import java.util.List;

public class RocketStationCategory implements DisplayCategory<BasicDisplay> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Stellaris.MODID, "textures/gui/rocket_station_no_inventory.png");
    public static final CategoryIdentifier<RocketStationDisplay> ROCKET_CRAFTING = CategoryIdentifier.of(Stellaris.MODID,"rocket_crafting");

    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return ROCKET_CRAFTING;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("stellaris.compat.rocket_crafting");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(BlocksRegistry.ROCKET_STATION.get().asItem().getDefaultInstance());
    }


    @Override
    public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
        final Point startPoint = new Point(bounds.getX(), bounds.getY());
        List<Widget> widgets = new LinkedList<>();
        //widgets.add(Widgets.createRecipeBase(bounds));

        //THIS LINE
        widgets.add(Widgets.createTexturedWidget(TEXTURE, bounds));

        inputSlotAdder(widgets,0, 56, 20 , startPoint, display);
        inputSlotAdder(widgets,1, 47, 38 , startPoint, display);
        inputSlotAdder(widgets,2, 65, 38 , startPoint, display);
        inputSlotAdder(widgets,3, 47, 56 , startPoint, display);
        inputSlotAdder(widgets,4, 65, 56 , startPoint, display);
        inputSlotAdder(widgets,5, 47, 74 , startPoint, display);
        inputSlotAdder(widgets,6, 65, 74 , startPoint, display);
        inputSlotAdder(widgets,7, 29, 92 , startPoint, display);
        inputSlotAdder(widgets,8, 47, 92 , startPoint, display);
        inputSlotAdder(widgets,9, 65, 92 , startPoint, display);
        inputSlotAdder(widgets,10, 83, 92 , startPoint, display);
        inputSlotAdder(widgets,11, 29, 110 , startPoint, display);
        inputSlotAdder(widgets,12, 56, 110 , startPoint, display);
        inputSlotAdder(widgets,13, 83, 110 , startPoint, display);

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 129,startPoint.y + 56))
                .entries(display.getOutputEntries().getFirst()).markOutput().disableBackground());

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 132;
    }

    @Override
    public int getDisplayWidth(BasicDisplay display) {
        return 177;
    }

    private static void inputSlotAdder(List<Widget> widgets, int slotIndex, int x, int y, Point startPoint, BasicDisplay display) {
        widgets.add(Widgets.createSlot(new Point(startPoint.x + x,startPoint.y + y)).entries(display.getInputEntries().get(slotIndex)).markInput().disableBackground());
    }
}
