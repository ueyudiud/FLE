package fle.core.block;

import farcore.block.ItemBlockBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class ItemSnow extends ItemBlockBase
{
	public ItemSnow(Block block)
	{
		super(block);
		hasSubtypes = true;
	}

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (stack.stackSize == 0)
        {
            return false;
        }
        else if (!player.canPlayerEdit(x, y, z, side, stack))
        {
            return false;
        }
        else
        {
            Block block = world.getBlock(x, y, z);

            if (block == this.block)
            {
                int i1 = world.getBlockMetadata(x, y, z);

                AxisAlignedBB aabb = block.getCollisionBoundingBoxFromPool(world, x, y, z);
                if (i1 < 15 && 
                		(aabb == null || world.checkNoEntityCollision(aabb)) && 
                		world.setBlockMetadataWithNotify(x, y, z, i1 + 1, 2))
                {
                    world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.field_150939_a.stepSound.func_150496_b(), (this.field_150939_a.stepSound.getVolume() + 1.0F) / 2.0F, this.field_150939_a.stepSound.getPitch() * 0.8F);
                    --stack.stackSize;
                    return true;
                }
            }

            return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
        }
    }
}