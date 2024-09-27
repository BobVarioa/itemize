package com.bobvarioa.mobitems.render;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MobItemGeometryLoader {
	public static class MyGeometryLoader implements IGeometryLoader<MyGeometry> {
		// It is highly recommended to use a singleton pattern for geometry loaders, as all models can be loaded through one loader.
		public static final MyGeometryLoader INSTANCE = new MyGeometryLoader();
		// The id we will use to register this loader. Also used in the loader datagen class.
		public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("examplemod", "my_custom_loader");

		// In accordance with the singleton pattern, make the constructor private.        
		private MyGeometryLoader() {
		}

		@Override
		public MyGeometry read(JsonObject jsonObject, JsonDeserializationContext context) throws JsonParseException {
			// Use the given JsonObject and, if needed, the JsonDeserializationContext to get properties from the model JSON.
			// The MyGeometry constructor may have constructor parameters (see below).
			return new MyGeometry();
		}
	}

	public static class MyGeometry implements IUnbakedGeometry<MyGeometry> {
		// The constructor may have any parameters you need, and store them in fields for further usage below.
		// If the constructor has parameters, the constructor call in MyGeometryLoader#read must match them.
		public MyGeometry() {
		}

		// Method responsible for model baking, returning our dynamic model. Parameters in this method are:
		// - The geometry baking context. Contains many properties that we will pass into the model, e.g. light and ao values.
		// - The model baker. Can be used for baking sub-models.
		// - The sprite getter. Maps materials (= texture variables) to TextureAtlasSprites. Materials can be obtained from the context.
		//   For example, to get a model's particle texture, call spriteGetter.apply(context.getMaterial("particle"));
		// - The model state. This holds the properties from the blockstate file, e.g. rotations and the uvlock boolean.
		// - The item overrides. This is the code representation of an "overrides" block in an item model.

		// Method responsible for correctly resolving parent properties. Required if this model loads any nested models or reuses the vanilla loader on itself (see below).
		@Override
		public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
			// UnbakedModel#resolveParents
		}

		@Override
		public BakedModel bake(IGeometryBakingContext context, ModelBaker modelBaker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
			// See info on the parameters below.
			return new MyDynamicModel(context.useAmbientOcclusion(), context.isGui3d(), context.useBlockLight(),
				spriteGetter.apply(context.getMaterial("particle")), overrides);
		}
	}

	// BakedModelWrapper can be used as well to return default values for most methods, allowing you to only override what actually needs to be overridden.
	public static class MyDynamicModel implements IDynamicBakedModel {
		// Material of the missing texture. Its sprite can be used as a fallback when needed.
		private static final Material MISSING_TEXTURE =
			new Material(TextureAtlas.LOCATION_BLOCKS, MissingTextureAtlasSprite.getLocation());

		// Attributes for use in the methods below. Optional, the methods may also use constant values if applicable.
		private final boolean useAmbientOcclusion;
		private final boolean isGui3d;
		private final boolean usesBlockLight;
		private final TextureAtlasSprite particle;
		private final ItemOverrides overrides;

		// The constructor does not require any parameters other than the ones for instantiating the final fields.
		// It may specify any additional parameters to store in fields you deem necessary for your model to work.
		public MyDynamicModel(boolean useAmbientOcclusion, boolean isGui3d, boolean usesBlockLight, TextureAtlasSprite particle, ItemOverrides overrides) {
			this.useAmbientOcclusion = useAmbientOcclusion;
			this.isGui3d = isGui3d;
			this.usesBlockLight = usesBlockLight;
			this.particle = particle;
			this.overrides = overrides;
		}

		// Use our attributes. Refer to the article on baked models for more information on the method's enchantments.
		@Override
		public boolean useAmbientOcclusion() {
			return useAmbientOcclusion;
		}

		@Override
		public boolean isGui3d() {
			return isGui3d;
		}

		@Override
		public boolean usesBlockLight() {
			return usesBlockLight;
		}

		@Override
		public TextureAtlasSprite getParticleIcon() {
			// Return MISSING_TEXTURE.sprite() if you don't need a particle, e.g. when in an item model context.
			return particle;
		}

		@Override
		public ItemOverrides getOverrides() {
			// Return ItemOverrides.EMPTY when in a block model context.
			return overrides;
		}

		// Override this to true if you want to use a custom block entity renderer instead of the default renderer.
		@Override
		public boolean isCustomRenderer() {
			return true;
		}

		// This is where the magic happens. Return a list of the quads to render here. Parameters are:
		// - The blockstate being rendered. May be null if rendering an item.
		// - The side being culled against. May be null, which means quads that cannot be occluded should be returned.
		// - A client-bound random source you can use for randomizing stuff.
		// - The extra data to use. Originates from a block entity (if present), or from BakedModel#getModelData().
		// - The render type for which quads are being requested.
		// NOTE: This may be called many times in quick succession, up to several times per block.
		// This should be as fast as possible and use caching wherever applicable.
		@Override
		public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData extraData, @Nullable RenderType renderType) {
			List<BakedQuad> quads = new ArrayList<>();
			// add elements to the quads list as needed here
			return quads;
		}
	}
}