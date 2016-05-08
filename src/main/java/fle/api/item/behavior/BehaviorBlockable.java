package fle.api.item.behavior;

import farcore.util.U.Player;
import farcore.util.Values;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BehaviorBlockable extends BehaviorBase
{
	private int blockSize;
	private ItemBlock block;
	private int meta;

	public BehaviorBlockable(Block block)
	{
		this(1, block, 0);
	}
	public BehaviorBlockable(int size, Block block, int metadata)
	{
		this.blockSize = size;
		this.block = (ItemBlock) Item.getItemFromBlock(block);
		this.meta = metadata;
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ)
	{
		if(stack == null || stack.stackSize < blockSize) return false;
		ItemStack stack2 = new ItemStack(block, 1, meta);
		boolean flag = block.onItemUse(stack2, player, world, x, y, z, side, hitX, hitY, hitZ);
		if(flag)
		{
			stack.stackSize -= blockSize;
		}
		return flag;
	}
}