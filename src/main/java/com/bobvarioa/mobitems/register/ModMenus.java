package com.bobvarioa.mobitems.register;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.gui.menu.MobColosseumMenu;
import com.bobvarioa.mobitems.gui.menu.MobEnchanterMenu;
import com.bobvarioa.mobitems.gui.screen.MobColosseumScreen;
import com.bobvarioa.mobitems.gui.menu.MobModifierMenu;
import com.bobvarioa.mobitems.gui.screen.MobEnchanterScreen;
import com.bobvarioa.mobitems.gui.screen.MobModifierScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenus {
	private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, MobItems.MODID);
	public static Supplier<MenuType<MobColosseumMenu>> MOB_COLOSSEUM = MENUS.register("mob_colosseum", 
		() -> IMenuTypeExtension.create(MobColosseumMenu::new)
	);

	public static Supplier<MenuType<MobModifierMenu>> MOB_MODIFIER = MENUS.register("mob_modifier",
		() -> IMenuTypeExtension.create(MobModifierMenu::new)
	);

	public static Supplier<MenuType<MobEnchanterMenu>> MOB_ENCHANTER = MENUS.register("mob_enchanter",
		() -> IMenuTypeExtension.create(MobEnchanterMenu::new)
	);

	public static void registerScreens(RegisterMenuScreensEvent event) {
		event.register(MOB_COLOSSEUM.get(), MobColosseumScreen::new);
		event.register(MOB_MODIFIER.get(), MobModifierScreen::new);
		event.register(MOB_ENCHANTER.get(), MobEnchanterScreen::new);
	}

	public static void register(IEventBus bus) {
		MENUS.register(bus);
	}
}
