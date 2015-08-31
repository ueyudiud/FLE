package fle.core.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import fle.FLE;
import fle.api.FleAPI;
import fle.api.block.ItemSubBlock;
import fle.api.world.BlockPos;
import fle.api.world.TreeInfo;

public class ItemLog extends ItemSubBlock
{
	public ItemLog(Block aBlock)
	{
		super(aBlock);
		for(TreeInfo tree : BlockLog.trees)
		{
			FleAPI.lm.registerLocal(new ItemStack(this, 1, BlockLog.trees.serial(tree)).getUnlocalizedName() + ".name", tree.getName().substring(0, 1).toUpperCase() + tree.getName().substring(1) + " Log");
		}
	}
	
	@Override
	public boolean placeBlockAt(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int side, float hitX, float hitY, float hitZ, int aMeta)
	{
		short tDamage = (short)getDamage(aStack);

    	if (!aWorld.setBlock(aX, aY, aZ, this.field_150939_a, 0, 3))
    	{
    		return false;
    	}
    	BlockLog.setData(new BlockPos(aWorld, aX, aY, aZ), tDamage);
	    if (aWorld.getBlock(aX, aY, aZ) == this.field_150939_a)
	    {
	    	this.field_150939_a.onBlockPlacedBy(aWorld, aX, aY, aZ, aPlayer, aStack);
	    	this.field_150939_a.onPostBlockPlaced(aWorld, aX, aY, aZ, tDamage);
	    }
	    return true;
	}
}