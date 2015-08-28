package fle.core.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import fle.api.block.IWorldNBT;
import fle.api.material.MaterialRock;
import fle.core.block.BlockRock;
import fle.core.init.IB;

public class FleRockGen extends FleMineableGen
{
	public FleRockGen(Block base, short meta, int number)
	{
		super(IB.rock, meta, number, base);
	}
	
	@Override
	protected void genBlockAt(World aWorld, Random rand, int x, int y, int z)
	{
		aWorld.setBlock(x, y, z, target, BlockRock.getHarvestLevel(MaterialRock.getOreFromID(mineableBlockMeta)), 2);
		((IWorldNBT) target).setMetadata(aWorld, x, y, z, mineableBlockMeta);
	}
}