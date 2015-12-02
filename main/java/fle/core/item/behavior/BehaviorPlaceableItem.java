package fle.core.item.behavior;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.FleValue;
import fle.api.block.IDebugableBlock;
import fle.api.item.ItemFleMetaBase;
import fle.core.block.BlockPlacedItem;
import fle.core.init.IB;
import fle.core.te.TileEntityPlacedItem;

public class BehaviorPlaceableItem extends BehaviorBlockable implements IDebugableBlock
{
	public BehaviorPlaceableItem()
	{
		super(IB.placedItem, 0);
	}
	
	@Override
	public boolean onItemUse(ItemFleMetaBase item, ItemStack itemstack,
			EntityPlayer player, World world, int x, int y, int z,
			ForgeDirection side, float xPos, float yPos, float zPos) 
	{
		if(itemstack.stackSize <= 64)
		{
			Block block = world.getBlock(x, y, z);
			if (block != Blocks.air)
			{
	            x += side.offsetX;
	            y += side.offsetY;
	            z += side.offsetZ;
			}
			return BlockPlacedItem.putPlacedItem(itemstack, player, world, x, y, z, xPos, yPos, zPos);
		}
		return false;
	}

	@Override
	public void addInfomationToList(World aWorld, int x, int y, int z,
			List aList)
	{
		
	}
}