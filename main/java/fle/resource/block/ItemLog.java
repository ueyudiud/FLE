package fle.resource.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import flapi.block.old.ItemSubBlock;

public class ItemLog extends ItemSubBlock
{
	public ItemLog(Block aBlock)
	{
		super(aBlock);
	}
	
	@Override
	public boolean placeBlockAt(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int side, float hitX, float hitY, float hitZ, int aMeta)
	{
		short tDamage = (short)getDamage(aStack);

    	if (!aWorld.setBlock(aX, aY, aZ, block, block.onBlockPlaced(aWorld, aX, aY, aZ, side, hitX, hitY, hitZ, tDamage), 3))
    	{
    		return false;
    	}
	    if (aWorld.getBlock(aX, aY, aZ) == block)
	    {
	    	block.onBlockPlacedBy(aWorld, aX, aY, aZ, aPlayer, aStack);
	    	block.onPostBlockPlaced(aWorld, aX, aY, aZ, tDamage);
	    }
	    return true;
	}
}