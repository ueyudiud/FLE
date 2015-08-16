package fle.core.item.behavior;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.item.ItemFleMetaBase;

public class BehaviorCeramics extends BehaviorBase
{
	@Override
	public boolean onItemUse(ItemFleMetaBase item, ItemStack itemstack,
			EntityPlayer player, World world, int x, int y, int z,
			ForgeDirection side, float xPos, float yPos, float zPos)
	{
		if(itemstack.stackSize >= 4)
		{
			itemstack.stackSize -=4;
			if(!world.isRemote)
			{
				player.openGui(FLE.MODID, -3, world, x, y, z);
			}
			return true;
		}
		return super.onItemUse(item, itemstack, player, world, x, y, z, side, xPos,
				yPos, zPos);
	}
}
