package com.st0x0ef.stellaris.platform.neoforge;

import com.st0x0ef.stellaris.Stellaris;
import com.st0x0ef.stellaris.common.compats.ModCompat;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;

import java.lang.annotation.ElementType;
import java.lang.reflect.Constructor;
import java.util.*;

public class CompatPlatformImpl {

    public static void loadCompats() {
        List<ModFileScanData> modFiles = ModList.get().getAllScanData();
        Set<String> compatClassNames = new LinkedHashSet<>();

        for(ModFileScanData  modFile : modFiles) {
            modFile.getAnnotatedBy(ModCompat.Compat.class, ElementType.TYPE).forEach(annotationData -> {
                String className = annotationData.memberName();
                compatClassNames.add(className);
            });

        }

        for (String className : compatClassNames) {
            try {
                Class<?> asmClass = Class.forName(className);
                Class<? extends ModCompat> modCompatClass = asmClass.asSubclass(ModCompat.class);

                if(modCompatClass.getAnnotation(ModCompat.Compat.class) != null) {
                    ModCompat.Compat compat = modCompatClass.getAnnotation(ModCompat.Compat.class);
                    Constructor<? extends ModCompat> constructor = modCompatClass.getDeclaredConstructor();
                    ModCompat instance = constructor.newInstance();
                    if(ModList.get().isLoaded(compat.modid())) {
                        instance.init();
                    }
                }

            } catch (Exception e) {
                Stellaris.LOG.error(e.toString());
            }
        }
    }

}
