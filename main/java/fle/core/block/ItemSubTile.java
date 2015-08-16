package fle.core.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import fle.FLE;
import fle.api.block.ItemFleBlock;
import fle.api.world.BlockPos;

public class ItemSubTile extends ItemFleBlock
{
	public ItemSubTile(Block aBlock)
	{
		super(aBlock);
		hasSubtypes = true;
		canRepair = false;
		setMaxDamage(0);
	}

    /**
     * Called to actually place the block, after the location is determined
     * and all permission checks have been made.
     *
     * @param stack The item stack that was used to place the block. This can be changed inside the method.
     * @param player The player who is placing the block. Can be null if the block is not being placed by a player.
     * @param side The side the player (or machine) right-clicked on.
     */
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
    	short tDamage = (short) getDamage(stack);
    	FLE.fle.getWorldManager().setData(new BlockPos(world, x, y, z), 0, tDamage);
    	if (!world.setBlock(x, y, z, field_150939_a, tDamage, 3))
    	{
    		return false;
    	}

    	if (world.getBlock(x, y, z) == field_150939_a)
    	{
    		field_150939_a.onBlockPlacedBy(world, x, y, z, player, stack);
    		field_150939_a.onPostBlockPlaced(world, x, y, z, tDamage);
    	}

    	return true;
    }
}