package com.st0x0ef.stellaris.mixin.compats;


import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.compats.ModCompat;
import com.st0x0ef.stellaris.platform.CompatPlatform;
import dev.architectury.platform.Platform;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.Annotations;

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

        try {
            ClassNode node = MixinService.getService().getBytecodeProvider().getClassNode(mixinClassName);

            if(node.visibleAnnotations != null) {
                AnnotationNode annotationNode = Annotations.get(node.visibleAnnotations, ModCompat.MixinCompat.class.descriptorString());
                if(annotationNode != null) {
                    String object = Annotations.getValue(annotationNode, "modid", ModCompat.MixinCompat.class);
                    return CompatPlatform.isModLoading(object);
                }
            }
        } catch (Exception e) {
            System.out.println("Checking mixin class: " + mixinClassName + " with annotation: " + e);
        }

        return true;

    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        Stellaris.LOG.error("eeeee");
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
