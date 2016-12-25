/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.model.item;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;

import farcore.lib.io.binding.StackBinding;
import farcore.lib.io.javascript.IScriptHandler;
import farcore.lib.io.javascript.IScriptObjectEncoder;
import farcore.lib.io.javascript.ScriptBuilder;
import farcore.lib.io.javascript.ScriptHandler;
import farcore.util.IO;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 * @since 1.5
 */
@SideOnly(Side.CLIENT)
public class FarCoreItemSubmetaGetterLoader
{
	private static final Map<ResourceLocation, SubmetaGetter> BUILTIN_SUBMETA_GETTER = new HashMap();
	
	private static final Map<ResourceLocation, SubmetaGetter> STORED_FUNCTION = new HashMap();
	
	static final JsonDeserializer<SubmetaGetter> DESERIALIZER = (json, typeOfT, context) ->
	{
		if(json.isJsonObject())
		{
			JsonObject object = json.getAsJsonObject();
			SubmetaGetter getter = context.deserialize(object.get("parent"), SubmetaGetter.class);
			if(object.has("prefix"))
			{
				final SubmetaGetter getter1 = getter;
				SubmetaGetter prefix = context.deserialize(object.get("prefix"), SubmetaGetter.class);
				getter = stack -> prefix.apply(stack) + "." + getter1.apply(stack);
			}
			if(object.has("postfix"))
			{
				final SubmetaGetter getter1 = getter;
				SubmetaGetter postfix = context.deserialize(object.get("postfix"), SubmetaGetter.class);
				getter = stack -> getter1.apply(stack)  + postfix.apply(stack);
			}
			return getter;
		}
		else
		{
			String key = json.getAsString();
			switch (key.charAt(0))
			{
			case '#' :
				return getSubmetaGetter(new ResourceLocation(key.substring(1)));
			default :
				return stack -> key;
			}
		}
	};
	
	static void cleanCache()
	{
		STORED_FUNCTION.clear();
	}
	
	public static void registerSubmetaGetter(ResourceLocation location, SubmetaGetter submetaGetter)
	{
		BUILTIN_SUBMETA_GETTER.put(location, submetaGetter);
	}
	
	public static SubmetaGetter getSubmetaGetter(ResourceLocation location)
	{
		if(BUILTIN_SUBMETA_GETTER.containsKey(location))
		{
			return BUILTIN_SUBMETA_GETTER.get(location);
		}
		return getSubmetaGetterFromResource(location);
	}
	
	public static SubmetaGetter getSubmetaGetterFromResource(ResourceLocation location)
	{
		if(STORED_FUNCTION.containsKey(location))
		{
			return STORED_FUNCTION.get(location);
		}
		if(FarCoreItemModelLoader.getResourceManaer() != null)
		{
			ResourceLocation location2 = new ResourceLocation(location.getResourceDomain(), "models/mg/" + location.getResourcePath() + ".js");
			try
			{
				SubmetaGetter getter = new ScriptSubmetaGetter(IO.copyResource(FarCoreItemModelLoader.getResourceManaer(), location));
				STORED_FUNCTION.put(location, getter);
				return getter;
			}
			catch (Exception exception)
			{
				return FarCoreItemModelLoader.NORMAL_FUNCTION;
			}
		}
		throw new IllegalStateException("The resource manager is not loading resources!");
	}
	
	@FunctionalInterface
	public static interface SubmetaGetter extends Function<ItemStack, String>
	{
		
	}
	
	public static class ScriptSubmetaGetter implements SubmetaGetter
	{
		private static final ScriptEngineManager MANAGER = new ScriptEngineManager();
		
		private static final IScriptObjectEncoder<ItemStack> STACK_ENCODER = new IScriptObjectEncoder<ItemStack>()
		{
			@Override
			public boolean access(Type type)
			{
				return type == ItemStack.class;
			}
			
			@Override
			public Object apply(ItemStack target, IScriptHandler handler) throws ScriptException
			{
				return new StackBinding(target);
			}
		};
		
		private static final Map<String, Object> GLOBLE_VALUES = new HashMap();
		
		private final ScriptHandler handler;
		
		public ScriptSubmetaGetter(byte[] values) throws ScriptException
		{
			this.handler = new ScriptBuilder(values, MANAGER.getEngineByName("javascript"))
					.registerCoder(STACK_ENCODER)
					.putGlobleValues(GLOBLE_VALUES)
					.build();
			this.handler.test("apply", 1);
		}
		
		@Override
		public String apply(ItemStack target)
		{
			try
			{
				return this.handler.invoke(String.class, "apply", target);
			}
			catch (ScriptException | NoSuchMethodException exception)
			{
				return FarCoreItemModelLoader.NORMAL;
			}
		}
	}
}