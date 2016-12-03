/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.model.item;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import farcore.lib.collection.Ety;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * @since 1.4
 * @author ueyudiud
 */
public class FarCoreJSSubmetaProvider<T extends ScriptEngine & Invocable> implements Function<ItemStack, String>
{
	private static final ScriptEngineManager MANAGER;
	
	static
	{
		MANAGER = new ScriptEngineManager();
	}
	
	private final T engine;
	
	public FarCoreJSSubmetaProvider(byte[] values) throws ScriptException
	{
		this.engine = (T) MANAGER.getEngineByName("javascript");
		this.engine.eval(new String(values));
		try
		{
			this.engine.invokeFunction("apply", new Object[1]);
		}
		catch (NoSuchMethodException exception)
		{
			//Throw exception when method not detect.
			throw new ScriptException(exception);
		}
		catch (ScriptException exception)
		{
			//Ignore exception of error occurs of method in engine.
		}
	}
	
	@Override
	public String apply(ItemStack target)
	{
		try
		{
			return (String) this.engine.invokeFunction("apply", withBinding(target));
		}
		catch (ScriptException exception)
		{
			return FarCoreItemModelLoader.NORMAL;
		}
		catch (NoSuchMethodException exception)
		{
			return FarCoreItemModelLoader.NORMAL;
		}
	}
	
	private Bindings withBinding(ItemStack stack)
	{
		return new StackBinding(stack);
	}
	
	private static class StackBinding implements Bindings
	{
		private static final Set<String> KEY_SET = ImmutableSet.of("item", "size", "meta", "nbt");
		
		ItemStack stack;
		private Bindings nbtBinding;
		private Collection<Object> values;
		private Set<Entry<String, Object>> entrySet;
		
		public StackBinding(ItemStack stack)
		{
			this.stack = stack;
		}
		
		@Override
		public int size()
		{
			return 4;
		}
		
		@Override
		public boolean isEmpty()
		{
			return false;
		}
		
		@Override
		public boolean containsValue(Object value)
		{
			return values().contains(value);
		}
		
		@Override
		public void clear()
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public Set<String> keySet()
		{
			return KEY_SET;
		}
		
		@Override
		public Collection<Object> values()
		{
			return this.values != null ? this.values :
				(this.values = ImmutableList.of(
						this.stack.getItem().getRegistryName().toString(),
						this.stack.stackSize,
						this.stack.getItemDamage(),
						getNBTBinding()));
		}
		
		@Override
		public Set<java.util.Map.Entry<String, Object>> entrySet()
		{
			return this.entrySet != null ? this.entrySet :
				(this.entrySet = ImmutableSet.of(
						new Ety("item", this.stack.getItem().getRegistryName().toString()),
						new Ety("size", this.stack.stackSize),
						new Ety("meta", this.stack.getItemDamage()),
						new Ety("nbt", getNBTBinding())));
		}
		
		@Override
		public Object put(String name, Object value)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public void putAll(Map<? extends String, ? extends Object> toMerge)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean containsKey(Object key)
		{
			return KEY_SET.contains(key);
		}
		
		@Override
		public Object get(Object key)
		{
			if(!(key instanceof String)) return false;
			switch ((String) key)
			{
			case "item" : return this.stack.getItem().getRegistryName().toString();
			case "size" : return this.stack.stackSize;
			case "meta" : return this.stack.getItemDamage();
			case "nbt" : return getNBTBinding();
			default : return null;
			}
		}
		
		@Override
		public Object remove(Object key)
		{
			throw new UnsupportedOperationException();
		}
		
		private Object getNBTBinding()
		{
			return this.stack.hasTagCompound() ? (this.nbtBinding == null ? this.nbtBinding = new NBTBinding(this.stack.getTagCompound()) : this.nbtBinding) : null;
		}
	}
	
	private static class NBTBinding implements Bindings
	{
		NBTTagCompound nbt;
		private Collection<Object> values;
		private Set<Entry<String, Object>> entrySet;
		
		NBTBinding(NBTTagCompound nbt)
		{
			this.nbt = nbt;
		}
		
		@Override
		public int size()
		{
			return this.nbt.getSize();
		}
		
		@Override
		public boolean isEmpty()
		{
			return this.nbt.hasNoTags();
		}
		
		@Override
		//Raw method.
		public boolean containsValue(Object value)
		{
			return false;
		}
		
		@Override
		public void clear()
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public Set<String> keySet()
		{
			return this.nbt.getKeySet();
		}
		
		@Override
		public Collection<Object> values()
		{
			return this.values != null ? this.values :
				(this.values = Collections2.transform(this.nbt.getKeySet(), key -> decode(this.nbt.getTag(key))));
		}
		
		private Object decode(NBTBase tag)
		{
			return tag == null ? null : decode(tag, tag.getId());
		}
		
		private Object decode(NBTBase tag, byte id)
		{
			switch (id)
			{
			case NBT.TAG_BYTE :
			case NBT.TAG_SHORT :
			case NBT.TAG_INT :
				return ((NBTPrimitive) tag).getInt();
			case NBT.TAG_LONG :
				return ((NBTPrimitive) tag).getLong();
			case NBT.TAG_FLOAT :
				return ((NBTPrimitive) tag).getFloat();
			case NBT.TAG_DOUBLE :
				return ((NBTPrimitive) tag).getDouble();
			case NBT.TAG_STRING :
				return ((NBTTagString) tag).getString();
			case NBT.TAG_COMPOUND :
				return new NBTBinding((NBTTagCompound) tag);
			case NBT.TAG_INT_ARRAY :
				return ((NBTTagIntArray) tag).getIntArray();
			case NBT.TAG_BYTE_ARRAY :
				return ((NBTTagByteArray) tag).getByteArray();
			case NBT.TAG_LIST :
				return toObjectArray((NBTTagList) tag);
			default : return null;
			}
		}
		
		private Object[] toObjectArray(NBTTagList list)
		{
			byte id = (byte) list.getTagType();
			if(id == 0) return new Object[0];
			Object[] result = new Object[list.tagCount()];
			for(int i = 0; i < result.length; ++i)
			{
				result[i] = decode(list.get(i), id);
			}
			return result;
		}
		
		@Override
		public Set<java.util.Map.Entry<String, Object>> entrySet()
		{
			return this.entrySet != null ? this.entrySet :
				(this.entrySet = ImmutableSet.copyOf(Collections2.transform(this.nbt.getKeySet(), key -> new Ety(key, decode(this.nbt.getTag(key))))));
		}
		
		@Override
		public Object put(String name, Object value)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public void putAll(Map<? extends String, ? extends Object> toMerge)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean containsKey(Object key)
		{
			return key instanceof String ? this.nbt.hasKey((String) key) : false;
		}
		
		@Override
		public Object get(Object key)
		{
			return !(key instanceof String) ? null : decode(this.nbt.getTag((String) key));
		}
		
		@Override
		public Object remove(Object key)
		{
			throw new UnsupportedOperationException();
		}
	}
}