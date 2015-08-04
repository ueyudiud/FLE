package fle.core.item.behavior;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.FleAPI;
import fle.api.item.ItemFleMetaBase;

public class BehaviorBlockable extends BehaviorBase
{
	protected Block target;
	protected int targetMeta;

	public BehaviorBlockable(Block aBlock) 
	{
		this(aBlock, 0);
	}
	public BehaviorBlockable(Block aBlock, int aMeta) 
	{
		target = aBlock;
		targetMeta = aMeta;
	}
	
	@Override
	public boolean onItemUse(ItemFleMetaBase item, ItemStack itemstack,
			EntityPlayer player, World world, int x, int y, int z,
			ForgeDirection side, float xPos, float yPos, float zPos) 
	{
		if(Item.getItemFromBlock(target).onItemUse(new ItemStack(Blocks.sapling, 1, targetMeta), player, world, x, y, z, FleAPI.getIndexFromDirection(side), xPos, yPos, zPos))
		{
			if(!player.capabilities.isCreativeMode)
				itemstack.stackSize -= 1;
			return true;
		}
		return false;
	}
}