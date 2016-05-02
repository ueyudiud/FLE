package fle.core.world.biome;

import java.util.Random;

import farcore.interfaces.ITreeGenerator;
import farcore.lib.substance.SubstanceWood;
import farcore.lib.world.biome.BiomeBase;
import farcore.lib.world.gen.tree.TreeGenPine;
import farcore.lib.world.gen.tree.TreeGenPine2;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeMountain extends BiomeBase
{
	private static final WorldGenerator theWorldGenerator = new WorldGenMinable(Blocks.monster_egg, 8);
	private static final ITreeGenerator genSpruce1 = new TreeGenPine();
	private static final ITreeGenerator genSpruce2 = new TreeGenPine2();
    
	private static boolean init = false;

	private void init()
	{
		if(!init)
		{
			SubstanceWood wood = SubstanceWood.getSubstance("spruce");
			genSpruce1.initLogBlock(wood.log, wood.leaves);
			genSpruce2.initLogBlock(wood.log, wood.leaves);
			init = true;
		}
	}
	
//	private WorldGenTaiga2 taigaTrees;
    private int field_150635_aE;
    private int field_150636_aF;
    private int field_150637_aG;
    private int field_150638_aH;
    
	public BiomeMountain(int id, boolean enableTree, boolean enableGrass)
	{
		super(id);
		init();
//        this.taigaTrees = new WorldGenTaiga2(false);
        this.field_150635_aE = 0;
        this.field_150636_aF = 1;
        this.field_150637_aG = 2;
        this.field_150638_aH = this.field_150635_aE;

        if(!enableTree)
        {
        	biomeDecorator.treesPerChunk = -999;
        }
        else
        {
            biomeDecorator.treesPerChunk = 3;
            this.field_150638_aH = this.field_150636_aF;
        }
        if(!enableGrass)
        {
        	biomeDecorator.grassPerChunk = -999;
        }
        else
        {
            biomeDecorator.ivyPerChunk = 2;
        }
	}
	
	@Override
	protected ITreeGenerator getTreeGenerator(World world, Random rand, int x, int z, double treeNoise)
	{
		return rand.nextInt(3) > 0 ? genSpruce2 : genSpruce1;
	}

//    public WorldGenAbstractTree func_150567_a(Random random)
//    {
//        return (WorldGenAbstractTree)(random.nextInt(3) > 0 ? this.taigaTrees : super.func_150567_a(random));
//    }

    public void decorate(World world, Random random, int x, int z)
    {
        super.decorate(world, random, x, z);
        int k = 3 + random.nextInt(6);
        int l;
        int i1;
        int j1;

//        for (l = 0; l < k; ++l)
//        {
//            i1 = x + random.nextInt(16);
//            j1 = random.nextInt(28) + 4;
//            int k1 = z + random.nextInt(16);
//
//            if (world.getBlock(i1, j1, k1).isReplaceableOreGen(world, i1, j1, k1, Blocks.stone))
//            {
//                world.setBlock(i1, j1, k1, Blocks.emerald_ore, 0, 2);
//            }
//        }

        for (k = 0; k < 7; ++k)
        {
            l = x + random.nextInt(16);
            i1 = random.nextInt(64);
            j1 = z + random.nextInt(16);
            theWorldGenerator.generate(world, random, l, i1, j1);
        }
    }

    public void genTerrainBlocks(World world, Random random, Block[] blocks, byte[] metas, int x, int z, double layer)
    {
        topBlock = Blocks.grass;
        topMeta = 0;
        secondBlock = Blocks.dirt;
        fillerBlock = Blocks.gravel;

        if ((layer < -1.0D || layer > 2.0D) && this.field_150638_aH == this.field_150637_aG)
        {
            topBlock = Blocks.gravel;
            secondBlock = Blocks.gravel;
            fillerBlock = Blocks.gravel;
        }
        else if (layer > 1.0D && this.field_150638_aH != this.field_150636_aF)
        {
            topBlock = Blocks.stone;
            secondBlock = Blocks.stone;
            fillerBlock = Blocks.stone;
        }

        super.genTerrainBlocks(world, random, blocks, metas, x, z, layer);
    }
}