package farcore.lib.world.gen.tree;

import java.util.Random;

import farcore.interfaces.ITreeGenerator;
import farcore.util.noise.NoiseBasic;
import farcore.util.noise.NoisePerlin;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class TreeGenEmpty extends TreeGenAbstract
{
	protected static final NoiseBasic customTreeNoise = new NoisePerlin(481945937195L, 6, 1.8D, 2.4D, 2D);
	protected static final NoiseBasic customPlantNoise = new NoisePerlin(39571194729417L, 5, 3.2D, 2D, 2D);
	
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