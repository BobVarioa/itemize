package com.bobvarioa.mobitems.register;

import com.bobvarioa.mobitems.MobItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.EventBus;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, MobItems.MODID);

    public static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MobItems.MODID, name)));
    }

    public static final DeferredHolder<SoundEvent, SoundEvent> VACUUM_START = registerSound("vacuum_start");
    public static final DeferredHolder<SoundEvent, SoundEvent> VACUUM_LOOP = registerSound("vacuum_loop");
    public static final DeferredHolder<SoundEvent, SoundEvent> VACUUM_END = registerSound("vacuum_end");

	public static void register(IEventBus bus) {
		SOUNDS.register(bus);
	}
}
