package fle.core.block.plant;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import flapi.block.old.ItemFleBlock;
import flapi.enums.EnumWorldNBT;
import flapi.world.BlockPos;
import fle.FLE;

public class ItemPlant extends ItemFleBlock
{
	public ItemPlant(Block aBlock)
	{
		super(aBlock);
	}
	
	@Override
	public IIcon getIconFromDamage(int meta)
	{
		return block.getIcon(0, meta);
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int i)
	{
		return block.getRenderColor(getDamage(stack));
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ, int metadata)
	{
		int data = getDamage(stack);
		
		if (!world.setBlock(x, y, z, block, data, 3))
		{
			return false;
		}
		
		FLE.fle.getWorldManager().setData(new BlockPos(world, x, y, z), EnumWorldNBT.Metadata, data);
		
		if (world.getBlock(x, y, z) == block)
	    {
			block.onBlockPlacedBy(world, x, y, z, player, stack);
			block.onPostBlockPlaced(world, x, y, z, metadata);
		}

		return true;
	}
}