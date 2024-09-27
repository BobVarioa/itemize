package com.bobvarioa.mobitems.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
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
	public static final Map<String, BBOverride> mobBBOverrides = new HashMap<>();
    public static boolean isInMobItemRenderer = false;
	
	record BBOverride(double scale) { }

    record ScaleAndTransOverrides(float scale, float x, float y, float z) { }

    static {
        mobOverrides.put("minecraft:squid", new ScaleAndTransOverrides(-0.2f, 0f, 0.4f, 0f));
        mobOverrides.put("minecraft:glow_squid", new ScaleAndTransOverrides(-0.2f, 0f, 0.4f, 0f));
        mobOverrides.put("minecraft:ghast", new ScaleAndTransOverrides(-0.1f, 0f, 0.4f, 0f));
        mobOverrides.put("minecraft:zoglin", new ScaleAndTransOverrides(-0.3f, 0f, 0f, 0f));
        mobOverrides.put("minecraft:hoglin", new ScaleAndTransOverrides(-0.2f, 0f, 0f, 0f));
        mobOverrides.put("minecraft:shulker", new ScaleAndTransOverrides(-0.2f, 0f, 0f, 0f));
        mobOverrides.put("minecraft:bat", new ScaleAndTransOverrides(0.1f, 0f, 0f, 0f));
        mobOverrides.put("minecraft:ender_dragon", new ScaleAndTransOverrides(0.0f, 0f, 0.4f, 0f));

		mobBBOverrides.put("minecraft:ender_dragon", new BBOverride(7.0D));
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

        double scaleDivisor = mobBBOverrides.containsKey(id) ? mobBBOverrides.get(id).scale : Math.max(mob.getBbWidth(), mob.getBbHeight());
        if (scaleDivisor > 1.0D) {
            entityScale /= scaleDivisor;
        }

		var override = mobOverrides.get(id);
        poseStack.translate(0.5F, 0.05F, 0.5F);
		switch (displayContext) {
			case GUI ->  {
				poseStack.mulPose(Axis.XP.rotationDegrees(15));
				poseStack.mulPose(Axis.YP.rotationDegrees(45));
			}
			
			case GROUND -> {
				poseStack.translate(0F, 0.2F, 0F);
				
			}
			
			case FIXED -> {
				entityScale += 0.2f;
				poseStack.mulPose(Axis.YP.rotationDegrees(180));
				poseStack.translate(0F, -0.2F, 0.1F);
			}
			
			case THIRD_PERSON_LEFT_HAND, THIRD_PERSON_RIGHT_HAND -> {
				entityScale /= 2;
				poseStack.translate(0f, 0.45f, 0f);
				poseStack.mulPose(Axis.YP.rotationDegrees(180));
				override = null;
			}
			
			case FIRST_PERSON_LEFT_HAND, FIRST_PERSON_RIGHT_HAND -> {
				poseStack.mulPose(Axis.YP.rotationDegrees(180));
				if (mob.getBbHeight() > 1.0D) {
					poseStack.translate(0f, 0.15f, 0f);
					
				}
				override = null;
			}
			
			case HEAD -> {
				entityScale += 0.1f;
				poseStack.translate(0f, 0.7f, 0f);
				poseStack.mulPose(Axis.YP.rotationDegrees(180));
				override = null;
			}
			
			default -> {}
		}
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.wrapDegrees(rotationDegrees)));

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
