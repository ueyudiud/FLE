package fle.core.item.behavior;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import flapi.cover.Cover;
import flapi.item.ItemFleMetaBase;
import fle.api.te.ICoverTE;

public class BehaviorCoverable extends BehaviorBase
{
	final Cover cover;
	
	public BehaviorCoverable(Cover aCover)
	{
		cover = aCover;
	}
	
	@Override
	public boolean onItemUse(ItemFleMetaBase item, ItemStack itemstack,
			EntityPlayer player, World world, int x, int y, int z,
			ForgeDirection side, float xPos, float yPos, float zPos)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof ICoverTE)
		{
			ICoverTE te = (ICoverTE) tile;
			if(te.addCover(side, cover))
			{
				--itemstack.stackSize;
				return true;
			}
		}
		return super.onItemUse(item, itemstack, player, world, x, y, z, side, xPos,
				yPos, zPos);
	}
}