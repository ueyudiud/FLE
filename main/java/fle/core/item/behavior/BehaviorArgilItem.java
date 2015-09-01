package fle.core.item.behavior;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.FleAPI;
import fle.api.FleValue;
import fle.api.block.IDebugableBlock;
import fle.api.item.ItemFleMetaBase;
import fle.core.init.IB;
import fle.core.te.argil.TileEntityArgilItems;

public class BehaviorArgilItem extends BehaviorBlockable implements IDebugableBlock
{
	public BehaviorArgilItem()
	{
		super(IB.argil_unsmelted, 1);
	}
	
	@Override
	public boolean onItemUse(ItemFleMetaBase item, ItemStack itemstack,
			EntityPlayer player, World world, int x, int y, int z,
			ForgeDirection side, float xPos, float yPos, float zPos) 
	{
		if(itemstack.stackSize <= 64)
			if(Item.getItemFromBlock(target).onItemUse(new ItemStack(target, 1, targetMeta), player, world, x, y, z, FleAPI.getIndexFromDirection(side), xPos, yPos, zPos))
			{
				Block block = world.getBlock(x, y, z);
				if (block != Blocks.air)
				{
		            x += side.offsetX;
		            y += side.offsetY;
		            z += side.offsetZ;
				}
				if(world.getTileEntity(x, y, z) != null)
				{
					TileEntityArgilItems tile = (TileEntityArgilItems) world.getTileEntity(x, y, z);
					tile.stack = itemstack.copy();
				}
				if(!player.capabilities.isCreativeMode)
					player.setCurrentItemOrArmor(0, null);;
				return true;
			}
		return false;
	}

	@Override
	public void addInfomationToList(World aWorld, int x, int y, int z,
			List aList)
	{
		if(aWorld.getTileEntity(x, y, z) instanceof TileEntityArgilItems)
		{
			aList.add(FleValue.format_progress.format_c(((TileEntityArgilItems) aWorld.getTileEntity(x, y, z)).getProgress()));
		}
	}
}