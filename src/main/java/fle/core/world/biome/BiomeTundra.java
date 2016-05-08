package fle.core.world.biome;

import java.util.Random;

import farcore.interfaces.ITreeGenerator;
import farcore.lib.substance.SubstanceWood;
import farcore.lib.world.biome.BiomeBase;
import farcore.lib.world.gen.tree.TreeGenPine;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeTundra extends BiomeBase
{
//    private static final WorldGenTaiga1 tree = new WorldGenTaiga1();

	private static boolean init = false;

    private static final ITreeGenerator genSpruce1 = new TreeGenPine();
	
	private void init()
	{
		if(!init)
		{
			SubstanceWood wood = SubstanceWood.getSubstance("spruce");
			genSpruce1.initLogBlock(wood);
			init = true;
		}
	}
	
	public BiomeTundra(int id)
	{
		super(id);
		init();
		topBlock = Blocks.dirt;
		field_150604_aj = 2;
		biomeDecorator.grassPerChunk = 2;
		spawnableCreatureList.clear();

        spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 5, 4, 4));
	}
	
	@Override
	protected ITreeGenerator getTreeGenerator(World world, Random rand, int x, int z, double treeNoise)
	{
		return genSpruce1;
	}
	
//	@Override
//	public WorldGenAbstractTree func_150567_a(Random random)
//	{
//		return tree;
//	}

    /**
     * Gets a WorldGen appropriate for this biome.
     */
    public WorldGenerator getRandomWorldGenForGrass(Random random)
    {
        return random.nextInt(5) > 0 ? new WorldGenTallGrass(Blocks.tallgrass, 2) : new WorldGenTallGrass(Blocks.tallgrass, 1);
    }
}