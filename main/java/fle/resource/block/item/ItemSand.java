package fle.resource.block.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import farcore.block.item.ItemBlockBase;
import fle.resource.block.BlockSand;

public class ItemSand extends ItemBlockBase<BlockSand>
{
	public ItemSand(Block block)
	{
		super(block);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) + "." + this.block.getSandName(stack).replace(' ', '.');
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return translateToLocal(this.block.getSand(stack).getTranslateName(), new Object[0]);
	}
}