package fle.resource.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import flapi.block.old.ItemSubBlock;
import flapi.material.MaterialOre;
import flapi.world.BlockPos;

public class ItemOre extends ItemSubBlock
{
	public ItemOre(Block aBlock)
	{
		super(aBlock);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack aStack) 
	{
		return block.getUnlocalizedName() + ":" + MaterialOre.getOreFromID(getDamage(aStack)).getOreName().toLowerCase();
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int aMeta)
	{
		short tDamage = (short)getDamage(stack);

    	if (!world.setBlock(x, y, z, block, block.onBlockPlaced(world, x, y, z, side, hitY, hitY, hitZ, tDamage), 3))
    	{
    		return false;
    	}
	    if (world.getBlock(x, y, z) == block)
	    {
	    	block.onBlockPlacedBy(world, x, y, z, player, stack);
	    	block.onPostBlockPlaced(world, x, y, z, tDamage);
	    	BlockOre.setData(new BlockPos(world, x, y, z), tDamage);
	    }
	    return true;
	}
}