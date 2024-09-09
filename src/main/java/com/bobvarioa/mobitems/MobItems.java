package com.bobvarioa.mobitems;

import com.bobvarioa.mobitems.entity.EntityUtils;
import com.bobvarioa.mobitems.register.ModBlockEntities;
import com.bobvarioa.mobitems.register.ModBlocks;
import com.bobvarioa.mobitems.register.ModItems;
import com.bobvarioa.mobitems.register.ModSounds;
import com.bobvarioa.mobitems.render.MobItemRenderer;
import com.bobvarioa.mobitems.blocks.SpawnCapturerEntity;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;

import static com.bobvarioa.mobitems.items.MobItem.mobItemOf;

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
                    output.accept(mobItemOf(level, ele));
                }
            }).build());

    public MobItems(IEventBus modBus) {

        ModItems.ITEMS.register(modBus);
        ModBlocks.BLOCKS.register(modBus);
        ModBlockEntities.BLOCK_ENTITY_TYPES.register(modBus);
        ModSounds.SOUNDS.register(modBus);
        CREATIVE_MODE_TABS.register(modBus);

        modBus.addListener(MobItems::clientInit);

        NeoForge.EVENT_BUS.addListener(SpawnCapturerEntity::onEntitySpawn);
    }

    private static void clientInit(final FMLClientSetupEvent event) {
        ItemProperties.register(ModItems.MOB_ITEM.get(), ModItems.MOB_ITEM.getId(), MobItemRenderer.getInstance());
    }
}
