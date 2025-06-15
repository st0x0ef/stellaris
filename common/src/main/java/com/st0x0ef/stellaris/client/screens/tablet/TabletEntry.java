package com.st0x0ef.stellaris.client.screens.tablet;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public record TabletEntry(String id, String description, ResourceLocation icon, ResourceLocation hoverIcon,
                          List<ItemInfo> infos) {

    public static final Codec<TabletEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("id").forGetter(TabletEntry::id),
            Codec.STRING.fieldOf("description").forGetter(TabletEntry::description),
            ResourceLocation.CODEC.fieldOf("icon").forGetter(TabletEntry::icon),
            ResourceLocation.CODEC.fieldOf("hoverIcon").forGetter(TabletEntry::hoverIcon),
            ItemInfo.CODEC.listOf().fieldOf("infos").forGetter(TabletEntry::infos)
    ).apply(instance, TabletEntry::new));

    public record ItemInfo(String id, String title, String iconType, List<InfoComponent> components) {

        public static final Codec<ItemInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("id").forGetter(ItemInfo::id),
                Codec.STRING.fieldOf("title").forGetter(ItemInfo::title),
                Codec.STRING.fieldOf("iconType").forGetter(ItemInfo::iconType),
                InfoComponent.CODEC.listOf().fieldOf("components").forGetter(ItemInfo::components)
        ).apply(instance, ItemInfo::new));
    }

    public record InfoComponent(String type, Optional<String> text, Optional<ImageComponent> image,
                                Optional<ItemComponent> item, Optional<EntityComponent> entity) {

        public static final Codec<InfoComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("type").forGetter(InfoComponent::type),
                Codec.STRING.optionalFieldOf("text").forGetter(InfoComponent::text),
                ImageComponent.CODEC.optionalFieldOf("image").forGetter(InfoComponent::image),
                ItemComponent.CODEC.optionalFieldOf("item").forGetter(InfoComponent::item),
                EntityComponent.CODEC.optionalFieldOf("entity").forGetter(InfoComponent::entity)

        ).apply(instance, InfoComponent::new));
    }

    public record ImageComponent(ResourceLocation location, int width, int height) {

        public static final Codec<ImageComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("location").forGetter(ImageComponent::location),
                Codec.INT.fieldOf("width").forGetter(ImageComponent::width),
                Codec.INT.fieldOf("height").forGetter(ImageComponent::height)
        ).apply(instance, ImageComponent::new));
    }

    public record ItemComponent(ItemStack stack, float size, Optional<Boolean> onlyIcon) {

        public static final Codec<ItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ItemStack.CODEC.fieldOf("stack").forGetter(ItemComponent::stack),
                Codec.FLOAT.fieldOf("size").forGetter(ItemComponent::size),
                Codec.BOOL.optionalFieldOf("onlyIcon").forGetter(ItemComponent::onlyIcon)
        ).apply(instance, ItemComponent::new));
    }

    public record EntityComponent(ResourceLocation entity, int scale) {

        public static final Codec<EntityComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("id").forGetter(EntityComponent::entity),
                Codec.INT.fieldOf("scale").forGetter(EntityComponent::scale)
        ).apply(instance, EntityComponent::new));
    }
}
