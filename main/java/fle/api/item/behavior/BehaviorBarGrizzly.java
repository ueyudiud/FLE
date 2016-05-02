package fle.api.item.behavior;

import farcore.enums.EnumBlock;
import farcore.util.U;
import fle.api.FleAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class BehaviorBarGrizzly extends BehaviorBase
{
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		MovingObjectPosition mop = U.Worlds.getMovingObjectPosition(world, player, true);
		if(mop.typeOfHit == MovingObjectType.BLOCK)
		{
			int x = mop.blockX;
			int y = mop.blockY;
			int z = mop.blockZ;
			if(y > 110 && y < 200 &&
					U.Worlds.isBlock(world, x, y, z, EnumBlock.water.block(), -1, false))
			{
				int count = 15;
				for(int i = -3; i <= 3; ++i)
					for(int j = -4; j <= 0; ++j)
						for(int k = -3; k <= 3; ++k)
						{
							if(i == 0 && j == 0 && k == 0) continue;
							if(U.Worlds.isBlock(world, x + i, y + j, z + k, EnumBlock.water.block(), -1, false))
							{
								count += world.getBlockMetadata(x + i, y + j, z + k);
							}
						}
				if(count >= 1200)
				{
					FleAPI.guiFactory.openGui(-1, player, world, x, y, z);
				}
			}
		}
		return super.onItemRightClick(stack, world, player);
	}
}