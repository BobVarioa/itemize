package com.bobvarioa.mobitems.register;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.entity.simulator.EmptyBehavior;
import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.entity.simulator.behaviors.entity.*;
import com.bobvarioa.mobitems.entity.simulator.behaviors.item.TotemOfUndyingBehavior;
import com.bobvarioa.mobitems.entity.simulator.behaviors.item.WhiteBannerBehavior;
import com.bobvarioa.mobitems.entity.simulator.enchantments.VoidStrikeEnchantment;
import com.bobvarioa.mobitems.entity.simulator.enchantments.*;
import com.bobvarioa.mobitems.register.events.RegisterMobEnchantmentEvent;
import com.bobvarioa.mobitems.register.events.RegisterEntityBehaviorEvent;
import com.bobvarioa.mobitems.register.events.RegisterItemBehaviorEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModBehaviors {
	private static final DeferredRegister<SimulatedBehavior.Supplier<?>> INTERNAL = DeferredRegister.create(ModRegistries.SIMULATED_BEHAVIORS, MobItems.MODID);

	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<EmptyBehavior>> EMPTY = INTERNAL.register("empty", () -> EmptyBehavior::new);
	
	// Mob Behaviors
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<BeeBehavior>> BEE = INTERNAL.register("bee_behavior", () -> BeeBehavior::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<BlazeBehavior>> BLAZE = INTERNAL.register("blaze_behavior", () -> BlazeBehavior::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<BoggedBehavior>> BOGGED = INTERNAL.register("bogged_behavior", () -> BoggedBehavior::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<CaveSpiderBehavior>> CAVE_SPIDER = INTERNAL.register("cave_spider_behavior", () -> CaveSpiderBehavior::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<CreeperBehavior>> CREEPER = INTERNAL.register("creeper_behavior", () -> CreeperBehavior::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<ElderGuardianBehavior>> ELDER_GUARDIAN = INTERNAL.register("elder_guardian_behavior", () -> ElderGuardianBehavior::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<GhastBehavior>> GHAST = INTERNAL.register("ghast_behavior", () -> GhastBehavior::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<GuardianBehavior>> GUARDIAN = INTERNAL.register("guardian_behavior", () -> GuardianBehavior::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<WitchBehavior>> WITCH = INTERNAL.register("witch_behavior", () -> WitchBehavior::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<WitherSkeletonBehavior>> WITHER_SKELETON = INTERNAL.register("wither_skeleton_behavior", () -> WitherSkeletonBehavior::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<SlimeBehavior>> SLIME = INTERNAL.register("slime_behavior", () -> SlimeBehavior::new);
	
	// Item Behaviors
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<TotemOfUndyingBehavior>> TOTEM_OF_UNDYING = INTERNAL.register("totem_of_undying_behavior", () -> TotemOfUndyingBehavior::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<WhiteBannerBehavior>> WHITE_BANNER = INTERNAL.register("white_banner_behavior", () -> WhiteBannerBehavior::new);
	
	// Mob Enchantments
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<ProtectionEnchantment>> PROTECTION = INTERNAL.register("protection", () -> ProtectionEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<KnockbackEnchantment>> KNOCKBACK = INTERNAL.register("knockback", () -> KnockbackEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<EchoEnchantment>> ECHO = INTERNAL.register("echo", () -> EchoEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<SharpnessEnchantment>> SHARPNESS = INTERNAL.register("sharpness", () -> SharpnessEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<CriticalHitEnchantment>> CRITICAL_HIT = INTERNAL.register("critical_hit", () -> CriticalHitEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<RecklessEnchantment>> RECKLESS = INTERNAL.register("reckless", () -> RecklessEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<FinalShoutEnchantment>> FINAL_SHOUT = INTERNAL.register("final_shout", () -> FinalShoutEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<RegenerationEnchantment>> REGENERATION = INTERNAL.register("regeneration", () -> RegenerationEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<LootingEnchantment>> LOOTING = INTERNAL.register("looting", () -> LootingEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<CommittedEnchantment>> COMMITTED = INTERNAL.register("committed", () -> CommittedEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<PainCycleEnchantment>> PAIN_CYCLE = INTERNAL.register("pain_cycle", () -> PainCycleEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<VoidStrikeEnchantment>> VOID_STRIKE = INTERNAL.register("void_strike", () -> VoidStrikeEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<FireAspectEnchantment>> FIRE_ASPECT = INTERNAL.register("fire_aspect", () -> FireAspectEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<FreezingEnchantment>> FREEZING = INTERNAL.register("freezing", () -> FreezingEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<LeechingEnchantment>> LEECHING = INTERNAL.register("leeching", () -> LeechingEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<GuardingStrikeEnchantment>> GUARDING_STRIKE = INTERNAL.register("guarding_strike", () -> GuardingStrikeEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<RampagingEnchantment>> RAMPAGING = INTERNAL.register("rampaging", () -> RampagingEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<StunningEnchantment>> STUNNING = INTERNAL.register("stunning", () -> StunningEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<WeakeningEnchantment>> WEAKENING = INTERNAL.register("weakening", () -> WeakeningEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<PoisonCloudEnchantment>> POISON_CLOUD = INTERNAL.register("poison_cloud", () -> PoisonCloudEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<DeflectEnchantment>> DEFLECT = INTERNAL.register("deflect", () -> DeflectEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<RushEnchantment>> RUSH = INTERNAL.register("rush", () -> RushEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<ThornsEnchantment>> THORNS = INTERNAL.register("thorns", () -> ThornsEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<RadianceEnchantment>> RADIANCE = INTERNAL.register("radiance", () -> RadianceEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<EnigmaResonatorEnchantment>> ENIGMA_RESONATOR = INTERNAL.register("enigma_resonator", () -> EnigmaResonatorEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<AnimaConduitEnchantment>> ANIMA_CONDUIT = INTERNAL.register("anima_conduit", () -> AnimaConduitEnchantment::new);
	public static final DeferredHolder<SimulatedBehavior.Supplier<?>, SimulatedBehavior.Supplier<SoulSiphonEnchantment>> SOUL_SIPHON = INTERNAL.register("soul_siphon", () -> SoulSiphonEnchantment::new);
	
	public static void register(IEventBus bus) {
		INTERNAL.register(bus);
		bus.addListener(ModBehaviors::registerMobBehaviors);
		bus.addListener(ModBehaviors::registerItemBehaviors);
		bus.addListener(ModBehaviors::registerMobEnchantments);
	}
	private static void registerMobBehaviors(RegisterEntityBehaviorEvent event) {
		event.register(EntityType.BEE, BEE.get());
		event.register(EntityType.BLAZE, BLAZE.get());
		event.register(EntityType.BOGGED, BOGGED.get());
		event.register(EntityType.CAVE_SPIDER, CAVE_SPIDER.get());
		event.register(EntityType.CREEPER, CREEPER.get());
		event.register(EntityType.ELDER_GUARDIAN, ELDER_GUARDIAN.get());
		event.register(EntityType.GHAST, GHAST.get());
		event.register(EntityType.GUARDIAN, GUARDIAN.get());
		event.register(EntityType.WITCH, WITCH.get());
		event.register(EntityType.WITHER_SKELETON, WITHER_SKELETON.get());
		event.register(EntityType.SLIME, SLIME.get());
	}
	
	private static void registerItemBehaviors(RegisterItemBehaviorEvent event) {
		event.register(Items.TOTEM_OF_UNDYING, TOTEM_OF_UNDYING.get());
		event.register(Items.WHITE_BANNER, WHITE_BANNER.get());
	}

	private static void registerMobEnchantments(RegisterMobEnchantmentEvent event) {
		event.register(PROTECTION.get());
		event.register(KNOCKBACK.get());
		event.register(ECHO.get());
		event.register(SHARPNESS.get());
		event.register(CRITICAL_HIT.get());
		event.register(RECKLESS.get());
		event.register(FINAL_SHOUT.get());
		event.register(REGENERATION.get());
//		event.register(LOOTING.get());
		event.register(COMMITTED.get());
		event.register(PAIN_CYCLE.get());
		event.register(VOID_STRIKE.get());
		event.register(FIRE_ASPECT.get());
		event.register(FREEZING.get());
		event.register(LEECHING.get());
		event.register(GUARDING_STRIKE.get());
		event.register(RAMPAGING.get());
		event.register(STUNNING.get());
		event.register(WEAKENING.get());
		event.register(POISON_CLOUD.get());
		event.register(DEFLECT.get());
		event.register(RUSH.get());
		event.register(THORNS.get());
		event.register(RADIANCE.get());
		event.register(ENIGMA_RESONATOR.get());
		event.register(ANIMA_CONDUIT.get());
		event.register(SOUL_SIPHON.get());
	}
}
