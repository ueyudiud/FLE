package fle.core.item.behavior;

import fle.api.FleAPI;
import fle.api.item.behavior.BehaviorBase;
import fle.core.container.alpha.ContainerCeramics;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BehaviorArgilBall extends BehaviorBase
{
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ)
	{
		if(stack.stackSize >= 6)
		{
			if(!world.isRemote)
			{
				FleAPI.openGui(-3, player, world, x, y, z);
			}
			return true;
		}
		return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
	}
}