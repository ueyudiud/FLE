package fle.core.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import flapi.block.old.ItemFleBlock;
import fle.core.te.TileEntityPlacedItem;

public class ItemPlaceable extends ItemFleBlock
{
	public ItemPlaceable(Block aBlock)
	{
		super(aBlock);
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ, int metadata)
	{
		if (!world.setBlock(x, y, z, block, metadata, 3))
	    {
			return false;
		}
		
		if(world.getTileEntity(x, y, z) instanceof TileEntityPlacedItem)
		{
			((TileEntityPlacedItem) world.getTileEntity(x, y, z)).init(stack.copy());
			stack.stackSize = 0;
		}
		else
		{
			return false;
		}

		if (world.getBlock(x, y, z) == block)
		{
			block.onPostBlockPlaced(world, x, y, z, metadata);
		}
		return true;
	}
}