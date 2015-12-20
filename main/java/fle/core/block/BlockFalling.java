package fle.core.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import flapi.block.interfaces.IMovableBlock;
import flapi.block.old.BlockFle;
import flapi.block.old.ItemFleBlock;
import flapi.world.BlockPos;
import fle.FLE;
import fle.core.entity.EntityFleFallingBlock;

public abstract class BlockFalling extends BlockFle implements IMovableBlock
{
	public BlockFalling(String aName, Material aMaterial)
	{
		super(aName, aMaterial);
		setTickRandomly(true);
	}

	protected BlockFalling(Class<? extends ItemFleBlock> aItemClass,
			String aName, Material aMaterial)
	{
		super(aItemClass, aName, aMaterial);
		setTickRandomly(true);
	}

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World aWorld, int x, int y, int z)
    {
        aWorld.scheduleBlockUpdate(x, y, z, this, this.tickRate(aWorld));
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World aWorld, int x, int y, int z, Block p_149695_5_)
    {
        aWorld.scheduleBlockUpdate(x, y, z, this, this.tickRate(aWorld));
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World aWorld, int x, int y, int z, Random rand)
    {
        if (!aWorld.isRemote)
        {
            this.func_149830_m(aWorld, x, y, z);
        }
    }

    private void func_149830_m(World aWorld, int x, int y, int z)
    {
        if (checkCanDrop(aWorld, x, y - 1, z) && y >= 0)
        {
            byte b0 = 32;

            if (!net.minecraft.block.BlockFalling.fallInstantly && aWorld.checkChunksExist(x - b0, y - b0, z - b0, x + b0, y + b0, z + b0))
            {
                if (!aWorld.isRemote)
                {
                	EntityFleFallingBlock.setDropBlockInWorld(aWorld, new BlockPos(aWorld, x, y, z));
                }
            }
            else
            {
            	int metadata = aWorld.getBlockMetadata(x, y, z);
            	short[] data = FLE.fle.getWorldManager().getDatas(new BlockPos(aWorld, x, y, z));
                aWorld.setBlockToAir(x, y, z);
                
                while (checkCanDrop(aWorld, x, y - 1, z) && y > 0)
                {
                    --y;
                }

                if (y > 0)
                {
                    aWorld.setBlock(x, y, z, this, metadata, 3);
                    FLE.fle.getWorldManager().setDatas(new BlockPos(aWorld, x, y, z), data, false);
                }
            }
        }
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World aWorld)
    {
        return 2;
    }
    
    public static boolean fallInstantly()
    {
    	return net.minecraft.block.BlockFalling.fallInstantly;
    }

    public static boolean checkCanDrop(World aWorld, int x, int y, int z)
    {
        Block block = aWorld.getBlock(x, y, z);

        if (block.isAir(aWorld, x, y, z) || block == Blocks.fire)
        {
            return true;
        }
        else
        {
            Material material = block.getMaterial();
            return material == Material.water || material == Material.lava;
        }
    }
}