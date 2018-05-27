/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.oredict;

import java.util.List;

import nebula.common.stack.AbstractStack;
import nebula.common.stack.OreStack;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class OreStackExt implements AbstractStack
{
	private final String	name;
	private final int		size;
	
	public OreStackExt(String ore)
	{
		this(ore, 1);
	}
	
	public OreStackExt(String ore, int size)
	{
		this.name = ore;
		this.size = size;
	}
	
	public OreStackExt(OreStack stack)
	{
		this.name = stack.oreName;
		this.size = stack.size;
	}
	
	@Override
	public boolean similar(ItemStack stack)
	{
		return stack != null && OreDictExt.oreMatchs(stack, this.name);
	}
	
	@Override
	public List<ItemStack> display()
	{
		return OreDictExt.getOres(this.name);
	}
	
	@Override
	public boolean valid()
	{
		return !OreDictExt.getOres(this.name, false).isEmpty();
	}
	
	@Override
	public AbstractStack copyWithSize(int size)
	{
		return new OreStackExt(this.name, size);
	}
	
	@Override
	public int size(ItemStack stack)
	{
		return this.size;
	}
	
	@Override
	public String toString()
	{
		return "[ore_ext:" + this.name + "]x" + this.size;
	}
}
