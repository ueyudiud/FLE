package fle.core.world.biome;

import java.util.Random;

import farcore.interfaces.ITreeGenerator;
import farcore.lib.substance.SubstanceWood;
import farcore.lib.world.biome.BiomeBase;
import farcore.lib.world.gen.tree.TreeGenSimple;
import fle.api.world.gen.TreeGenStraight;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BiomeRiver extends BiomeBase
{
	private static boolean init = false;

	protected static final ITreeGenerator genCeiba1 = new TreeGenSimple(7, true);
	protected static final ITreeGenerator genBirch1 = new TreeGenSimple(5, false);
	protected static final ITreeGenerator genWillow1 = new TreeGenStraight(4, 4, 1, 6, 4.6F, false);

	private void init()
	{
		if(!init)
		{
			SubstanceWood wood = SubstanceWood.getSubstance("ceiba");
			genCeiba1.initLogBlock(wood);
			wood = SubstanceWood.getSubstance("birch");
			genBirch1.initLogBlock(wood);
			wood = SubstanceWood.getSubstance("willow");
			genWillow1.initLogBlock(wood);
			init = true;
		}
	}
	
	public BiomeRiver(int id, boolean onDesert)
	{
		super(id);
		init();
		sedimentaryMultiply = 3.0F;
		metamorphismMultiply = 1.1F;
        spawnableCreatureList.clear();
        biomeDecorator.treesPerChunk = 1;
        biomeDecorator.ivyPerChunk = 1;
        if(onDesert)
        {
        	waterTop = Blocks.sand;
        	biomeDecorator.reedsPerChunk = 3;
        	biomeDecorator.cactiPerChunk = 3;
        	biomeDecorator.clayPerChunk = -999;
        }
        else
        {
        	waterTop = Blocks.dirt;
        }
	}
	
	@Override
	protected ITreeGenerator getTreeGenerator(World world, Random rand, int x, int z, double treeNoise)
	{
    	float rainfall = getRainfall(world, x, 0, z);
    	float temp = getTemperature(world, x, 0, z);
		return temp > 0.2F && rand.nextBoolean() ? genWillow1 : rainfall > 0.8F ? temp > 0.8F ? genCeiba1 : genBirch1 : rainfall > 0.5F ? genBirch1 : null;
	}
}