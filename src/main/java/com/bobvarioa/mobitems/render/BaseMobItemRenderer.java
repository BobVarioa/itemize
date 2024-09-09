package com.bobvarioa.mobitems.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BaseMobItemRenderer {
    public static final Map<String, ScaleAndTransOverrides> mobOverrides = new HashMap<>();
    public static boolean isInMobItemRenderer = false;

    record ScaleAndTransOverrides(float scale, float x, float y, float z) {
    }

    static {
        mobOverrides.put("minecraft:squid", new ScaleAndTransOverrides(-0.2f, 0f, 0.4f, 0f));
        mobOverrides.put("minecraft:glow_squid", new ScaleAndTransOverrides(-0.2f, 0f, 0.4f, 0f));
        mobOverrides.put("minecraft:ghast", new ScaleAndTransOverrides(-0.1f, 0f, 0.4f, 0f));
        mobOverrides.put("minecraft:zoglin", new ScaleAndTransOverrides(-0.3f, 0f, 0f, 0f));
        mobOverrides.put("minecraft:hoglin", new ScaleAndTransOverrides(-0.2f, 0f, 0f, 0f));
        mobOverrides.put("minecraft:shulker", new ScaleAndTransOverrides(-0.2f, 0f, 0f, 0f));
        mobOverrides.put("minecraft:bat", new ScaleAndTransOverrides(0.1f, 0f, 0f, 0f));
//        mobOverrides.put("minecraft:ender_dragon", new ScaleAndTransOverrides(-0.1f, 0f, 0.4f, 0f));
    }


    public static final Vector3f XP = new Vector3f(1.0F, 0.0F, 0.0F);
    public static final Vector3f YP = new Vector3f(0.0F, 1.0F, 0.0F);

    public static Quaternionf rotationDegrees(Vector3f vec, float degrees) {
        return new Quaternionf().set(new AxisAngle4f(((float) Math.toRadians(degrees)), vec));
    }

    public static void render(CompoundTag tag, float rotationDegrees, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        var id = tag.getString("id");
        Optional<EntityType<?>> optionalEntityType = EntityType.byString(id);
        if (!optionalEntityType.isPresent()) return;
        var mobType = optionalEntityType.get();
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        isInMobItemRenderer = true;
        Entity mob = mobType.create(mc.level);
        isInMobItemRenderer = false;
        if (mob == null) {
            return;
        }
        mob.load(tag);

        // the following stuff attempts to zero out anything that might change between renders

        mob.setXRot(0);
        mob.xRotO = 0;
        mob.setYRot(0);
        mob.yRotO = 0;

        mob.setYHeadRot(0);
        mob.setYBodyRot(0);

        mob.tickCount = 0;

        mob.xOld = mob.getX();
        mob.yOld = mob.getY();
        mob.zOld = mob.getZ();
        mob.xo = mob.getX();
        mob.yo = mob.getY();
        mob.zo = mob.getZ();

        mob.setId(0); // for phantoms

        if (mob instanceof LivingEntity lmob) {
//            lmob.animationSpeed = 0;
//            lmob.animationSpeedOld = 0;
            lmob.yBodyRotO = 0;
            lmob.yHeadRotO = 0;
            lmob.attackAnim = 0;
            lmob.oAttackAnim = 0;
//            lmob.animationPosition = 0;

            if (lmob instanceof Guardian guardian) {
                // yes, there is no other good way around this
                guardian.clientSideTailAnimation = 0;
                guardian.clientSideTailAnimationO = 0;
            }
        }

        poseStack.pushPose();

        float entityScale = 0.85F;

        double scaleDivisor = Math.max(mob.getBbWidth(), mob.getBbHeight());
        if (scaleDivisor > 1.0D) {
            entityScale /= scaleDivisor;
        }

        poseStack.translate(0.5F, 0.05F, 0.5F);
        if (displayContext.equals(ItemDisplayContext.GUI)) {
            poseStack.mulPose(rotationDegrees(XP, 15));
            poseStack.mulPose(rotationDegrees(YP, 45));
        }
        if (displayContext.equals(ItemDisplayContext.FIXED)) {
            entityScale += 0.2f;
            poseStack.translate(0F, -0.2F, 0.1F);
        }
        if (displayContext.equals(ItemDisplayContext.THIRD_PERSON_LEFT_HAND) || displayContext.equals(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)) {
            poseStack.mulPose(rotationDegrees(YP, 180));
        }
        poseStack.mulPose(rotationDegrees(YP, Mth.wrapDegrees(rotationDegrees)));

        var override = mobOverrides.get(id);
        if (override != null) {
            entityScale += override.scale;
            poseStack.translate(override.x, override.y, override.z);
        }
        poseStack.scale(entityScale, entityScale, entityScale);

        EntityRenderDispatcher rendererManager = mc.getEntityRenderDispatcher();
        rendererManager.setRenderShadow(false);
        rendererManager.setRenderHitBoxes(false);

        rendererManager.render(mob, 0.0D, 0.0D, 0.0D, 0.0f, 0.0F, poseStack, buffer, packedLight);

        poseStack.popPose();
    }
}
