package fle.core.world.biome;

import java.util.Random;

import farcore.interfaces.ITreeGenerator;
import farcore.lib.substance.SubstanceWood;
import farcore.lib.world.biome.BiomeBase;
import farcore.lib.world.gen.tree.TreeGenPine;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeTaiga extends BiomeBase
{
//    private static final WorldGenTaiga1 field_150639_aC = new WorldGenTaiga1();
//    private static final WorldGenTaiga2 field_150640_aD = new WorldGenTaiga2(false);
    private static final WorldGenMegaPineTree field_150641_aE = new WorldGenMegaPineTree(false, false);
    private static final WorldGenMegaPineTree field_150642_aF = new WorldGenMegaPineTree(false, true);
    private static final WorldGenBlockBlob field_150643_aG = new WorldGenBlockBlob(Blocks.mossy_cobblestone, 0);

	private static boolean init = false;

    private static final ITreeGenerator genSpruce1 = new TreeGenPine();
	
	private void init()
	{
		if(!init)
		{
			SubstanceWood wood = SubstanceWood.getSubstance("spruce");
			genSpruce1.initLogBlock(wood.log, wood.leaves);
			init = true;
		}
	}
	
	private int type;
    
    public BiomeTaiga(int id, int type)
    {
        super(id);
        init();
        this.type = type;
        this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityWolf.class, 8, 4, 4));
        this.biomeDecorator.treesPerChunk = 10;

        if (type != 1 && type != 2)
        {
            this.biomeDecorator.grassPerChunk = 1;
            this.biomeDecorator.brownMushroomsPerChunk = 1;
        }
        else
        {
            this.biomeDecorator.grassPerChunk = 7;
            this.biomeDecorator.deadBushPerChunk = 1;
            this.biomeDecorator.brownMushroomsPerChunk = 3;
        }
    }
    
    @Override
    protected ITreeGenerator getTreeGenerator(World world, Random rand, int x, int z, double treeNoise)
    {
    	return genSpruce1;
    }

//    public WorldGenAbstractTree func_150567_a(Random p_150567_1_)
//    {
//        return (WorldGenAbstractTree)((this.type == 1 || this.type == 2) && p_150567_1_.nextInt(3) == 0 ? (this.type != 2 && p_150567_1_.nextInt(13) != 0 ? field_150641_aE : field_150642_aF) : (p_150567_1_.nextInt(3) == 0 ? field_150639_aC : field_150640_aD));
//    }

    /**
     * Gets a WorldGen appropriate for this biome.
     */
    public WorldGenerator getRandomWorldGenForGrass(Random random)
    {
        return random.nextInt(5) > 0 ? new WorldGenTallGrass(Blocks.tallgrass, 2) : new WorldGenTallGrass(Blocks.tallgrass, 1);
    }

    public void decorate(World world, Random random, int x, int z)
    {
        int k;
        int l;
        int i1;
        int j1;

        if (this.type == 1 || this.type == 2)
        {
            k = random.nextInt(3);

            for (l = 0; l < k; ++l)
            {
                i1 = x + random.nextInt(16) + 8;
                j1 = z + random.nextInt(16) + 8;
                int k1 = world.getHeightValue(i1, j1);
                field_150643_aG.generate(world, random, i1, k1, j1);
            }
        }

        genTallFlowers.func_150548_a(3);

        for (k = 0; k < 7; ++k)
        {
            l = x + random.nextInt(16) + 8;
            i1 = z + random.nextInt(16) + 8;
            j1 = random.nextInt(world.getHeightValue(l, i1) + 32);
            genTallFlowers.generate(world, random, l, j1, i1);
        }

        super.decorate(world, random, x, z);
    }

    public void genTerrainBlocks(World world, Random random, Block[] blocks, byte[] metas, int x, int z, double layer)
    {
        if (this.type == 1 || this.type == 2)
        {
            this.topBlock = Blocks.grass;
            this.field_150604_aj = 0;
            this.fillerBlock = Blocks.dirt;

            if (layer > 1.75D)
            {
                this.topBlock = Blocks.dirt;
                this.field_150604_aj = 1;
            }
            else if (layer > -0.95D)
            {
                this.topBlock = Blocks.dirt;
                this.field_150604_aj = 2;
            }
        }

        this.genBiomeTerrain(world, random, blocks, metas, x, z, layer);
    }
}