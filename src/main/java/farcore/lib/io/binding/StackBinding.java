/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.io.binding;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.script.Bindings;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import farcore.lib.collection.Ety;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class StackBinding implements Bindings
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