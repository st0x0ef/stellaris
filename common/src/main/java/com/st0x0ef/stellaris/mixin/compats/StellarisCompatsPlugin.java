package com.st0x0ef.stellaris.mixin.compats;


import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.compats.ModCompat;
import dev.architectury.platform.Platform;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class StellarisCompatsPlugin implements IMixinConfigPlugin {

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    /**
     * Mixin Registry is done before we enable compats. This is why this is needed.
     */
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {

        if(mixinClassName.contains("Compat")) {
            try {

                Class<?> mixinClass = Class.forName(mixinClassName);

                if(mixinClass.isAnnotationPresent(ModCompat.MixinCompat.class)) {
                    ModCompat.MixinCompat annotation = mixinClass.getAnnotation(ModCompat.MixinCompat.class);

                    return Platform.isModLoaded(annotation.modid());

                }
            } catch (ClassNotFoundException e) {
                Stellaris.LOG.error("Error loading mixin class: " + mixinClassName, e);
            }
        }

        return true;

    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        if(mixinClassName.contains("Compat")) {
            Stellaris.LOG.info("Applying compatibility mixin: {}", mixinClassName);
        }
    }
}
