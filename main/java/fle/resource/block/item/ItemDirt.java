package fle.resource.block.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import farcore.block.item.ItemBlockBase;
import fle.resource.block.BlockDirt;

public class ItemDirt extends ItemBlockBase<BlockDirt>
{
	public ItemDirt(Block block)
	{
		super(block);
		hasSubtypes = true;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) + "."
				+ this.block.getDirtName(stack).replace(' ', '.');
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return translateToLocal(
				block.getState(stack).getUnlocalized() + ".name",
				translateToLocal(block.getDirt(stack).getTranslateName(),
						new Object[0]));
	}
}