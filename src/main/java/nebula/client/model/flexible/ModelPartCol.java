/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model.flexible;

import static nebula.client.model.flexible.NebulaModelDeserializer.deserialize;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import nebula.base.INode;
import nebula.base.IntegerEntry;
import nebula.base.IntegerMap;
import nebula.base.Node;
import nebula.base.function.Selector;
import nebula.base.function.WeightedRandomSelector;
import nebula.client.util.IIconCollection;
import nebula.common.util.Jsons;
import nebula.common.util.L;
import nebula.common.util.Strings;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

/**
 * @author ueyudiud
 */
public class ModelPartCol implements INebulaModelPart
{
	static final JsonDeserializer<ModelPartCol> LOADER = (json, typeOfT, context)-> {
		if (json.isJsonObject())
		{
			JsonObject object = json.getAsJsonObject();
			if (object.has("variants"))
			{
				Variant def = object.has("default") ? loadVariant(object.get("default"), context) : defaultVariant();
				Map<String, Map<String, IntegerMap<Variant>>> map =
						Jsons.getAsMap(object.getAsJsonObject("variants"),
								j->Jsons.getAsMap(j.getAsJsonObject(), j1-> {
									IntegerMap<Variant> m1 = new IntegerMap();
									if (j1.isJsonArray())
									{
										for (JsonElement json1 : j1.getAsJsonArray())
										{
											Variant variant = loadVariant(json1, context);
											int weight = json1.isJsonObject() ? Jsons.getOrDefault(json1.getAsJsonObject(), "weight", 1) : 1;
											m1.put(variant, weight);
										}
										return m1;
									}
									else
									{
										Variant variant = loadVariant(j1, context);
										m1.put(variant, 1);
										return m1;
									}
								}));
				IntegerMap<Variant> m1 = new IntegerMap();
				m1.put(def, 1);
				return new ModelPartCol(compose(map, def), m1, Jsons.getOrDefault(object, "extendData", false));
			}
			else
			{
				return new ModelPartCol(ImmutableMap.of(), loadVariant(object.get("variant"), context), Jsons.getOrDefault(object, "extendData", false));
			}
		}
		else if (json.isJsonArray())
		{
			IntegerMap<Variant> map = new IntegerMap();
			for (JsonElement json1 : json.getAsJsonArray())
			{
				Variant variant = loadVariant(json1, context);
				
				//If no model data exist, used default model.
				variant.parts = variant.parts.or(Optional.of(ImmutableList.of(new ModelPartVerticalCube())));
				
				int weight = json1.isJsonObject() ? Jsons.getOrDefault(json1.getAsJsonObject(), "weight", 1) : 1;
				map.put(variant, weight);
			}
			return new ModelPartCol(ImmutableMap.of(), map, false);
		}
		else throw new JsonParseException("Can not parse " + json);
	};
	
	private static final Selector<List<INebulaBakedModelPart>> NONE = Selector.single(ImmutableList.of());
	
	private static Map<Map<String, String>, IntegerMap<Variant>> compose(Map<String, Map<String, IntegerMap<Variant>>> map, Variant def)
	{
		INode<Entry<String, Map<String, IntegerMap<Variant>>>> node = Node.chain(map.entrySet());
		Map<Map<String, String>, IntegerMap<Variant>> states = new HashMap<>();
		IntegerMap<Variant> base = new IntegerMap<>();
		base.put(def, 1);
		put(node, ImmutableMap.of(), base, states);
		return ImmutableMap.copyOf(states);
	}
	
	private static void put(INode<Entry<String, Map<String, IntegerMap<Variant>>>> node, Map<String, String> base,
			IntegerMap<Variant> parent, Map<Map<String, String>, IntegerMap<Variant>> states)
	{
		if (node == null)
		{
			states.put(ImmutableMap.copyOf(base), parent);
		}
		else
		{
			String key = node.value().getKey();
			Map<String, String> base1 = new HashMap<>(base);
			for (Entry<String, IntegerMap<Variant>> e : node.value().getValue().entrySet())
			{
				base1.put(key, e.getKey());
				IntegerMap<Variant> next = new IntegerMap<>();
				for (IntegerEntry<Variant> e1 : parent)
				{
					for (IntegerEntry<Variant> e2 : e.getValue())
					{
						Variant variant2 = new Variant();
						variant2.or(e2.getKey(), e1.getKey());
						next.putOrAdd(variant2, e1.getValue() * e2.getValue());
					}
				}
				next.rescale();
				put(node.next(), base1, next, states);
			}
		}
	}
	
	private static Variant defaultVariant()
	{
		Variant variant = new Variant();
		variant.enable = Optional.absent();
		variant.parts = Optional.absent();
		variant.x = variant.y = Optional.absent();
		variant.retextures = Retextures.TOP;
		return variant;
	}
	
	private static Variant loadVariant(JsonElement json, JsonDeserializationContext context) throws JsonParseException
	{
		Variant variant;
		if (json.isJsonObject())
		{
			variant = new Variant();
			JsonObject object = json.getAsJsonObject();
			variant.x = !object.has("x") ? Optional.absent() : Optional.of(object.get("x").getAsInt());
			variant.y = !object.has("y") ? Optional.absent() : Optional.of(object.get("y").getAsInt());
			variant.enable = !object.has("enable") ? Optional.absent() : Optional.of(object.get("enable").getAsBoolean());
			variant.parts = Optional.fromNullable(loadParts(object, context));
			variant.retextures = !object.has("textures") ? Retextures.TOP : new Retextures(Jsons.getAsMap(object.getAsJsonObject("textures"), JsonElement::getAsString), null);
		}
		else if (json.isJsonArray())
		{
			variant = defaultVariant();
			variant.parts = Optional.of(loadParts(json.getAsJsonArray(), context));
		}
		else throw new JsonParseException("The variant json should be a object or array.");
		return variant;
	}
	
	private static List<INebulaModelPart> loadParts(JsonObject object, JsonDeserializationContext context) throws JsonParseException
	{
		if (object.has("part"))
		{
			JsonElement json1 = object.get("part");
			return ImmutableList.of(deserialize(json1, context));
		}
		else if (object.has("parts"))
			return loadParts(object.getAsJsonArray("parts"), context);
		else return null;
	}
	
	private static List<INebulaModelPart> loadParts(JsonArray array, JsonDeserializationContext context) throws JsonParseException
	{
		ImmutableList.Builder<INebulaModelPart> builder = ImmutableList.builder();
		for (JsonElement json1 : array)
		{
			builder.add(deserialize(json1, context));
		}
		return builder.build();
	}
	
	static class Retextures
	{
		static final Retextures TOP = new Retextures(ImmutableMap.of(), null)
		{
			@Override
			String get$(String key, LinkedList<String> list)
			{
				return key;
			}
		};
		
		@Nullable
		Retextures parent;
		@Nonnull
		Map<String, String> map;
		
		Retextures(Map<String, String> map, Retextures parent)
		{
			this.map = map;
			this.parent = parent;
		}
		
		String get(String key)
		{
			key = Strings.validate(key);
			return get$(key, new LinkedList<>());
		}
		
		String get$(String key, LinkedList<String> list)
		{
			if (key.length() == 0 || key.charAt(0) != '#') return key;
			if (list.contains(key))
				throw new RuntimeException("Resource location :" + key + ", looped loading.");
			key = key.substring(1);
			list.addLast(key);
			if (this.map.containsKey(key))
			{
				return get$(this.map.get(key), list);
			}
			else if (this.parent != null)
			{
				String key1 = this.parent.get$(key, list);
				return key != key1 ? get$(key1, list) : key1;
			}
			else return key;
		}
		
		Map<String, String> flatMap()
		{
			Map<String, String> map = new HashMap<>();
			flatMap$(map);
			return map;
		}
		
		void flatMap$(Map<String, String> map)
		{
			for (Entry<String, String> entry : this.map.entrySet())
			{
				if (!map.containsKey(entry.getKey()))
				{
					map.put(entry.getKey(), get(entry.getValue()));
				}
			}
			if (this.parent != null)
				this.parent.flatMap$(map);
		}
		
		boolean isEmpty()
		{
			return this.map.isEmpty() && (this.parent == null || this.parent.isEmpty());
		}
		
		@Override
		public boolean equals(Object obj)
		{
			return obj == this || (!(obj instanceof Retextures) ? false :
				this.map.equals(((Retextures) obj).map) && L.equal(this.parent, ((Retextures) obj).parent));
		}
	}
	
	static class Variant
	{
		Optional<Integer> x;
		Optional<Integer> y;
		Optional<Boolean> enable;
		Optional<List<INebulaModelPart>> parts;
		Retextures retextures;
		
		Variant()
		{
		}
		Variant(Variant base)
		{
			this.x = base.x;
			this.y = base.y;
			this.enable = base.enable;
			this.parts = base.parts;
			this.retextures = base.retextures;
		}
		
		void or(Variant base, Variant def)
		{
			this.x = base.x.or(def.x);
			this.y = base.y.or(def.y);
			this.enable = base.enable.or(def.enable);
			this.parts = base.parts.or(def.parts);
			this.retextures = new Retextures(base.retextures.map, def.retextures);
		}
		
		@Override
		public boolean equals(Object obj)
		{
			return obj == this || (
					!(obj instanceof Variant) ? false :
						((Variant) obj).x.equals(this.x) &&
						((Variant) obj).y.equals(this.y) &&
						((Variant) obj).enable.equals(this.enable) &&
						((Variant) obj).parts.equals(this.parts));
		}
	}
	
	Map<Map<String, String>, IntegerMap<Variant>> function;
	IntegerMap<Variant> def;
	boolean extendData;
	
	public ModelPartCol(Map<Map<String, String>, IntegerMap<Variant>> function, Variant def, boolean extendData)
	{
		this(function, new IntegerMap<>(), extendData);
		this.def.put(def, 1);
	}
	
	public ModelPartCol(Map<Map<String, String>, IntegerMap<Variant>> function, IntegerMap<Variant> def, boolean extendData)
	{
		this.function = function;
		this.def = def;
		this.extendData = extendData;
		
		retexLocations();
	}
	
	private void retexLocations()
	{
		List<Variant> set = new ArrayList<>(this.def.keySet());
		this.function.values().forEach(v->set.addAll(v.keySet()));
		
		for (Variant variant : set)
		{
			if (variant.parts.isPresent())
			{
				if (!variant.retextures.isEmpty())
				{
					ImmutableList.Builder<INebulaModelPart> parts = ImmutableList.builder();
					Map<String, String> retexture = variant.retextures.flatMap();
					for (INebulaModelPart part : variant.parts.get())
					{
						parts.add(part.retexture(retexture));
					}
					variant.retextures = Retextures.TOP;
					variant.parts = Optional.of(parts.build());
				}
			}
		}
	}
	
	private <E> Collection<E> collect(BiConsumer<INebulaModelPart, Collection<E>> consumer)
	{
		Set<E> set = L.collect(this.function.values(),
				(map, c)->c.addAll(L.collect(map, (e, c1)->e.getKey().parts.or(ImmutableList.of()).forEach(p->consumer.accept(p, c1)))));
		this.def.forEach(e->set.addAll(L.collect(e.getKey().parts.or(ImmutableList.of()), consumer)));
		return set;
	}
	
	@Override
	public Collection<ResourceLocation> getDependencies()
	{
		return collect((p, c)->c.addAll(p.getDependencies()));
	}
	
	@Override
	public Collection<String> getResources()
	{
		return collect((p, c)-> c.addAll(p.getResources()));
	}
	
	@Override
	public Collection<ResourceLocation> getDirectResources()
	{
		return collect((p, c)-> c.addAll(p.getDirectResources()));
	}
	
	@Override
	public INebulaBakedModelPart bake(VertexFormat format, Function<String, IIconCollection> iconHandlerGetter,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, TRSRTransformation transformation)
	{
		//Builds id->variant logic.
		List<Variant> list = new ArrayList<>();
		list.addAll(this.def.keySet());
		for (IntegerMap<Variant> key : this.function.values())
		{
			for (IntegerEntry<Variant> variant : key)
			{
				int idx = list.indexOf(variant.getKey());
				if (idx == -1)
				{
					idx = list.size();
					list.add(variant.getKey());
				}
			}
		}
		
		List<INebulaBakedModelPart>[] cachedParts = new List[list.size()];
		for (int i = 0; i < list.size(); cachedParts[i] =
				bakeVariant(list.get(i), format, iconHandlerGetter, bakedTextureGetter, transformation),
				++i);
		
		ImmutableMap.Builder<Map<String, String>, Selector<List<INebulaBakedModelPart>>> builder = ImmutableMap.builder();
		for (Entry<Map<String, String>, IntegerMap<Variant>> entry : this.function.entrySet())
		{
			builder.put(entry.getKey(), packBakedParts(entry.getValue(), list, cachedParts));
		}
		
		return new BakedModelPart(builder.build(), packBakedParts(this.def, list, cachedParts), this.extendData);
	}
	
	private Selector<List<INebulaBakedModelPart>> packBakedParts(IntegerMap<Variant> variants,
			List<Variant> idxList, List<INebulaBakedModelPart>[] cachedParts)
	{
		switch (variants.size())
		{
		case 0 : return NONE;
		case 1 :
			Variant variant = Iterables.getOnlyElement(variants).getKey();
			List<INebulaBakedModelPart> parts = cachedParts[idxList.indexOf(variant)];
			if (parts.size() == 1 && parts.get(0) instanceof ModelPartCol.BakedModelPart &&
					(((ModelPartCol.BakedModelPart) parts.get(0)).map.isEmpty() || !this.extendData))
			{
				return ((ModelPartCol.BakedModelPart) parts.get(0)).defaultPart;//Part extends.
			}
			return parts.size() == 0 ? NONE : Selector.single(parts);
		default:
			WeightedRandomSelector<List<INebulaBakedModelPart>> selector = new WeightedRandomSelector<>();
			for (IntegerEntry<Variant> e : variants)
			{
				selector.add(cachedParts[idxList.indexOf(e.getKey())], e.getValue());
			}
			return selector;
		}
	}
	
	private static List<INebulaBakedModelPart> bakeVariant(Variant variant, VertexFormat format, Function<String, IIconCollection> iconHandlerGetter,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, TRSRTransformation transformation)
	{
		if (variant.enable.or(true) && variant.parts.isPresent())
		{
			TRSRTransformation t = ModelRotation
					.getModelRotation(variant.x.or(0), variant.y.or(0))
					.apply(Optional.absent())
					.or(TRSRTransformation.identity());
			if (transformation != TRSRTransformation.identity())
			{
				Matrix4f matrix = t.getMatrix();
				matrix.mul(transformation.getMatrix(), matrix);
				t = new TRSRTransformation(matrix);
			}
			List<INebulaModelPart> l1 = variant.parts.get();
			ImmutableList.Builder<INebulaBakedModelPart> builder = ImmutableList.builder();
			for (INebulaModelPart part : l1)
				builder.add(part.bake(format, iconHandlerGetter, bakedTextureGetter, t));
			return builder.build();
		}
		else
		{
			return ImmutableList.of();//No part elements.
		}
	}
	
	@Override
	public INebulaModelPart retexture(Map<String, String> retexture)
	{
		return new ModelPartCol(ImmutableMap.copyOf(Maps.<Map<String, String>, IntegerMap<Variant>, IntegerMap<Variant>>transformValues(
				this.function, map->
				{
					IntegerMap<Variant> map1 = new IntegerMap<>(map.size(), 1.0F);
					map.forEach(e-> {
						Variant variant = new Variant(e.getKey());
						variant.retextures = new Retextures(retexture, variant.retextures);
						map1.put(variant, e.getValue());
					});
					return map;
				})), this.def, this.extendData);
	}
	
	private static class BakedModelPart implements INebulaBakedModelPart
	{
		final Map<Map<String, String>, Selector<List<INebulaBakedModelPart>>> map;
		final Selector<List<INebulaBakedModelPart>> defaultPart;
		final boolean extendData;
		
		BakedModelPart(Map<Map<String, String>, Selector<List<INebulaBakedModelPart>>> parts,
				Selector<List<INebulaBakedModelPart>> defaultPart, boolean extendData)
		{
			this.map = parts;
			this.defaultPart = defaultPart;
			this.extendData = extendData;
		}
		
		@Override
		public List<BakedQuad> getQuads(EnumFacing facing, String key, long rand)
		{
			Selector<List<INebulaBakedModelPart>> selector = this.map.get(parse(key));
			int random = (int) (rand ^ rand >> 32);
			return new ArrayList<>(L.collect(selector == null ? this.defaultPart.next(random) : selector.next(random),
					(p, c)-> c.addAll(p.getQuads(facing, this.extendData ? key : NebulaModelLoader.NORMAL))));
		}
		
		@Override
		public List<BakedQuad> getQuads(EnumFacing facing, String key)
		{
			return getQuads(facing, key, 0L);
		}
		
		private static Map<String, String> parse(String key)
		{
			String[] split = Strings.split(key, ',');
			Map<String, String> map = new HashMap<>();
			for (String v : split)
			{
				if (v.length() == 0) continue;
				String v1 = v.trim();
				int i = v1.indexOf('=');
				map.put(v1.substring(0, i), v1.substring(i + 1));
			}
			return map;
		}
	}
}