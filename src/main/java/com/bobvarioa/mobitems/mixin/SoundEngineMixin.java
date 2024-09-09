package com.bobvarioa.mobitems.mixin;

import com.bobvarioa.mobitems.sounds.ExtendedSoundManager;
import com.google.common.collect.Multimap;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SoundEngine.class)
public class SoundEngineMixin implements ExtendedSoundManager {
    @Final
    @Shadow
    private Multimap<SoundSource, SoundInstance> instanceBySource;



    @Override
    public boolean mobitems$isActive(ResourceLocation location, SoundSource source) {
        for (SoundInstance inst :  instanceBySource.get(source)) {
            if (inst.getLocation().equals(location)) {
                return true;
            }
        }
        return false;
    }
}
