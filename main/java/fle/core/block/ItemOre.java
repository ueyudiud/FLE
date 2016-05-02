package fle.core.block;

import farcore.block.ItemBlockBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemOre extends ItemBlockBase
{
	public ItemOre(Block block)
	{
		super(block);
		hasSubtypes = true;
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ, int metadata)
	{
		return super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, 0);
	}
}