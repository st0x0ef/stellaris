package com.st0x0ef.stellaris.platform.systems.core.fluid.lookup;

import com.st0x0ef.stellaris.platform.systems.core.storage.base.CommonStorage;
import com.st0x0ef.stellaris.platform.systems.lookup.EntityLookup;
import com.st0x0ef.stellaris.platform.systems.resources.fluid.FluidResource;
import com.st0x0ef.stellaris.platform.systems.core.fluid.wrappers.CommonFluidContainer;
import com.st0x0ef.stellaris.platform.systems.core.fluid.wrappers.NeoFluidContainer;
import com.st0x0ef.stellaris.platform.systems.lookup.RegistryEventListener;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class FluidEntityLookup implements EntityLookup<CommonStorage<FluidResource>, Direction>, RegistryEventListener {
    public static final FluidEntityLookup INSTANCE = new FluidEntityLookup();
    private final List<Consumer<EntityLookup.EntityRegistrar<CommonStorage<FluidResource>, Direction>>> registrars = new ArrayList<>();

    private FluidEntityLookup() {
        registerSelf();
    }

    @Override
    public @Nullable CommonStorage<FluidResource> find(Entity entity, Direction context) {
        IFluidHandler handler = entity.getCapability(Capabilities.FluidHandler.ENTITY, context);

        if (handler instanceof NeoFluidContainer(CommonStorage<FluidResource> container)) {
            return container;
        }

        return handler == null ? null : new CommonFluidContainer(handler);
    }

    @Override
    public void onRegister(Consumer<EntityRegistrar<CommonStorage<FluidResource>, Direction>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept((getter, containers) -> {
            for (var container : containers) {
                event.registerEntity(Capabilities.FluidHandler.ENTITY, container, (entity, direction) -> {
                    CommonStorage<FluidResource> storage = getter.getContainer(entity, direction);
                    return storage == null ? null : new NeoFluidContainer(storage);
                });
            }
        }));
    }
}
