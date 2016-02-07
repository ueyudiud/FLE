package farcore.fluid;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemFluid extends ItemBlock
{
	public ItemFluid(Block block)
	{
		super(block);
	}
	
	@Override
	public int getMetadata(int meta)
	{
		return meta;
	}
}