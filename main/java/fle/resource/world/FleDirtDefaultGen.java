package fle.resource.world;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class FleDirtDefaultGen extends FleDirtGen
{
	public FleDirtDefaultGen(Block aBlock, int num)
	{
		super(aBlock, num);
	}

	@Override
	protected boolean checkEnvironmentBlockAndMeta(Block block, int metadata)
	{
		return block.getMaterial() == Material.water;
	}

	@Override
	protected boolean checkTargetBlockAndMeta(Block block, int metadata)
	{
		return block == Blocks.dirt || block == target;
	}

	@Override
	protected void generateBlockAt(World aWorld, int x, int y, int z)
	{
		aWorld.setBlock(x, y, z, target, 0, 2);
	}
}