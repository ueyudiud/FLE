package fle.core.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import fle.FLE;
import fle.api.block.ItemSubBlock;
import fle.api.material.MaterialOre;
import fle.api.world.BlockPos;

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
	public boolean placeBlockAt(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int side, float hitX, float hitY, float hitZ, int aMeta)
	{
		short tDamage = (short)getDamage(aStack);

    	if (!aWorld.setBlock(aX, aY, aZ, this.field_150939_a, 0, 3))
    	{
    		return false;
    	}
    	BlockOre.setData(new BlockPos(aWorld, aX, aY, aZ), tDamage);
	    if (aWorld.getBlock(aX, aY, aZ) == this.field_150939_a)
	    {
	    	this.field_150939_a.onBlockPlacedBy(aWorld, aX, aY, aZ, aPlayer, aStack);
	    	this.field_150939_a.onPostBlockPlaced(aWorld, aX, aY, aZ, tDamage);
	    }
	    return true;
	}
}