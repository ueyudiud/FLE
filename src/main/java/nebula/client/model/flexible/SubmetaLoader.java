/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model.flexible;

import java.lang.reflect.Type;
import java.util.function.Function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import nebula.common.util.A;
import nebula.common.util.Jsons;
import nebula.common.util.L;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public enum SubmetaLoader implements JsonDeserializer<Function<? extends Object, String>>
{
	BLOCK_LOADER {
		@Override
		public Function<IBlockState, String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException
		{
			if (json.isJsonPrimitive())
				return (Function<IBlockState, String>) NebulaModelLoader.NORMAL_METAGENERATOR;//TODO
			if (json.isJsonObject())
			{
				JsonObject object = json.getAsJsonObject();
				Block block = Block.getBlockFromName(object.get("source").getAsString());
				if (block == null)
					throw new JsonParseException("No source of json found, got: " + object.get("source"));
				BlockStateContainer container = block.getBlockState();
				String key = object.get("key").getAsString();
				IProperty<?>[] formats = A.allNonNull(Jsons.getArray(object.getAsJsonArray("formats"), -1, L.castAny(IProperty.class),
						j-> L.castAny(container.getProperty(j.getAsString()))));
				return new BlockSubmetaGetter(key, formats);
			}
			else throw new JsonParseException("Unknown json type, got: " + json.getClass());
		}
	},
	ITEM_LOADER {
		@Override
		public Function<ItemStack, String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException
		{
			if (json.isJsonPrimitive())
				return NebulaModelLoader.loadItemMetaGenerator(json.getAsString());
			if (json.isJsonObject())
			{
				JsonObject object = json.getAsJsonObject();
				String key = object.get("key").getAsString();
				Function<ItemStack, String>[] funcs = object.has("formats") ? Jsons.getArray(object.getAsJsonArray("formats"), -1, Function.class, j->deserialize(j, typeOfT, context)) : new Function[0];
				return new ItemSubmetaGetterCompose(key, funcs);
			}
			else throw new JsonParseException("Unknown json type, got: " + json.getClass());
		}
	};
	
	@Override
	public Function<?, String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException
	{
		return null;
	}
	
	static class BlockSubmetaGetter implements Function<IBlockState, String>
	{
		String key;
		IProperty<?>[] formats;
		
		BlockSubmetaGetter(String key, IProperty<?>[] formats)
		{
			this.key = key;
			this.formats = formats;
		}
		
		@Override
		public String apply(IBlockState state)
		{
			return String.format(this.key, A.transform(this.formats,
					p->p.getName(L.castAny(state.getValue(p)))));
		}
	}
	
	static abstract class ItemSubmetaGetter implements Function<ItemStack, String> {}
	
	private static class ItemSubmetaGetterCompose extends ItemSubmetaGetter
	{
		String key;
		Function<ItemStack, String>[] formats;
		
		ItemSubmetaGetterCompose(String key, Function<ItemStack, String>[] formats)
		{
			this.key = key;
			this.formats = formats;
		}
		
		@Override
		public String apply(ItemStack t)
		{
			return String.format(this.key, A.transform(this.formats, f->f.apply(t)));
		}
	}
}