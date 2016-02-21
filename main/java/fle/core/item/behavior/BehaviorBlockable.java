package fle.core.item.behavior;

import java.util.List;

import farcore.block.BlockFle;
import farcore.util.U.N;
import flapi.item.ItemFleMetaBase;
import flapi.util.FleValue;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

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
		{
			ItemStack stack = new ItemStack(target, 1, targetMeta);
			if(itemstack.hasTagCompound())
				stack.stackTagCompound = itemstack.stackTagCompound;
			if(Item.getItemFromBlock(target).onItemUse(stack, player, world, x, y, z, N.side(side), xPos, yPos, zPos))
			{
				if(!player.capabilities.isCreativeMode)
					itemstack.stackSize -= sizeRequire;
				return true;
			}
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