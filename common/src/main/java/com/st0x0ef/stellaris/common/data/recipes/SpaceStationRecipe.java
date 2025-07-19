package com.st0x0ef.stellaris.common.data.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.st0x0ef.stellaris.common.data.recipes.input.ItemInput;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public record SpaceStationRecipe(List<ItemInput> items, ResourceLocation location, Vec3 antenna_position) {

    public static final Codec<SpaceStationRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemInput.CODEC.listOf().fieldOf("items").forGetter(SpaceStationRecipe::items),
            ResourceLocation.CODEC.fieldOf("location").forGetter(SpaceStationRecipe::location),
            Vec3.CODEC.fieldOf("antenna_position").forGetter(SpaceStationRecipe::antenna_position)
    ).apply(instance, SpaceStationRecipe::new));

    public static RegistryFriendlyByteBuf toBuffer(SpaceStationRecipe recipe, final RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(recipe.items.size());

        recipe.items.forEach(((item) -> ItemInput.STREAM_CODEC.encode(buffer, item)));
        buffer.writeResourceLocation(recipe.location);
        buffer.writeVec3(recipe.antenna_position);

        return buffer;

    }
    public static SpaceStationRecipe readFromBuffer(RegistryFriendlyByteBuf buffer) {
        ArrayList<ItemInput> items = new ArrayList<>();

        int k = buffer.readInt();

        for (int i = 0; i < k; i++) {
            items.add(ItemInput.STREAM_CODEC.decode(buffer));
        }
        return new SpaceStationRecipe(items, buffer.readResourceLocation(), buffer.readVec3());
    }


    private boolean haveMaterials(Player player) {
        for (ItemInput item : items) {
            int itemLeftToAdd = item.count();
            for (Item itemType : item.getItems()) {
                itemLeftToAdd -= player.getInventory().countItem(itemType);
                if (itemLeftToAdd <= 0) {
                    break;
                }
            }
            if (itemLeftToAdd > 0) {
                return false;
            }
        }
        return true;
    }

    private boolean haveMaterials(ItemInput item, Player player) {
        int itemLeftToAdd = item.count();
        for (Item itemType : item.getItems()) {
            itemLeftToAdd -= player.getInventory().countItem(itemType);
            if (itemLeftToAdd <= 0) {
                break;
            }
        }
        return itemLeftToAdd <= 0;
    }

    public void removeMaterials(Player player) {
        for (ItemInput item : items) {
            int itemLeftToRemove = item.count();
            for (Item itemType : item.getItems()) {
                while (itemLeftToRemove > 0) {
                    int count = player.getInventory().countItem(itemType);
                    if (count <= 0) {
                        break;
                    }

                    if (count >= itemLeftToRemove) {
                        int stacksToRemove = itemLeftToRemove / itemType.getDefaultMaxStackSize();
                        int remainder = itemLeftToRemove % itemType.getDefaultMaxStackSize();
                        for (int i = 0; i < stacksToRemove; i++) {
                            player.getInventory().removeItem(itemType.getDefaultInstance().copyWithCount(itemType.getDefaultMaxStackSize()));
                        }
                        if (remainder > 0) {
                            player.getInventory().removeItem(itemType.getDefaultInstance().copyWithCount(remainder));
                        }
                        itemLeftToRemove = 0;
                    } else {
                        int stacksToRemove = count / itemType.getDefaultMaxStackSize();
                        int remainder = count % itemType.getDefaultMaxStackSize();
                        for (int i = 0; i < stacksToRemove; i++) {
                            player.getInventory().removeItem(itemType.getDefaultInstance().copyWithCount(itemType.getDefaultMaxStackSize()));
                        }
                        if (remainder > 0) {
                            player.getInventory().removeItem(itemType.getDefaultInstance().copyWithCount(remainder));
                        }
                        itemLeftToRemove -= count;
                    }
                }
            }
        }
    }

    public MutableComponent getDisplayName() {
        return Component.translatable("station." + this.location.getNamespace() + "." + this.location.getPath());
    }

    public MutableComponent getTooltip(Player player) {
        MutableComponent tooltip = getDisplayName();

        if(player == null) return tooltip;

        for (ItemInput item : items) {
            String text = "\n" + item.getDisplayName() + " x " + item.count();
            if (haveMaterials(item, player)) {
                tooltip.append(Component.literal(text).withStyle(ChatFormatting.GREEN));
            } else {
                tooltip.append(Component.literal(text).withStyle(ChatFormatting.RED));
            }
        }

        return tooltip;
    }


    public SpaceStationRecipesManager.SpaceStationRecipeState fromRecipe(Player player) {
        return new SpaceStationRecipesManager.SpaceStationRecipeState(this, this.getTooltip(player), this.haveMaterials(player));
    }

}
