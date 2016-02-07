package fle.resource.block.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import farcore.block.item.ItemBlockBase;
import fle.resource.block.BlockStoneChip;

public class ItemStoneChip extends ItemBlockBase<BlockStoneChip>
{
	public ItemStoneChip(Block block)
	{
		super(block);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName(stack) + "."
				+ this.block.getRockName(stack).replace(' ', '.') + "."
				+ this.block.getRockSize(stack).name();
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return translateToLocal(
				this.block.getRockSize(stack).getUnlocalized() + ".name",
				translateToLocal(this.block.getRock(stack).getTranslateName(),
						new Object[0]));
	}
}