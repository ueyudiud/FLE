/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model.flexible;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import nebula.Nebula;
import nebula.base.Cache;
import nebula.base.JsonDeserializerGateway;
import nebula.client.model.ModelHelper;
import nebula.common.util.A;
import nebula.common.util.Jsons;
import nebula.common.util.L;
import nebula.common.util.R;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public enum NebulaModelDeserializer implements JsonDeserializer<IModel>
{
	GENERAL(Nebula.MODID, "general")
	{
		@Override
		public FlexibleModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
		{
			throw new JsonParseException("The general model are not available yet.");//XXX
		}
	},
	ITEM(Nebula.MODID, "item")
	{
		@Override
		public FlexibleModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
		{
			ImmutableList.Builder<INebulaModelPart> parts = ImmutableList.builder();
			JsonObject object = json.getAsJsonObject();
			
			JsonElement[] elements;
			if (object.has("layers"))
			{
				JsonArray array = object.getAsJsonArray("layers");
				elements = Iterables.toArray(array, JsonElement.class);
			}
			else
			{
				elements = new JsonElement[]{ object.get("layer") };
			}
			
			Cache<Boolean> flag = new Cache<>(false);
			ToIntFunction[] functions = new ToIntFunction[elements.length];
			int[] poses = new int[elements.length];
			List<Function<ItemStack, String>> datas = new ArrayList<>();
			
			A.executeAll(elements, (json1, i)-> {
				ModelPartItemLayer layer = new ModelPartItemLayer();
				layer.index = i;
				if (json1.isJsonObject())
				{
					JsonObject object2 = json1.getAsJsonObject();
					layer.icon = object2.get("texture").getAsString();
					layer.zOffset = Jsons.getOrDefault(object2, "zOffset", 0.5F);
					if (object2.has("meta"))
					{
						Function<ItemStack, String> function = context.deserialize(object2.get("meta"), SubmetaLoader.ItemSubmetaGetter.class);
						poses[i] = datas.indexOf(function);
						if (function != NebulaModelLoader.NORMAL_METAGENERATOR && poses[i] == -1)
						{
							poses[i] = datas.size();
							datas.add(function);
						}
					}
					else poses[i] = -1;
					if (object2.has("colorMultiplier"))
					{
						flag.set(true);
						functions[i] = NebulaModelLoader.loadItemColorMultiplier(object2.get("colorMultiplier").getAsString());
					}
					else
					{
						functions[i] = NebulaModelLoader.NORMAL_MULTIPLIER;
					}
					switch (Jsons.getOrDefault(object2, "type", "normal"))
					{
					case "flat" :
						layer = new ModelPartItemLayerFlat(layer);
						break;
					case "convert":
						layer = new ModelPartItemLayerConvert(layer, Jsons.getOrDefault(object2, "convert", "#convert"));
						break;
					default:
						break;
					}
				}
				else
				{
					layer.icon = json1.getAsString();
					layer.zOffset = 0.5F;
					functions[i] = NebulaModelLoader.NORMAL_MULTIPLIER;
					poses[i] = -1;
				}
				parts.add(layer);
			});
			
			FlexibleModel model = new FlexibleModel(NebulaModelLoader.INSTANCE.currentItem,
					deserializeOrDefault(object, context, ModelHelper.ITEM_STANDARD_TRANSFORMS),
					parts.build(), false, true, false);
			
			if (flag.get())
				model.itemColors = functions;
			model.itemLoadingData = poses;
			model.itemDataGen = L.cast(datas, Function.class);
			
			return (FlexibleModel) loadRetexture(model, object);
		}
	},
	BLOCK(Nebula.MODID, "block")
	{
		@Override
		public FlexibleModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
		{
			JsonObject object = json.getAsJsonObject();
			JsonArray array = object.getAsJsonArray("part");
			Cache<Boolean> flag1 = new Cache<>(false);
			Cache<Boolean> flag2 = new Cache<>(false);
			Function<ItemStack, String>[] func1 = new Function[array.size()];
			Function<IBlockState, String>[] func2 = new Function[array.size()];
			List<INebulaModelPart> parts = Jsons.getAsList(array, (i, j)->
			{
				INebulaModelPart part = deserialize(j, context);
				if (j.isJsonObject())
				{
					JsonObject obj = j.getAsJsonObject();
					if (obj.has("itemmeta"))
					{
						flag1.set(true);
						func1[i] = context.deserialize(obj.get("itemmeta"), SubmetaLoader.ItemSubmetaGetter.class);
					}
					else func1[i] = (Function<ItemStack, String>) NebulaModelLoader.NORMAL_METAGENERATOR;
					if (obj.has("blockmeta"))
					{
						flag2.set(true);
						func2[i] = context.deserialize(obj.get("blockmeta"), SubmetaLoader.BlockSubmetaGetter.class);
					}
					else func2[i] = (Function<IBlockState, String>) NebulaModelLoader.NORMAL_METAGENERATOR;
				}
				return part;
			});
			FlexibleModel model = new FlexibleModel(NebulaModelLoader.INSTANCE.currentItem,
					deserializeOrDefault(object, context, ModelHelper.BLOCK_STANDARD_TRANSFORMS),
					parts, Jsons.getOrDefault(object, "gui3D", true), Jsons.getOrDefault(object, "smooth_lighting", true), Jsons.getOrDefault(object, "builtIn", false));
			if (flag1.get())
			{
				model.itemDataGen = func1;
				model.itemLoadingData = A.rangeIntArray(func1.length);
			}
			if (flag2.get())
			{
				model.blockDataGen = func2;
				model.blockLoadingData = A.rangeIntArray(func2.length);
			}
			return (FlexibleModel) loadRetexture(model, object);
		}
	},
	VANILLA_ITEM("minecraft", "item/generated")
	{
		@Override
		public IModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
		{
			JsonObject object = json.getAsJsonObject();
			return loadRetexture(ItemLayerModel.INSTANCE, object);
		}
	},
	VANILLA("minecraft", "generated")
	{
		private final Object instance;
		private final Method loadModel;
		
		{
			try
			{
				Class<?> clazz = Class.forName("net.minecraftforge.client.model.ModelLoader$VanillaLoader");
				this.instance = R.getValue(clazz, "INSTANCE", "INSTANCE", null, true);
				this.loadModel = R.getMethod(clazz, "loadModel", ResourceLocation.class);
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
		
		@Override
		public IModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
		{
			try
			{
				return (IModel) this.loadModel.invoke(this.instance, NebulaModelLoader.INSTANCE.currentLocation);
			}
			catch (Exception exception)
			{
				return ModelLoaderRegistry.getMissingModel();
			}
		}
	};
	
	static class Transform
	{
		Map<TransformType, TRSRTransformation> map = new EnumMap<>(TransformType.class);
	}
	
	static final JsonDeserializerGateway<INebulaModelPart> BLOCK_MODEL_PART_DESERIALIZERS =
			new JsonDeserializerGateway<INebulaModelPart>("type", ModelPartVerticalCube.LOADER)
	{
		@Override
		public INebulaModelPart deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException
		{
			if (json.isJsonObject())
			{
				JsonObject obj = json.getAsJsonObject();
				INebulaModelPart part = super.deserialize(obj, typeOfT, context);
				if (obj.has("textures"))
				{
					part = part.retexture(Jsons.getAsMap(obj.getAsJsonObject("textures"), JsonElement::getAsString));
				}
				return part;
			}
			else if (json.isJsonPrimitive())
				return NebulaModelLoader.getModelPart(json.getAsString());
			else throw new JsonParseException("Unknown model part, got: " + json);
		}
	}
	.setThrowExceptionWhenNoMatched();
	
	public static INebulaModelPart deserialize(JsonElement json, JsonDeserializationContext context)
	{
		if (json.isJsonPrimitive())
		{
			return NebulaModelLoader.getModelPart(json.getAsString());
		}
		else if (json.isJsonArray())
		{
			return ModelPartCol.LOADER.deserialize(json, INebulaModelPart.class, context);
		}
		return BLOCK_MODEL_PART_DESERIALIZERS.deserialize(json, INebulaModelPart.class, context);
	}
	
	protected IModel loadRetexture(IRetexturableModel model, JsonObject object)
	{
		if (object.has("textures"))
		{
			return model.retexture(ImmutableMap.copyOf(Jsons.getAsMap(object.getAsJsonObject("textures"),
					json->json.isJsonObject() ? json.toString() : json.getAsString())));
		}
		return model;
	}
	
	static
	{
		BLOCK_MODEL_PART_DESERIALIZERS.addDeserializer("void", (j,t,c)->INebulaModelPart.VOID);
		BLOCK_MODEL_PART_DESERIALIZERS.addDeserializer("nebula:cube", ModelPartVerticalCube.LOADER);
		BLOCK_MODEL_PART_DESERIALIZERS.addDeserializer("nebula:quad", ModelPartQuad.LOADER);
		BLOCK_MODEL_PART_DESERIALIZERS.addDeserializer("multi", ModelPartCol.LOADER);
	}
	
	public static void registerBlockDeserializer(String key, JsonDeserializer<? extends INebulaModelPart> deserializer)
	{
		BLOCK_MODEL_PART_DESERIALIZERS.addDeserializer(key, deserializer);
	}
	
	public static ImmutableMap<TransformType, TRSRTransformation> deserializeOrDefault(JsonObject object,
			JsonDeserializationContext context, ImmutableMap<TransformType, TRSRTransformation> defaultTransformation)
	{
		return object.has("transform") ?
				ImmutableMap.copyOf(context.<Transform>deserialize(object, Transform.class).map) :
					defaultTransformation;
	}
	
	NebulaModelDeserializer(String modid, String path)
	{
		NebulaModelLoader.registerDeserializer(new ResourceLocation(modid, path), this);
	}
	
	/* Unused, override in each deserializer. */
	public IModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) { return null; }
}