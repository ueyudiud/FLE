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
import fle.api.block.BlockFle;
import fle.api.item.ItemFleMetaBase;

public class BehaviorBlockable extends BehaviorBase
{
	protected int sizeRequire = 1;
	protected Block target;
	protected int targetMeta;

	public BehaviorBlockable(Block aBlock) 
	{
		this(aBlock, 0);
	}
	public BehaviorBlockable(int aSize, Block aBlock) 
	{
		this(aBlock, 0);
		sizeRequire = aSize;
	}
	public BehaviorBlockable(Block aBlock, int aMeta) 
	{
		target = aBlock;
		targetMeta = aMeta;
	}
	public BehaviorBlockable(int aSize, Block aBlock, int aMeta) 
	{
		this(aBlock, aMeta);
		sizeRequire = aSize;
	}
	
	@Override
	public boolean onItemUse(ItemFleMetaBase item, ItemStack itemstack,
			EntityPlayer player, World world, int x, int y, int z,
			ForgeDirection side, float xPos, float yPos, float zPos) 
	{
		if(itemstack.stackSize >= sizeRequire)
			if(Item.getItemFromBlock(target).onItemUse(new ItemStack(target, 1, targetMeta), player, world, x, y, z, FleAPI.getIndexFromDirection(side), xPos, yPos, zPos))
			{
				if(!player.capabilities.isCreativeMode)
					itemstack.stackSize -= sizeRequire;
				return true;
			}
		return false;
	}
	
	@Override
	public void getAdditionalToolTips(ItemFleMetaBase item, List<String> list,
			ItemStack itemstack, boolean flag)
	{
		if(flag)
		{
			list.add("Use  " + sizeRequire + " " + itemstack.getDisplayName() + " to place " + new ItemStack(target, 1, targetMeta).getDisplayName() + " on the ground.");
		}
		else
		{
			list.add(FleValue.info_shift);
		}
		super.getAdditionalToolTips(item, list, itemstack, flag);
	}
	
	@Override
	public void getAdditionalToolTips(ItemFleMetaBase item, List<String> list,
			ItemStack itemstack)
	{
		super.getAdditionalToolTips(item, list, itemstack);
		if (target instanceof BlockFle)
		{
			((BlockFle) target).addInformation(itemstack, list, null);
		}
	}
}