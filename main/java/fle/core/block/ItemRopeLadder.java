package fle.core.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import fle.api.block.ItemFleBlock;
import fle.core.init.IB;

public class ItemRopeLadder extends ItemFleBlock
{
	public ItemRopeLadder(Block aBlock)
	{
		super(aBlock);
	}
	
	private boolean placedFlag;
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ, int metadata)
	{
		if(placedFlag) return super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
		placedFlag = true;
		boolean flag = true;
		int length = b(stack);
		if(length <= 0) return false;
		else for(int i = 0; i < length; ++i)
		{
			if(world.getBlock(x, y - i, z) != Blocks.air)
			{
				placedFlag = false;
				return false;
			}
		}
		for(int i = 0; i < length; ++i)
		{
			super.placeBlockAt(new ItemStack(IB.ropeLadder), player, world, x, y - i, z, side, hitX, hitY, hitZ, metadata);
		}
		placedFlag = false;
		return true;
	}
	
	public static ItemStack a(int length)
	{
		ItemStack ret = new ItemStack(IB.ropeLadder);
		setupNBT(ret).setInteger("Length", length);
		return ret;
	}
	
	public static int b(ItemStack aStack)
	{
		return setupNBT(aStack).getInteger("Length");
	}
}