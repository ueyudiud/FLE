/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.model.item.unused;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import farcore.lib.io.binding.StackBinding;
import farcore.lib.io.javascript.IScriptHandler;
import farcore.lib.io.javascript.IScriptObjectEncoder;
import farcore.lib.io.javascript.ScriptBuilder;
import farcore.lib.io.javascript.ScriptHandler;
import net.minecraft.item.ItemStack;

/**
 * @since 1.4
 * @author ueyudiud
 */
public class FarCoreJSSubmetaProvider implements Function<ItemStack, String>
{
	private static final ScriptEngineManager MANAGER;
	private static final IScriptObjectEncoder<ItemStack> STACK_ENCODER =
			new IScriptObjectEncoder<ItemStack>()
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
	
	static
	{
		MANAGER = new ScriptEngineManager();
	}
	
	private final ScriptHandler handler;
	
	public FarCoreJSSubmetaProvider(byte[] values) throws ScriptException
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