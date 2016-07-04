package farcore.alpha.interfaces.block.tree;

import java.util.Random;

import farcore.interfaces.ITreeGenerator;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class ITreeInfo
{
	protected Block log;
	protected Block logArt;
	protected Block leaves;
	
	protected final String name;
	
	public ITreeInfo(String name)
	{
		this.name = name;
	}
	
	public void updateLog(World world, int x, int y, int z, Random random)
	{
		
	}
	
	public void updateLeaves(World world, int x, int y, int z, Random random)
	{
		
	}
	
	protected boolean shouldLeavesDency(World world, int x, int y, int z)
	{
		return (world.getBlockMetadata(x, y, z) & 0x8) != 0;
	}

	public void beginLeavesDency(World world, int x, int y, int z)
	{
		world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) | 0x8, 2);
	}

	public void stopLeavesDency(World world, int x, int y, int z)
	{
		world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) | (~0x8), 2);
	}
	
	protected void checkDencyLeaves(World world, int x, int y, int z, int maxL)
	{
		int range = 2 * maxL + 1;
		int[][][] checkBuffer = new int[range][range][range];
		
		dencyLeaves(maxL, world, x, y, z, 0, 0, 0, checkBuffer);
		beginLeavesDency(maxL, world, x, y, z, checkBuffer);
	}
	
	private void dencyLeaves(int length, World world, int x, int y, int z, int ofX, int ofY, int ofZ, int[][][] flags)
	{
		if(flags[ofX + length][ofY + length][ofZ + length] > 0) return;
		int v = 0;
		int v1;
		if(ofX + length >= 1 && (v1 = flags[ofX + length - 1][ofY + length    ][ofZ + length    ]) > v + 1){v = v1 - 1;}
		if(ofX < length      && (v1 = flags[ofX + length + 1][ofY + length    ][ofZ + length    ]) > v + 1){v = v1 - 1;}
		if(ofY + length >= 1 && (v1 = flags[ofX + length    ][ofY + length - 1][ofZ + length    ]) > v + 1){v = v1 - 1;}
		if(ofY < length      && (v1 = flags[ofX + length    ][ofY + length + 1][ofZ + length    ]) > v + 1){v = v1 - 1;}
		if(ofZ + length >= 1 && (v1 = flags[ofX + length    ][ofY + length    ][ofZ + length - 1]) > v + 1){v = v1 - 1;}
		if(ofZ < length      && (v1 = flags[ofX + length    ][ofY + length    ][ofZ + length + 1]) > v + 1){v = v1 - 1;}
		if(canLeaveGrowNearby(world, x + ofX, y + ofY, z + ofZ))
		{
			v = length;
		}
		if((flags[ofX + length][ofY + length][ofZ + length] = v) > 1)
		{
			if(ofX + length >= 1)
				dencyLeaves(length, world, x, y, z, ofX - 1, ofY, ofZ, flags);
			if(ofX < length)
				dencyLeaves(length, world, x, y, z, ofX + 1, ofY, ofZ, flags);
			if(ofY + length >= 1)
				dencyLeaves(length, world, x, y, z, ofX, ofY - 1, ofZ, flags);
			if(ofY < length)
				dencyLeaves(length, world, x, y, z, ofX, ofY + 1, ofZ, flags);
			if(ofZ + length >= 1)
				dencyLeaves(length, world, x, y, z, ofX, ofY, ofZ - 1, flags);
			if(ofZ < length)
				dencyLeaves(length, world, x, y, z, ofX, ofY, ofZ + 1, flags);
		}
	}
	
	private void beginLeavesDency(int length, World world, int x, int y, int z, int[][][] flags)
	{
		for(int i = -length; i <= length; ++i)
			for(int j = -length; j <= length; ++j)
				for(int k = -length; k <= length; ++k)
				{
					if(flags[i + length][j + length][k + length] > 0)
					{
						stopLeavesDency(world, x, y, z);
					}
					else
					{
						onLeavesDead(world, x + i, y + j, z + k);
					}
				}
	}
	
	protected void onLeavesDead(World world, int x, int y, int z)
	{
		
	}
	
	protected boolean canLeaveGrowNearby(World world, int x, int y, int z)
	{
		return U.Worlds.isBlockNearby(world, x, y, z, log, -1, false);
	}
	
    protected void dropBlockAsItem(World world, int x, int y, int z, ItemStack stack)
    {
    	if(stack == null) return;
        if (!world.isRemote)
        {
            float f = 0.5F;
            double d0 = (world.rand.nextFloat() * f) + (1.0F - f) * 0.5 + .5;
            double d1 = (world.rand.nextFloat() * f) + (1.0F - f) * 0.5 + .5;
            double d2 = (world.rand.nextFloat() * f) + (1.0F - f) * 0.5 + .5;
            EntityItem entityitem = new EntityItem(world, x + d0, y + d1, z + d2, stack.copy());
            entityitem.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(entityitem);
        }
    }
    
    protected abstract ITreeGenerator generator();
}