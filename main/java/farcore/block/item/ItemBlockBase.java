package farcore.block.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import farcore.block.BlockBase;
import flapi.FleAPI;

public class ItemBlockBase<T extends BlockBase> extends ItemBlock
{
	protected T block;
	
	public ItemBlockBase(Block block)
	{
		super(block);
		if(block instanceof BlockBase)
			this.block = (T) block;
	}
	
	protected String translateToLocal(String name, Object...objects)
	{
		return FleAPI.lang.translateToLocal(name, objects);
	}

    /**
     * Called when a Block is right-clicked with this Item
     *  
     * @param pos The block being right-clicked
     * @param side The side being right-clicked
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
    	IBlockState iblockstate = worldIn.getBlockState(pos);
    	Block block = iblockstate.getBlock();

        if (!block.isReplaceable(worldIn, pos))
        {
        	pos = pos.offset(side);
        }

        if (stack.stackSize == 0)
        {
        	return false;
        }
        else if (!playerIn.canPlayerEdit(pos, side, stack))
        {
        	return false;
        }
        else if (pos.getY() == 255 && this.block.getMaterial().isSolid())
        {
        	return false;
        }
        else if (worldIn.canBlockBePlaced(this.block, pos, false, side, (Entity)null, stack))
        {
        	int i = this.getMetadata(stack.getMetadata());
        	IBlockState iblockstate1 = this.block.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, i, playerIn, stack);

            if (placeBlockAt(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ, iblockstate1))
            {
                worldIn.playSoundEffect((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0F) / 2.0F, this.block.stepSound.getFrequency() * 0.8F);
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
