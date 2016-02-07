package flapi.block.item;

import farcore.block.item.ItemBlockBase;
import flapi.block.BlockSub;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockSub<T extends BlockSub> extends ItemBlockBase<T>
{
	public ItemBlockSub(Block block)
	{
		super(block);
	}
}