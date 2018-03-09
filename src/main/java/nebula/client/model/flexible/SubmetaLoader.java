/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.client.model.flexible;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import nebula.base.A;
import nebula.base.Cache;
import nebula.client.blockstate.BlockStateTileEntityWapper;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_CustomModelData;
import nebula.common.util.ItemStacks;
import nebula.common.util.Jsons;
import nebula.common.util.L;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public enum SubmetaLoader implements JsonDeserializer<Function<? extends Object, String>>
{
	BLOCK_LOADER
	{
		@Override
		public Function<IBlockState, String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			if (json.isJsonPrimitive()) return loadBlockMetaGenerator(json.getAsString());
			if (json.isJsonObject())
			{
				JsonObject object = json.getAsJsonObject();
				int marker = Jsons.getOrDefault(object, "marker", 0);
				String key;
				switch (marker)
				{
				case 0 :
				{
					key = object.get("key").getAsString();
					return new BlockSubmetaGetterFromState(key, A.allNonNull(Jsons.getArray(object.getAsJsonArray("formats"), -1, String.class, JsonElement::getAsString)), A.allNonNull(Jsons.getArray(object.getAsJsonArray("default"), -1, String.class, JsonElement::getAsString)));
				}
				case 1 :
				{
					key = object.get("key").getAsString();
					Function<IBlockState, String>[] funcs = object.has("formats") ? Jsons.getArray(object.getAsJsonArray("formats"), -1, Function.class, j -> deserialize(j, typeOfT, context)) : new Function[0];
					return new BlockSubmetaGetterCompose(key, funcs);
				}
				default:
					throw new JsonParseException("Unsupported marker yet, got: " + marker);
				}
			}
			else
				throw new JsonParseException("Unknown json type, got: " + json.getClass());
		}
	},
	ITEM_LOADER
	{
		@Override
		public Function<ItemStack, String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			if (json.isJsonPrimitive()) return loadItemMetaGenerator(json.getAsString());
			if (json.isJsonObject())
			{
				JsonObject object = json.getAsJsonObject();
				String key = object.get("key").getAsString();
				Function<ItemStack, String>[] funcs = object.has("formats") ? Jsons.getArray(object.getAsJsonArray("formats"), -1, Function.class, j -> deserialize(j, typeOfT, context)) : new Function[0];
				return new ItemSubmetaGetterCompose(key, funcs);
			}
			else
				throw new JsonParseException("Unknown json type, got: " + json.getClass());
		}
	};
	
	static final Map<String, Function<String, Function<ItemStack, String>>>		ITEM_META_GENERATOR_APPLIER		= new HashMap<>();
	static final Map<String, Function<String, Function<IBlockState, String>>>	BLOCK_META_GENERATOR_APPLIER	= new HashMap<>();
	static final Map<ResourceLocation, Function<ItemStack, String>>		ITEM_META_GENERATOR		= new HashMap<>();
	static final Map<ResourceLocation, Function<IBlockState, String>>	BLOCK_META_GENERATOR	= new HashMap<>();
	private static Map<String, Function<String, Function<ItemStack, String>>>	imgCache;
	private static Map<String, Function<String, Function<IBlockState, String>>>	bmgCache;
	
	static void onResourceReloadStart()
	{
		imgCache = new HashMap<>(ITEM_META_GENERATOR_APPLIER);
		bmgCache = new HashMap<>(BLOCK_META_GENERATOR_APPLIER);
	}
	
	static void onResourceReloadEnd()
	{
		imgCache.clear();
		imgCache = null;
		bmgCache.clear();
		bmgCache = null;
	}
	
	private static <T> Function<String, Function<T, String>> wrap(Function<String, Function<T, String>> function)
	{
		Map<String, Function<T, String>> map = new HashMap<>();
		return key -> map.computeIfAbsent(key, function);
	}
	
	private static Function<String, Function<ItemStack, String>> getItemMetaGeneratorApplier(String domain)
	{
		Function<String, Function<ItemStack, String>> function = imgCache.get(domain);
		if (function == null)
		{
			function = path -> ITEM_META_GENERATOR.get(new ResourceLocation(domain, path));
			imgCache.put(domain, function);
		}
		return function;
	}
	
	private static Function<String, Function<IBlockState, String>> getBlockMetaGeneratorApplier(String domain)
	{
		Function<String, Function<IBlockState, String>> function = bmgCache.get(domain);
		if (function == null)
		{
			function = path -> BLOCK_META_GENERATOR.get(new ResourceLocation(domain, path));
			bmgCache.put(domain, function);
		}
		return function;
	}
	
	static
	{
		BLOCK_META_GENERATOR_APPLIER.put("tile", SubmetaLoader.<IBlockState> wrap(path -> (state -> {
			TileEntity tile = BlockStateTileEntityWapper.unwrap(state);
			return tile instanceof ITP_CustomModelData ? ((ITP_CustomModelData) tile).getCustomModelData(path) : NebulaModelLoader.NORMAL;
		})));
		ITEM_META_GENERATOR_APPLIER.put("nbt", wrap(path -> {
			ItemSubmetaGetterNBT.Builder builder = ItemSubmetaGetterNBT.builder();
			StringTokenizer tokenizer = new StringTokenizer(path, "/\\");
			while (tokenizer.hasMoreTokens())
			{
				String token = tokenizer.nextToken();
				if (token.length() == 0)
				{
					NebulaModelLoader.INSTANCE.stream.println("Invalid nbt item meta generator. got: " + path);
					throw new IllegalArgumentException();
				}
				switch (token.charAt(0))
				{
				case '[':
					builder.appendAt(Short.parseShort(token.substring(1)));
					break;
				default :
					if (token.startsWith("\\["))
					{
						token = token.substring(2);//When first char in tag is '['.
					}
					builder.append(token);
					break;
				}
			}
			return builder.build();
		}));
	}
	
	@Nonnull
	static Function<ItemStack, String> loadItemMetaGenerator(String path)
	{
		ResourceLocation location = new ResourceLocation(path);
		Function<ItemStack, String> result;
		try
		{
			result = getItemMetaGeneratorApplier(location.getResourceDomain()).apply(location.getResourcePath());
			if (result == null)
			{
				result = (Function<ItemStack, String>) NebulaModelLoader.NORMAL_METAGENERATOR;
			}
		}
		catch (Exception exception)
		{
			result = (Function<ItemStack, String>) NebulaModelLoader.NORMAL_METAGENERATOR;
		}
		return result;
	}
	
	static Function<IBlockState, String> loadBlockMetaGenerator(String path)
	{
		ResourceLocation location = new ResourceLocation(path);
		Function<IBlockState, String> result;
		try
		{
			result = getBlockMetaGeneratorApplier(location.getResourceDomain()).apply(location.getResourcePath());
			if (result == null)
			{
				result = (Function<IBlockState, String>) NebulaModelLoader.NORMAL_METAGENERATOR;
			}
		}
		catch (Exception exception)
		{
			result = (Function<IBlockState, String>) NebulaModelLoader.NORMAL_METAGENERATOR;
		}
		return result;
	}
	
	@Override
	public Function<?, String> deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException
	{
		return null;
	}
	
	static abstract class BlockSubmetaGetter implements Function<IBlockState, String>
	{
		
	}
	
	static class BlockSubmetaGetterFromState extends BlockSubmetaGetter
	{
		String							key;
		Function<IBlockState, String>[]	formats;
		String[]						def;
		
		BlockSubmetaGetterFromState(String key, String[] formats, final String[] def)
		{
			if (formats.length != def.length) throw new IllegalArgumentException("The formats and default values length are not same.");
			this.key = key;
			this.formats = new Function[formats.length];
			for (int i = 0; i < formats.length; ++i)
			{
				if (formats[i].length() == 0) throw new JsonParseException("Unsupported format. got:''");
				if (formats[i].charAt(0) == '#')
					this.formats[i] = NebulaModelLoader.loadBlockMetaGenerator(formats[i].substring(1));
				else
				{
					final String str = formats[i];
					final Cache<IProperty<?>> property = new Cache<>();
					this.formats[i] = state -> {
						property.setIfAbsent(() -> state.getBlock().getBlockState().getProperty(str));
						IProperty<?> p = property.get();
						return p == null ? "missing" : p.getName(L.castAny(state.getValue(p)));
					};
				}
			}
			this.def = def;
		}
		
		@Override
		public String apply(IBlockState state)
		{
			return state == null ? String.format(this.key, (Object[]) this.def) : String.format(this.key, A.transform(this.formats, L.funtional(state)));
		}
	}
	
	private static class BlockSubmetaGetterCompose extends BlockSubmetaGetter
	{
		String							key;
		Function<IBlockState, String>[]	formats;
		
		BlockSubmetaGetterCompose(String key, Function<IBlockState, String>[] formats)
		{
			this.key = key;
			this.formats = formats;
		}
		
		@Override
		public String apply(IBlockState state)
		{
			return String.format(this.key, A.transform(this.formats, L.funtional(state)));
		}
	}
	
	static abstract class ItemSubmetaGetter implements Function<ItemStack, String>
	{
	}
	
	static class ItemSubmetaGetterNBT extends ItemSubmetaGetter
	{
		static Builder builder()
		{
			return new Builder();
		}
		
		static class Builder
		{
			private LinkedList<Function<NBTBase, NBTBase>> list = new LinkedList<>();
			
			void append(String key)
			{
				this.list.add(L.withCastIn(L.toFunction(NBTTagCompound::getTag, key)));
			}
			
			void appendAt(int id)
			{
				this.list.add(L.withCastIn(L.toFunction(NBTTagList::get, id)));
			}
			
			private Function<NBTBase, NBTBase> transform()
			{
				Function<NBTBase, NBTBase> func = this.list.removeFirst();
				while (!this.list.isEmpty())
				{
					func = func.andThen(this.list.removeFirst());
				}
				return func;
			}
			
			ItemSubmetaGetterNBT build()
			{
				ItemSubmetaGetterNBT result = new ItemSubmetaGetterNBT();
				result.formats = transform();
				return result;
			}
		}
		
		Function<NBTBase, NBTBase> formats;
		
		@Override
		public String apply(ItemStack t)
		{
			NBTBase nbt = ItemStacks.getOrSetupNBT(t, false);
			try
			{
				nbt = this.formats.apply(nbt);
			}
			catch (ClassCastException | NullPointerException exception)
			{
				return "<error>";
			}
			if (nbt instanceof NBTTagDouble)
			{
				return Double.toString(((NBTTagDouble) nbt).getDouble());
			}
			else if (nbt instanceof NBTTagFloat)
			{
				return Float.toString(((NBTTagDouble) nbt).getFloat());
			}
			else if (nbt instanceof NBTTagLong)
			{
				return Long.toString(((NBTTagLong) nbt).getLong());
			}
			else if (nbt instanceof NBTPrimitive)
			{
				return Integer.toString(((NBTTagDouble) nbt).getInt());
			}
			else if (nbt instanceof NBTTagString)
			{
				return ((NBTTagString) nbt).getString();
			}
			else
			{
				return "<error>";
			}
		}
	}
	
	private static class ItemSubmetaGetterCompose extends ItemSubmetaGetter
	{
		String							key;
		Function<ItemStack, String>[]	formats;
		
		ItemSubmetaGetterCompose(String key, Function<ItemStack, String>[] formats)
		{
			this.key = key;
			this.formats = formats;
		}
		
		@Override
		public String apply(ItemStack stack)
		{
			return String.format(this.key, A.transform(this.formats, L.funtional(stack)));
		}
	}
}
