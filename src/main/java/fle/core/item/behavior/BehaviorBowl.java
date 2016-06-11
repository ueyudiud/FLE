package fle.core.item.behavior;

import farcore.enums.EnumBlock;
import farcore.enums.EnumItem;
import farcore.item.ItemBase;
import farcore.util.U;
import fle.api.item.BowlEvent.BowlUseEvent;
import fle.api.item.behavior.BehaviorBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.TempCategory;

public class BehaviorBowl extends BehaviorBase
{
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		BowlUseEvent event = new BowlUseEvent(player, stack, ((ItemBase) stack.getItem()).getMovingObjectPositionFromPlayer(world, player, true));
		if(event.isCanceled())
		{
			return event.getStack();
		}
		else
		{
			if(event.mop.typeOfHit == MovingObjectType.BLOCK)
			{
				int x = event.mop.blockX;
				int y = event.mop.blockY;
				int z = event.mop.blockZ;
				if(world.getBiomeGenForCoords(x, z).getTempCategory() != TempCategory.OCEAN && 
						U.Worlds.isBlock(world, x, y, z, EnumBlock.water.block(), -1, false))
				{
					int c = 0;
					while(c < 6)
					{
						if(U.Worlds.isBlock(world, x, --y, z, EnumBlock.water.block(), -1, false))
						{
							++c;
						}
						else
						{
							break;
						}
					}
					
					ItemStack output = c > 3 ? EnumItem.bowl.instance(1, "water_purified") : EnumItem.bowl.instance(1, "water_dirty");
					if(stack.stackSize <= 1)
					{
						return output;
					}
					else
					{
						stack.stackSize--;
						U.Inventorys.givePlayer(player, output);
						return stack;
					}
				}
			}
			return super.onItemRightClick(event.getStack(), world, player);
		}
	}
}