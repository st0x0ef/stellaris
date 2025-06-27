package com.st0x0ef.stellaris.neoforge.mixin.mekanism;

import com.st0x0ef.stellaris.common.compats.ModCompat;
import com.st0x0ef.stellaris.common.data_components.RadioactiveComponent;
import com.st0x0ef.stellaris.common.registry.DataComponentsRegistry;
import com.st0x0ef.stellaris.neoforge.compat.MekanismCompat;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.function.Function;

@ModCompat.MixinCompat(modid = "mekanism")
@Mixin(ItemDeferredRegister.class)
public class RadioactiveItemMixin {

    /**
     * @author TathanDev
     * @reason This allows us to add radioactivity with Mekanism items
     */
    @Overwrite
    public <ITEM extends Item> ItemRegistryObject<ITEM> registerItem(String name, Function<Item.Properties, ITEM> sup) {
        ItemDeferredRegister itemDeferredRegister = (ItemDeferredRegister) (Object) this;
        return itemDeferredRegister.register(name, () -> {
            if (MekanismCompat.RADIOACTIVE_ITEMS.containsKey(name)) {
                return sup.apply(new Item.Properties().component(DataComponentsRegistry.RADIOACTIVE.get(), new RadioactiveComponent(MekanismCompat.RADIOACTIVE_ITEMS.get(name), false)));
            }
            return  sup.apply(new Item.Properties());
        });
    }



}
