package farcore.lib.world.gen.tree;

import java.util.Random;

import farcore.interfaces.ITreeGenerator;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class TreeGenEmpty extends TreeGenAbstract
{
	@Override
	public boolean generate(World world, Random random, int x, int y, int z, boolean isNatural)
	{
		return true;
	}

	@Override
	public void initLogBlock(Block log, Block leaves)
	{
		
	}
}