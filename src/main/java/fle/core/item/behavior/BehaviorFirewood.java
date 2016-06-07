package fle.core.item.behavior;

import farcore.handler.FarCoreKeyHandler;
import fle.api.item.behavior.BehaviorBase;
import fle.core.tile.TileEntityCampfire;
import fle.load.BlockItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BehaviorFirewood extends BehaviorBase
{
	private static ItemBlock item;
	private static ItemStack stack;
	
	private static void init()
	{
		if(stack == null)
		{
			stack = new ItemStack(BlockItems.campfire);
			item = (ItemBlock) Item.getItemFromBlock(BlockItems.campfire);
		}
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ) 
	{
		if(stack.stackSize >= 4 && player.isSneaking() && FarCoreKeyHandler.get(player, "place"))
		{
			init();
			if(item.onItemUse(this.stack.copy(), player, world, x, y, z, side, hitX, hitY, hitZ))
			{
				stack.stackSize -= 4;
				return true;
			}
		}
		return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
	}
}