package fle.core.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import fle.api.block.ItemSubBlock;
import fle.api.material.MaterialRock;
import fle.api.world.BlockPos;

public class ItemRock extends ItemSubBlock
{
	public ItemRock(Block aBlock)
	{
		super(aBlock);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack aStack) 
	{
		return block.getUnlocalizedName() + ":" + MaterialRock.getOreFromID(getDamage(aStack)).getRockName().toLowerCase();
	}
	
	@Override
	public boolean placeBlockAt(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int side, float hitX, float hitY, float hitZ, int aMeta)
	{
		short tDamage = (short)getDamage(aStack);

    	if (!aWorld.setBlock(aX, aY, aZ, field_150939_a, field_150939_a.onBlockPlaced(aWorld, aX, aY, aZ, side, hitY, hitY, hitZ, tDamage), 3))
    	{
    		return false;
    	}
	    if (aWorld.getBlock(aX, aY, aZ) == field_150939_a)
	    {
	    	((BlockRock) field_150939_a).setMetadata(aWorld, aX, aY, aZ, tDamage);
	    	field_150939_a.onBlockPlacedBy(aWorld, aX, aY, aZ, aPlayer, aStack);
	    	field_150939_a.onPostBlockPlaced(aWorld, aX, aY, aZ, tDamage);
	    }
	    return true;
	}
}