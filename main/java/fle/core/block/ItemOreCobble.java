package fle.core.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import fle.api.block.ItemSubBlock;
import fle.api.material.MaterialOre;
import fle.api.world.BlockPos;
import fle.core.te.TileEntityOreCobble;

public class ItemOreCobble extends ItemSubTile
{
	public ItemOreCobble(Block aBlock)
	{
		super(aBlock);
	}
	
	@Override
	public boolean placeBlockAt(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int side, float hitX, float hitY, float hitZ, int aMeta)
	{
		short tDamage = (short)getDamage(aStack);

    	if (!aWorld.setBlock(aX, aY, aZ, field_150939_a, field_150939_a.onBlockPlaced(aWorld, aX, aY, aZ, side, hitY, hitY, hitZ, (int) Math.ceil(MaterialOre.getOreFromID(tDamage).getPropertyInfo().getHardness())), 3))
    	{
    		return false;
    	}
	    if (aWorld.getBlock(aX, aY, aZ) == field_150939_a)
	    {
	    	BlockOreCobble.setOre(aWorld, aX, aY, aZ, tDamage);
	    	if (aWorld.getTileEntity(aX, aY, aZ) instanceof TileEntityOreCobble)
	    	{
	    		((TileEntityOreCobble) aWorld.getTileEntity(aX, aY, aZ)).init(MaterialOre.getOreFromID(tDamage));
	    	}
	    	field_150939_a.onBlockPlacedBy(aWorld, aX, aY, aZ, aPlayer, aStack);
	    	field_150939_a.onPostBlockPlaced(aWorld, aX, aY, aZ, tDamage);
	    }
	    return true;
	}
}