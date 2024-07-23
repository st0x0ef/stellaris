package com.st0x0ef.stellaris.platform.systems.core.energy.lookup;

import com.st0x0ef.stellaris.platform.systems.core.storage.base.ValueStorage;
import com.st0x0ef.stellaris.platform.systems.lookup.EntityLookup;
import com.st0x0ef.stellaris.platform.systems.core.energy.wrappers.CommonEnergyStorage;
import com.st0x0ef.stellaris.platform.systems.core.energy.wrappers.NeoEnergyContainer;
import com.st0x0ef.stellaris.platform.systems.lookup.RegistryEventListener;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class EnergyEntityLookup implements EntityLookup<ValueStorage, Direction>, RegistryEventListener {
    public static final EnergyEntityLookup INSTANCE = new EnergyEntityLookup();
    private final List<Consumer<EntityLookup.EntityRegistrar<ValueStorage, Direction>>> registrars = new ArrayList<>();

    private EnergyEntityLookup() {
        registerSelf();
    }

    @Override
    public @Nullable ValueStorage find(Entity entity, Direction context) {
        IEnergyStorage capability = entity.getCapability(Capabilities.EnergyStorage.ENTITY, context);
        if (capability instanceof NeoEnergyContainer(ValueStorage container)) {
            return container;
        }
        return capability == null ? null : new CommonEnergyStorage(capability);
    }

    @Override
    public void onRegister(Consumer<EntityRegistrar<ValueStorage, Direction>> registrar) {
        registrars.add(registrar);
    }

    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept((getter, containers) -> {
            for (EntityType<?> container : containers) {
                event.registerEntity(Capabilities.EnergyStorage.ENTITY, container, (entity, direction) -> {
                    ValueStorage storage = getter.getContainer(entity, direction);
                    return storage == null ? null : new NeoEnergyContainer(storage);
                });
            }
        }));
    }
}
