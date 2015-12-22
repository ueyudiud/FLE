package fle.resource.tree;

import java.util.Random;

import net.minecraft.world.World;
import flapi.plant.TreeInfo;

public class FleTreeCardGen extends FleAbstractTreeGen
{
	TreeInfo info;
	
	public FleTreeCardGen(TreeInfo tree)
	{
		super(false);
		this.info = tree;
	}
	
	@Override
	public boolean generate(World world, Random rand, int x, int y, int z)
	{
		return info.generate(world, x, y, z, rand);
	}
}