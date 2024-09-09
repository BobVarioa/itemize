package com.bobvarioa.mobitems.mixin;

import com.bobvarioa.mobitems.sounds.ExtendedSoundManager;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SoundManager.class)
public class SoundManagerMixin implements ExtendedSoundManager {
    @Final
    @Shadow
    private SoundEngine soundEngine;


    @Override
    public boolean mobitems$isActive(ResourceLocation location, SoundSource source) {
        return ((ExtendedSoundManager)(soundEngine)).mobitems$isActive(location, source);
    }
}
