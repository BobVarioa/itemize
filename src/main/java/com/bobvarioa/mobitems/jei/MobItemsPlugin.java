package com.bobvarioa.mobitems.jei;

import com.bobvarioa.mobitems.MobItems;
import com.bobvarioa.mobitems.entity.simulator.SimulatedBehavior;
import com.bobvarioa.mobitems.items.MobItem;
import com.bobvarioa.mobitems.recipe.MobEnchantmentRecipe;
import com.bobvarioa.mobitems.register.ModItems;
import com.bobvarioa.mobitems.register.ModRecipeTypes;
import com.bobvarioa.mobitems.register.ModRegistries;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// we have to use the unparameterized version for type checking reasons
@SuppressWarnings("rawtypes")
@JeiPlugin
public class MobItemsPlugin implements IModPlugin {

	@Override
	public ResourceLocation getPluginUid() {
		return ResourceLocation.fromNamespaceAndPath(MobItems.MODID, "jei");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new MobEnchantmentCategory(registration.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registration) {
		List<SimulatedBehavior.Supplier> list = new ArrayList<>();
		list.addAll(ModRegistries.MOB_ENCHANTMENTS);
		registration.register(MobEnchantmentIngredient.INSTANCE, list, MobEnchantmentIngredientHelper.INSTANCE, MobEnchantmentRenderer.LIST_INSTANCE);
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {
		registration.registerSubtypeInterpreter(ModItems.MOB_ITEM.get(), (ingredient, context) -> MobItem.getEntityData(ingredient).getString("id"));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(ModItems.MOB_ENCHANTER.get()), MobEnchantmentCategory.MOB_ENCHANTMENT_TYPE);
	}

	@Override
	public void registerModInfo(IModInfoRegistration modAliasRegistration) {
		modAliasRegistration.addModAliases("mobitems", "Itemize", "itemize");
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		List<MobEnchantmentRecipe> list = new ArrayList<>();
		var recipeManager = Minecraft.getInstance().level.getRecipeManager();
		for (var recipe : recipeManager.getAllRecipesFor(ModRecipeTypes.MOB_ENCHANTMENT.get())) {
			list.add(recipe.value());
		}
		registration.addRecipes(MobEnchantmentCategory.MOB_ENCHANTMENT_TYPE, list);

		registration.addItemStackInfo(new ItemStack(ModItems.CONVERTER_ITEM.get()), Component.translatable("jei.mobitems.converter.description"));
		registration.addItemStackInfo(new ItemStack(ModItems.SPAWN_CAPTURER_ITEM.get()), Component.translatable("jei.mobitems.spawn_capturer.description"));
		registration.addItemStackInfo(new ItemStack(ModItems.MOB_SOUL.get()), Component.translatable("jei.mobitems.mob_soul.description"));
		registration.addItemStackInfo(new ItemStack(ModItems.DUSTY_MOB_SOUL.get()), Component.translatable("jei.mobitems.dusty_mob_soul.description"));
	}

	public static class MobEnchantmentIngredient implements IIngredientType<SimulatedBehavior.Supplier> {
		public static MobEnchantmentIngredient INSTANCE = new MobEnchantmentIngredient();
		@Override
		public Class<SimulatedBehavior.Supplier> getIngredientClass() {
			return SimulatedBehavior.Supplier.class;
		}
	}

	public static class MobEnchantmentIngredientHelper implements IIngredientHelper<SimulatedBehavior.Supplier> {
		public static MobEnchantmentIngredientHelper INSTANCE = new MobEnchantmentIngredientHelper();

		@Override
		public IIngredientType<SimulatedBehavior.Supplier> getIngredientType() {
			return MobEnchantmentIngredient.INSTANCE;
		}

		@Override
		public String getDisplayName(SimulatedBehavior.Supplier ingredient) {
			return I18n.get(ingredient.get().id().toLanguageKey("tooltip.mob_enchantment"));
		}

		@Override
		public String getUniqueId(SimulatedBehavior.Supplier ingredient, UidContext context) {
			return ingredient.get().id().toString();
		}

		@Override
		public ResourceLocation getResourceLocation(SimulatedBehavior.Supplier ingredient) {
			return ingredient.get().id();
		}

		@Override
		public SimulatedBehavior.Supplier<?> copyIngredient(SimulatedBehavior.Supplier ingredient) {
			return ingredient; // instance is immutable anyways so this is fine
		}

		@Override
		public String getErrorInfo(SimulatedBehavior.@Nullable Supplier ingredient) {
			return "null ingredient"; // this is really the only error you could get, i think?
		}
	}

	public static record MobEnchantmentRenderer(boolean list) implements IIngredientRenderer<SimulatedBehavior.Supplier> {
		public static MobEnchantmentRenderer LIST_INSTANCE = new MobEnchantmentRenderer(true);
		public static MobEnchantmentRenderer GUI_INSTANCE = new MobEnchantmentRenderer(false);

		@Override
		public int getWidth() {
			return list ? 16 : 25;
		}

		@Override
		public int getHeight() {
			return list ? 16 : 25;
		}

		@Override
		public void render(GuiGraphics guiGraphics, SimulatedBehavior.Supplier ingredient) {
			ResourceLocation id = ingredient.get().id();
			var sprite = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "mob_enchantment/" + id.getPath());

			if (list) {
				guiGraphics.blitSprite(sprite, 0, 0, 16, 16);
			} else {
				guiGraphics.blitSprite(sprite, -4, -4, 32, 32);
			}
		}

		@Override
		public List<Component> getTooltip(SimulatedBehavior.Supplier ingredient, TooltipFlag tooltipFlag) {
			List<Component> list = new ArrayList<>();
			ResourceLocation id = ingredient.get().id();
			list.add(Component.translatable(id.toLanguageKey("tooltip.mob_enchantment")));
			list.add(Component.translatable(id.toLanguageKey("tooltip.mob_enchantment", "description")));
			return list;
		}
	}

	public record MobEnchantmentCategory(IGuiHelper guiHelper) implements IRecipeCategory<MobEnchantmentRecipe> {
		public static RecipeType<MobEnchantmentRecipe> MOB_ENCHANTMENT_TYPE = RecipeType.create(MobItems.MODID, "mob_enchantment", MobEnchantmentRecipe.class);

		@Override
		public RecipeType<MobEnchantmentRecipe> getRecipeType() {
			return MOB_ENCHANTMENT_TYPE;
		}

		@Override
		public Component getTitle() {
			return Component.translatable("block.mobitems.mob_enchanter");
		}

		@Override
		public IDrawable getBackground() {
			return guiHelper.drawableBuilder(
				ResourceLocation.fromNamespaceAndPath("mobitems", "textures/gui/container/mob_enchanter_jei.png"), 
				0, 0, 79, 31
			).setTextureSize(79, 31).build();
		}

		@Override
		public @Nullable IDrawable getIcon() {
			return guiHelper.createDrawableItemStack(new ItemStack(ModItems.MOB_ENCHANTER.get()));
		}

		@Override
		public void setRecipe(IRecipeLayoutBuilder builder, MobEnchantmentRecipe recipe, IFocusGroup focuses) {
			builder.addSlot(RecipeIngredientRole.INPUT, 4, 7)
					.addIngredients(recipe.catalyst());
			
			builder.addSlot(RecipeIngredientRole.OUTPUT, 51, 3)
				.addIngredient(MobEnchantmentIngredient.INSTANCE, recipe.enchantment().value())
				.setCustomRenderer(MobEnchantmentIngredient.INSTANCE, MobEnchantmentRenderer.GUI_INSTANCE);
		}
	}

}
