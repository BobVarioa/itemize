package com.bobvarioa.mobitems.mixin;

import com.bobvarioa.mobitems.entity.EntitySoulAccessor;
import com.bobvarioa.mobitems.render.BaseMobItemRenderer;
import com.bobvarioa.mobitems.render.DummyRandomSource;
import com.bobvarioa.mobitems.entity.EntityUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin implements EntitySoulAccessor {
    
    @Shadow
    public RandomSource random;


    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V")
    public void constructor(EntityType pEntityType, Level pLevel, CallbackInfo ci) {
        if (pLevel.isClientSide && BaseMobItemRenderer.isInMobItemRenderer) {
            this.random = new DummyRandomSource();
        }
    }

    @Unique
    public float mobitems$soul = 0.0f;
    @Unique
    public float mobitems$usedSoul = 0.0f;

    @Inject(at = @At("HEAD"), method = "saveWithoutId(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;")
    public void saveWithoutId(CompoundTag pCompound, CallbackInfoReturnable<CompoundTag> cir) {
        pCompound.putFloat(EntityUtils.SOUL_KEY, mobitems$soul);
        pCompound.putFloat(EntityUtils.USED_SOUL_KEY, mobitems$soul);
    }

    @Inject(at = @At("HEAD"), method = "load(Lnet/minecraft/nbt/CompoundTag;)V")
    public void load(CompoundTag pCompound, CallbackInfo ci) {
        mobitems$soul = pCompound.getFloat(EntityUtils.SOUL_KEY);
        mobitems$usedSoul = pCompound.getFloat(EntityUtils.USED_SOUL_KEY);
    }

    @Override
    public float getSoul() {
        return mobitems$soul;
    }

    @Override
    public void setSoul(float soul) {
        mobitems$soul = soul;
    }

    @Override
    public float getUsedSoul() {
        return mobitems$usedSoul;
    }

    @Override
    public void setUsedSoul(float usedSoul) {
        mobitems$usedSoul = usedSoul;
    }

}
