package fle.core.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class ItemBowl extends IItemControl<Item>
{
	public ItemBowl()
	{
		super();
	}
	
	@Override
	protected boolean doListenItem(ItemStack aStack)
	{
		return aStack != null ? aStack.getItem() == Items.bowl : false;
	}
	
	@Override
	protected boolean shouldCheckMOPFirst()
	{
		return true;
	}
	
	@Override
	protected boolean onPlayerMOPActive(EntityPlayer entityPlayer, World world, 
			MovingObjectPosition fluidMOP, MovingObjectPosition withoutFluidMOP)
	{
		ItemStack stack = entityPlayer.getCurrentEquippedItem();
		if(Items.bowl == stack.getItem())
		{
			if(fluidMOP == null) return false;
			if(fluidMOP.typeOfHit == MovingObjectType.BLOCK)
			{
				int x = fluidMOP.blockX;
				int y = fluidMOP.blockY;
				int z = fluidMOP.blockZ;
				if(world.getBlock(x, y, z) == Blocks.water)
				{
					if(stack.stackSize == 1)
					{
						entityPlayer.setCurrentItemOrArmor(0, ItemFleSub.a("bowl_water"));
					}
					else
					{
						stack.stackSize--;
						ItemStack a = ItemFleSub.a("bowl_water");
						if(!entityPlayer.inventory.addItemStackToInventory(a))
						{
							if(world.isRemote) return true;
							entityPlayer.dropPlayerItemWithRandomChoice(a, false);
						}
					}
				}
			}
			return true;
		}
		return false;
	}
}