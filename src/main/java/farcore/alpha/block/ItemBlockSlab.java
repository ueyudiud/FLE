package farcore.alpha.block;

import farcore.enums.Direction;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockSlab extends ItemBlockBase
{
	public ItemBlockSlab(Block block)
	{
		super(block);
	}
	
    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        Block block = world.getBlock(x, y, z);

        if(block == this.block)
        {
        	int meta = world.getBlockMetadata(x, y, z);
        	if(meta == Direction.oppsite[side])
        	{
        		if (stack.stackSize == 0 || !player.canPlayerEdit(x, y, z, side, stack) ||
                		y == 255)
                {
                    return false;
                }
        		if(meta == 0 || meta == 1)
        		{
        			if(world.setBlockMetadataWithNotify(x, y, z, 6, 3))
        			{
                        world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.field_150939_a.stepSound.func_150496_b(), (this.field_150939_a.stepSound.getVolume() + 1.0F) / 2.0F, this.field_150939_a.stepSound.getPitch() * 0.8F);
                        --stack.stackSize;
        			}
                    return true;
        		}
        		else if(meta == 2 || meta == 3)
        		{
        			if(world.setBlockMetadataWithNotify(x, y, z, 7, 3))
        			{
                        world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.field_150939_a.stepSound.func_150496_b(), (this.field_150939_a.stepSound.getVolume() + 1.0F) / 2.0F, this.field_150939_a.stepSound.getPitch() * 0.8F);
                        --stack.stackSize;
        			}
                    return true;
        		}
        		else if(meta == 4 || meta == 5)
        		{
        			if(world.setBlockMetadataWithNotify(x, y, z, 8, 3))
        			{
                        world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.field_150939_a.stepSound.func_150496_b(), (this.field_150939_a.stepSound.getVolume() + 1.0F) / 2.0F, this.field_150939_a.stepSound.getPitch() * 0.8F);
                        --stack.stackSize;
        			}
                    return true;
        		}
        		return true;
        	}
        	else
        	{
            	Direction direction = Direction.directions[side];
            	x += direction.x;
            	y += direction.y;
            	z += direction.z;
        	}
        }
        else if (block == Blocks.snow_layer && (world.getBlockMetadata(x, y, z) & 7) < 1)
        {
            side = 1;
        }
        else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush 
        		&& !block.isReplaceable(world, x, y, z))
        {
        	Direction direction = Direction.directions[side];
        	x += direction.x;
        	y += direction.y;
        	z += direction.z;
        }

        if (stack.stackSize == 0 || !player.canPlayerEdit(x, y, z, side, stack) ||
        		(y == 255 && block.getMaterial().isSolid()))
        {
            return false;
        }
        else if (world.canPlaceEntityOnSide(block, x, y, z, false, side, player, stack))
        {
            int i1 = getMetadata(stack.getItemDamage());
            int j1 = this.block.onBlockPlaced(world, x, y, z, U.Worlds.fixSide(side, hitX, hitY, hitZ), hitX, hitY, hitZ, i1);

            if (placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, j1))
            {
                world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.field_150939_a.stepSound.func_150496_b(), (this.field_150939_a.stepSound.getVolume() + 1.0F) / 2.0F, this.field_150939_a.stepSound.getPitch() * 0.8F);
                --stack.stackSize;
            }

            return true;
        }
        else
        {
            return false;
        }
    }
}