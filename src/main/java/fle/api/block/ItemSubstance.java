package fle.api.block;

import java.util.Map;

import farcore.block.ItemBlockBase;
import farcore.enums.EnumItem.IInfomationable;
import farcore.lib.collection.IRegister;
import farcore.lib.substance.SubstanceBlockAbstract;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemSubstance extends ItemBlockBase implements IInfomationable
{
	private IRegister<Block> register;
	
	public ItemSubstance(Block block)
	{
		super(block);
		hasSubtypes = true;
	}
	
	void initRegister()
	{
		this.register = ((BlockSubstance) block).register;
	}
	
	@Override
	public int getMetadata(int meta)
	{
		return meta;
	}
	
	public Block block(SubstanceBlockAbstract substance)
	{
		return register.get(substance.getName());
	}

	public ItemStack provide(SubstanceBlockAbstract substance)
	{
		return provide(substance, 1);
	}
	public ItemStack provide(SubstanceBlockAbstract substance, int size)
	{
		ItemStack stack = new ItemStack(block(substance), size);
		return stack;
	}

	@Override
	public ItemStack provide(int size, Object... objects)
	{
		if(objects.length == 1)
		{
			if(objects[0] instanceof String)
			{
				return new ItemStack(register.get((String) objects[0]), size);
			}
			else if(objects[0] instanceof SubstanceBlockAbstract)
			{
				return provide((SubstanceBlockAbstract) objects[0], size);
			}
		}
		return null;
	}
}