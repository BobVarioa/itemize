package com.bobvarioa.mobitems.sounds;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;

public interface ExtendedSoundManager {
    boolean mobitems$isActive(ResourceLocation location, SoundSource source);
}
