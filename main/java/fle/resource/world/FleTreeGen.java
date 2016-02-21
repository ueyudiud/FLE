package fle.resource.world;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class FleTreeGen extends WorldGenAbstractTree
{
	public FleTreeGen(boolean flag)
	{
		super(flag);
	}

	@Override
	public boolean generate(World aWorld, Random aRand,
			int x, int y, int z)
	{
		return false;
	}
}