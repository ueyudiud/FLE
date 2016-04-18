package fle.core.world.biome;

import java.util.Random;

import farcore.interfaces.ITreeGenerator;
import farcore.lib.substance.SubstanceWood;
import farcore.lib.world.biome.BiomeBase;
import farcore.lib.world.gen.tree.TreeGenJungle;
import farcore.lib.world.gen.tree.TreeGenShrub;
import farcore.lib.world.gen.tree.TreeGenSimple;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMelon;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenVines;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeJungle extends BiomeBase
{
	private static boolean init = false;
	
	protected static final ITreeGenerator genOak1 = new TreeGenSimple(5, false);
	protected static final ITreeGenerator genOak2 = new TreeGenShrub();
	protected static final ITreeGenerator genCeiba1 = new TreeGenJungle(40, 18);
	protected static final ITreeGenerator genCeiba2 = new TreeGenJungle(55, 21);
	protected static final ITreeGenerator genCeiba3 = new TreeGenSimple(7, true);

	private void init()
	{
		if(!init)
		{
			SubstanceWood wood = SubstanceWood.getSubstance("oak");
			genOak1.initLogBlock(wood.log, wood.leaves);
			genOak2.initLogBlock(wood.log, wood.leaves);
			wood = SubstanceWood.getSubstance("ceiba");
			genCeiba1.initLogBlock(wood.log, wood.leaves);
			genCeiba2.initLogBlock(wood.log, wood.leaves);
			genCeiba3.initLogBlock(wood.log, wood.leaves);
			init = true;
		}
	}
	
    private boolean type;
	
	public BiomeJungle(int id, boolean flag)
	{
		super(id);
		init();
        this.type = flag;

        if (flag)
        {
            this.biomeDecorator.treesPerChunk = 2;
        }
        else
        {
            this.biomeDecorator.treesPerChunk = 50;
        }

        this.biomeDecorator.grassPerChunk = 25;
        this.biomeDecorator.flowersPerChunk = 4;

        if (!flag)
        {
            this.spawnableMonsterList.add(new SpawnListEntry(EntityOcelot.class, 2, 1, 1));
        }

        this.spawnableCreatureList.add(new SpawnListEntry(EntityChicken.class, 10, 4, 4));
	}
	
	@Override
	protected ITreeGenerator getTreeGenerator(World world, Random rand, int x, int z, double treeNoise)
	{
		if(type)
		{
			return rand.nextInt(4) == 0 ? (treeNoise > 0.6F ? genCeiba2 : genCeiba1) :
				rand.nextBoolean() ? genCeiba3 : rand.nextInt(4) == 0 ? genOak1 : genOak2;
		}
		else
		{
			return rand.nextInt(4) == 0 ? genOak1 : genCeiba3;
		}
	}

//    public WorldGenAbstractTree func_150567_a(Random random)
//    {
//        return (WorldGenAbstractTree)(random.nextInt(10) == 0 ? this.worldGeneratorBigTree : (random.nextInt(2) == 0 ? new WorldGenShrub(3, 0) : (!this.type && random.nextInt(3) == 0 ? new WorldGenMegaJungle(false, 10, 20, 3, 3) : new WorldGenTrees(false, 4 + random.nextInt(7), 3, 3, true))));
//    }

    /**
     * Gets a WorldGen appropriate for this biome.
     */
    public WorldGenerator getRandomWorldGenForGrass(Random random)
    {
        return new WorldGenTallGrass(Blocks.tallgrass, random.nextInt(4) == 0 ? 2 : 1);
    }

    public void decorate(World world, Random random, int x, int z)
    {
        super.decorate(world, random, x, z);
        int k = x + random.nextInt(16) + 8;
        int l = z + random.nextInt(16) + 8;
        int height = world.getHeightValue(k, l) * 2; //This was the original input for the nextInt below.  But it could == 0, which crashes nextInt
        if (height < 1) height = 1;
        int i1 = random.nextInt(height);
        (new WorldGenMelon()).generate(world, random, k, i1, l);
        WorldGenVines worldgenvines = new WorldGenVines();

        for (l = 0; l < 50; ++l)
        {
            i1 = x + random.nextInt(16) + 8;
            short short1 = 128;
            int j1 = z + random.nextInt(16) + 8;
            worldgenvines.generate(world, random, i1, short1, j1);
        }
    }
}