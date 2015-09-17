package fle.core.world;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.world.BlockPos;

public class FleVineGen extends FleSurfaceGen
{
	public FleVineGen(int aSize, int aCount)
	{
		super(aSize, aCount);
	}
	
	ForgeDirection dirs[] = {ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST};

	@Override
	public boolean generateAt(World aWorld, Random aRand, int x, int y, int z)
	{
		BlockPos pos = new BlockPos(aWorld, x, y, z);
		if(pos.getBlock() != Blocks.air) return false;
		boolean flag = false;
		for(ForgeDirection dir : dirs)
		{
			if(pos.toPos(dir).getBlock().isLeaves(aWorld, x, y, z))
			{
				flag = true;
				aWorld.setBlock(x, y, z, Blocks.vine);
			}
		}
		return flag;
	}
}