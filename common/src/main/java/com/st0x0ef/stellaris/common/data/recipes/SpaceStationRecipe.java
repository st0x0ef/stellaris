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


    public boolean haveMaterials(Player player) {
        for (ItemInput item : items) {
            if (player.getInventory().countItem(item.getItem()) < item.count()) {
                return false;
            }
        }
        return true;
    }

    public void removeMaterials(Player player) {
        for (ItemInput item : items) {
            int stacksNeeded = item.count() / item.getItem().getDefaultMaxStackSize();
            int remainder = item.count() % item.getItem().getDefaultMaxStackSize();
            for (int i = 0; i < stacksNeeded; i++) {
                player.getInventory().removeItem(item.getItem().getDefaultInstance().copyWithCount(item.getItem().getDefaultMaxStackSize()));
            }
            if (remainder > 0) {
                player.getInventory().removeItem(item.getItem().getDefaultInstance().copyWithCount(remainder));
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
            String name = "\n" + item.getItem().getDefaultInstance().getHoverName().getString() + " x" + item.count() ;
            if (player.getInventory().countItem(item.getItem()) < item.count()) {
                tooltip.append(Component.literal(name).withStyle(ChatFormatting.RED));
            } else {
                tooltip.append(Component.literal(name).withStyle(ChatFormatting.GREEN));
            }
        }

        return tooltip;
    }


    public SpaceStationRecipesManager.SpaceStationRecipeState fromRecipe(Player player) {
        return new SpaceStationRecipesManager.SpaceStationRecipeState(this, this.getTooltip(player), this.haveMaterials(player));
    }

}
