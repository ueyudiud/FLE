package fle.api.block;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemFleBlock extends ItemBlock
{
	protected BlockFle block;

	public ItemFleBlock(Block aBlock)
	{
		super(aBlock);
		if(aBlock instanceof BlockFle)
		{
			block = (BlockFle) aBlock;
		}
	}

	@Override
	public ItemFleBlock setMaxStackSize(int aSize)
	{
		block.setMaxStackSize(maxStackSize = aSize);
		return this;
	}
}