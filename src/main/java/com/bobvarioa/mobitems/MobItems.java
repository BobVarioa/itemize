package com.bobvarioa.mobitems;

import com.bobvarioa.mobitems.helpers.EntityUtils;
import com.bobvarioa.mobitems.entity.SoulDropHandler;
import com.bobvarioa.mobitems.register.*;
import com.bobvarioa.mobitems.register.events.RegisterMobEnchantmentEvent;
import com.bobvarioa.mobitems.register.events.RegisterEntityBehaviorEvent;
import com.bobvarioa.mobitems.register.events.RegisterItemBehaviorEvent;
import com.bobvarioa.mobitems.render.MobItemRenderer;
import com.bobvarioa.mobitems.blocks.entities.SpawnCapturerEntity;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;

import static com.bobvarioa.mobitems.items.MobItem.mobItemOf;
import static com.bobvarioa.mobitems.register.ModRegistries.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MobItems.MODID)
public class MobItems {
	public static final String MODID = "mobitems";
	private static final Logger LOGGER = LogUtils.getLogger();

	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOBITEMS_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
		.title(Component.translatable("itemGroup.mobitems"))
		.withTabsBefore(CreativeModeTabs.COMBAT)
		.icon(() -> mobItemOf(Minecraft.getInstance().level, EntityType.ZOMBIE))
		.displayItems((parameters, output) -> {
			output.accept(new ItemStack(ModItems.MOB_SOUL.get()));
			output.accept(new ItemStack(ModItems.DUSTY_MOB_SOUL.get()));
			output.accept(new ItemStack(ModItems.CONVERTER_ITEM.get()));
			output.accept(new ItemStack(ModItems.CONVERTER_CASING_ITEM.get()));
			output.accept(new ItemStack(ModItems.SOUL_INSERTER_ITEM.get()));
			output.accept(new ItemStack(ModItems.SOUL_EXTRACTOR_ITEM.get()));
			output.accept(new ItemStack(ModItems.SPAWN_CAPTURER_ITEM.get()));
			output.accept(new ItemStack(ModItems.MOB_COLOSSEUM.get()));
			output.accept(new ItemStack(ModItems.MOB_MODIFIER.get()));
			output.accept(new ItemStack(ModItems.MOB_ENCHANTER.get()));
			output.accept(new ItemStack(ModItems.WORM_FOOD.get()));
			output.accept(new ItemStack(ModItems.CREATIVE_POCKET_CONVERTER.get()));
			output.accept(new ItemStack(ModItems.RITUAL_TABLET.get()));
			output.accept(new ItemStack(ModItems.VACUUM.get()));

			Set<EntityType<?>> set = new HashSet<>();
			for (var ele : BuiltInRegistries.ENTITY_TYPE.entrySet()) {
				var entityType = ele.getValue();
				if (entityType.getCategory() != MobCategory.MISC || entityType.equals(EntityType.VILLAGER) || entityType.equals(EntityType.IRON_GOLEM)) {
					set.add(entityType);
				}
			}

			var level = Minecraft.getInstance().level;

			for (var ele : set.stream().toList()) {
				Entity entity = ele.create(level);
				if (entity instanceof LivingEntity le) {
					output.accept(EntityUtils.entityToItem(le));
				}
			}
		}).build());

	private final IEventBus modBus;
	public MobItems(IEventBus modBus) {
		ModRegistries.register(modBus);

		ModDataComponents.register(modBus);
		ModItems.register(modBus);
		ModBlocks.register(modBus);
		ModBlockEntities.register(modBus);
		ModSounds.register(modBus);
		ModAttachments.register(modBus);
		CREATIVE_MODE_TABS.register(modBus);
		ModMenus.register(modBus);
		ModBehaviors.register(modBus);
		ModSimulatedEffects.register(modBus);
		ModRecipeTypes.register(modBus);
		ModRecipeSerializers.register(modBus);
		
		modBus.addListener(ModMenus::registerScreens);
		modBus.addListener(MobItemRenderer::clientInit);
		modBus.addListener(this::loadComplete);

		NeoForge.EVENT_BUS.addListener(SpawnCapturerEntity::onEntitySpawn);
		NeoForge.EVENT_BUS.addListener(SoulDropHandler::entityDrops);
		
		this.modBus = modBus;
	}
	
	private void loadComplete(FMLLoadCompleteEvent event) {
		modBus.post(new RegisterItemBehaviorEvent(ITEM_BEHAVIORS::put));
		modBus.post(new RegisterEntityBehaviorEvent(ENTITY_BEHAVIORS::put));
		modBus.post(new RegisterMobEnchantmentEvent(MOB_ENCHANTMENTS::add));
	}
}
