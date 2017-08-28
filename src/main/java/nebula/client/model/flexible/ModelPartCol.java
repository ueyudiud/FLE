/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model.flexible;

import static nebula.client.model.flexible.NebulaModelDeserializer.deserialize;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import nebula.base.INode;
import nebula.base.IntegerMap;
import nebula.base.Node;
import nebula.client.util.IIconCollection;
import nebula.common.util.A;
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
public class ModelPartCol implements INebulaDirectResourcesModelPart, IRetexturableNebulaModelPart
{
	static final JsonDeserializer<ModelPartCol> LOADER = (json, typeOfT, context)-> {
		JsonObject object = json.getAsJsonObject();
		if (object.has("variants"))
		{
			Variant def = object.has("default") ? loadVariant(object.get("default"), context) : defaultVariant();
			Map<String, Map<String, Variant>> map =
					Jsons.getAsMap(object.getAsJsonObject("variants"),
							j->Jsons.getAsMap(j.getAsJsonObject(), j1->loadVariant(j1, context)));
			return new ModelPartCol(compose(map, def), def);
		}
		else
		{
			return new ModelPartCol(ImmutableMap.of(), loadVariant(object.get("variant"), context));
		}
	};
	
	private static Map<Map<String, String>, Variant> compose(Map<String, Map<String, Variant>> map, Variant def)
	{
		INode<Entry<String, Map<String, Variant>>> node = Node.chain(map.entrySet());
		Map<Map<String, String>, Variant> states = new HashMap<>();
		put(node, ImmutableMap.of(), def, states);
		return ImmutableMap.copyOf(states);
	}
	
	private static void put(INode<Entry<String, Map<String, Variant>>> node, Map<String, String> base,
			Variant variant, Map<Map<String, String>, Variant> states)
	{
		if (!node.hasNext())
		{
			states.put(ImmutableMap.copyOf(base), variant);
			return;
		}
		else
		{
			INode<Entry<String, Map<String, Variant>>> next = node.next();
			String key = node.value().getKey();
			Map<String, String> base1 = new HashMap<>(base);
			for (Entry<String, Variant> e : node.value().getValue().entrySet())
			{
				base1.put(key, e.getKey());
				Variant variant2 = new Variant();
				variant2.or(e.getValue(), variant);
				put(next, base1, variant2, states);
			}
		}
	}
	
	private static Variant defaultVariant()
	{
		Variant variant = new Variant();
		variant.enable = Optional.absent();
		variant.parts = Optional.absent();
		variant.x = variant.y = Optional.absent();
		return variant;
	}
	
	private static Variant loadVariant(JsonElement json, JsonDeserializationContext context) throws JsonParseException
	{
		if (!json.isJsonObject())
			throw new JsonParseException("The variant json should be a object.");
		JsonObject object = json.getAsJsonObject();
		Variant variant = new Variant();
		variant.x = !object.has("x") ? Optional.absent() : Optional.of(object.get("x").getAsInt());
		variant.y = !object.has("y") ? Optional.absent() : Optional.of(object.get("y").getAsInt());
		variant.enable = !object.has("enable") ? Optional.absent() : Optional.of(object.get("enable").getAsBoolean());
		variant.parts = object.has("parts") ? Optional.of(Jsons.<INebulaModelPart>getAsList(object.get("parts").getAsJsonArray(), j->
		deserialize(j, context))) : object.has("part") ? Optional.of(ImmutableList.of(deserialize(object.get("part"), context))) : Optional.absent();
		return variant;
	}
	
	static class Variant
	{
		Optional<Integer> x;
		Optional<Integer> y;
		Optional<Boolean> enable;
		Optional<List<INebulaModelPart>> parts;
		
		void or(Variant base, Variant def)
		{
			this.x = base.x.or(def.x);
			this.y = base.y.or(def.y);
			this.enable = base.enable.or(def.enable);
			this.parts = base.parts.or(def.parts);
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
	
	Map<Map<String, String>, Variant> function;
	Variant def;
	
	public ModelPartCol(Map<Map<String, String>, Variant> function, Variant def)
	{
		this.function = function;
		this.def = def;
	}
	
	@Override
	public Collection<ResourceLocation> getDependencies()
	{
		Set<ResourceLocation> set = L.collect(this.function.values(),
				(l, c)->c.addAll(L.collect(l.parts.get(), (p, c1)->c1.addAll(p.getDependencies()))));
		set.addAll(L.collect(this.def.parts.or(ImmutableList.of()), (p, c)->c.addAll(p.getDependencies())));
		return set;
	}
	
	@Override
	public Collection<String> getResources()
	{
		Set<String> set = L.collect(this.function.values(),
				(l, c)->c.addAll(L.collect(l.parts.get(), (p, c1)->c1.addAll(p.getResources()))));
		set.addAll(L.collect(this.def.parts.or(ImmutableList.of()), (p, c)->c.addAll(p.getResources())));
		return set;
	}
	
	@Override
	public INebulaBakedModelPart bake(VertexFormat format, Function<String, IIconCollection> iconHandlerGetter,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, TRSRTransformation transformation)
	{
		IntegerMap<Map<String, String>> map = new IntegerMap<>(this.function.size(), 1.0F);
		List<Variant> list = new ArrayList<>();
		for (Entry<Map<String, String>, Variant> entry : this.function.entrySet())
		{
			int idx = list.indexOf(entry.getValue());
			if (idx == -1)
			{
				idx = list.size();
				list.add(entry.getValue());
			}
		}
		
		List<INebulaBakedModelPart>[] parts = new List[list.size() + 1];
		for (int i = 0; i < parts.length; ++i)
		{
			Variant state = i < list.size() ? list.get(i) : this.def;
			if (state.enable.or(true))
			{
				TRSRTransformation t = ModelRotation
						.getModelRotation(state.x.or(0), state.y.or(0))
						.apply(Optional.absent())
						.or(TRSRTransformation.identity());
				if (transformation != TRSRTransformation.identity())
				{
					Matrix4f matrix = t.getMatrix();
					matrix.mul(transformation.getMatrix(), matrix);
					t = new TRSRTransformation(matrix);
				}
				final TRSRTransformation t1 = t;
				parts[i] = ImmutableList.copyOf(
						Lists.transform(state.parts.or(ImmutableList.of()),
								p->p.bake(format, iconHandlerGetter, bakedTextureGetter, t1)));
			}
			else
			{
				parts[i] = ImmutableList.of();//No part elements.
			}
		}
		return new BakedModelPart(A.copyToLength(parts, list.size()), parts[list.size()], map);
	}
	
	@Override
	public INebulaModelPart retexture(Map<String, String> retexture)
	{
		return new ModelPartCol(ImmutableMap.copyOf(Maps.<Map<String, String>, Variant, Variant>transformValues(
				this.function, v->
				{
					Variant v1 = new Variant();
					v1.x = v.x; v1.y = v.y; v1.enable = v.enable;
					v1.parts = Optional.of(Lists.transform(v.parts.get(), p->
					p instanceof IRetexturableNebulaModelPart ?
							((IRetexturableNebulaModelPart) p).retexture(retexture) : p));
					return v1;
				})), this.def);
	}
	
	@Override
	public Collection<ResourceLocation> getDirectResources()
	{
		return L.collect(this.function.values(),
				(l, c)->c.addAll(L.collect(l.parts.get(), (p, c1)-> {
					if (p instanceof INebulaDirectResourcesModelPart)
						c1.addAll(((INebulaDirectResourcesModelPart) p).getDirectResources());
				})));
	}
	
	private static class BakedModelPart implements INebulaBakedModelPart
	{
		final IntegerMap<Map<String, String>> map;
		final List<INebulaBakedModelPart>[] parts;
		@Nullable
		final List<INebulaBakedModelPart> defaultPart;
		
		BakedModelPart(List<INebulaBakedModelPart>[] parts, List<INebulaBakedModelPart> defaultPart, IntegerMap<Map<String, String>> map)
		{
			this.parts = parts;
			this.defaultPart = defaultPart;
			this.map = map;
		}
		
		@Override
		public List<BakedQuad> getQuads(EnumFacing facing, String key)
		{
			int id = this.map.getOrDefault(parse(key), -1);
			return new ArrayList<>(L.collect(id == -1 ? this.defaultPart : this.parts[id], (p, c)-> c.addAll(p.getQuads(facing, key))));
		}
		
		private static Map<String, String> parse(String key)
		{
			String[] split = Strings.split(key, ',');
			Map<String, String> map = new HashMap<>();
			for (String v : split)
			{
				String v1 = v.trim();
				int i = v1.indexOf('=');
				map.put(v1.substring(0, i), v1.substring(i + 1));
			}
			return map;
		}
	}
}