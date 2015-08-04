package fle.api.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import fle.api.te.IMetadataTile;

public class ItemSubBlock extends ItemFleBlock
{
	public ItemSubBlock(Block aBlock)
	{
		super(aBlock);
		hasSubtypes = true;
	}

	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		return false;
	}
	  
	public String getUnlocalizedName(ItemStack aStack)
	{
		return block.getUnlocalizedName() + ":" + getDamage(aStack);
	}
	  
	public boolean placeBlockAt(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int side, float hitX, float hitY, float hitZ, int aMeta)
	{
		short tDamage = (short) getDamage(aStack);
	    if (tDamage > 0)
	    {
	    	if (!aWorld.setBlock(aX, aY, aZ, block, block.getHarvestData(tDamage), 3))
	    	{
	    		return false;
	    	}
	    	IMetadataTile tTileEntity = (IMetadataTile) aWorld.getTileEntity(aX, aY, aZ);
	    	tTileEntity.setMetadata(tDamage);
	    }
	    else if (!aWorld.setBlock(aX, aY, aZ, block, 0, 3))
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