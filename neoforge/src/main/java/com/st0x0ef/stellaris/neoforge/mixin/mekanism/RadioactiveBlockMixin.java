package com.st0x0ef.stellaris.neoforge.mixin.mekanism;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.st0x0ef.stellaris.common.compats.ModCompat;
import com.st0x0ef.stellaris.common.data_components.RadioactiveComponent;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import com.st0x0ef.stellaris.neoforge.compat.MekanismCompat;
import mekanism.api.security.SecurityMode;
import mekanism.common.attachments.component.UpgradeAware;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.attribute.AttributeUpgradeSupport;
import mekanism.common.block.attribute.Attributes;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registries.MekanismDataComponents;
import mekanism.common.tile.interfaces.IRedstoneControl;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.function.BiFunction;
import java.util.function.Supplier;

@ModCompat.MixinCompat(modid = "mekanism")
@Mixin(BlockDeferredRegister.class)
public class RadioactiveBlockMixin {

    /**
     * @author TathanDev
     * @reason This allows us to add radioactivity with Mekanism items
     */
    @Overwrite
    @SuppressWarnings("unchecked")
    public <BLOCK extends Block, ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> register(String name, Supplier<? extends BLOCK> blockSupplier, BiFunction<BLOCK, Item.Properties, ITEM> itemCreator) {
        BlockDeferredRegister blockDeferredRegister = (BlockDeferredRegister) (Object) this;
        return (BlockRegistryObject) blockDeferredRegister.register(name, blockSupplier, (block) -> {

            Item.Properties properties = new Item.Properties();
            if (Attribute.has(block, Attributes.AttributeSecurity.class)) {
                properties.component(MekanismDataComponents.SECURITY, SecurityMode.PUBLIC);
            }

            if (Attribute.has(block, Attributes.AttributeRedstone.class)) {
                properties.component(MekanismDataComponents.REDSTONE_CONTROL, IRedstoneControl.RedstoneControl.DISABLED);
            }

            if (Attribute.has(block, AttributeUpgradeSupport.class)) {
                properties.component(MekanismDataComponents.UPGRADES, UpgradeAware.EMPTY);
            }

            if(MekanismCompat.RADIOACTIVE_ITEMS.containsKey(name)) {
                properties.component(DataComponentsRegistry.RADIOACTIVE.get(), new RadioactiveComponent(MekanismCompat.RADIOACTIVE_ITEMS.get(name), true));
            }

            return (BlockItem)itemCreator.apply(block, properties);

            }, BlockRegistryObject::new);
    }

}
