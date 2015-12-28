package fle.resource.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import flapi.material.MaterialOre;
import fle.core.block.ItemSubTile;
import fle.core.init.IB;

public class ItemOreCobble extends ItemSubTile
{
	public ItemOreCobble(Block aBlock)
	{
		super(aBlock);
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta)
	{
		short damage = (short)getDamage(stack);

    	if (!world.setBlock(x, y, z, block, block.onBlockPlaced(world, x, y, z, side, hitY, hitY, hitZ, (int) Math.ceil(MaterialOre.getOreFromID(damage).getPropertyInfo().getHardness())), 3))
    	{
    		return false;
    	}
	    if (world.getBlock(x, y, z) == block)
	    {
	    	if (world.getTileEntity(x, y, z) instanceof TileEntityOreCobble)
	    	{
	    		((TileEntityOreCobble) world.getTileEntity(x, y, z)).init(MaterialOre.getOreFromID(damage), ((fle.resource.item.ItemOre) IB.oreChip).getOreAmount(stack));
	    	}
	    	block.onBlockPlacedBy(world, x, y, z, player, stack);
	    	block.onPostBlockPlaced(world, x, y, z, damage);
	    }
	    return true;
	}
}